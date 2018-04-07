package co.aca.model.response;

import co.aca.model.UserCourses;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserCourses {

    private String isSuccessful;
    private UserCourses userCourses;
    private List<UserCourses> userCoursesList;
    private String errorMessage;
    private String successMessage;

    public ResponseUserCourses(UserCourses userCourses) {
        setIsSuccessful("1");
        setUserCourses(userCourses);
    }
    public ResponseUserCourses(List<UserCourses> userCoursesList) {
        setIsSuccessful("1");
        setUserCoursesList(userCoursesList);
    }
    public ResponseUserCourses(String message, String isSuccess) {
        if(isSuccess.equals("1")){
            setSuccessMessage(message);
        }else{
            setErrorMessage(message);
        }
        setIsSuccessful(isSuccess);
    }

    public String getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(String isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public UserCourses getUserCourses() {
        return userCourses;
    }

    public void setUserCourses(UserCourses userCourses) {
        this.userCourses = userCourses;
    }

    public List<UserCourses> getUserCoursesList() {
        return userCoursesList;
    }

    public void setUserCoursesList(List<UserCourses> userCoursesList) {
        this.userCoursesList = userCoursesList;
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
