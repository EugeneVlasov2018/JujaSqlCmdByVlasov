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

public class Update extends CommandChekkerAndFormatter implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
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
        logger.debug("Запущен метод doWork()");
        String result = "";
        try {
            if (commandIsRight(command.length, 4, 2)) {
                List<String> columnName;
                List<String> columnValue;
                try {
                    columnName = new ArrayList(model.getColumnNameForUpdateOrDelete(command));
                    logger.info("ArrayList имен колонок таблицы заполнен");
                    columnValue = new ArrayList<>(model.getColumnValuesForUpdateOrDelete(command));
                    logger.info("ArrayList запрашиваемых значений таблицы заполнен");
                    model.update(command);
                    result = String.format("Были изменены следующие строки:\n%s", createTable(columnName, columnValue));
                } catch (CreatedInModelException b) {
                    result = b.getMessage();
                    logger.warn(String.format("поймано исключение из уровня модели\n" +
                            "текст исключения, выведенный пользователю в консоль:\n%s", b.getMessage()));
                }
            }
        } catch (CommandIsWrongException e) {
            result = e.getMessage();
        }
        view.write(result);
        logger.info(String.format("отработал корректно, инфа в консоли:\n%s", result));
    }
}
