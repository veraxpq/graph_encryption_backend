package graph_encryption;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;

import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains integration tests for ImageController class.
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = MlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(locations = "file:src/main/resources/mapper/ImageInfoMapper.xml")
public class ImageControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private String token;

    private static String encryptedUrl;

    /**
     * This method initializes the variables needed for the following tests.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Before
    public void setUp() {
        headers.set("content-type", "application/json");

        JSONObject obj = new JSONObject();
        obj.put("email", "hi@gmail.com");
        obj.put("password", "asdlkfjasd");

        HttpEntity<String> entity = new HttpEntity<>(obj.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.
                exchange(
                        createURLWithPort("/en-graph/login"),
                        HttpMethod.POST, entity, String.class);

        JSONObject resJsonObj = (JSONObject) JSON.parse(response.getBody());

        this.token = ((JSONObject) (resJsonObj.get("data"))).getString("token");
        headers.set("Authorization", token);
    }

    /**
     * This method tests getImageList() method.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test1_GetImageList() throws org.json.JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("en-graph/getImageList?userId=5"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\n" +
                "    \"data\": [],\n" +
                "    \"status\": 1\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * This method tests getImageList() method when the userId is illegal.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test2_GetImageListWithIllegalUserId() throws org.json.JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("en-graph/getImageList?userId=-1"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\n" +
                "    \"data\": [],\n" +
                "    \"status\": 1\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * This method tests encryptInput() method.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test3_EncryptInput() {
        JSONObject obj = new JSONObject();
        obj.put("userId", 101);
        obj.put("password", "1111");
        obj.put("message", "hello");
        obj.put("originalImageUrl", "https://i.imgur.com/cM7KqhE.png");

        HttpEntity<JSONObject> entity = new HttpEntity<>(obj, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("en-graph/encrypt"),
                HttpMethod.POST, entity, String.class);

        Pattern pattern = Pattern.compile("https://i.imgur.com/(.*).png");
        JSONObject resJson = (JSONObject) JSON.parse(response.getBody());
        this.encryptedUrl = ((JSONObject) (resJson.get("data"))).getString("encrypted_image_url");
        Matcher matcher = pattern.matcher(encryptedUrl);
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(resJson.get("status"), 1);
    }

    /**
     * This method tests encryptInput() method.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test4_DecryptInput() throws org.json.JSONException {
        JSONObject obj = new JSONObject();
        obj.put("password", "1111");
        obj.put("url", this.encryptedUrl);

        HttpEntity<JSONObject> entity = new HttpEntity<>(obj, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("en-graph/decrypt"),
                HttpMethod.POST, entity, String.class);

        String expected = "{\n" +
                "    \"data\": {\n" +
                "        \"message\": \"hello\"\n" +
                "    },\n" +
                "    \"status\": 1\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * This method tests encryptInput() method when the password is wrong.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test5_DecryptInputWithWrongPassword() throws org.json.JSONException {
        JSONObject obj = new JSONObject();
        obj.put("password", "11112");
        obj.put("url", this.encryptedUrl);

        HttpEntity<JSONObject> entity = new HttpEntity<>(obj, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("en-graph/decrypt"),
                HttpMethod.POST, entity, String.class);

        String expected = "{\n" +
                "    \"data\": {\n" +
                "        \"error\": \"The password is wrong.\"\n" +
                "    },\n" +
                "    \"status\": 0\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}