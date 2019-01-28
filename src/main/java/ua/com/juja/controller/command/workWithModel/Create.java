package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.CommandIsWrongException;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Create extends CommandChekkerAndFormatter implements Command {
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
        String result = "";
        try {
            if (commandIsRight(command.length, 3)) {
                try {
                    model.create(command);
                    result = String.format("Таблица '%s' успешно создана", command[1]);
                    logger.info("cформирована таблица в БД согласно запроса юзера");
                } catch (CreatedInModelException a) {
                    result = a.getMessage();
                }
            }
        } catch (CommandIsWrongException e) {
            result = e.getMessage();
        }
        view.write(result);
        logger.info(String.format("сообщение выведено в консоль:\n%s", result));
    }
}