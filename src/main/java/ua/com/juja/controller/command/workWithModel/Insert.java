package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.CommandIsWrongException;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Insert extends CommandChekkerAndFormatter implements Command {
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
        String result = "";
        try {
            if (commandIsRight(command.length, 4, 2)) {
                try {
                    model.insert(command);
                    result = "Все данные успешно добавлены";
                    logger.info("метод model.insert успешно отработал, сформирован ответ пользователю, что все ок");
                } catch (CreatedInModelException b) {
                    result = b.getMessage();
                }
            }
        } catch (CommandIsWrongException e) {
            result = e.getMessage();
        }
        view.write(result);
        logger.info(String.format("отработал корректно, инфа в консоли:\n%s", result));
    }
}

