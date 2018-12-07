package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class FindTest {
    private Model model;
    private View view;
    private Command find;

    @Before
    public void setup(){
        view = mock(View.class);
        model = mock(Model.class);
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
                "+--+---------+----------+--------+";
        String params[] = new String[]{"find", "users"};

        try {
            when(model.getColumnNameForFind(params)).
                    thenReturn(new ArrayList<String>(Arrays.asList("id", "firstname", "secondname", "password")));
            when(model.getColumnValuesForFind(params)).
                    thenReturn(new ArrayList<String>(Arrays.asList("1", "John", "Dou", "123")));
        } catch (UnknowShitException e) {
            //do nothing
        }
        find.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] commandWhitoutParameters = new String[]{"find"};
        find.doWork(commandWhitoutParameters);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).write(captor.capture());
        assertEquals("Недостаточно данных для запуска команды." +
                "Укажите имя таблицы, которую собираетесь вывести на екран", captor.getValue());
    }

    @Test
    public void testDoWorkWithException() {
        String expected = "Непредвиденная ошибка в работе с базой данных.\n" +
                "Причина: null";
        String[] params = new String[]{"find", "users"};
        try {
            when(model.getColumnNameForFind(params)).
                    thenThrow(new UnknowShitException("ExpectedMessageFromException"));
            when(model.getColumnValuesForFind(params)).
                    thenThrow(new UnknowShitException("ExpectedMessageFromException"));
            doThrow(new UnknowShitException("ExpectedMessageFromException")).
                    when(model).delete(params);
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        find.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("ExpectedMessageFromException", captor.getValue());
    }
}