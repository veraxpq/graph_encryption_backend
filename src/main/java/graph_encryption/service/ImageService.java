package graph_encryption.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ImageService {
    void save(JSONObject image);
    JSONArray getImagesByUserId(int userId);
}
