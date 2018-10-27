package ua.com.juja.view;



import java.util.List;
import java.util.Scanner;

public class ViewImpl implements ViewInterface {

    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void write(String message) {
        System.out.println(message);
    }

}
