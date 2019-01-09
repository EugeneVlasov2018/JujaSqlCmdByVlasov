package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Insert implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private Model model;
    private View view;

    public Insert(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("insert");
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        String answer = "";
        if (command.length < 4 || command.length % 2 != 0) {
            if (command.length < 4) {
                answer = "Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
                logger.debug("Комманда к выполнению корректно не прошла проверку, (недостаточно данных в команде)" +
                        "метод doWork() отработал, процесс вернулся в MainController");
            } else {
                answer = "Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
                logger.debug("Комманда к выполнению корректно не прошла проверку (например имен колонок 3, а значений всего 2) " +
                        "метод doWork() отработал, процесс вернулся в MainController");
            }
        } else {
            try {
                model.insert(command);
                answer = "Все данные успешно добавлены";
                logger.debug("метод model.insert успешно отработал, сформирован ответ пользователю, что все ок");
            } catch (UnknowShitException b){
                answer = b.getMessage();
                logger.warn(String.format("поймано исключение из уровня модели\n" +
                        "текст исключения, выведенный пользователю в консоль:\n%s", b.getMessage()));
            }
        }
        view.write(answer);
    }
}

