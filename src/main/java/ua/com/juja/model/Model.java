package ua.com.juja.model;

import ua.com.juja.model.exceptions.CreatedInModelException;

import java.util.List;

public interface Model {

    void exit() throws CreatedInModelException;

    void insert(String[] params) throws CreatedInModelException;

    void update(String[] params) throws CreatedInModelException;

    void delete(String[] params) throws CreatedInModelException;

    void clear(String[] params) throws CreatedInModelException;

    void drop(String[] params) throws CreatedInModelException;

    void create(String[] params) throws CreatedInModelException;

    List<String> tables() throws CreatedInModelException;

    List<String> getColumnNameForFind(String[] command) throws CreatedInModelException;

    List<String> getColumnValuesForFind(String[] command) throws CreatedInModelException;

    List<String> getColumnNameForUpdateOrDelete(String[] command) throws CreatedInModelException;

    List<String> getColumnValuesForUpdateOrDelete(String[] command) throws CreatedInModelException;

    void connect(String[] responceToDb) throws CreatedInModelException;
}