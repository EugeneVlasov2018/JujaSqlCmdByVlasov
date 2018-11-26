package ua.com.juja.controller.command.workWithModel;

import org.postgresql.util.PSQLException;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewImpl;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

public class Create implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Create(ModelInterface model, ViewInterface view) {
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
        }
        else {
            try {
                model.create(command, connection);
                answer = "Таблица '" + command[1] + "' успешно создана";
            } catch (PSQLException c) {
                answer = "Таблица с таким именем уже существует. Введите команду 'tables'" +
                        "чтобы увидеть существующие таблицы";
            } catch (SQLException a) {
                answer = "Неизвестная ошибка. Обратитесь с возникшей проблемой к разработчику";
            } catch (NullPointerException b) {
                answer = "Вы попытались создать таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            }
            view.setMessage(answer);
            view.write();
        }
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}