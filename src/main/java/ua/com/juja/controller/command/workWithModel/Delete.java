package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Delete extends CommandWithTableInResponce implements Command {
    private Model model;
    private View view;

    public Delete(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("delete");
    }

    @Override
    public void doWork(String[] command) {
        String answer = "";
        if (command.length < 4) {
            answer = "Недостаточно данных для запуска команды." +
                    "Недостаточно данных для ее выполнения. Попробуйте еще раз.";

        } else {
            List<String> columnName = new ArrayList();
            List<String> columnValue = new ArrayList<>();
            try {
                columnValue = model.getColumnValuesForUpdateOrDelete(command);
                columnName = model.getColumnNameForUpdateOrDelete(command);
                model.delete(command);
                answer = String.format("Были удалены следующие строки:\n%s", createTable(columnName, columnValue));
            } catch (UnknowShitException a) {
                answer = a.getMessage();
            }
        }
        view.write(answer);
    }
}
