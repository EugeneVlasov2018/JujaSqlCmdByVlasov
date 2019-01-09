package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.SystemExitException;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import java.sql.Connection;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Exit implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private View view;
    private Model model;

    public Exit(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("exit");
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        try {
            model.exit();
            logger.debug("отработал model.exit()");
            view.write("Всего хорошего, до встречи снова))");
        } catch (UnknowShitException e) {
            logger.warn(String.format("поймано исключение из уровня модели\n" +
                    "текст исключения, выведенный пользователю в консоль:\n%s", e.getMessage()));
            view.write(e.getMessage());
        }
    }
}
