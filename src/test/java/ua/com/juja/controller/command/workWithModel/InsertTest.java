package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class InsertTest {
    private ModelInterface model;
    private Connection connection;
    private Command insert;
    private ViewInterface view;

    @Before
    public void setup() {

        model = Mockito.mock(ModelInterface.class);
        view = Mockito.mock(ViewInterface.class);
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
        Mockito.when(model.insert(params, connection)).thenReturn(expected);
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutTableNameAndParams() {
        String expected = "Недостаточно данных для запуска команды." +
                "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
        String[] params = new String[]{"insert"};
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutParams() {
        String expected = "Ошибка в формате команды." +
                "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
        String[] params = new String[]{"insert", "users", "password", "123", "firstname"};
        insert.doWork(params, connection);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

}