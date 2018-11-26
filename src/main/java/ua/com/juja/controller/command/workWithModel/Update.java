package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

public class Update implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Update(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("update");
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
                answer = "Были изменены следующие строки:\n" + model.update(command, connection);
            } catch (NullPointerException c) {
                answer = "Вы попытались обновить данные в таблице, не подключившись к базе данных. Сначала подключитесь";
            } catch (SQLException d) {
                StringBuilder causeOfError = new StringBuilder("Ошибка в работе с базой данных. Причина:\n");
                if (d.getSQLState().equals("42P01")) {
                    causeOfError.append("Таблицы '").append(command[1]).
                            append("' не сущетвует. Переформулируйте запрос");
                } else if (d.getSQLState().equals("02000")) {
                    causeOfError.append("Запрошенных данных не существует");
                } else if (d.getSQLState().equals("42703")) {
                    causeOfError.append("Среди параметров, которые нужно изменить, " +
                            "введено несуществующее имя колонки.\n" +
                            "Переформулируйте запрос.");
                } else {
                    causeOfError.append("Непредвиденная ошибка. Код ошибки, - ").append(d.getSQLState());
                }
                answer = causeOfError.toString();
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
