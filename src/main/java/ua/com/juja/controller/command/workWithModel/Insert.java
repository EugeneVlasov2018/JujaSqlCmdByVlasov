package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

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
        String answer = "";
        if (command.length < 4 || command.length % 2 != 0) {
            if (command.length < 4) {
                answer = "Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
            } else {
                answer = "Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
            }
        } else {
            try {
                model.insert(command, connection);
                answer = "Все данные успешно добавлены";
            } catch (NullPointerException a) {
                answer = "Вы попытались вставить информацию в таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (SQLException b) {
                StringBuilder causeOfError = new StringBuilder("Ошибка в работе с базой данных. Причина:\n");
                if (b.getSQLState().equals("42P01")) {
                    causeOfError.append("Таблицы '").append(command[1]).append("' не сущетвует. Переформулируйте запрос");
                } else if (b.getSQLState().equals("42703")) {
                    causeOfError.append("Среди параметров, которые нужно ввести, введено несуществующее имя колонки.\n" +
                            "Переформулируйте запрос.");
                } else {
                    causeOfError.append("Непредвиденная ошибка. Код ошибки, - ").append(b.getSQLState());
                }
                answer = causeOfError.toString();
            }
            view.setMessage(answer);
            view.write();
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

