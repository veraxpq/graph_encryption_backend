package graph_encryption.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.domain.client.ImageInfoMapper;
import graph_encryption.domain.model.ImageInfo;
import graph_encryption.domain.model.ImageInfoExample;
import graph_encryption.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageInfoMapper imageInfoMapper;

    @Override
    public void save(JSONObject image) {
        ImageInfo imageInfo = image.toJavaObject(ImageInfo.class);
        imageInfoMapper.insert(imageInfo);
    }

    @Override
    public JSONArray getImagesByUserId(int userId) {
        ImageInfoExample example = new ImageInfoExample();
        ImageInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<ImageInfo> imageInfos = imageInfoMapper.selectByExample(example);
        return (JSONArray) JSONArray.toJSON(imageInfos);
    }
}
