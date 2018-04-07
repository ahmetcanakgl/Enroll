package co.aca.model;

public class GeneralResponse<T> {

    public GeneralResponse(int isSuccessful, T object) {
        setObject(object);
        setIsSuccessful(isSuccessful);
    }

    public GeneralResponse(int isSuccessful, String errorMessage) {
        setIsSuccessful(isSuccessful);
        setErrorMessage(errorMessage);
    }

    public GeneralResponse(int isSuccessful) {
        setIsSuccessful(isSuccessful);
    }

    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    private int isSuccessful = 1;
    private String errorMessage;

    public int getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(int isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
