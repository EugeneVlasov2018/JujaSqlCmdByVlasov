package ua.com.juja.controller;

import ua.com.juja.model.PostgreModel;
import ua.com.juja.view.ConsoleView;

public class Main {
    private static final MainController mainController = new MainController(new PostgreModel(),new ConsoleView());

    public static void main(String[] args) {
        mainController.beginWork();
    }
}
