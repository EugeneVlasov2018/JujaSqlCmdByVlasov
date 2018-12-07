package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import java.sql.Connection;

public class Connect implements Command {
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
        if (command.length < 4) {
            view.write("Недостаточно данных для запуска команды. Попробуйте еще раз");
        } else {
            try {
                model.connect(command);
                view.write("База успешно подключена");
            } catch (UnknowShitException e) {
                view.write(e.getMessage());
            }
        }
    }
}
