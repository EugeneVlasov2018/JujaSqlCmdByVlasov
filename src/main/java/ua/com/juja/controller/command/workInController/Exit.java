package ua.com.juja.controller.command.workInController;

import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.SystemExitException;
import ua.com.juja.view.View;

import java.sql.Connection;

public class Exit implements Command {
    private View view;

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("exit");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        view.write("Всего хорошего, до встречи снова))");
        throw new SystemExitException();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
