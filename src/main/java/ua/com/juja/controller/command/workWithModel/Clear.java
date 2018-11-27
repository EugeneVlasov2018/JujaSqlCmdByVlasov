package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.newExceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

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
        String answer = "";
        if(command.length<2){
            answer = "Недостаточно данных для запуска команды. " +
                    "Укажите имя таблицы, которое собираетесь очистить";
        } else {
            try {
                model.clear(command, connection);
                answer = "Все данные из таблицы ".concat(command[1]).concat(" были удалены");
            } catch (UnknowTableException a) {
                answer = "Вы пытаетесь очистить несуществующую таблицу.\n" +
                        "Вызовите команду 'tables', чтобы увидеть, какие таблицы есть в базе данных";
            } catch (NullPointerException b) {
                answer = "Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "'connect|database|username|password'";
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

