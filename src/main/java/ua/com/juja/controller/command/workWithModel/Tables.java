package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.parentClassesAndInterfaces.Model;
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
    public void doWork(String[] command, Connection connection) {
        String answer = "";
        try {
            List<String> resqponseFromDB = model.tables(connection);
            answer = resqponseFromDB.toString();
        } catch (NullPointerException b) {
            answer = "Вы попытались получить список таблиц, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password";
        } catch (NullableAnswerException c){
            answer = "В базе данных нет ни одной таблицы";
        } catch (UnknowShitException e) {
            answer = "обратитесь к разработчику с жалобой";
        }
        view.setMessage(answer);
        view.write();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

