package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Clear implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Clear(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("clear");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        if(command.length<2){
            view.setMessage("Недостаточно данных для запуска команды. " +
                    "Укажите имя таблицы, которое собираетесь очистить");
            view.write();
        } else {
            view.setMessage(model.clear(command, connection));
            view.write();
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

