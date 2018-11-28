package ua.com.juja.controller.command.workWithController;

import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.SystemExitException;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Exit implements Command {
    private ViewInterface view;

    public Exit(ViewInterface view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("exit");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        view.setMessage("Всего хорошего, до встречи снова))");
        view.write();
        throw new SystemExitException();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
