package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Update extends CommandWithTableInResponce implements Command {
    private Model model;
    private View view;

    public Update(Model model, View view) {
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
                answer = String.format("Были изменены следующие строки:\n%s", createTable(columnName, columnValue));
            } catch (NullPointerException a) {
                answer = "Вы попытались изменить информацию в таблице, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (UnknowShitException b) {
                answer = b.getMessage();
            }
        }
        view.write(answer);
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
