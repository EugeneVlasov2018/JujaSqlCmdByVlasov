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

public class Find extends CommandWithTableInResponce implements Command {
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
        String answer = "";
        if (command.length < 2) {
            answer = "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которую собираетесь вывести на екран";
            logger.debug("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
        } else {
            List<String> columnName = new ArrayList();
            List<String> columnValue = new ArrayList<>();
            try {
                columnName = model.getColumnNameForFind(command);
                logger.debug("ArrayList имен колонок таблицы заполнен");
                columnValue = model.getColumnValuesForFind(command);
                logger.debug("ArrayList запрашиваемых значений таблицы заполнен");
                answer = createTable(columnName, columnValue);
                logger.debug("таблица ответа создана и готова к выводу в консоль");
            } catch (UnknowShitException a) {
                answer = a.getMessage();
                logger.warn(String.format("поймано исключение из уровня модели\n" +
                        "текст исключения, выведенный пользователю в консоль:\n%s", a.getMessage()));
            }
        }
        view.write(answer);
    }
}
