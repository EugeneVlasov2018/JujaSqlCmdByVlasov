package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.newExceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String answer = "";
        if (command.length < 2) {
            answer = "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которую собираетесь вывести на екран";
        } else {
            try {
                List<List<String>>responseFromDB=new ArrayList<>();
                answer = model.find(command, connection);
            } catch (UnknowTableException e) {
                answer = "такой таблицы не существует";
            } catch (NullPointerException e1) {
                answer = "Вы попытались найти таблицу, не подключившись к базе данных. Сначала подключитесь";
            }
        }
        view.setMessage(answer);
            view.write();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
