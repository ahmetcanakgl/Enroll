package co.aca.controller;

import co.aca.model.PushCallModel;
import co.aca.model.PushModel;
import co.aca.service.AndroidPushNotificationsService;
import co.aca.service.imp.IAdminService;
import co.aca.service.imp.IUserService;
import co.aca.util.LangString;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/")
public class WebController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IUserService userService;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @PostMapping("send")
    public ResponseEntity<String> send(@RequestHeader("lang") String locale, @RequestBody PushCallModel pushCallModel) throws JSONException {


        JSONObject body = new JSONObject();
        //body.put("to", "cRKZHCMmqao:APA91bE52wHtkIz0rUmPQwo3FsYEnpHNZTLbTRUWNo3Gz-YIXlV3L3Osiuxi-6DJklU7knqRtX82w8WD48zbV3eM9ZlKTwMdctDFtgETntvpuWrvQPr3i8fiBJvpBj1-eaBYpYDua_U9");
        JSONObject notification = new JSONObject();
        JSONObject data = new JSONObject();

        switch (Integer.valueOf(pushCallModel.getPushCallModel().getOpType())) {

            case 0: //user_register_req > push to super_admins

                body.put("to", "/topics/superAdmin");
                notification.put("title", new LangString().getNewUserRegister(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());  //İçerik (app kapalıyken)
                data.put("title", new LangString().getNewUserRegister(locale));        //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage());    //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 1://admin_register_req> push to super_admins

                body.put("to", "/topics/superAdmin");
                notification.put("title", new LangString().getNewAdminRegister(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getNewAdminRegister(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 2://user_registered > push to user by id

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 0));
                notification.put("title", new LangString().getRegistrationIsDone(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getRegistrationIsDone(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 3://admin_registered > push to admin by id

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 1));
                notification.put("title", new LangString().getRegistrationIsDone(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getRegistrationIsDone(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 4://user_course_take_req > push to admin by course_id

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 1));
                notification.put("title", new LangString().getTakeCourseReq(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getTakeCourseReq(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 5://user_course_took > push to user by id

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 0));
                notification.put("title", new LangString().getTakeCourseReqIsDone(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getTakeCourseReqIsDone(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 6://message to admin

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 1));
                notification.put("title", new LangString().getYouHavePrivateMessage(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getYouHavePrivateMessage(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 7://message to user

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 0));
                notification.put("title", new LangString().getYouHavePrivateMessage(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getYouHavePrivateMessage(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());

                break;
            case 8://all users

                body.put("to", "/topics/user");
                notification.put("title", "Announcement");  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", "Announcement");     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());
                break;

            case 9: //dummy 2

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 0));
                notification.put("title", new LangString().getNewAdminRegister(locale));  //Title (app kapalıyken)
                notification.put("body", pushCallModel.getPushCallModel().getMessage());     //İçerik (app kapalıyken)
                data.put("title", new LangString().getNewAdminRegister(locale));     //Title (App Açıkken)
                data.put("message", pushCallModel.getPushCallModel().getMessage()); //İçerik (App Açıkken)
                data.put("data", pushCallModel.getPushCallModel().getData());
                data.put("type", pushCallModel.getPushCallModel().getType());
                break;
        }

        body.put("priority", "high");
        body.put("notification", notification);
        body.put("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("sendRedir")
    public ResponseEntity<String> redirectedPush(Model model) {

        PushCallModel pushCallModel = null;

        String opType = "";
        String locale = "";

        String messageStr = "";
        String dataStr = "";
        String typeStr = "";

        opType = (String) model.asMap().get("opType");
        locale = (String) model.asMap().get("locale");

        messageStr = (String) model.asMap().get("message");
        dataStr = (String) model.asMap().get("data");
        typeStr = (String) model.asMap().get("type");

        JSONObject body = new JSONObject();
        JSONObject notification = new JSONObject();
        JSONObject data = new JSONObject();

        switch (Integer.valueOf(opType)) {

            case 4://user_course_take_req > push to admin by course_id

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 1));
                notification.put("title", new LangString().getTakeCourseReq(locale));  //Title (app kapalıyken)
                notification.put("body", messageStr);     //İçerik (app kapalıyken)
                data.put("title", new LangString().getTakeCourseReq(locale));     //Title (App Açıkken)
                data.put("message", messageStr); //İçerik (App Açıkken)
                data.put("data", dataStr);
                data.put("type", typeStr);

                break;
            case 5://user_course_took > push to user by id

                body.put("to", getFcmToken(pushCallModel.getPushCallModel(), 0));
                notification.put("title", new LangString().getTakeCourseReqIsDone(locale));  //Title (app kapalıyken)
                notification.put("body", messageStr);     //İçerik (app kapalıyken)
                data.put("title", new LangString().getTakeCourseReqIsDone(locale));     //Title (App Açıkken)
                data.put("message", messageStr); //İçerik (App Açıkken)
                data.put("data", dataStr);
                data.put("type", typeStr);

                break;
        }

        body.put("priority", "high");
        body.put("notification", notification);
        body.put("data", data);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }

    public String getFcmToken(PushModel pushModel, int userType) {

            if (userType == 0) {//user
                if (userService.getUserById(Integer.valueOf(pushModel.getPersonId())).getFcmToken() != null)
                    return userService.getUserById(Integer.valueOf(pushModel.getPersonId())).getFcmToken();
                else
                    return "";

            } else {//admin
                if (adminService.getAdminById(Integer.valueOf(pushModel.getPersonId())).getFcmToken() != null)
                    return adminService.getAdminById(Integer.valueOf(pushModel.getPersonId())).getFcmToken();
                else
                    return "";
            }
    }

    public void messageToAdmin() {

    }

    public void messageToUser() {

    }
}