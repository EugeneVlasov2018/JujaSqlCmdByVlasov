package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.CommandIsWrongException;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.view.View;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Connect extends CommandChekkerAndFormatter implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private Model model;
    private View view;

    public Connect(View view, Model model) {

        this.view = view;
        this.model = model;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("connect");
    }

    @Override
    public void doWork(String[] command) {
        logger.debug("Запущен метод doWork()");
        String result = "";
        try {
            if (commandIsRight(command.length, 4))
                try {
                    model.connect(command);
                    result = "База успешно подключена";
                    logger.debug("метод doWork() отработал,\n" +
                            "ответ о успешном подключении получен пользователем,\n" +
                            "процесс вернулся в MainController");
                } catch (CreatedInModelException e) {
                    result = e.getMessage();
                    logger.warn(String.format("поймано исключение из уровня модели\n" +
                            "текст исключения:\n%s", e.getMessage()));
                }
        } catch (CommandIsWrongException e) {
            result = e.getMessage();
        }
        view.write(result);
    }
}
