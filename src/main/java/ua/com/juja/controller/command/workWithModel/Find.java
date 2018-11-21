package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

public class Find implements Command {
    private ModelInterface model;

    public Find(ModelInterface model) {
        this.model = model;
    }

    @Override
    public boolean canProcess(String[] command) { return command[0].equalsIgnoreCase("find");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        model.find(command, connection);
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
