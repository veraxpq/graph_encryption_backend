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

/**
 * This class is the controller for imageInfo, providing apis to operate on imageInfo
 */
@RestController
@CrossOrigin
@RequestMapping("/en-graph")
@Service
public class ImageController {

    private ImageService imageService;

    /**
     * This is the constructor for ImageController
     *
     * @param imageService given imageService
     */
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * This method get an image list with the given userId.
     *
     * @param userId given userId
     * @return an image list uploaded by the given user
     */
    @VerifyToken
    @GetMapping(value = "/getImageList")
    public Result<JSONArray> getImageList(@RequestParam("userId") int userId) {
        return imageService.getImagesByUserId(userId);
    }

    /**
     * This method encrypts an image with the given image info
     *
     * @param imageInfo given image info
     * @return encrypted image info
     */
    @VerifyToken
    @PostMapping("/encrypt")
    public Result<JSONObject> encryptInput(@RequestBody JSONObject imageInfo) {
        return imageService.encrypt(imageInfo);
    }

    /**
     * This method decrypts an image.
     *
     * @param imageInfo given image info
     * @return decrypted message from the image
     */
    @VerifyToken
    @PostMapping("/decrypt")
    public Result<JSONObject> decryptInput(@RequestBody JSONObject imageInfo) {
        return imageService.decrypt(imageInfo);
    }

}
