package ua.com.juja.model.parentClassesAndInterfaces;

import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public interface ModelInterface {

    String tables(Connection connection);

    String clear(String[] params, Connection connection);

    String drop(String[] params, Connection connection);

    String create(String[] params, Connection connection);

    String find(String[] params, Connection connection);

    String insert(String[] params, Connection connection);

    String update(String[] params, Connection connection);

    String delete(String[] params, Connection connection);
}