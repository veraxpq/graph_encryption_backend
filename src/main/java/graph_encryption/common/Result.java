package graph_encryption.common;

/**
 * This class represents the Result object used to return to clients.
 *
 * @param <T> given type.
 */
public class Result<T> {
    T data;
    int status;

    /**
     * This is the constructor of Result class.
     *
     * @param data given data
     * @param status status of the result, 1 is success, 0 is failure
     */
    public Result(T data, int status) {
        this.data = data;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
