package co.aca.dao;

import co.aca.dao.imp.ICourseDAO;
import co.aca.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class CourseDAO implements ICourseDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Course getCourseById(int id) {
        return entityManager.find(Course.class, id);
    }

    @Override
    public List<Course> getCourseList() {
        String hql = "FROM Course as crs ORDER BY crs.courseId";
        return (List<Course>) entityManager.createQuery(hql).getResultList();
    }

    @Override
    public List<Course> getAdminCourses(Admin admin) {
        String hql = "FROM Course as crs WHERE crs.admin.adminId = ? ORDER BY crs.courseId";
        List list = (List<Course>) entityManager.createQuery(hql)
                .setParameter(1, admin.getAdminId()).getResultList();
        return list;
    }

    @Override
    public List<UserCourses> getUserCourses(User user) {

        String hql1 = "SELECT c.courseName, c.courseId " +
                "FROM UserCourse as uc, Course as c " +
                "WHERE uc.courseId = c.courseId " +
                "AND uc.isRegistered = ? " +
                "AND uc.userId = ?";


        List list = (List<UserCourses>) entityManager.createQuery(hql1)
                .setParameter(1, "1")
                .setParameter(2, user.getUserId())
                .getResultList();

        ArrayList<UserCourses> userCourses = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            UserCourses courses = new UserCourses();
            Object[] object = (Object[]) list.get(i);

            courses.setCourseName((String) object[0]);
            courses.setCourseId(String.valueOf((Integer) object[1]));
            userCourses.add(courses);
        }

        return userCourses;
    }

    @Override
    public void addCourse(Course course) {
        entityManager.persist(course);
    }

    @Override
    public void takeCourse(UserCourse userCourse) {
        entityManager.persist(userCourse);
    }

    @Override
    public void updateCourse(Course course) {
        Course crs = getCourseById(course.getCourseId());
        crs.setCourseName(course.getCourseName());
        crs.setCourseCode(course.getCourseCode());
        entityManager.flush();
    }

    @Override
    public void deleteCourse(int id) {

        entityManager.remove(getCourseById(id));
    }

    @Override
    public void assignAdminToCourse(Integer adminId, Integer courseId) {

        Course course = getCourseById(courseId);
        Admin admin = new Admin();
        admin.setAdminId(adminId);
        course.setAdmin(admin);
        entityManager.flush();
    }

    @Override
    public void dismissAdmin(Integer courseId) {
        Course course = getCourseById(courseId);
        course.setAdmin(null);
        entityManager.flush();
    }

    @Override
    public boolean isUserRegisteredToCourse(UserCourse userCourse) {

        String hql = "FROM UserCourse as uc WHERE uc.userId = ? AND  uc.courseId = ?";
        int count = entityManager.createQuery(hql)
                .setParameter(1, userCourse.getUserId())
                .setParameter(2, userCourse.getCourseId())
                .getResultList().size();
        return count > 0;
    }

    @Override
    public boolean courseExists(String courseName) {
        String hql = "FROM Course as crs WHERE crs.courseName = ?";
        int count = entityManager.createQuery(hql).setParameter(1, courseName).getResultList().size();
        return count > 0 ? true : false;
    }
}
