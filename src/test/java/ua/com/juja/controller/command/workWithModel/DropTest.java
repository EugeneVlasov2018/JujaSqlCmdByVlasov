package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DropTest {
    private ModelInterface model;
    private Connection connectionToDB;
    private Command drop;
    private ViewInterface view;

    @Before
    public void setup() {
        model = mock(ModelInterface.class);
        view = mock(ViewInterface.class);
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
        drop.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParams() {
        String expected = "Недостаточно данных для запуска команды." +
                "Укажите имя таблицы, которое собираетесь удалить";
        String[] params = new String[]{"drop"};
        drop.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithUnknowTableException() throws SQLException {
        String expected = "Вы попытались удалить несуществующую таблицу.\n" +
                "Введите команду 'tables', чтобы увидеть все созданные таблицы";
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new UnknowTableException()).when(model).drop(commandForWork, connectionToDB);
        } catch (UnknowTableException e) {
            //do nothing
        }
        drop.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());

    }

    //When .doWork() receives NullPointerException
    @Test
    public void testDoWorkWithNullPointerException() throws SQLException, UnknowTableException {
        String expected = "Вы попытались удалить таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'";
        String[] commandForWork = new String[]{"drop", "user"};
        try {
            doThrow(new NullPointerException()).when(model).drop(commandForWork, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        }
        drop.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());

    }

    @Test
    public void testDoWorkWithSQLException() throws UnknowTableException {
        String expected = "Неизвестная ошибка при попытке связаться с базой данных.\n" +
                "Причина: null";
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new SQLException()).when(model).drop(commandForWork, connectionToDB);
        } catch (SQLException e) {
            //do nothing
        }
        drop.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());

    }
}