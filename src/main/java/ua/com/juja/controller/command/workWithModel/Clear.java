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
        String result = "";
        try {
            if (commandIsRight(command.length, 2)) {
                try {
                    model.clear(command);
                    result = "Все данные из таблицы ".concat(command[1]).concat(" были удалены");
                } catch (CreatedInModelException e) {
                    result = e.getMessage();
                }
            }
        } catch (CommandIsWrongException e) {
            result = e.getMessage();
        }
        view.write(result);
        logger.info(String.format("отработал корректно, инфа в консоли:\n%s", result));
    }
}

