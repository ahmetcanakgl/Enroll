package co.aca.model.response;

import co.aca.model.Course;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCourse {

    private String isSuccessful;
    private Course course;
    private List<Course> courseList;
    private String errorMessage;
    private String successMessage;

    public ResponseCourse(Course course) {
        setIsSuccessful("1");
        setCourse(course);
    }
    public ResponseCourse(List<Course> courseList) {
        setIsSuccessful("1");
        setCourseList(courseList);
    }
    public ResponseCourse(String message, String isSuccess) {
        if(isSuccess.equals("1")){
            setSuccessMessage(message);
        }else{
            setErrorMessage(message);
        }
        setIsSuccessful(isSuccess);
    }

    public Course getCourse() {
        return course;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public void setCourse(Course course) {
        this.course = course;
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
