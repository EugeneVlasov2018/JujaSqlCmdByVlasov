package ua.com.juja.controller.command.workWithController;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class WrongCommand implements Command {
    private ViewInterface view;

    public WrongCommand(ViewInterface view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return true;
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        view.setMessage("Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'");
        view.write();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

