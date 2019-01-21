package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Drop implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private Model model;
    private View view;

    public Drop(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("drop");
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        String answer = "";
        if (command.length < 2) {
            answer = "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которое собираетесь удалить";
            logger.debug("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
        } else {
            try {
                model.drop(command);
                logger.debug("model.drop отработал, процесс вернулся в MainController");
                answer = String.format("Таблица %s успешно удалена", command[1]);
            } catch (CreatedInModelException a) {
                answer = a.getMessage();
                logger.warn(String.format("поймано исключение из уровня модели\n" +
                        "текст исключения, выведенный пользователю в консоль:\n%s", a.getMessage()));
            }
        }
        view.write(answer);
    }
}
