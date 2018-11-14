package ua.com.juja.controller;

import ua.com.juja.model.ModelImplWithPostgre;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewImpl;

public class Main {
    private static final MainController mainController = new MainController(new ModelImplWithPostgre(),new ViewImpl());

    public static void main(String[] args) {
        mainController.beginWork();
    }
}
