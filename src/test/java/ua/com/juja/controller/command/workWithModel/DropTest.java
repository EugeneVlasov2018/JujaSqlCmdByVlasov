package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class DropTest {
    private ModelInterface model;
    private Connection connectionToDB;
    private Command drop;
    private ViewInterface view;

    @Before
    public void setup() {
        model = Mockito.mock(ModelInterface.class);
        view = Mockito.mock(ViewInterface.class);
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
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParams() {
        String expected = "Недостаточно данных для запуска команды." +
                "Укажите имя таблицы, которое собираетесь удалить";
        String[] params = new String[]{"drop"};
        drop.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }


}