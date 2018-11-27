package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.newExceptions.NullableAnswerException;
import ua.com.juja.model.newExceptions.UnknowColumnNameException;
import ua.com.juja.model.newExceptions.UnknowTableException;
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
            } catch(UnknowTableException a){
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Среди параметров, которые нужно изменить, введено несуществующее имя колонки.\n" +
                        "Переформулируйте запрос.";
            } catch(UnknowColumnNameException b) {
                answer = String.format("Ошибка в работе с базой данных. Причина:\n" +
                        "Таблицы %s не существует. Переформулируйте запрос", command[1]);
            } catch (NullableAnswerException c) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Запрошенных данных не существует";
            } catch (NullPointerException d) {
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
