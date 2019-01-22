package ua.com.juja.controller.command.exceptions;

public class CommandIsWrongException extends Exception {
    private String message;

    public CommandIsWrongException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
