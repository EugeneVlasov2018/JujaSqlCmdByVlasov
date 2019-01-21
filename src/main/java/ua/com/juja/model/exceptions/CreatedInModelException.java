package ua.com.juja.model.exceptions;

public class CreatedInModelException extends Exception {

    private String message;

    public CreatedInModelException(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
