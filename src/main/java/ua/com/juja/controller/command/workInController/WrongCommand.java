package ua.com.juja.controller.command.workInController;

import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import java.sql.Connection;

public class WrongCommand implements Command {
    private View view;

    public WrongCommand(View view) {
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

