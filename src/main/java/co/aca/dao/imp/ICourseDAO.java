package co.aca.dao.imp;

import co.aca.model.*;

import java.util.List;

public interface ICourseDAO {

    List<Course> getCourseList();

    List<Course> getAdminCourses(Admin admin);

    List<UserCourses> getUserCourses(User user);

    Course getCourseById(int id);

    void addCourse(Course course);

    void takeCourse(UserCourse userCourse);

    void updateCourse(Course course);

    void deleteCourse(int id);

    void assignAdminToCourse(Integer adminId, Integer courseId);

    void dismissAdmin(Integer courseId);

    boolean isUserRegisteredToCourse(UserCourse userCourse);

    boolean courseExists(String courseName);
}
