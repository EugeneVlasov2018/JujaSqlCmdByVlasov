package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
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
    public void testDoWork() {
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
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] commandWhitoutParameters = new String[]{"delete"};
        delete.doWork(commandWhitoutParameters, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("Недостаточно данных для запуска команды." +
                "Недостаточно данных для ее выполнения. Попробуйте еще раз.", captor.getValue());
    }

    @Test
    public void testDoWorkWithException() {
        String[] params = new String[]{"delete", "users", "password", "123"};
        try {
            doThrow(new UnknowShitException("ExpectedMessageFromException")).when(model).delete(params, connectionToDB);
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        delete.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("ExpectedMessageFromException", captor.getValue());
    }

    @Test
    public void testDoWorkWithNullPointerException() {
        String[] params = new String[]{"delete", "users", "password", "123"};
        String expected = "Вы попытались удалить информацию из таблицы, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password";
        try {
            doThrow(new NullPointerException()).when(model).delete(params, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        delete.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }
}