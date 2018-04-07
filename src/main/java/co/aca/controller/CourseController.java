package co.aca.controller;

import co.aca.error.EntityNotFoundException;
import co.aca.model.*;
import co.aca.model.response.ResponseCourse;
import co.aca.model.response.ResponseUserCourses;
import co.aca.service.imp.ICourseService;
import co.aca.util.LangString;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/")
@SuppressWarnings("unchecked")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @GetMapping("get_course")
    public ResponseEntity<Course> getCourseById(@RequestHeader("lang") String locale, @RequestParam("id") Integer id) throws EntityNotFoundException {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return new ResponseEntity(new ResponseCourse(new LangString().getNoSuchACourse(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseCourse(course), HttpStatus.OK);
    }

    @GetMapping("get_courses")
    public ResponseEntity<List<Course>> getAllCourses() throws EntityNotFoundException {
        List<Course> list = courseService.getCourseList();
        return new ResponseEntity(new ResponseCourse(list), HttpStatus.OK);
    }

    @GetMapping("assign_admin")
    public ResponseEntity<Void> assignAdminToCourse(@RequestHeader("lang") String locale,
                                                    @RequestParam("adminId") Integer adminId,
                                                    @RequestParam("courseId") Integer courseId)
            throws EntityNotFoundException {

        courseService.assignAdminToCourse(adminId, courseId);
        return new ResponseEntity(new ResponseCourse(new LangString().getAssigned(locale), "1"), HttpStatus.OK);
    }

    @GetMapping("dismiss_admin")
    public ResponseEntity<Void> dismissAdmin(@RequestHeader("lang") String locale, @RequestParam("courseId") Integer courseId)
            throws EntityNotFoundException {

        courseService.dismissAdmin(courseId);
        return new ResponseEntity(new ResponseCourse(new LangString().getDismissed(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("add_course")
    public ResponseEntity<Void> addCourse(@RequestHeader("lang") String locale, @RequestBody Course course) throws EntityNotFoundException {
        boolean flag = courseService.addCourse(course);
        if (!flag) {
            return new ResponseEntity(new ResponseCourse(new LangString().getCourseExists(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseCourse(new LangString().getAdded(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("get_admin_courses")
    public ResponseEntity<Void> getAdminCourses(@RequestBody Admin admin) throws EntityNotFoundException {
        List<Course> adminCourses = courseService.getAdminCourses(admin);

        return new ResponseEntity(new ResponseCourse(adminCourses), HttpStatus.OK);
    }

    @PostMapping("get_user_courses")
    public ResponseEntity<Void> getUserCourses(@RequestBody User user) throws EntityNotFoundException {
        List<UserCourses> courses = courseService.getUserCourses(user);

        return new ResponseEntity(new ResponseUserCourses(courses), HttpStatus.OK);
    }

    //@PostMapping("take_course")
    //public ResponseEntity<Void> takeCourse(@RequestBody ArrayList<UserCourse> userCourseList) throws EntityNotFoundException {
    //public ResponseEntity<Void> takeCourse(@RequestBody UserCourseList userCourseList) throws EntityNotFoundException {

    //class UserCourseList {
    //    private List<UserCourse> takeCourseModels;
    //}
    //
    //class UserCourse {
    //    private long userId;
    //    private long courseId;
    //}
    @PostMapping("take_course")
    public ResponseEntity<Void> takeCourse(@RequestHeader("lang") String locale, @RequestBody String json) throws EntityNotFoundException {

        ArrayList<UserCourse> userCourseList = new ArrayList<>();
        ArrayList<String> fcmTokenList = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(json);
        JSONArray jsonArray = jsnobject.getJSONArray("takeCourseModels");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            UserCourse userCourse = new UserCourse();
            userCourse.setCourseId(Integer.valueOf(explrObject.getString("courseId")));
            userCourse.setUserId(Integer.valueOf(explrObject.getString("userId")));
            userCourse.setIsRegistered("0");
            userCourseList.add(userCourse);
        }

        int flag = courseService.takeCourse(userCourseList);
        if (flag == 0) {
            return new ResponseEntity(new ResponseCourse(new LangString().getAlreadyTaken(locale), "0"), HttpStatus.OK);
        }else if(flag == 2){
            return new ResponseEntity(new ResponseCourse(new LangString().getCourseCapasityFull(locale), "0"), HttpStatus.OK);
        }
        return new ResponseEntity(new ResponseCourse(new LangString().getAddedToCourseList(locale), "1"), HttpStatus.OK);
    }

    @PostMapping("update_course")
    public ResponseEntity<Course> updateCourse(@RequestHeader("lang") String locale, @RequestBody Course course) throws EntityNotFoundException {
        courseService.updateCourse(course);
        return new ResponseEntity(new ResponseCourse(new LangString().getUpdated(locale), "1"), HttpStatus.OK);
    }

    @GetMapping("delete_course")
    public ResponseEntity<Void> deleteCourse(@RequestHeader("lang") String locale, @RequestParam("id") Integer id) throws EntityNotFoundException {
        courseService.deleteCourse(id);
        return new ResponseEntity(new ResponseCourse(new LangString().getDeleted(locale), "1"), HttpStatus.OK);
    }
}