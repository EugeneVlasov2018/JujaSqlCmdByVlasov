package ua.com.juja.model.parentClassesAndInterfaces;

import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

public interface ModelInterface {

    String tables(Connection connection) throws SQLException;

    void clear(String[] params, Connection connection) throws SQLException;

    void drop(String[] params, Connection connection) throws SQLException;

    void create(String[] params, Connection connection) throws SQLException;

    String find(String[] params, Connection connection) throws SQLException;

    void insert(String[] params, Connection connection) throws SQLException;

    String update(String[] params, Connection connection) throws SQLException;

    String delete(String[] params, Connection connection) throws SQLException;
}