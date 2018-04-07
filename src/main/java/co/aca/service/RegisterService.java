package co.aca.service;

import co.aca.dao.imp.IRegisterDAO;
import co.aca.model.Course;
import co.aca.model.Register;
import co.aca.model.Registration;
import co.aca.service.imp.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RegisterService implements IRegisterService {

    @Autowired
    private IRegisterDAO registerDAO;


    @Override
    public ArrayList<Registration> getRegistrations(Course course) {
        return registerDAO.getRegistrations(course);
    }

    @Override
    public ArrayList<Registration> getRegistration(Register register) {
        return registerDAO.getRegistration(register);
    }

    @Override
    public List<String> getCourseDates() {
        return null;
    }

    @Override
    public int userRegister(Register register) {
        if (registerDAO.isUserBelongToCourse(register)) {
            if (!registerDAO.isUserRegisteredToday(register)) {
                registerDAO.userRegister(register);
                return 1;
            }else
                return 2;
        } else
            return 0;
    }
}
