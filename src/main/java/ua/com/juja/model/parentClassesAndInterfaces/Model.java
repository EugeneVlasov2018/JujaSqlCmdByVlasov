package ua.com.juja.model.parentClassesAndInterfaces;

import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.exceptions.UnknowTableException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Model {

    void insert(String[] params, Connection connection) throws NullPointerException, UnknowShitException;

    void update(String[] params, Connection connection) throws NullPointerException, UnknowShitException;

    void delete(String[] params, Connection connection) throws NullPointerException, UnknowShitException;

    void clear(String[] params, Connection connection) throws NullPointerException, UnknowShitException;

    void drop(String[] params, Connection connection) throws NullPointerException, UnknowShitException;

    void create(String[] params, Connection connection) throws NullPointerException, UnknowShitException;

    void workWithDbWithoutAnswer(Connection connectionToDb, String sqlRequest) throws UnknowShitException,
            NullPointerException;

    List<String> tables(Connection connection) throws UnknowShitException, NullPointerException;

    List<String> getColumnNameForFind(String[] command, Connection connectionToDatabase)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnValuesForFind(String[] command, Connection connectionToDatabase)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnNameForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnValuesForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnNamesFromDB(String responceToDB, Connection connectionToDatabase)
            throws NullPointerException, SQLException;

    List<String> getColumnValuesFromDB(String responceToDB, Connection connectionToDatabase)
            throws NullPointerException, UnknowShitException;

}