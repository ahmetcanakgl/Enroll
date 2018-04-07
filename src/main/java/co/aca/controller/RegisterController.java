package co.aca.controller;

import co.aca.error.EntityNotFoundException;
import co.aca.model.Course;
import co.aca.model.Register;
import co.aca.model.Registration;
import co.aca.model.response.ResponseRegister;
import co.aca.model.response.ResponseRegistration;
import co.aca.service.imp.IRegisterService;
import co.aca.util.LangString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/")
@SuppressWarnings("unchecked")
public class RegisterController {

    @Autowired
    private IRegisterService registerService;

    @PostMapping("user_registration")
    public ResponseEntity<Void> registerUser(@RequestHeader("lang") String locale,
                                             @RequestBody Register register) throws EntityNotFoundException {

        int flag = registerService.userRegister(register);
        if (flag == 0) {
            return new ResponseEntity(new ResponseRegister(new LangString().getYouRNotTakeThisCourse(locale), "0"), HttpStatus.OK);
        }else if(flag == 2){
            return new ResponseEntity(new ResponseRegister(new LangString().getAlreadyRegisteredThisCourse(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseRegister(new LangString().getSuccess(locale),"1"), HttpStatus.OK);
    }

    @PostMapping("get_registrations")
    public ResponseEntity<Void> getRegistrations(@RequestBody Course course) throws EntityNotFoundException {

        ArrayList<Registration> flag = registerService.getRegistrations(course);
        return new ResponseEntity(new ResponseRegistration(flag), HttpStatus.OK);
    }

    @PostMapping("get_registration")
    public ResponseEntity<Void> getRegistration(@RequestBody Register register) throws EntityNotFoundException {

        ArrayList<Registration> flag = registerService.getRegistration(register);
        return new ResponseEntity(new ResponseRegistration(flag), HttpStatus.OK);
    }
}
