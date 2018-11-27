package ua.com.juja.model.parentClassesAndInterfaces;

import ua.com.juja.model.newExceptions.NullableAnswerException;
import ua.com.juja.model.newExceptions.UnknowColumnNameException;
import ua.com.juja.model.newExceptions.UnknowTableException;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ModelInterface {

    ArrayList<String> tables(Connection connection) throws SQLException, NullPointerException, NullableAnswerException;

    void clear(String[] params, Connection connection) throws UnknowTableException, NullPointerException;

    void drop(String[] params, Connection connection) throws UnknowTableException, NullPointerException;

    void create(String[] params, Connection connection) throws UnknowTableException, NullPointerException;

    String find(String[] params, Connection connection) throws UnknowTableException, NullPointerException;

    void insert(String[] params, Connection connection) throws UnknowTableException, UnknowColumnNameException,
            NullPointerException;

    String update(String[] params, Connection connection) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException, NullPointerException;

    String delete(String[] params, Connection connection) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException, NullPointerException;
}