package graph_encryption.service;

import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;

/**
 * This interface represents the service can be called related to user
 */
public interface UserService {

    /**
     * This method verifies the given user info, to login the user.
     *
     * @param user given user info, containing email and password
     * @return if the user info is valid, if it is, return the generated token, otherwise, return error
     */
    Result login(JSONObject user);

    /**
     * This method gets user info with given id.
     *
     * @param id given user id
     * @return user info
     */
    JSONObject getUserInfo(int id);

    /**
     * This method updates user info with the given user info json.
     *
     * @param user given user info
     */
    void updateUserInfo(JSONObject user);

    /**
     * This method delete user with given user id.
     *
     * @param id given user id
     */
    void deleteUser(int id);

    /**
     * This method creates a new user with the given user info.
     *
     * @param user given user info
     */
    void createUser(JSONObject user);

}
