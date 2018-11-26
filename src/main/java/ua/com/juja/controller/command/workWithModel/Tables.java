package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

public class Tables implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Tables(ModelInterface model, ViewInterface view) {
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
            answer = model.tables(connection);
        } catch (SQLException a) {
            answer = "Возникли проблемы в методе Tables. " +
                    "Обратитесь к разработчику. Код ошибки: " + a.getSQLState();
        } catch (NullPointerException b) {
            answer = "Вы попытались получить список таблиц, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "connect|database|username|password";
        }


        view.setMessage(answer);
        view.write();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

