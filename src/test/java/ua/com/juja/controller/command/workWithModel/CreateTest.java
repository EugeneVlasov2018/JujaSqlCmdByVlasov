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
    private Connection connectionToDB;
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
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutParams() {
        String expected = "Недостаточно данных для запуска команды. Попробуйте еще раз";
        String[] params = new String[]{"create", "users"};
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testWithUnknowTableException() {
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        try {
            doThrow(new UnknowShitException("MessageFromException")).when(model).create(params, connectionToDB);
        } catch (UnknowShitException e) {
            //do nothing
        }
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("MessageFromException", captor.getValue());
    }

    @Test
    public void testWithNullPointerException() {
        String expected = "Вы попытались создать таблицу, не подключившись к БД.\n" +
                "Сначала подключитесь командой 'connect' или вызовите команду 'help'";
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        try {
            doThrow(new NullPointerException()).when(model).create(params, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }
}