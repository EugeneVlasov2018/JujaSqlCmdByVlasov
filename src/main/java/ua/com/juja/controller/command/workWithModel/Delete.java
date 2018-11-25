package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Delete implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Delete(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("delete");
    }

    @Override
    public void doWork(String[] command, Connection connection) {

        view.setMessage(model.delete(command, connection));
        view.write();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
