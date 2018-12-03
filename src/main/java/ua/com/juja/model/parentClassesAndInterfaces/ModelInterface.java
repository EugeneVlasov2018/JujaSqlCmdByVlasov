package ua.com.juja.model.parentClassesAndInterfaces;

import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ModelInterface {

    void insert(String[] params, Connection connection) throws UnknowTableException, UnknowColumnNameException,
            NullPointerException;

    void update(String[] params, Connection connection) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException, NullPointerException;

    void delete(String[] params, Connection connection) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException, NullPointerException, SQLException;

    void clear(String[] params, Connection connection) throws UnknowTableException, NullPointerException, SQLException;

    void drop(String[] params, Connection connection) throws UnknowTableException, NullPointerException, SQLException;

    void create(String[] params, Connection connection) throws UnknowTableException, NullPointerException, SQLException;

    void workWithDbWithoutAnswer(Connection connectionToDb, String sqlRequest) throws SQLException,
            NullPointerException;

    ArrayList<String> tables(Connection connection) throws SQLException, NullPointerException, NullableAnswerException;

    ArrayList<String> getColumnNameForFind(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    ArrayList<String> getColumnValuesForFind(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    ArrayList<String> getColumnNameForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    ArrayList<String> getColumnValuesForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    ArrayList<String> getColumnNamesFromDB(String responceToDB, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    ArrayList<String> getColumnValuesFromDB(String responceToDB, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

}