package ua.com.juja.controller.command;

import java.sql.Connection;

public interface Command {
    boolean canProcess(String[]command);

    void doWork(String[] command, Connection connection);

    Connection getConnection();
}
