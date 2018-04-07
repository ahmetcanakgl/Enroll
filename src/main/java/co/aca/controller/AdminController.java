package co.aca.controller;

import co.aca.error.EntityNotFoundException;
import co.aca.model.Admin;
import co.aca.model.PushCallModel;
import co.aca.model.PushModel;
import co.aca.model.User;
import co.aca.model.response.ResponseAdmin;
import co.aca.service.AndroidPushNotificationsService;
import co.aca.service.imp.IAdminService;
import co.aca.util.LangString;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/api/")
@SuppressWarnings("unchecked")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @GetMapping("get_admin")
    public ResponseEntity<Admin> getAdminById(@RequestHeader("lang") String locale,
                                              @RequestParam("id") Integer id,
                                              RedirectAttributes redirectAttrs) throws EntityNotFoundException {
        Admin admin = adminService.getAdminById(id);
        if (admin == null) {
            return new ResponseEntity(new ResponseAdmin(new LangString().getNoSuchAnAdmin(locale),"0"), HttpStatus.OK);
        }

        return new ResponseEntity(new ResponseAdmin(admin), HttpStatus.OK);
    }

    @GetMapping("get_admins")
    public ResponseEntity<List<Admin>> getAllAdmins() throws EntityNotFoundException{
        List<Admin> list = adminService.getAdminList();
        return new ResponseEntity(new ResponseAdmin(list), HttpStatus.OK);
    }

    @PostMapping("update_admin")
    public ResponseEntity<Admin> updateAdmin(@RequestHeader("lang") String locale,
                                             @RequestBody Admin admin) throws EntityNotFoundException{
        adminService.updateAdmin(admin);
        return new ResponseEntity(new ResponseAdmin(new LangString().getUpdated(locale),"1"), HttpStatus.OK);
    }

    @GetMapping("delete_admin")
    public ResponseEntity<Void> deleteAdmin(@RequestHeader("lang") String locale,
                                            @RequestParam("id") Integer id) throws EntityNotFoundException{
        adminService.deleteAdmin(id);
        return new ResponseEntity(new ResponseAdmin(new LangString().getDeleted(locale),"1"), HttpStatus.OK);
    }

    @PostMapping("register_admin")
    public ResponseEntity<Void> registerAdmin(@RequestHeader("lang") String locale,
                                              @RequestBody String json) throws EntityNotFoundException {

        ArrayList<Admin> adminList = new ArrayList<>();
        ArrayList<String> fcmTokenList = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("registerAdminOpsModels");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            Admin admin = new Admin();
            admin.setAdminId(Integer.valueOf(explrObject.getString("adminId")));
            adminList.add(admin);
        }

        for (Admin anAdminList : adminList) {
            String fcmToken = getFcmToken(anAdminList.getAdminId());
            fcmTokenList.add(fcmToken);
        }

        boolean flag = adminService.registerAdmin(adminList);

        PushModel pushModel = new PushModel();
        pushModel.setOpType("3");
        pushModel.setData("data");
        pushModel.setMessage(new LangString().getRegistrationIsDoneMsg(locale));
        pushModel.setType("0");

        for (String aFcmTokenList : fcmTokenList) {
            approvedRegistrationPush(pushModel, aFcmTokenList, locale);
        }

        if (!flag) {
            return new ResponseEntity(new ResponseAdmin(new LangString().getAdminsAlreadyRegistered(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseAdmin(new LangString().getRegistered(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("unregister_admin")
    public ResponseEntity<Void> unregisterAdmin(@RequestHeader("lang") String locale,
                                                @RequestBody String json) throws EntityNotFoundException {

        ArrayList<Admin> adminList = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("unregisterAdminOpsModels");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            Admin admin = new Admin();
            admin.setAdminId(Integer.valueOf(explrObject.getString("adminId")));
            adminList.add(admin);
        }

        boolean flag = adminService.unregisterAdmin(adminList);

        if (!flag) {
            return new ResponseEntity(new ResponseAdmin(new LangString().getAdminIsNotRegistered(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseAdmin(new LangString().getUnregistered(locale), "1"), HttpStatus.OK);
    }

    @GetMapping("registered_admins")
    public ResponseEntity<List<Admin>> getRegisteredAdmins() throws EntityNotFoundException {
        List<Admin> list = adminService.getRegisteredAdminList();
        return new ResponseEntity(new ResponseAdmin(list), HttpStatus.OK);
    }

    @GetMapping("unregistered_admins")
    public ResponseEntity<List<Admin>> getUnregisteredAdmins() throws EntityNotFoundException {
        List<Admin> list = adminService.getUnregisteredAdminList();
        return new ResponseEntity(new ResponseAdmin(list), HttpStatus.OK);
    }

    @PostMapping("update_admin_fcm")
    public ResponseEntity<Admin> updateAdminFcm(@RequestHeader("lang") String locale,
                                                @RequestBody Admin admin) throws EntityNotFoundException{

        adminService.updateAdminFcm(admin);
        return new ResponseEntity(new ResponseAdmin(new LangString().getUpdated(locale),"1"), HttpStatus.OK);
    }

    public String getFcmToken(Integer id) {

            if (adminService.getAdminById(id).getFcmToken() != null)
                return adminService.getAdminById(id).getFcmToken();
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