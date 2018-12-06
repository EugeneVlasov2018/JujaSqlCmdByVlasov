package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.Model;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Find extends CommandWithTableInResponce implements Command {
    private Model model;
    private View view;

    public Find(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("find");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        String answer = "";
        if (command.length < 2) {
            answer = "Недостаточно данных для запуска команды." +
                    "Укажите имя таблицы, которую собираетесь вывести на екран";
        } else {
            List<String> columnName = new ArrayList();
            List<String> columnValue = new ArrayList<>();
            try {
                columnName = model.getColumnNameForFind(command, connection);
                columnValue = model.getColumnValuesForFind(command, connection);
                answer = createTable(columnName, columnValue);
            } catch (UnknowTableException a) {
                answer = String.format("Ошибка в работе с базой данных. Причина:\n" +
                        "Таблицы '%s' не существует. Переформулируйте запрос", command[1]);
            } catch (UnknowColumnNameException b) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Среди параметров, которые нужно получить, введено несуществующее имя колонки.\n" +
                        "Переформулируйте запрос.";
            } catch (NullableAnswerException c) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Запрошенных данных не существует";
            } catch (NullPointerException d) {
                answer = "Вы попытались получить информацию из таблицы, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
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
