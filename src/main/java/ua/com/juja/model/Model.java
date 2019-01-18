package ua.com.juja.model;

import ua.com.juja.model.exceptions.UnknowShitException;

import java.sql.Connection;
import java.util.List;

public interface Model {

    void exit() throws UnknowShitException;

    void insert(String[] params) throws UnknowShitException;

    void update(String[] params) throws UnknowShitException;

    void delete(String[] params) throws UnknowShitException;

    void clear(String[] params) throws UnknowShitException;

    void drop(String[] params) throws UnknowShitException;

    void create(String[] params) throws UnknowShitException;

    //void workWithDbWithoutAnswer(String sqlRequest) throws UnknowShitException;

    List<String> tables() throws UnknowShitException;

    List<String> getColumnNameForFind(String[] command) throws UnknowShitException;

    List<String> getColumnValuesForFind(String[] command) throws UnknowShitException;

    List<String> getColumnNameForUpdateOrDelete(String[] command) throws UnknowShitException;

    List<String> getColumnValuesForUpdateOrDelete(String[] command) throws UnknowShitException;

    //List<String> getColumnNamesFromDB(String responceToDB) throws UnknowShitException;

    //List<String> getColumnValuesFromDB(String responceToDB) throws UnknowShitException;

    void connect(String[] responceToDb) throws UnknowShitException;
}