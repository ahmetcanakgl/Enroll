package co.aca.service;

import co.aca.dao.imp.IUserDAO;
import co.aca.model.User;
import co.aca.model.UserCourse;
import co.aca.model.CourseReq;
import co.aca.service.imp.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService { //database işlemleri kontrol

    @Autowired
    private IUserDAO userDAO;

    @Override
    public List<User> getUserList() {
        return userDAO.getUserList();
    }

    public List<User> getRegisteredUserList() {
        return userDAO.getRegisteredUserList();
    }

    public List<User> getUnregisteredUserList() {
        return userDAO.getUnregisteredUserList();
    }

    @Override
    public ArrayList<User> listCourseApplies(UserCourse userCourse) {
        return userDAO.listCourseApplies(userCourse);
    }

    @Override
    public void applyTakingCourse(List<UserCourse> userCourseList) {
        userDAO.applyTakingCourse(userCourseList);
    }

    @Override
    public User getUserById(int id) {
        User obj = userDAO.getUserById(id);
        return obj;
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @Override
    public void updateUserFcm(User user) {
        userDAO.updateUserFcm(user);
    }


    @Override
    public synchronized int addUser(User user) {
        if (userDAO.userExists(user.getUserId(), user.getUsername())) {
            return 0;
        } else if (userDAO.isDeviceIdUsedBefore(user.getDeviceId())) {
            return 1;
        } else {
            userDAO.addUser(user);
            return 9;
        }
    }

    @Override
    public synchronized boolean registerUser(ArrayList<User> userList) {

        Boolean flag = true;

        for (User anUserList : userList) {

            if (userDAO.isUserRegistered(anUserList.getUserId()))
                flag = false;
            else
                userDAO.registerUser(anUserList);
        }
        return flag;
    }

    @Override
    public synchronized boolean unregisterUser(List<User> userList) {

        Boolean flag = true;

        for (User anUserList : userList) {

            if (userDAO.isUserRegistered(anUserList.getUserId()))
                userDAO.unregisterUser(anUserList);
            else
                flag = false;
        }
        return flag;
    }

    @Override
    public void deleteUser(int id) {
        userDAO.deleteUser(id);
    }

    @Override
    public void setLastLogin(User user) {
        userDAO.setLastLogin(user);
    }
}
