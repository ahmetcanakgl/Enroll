package co.aca.dao;

import co.aca.dao.imp.IUserDAO;
import co.aca.model.User;
import co.aca.model.UserCourse;
import co.aca.model.CourseReq;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class UserDAO implements IUserDAO { //database işlemlerinin yapıldığı yer

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getUserList() {
        String hql = "FROM User as usr ORDER BY usr.userId";
        return (List<User>) entityManager.createQuery(hql)
                .getResultList();
    }

    @Override
    public List<User> getRegisteredUserList() {
        String hql = "FROM User as usr Where usr.registered = ? ORDER BY usr.userId";
        return (List<User>) entityManager.createQuery(hql)
                .setParameter(1, "1").getResultList();
    }

    @Override
    public List<User> getUnregisteredUserList() {
        String hql = "FROM User as usr Where usr.registered = ? ORDER BY usr.userId";
        return (List<User>) entityManager.createQuery(hql).setParameter(1, "0").getResultList();
    }

    @Override
    public ArrayList<User> listCourseApplies(UserCourse userCourse) {

        String hqll = "SELECT u.fname, u.lname, u.userId  " +
                "FROM UserCourse as uc, User as u  " +
                "WHERE uc.isRegistered = ? " +
                "AND uc.courseId = ? " +
                "AND u.userId = uc.userId";

        List list = entityManager.createQuery(hqll)
                .setParameter(1, "0")
                .setParameter(2, userCourse.getCourseId())
                .getResultList();

        ArrayList<User> courseReqs = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            User spec = new User();
            Object[] object = (Object[]) list.get(i);

            spec.setFname((String) object[0]);
            spec.setLname((String) object[1]);
            spec.setUserId((Integer) object[2]);
            courseReqs.add(spec);
        }

        return courseReqs;
    }

    @Override
    public void applyTakingCourse(List<UserCourse> userCourseList) {

        for (UserCourse anUserCourseList : userCourseList) {
            UserCourse userCrs = findTheCourse(anUserCourseList.getUserId(), anUserCourseList.getCourseId());
            userCrs.setIsRegistered("1");
            entityManager.flush();
        }
    }

    private UserCourse findTheCourse(int userId, int courseId) {

        String hql = "FROM UserCourse as uc WHERE uc.userId = ? AND uc.courseId = ?";
        List<UserCourse> list = entityManager.createQuery(hql)
                .setParameter(1, userId)
                .setParameter(2, courseId)
                .getResultList();

        return list.get(0);
    }

    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        User usr = getUserById(user.getUserId());
        usr.setFname(user.getFname());
        usr.setLname(user.getLname());
        usr.setPassword(user.getPassword());
        usr.setUsername(user.getUsername());
        entityManager.flush();
    }

    @Override
    public void updateUserFcm(User user) {
        User usr = getUserById(user.getUserId());
        usr.setFcmToken(user.getFcmToken());
        entityManager.flush();
    }

    @Override
    public void registerUser(User user) {
        User usr = getUserById(user.getUserId());
        usr.setRegistered("1");
        entityManager.flush();
    }

    @Override
    public void unregisterUser(User user) {
        User usr = getUserById(user.getUserId());
        usr.setRegistered("0");
        entityManager.flush();
    }

    @Override
    public void deleteUser(int id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public boolean userExists(int userId, String username) {
        String hql = "FROM User as usr WHERE usr.userId = ? or usr.username = ?";
        List list = entityManager.createQuery(hql).setParameter(1, userId)
                .setParameter(2, username).getResultList();
        int count = list.size();
        return count > 0;
    }

    @Override
    public boolean isDeviceIdUsedBefore(String deviceId) {
        String hql = "FROM User as usr WHERE usr.deviceId = ?";
        List list = entityManager.createQuery(hql).setParameter(1, deviceId).getResultList();
        int count = list.size();
        return count > 0;
    }

    @Override
    public boolean isUserRegistered(int userId) {
        String hql = "SELECT usr.registered FROM User as usr WHERE usr.userId = ?";
        System.out.println("" + hql);
        List resultList = entityManager.createQuery(hql).setParameter(1, userId).getResultList();
        return resultList.size() > 0 && !resultList.get(0).equals("0");
    }

    @Override
    public void setLastLogin(User user) {

        DateTime dt = new DateTime();
        DateTimeZone dtZone = DateTimeZone.forOffsetHoursMinutes(3,0);
        DateTime dtus = dt.withZone(dtZone);
        Date gmtPlusThreeDate = dtus.toLocalDateTime().toDate();

        user.setLastLogin(gmtPlusThreeDate);
        entityManager.flush();
    }
}
