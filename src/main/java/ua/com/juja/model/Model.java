package ua.com.juja.model;

import ua.com.juja.model.exceptions.UnknowShitException;

import java.sql.Connection;
import java.util.List;

public interface Model {

    void insert(String[] params) throws NullPointerException, UnknowShitException;

    void update(String[] params) throws NullPointerException, UnknowShitException;

    void delete(String[] params) throws NullPointerException, UnknowShitException;

    void clear(String[] params) throws NullPointerException, UnknowShitException;

    void drop(String[] params) throws NullPointerException, UnknowShitException;

    void create(String[] params) throws NullPointerException, UnknowShitException;

    void workWithDbWithoutAnswer(String sqlRequest) throws UnknowShitException,
            NullPointerException;

    List<String> tables() throws UnknowShitException, NullPointerException;

    List<String> getColumnNameForFind(String[] command)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnValuesForFind(String[] command)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnNameForUpdateOrDelete(String[] command)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnValuesForUpdateOrDelete(String[] command)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnNamesFromDB(String responceToDB)
            throws NullPointerException, UnknowShitException;

    List<String> getColumnValuesFromDB(String responceToDB)
            throws NullPointerException, UnknowShitException;

    void connect(String[] responceToDb) throws UnknowShitException;
}