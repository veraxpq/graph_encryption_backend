package graph_encryption.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;
import graph_encryption.service.ImageService;
import graph_encryption.wrapper.VerifyToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/en-graph")
@Service
public class ImageController {

    @Autowired
    private ImageService imageService;

    @VerifyToken
    @PostMapping(value = "/image")
    public Result<JSONObject> postImage(@RequestBody JSONObject image) {
        return new Result<>(imageService.save(image), 1);
    }

    @VerifyToken
    @GetMapping(value = "/getImageList")
    public Result<JSONArray> getUserInfo(@RequestParam("userId") int userId) {
        return new Result<>(imageService.getImagesByUserId(userId), 1);
    }

    @PostMapping("/encrypt")
    public Result<JSONObject> encryptInput(@RequestBody JSONObject imageInfo) {
//        System.out.println("Encrypted successfully");
        String url = imageInfo.getString("url");
        String message = imageInfo.getString("message");
        String encryptedImageUrl = imageService.encrypt(url, message);
        JSONObject obj = new JSONObject();
        obj.put("encrypted_image_url", encryptedImageUrl);
        return new Result<>(obj, 1);
    }

    @PostMapping("/decrypt")
    public Result<JSONObject> decryptInput(@RequestBody JSONObject imageInfo) {
        System.out.println("Decrypted successfully");
        String url = imageInfo.getString("url");
        String password = imageInfo.getString("password");
        int imageId = imageInfo.getInteger("imageId");
        String message = imageService.decrypt(url, password, imageId);
        JSONObject obj = new JSONObject();
        obj.put("message", message);
        return new Result<>(obj, 1);
    }

}
