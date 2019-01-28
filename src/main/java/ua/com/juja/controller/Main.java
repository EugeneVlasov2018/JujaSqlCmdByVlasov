package ua.com.juja.controller;

import org.apache.log4j.Logger;
import ua.com.juja.model.PostgreModel;
import ua.com.juja.view.ConsoleView;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Main {
    private static final MainController mainController = new MainController(new PostgreModel(),new ConsoleView());
    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    public static void main(String[] args) {

        logger.info("Приложение запустилось, отрабатывает mainController.beginWork()");
        mainController.beginWork();
        logger.info("Приложение отработало корректно и завершило свою работу");
    }
}
