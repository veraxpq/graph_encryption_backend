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
@CrossOrigin
@RequestMapping("/en-graph")
@Service
public class ImageController {

    @Autowired
    private ImageService imageService;

    @VerifyToken
    @GetMapping(value = "/getImageList")
    public Result<JSONArray> getUserInfo(@RequestParam("userId") int userId) {
        return new Result<>(imageService.getImagesByUserId(userId), 1);
    }

    @PostMapping("/encrypt")
    public Result<JSONObject> encryptInput(@RequestBody JSONObject imageInfo) {
        return imageService.encrypt(imageInfo);
    }

    @PostMapping("/decrypt")
    public Result<JSONObject> decryptInput(@RequestBody JSONObject imageInfo) {
        return imageService.decrypt(imageInfo);
    }

}
