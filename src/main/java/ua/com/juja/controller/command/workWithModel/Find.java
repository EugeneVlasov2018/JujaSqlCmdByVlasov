package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.CommandIsWrongException;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.util.ArrayList;
import java.util.List;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Find extends CommandChekkerAndFormatter implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
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
        logger.debug("Запущен метод doWork()");
        String result = "";
        try {
            if (commandIsRight(command.length, 2)) {
                List<String> columnName;
                List<String> columnValue;
                try {
                    columnName = model.getColumnNameForFind(command);
                    logger.info("ArrayList имен колонок таблицы заполнен");
                    columnValue = model.getColumnValuesForFind(command);
                    logger.info("ArrayList запрашиваемых значений таблицы заполнен");
                    result = createTable(columnName, columnValue);
                    logger.info("таблица ответа создана и готова к выводу в консоль");
                } catch (CreatedInModelException a) {
                    result = a.getMessage();
                }
            }
        } catch (CommandIsWrongException e) {
            result = e.getMessage();
        }
        view.write(result);
        logger.info(String.format("отработал корректно, инфа в консоли:\n%s", result));
    }
}
