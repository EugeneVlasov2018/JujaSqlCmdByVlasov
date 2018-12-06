package ua.com.juja.model;

import ua.com.juja.model.exceptions.UnknowShitException;

import java.sql.Connection;
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
            throws NullPointerException, UnknowShitException;

    List<String> getColumnValuesFromDB(String responceToDB, Connection connectionToDatabase)
            throws NullPointerException, UnknowShitException;

}