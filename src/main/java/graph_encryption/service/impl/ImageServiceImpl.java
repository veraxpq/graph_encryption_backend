package graph_encryption.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;
import graph_encryption.domain.client.ImageInfoMapper;
import graph_encryption.domain.model.ImageInfo;
import graph_encryption.domain.model.ImageInfoExample;
import graph_encryption.service.ImageService;
import graph_encryption.util.DecryptLSB;
import graph_encryption.util.EncryptLSB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageInfoMapper imageInfoMapper;

    @Override
    public JSONObject save(JSONObject image) {
        int userId = image.getInteger("userId");
        String originalImageUrl = image.getString("originalImageUrl");
        String password = image.getString("password");
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setUserId(userId);
        imageInfo.setOriginalImageUrl(originalImageUrl);
        imageInfoMapper.insert(imageInfo);
        return (JSONObject) JSONObject.toJSON(imageInfo);
    }

    @Override
    public JSONArray getImagesByUserId(int userId) {
        ImageInfoExample example = new ImageInfoExample();
        ImageInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<ImageInfo> imageInfos = imageInfoMapper.selectByExample(example);
        return (JSONArray) JSONArray.toJSON(imageInfos);
    }

    @Override
    public Result<JSONObject> encrypt(JSONObject image) {
        // save image into db
        int userId = image.getInteger("userId");
        String originalImageUrl = image.getString("originalImageUrl");
        String password = image.getString("password");
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setUserId(userId);
        imageInfo.setOriginalImageUrl(originalImageUrl);
        imageInfoMapper.insert(imageInfo);

        //encrypt image
        String message = image.getString("message");
        String newMessage = password.length() + password + message;
        String encryptedImageUrl = EncryptLSB.Encrypt(originalImageUrl, newMessage);

        //update imageInfo in db
        ImageInfoExample example = new ImageInfoExample();
        ImageInfoExample.Criteria criteria = example.createCriteria();
        criteria.andImageIdEqualTo(imageInfo.getImageId());
        imageInfo.setEncryptedImageUrl(encryptedImageUrl);
        imageInfoMapper.updateByExampleSelective(imageInfo, example);

        JSONObject obj = new JSONObject();
        obj.put("encrypted_image_url", encryptedImageUrl);
        return new Result<>(obj, 1);
    }

    @Override
    public Result<JSONObject> decrypt(JSONObject imageInfo) {
        String url = imageInfo.getString("url");
        String password = imageInfo.getString("password");

        //decrypt the image
        String message = DecryptLSB.Decrypt(url);
        if (!message.isEmpty()) {
            int len = message.charAt(0);
            if (len < message.length() - 1) {
                if (password.equals(message.substring(1, len + 1))) {
                    JSONObject obj = new JSONObject();
                    obj.put("message", message.substring(len));
                    return new Result<>(obj, 1);
                }
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("error", "Decrypt Failed");
        return new Result<>(obj, 0);
    }
}
