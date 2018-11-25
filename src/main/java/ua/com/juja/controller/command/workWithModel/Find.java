package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Find implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Find(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("find");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        if (command.length < 2) {
            view.setMessage("Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которую собираетесь вывести на екран");
            view.write();
        } else {
            view.setMessage(model.find(command, connection));
            view.write();
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
