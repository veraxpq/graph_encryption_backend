package graph_encryption.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import graph_encryption.common.Result;
import graph_encryption.service.ImageService;
import graph_encryption.wrapper.VerifyToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/en-graph")
@Service
public class ImageController {

    @Autowired
    private ImageService imageService;

    @VerifyToken
    @PostMapping(value="/image")
    public void postImage(@RequestBody JSONObject image) {
        imageService.save(image);
    }

    @VerifyToken
    @GetMapping(value="/getImageList")
    public JSONArray getUserInfo(@RequestParam("userId") int userId) {
        return imageService.getImagesByUserId(userId);
    }
}
