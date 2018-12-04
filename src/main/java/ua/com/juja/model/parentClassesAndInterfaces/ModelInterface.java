package ua.com.juja.model.parentClassesAndInterfaces;

import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    List<String> tables(Connection connection) throws SQLException, NullPointerException, NullableAnswerException;

    List<String> getColumnNameForFind(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    List<String> getColumnValuesForFind(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    List<String> getColumnNameForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    List<String> getColumnValuesForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    List<String> getColumnNamesFromDB(String responceToDB, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

    List<String> getColumnValuesFromDB(String responceToDB, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException;

}