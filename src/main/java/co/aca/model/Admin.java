package co.aca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "admins")
@JsonIgnoreProperties("inspection")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "admin_id")
    private int adminId;

    @Column(name = "admin_username")
    private String adminUsername;

    @Column(name = "admin_password")
    private String adminPassword;

    @Column(name = "admin_fname")
    private String adminFName;

    @Column(name = "admin_lname")
    private String adminLName;

    @Column(name = "number_of_courses")
    @ColumnDefault("0")
    private String numberOfCourses;

    @Column(name = "super_admin")
    @ColumnDefault("0")
    private String isSuperAdmin;

    @Column(name = "registered")
    @ColumnDefault("0")
    private String registered;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "last_login")
    private Date lastLogin;

    @OneToMany(targetEntity=Course.class, mappedBy="admin", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courseList;

    public String getNumberOfCourses() {
        return numberOfCourses;
    }

    public void setNumberOfCourses(String numberOfCourses) {
        this.numberOfCourses = numberOfCourses;
    }

    public String getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(String isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public String getAdminFName() {
        return adminFName;
    }

    public void setAdminFName(String adminFName) {
        this.adminFName = adminFName;
    }

    public String getAdminLName() {
        return adminLName;
    }

    public void setAdminLName(String adminLName) {
        this.adminLName = adminLName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

}