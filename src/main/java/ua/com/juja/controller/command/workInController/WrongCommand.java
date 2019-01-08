package ua.com.juja.controller.command.workInController;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import java.sql.Connection;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class WrongCommand implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private View view;

    public WrongCommand(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return true;
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        view.write("Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'");
        logger.debug("doWork() отработал корректно");
    }
}

