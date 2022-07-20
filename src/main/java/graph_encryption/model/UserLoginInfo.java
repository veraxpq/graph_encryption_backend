package graph_encryption.model;

/**
 * This class represents the info related to user login process.
 */
public class UserLoginInfo {
    private String email;
    private String password;

    /**
     * Set email to the object with the given email
     *
     * @param email given email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get email of the object.
     *
     * @return email of the object
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get password of the object.
     *
     * @return password of the object
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password of the object.
     *
     * @param password given password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
