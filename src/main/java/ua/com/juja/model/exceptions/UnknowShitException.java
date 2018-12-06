package ua.com.juja.model.exceptions;

public class UnknowShitException extends Exception {

    private String message;

    public UnknowShitException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
