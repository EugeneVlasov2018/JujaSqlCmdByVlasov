package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

public class Drop implements Command {
    private ModelInterface model;

    public Drop(ModelInterface model) {
        this.model = model;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("drop");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        model.drop(command, connection);
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
