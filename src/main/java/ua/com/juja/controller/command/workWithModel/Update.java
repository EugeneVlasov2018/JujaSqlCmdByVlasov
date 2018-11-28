package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;
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
                List<String> columnName = new ArrayList(model.getColumnNameForUpdateOrDelete(command, connection));
                List<String> columnValue = new ArrayList<>(model.getColumnValuesForUpdateOrDelete(command, connection));
                model.update(command, connection);
                answer = createTable(columnName, columnValue);
            } catch (UnknowTableException a) {
                answer = String.format("Ошибка в работе с базой данных. Причина:\n" +
                        "Таблицы %s не существует. Переформулируйте запрос", command[1]);
            } catch (UnknowColumnNameException b) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Среди параметров, которые нужно изменить, введено несуществующее имя колонки.\n" +
                        "Переформулируйте запрос.";
            } catch (NullableAnswerException c) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Запрошенных данных не существует";
            } catch (NullPointerException d) {
                answer = "Вы попытались изменить информацию в таблице, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (SQLException e) {
                answer = String.format("Непредвиденная ошибка в работе с базой данных.\n" +
                        "Причина: %s", e.getMessage());
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
