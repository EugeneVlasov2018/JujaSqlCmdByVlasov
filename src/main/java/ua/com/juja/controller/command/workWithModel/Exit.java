package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.SystemExitException;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import java.sql.Connection;

public class Exit implements Command {
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
        try {
            model.exit();
            view.write("Всего хорошего, до встречи снова))");
        } catch (UnknowShitException e) {
            view.write(e.getMessage());
        }
    }
}
