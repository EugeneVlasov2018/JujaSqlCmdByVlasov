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

public class InsertTest extends ActualValueGetter {
    private Model model;
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
        assertEquals(expected, getActualValue(insert, view, params));
    }

    @Test
    public void testDoWorkWithoutTableNameAndParams() {
        String expected = "Недостаточно данных для запуска команды." +
                "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
        String[] params = new String[]{"insert"};
        assertEquals(expected, getActualValue(insert, view, params));
    }

    @Test
    public void testDoWorkWithoutParams() {
        String expected = "Ошибка в формате команды." +
                "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
        String[] params = new String[]{"insert", "users", "password", "123", "firstname"};
        assertEquals(expected, getActualValue(insert, view, params));
    }

    @Test
    public void testDoWorkWithException() throws UnknowShitException {
        String params[] = new String[]{"insert", "users", "password", "123", "firstname", "John"};
        doThrow(new UnknowShitException("ExpectedMessageFromException")).when(model).insert(params);
        assertEquals("ExpectedMessageFromException", getActualValue(insert, view, params));
    }
}