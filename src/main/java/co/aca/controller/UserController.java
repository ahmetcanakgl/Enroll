package co.aca.controller;

import co.aca.error.EntityNotFoundException;
import co.aca.model.PushModel;
import co.aca.model.User;
import co.aca.model.UserCourse;
import co.aca.model.response.ResponseCourseReq;
import co.aca.model.response.ResponseUser;
import co.aca.service.AndroidPushNotificationsService;
import co.aca.service.imp.IUserService;
import co.aca.util.LangString;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/api/")
@SuppressWarnings("unchecked")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @GetMapping("get_user")
    public ResponseEntity<User> getUserById(@RequestHeader("lang") String locale,
                                            @RequestParam("id") Integer id) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity(new ResponseUser(new LangString().getNoSuchAnUser(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseUser(user), HttpStatus.OK);
    }

    @PostMapping("get_user")
    public ResponseEntity<User> getUserById(@RequestHeader("lang") String locale,
                                            @RequestBody User user) throws EntityNotFoundException {
        User usr = userService.getUserById(user.getUserId());
        if (usr == null) {
            return new ResponseEntity(new ResponseUser(new LangString().getNoSuchAnUser(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseUser(usr), HttpStatus.OK);
    }

    @GetMapping("get_users")
    public ResponseEntity<List<User>> getAllUsers() throws EntityNotFoundException {
        List<User> list = userService.getUserList();
        return new ResponseEntity(new ResponseUser(list), HttpStatus.OK);
    }

    @GetMapping("registered_users")
    public ResponseEntity<List<User>> getRegisteredUsers() throws EntityNotFoundException {
        List<User> list = userService.getRegisteredUserList();
        return new ResponseEntity(new ResponseUser(list), HttpStatus.OK);
    }

    @GetMapping("unregistered_users")
    public ResponseEntity<List<User>> getUnregisteredUsers() throws EntityNotFoundException {
        List<User> list = userService.getUnregisteredUserList();
        return new ResponseEntity(new ResponseUser(list), HttpStatus.OK);
    }

    @PostMapping("get_taking_course_requests")
    public ResponseEntity<Void> getCourseApplies(@RequestBody UserCourse userCourse) throws EntityNotFoundException {
        ArrayList<User> list = userService.listCourseApplies(userCourse);
        return new ResponseEntity(new ResponseUser(list), HttpStatus.OK);
    }

    @PostMapping("apply_taking_course_request")
    public ResponseEntity<Void> applyTakingCourse(@RequestHeader("lang") String locale,
                                                  @RequestBody String json) throws EntityNotFoundException {

        ArrayList<UserCourse> userCourseList = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("applyCourseList");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            UserCourse userCourse = new UserCourse();
            userCourse.setCourseId(Integer.valueOf(explrObject.getString("courseId")));
            userCourse.setUserId(Integer.valueOf(explrObject.getString("userId")));
            userCourseList.add(userCourse);
        }

        userService.applyTakingCourse(userCourseList);
        return new ResponseEntity(new ResponseCourseReq(new LangString().getApplied(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("register_user")
    public ResponseEntity<Void> registerUser(@RequestHeader("lang") String locale,
                                             @RequestBody String json) throws EntityNotFoundException {

        ArrayList<User> userList = new ArrayList<>();
        ArrayList<String> fcmTokenList = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("registerUserOpsModels");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            User user = new User();
            user.setUserId(Integer.valueOf(explrObject.getString("userId")));
            userList.add(user);
        }

        for (User anUserList : userList) {
            String fcmToken = getFcmToken(anUserList.getUserId());
            fcmTokenList.add(fcmToken);
        }

        boolean flag = userService.registerUser(userList);

        PushModel pushModel = new PushModel();
        pushModel.setOpType("2");
        pushModel.setData("data");
        pushModel.setMessage(new LangString().getRegistrationIsDoneMsg(locale));
        pushModel.setType("0");

        for (String aFcmTokenList : fcmTokenList) {
            approvedRegistrationPush(pushModel, aFcmTokenList, locale);
        }

        if (!flag) {
            return new ResponseEntity(new ResponseUser(new LangString().getUsersAlreadyRegistered(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseUser(new LangString().getRegistered(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("unregister_user")
    public ResponseEntity<Void> unregisterUser(@RequestHeader("lang") String locale,
                                               @RequestBody String json) throws EntityNotFoundException {

        ArrayList<User> userList = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("unregisterUserOpsModels");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            User user = new User();
            user.setUserId(Integer.valueOf(explrObject.getString("userId")));
            userList.add(user);
        }

        boolean flag = userService.unregisterUser(userList);
        if (!flag) {
            return new ResponseEntity(new ResponseUser(new LangString().getUserIsNotRegistered(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseUser(new LangString().getUnregistered(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("update_user")
    public ResponseEntity<User> updateUser(@RequestHeader("lang") String locale,
                                           @RequestBody User user) throws EntityNotFoundException {
        userService.updateUser(user);
        return new ResponseEntity(new ResponseUser(new LangString().getUpdated(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("update_user_fcm")
    public ResponseEntity<User> updateUserFcm(@RequestHeader("lang") String locale,
                                                @RequestBody User user) throws EntityNotFoundException{

        userService.updateUserFcm(user);
        return new ResponseEntity(new ResponseUser(new LangString().getUpdated(locale),"1"), HttpStatus.OK);
    }

    @PostMapping("delete_user")
    public ResponseEntity<Void> deleteUser(@RequestHeader("lang") String locale,
                                           @RequestBody User user) throws EntityNotFoundException {
        userService.deleteUser(user.getUserId());
        return new ResponseEntity(new ResponseUser(new LangString().getDeleted(locale), "1"), HttpStatus.OK);
    }

    public String getFcmToken(Integer id) {

        if (userService.getUserById(id).getFcmToken() != null)
            return userService.getUserById(id).getFcmToken();
        else
            return "";
    }

    private void approvedRegistrationPush(PushModel pushModel,String fcmToken, String locale) {

        JSONObject body = new JSONObject();
        JSONObject notification = new JSONObject();
        JSONObject data = new JSONObject();

        notification.put("title", new LangString().getRegistrationIsDone(locale));
        notification.put("body", pushModel.getMessage());
        data.put("title", new LangString().getRegistrationIsDone(locale));
        data.put("message", pushModel.getMessage());
        data.put("data", pushModel.getData());
        data.put("type", pushModel.getType());
        body.put("to", fcmToken);
        body.put("priority", "high");
        body.put("notification", notification);
        body.put("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();
    }
}