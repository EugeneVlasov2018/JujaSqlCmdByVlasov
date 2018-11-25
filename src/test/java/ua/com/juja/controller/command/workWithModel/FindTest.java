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

public class FindTest {
    private ModelInterface model;
    private ViewInterface view;
    private Connection connectionToDB;
    private Command find;

    @Before
    public void setup(){
        view = Mockito.mock(ViewInterface.class);
        model = Mockito.mock(ModelInterface.class);
        find = new Find(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = find.canProcess(new String[]{"find"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = find.canProcess(new String[]{"fdsfds"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String expected = "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+\n";
        String command[] = new String[]{"find", "users"};
        Mockito.when(model.find(command, connectionToDB)).thenReturn(expected);
        find.doWork(command, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] commandWhitoutParameters = new String[]{"find"};
        find.doWork(commandWhitoutParameters, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals("Недостаточно данных для запуска команды." +
                "Укажите имя таблицы, которую собираетесь вывести на екран", captor.getValue());
    }
}