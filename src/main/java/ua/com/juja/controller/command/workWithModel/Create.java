package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Create implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private Model model;
    private View view;

    public Create(Model model, View view) {
        this.model = model;
        this.view = view;
    }
    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("create");
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        String answer = "";
        if(command.length<3){
            answer = "Недостаточно данных для запуска команды. Попробуйте еще раз";
            logger.debug("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
        } else {
            try {
                model.create(command);
                answer = String.format("Таблица '%s' успешно создана",command[1]);
                logger.debug("cформирована таблица в БД согласно запроса юзера");
            } catch (UnknowShitException a) {
                answer = a.getMessage();
                logger.warn(String.format("поймано исключение из уровня модели\n" +
                        "текст исключения:\n%s", a.getMessage()));
            }
        }
        view.write(answer);
        logger.debug(String.format("сообщение выведено в консоль:\n%s", answer));
    }
}