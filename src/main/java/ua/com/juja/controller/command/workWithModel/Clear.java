package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

public class Clear implements Command {
    private ModelInterface model;


    public Clear(ModelInterface model) {
        this.model = model;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("clear");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
       model.clear(command, connection);
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

