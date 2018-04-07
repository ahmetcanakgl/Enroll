package co.aca.dao;

import co.aca.dao.imp.IAdminDAO;
import co.aca.model.Admin;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hibernate.type.descriptor.java.DateTypeDescriptor.DATE_FORMAT;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class AdminDAO implements IAdminDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Admin getAdminById(int id) {
        return entityManager.find(Admin.class, id);
    }

    @Override
    public Admin getAdminByUsername(String username) {
        String hql = "FROM Admin as adm WHERE adm.adminUsername = ?";

        List resultList = entityManager.createQuery(hql).setParameter(1,username).getResultList();
        if(resultList.size()>0){
            return (Admin) resultList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<Admin> getAdminList() {
        String hql = "FROM Admin as adm ORDER BY adm.adminId";
        return (List<Admin>) entityManager.createQuery(hql).getResultList();
    }

    @Override
    public void addAdmin(Admin admin) {
        entityManager.persist(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        Admin admn = getAdminById(admin.getAdminId());
        admn.setAdminFName(admin.getAdminFName());
        admn.setAdminLName(admin.getAdminLName());
        admn.setAdminUsername(admin.getAdminUsername());
        admn.setAdminPassword(admin.getAdminPassword());
        entityManager.flush();
    }

    @Override
    public void updateAdminFcm(Admin admin) {
        Admin admn = getAdminById(admin.getAdminId());
        admn.setFcmToken(admin.getFcmToken());
        entityManager.flush();
    }

    @Override
    public void deleteAdmin(int id) {
        entityManager.remove(getAdminById(id));
    }

    @Override
    public boolean adminExists(String username) {
        String hql = "FROM Admin as adm WHERE adm.adminUsername = ?";
        int count = entityManager.createQuery(hql).setParameter(1, username).getResultList().size();
        return count > 0 ? true : false;
    }

    @Override
    public boolean isAdminRegistered(int adminId) {
        String hql = "SELECT usr.registered FROM Admin as usr WHERE usr.adminId = ?";
        System.out.println("" + hql);
        List resultList = entityManager.createQuery(hql).setParameter(1, adminId).getResultList();
        return resultList.size() > 0 && !resultList.get(0).equals("0");
    }

    @Override
    public void registerAdmin(Admin admin) {
        Admin usr = getAdminById(admin.getAdminId());
        usr.setRegistered("1");
        entityManager.flush();
    }

    @Override
    public void unregisterAdmin(Admin admin) {
        Admin usr = getAdminById(admin.getAdminId());
        usr.setRegistered("0");
        entityManager.flush();
    }

    @Override
    public List<Admin> getRegisteredAdminList() {
        String hql = "FROM Admin as usr Where usr.registered = ? ORDER BY usr.adminId";
        return (List<Admin>) entityManager.createQuery(hql)
                .setParameter(1, "1").getResultList();
    }

    @Override
    public List<Admin> getUnregisteredAdminList() {
        String hql = "FROM Admin as usr Where usr.registered = ? ORDER BY usr.adminId";
        return (List<Admin>) entityManager.createQuery(hql).setParameter(1, "0").getResultList();
    }

    @Override
    public void setLastLogin(Admin admin) {

        DateTime dt = new DateTime();
        DateTimeZone dtZone = DateTimeZone.forOffsetHoursMinutes(3,0);
        DateTime dtus = dt.withZone(dtZone);
        Date gmtPlusThreeDate = dtus.toLocalDateTime().toDate();

        admin.setLastLogin(gmtPlusThreeDate);
        entityManager.flush();
    }
}
