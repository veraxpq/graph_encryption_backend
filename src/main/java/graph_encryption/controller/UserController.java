package graph_encryption.controller;

import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;
import graph_encryption.service.UserService;
import graph_encryption.wrapper.VerifyToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * This class represents the controller of the operations related to users.
 */
@RestController
@CrossOrigin
@RequestMapping("/en-graph")
@Service
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * This method get user info with the given user id.
     *
     * @param id given user id
     * @return user info
     */
    @VerifyToken
    @GetMapping(value = "/user")
    public Result<JSONObject> getUserInfo(@RequestParam("id") int id) {
        return userService.getUserInfo(id);
    }

    /**
     * This method update user info with the given user info.
     *
     * @param user given user info
     * @return if the update is successful
     */
    @VerifyToken
    @PutMapping(value = "/user")
    public Result updateUserInfo(@RequestBody JSONObject user) {
        return userService.updateUserInfo(user);
    }

    /**
     * This method creates a user with the given user info.
     *
     * @param user given user info
     * @return if the operation is successful
     */
    @PostMapping(value = "/user")
    public Result createUser(@RequestBody JSONObject user) {
        return userService.createUser(user);
    }

    /**
     * This method login user with the given user info.
     *
     * @param user given user info
     * @return if the operation succeeds
     */
    @PostMapping(value = "/login")
    public Result<JSONObject> login(@RequestBody JSONObject user) {
        return userService.login(user);
    }

}