package co.aca.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_course")
//@JsonDeserialize(using = UserCourseDes.class)
public class UserCourse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int userCourseId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "registered")
    @ColumnDefault("0")
    private String isRegistered;

//    public UserCourse(String userId, String courseId){
//        this.userId = Integer.valueOf(userId);
//        this.courseId = Integer.valueOf(courseId);
//    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getUserCourseId() {
        return userCourseId;
    }

    public void setUserCourseId(int userCourseId) {
        this.userCourseId = userCourseId;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }
}
