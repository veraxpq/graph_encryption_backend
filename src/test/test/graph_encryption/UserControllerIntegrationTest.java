package graph_encryption;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
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

import java.util.Random;

/**
 * This class contains integration tests for UserController class.
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = MlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(locations = "file:src/main/resources/mapper/UserInfoMapper.xml")
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    private static HttpHeaders headers = new HttpHeaders();

    private static String token;

    /**
     * This method initializes the variables needed for the following tests and tests the login() method.*
     */
    @Before
    public void setUp() throws org.json.JSONException {
        headers.set("content-type", "application/json");
    }

    /**
     * This method tests login() method.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test1_Login() throws org.json.JSONException {
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

        String expected = "{\n" +
                "    \"data\": {\n" +
                "        \"email\": \"hi@gmail.com\",\n" +
                "        \"token\": \"" + token + "\",\n" +
                "        \"id\": 144\n" +
                "    },\n" +
                "    \"status\": 1\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * This method tests the getUserInfo() method.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test2_GetUserInfo() throws JSONException, org.json.JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/en-graph/user?id=144"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\n" +
                "    \"data\": {\n" +
                "        \"password\": null,\n" +
                "        \"phone\": \"312312312\",\n" +
                "        \"userId\": 144,\n" +
                "        \"email\": \"hi@gmail.com\"\n" +
                "    },\n" +
                "    \"status\": 1\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }


    /**
     * This method tests the getUserInfo() method with illegal userId.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test3_GetUserInfoWithIllegalUserId() throws JSONException, org.json.JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/en-graph/user?id=-1"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\n" +
                "    \"data\": {\n" +
                "        \"error\": \"Illegal userId\"\n" +
                "    },\n" +
                "    \"status\": 0\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * This method tests updateUserInfo() method.
     *
     * @throws JSONException when there is issue transforming JSON object.
     */
    @Test
    public void test4_UpdateUserInfoWithIllegalUserId() throws JSONException, org.json.JSONException {

        JSONObject obj = new JSONObject();
        obj.put("email", "hi@gmail.com");
        obj.put("phone", "312312312");
        obj.put("userId", 144);
        HttpEntity<String> entity = new HttpEntity<>(obj.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/en-graph/user"),
                HttpMethod.PUT, entity, String.class);

        String expected = "{\n" +
                "    \"data\": \"\",\n" +
                "    \"status\": 1\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * This method tests createUser() method.
     *
     * @throws JSONException
     */
    @Test
    public void test5_CreateUserInfo() throws JSONException, org.json.JSONException {

        System.out.println(this.token);
        JSONObject obj = new JSONObject();
        obj.put("email", getRandomString());
        obj.put("phone", "312312312");
        obj.put("password", "1111");
        HttpEntity<String> entity = new HttpEntity<>(obj.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/en-graph/user"),
                HttpMethod.POST, entity, String.class);

        String expected = "{\n" +
                "    \"data\": \"\",\n" +
                "    \"status\": 1\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    /**
     * This method tests createUser() method when the email already registered.
     *
     * @throws JSONException
     */
    @Test
    public void test6_CreateUserInfoWithDuplicateEmail() throws JSONException, org.json.JSONException {

        System.out.println(this.token);
        JSONObject obj = new JSONObject();
        obj.put("email", "hi@gmail.com");
        obj.put("phone", "312312312");
        obj.put("password", "1111");
        HttpEntity<String> entity = new HttpEntity<>(obj.toString(), headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/en-graph/user"),
                HttpMethod.POST, entity, String.class);

        String expected = "{\n" +
                "    \"data\": {\n" +
                "        \"error\": \"This email already registered, please login.\"\n" +
                "    },\n" +
                "    \"status\": 0\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
