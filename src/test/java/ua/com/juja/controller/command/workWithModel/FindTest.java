package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class FindTest {
    private ModelInterface model;
    private ViewInterface view;
    private Connection connectionToDB;
    private Command find;

    @Before
    public void setup(){
        view = mock(ViewInterface.class);
        model = mock(ModelInterface.class);
        find = new Find(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = find.canProcess(new String[]{"find"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = find.canProcess(new String[]{"fdsfds"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() throws SQLException, UnknowColumnNameException, NullableAnswerException, UnknowTableException {
        String expected = "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+";
        String params[] = new String[]{"find", "users"};

        when(model.getColumnNameForFind(params, connectionToDB)).
                thenReturn(new ArrayList<String>(Arrays.asList("id", "firstname", "secondname", "password")));
        when(model.getColumnValuesForFind(params, connectionToDB)).
                thenReturn(new ArrayList<String>(Arrays.asList("1", "John", "Dou", "123")));
        find.doWork(params, connectionToDB);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] commandWhitoutParameters = new String[]{"find"};
        find.doWork(commandWhitoutParameters, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals("Недостаточно данных для запуска команды." +
                "Укажите имя таблицы, которую собираетесь вывести на екран", captor.getValue());
    }

    @Test
    public void testDoWorkWithSQLException() throws UnknowColumnNameException, NullableAnswerException,
            UnknowTableException {
        String expected = "Непредвиденная ошибка в работе с базой данных.\n" +
                "Причина: null";
        String[] params = new String[]{"find", "users"};
        try {
            when(model.getColumnNameForFind(params, connectionToDB)).
                    thenThrow(new SQLException());
            when(model.getColumnValuesForFind(params, connectionToDB)).
                    thenThrow(new SQLException());
            doThrow(new SQLException()).when(model).delete(params, connectionToDB);
        } catch (SQLException e) {
            //do nothing
        }
        find.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithUnknowTableException() throws SQLException, NullableAnswerException,
            UnknowColumnNameException {
        String[] params = new String[]{"find", "users"};
        String expected = String.format("Ошибка в работе с базой данных. Причина:\n" +
                "Таблицы '%s' не существует. Переформулируйте запрос", params[1]);
        try {
            when(model.getColumnNameForFind(params, connectionToDB)).
                    thenThrow(new UnknowTableException());
            when(model.getColumnValuesForFind(params, connectionToDB)).
                    thenThrow(new UnknowTableException());
            doThrow(new UnknowTableException()).when(model).delete(params, connectionToDB);
        } catch (UnknowTableException e) {
            //do nothing
        }
        find.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithColumnNameException() throws SQLException, NullableAnswerException,
            UnknowTableException {
        String[] params = new String[]{"find", "users"};
        String expected = "Ошибка в работе с базой данных. Причина:\n" +
                "Среди параметров, которые нужно получить, введено несуществующее имя колонки.\n" +
                "Переформулируйте запрос.";
        try {
            when(model.getColumnNameForFind(params, connectionToDB)).
                    thenThrow(new UnknowColumnNameException());
            when(model.getColumnValuesForFind(params, connectionToDB)).
                    thenThrow(new UnknowColumnNameException());
            doThrow(new UnknowColumnNameException()).when(model).delete(params, connectionToDB);
        } catch (UnknowColumnNameException e) {
            //do nothing
        }
        find.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithNullableAnswerException() throws SQLException, UnknowColumnNameException,
            UnknowTableException {
        String[] params = new String[]{"find", "users"};
        String expected = "Ошибка в работе с базой данных. Причина:\n" +
                "Запрошенных данных не существует";
        try {
            when(model.getColumnNameForFind(params, connectionToDB)).
                    thenThrow(new NullableAnswerException());
            when(model.getColumnValuesForFind(params, connectionToDB)).
                    thenThrow(new NullableAnswerException());
            doThrow(new NullableAnswerException()).when(model).delete(params, connectionToDB);
        } catch (NullableAnswerException e) {
            //do nothing
        }
        find.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithNullPointerException() throws SQLException, UnknowColumnNameException,
            UnknowTableException, NullableAnswerException {
        String[] params = new String[]{"delete", "users", "password", "123"};
        String expected = "Вы попытались получить информацию из таблицы, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password";
        try {
            when(model.getColumnNameForFind(params, connectionToDB)).
                    thenThrow(new NullPointerException());
            when(model.getColumnValuesForFind(params, connectionToDB)).
                    thenThrow(new NullPointerException());
            doThrow(new NullPointerException()).when(model).delete(params, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        }
        find.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }


}