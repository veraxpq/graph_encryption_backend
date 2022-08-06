package graph_encryption;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;
import graph_encryption.controller.ImageController;
import graph_encryption.model.UserLoginInfo;
import graph_encryption.service.ImageService;
import graph_encryption.util.JwtUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * This class is a unit test class to test all methods in ImageController
 */
@RunWith(SpringRunner.class)
public class ImageControllerTest {

    private ImageController imageController;
    @MockBean
    private ImageService imageService;

    private JSONObject userInfo;
    private JSONObject loginJB;

    @Before
    public void setUp() {
        imageController = new ImageController(imageService);

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
     * test getImageList() with given userId
     */
    @Test
    public void testGetImageList() {
        JSONArray jsonArray = new JSONArray();
        JSONObject image = new JSONObject();
        image.put("date", new Date());
        image.put("originalImageUrl", "https://image");
        image.put("encryptedImageUrl", "https://encrypted");
        image.put("imageId", 1);
        image.put("userId", 144);
        jsonArray.add(image);
        Result<JSONArray> expected = new Result(jsonArray, 1);
        Mockito.when(imageService.getImagesByUserId(144)).thenReturn(expected);
        final Result result = imageController.getImageList(144);
        Assert.assertEquals(expected, result);
    }

    /**
     * test encryptInput()
     */
    @Test
    public void testEncryptInput() {
        JSONObject image = new JSONObject();
        image.put("message", "hello");
        image.put("password", "1111");
        image.put("originalImageUrl", "https://encrypted");
        image.put("userId", 144);
        Result<JSONObject> expected = new Result(image, 1);
        Mockito.when(imageService.encrypt(image)).thenReturn(expected);
        final Result result = imageController.encryptInput(image);
        Assert.assertEquals(expected, result);
    }

    /**
     * test decryptInput()
     */
    @Test
    public void testDecryptInput() {
        JSONObject image = new JSONObject();
        image.put("password", "1111");
        image.put("url", "https://encrypted");

        JSONObject encryptReturn = new JSONObject();
        encryptReturn.put("message", "hello");
        Result<JSONObject> expected = new Result(encryptReturn, 1);
        Mockito.when(imageService.decrypt(image)).thenReturn(expected);
        final Result result = imageController.decryptInput(image);
        Assert.assertEquals(expected, result);
    }

    /**
     * test decryptInput() when the given image does not contain a message
     */
    @Test
    public void testDecryptInputWithEmptyImage() {
        JSONObject image = new JSONObject();
        image.put("password", "1111");
        image.put("url", "https://encrypted");

        JSONObject encryptReturn = new JSONObject();
        encryptReturn.put("message", "");
        Result<JSONObject> expected = new Result(encryptReturn, 1);
        Mockito.when(imageService.decrypt(image)).thenReturn(expected);
        final Result result = imageController.decryptInput(image);
        Assert.assertEquals(expected, result);
    }

    /**
     * test decryptInput() when the password is wrong
     */
    @Test
    public void testDecryptInputWithWrongPassword() {
        JSONObject image = new JSONObject();
        image.put("password", "1111");
        image.put("url", "https://encrypted");

        JSONObject encryptReturn = new JSONObject();
        encryptReturn.put("error", "The password is wrong.");
        Result<JSONObject> expected = new Result(encryptReturn, 1);
        Mockito.when(imageService.decrypt(image)).thenReturn(expected);
        final Result result = imageController.decryptInput(image);
        Assert.assertEquals(expected, result);
    }

}
