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

import static org.junit.Assert.*;

public class DropTest {
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
        drop.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParams() {
        String expected = "Недостаточно данных для запуска команды." +
                "Укажите имя таблицы, которое собираетесь удалить";
        String[] params = new String[]{"drop"};
        drop.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithException() {
        String expected = "Вы попытались удалить несуществующую таблицу.\n" +
                "Введите команду 'tables', чтобы увидеть все созданные таблицы";
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new UnknowShitException("ExpectedMessageFromException")).when(model).drop(commandForWork);
        } catch (UnknowShitException e) {
            //do nothing
        }
        drop.doWork(commandForWork);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("ExpectedMessageFromException", captor.getValue());
    }
}