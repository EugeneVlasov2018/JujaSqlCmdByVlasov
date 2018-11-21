package ua.com.juja.model.parentClassesAndInterfaces;

import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

public interface ModelInterface {

    ViewInterface getView();

    void tables(Connection connection);

    void clear(String[] params, Connection connection);

    void drop(String[] params, Connection connection);

    void create(String[] params, Connection connection);

    void find(String[] params, Connection connection);

    void insert(String[] params, Connection connection);

    void update(String[] params, Connection connection);

    void delete(String[] params, Connection connection);
}