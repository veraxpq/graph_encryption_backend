package graph_encryption.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


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

    String encrypt(String imageUrl, String message);

    String decrypt(String imageUrl, String password, int imageId);
}
