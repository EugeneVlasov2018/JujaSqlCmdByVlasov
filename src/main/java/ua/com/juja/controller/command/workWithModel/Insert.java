package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

public class Insert implements Command {
    private ModelInterface model;

    public Insert(ModelInterface model) {
        this.model = model;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("insert");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        model.insert(command, connection);
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

