package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.Model;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DeleteTest {
    private Model model;
    private Connection connectionToDB;
    private Command delete;
    private View view;

    @Before
    public void setup() {
        model = mock(Model.class);
        view = mock(View.class);
        delete = new Delete(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = delete.canProcess(new String[]{"delete"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = delete.canProcess(new String[]{"fdsfds"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() throws SQLException, UnknowColumnNameException, NullableAnswerException,
            UnknowTableException {
        String expected = "Были удалены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+";
        String[] params = new String[]{"delete", "users", "password", "123"};
        try {
            when(model.getColumnNameForUpdateOrDelete(params, connectionToDB)).
                    thenReturn(new ArrayList<String>(Arrays.asList("id", "firstname", "secondname", "password")));
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }
        try {
            when(model.getColumnValuesForUpdateOrDelete(params, connectionToDB)).
                    thenReturn(new ArrayList<String>(Arrays.asList("1", "John", "Dou", "123")));
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }

        delete.doWork(params, connectionToDB);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] commandWhitoutParameters = new String[]{"delete"};
        delete.doWork(commandWhitoutParameters, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("Недостаточно данных для запуска команды." +
                "Недостаточно данных для ее выполнения. Попробуйте еще раз.", captor.getValue());
    }

    @Test
    public void testDoWorkWithSQLException() throws UnknowColumnNameException, NullableAnswerException,
            UnknowTableException {
        String expected = "Непредвиденная ошибка в работе с базой данных.\n" +
                "Причина: null";
        String[] params = new String[]{"delete", "users", "password", "123"};
        try {
            when(model.getColumnNameForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new SQLException());
            when(model.getColumnValuesForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new SQLException());
            doThrow(new SQLException()).when(model).delete(params, connectionToDB);
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }
        delete.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithUnknowTableException() throws SQLException, NullableAnswerException,
            UnknowColumnNameException {
        String[] params = new String[]{"delete", "users", "password", "123"};
        String expected = String.format("Ошибка в работе с базой данных. Причина:\n" +
                "Таблицы '%s' не существует. Переформулируйте запрос", params[1]);
        try {
            when(model.getColumnNameForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new UnknowTableException());
            when(model.getColumnValuesForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new UnknowTableException());
            doThrow(new UnknowTableException()).when(model).delete(params, connectionToDB);
        } catch (UnknowTableException e) {
            //do nothing
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }
        delete.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithColumnNameException() throws SQLException, NullableAnswerException,
            UnknowTableException {
        String[] params = new String[]{"delete", "users", "password", "123"};
        String expected = "Ошибка в работе с базой данных. Причина:\n" +
                "Среди параметров, которые нужно удалить, введено несуществующее имя колонки.\n" +
                "Переформулируйте запрос.";
        try {
            when(model.getColumnNameForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new UnknowColumnNameException());
            when(model.getColumnValuesForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new UnknowColumnNameException());
            doThrow(new UnknowColumnNameException()).when(model).delete(params, connectionToDB);
        } catch (UnknowColumnNameException e) {
            //do nothing
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }
        delete.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithNullableAnswerException() throws SQLException, UnknowColumnNameException,
            UnknowTableException {
        String[] params = new String[]{"delete", "users", "password", "123"};
        String expected = "Ошибка в работе с базой данных. Причина:\n" +
                "Запрошенных данных не существует";
        try {
            when(model.getColumnNameForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new NullableAnswerException());
            when(model.getColumnValuesForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new NullableAnswerException());
            doThrow(new NullableAnswerException()).when(model).delete(params, connectionToDB);
        } catch (NullableAnswerException e) {
            //do nothing
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }
        delete.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithNullPointerException() throws SQLException, UnknowColumnNameException,
            UnknowTableException, NullableAnswerException {
        String[] params = new String[]{"delete", "users", "password", "123"};
        String expected = "Вы попытались удалить информацию из таблицы, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password";
        try {
            when(model.getColumnNameForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new NullPointerException());
            when(model.getColumnValuesForUpdateOrDelete(params, connectionToDB)).
                    thenThrow(new NullPointerException());
            doThrow(new NullPointerException()).when(model).delete(params, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }
        delete.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }
}