package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.newExceptions.NullableAnswerException;
import ua.com.juja.model.newExceptions.UnknowColumnNameException;
import ua.com.juja.model.newExceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Update extends CommandWithTableInResponce implements Command {
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
                List<String> ColumnName = new ArrayList(model.getColumnName(command, connection));
                List<String> ColumnValue = new ArrayList<>(model.getColumnValues(command, connection));
                answer = "Были изменены следующие строки:\n" + model.update(command, connection);
            } catch (NullPointerException a) {
                answer = "Вы попытались обновить данные в таблице, не подключившись к базе данных. Сначала подключитесь";
            } catch (UnknowTableException b) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Запрошенных данных не существует";
            } catch (UnknowColumnNameException c) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Среди параметров, которые нужно изменить, введено несуществующее имя колонки.\n" +
                        "Переформулируйте запрос.";
            } catch (NullableAnswerException d) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Запрошенных данных не существует";
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
