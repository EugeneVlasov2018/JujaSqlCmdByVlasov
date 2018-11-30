package ua.com.juja.controller.command.workWithModel;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Delete extends CommandWithTableInResponce implements Command {
    private ModelInterface model;
    private ViewInterface view;

    public Delete(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("delete");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        String answer = "";
        if (command.length < 4) {
            answer = "Недостаточно данных для запуска команды." +
                    "Недостаточно данных для ее выполнения. Попробуйте еще раз.";

        } else {
            List<String> columnName = new ArrayList();
            List<String> columnValue = new ArrayList<>();
            try {
                columnValue = model.getColumnValuesForUpdateOrDelete(command, connection);
                columnName = model.getColumnNameForUpdateOrDelete(command, connection);
                model.delete(command, connection);
                answer = String.format("Были удалены следующие строки:\n%s", createTable(columnName, columnValue));
            } catch(UnknowTableException a){
                answer = String.format("Ошибка в работе с базой данных. Причина:\n" +
                        "Таблицы '%s' не существует. Переформулируйте запрос", command[1]);
            } catch (UnknowColumnNameException b) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Среди параметров, которые нужно удалить, введено несуществующее имя колонки.\n" +
                        "Переформулируйте запрос.";
            } catch (NullableAnswerException c) {
                answer = "Ошибка в работе с базой данных. Причина:\n" +
                        "Запрошенных данных не существует";
            } catch (NullPointerException d) {
                answer = "Вы попытались удалить информацию из таблицы, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "connect|database|username|password";
            } catch (SQLException e) {
                answer = String.format("Непредвиденная ошибка в работе с базой данных.\n" +
                        "Причина: %s", e.getMessage());
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
