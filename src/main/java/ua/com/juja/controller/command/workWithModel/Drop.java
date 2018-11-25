package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Drop implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Drop(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("drop");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        view.setMessage(model.drop(command, connection));
        view.write();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
