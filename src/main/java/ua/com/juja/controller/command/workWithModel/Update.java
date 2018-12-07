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
    public void doWork(String[] command) {
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
                List<String> columnName = new ArrayList(model.getColumnNameForUpdateOrDelete(command));
                List<String> columnValue = new ArrayList<>(model.getColumnValuesForUpdateOrDelete(command));
                model.update(command);
                answer = String.format("Были изменены следующие строки:\n%s", createTable(columnName, columnValue));
            } catch (UnknowShitException b) {
                answer = b.getMessage();
            }
        }
        view.write(answer);
    }
}
