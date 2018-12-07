package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.util.List;

public class Tables implements Command {
    private Model model;
    private View view;

    public Tables(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("tables");
    }

    @Override
    public void doWork(String[] command) {
        String answer = "";
        try {
            List<String> resqponseFromDB = model.tables();
            answer = resqponseFromDB.toString();
        } catch (UnknowShitException b) {
            answer = b.getMessage();
        }
        view.write(answer);
    }
}

