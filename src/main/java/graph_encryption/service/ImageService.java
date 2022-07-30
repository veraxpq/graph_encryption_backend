package graph_encryption.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;


/**
 * This interface represents the services related to image.
 */
public interface ImageService {

    /**
     * This method save the image info into the database.
     *
     * @param image input image
     */
    JSONObject save(JSONObject image);

    /**
     * This method get the info of the image from database.
     *
     * @param userId given userId
     * @return the image retrieved from database
     */
    JSONArray getImagesByUserId(int userId);

    Result<JSONObject> encrypt(JSONObject image);

    Result<JSONObject> decrypt(JSONObject imageInfo);
}
