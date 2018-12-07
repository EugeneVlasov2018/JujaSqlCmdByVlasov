package ua.com.juja.view;


import java.util.Scanner;

public class ConsoleView implements View {

    @Override
    public String read() {
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }

    @Override
    public void write(String message) {
        System.out.println(message);
    }

}
