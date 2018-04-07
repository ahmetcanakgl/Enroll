package co.aca.model;

public class PushModel {

    //below related to Android App
    private String message;//İçerik (App Açıkken)
    private String data;
    private String type;
    //below related to backend
    private String personId;//user or admin id
    private String opType;  //0 = user_register_req > push to super_admins
                            //1 = admin_register_req> push to super_admins
                            //2 = user_registered > push to user by id
                            //3 = admin_registered > push to admin by id
                            //4 = user_course_take_req > push to admin by course_id
                            //5 = user_course_took > push to user by id
                            //6 = message to admin
                            //7 = message to user

    private String opContent;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getOpContent() {
        return opContent;
    }

    public void setOpContent(String opContent) {
        this.opContent = opContent;
    }
}
