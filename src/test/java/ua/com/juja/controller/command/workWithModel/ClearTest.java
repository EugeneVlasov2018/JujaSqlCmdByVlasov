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

public class ClearTest {

    private ModelInterface model;
    private Connection connectionToDB;
    private Command clear;
    private ViewInterface view;

    @Before
    public void setup() {
        model = mock(ModelInterface.class);
        view = mock(ViewInterface.class);
        clear = new Clear(model, view);
    }


    @Test
    public void testCanProcess() {
        boolean canProcess = clear.canProcess(new String[]{"clear"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = clear.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    //When all work good
    @Test
    public void testDoWork() {
        String[] commandForWork = new String[]{"clear", "users"};
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("Все данные из таблицы users были удалены", captor.getValue());

    }

    //When command is not complete
    @Test
    public void testDoWorkWithoutParameters() {
        String[] commandForWork = new String[]{"clear"};
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("Недостаточно данных для запуска команды. Укажите имя таблицы, " +
                "которое собираетесь очистить", captor.getValue());
    }

    //When .doWork() receives UnkowTableException
    @Test
    public void testDoWorkWithUnknowTableException() throws SQLException {
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new UnknowTableException()).when(model).clear(commandForWork, connectionToDB);
        } catch (UnknowTableException e) {
            //do nothing
        }
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("Вы пытаетесь очистить несуществующую таблицу.\n" +
                "Вызовите команду 'tables', чтобы увидеть, какие таблицы есть в базе данных", captor.getValue());

    }

    //When .doWork() receives NullPointerException
    @Test
    public void testDoWorkWithNullPointerException() throws SQLException, UnknowTableException {
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new NullPointerException()).when(model).clear(commandForWork, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        }
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'", captor.getValue());

    }

    @Test
    public void testDoWorkWithSQLException() throws UnknowTableException {
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new SQLException()).when(model).clear(commandForWork, connectionToDB);
        } catch (SQLException e) {
            //do nothing
        }
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals("Неизвестная ошибка при работе с базой данных. Причина: null", captor.getValue());

    }
}