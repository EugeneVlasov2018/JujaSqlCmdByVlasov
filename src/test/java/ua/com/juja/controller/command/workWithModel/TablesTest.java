package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TablesTest {
    private ModelInterface model;
    private Command tables;
    private Connection connectionToDB;
    private ViewInterface view;

    @Before
    public void setup() {

        model = mock(ModelInterface.class);
        view = mock(ViewInterface.class);
        tables = new Tables(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = tables.canProcess(new String[]{"tables"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = tables.canProcess(new String[]{"fdsfds"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() throws SQLException, NullableAnswerException {
        String[] command = new String[]{"tables"};
        ArrayList<String> expectedFromTables = new ArrayList<>(Arrays.asList("users"));
        String expectedOnWiew = "[users]";

        doReturn(expectedFromTables).when(model).tables(connectionToDB);
        tables.doWork(command, connectionToDB);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expectedOnWiew, captor.getValue());
    }

    @Test
    public void testDoWorkWithNullPointerException() throws SQLException, NullableAnswerException {
        String[] command = new String[]{"tables"};
        String expectedOnWiew = "Вы попытались получить список таблиц, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password";

        doThrow(new NullPointerException()).when(model).tables(connectionToDB);

        tables.doWork(command, connectionToDB);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expectedOnWiew, captor.getValue());
    }

    @Test
    public void testDoWorkWithSQLException() throws NullableAnswerException {
        String[] command = new String[]{"tables"};
        String expectedOnWiew = "Возникли проблемы в методе Tables. Обратитесь к разработчику. Код ошибки: null";

        try {
            doThrow(new SQLException()).when(model).tables(connectionToDB);
        } catch (SQLException e) {
            //do nothing
        }

        tables.doWork(command, connectionToDB);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expectedOnWiew, captor.getValue());
    }

    @Test
    public void testDoWorkWithNullableAnswerException() throws SQLException {
        String[] command = new String[]{"tables"};
        String expectedOnWiew = "В базе данных нет ни одной таблицы";

        try {
            doThrow(new NullableAnswerException()).when(model).tables(connectionToDB);
        } catch (NullableAnswerException e) {
            //do nothing
        }

        tables.doWork(command, connectionToDB);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).setMessage(captor.capture());
        assertEquals(expectedOnWiew, captor.getValue());
    }
}