package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.newExceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

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
        String answer = "";
        if (command.length < 2) {
            answer = "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которое собираетесь удалить";
        } else {
            try {
                model.drop(command, connection);
                answer = String.format("Таблица %s успешно удалена", command[1]);
            } catch (UnknowTableException a) {
                answer = "Вы попытались удалить несуществующую таблицу.\n" +
                        "Введите команду 'tables', чтобы увидеть все созданные таблицы";
            } catch (NullPointerException b) {
                answer = "Вы попытались удалить таблицу, не подключившись к базе данных.\n" +
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
