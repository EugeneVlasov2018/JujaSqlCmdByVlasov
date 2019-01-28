package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.view.View;

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
        String result;
        try {
            model.exit();
            result = "Всего хорошего, до встречи снова))";
            logger.info("отработал model.exit()");
        } catch (CreatedInModelException e) {
            result = e.getMessage();
        }
        view.write(result);
        logger.info(String.format("отработал корректно, инфа в консоли:\n%s", result));
    }
}
