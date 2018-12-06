package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

public class Create implements Command {
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
    public void doWork(String[] command, Connection connection) {
        String answer = "";
        if(command.length<3){
            answer = "Недостаточно данных для запуска команды. Попробуйте еще раз";
        } else {
            try {
                model.create(command, connection);
                answer = String.format("Таблица '%s' успешно создана",command[1]);
            } catch (UnknowShitException a) {
                answer = String.format("Ошибка в работе с базой данных. Причина: %s",a.getMessage());
            } catch (NullPointerException b) {
                answer = "Вы попытались создать таблицу, не подключившись к БД.\n" +
                        "Сначала подключитесь командой 'connect' или вызовите команду 'help'";
            }
        }
            view.setMessage(answer);
            view.write();
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}