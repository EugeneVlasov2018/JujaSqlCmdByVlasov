package ua.com.juja.view;



import java.util.List;
import java.util.Scanner;

public class ViewImpl implements ViewInterface {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void write() {
        System.out.println(message);
    }

    @Override
    public void writeWithFormat() {
        System.out.printf(message);
    }

}
