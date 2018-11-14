package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

public class Delete implements Command {
    private ModelInterface model;

    public Delete(ModelInterface model) {
        this.model = model;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("delete");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
       model.delete(command, connection);
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
