package graph_encryption.wrapper;

public class ErrorMessage {
    private Integer status;
    private String data;

    public ErrorMessage() {}

    public ErrorMessage(String data) {
        this.data = data;
    }

    public ErrorMessage(Integer status, String data) {
        this.status = status;
        this.data = data;
    }

    public Integer getCode() {
        return status;
    }

    public void setCode(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return data;
    }

    public void setMessage(String data) {
        this.data = data;
    }
}
