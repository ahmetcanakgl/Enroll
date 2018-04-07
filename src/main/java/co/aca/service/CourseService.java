package co.aca.service;

import co.aca.dao.imp.ICourseDAO;
import co.aca.model.*;
import co.aca.service.imp.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements ICourseService {

    @Autowired
    private ICourseDAO courseDAO;

    @Override
    public Course getCourseById(int id) {
        Course obj = courseDAO.getCourseById(id);
        return obj;
    }

    @Override
    public List<Course> getCourseList() {
        return courseDAO.getCourseList();
    }

    @Override
    public List<Course> getAdminCourses(Admin admin) {
        return courseDAO.getAdminCourses(admin);
    }

    @Override
    public List<UserCourses> getUserCourses(User user) {
        return courseDAO.getUserCourses(user);
    }

    @Override
    public synchronized boolean addCourse(Course course) {
        if (courseDAO.courseExists(course.getCourseName())) {
            return false;
        } else {
            courseDAO.addCourse(course);
            return true;
        }
    }

    @Override
    public int takeCourse(List<UserCourse> userCourseList) {

        int flag = 2;

        for (int i = 0; i < userCourseList.size(); i++) {
            if (courseDAO.isUserRegisteredToCourse(userCourseList.get(i))) {
                flag = 0;
            } else {
                courseDAO.takeCourse(userCourseList.get(i));
                flag = 1;
            }
        }

        return flag;
    }

    @Override
    public void assignAdminToCourse(Integer adminId, Integer courseId) {
        courseDAO.assignAdminToCourse(adminId, courseId);
    }

    @Override
    public void dismissAdmin(Integer courseId) {
        courseDAO.dismissAdmin(courseId);
    }

    @Override
    public void updateCourse(Course course) {
        courseDAO.updateCourse(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseDAO.deleteCourse(id);
    }
}
