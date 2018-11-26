package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

public class Delete implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Delete(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("delete");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        String answer = "";
        if (command.length < 4) {
            answer = "Недостаточно данных для запуска команды." +
                    "Недостаточно данных для ее выполнения. Попробуйте еще раз.";

        } else {
            try {
                answer = model.delete(command, connection);
            } catch (SQLException a) {
                StringBuilder causeOfError = new StringBuilder("Ошибка в работе с базой данных. Причина:\n");
                if (a.getSQLState().equals("42P01")) {
                    causeOfError.append("Таблицы '").append(command[1]).append("' не сущетвует. Переформулируйте запрос");
                } else if (a.getSQLState().equals("02000")) {
                    causeOfError.append("Запрошенных данных не существует");
                } else if (a.getSQLState().equals("42703")) {
                    causeOfError.append("Среди параметров, которые нужно изменить, введено несуществующее имя колонки.\n" +
                            "Переформулируйте запрос.");
                } else {
                    causeOfError.append("Непредвиденная ошибка. Код ошибки, - ").append(a.getSQLState());
                }
                answer = causeOfError.toString();
            } catch (NullPointerException e1) {
                answer = "Вы попытались удалить информацию из таблицы, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
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
