package co.aca.dao.imp;

import co.aca.model.Course;
import co.aca.model.Register;
import co.aca.model.Registration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface IRegisterDAO {

    ArrayList<Registration> getRegistrations(Course course);

    ArrayList<Registration> getRegistration(Register register);

    List<String> getCourseDates();

    void userRegister(Register register);

    boolean isUserBelongToCourse(Register register);

    boolean isUserRegisteredToday(Register register);
}
