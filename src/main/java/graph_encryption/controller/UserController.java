package graph_encryption.controller;

import com.alibaba.fastjson.JSONArray;
import graph_encryption.common.Result;
import graph_encryption.service.UserService;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.wrapper.VerifyToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@RestController
@RequestMapping("/en-graph")
@Service
public class UserController {

    @Autowired
    private UserService userService;

    @VerifyToken
    @GetMapping(value="/user")
    public Result<JSONObject> getUserInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") int id) {
        JSONObject user = userService.getUserInfo(id);
        return new Result<>(user, 1);
    }

    @VerifyToken
    @PutMapping(value = "/user")
    public Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject user) {
        userService.updateUserInfo(user);
        return new Result("", 1);
    }

    @PostMapping(value = "/user")
    public Result createUser(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {
        userService.createUser(obj);
        return new Result("", 1);
    }

    @VerifyToken
    @DeleteMapping(value = "/user")
    public Result deleteUser(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("id") int id) {
        userService.deleteUser(id);
        return new Result("", 1);
    }

    @PostMapping(value = "/login")
    public Result<JSONObject> login(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {
        return userService.login(obj);
    }
}