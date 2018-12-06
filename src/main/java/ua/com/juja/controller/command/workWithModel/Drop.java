package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

public class Drop implements Command {
    private Model model;
    private View view;

    public Drop(Model model, View view) {
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
            } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
                e.printStackTrace();
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
