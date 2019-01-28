package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.CommandIsWrongException;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.util.List;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Delete extends CommandChekkerAndFormatter implements Command {
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
        String result = "";
        try {
            if (commandIsRight(command.length, 4)) {
                List<String> columnName;
                List<String> columnValue;
                try {
                    columnName = model.getColumnNameForUpdateOrDelete(command);
                    logger.info("ArrayList имен колонок таблицы заполнен");
                    columnValue = model.getColumnValuesForUpdateOrDelete(command);
                    logger.info("ArrayList значений колонок таблицы заполнен");
                    model.delete(command);
                    result = String.format("Были удалены следующие строки:\n%s", createTable(columnName, columnValue));
                } catch (CreatedInModelException a) {
                    result = a.getMessage();
                }
            }
        } catch (CommandIsWrongException e) {
            result = e.getMessage();
        }
        view.write(result);
        logger.info(String.format("Юзер получил результат работы метода.\n" +
                "Текст сообщения: %s", result));
    }
}
