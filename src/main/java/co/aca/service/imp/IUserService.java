package co.aca.service.imp;

import co.aca.model.User;
import co.aca.model.UserCourse;
import co.aca.model.CourseReq;

import java.util.ArrayList;
import java.util.List;

public interface IUserService {

    List<User> getUserList();

    List<User> getRegisteredUserList();

    List<User> getUnregisteredUserList();

    ArrayList<User> listCourseApplies(UserCourse userCourse);

    void applyTakingCourse(List<UserCourse> userCourseList);

    User getUserById(int id);

    int addUser(User user);

    void updateUser(User user);

    void updateUserFcm(User user);

    boolean registerUser(ArrayList<User> userList);

    boolean unregisterUser(List<User> userList);

    void deleteUser(int id);

    void setLastLogin(User user);

}
