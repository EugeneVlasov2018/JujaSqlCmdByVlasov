package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public class Insert implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Insert(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("insert");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        if (command.length < 4 || command.length % 2 != 0) {
            if (command.length < 4) {
                view.setMessage("Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.");
                view.write();
            } else {
                view.setMessage("Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот");
                view.write();
            }
        } else {
            view.setMessage(model.insert(command, connection));
            view.write();
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

