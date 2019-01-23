package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.CommandIsWrongException;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Clear extends CommandChekkerAndFormatter implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private Model model;
    private View view;

    public Clear(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("clear");
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        String answer = "";
        try {
            if (commandIsRight(command.length, 2)) {
                try {
                    model.clear(command);
                    logger.debug("метод model.clear() отработал, сформирована строка ответа");
                    answer = "Все данные из таблицы ".concat(command[1]).concat(" были удалены");
                } catch (CreatedInModelException e) {
                    answer = e.getMessage();
                    logger.warn("Из модели прилетело исключение. Месседж исключения добавлен в ответ.\n" +
                            "метод doWork() отработал, процесс вернулся в MainController");
                }
            }
        } catch (CommandIsWrongException e) {
            answer = e.getMessage();
        }
        view.write(answer);
        logger.debug("метод doWork() отработал, ответ выведен в консоль");
    }
}

