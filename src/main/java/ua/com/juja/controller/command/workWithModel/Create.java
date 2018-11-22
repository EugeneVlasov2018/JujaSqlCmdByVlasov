package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Create implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Create(ModelInterface model) {
        this.model = model;
    }
    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("create");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        if(command.length<3){
            view = new ViewImpl();
            view.setMessage("Недостаточно данных для запуска команды. Попробуйте еще раз");
            view.write();
        }
        else {
            model.create(command, connection);
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}