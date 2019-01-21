package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class DropTest extends ActualValueGetter {
    private Model model;
    private Command drop;
    private View view;

    @Before
    public void setup() {
        model = mock(Model.class);
        view = mock(View.class);
        drop = new Drop(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = drop.canProcess(new String[]{"drop"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = drop.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String expected = "Таблица users успешно удалена";
        String[] params = new String[]{"drop", "users"};
        assertEquals(expected, getActualValue(drop, view, params));
    }

    @Test
    public void testDoWorkWhitoutParams() {
        String expected = "Недостаточно данных для запуска команды." +
                "Укажите имя таблицы, которое собираетесь удалить";
        String[] params = new String[]{"drop"};
        assertEquals(expected, getActualValue(drop, view, params));
    }

    @Test
    public void testDoWorkWithException() throws CreatedInModelException {
        String expected = "Вы попытались удалить несуществующую таблицу.\n" +
                "Введите команду 'tables', чтобы увидеть все созданные таблицы";
        String[] params = new String[]{"clear", "user"};
        doThrow(new CreatedInModelException("ExpectedMessageFromException")).when(model).drop(params);
        assertEquals("ExpectedMessageFromException", getActualValue(drop, view, params));
    }
}