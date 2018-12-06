package ua.com.juja.view;


public class ConsoleView implements View {
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

}
