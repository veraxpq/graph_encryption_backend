package graph_encryption.common;

public class Result<T> {
    T data;
    int status;

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
