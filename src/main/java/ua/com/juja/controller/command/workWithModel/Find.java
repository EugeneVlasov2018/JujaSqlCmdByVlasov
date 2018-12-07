package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Find extends CommandWithTableInResponce implements Command {
    private Model model;
    private View view;

    public Find(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("find");
    }

    @Override
    public void doWork(String[] command) {
        String answer = "";
        if (command.length < 2) {
            answer = "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которую собираетесь вывести на екран";
        } else {
            List<String> columnName = new ArrayList();
            List<String> columnValue = new ArrayList<>();
            try {
                columnName = model.getColumnNameForFind(command);
                columnValue = model.getColumnValuesForFind(command);
                answer = createTable(columnName, columnValue);
            } catch (UnknowShitException a) {
                answer = a.getMessage();
            } catch (NullPointerException b) {
                answer = "Вы попытались получить информацию из таблицы, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            }
        }
        view.write(answer);
    }
}
