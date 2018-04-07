package co.aca.model.response;

import co.aca.model.CourseReq;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCourseReq {

    private String isSuccessful;
    private CourseReq user;
    private List<CourseReq> userList;
    private String errorMessage;
    private String successMessage;

    public ResponseCourseReq(CourseReq courseReq) {
        setIsSuccessful("1");
        setUser(courseReq);
    }

    public ResponseCourseReq(ArrayList<CourseReq> specArrayList) {
        setIsSuccessful("1");
        setUserList(specArrayList);
    }

    public ResponseCourseReq(String message, String isSuccess) {
        if (isSuccess.equals("1")) {
            setSuccessMessage(message);
        } else {
            setErrorMessage(message);
        }
        setIsSuccessful(isSuccess);
    }

    public CourseReq getUser() {
        return user;
    }

    public List<CourseReq> getUserList() {
        return userList;
    }

    public void setUserList(List<CourseReq> userList) {
        this.userList = userList;
    }

    public void setUser(CourseReq user) {
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
