package graph_encryption.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;

import java.text.ParseException;

public interface UserService {

    Result login(JSONObject user);
    JSONObject getUserInfo(int id);
    void updateUserInfo(JSONObject user);
    void deleteUser(int id);
    void createUser(JSONObject user);

}
