package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Create implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Create(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }
    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("create");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        if(command.length<3){
            view.setMessage("Недостаточно данных для запуска команды. Попробуйте еще раз");
            view.write();
        }
        else {
            view.setMessage(model.create(command, connection));
            view.write();
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}