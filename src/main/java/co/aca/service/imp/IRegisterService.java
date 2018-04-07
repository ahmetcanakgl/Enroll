package co.aca.service.imp;

import co.aca.model.Course;
import co.aca.model.Register;
import co.aca.model.Registration;
import co.aca.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface IRegisterService {

    ArrayList<Registration> getRegistrations(Course course);

    ArrayList<Registration> getRegistration(Register register);

    List<String> getCourseDates();

    int userRegister(Register register);
}
