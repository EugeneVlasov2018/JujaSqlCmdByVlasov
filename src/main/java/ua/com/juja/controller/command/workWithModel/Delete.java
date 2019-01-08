package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Delete extends CommandWithTableInResponce implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
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
                columnName = model.getColumnNameForUpdateOrDelete(command);
                columnValue = model.getColumnValuesForUpdateOrDelete(command);
                model.delete(command);
                answer = String.format("Были удалены следующие строки:\n%s", createTable(columnName, columnValue));
            } catch (UnknowShitException a) {
                answer = a.getMessage();
            }
        }
        view.write(answer);
    }
}
