package co.aca.model.response;

import co.aca.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {

    private String isSuccessful;
    private User user;
    private List<User> userList;
    private String errorMessage;
    private String successMessage;

    public ResponseUser(User user) {
        setIsSuccessful("1");
        setUser(user);
    }
    public ResponseUser(List<User> userList) {
        setIsSuccessful("1");
        setUserList(userList);
    }
    public ResponseUser(String message, String isSuccess) {
        if(isSuccess.equals("1")){
            setSuccessMessage(message);
        }else{
            setErrorMessage(message);
        }
        setIsSuccessful(isSuccess);
    }

    public User getUser() {
        return user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(String isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
