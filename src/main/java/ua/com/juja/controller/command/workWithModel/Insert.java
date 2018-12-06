package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

public class Insert implements Command {
    private Model model;
    private View view;

    public Insert(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("insert");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        String answer = "";
        if (command.length < 4 || command.length % 2 != 0) {
            if (command.length < 4) {
                answer = "Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
            } else {
                answer = "Ошибка в формате команды." +
                        "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
            }
        } else {
            try {
                model.insert(command, connection);
                answer = "Все данные успешно добавлены";
            } catch (NullPointerException a) {
                answer = "Вы попытались вставить информацию в таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (UnknowTableException b){
                answer = String.format("Ошибка в работе с базой данных. Причина:\n" +
                        "Таблицы '%s' не сущетвует. Переформулируйте запрос", command[1]);
            } catch (UnknowColumnNameException c){
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Среди параметров, которые нужно ввести, введено несуществующее имя колонки.\n" +
                        "Переформулируйте запрос.";
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

