package co.aca.model.response;

import co.aca.model.Registration;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseRegistration {

    private String isSuccessful;
    private Registration registration;
    private List<Registration> registrationList;
    private String errorMessage;
    private String successMessage;

    public ResponseRegistration(Registration registration) {
        setIsSuccessful("1");
        setRegistration(registration);
    }

    public ResponseRegistration(ArrayList<Registration> regisArrayList) {
        setIsSuccessful("1");
        setRegistrationList(regisArrayList);
    }

    public ResponseRegistration(String message, String isSuccess) {
        if (isSuccess.equals("1")) {
            setSuccessMessage(message);
        } else {
            setErrorMessage(message);
        }
        setIsSuccessful(isSuccess);
    }

    public Registration getRegistration() {
        return registration;
    }

    public List<Registration> getRegistrationList() {
        return registrationList;
    }

    public void setRegistrationList(List<Registration> registrationList) {
        this.registrationList = registrationList;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public String getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(String isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
