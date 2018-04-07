package co.aca.dao;

import co.aca.dao.imp.IRegisterDAO;
import co.aca.model.Course;
import co.aca.model.Register;
import co.aca.model.Registration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class RegisterDAO implements IRegisterDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ArrayList<Registration> getRegistrations(Course course) {

        String hqll = "SELECT u.fname, u.lname, u.userId, r.signingDate  " +
                "FROM Register as r, User as u  " +
                "WHERE r.courseId = ? " +
                "AND r.userId = u.userId ";

        List list = entityManager.createQuery(hqll)
                .setParameter(1, course.getCourseId())
                .getResultList();

        ArrayList<Registration> registrations = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Registration regst = new Registration();
            Object[] object = (Object[]) list.get(i);

            regst.setFirstName((String) object[0]);
            regst.setLastName((String) object[1]);
            regst.setUserId((Integer) object[2]);
            regst.setSigningDate(String.valueOf(object[3]));
            registrations.add(regst);
        }

        return registrations;
    }

    @Override
    public ArrayList<Registration> getRegistration(Register register) {

        String hqll = "SELECT u.fname, u.lname, u.userId, r.signingDate  " +
                "FROM Register as r, User as u  " +
                "WHERE r.courseId = ? " +
                "AND r.userId = ? " +
                "AND r.userId = u.userId ";

        List list = entityManager.createQuery(hqll)
                .setParameter(1, register.getCourseId())
                .setParameter(2, register.getUserId())
                .getResultList();

        ArrayList<Registration> registrations = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Registration regst = new Registration();
            Object[] object = (Object[]) list.get(i);

            regst.setFirstName((String) object[0]);
            regst.setLastName((String) object[1]);
            regst.setUserId((Integer) object[2]);
            regst.setSigningDate(String.valueOf(object[3]));
            registrations.add(regst);
        }

        return registrations;
    }

    @Override
    public List<String> getCourseDates() {
        return null;
    }

    @Override
    public void userRegister(Register register) {
        entityManager.persist(register);
    }

    @Override
    public boolean isUserBelongToCourse(Register register) {
        String hql = "FROM UserCourse as u_c WHERE userId = ? AND courseId = ? AND registered = ?";
        System.out.println("" + hql);
        List resultList = entityManager.createQuery(hql)
                .setParameter(1, register.getUserId())
                .setParameter(2, register.getCourseId())
                .setParameter(3, "1")
                .getResultList();
        return resultList.size() > 0 && !resultList.get(0).equals("0");
    }

    @Override
    public boolean isUserRegisteredToday(Register register) {

        String hql = "FROM Register as r WHERE userId = ? AND courseId = ?";
        System.out.println("" + hql);
        List resultList = entityManager.createQuery(hql)
                .setParameter(1, register.getUserId())
                .setParameter(2, register.getCourseId())
                .getResultList();

        if (resultList.size() == 0) {
            return false;
        } else {
            Register registerModel = (Register) resultList.get(resultList.size() - 1);
            Date lastSignDate = registerModel.getSigningDate();

            long hourDiffer = getDateDiff(lastSignDate, new Date(), TimeUnit.HOURS);

            return hourDiffer < 20;
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {

        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
