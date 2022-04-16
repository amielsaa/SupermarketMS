package Utilities;

public class Response<T>
{
    private boolean status;
    private String message;
    private T data;

    public static <U> Response<U> makeSuccess(U _data) {
        return new Response<U>(_data, true, "");
    }

    public static <U> Response<U> makeFailure(String _message) {
        return new Response<U>(null, false, _message);
    }

    private Response(T _data, boolean _status, String _message) {
        this.data = _data;
        this.status = _status;
        this.message = _message;
    }

    public boolean isSuccess() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}