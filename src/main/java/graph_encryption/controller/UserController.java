package graph_encryption.controller;

import com.alibaba.fastjson.JSONArray;
import graph_encryption.common.Result;
import graph_encryption.service.UserService;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.wrapper.VerifyToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * This class represents the controller of the operations related to users.
 */
@RestController
@RequestMapping("/en-graph")
@Service
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * This method get user info with the given user id.
     *
     * @param request given servlet request
     * @param response given servlet response
     * @param id given user id
     * @return user info
     */
    @VerifyToken
    @GetMapping(value="/user")
    public Result<JSONObject> getUserInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") int id) {
        JSONObject user = userService.getUserInfo(id);
        return new Result<>(user, 1);
    }

    /**
     * This method update user info with the given user info.
     *
     * @param request given servlet request
     * @param response given servlet response
     * @param user given user info
     * @return if the update is successful
     */
    @VerifyToken
    @PutMapping(value = "/user")
    public Result updateUserInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject user) {
        userService.updateUserInfo(user);
        return new Result("", 1);
    }

    /**
     * This method creates a user with the given user info.
     *
     * @param request given servlet request
     * @param response given servlet response
     * @param user given user info
     * @return if the operation is successful
     */
    @PostMapping(value = "/user")
    public Result createUser(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject user) {
        userService.createUser(user);
        return new Result("", 1);
    }

    /**
     * This method deletes user with the given user id.
     *
     * @param request given servlet request
     * @param response given servlet response
     * @param id given user id
     * @return if the operation succeeds
     */
    @VerifyToken
    @DeleteMapping(value = "/user")
    public Result deleteUser(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("id") int id) {
        userService.deleteUser(id);
        return new Result("", 1);
    }

    /**
     * This method login user with the given user info.
     *
     * @param request given servlet request
     * @param response given servlet response
     * @param user given user info
     * @return if the operation succeeds
     */
    @PostMapping(value = "/login")
    public Result<JSONObject> login(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject user) {
        return userService.login(user);
    }

    @GetMapping("/encrypt")
    public void encryptInput(MultipartFile imageFile, String message){
        System.out.println("Encrypted successfully");

    }

    @GetMapping("/decrypt")
    public void decryptInput(){
        System.out.println("Decrypted successfully");
    }

}