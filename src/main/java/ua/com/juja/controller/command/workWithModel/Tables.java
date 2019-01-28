package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.util.List;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Tables implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private Model model;
    private View view;

    public Tables(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("tables");
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        String result = "";
        try {
            List<String> resqponseFromDB = model.tables();
            if (resqponseFromDB.size() == 0) {
                result = "В базе данных нет ни одной таблицы";
            } else {
                result = resqponseFromDB.toString();
            }
        } catch (CreatedInModelException b) {
            result = b.getMessage();
            logger.warn(String.format("поймано исключение из уровня модели\n" +
                    "текст исключения, выведенный пользователю в консоль:\n%s", b.getMessage()));
        }
        view.write(result);
        logger.info(String.format("отработал корректно, инфа в консоли:\n%s", result));
    }
}

