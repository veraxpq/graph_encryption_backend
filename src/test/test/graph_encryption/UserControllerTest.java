package graph_encryption;

import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;
import graph_encryption.controller.UserController;
import graph_encryption.model.UserLoginInfo;
import graph_encryption.service.UserService;
import graph_encryption.util.JwtUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This class is a unit test class to test all methods in UserController
 */
@RunWith(SpringRunner.class)
public class UserControllerTest {
    private UserController userController;
    @MockBean
    private UserService userService;

    private JSONObject userInfo;
    private JSONObject loginJB;

    @Before
    public void setUp() {
        userController = new UserController(userService);

        //initialize a userInfo json object
        userInfo = new JSONObject();
        userInfo.put("email", "hi@gmail.com");
        userInfo.put("password", "asdlkfjasd");

        //initialize a login return json object
        loginJB = new JSONObject();
        loginJB.put("email", "hi@gmail.com");
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setEmail("hi@gmail.com");
        userLoginInfo.setPassword("asdlkfjasd");
        loginJB.put("token", JwtUtils.createToken(userLoginInfo));
        loginJB.put("id", 144);
    }

    /**
     * test login() when the given email and password are correct
     */
    @Test
    public void testLogin() {
        Result<JSONObject> expected = new Result(loginJB, 1);
        Mockito.when(userService.login(userInfo)).thenReturn(expected);
        final Result result = userController.login(userInfo);
        Assert.assertEquals(expected, result);
    }

    /**
     * test login() when the given email is wrong
     */
    @Test
    public void testLoginWithWrongEmail() {
        JSONObject userInfoWithWrongEmail = new JSONObject();
        userInfo.put("email", "hii@gmail.com");
        userInfo.put("password", "asdlkfjasd");
        JSONObject loginJB = new JSONObject();
        loginJB.put("data", "The password is not correct.");
        Result<JSONObject> expected = new Result(loginJB, 0);
        Mockito.when(userController.login(userInfoWithWrongEmail)).thenReturn(expected);
        final Result result = userController.login(userInfoWithWrongEmail);
        Assert.assertEquals(expected, result);
    }

    /**
     * test login() when the given password is wrong
     */
    @Test
    public void testLoginWithWrongPassword() {
        JSONObject userInfoWithWrongEmail = new JSONObject();
        userInfo.put("email", "hi@gmail.com");
        userInfo.put("password", "asdlkfjasdd");
        JSONObject loginJB = new JSONObject();
        loginJB.put("data", "The password is not correct.");
        Result<JSONObject> expected = new Result(loginJB, 0);
        Mockito.when(userController.login(userInfoWithWrongEmail)).thenReturn(expected);
        final Result result = userController.login(userInfoWithWrongEmail);
        Assert.assertEquals(expected, result);
    }

    /**
     * test getUserInfo() method with userId
     */
    @Test
    public void testGetUserInfo() {
        //initialize a userInfo json object
        JSONObject returnedUserInfo = new JSONObject();
        returnedUserInfo.put("id", 144);
        returnedUserInfo.put("phone", "3123123122");
        returnedUserInfo.put("password", null);
        Result<JSONObject> expected = new Result(returnedUserInfo, 1);
        Mockito.when(userService.getUserInfo(144)).thenReturn(expected);
        final Result result = userController.getUserInfo(144);
        Assert.assertEquals(expected, result);
    }

    /**
     * test updateUserInfo() method with userId and phone
     */
    @Test
    public void testUpdateUserInfoWithUpdatedPhone() {
        //initialize a userInfo json object
        JSONObject userInfoUpdated = new JSONObject();
        userInfoUpdated.put("id", 144);
        userInfoUpdated.put("phone", "3123123122");
        Result<JSONObject> expected = new Result("", 1);
        Mockito.when(userService.updateUserInfo(userInfoUpdated)).thenReturn(expected);
        final Result result = userController.updateUserInfo(userInfoUpdated);
        Assert.assertEquals(expected, result);
    }

    /**
     * test updateUserInfo() method with userId and email
     */
    @Test
    public void testUpdateUserInfoWithUpdatedEmail() {
        //initialize a userInfo json object
        JSONObject userInfoUpdated = new JSONObject();
        userInfoUpdated.put("id", 144);
        userInfoUpdated.put("email", "new@gmail.com");
        Result<JSONObject> expected = new Result("", 1);
        Mockito.when(userService.updateUserInfo(userInfoUpdated)).thenReturn(expected);
        final Result result = userController.updateUserInfo(userInfoUpdated);
        Assert.assertEquals(expected, result);
    }

    /**
     * test createUser() method
     */
    @Test
    public void testCreateUser() {
        Result<JSONObject> expected = new Result("", 1);
        Mockito.when(userService.createUser(userInfo)).thenReturn(expected);
        final Result result = userController.createUser(userInfo);
        Assert.assertEquals(expected, result);
    }

    /**
     * test createUser() method when email exists in db
     */
    @Test
    public void testCreateUserWithDuplicateEmail() {
        Result<JSONObject> expected = new Result("Email has been registered.", 1);
        Mockito.when(userService.createUser(userInfo)).thenReturn(expected);
        final Result result = userController.createUser(userInfo);
        Assert.assertEquals(expected, result);
    }

}
