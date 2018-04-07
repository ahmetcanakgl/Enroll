package co.aca.service.imp;

import co.aca.model.*;

import java.util.List;

public interface ICourseService {

    List<Course> getCourseList();

    List<Course> getAdminCourses(Admin admin);

    List<UserCourses> getUserCourses(User user);

    Course getCourseById(int id);

    boolean addCourse(Course course);

    int takeCourse(List<UserCourse> userCourseList);

    void assignAdminToCourse(Integer adminId, Integer courseId);

    void dismissAdmin(Integer courseId);

    void updateCourse(Course course);

    void deleteCourse(int id);
}
