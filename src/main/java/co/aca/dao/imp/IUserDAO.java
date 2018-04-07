package co.aca.dao.imp;

import co.aca.model.User;
import co.aca.model.UserCourse;
import co.aca.model.CourseReq;

import java.util.ArrayList;
import java.util.List;

public interface IUserDAO {
    
    List<User> getUserList();

    List<User> getRegisteredUserList();

    List<User> getUnregisteredUserList();

    ArrayList<User> listCourseApplies(UserCourse userCourse);

    void applyTakingCourse(List<UserCourse> userCourseList);

    User getUserById(int id);

    void addUser(User user);

    void updateUser(User user);

    void updateUserFcm(User user);

    void registerUser(User user);

    void unregisterUser(User user);

    void deleteUser(int id);

    boolean userExists(int userId, String username);

    boolean isDeviceIdUsedBefore(String deviceId);

    boolean isUserRegistered(int userId);

    void setLastLogin(User user);
}
