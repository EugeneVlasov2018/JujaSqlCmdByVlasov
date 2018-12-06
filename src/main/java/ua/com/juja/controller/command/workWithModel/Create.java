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
            } catch (UnknowTableException a) {
                answer = "Таблица с таким именем уже существует. Введите команду 'tables'" +
                        "чтобы увидеть существующие таблицы";
            } catch (NullPointerException b) {
                answer = "Вы попытались создать таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (UnknowShitException e) {
                e.printStackTrace();
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