package graph_encryption.model;

/**
 * This class represents a UserText object.
 */
public class UserText {
    private String email;
    private String token;
    private int id;

    /**
     * Get email from the object
     *
     * @return email of the object
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get token of the object
     *
     * @return token of the object
     */
    public String getToken() {
        return token;
    }

    /**
     * Get id of the object
     *
     * @return id of the object
     */
    public int getId() {
        return id;
    }

    /**
     * Set id to the object.
     *
     * @param id given id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set email of the object.
     *
     * @param email given email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the token of the object.
     *
     * @param token given token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
