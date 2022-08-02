package graph_encryption.domain.model;

import java.util.Date;

/**
 * This class represents the ImageInfo table in the database.
 */
public class ImageInfo {
    private Integer imageId;

    private Date date;

    private String originalImageUrl;

    private Integer userId;

    private String password;

    private String message;

    private String encryptedImageUrl;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEncryptedImageUrl() {
        return encryptedImageUrl;
    }

    public void setEncryptedImageUrl(String encryptedImageUrl) {
        this.encryptedImageUrl = encryptedImageUrl;
    }
}