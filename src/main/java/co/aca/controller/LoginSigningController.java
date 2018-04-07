package co.aca.controller;

import co.aca.error.EntityNotFoundException;
import co.aca.model.*;
import co.aca.model.response.ResponseAdmin;
import co.aca.model.response.ResponseLogin;
import co.aca.model.response.ResponseUser;
import co.aca.service.AndroidPushNotificationsService;
import co.aca.service.imp.IAdminService;
import co.aca.service.imp.IUserService;
import co.aca.util.LangString;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Controller
@SuppressWarnings("unchecked")
public class LoginSigningController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAdminService adminService;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @PostMapping(value = "/user_login")
    public ResponseEntity<ResponseLogin> userLogin(@RequestBody Login login,
                                                   @RequestHeader("lang") String locale) throws ServletException {

        String jwtToken = "";

        if (login.getUserId() == null || login.getPassword() == null) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getFillUsernamePassword(locale), "0")
                    , HttpStatus.OK);
        }

        String userId = login.getUserId();
        String password = login.getPassword();
        String deviceId = login.getDeviceId();

        User user = userService.getUserById(Integer.valueOf(userId));

        if (user == null) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getUsernameNotFound(locale), "0")
                    , HttpStatus.OK);
        }

        String mDeviceId = user.getDeviceId();

        if (!mDeviceId.equals(deviceId)) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getLoginNotAllowed(locale), "0")
                    , HttpStatus.OK);
        }

        String pwd = user.getPassword();

        if (!password.equals(pwd)) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getInvalidLogin(locale), "0")
                    , HttpStatus.OK);
        }

        if (user.getRegistered().equals("0")) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getWaitForConfirmation(locale), "0")
                    , HttpStatus.OK);
        }

        userService.setLastLogin(user);

        jwtToken = Jwts
                .builder()
                .setSubject(userId)
                .claim("roles", "user")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey")
                .compact();

        return new ResponseEntity(new ResponseLogin(jwtToken, user), HttpStatus.OK);
    }

    @PostMapping(value = "/admin_login")
    public ResponseEntity<ResponseLogin> adminLogin(@RequestBody Login login,
                                                    @RequestHeader("lang") String locale) throws ServletException {

        String jwtToken = "";

        if (login.getAdminUsername() == null || login.getPassword() == null) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getFillUsernamePassword(locale), "0")
                    , HttpStatus.OK);
        }

        String adminUsername = login.getAdminUsername();
        String password = login.getPassword();

        Admin admin = adminService.getAdminByUsername(adminUsername);

        if (admin == null) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getUsernameNotFound(locale), "0")
                    , HttpStatus.OK);
        }

        String pwd = admin.getAdminPassword();

        if (!password.equals(pwd)) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getInvalidLogin(locale), "0")
                    , HttpStatus.OK);
        }

        if (admin.getRegistered().equals("0")) {
            return new ResponseEntity(
                    new ResponseLogin(new LangString().getWaitForConfirmation(locale), "0")
                    , HttpStatus.OK);
        }

        adminService.setLastLogin(admin);

        jwtToken = Jwts
                .builder()
                .setSubject(adminUsername)
                .claim("roles", "admin")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey")
                .compact();

        return new ResponseEntity(new ResponseLogin(jwtToken, admin), HttpStatus.OK);
    }

    @PostMapping("/add_user")
    public ResponseEntity<Void> addUser(@RequestHeader("lang") String locale,
                                        @RequestBody User user) throws EntityNotFoundException {
        user.setRegistered("0");
        int flag = userService.addUser(user);
        if (flag == 0) {
            return new ResponseEntity(new ResponseUser(new LangString().getUserExists(locale), "0"), HttpStatus.OK);
        } else if (flag == 1) {
            return new ResponseEntity(new ResponseUser(new LangString().getCantRegisterAgain(locale), "0"), HttpStatus.OK);
        }

        PushModel pushModel = new PushModel();
        pushModel.setOpType("0");
        pushModel.setData("data");
        pushModel.setMessage("message");
        pushModel.setType("0");

        addPersonPush(pushModel, locale);


        return new ResponseEntity(new ResponseUser(new LangString().getAdded(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("/add_admin")
    public ResponseEntity<Admin> addAdmin(@RequestHeader("lang") String locale,
                                          @RequestBody Admin admin) throws EntityNotFoundException {
        admin.setIsSuperAdmin("0");
        admin.setRegistered("0");
        admin.setNumberOfCourses("0");
        boolean flag = adminService.addAdmin(admin);
        if (!flag) {
            return new ResponseEntity(new ResponseAdmin(new LangString().getAdminExists(locale), "0"), HttpStatus.OK);
        }

        PushModel pushModel = new PushModel();
        pushModel.setOpType("1");
        pushModel.setData("data");
        pushModel.setMessage("message");
        pushModel.setType("0");

        addPersonPush(pushModel, locale);

        return new ResponseEntity(new ResponseAdmin(new LangString().getAdded(locale), "1"), HttpStatus.OK);
    }

    @GetMapping("tos")
    public String termOfUse(@RequestParam(value = "lang", required = false) String lang) throws EntityNotFoundException {

        if (lang == null) {
            return "redirect:/tos_en.html";
        } else {
            if (lang.equals("tr"))
                return "redirect:/tos_tr.html";
            else
                return "redirect:/tos_en.html";
        }
    }


    private void addPersonPush(PushModel pushModel, String locale) {

        JSONObject body = new JSONObject();
        JSONObject notification = new JSONObject();
        JSONObject data = new JSONObject();

        if (pushModel.getOpType().equals("0")) {
            notification.put("title", new LangString().getNewUserRegister(locale));
            data.put("title", new LangString().getNewUserRegister(locale));
        } else {
            notification.put("title", new LangString().getNewAdminRegister(locale));
            data.put("title", new LangString().getNewAdminRegister(locale));
        }

        notification.put("body", pushModel.getMessage());
        data.put("message", pushModel.getMessage());
        data.put("data", pushModel.getData());
        data.put("type", pushModel.getType());
        body.put("to", "/topics/superAdmin");
        body.put("priority", "high");
        body.put("notification", notification);
        body.put("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();
    }
}