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
        logger.debug("Запущен метод doWork()");
        String answer = "";
        if (command.length < 4) {
            answer = "Недостаточно данных для запуска команды." +
                    "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
            logger.debug("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");

        } else {
            List<String> columnName = new ArrayList();
            List<String> columnValue = new ArrayList<>();
            try {
                columnName = model.getColumnNameForUpdateOrDelete(command);
                logger.debug("ArrayList имен колонок таблицы заполнен");
                columnValue = model.getColumnValuesForUpdateOrDelete(command);
                logger.debug("ArrayList значений колонок таблицы заполнен");
                model.delete(command);
                logger.debug("model.delete успешно отработал");
                answer = String.format("Были удалены следующие строки:\n%s", createTable(columnName, columnValue));
            } catch (UnknowShitException a) {
                answer = a.getMessage();
                logger.warn(String.format("поймано исключение из уровня модели\n" +
                        "текст исключения, выведенный пользователю в консоль:\n%s", a.getMessage()));
            }
        }
        view.write(answer);
    }
}
