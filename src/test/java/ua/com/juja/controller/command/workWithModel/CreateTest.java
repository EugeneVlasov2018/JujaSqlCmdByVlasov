package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CreateTest {
    private Model model;
    private Command create;
    private View view;

    @Before
    public void setup() {
        model = mock(Model.class);
        view = mock(View.class);
        create = new Create(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = create.canProcess(new String[]{"create"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = create.canProcess(new String[]{"fdsfds"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String expected = "Таблица 'users' успешно создана";
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        create.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutParams() {
        String expected = "Недостаточно данных для запуска команды. Попробуйте еще раз";
        String[] params = new String[]{"create", "users"};
        create.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testWithUnknowTableException() {
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        try {
            doThrow(new UnknowShitException("MessageFromException")).when(model).create(params);
        } catch (UnknowShitException e) {
            //do nothing
        }
        create.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("MessageFromException", captor.getValue());
    }
}