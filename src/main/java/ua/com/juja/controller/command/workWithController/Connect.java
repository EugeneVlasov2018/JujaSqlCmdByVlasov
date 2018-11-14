package ua.com.juja.controller.command.workWithController;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect implements Command {
    private ViewInterface view;
    private Connection connectionToDatabase;

    public Connection getConnection() {
        return connectionToDatabase;
    }

    public Connect(ViewInterface view) {
        this.view = view;
    }
    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("connect");
    }

    @Override
    public void doWork(String[] command, Connection connection) {
        if(command.length<4){
            view.setMessage("Недостаточно данных для запуска команды. Попробуйте еще раз");
            view.write();
        }
        else {
            String url = "jdbc:postgresql://localhost:5432/" + command[1];
            String user = command[2];
            String password = command[3];
            String jdbcDriver = "org.postgresql.Driver";
            connectionToDatabase = returnConnection(url, user, password, jdbcDriver);
        }
    }

    private Connection returnConnection(String url, String user, String password, String jdbcDriver) {

        try {
            Class.forName(jdbcDriver);
            connectionToDatabase = DriverManager.getConnection(url, user, password);
            view.setMessage("База успешно подключена");
            view.write();
            return connectionToDatabase;
        } catch (SQLException e) {
            StringBuilder resultForView = new StringBuilder("Вы ввели: ");
            if (!(url.equalsIgnoreCase("jdbc:postgresql://localhost:5432/jujasqlcmd")))
                resultForView.append("\nНеверную ссылку на базу");
            if (!(user.equalsIgnoreCase("postgres")))
                resultForView.append("\nНеверное имя пользователя");
            if (!(password.equalsIgnoreCase("root")))
                resultForView.append("\nНеверный пароль");
            else {//donothing
            }
            resultForView.append("\nПопробуйте снова:P");
            view.setMessage(resultForView.toString());
            view.write();
        } catch (ClassNotFoundException e) {
            view.setMessage("Не найден драйвер подключения к базе\n" +
                    "Передайте разработчику, чтобы добавил либо джарник в либы, либо депенденс в мавен");
            view.write();
        }
        return null;
    }
}
