package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Clear implements Command {
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
        if(command.length<2){
            answer = "Недостаточно данных для запуска команды. " +
                    "Укажите имя таблицы, которое собираетесь очистить";
            logger.debug("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
        } else {
            try {
                model.clear(command);
                logger.debug("метод model.clear() отработал, сформирована строка ответа");
                answer = "Все данные из таблицы ".concat(command[1]).concat(" были удалены");
            } catch (UnknowShitException e) {
                answer = e.getMessage();
                logger.warn("Из модели прилетело исключение. Месседж исключения добавлен в ответ.\n" +
                        "метод doWork() отработал, процесс вернулся в MainController");
            }
        }
        view.write(answer);
        logger.debug("метод doWork() отработал, ответ выведен в консоль");
    }
}

