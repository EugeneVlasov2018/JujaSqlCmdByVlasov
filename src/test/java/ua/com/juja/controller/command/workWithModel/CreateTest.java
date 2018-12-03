package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CreateTest {
    private ModelInterface model;
    private Connection connectionToDB;
    private Command create;
    private ViewInterface view;

    @Before
    public void setup() {
        model = mock(ModelInterface.class);
        view = mock(ViewInterface.class);
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
    public void testWithUnknowTableException() throws SQLException {
        String expected = "Таблица с таким именем уже существует. Введите команду 'tables'" +
                "чтобы увидеть существующие таблицы";
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        try {
            doThrow(new UnknowTableException()).when(model).create(params, connectionToDB);
        } catch (UnknowTableException e) {
            //do nothing
        }
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testWithNullPointerException() throws UnknowTableException, SQLException {
        String expected = "Вы попытались создать таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password";
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        try {
            doThrow(new NullPointerException()).when(model).create(params, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        }
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testWithSQLException() throws UnknowTableException {
        String expected = "Неизвестная ошибка при работе с базой данных. Причина: null";
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        try {
            doThrow(new SQLException()).when(model).create(params, connectionToDB);
        } catch (SQLException e) {
            //do nothing
        }
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

}