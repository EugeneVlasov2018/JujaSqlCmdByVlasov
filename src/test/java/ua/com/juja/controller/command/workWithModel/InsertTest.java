package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

import static org.junit.Assert.*;

public class InsertTest {
    private Model model;
    private Connection connection;
    private Command insert;
    private View view;

    @Before
    public void setup() {

        model = mock(Model.class);
        view = mock(View.class);
        insert = new Insert(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = insert.canProcess(new String[]{"insert"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = insert.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String expected = "Все данные успешно добавлены";
        String[] params = new String[]{"insert", "users", "password", "123"};
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutTableNameAndParams() {
        String expected = "Недостаточно данных для запуска команды." +
                "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
        String[] params = new String[]{"insert"};
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutParams() {
        String expected = "Ошибка в формате команды." +
                "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
        String[] params = new String[]{"insert", "users", "password", "123", "firstname"};
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithNullPointerException() {
        String expected = "Вы попытались вставить информацию в таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password";
        String params[] = new String[]{"insert", "users", "password", "123", "firstname", "John"};
        try {
            doThrow(new NullPointerException()).when(model).insert(params,connection);
        } catch (UnknowShitException e) {
            //doNothing
        }
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithException() {
        String params[] = new String[]{"insert", "users", "password", "123", "firstname", "John"};
        try {
            doThrow(new UnknowShitException("ExpectedMessageFromException")).when(model).insert(params,connection);
        } catch (UnknowShitException e) {
            //do nothing
        }
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("ExpectedMessageFromException", captor.getValue());
    }
}