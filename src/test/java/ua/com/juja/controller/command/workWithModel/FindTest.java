package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class FindTest extends ActualValueGetter {
    private Model model;
    private View view;
    private Command find;

    @Before
    public void setup() {
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
    public void testDoWork() throws CreatedInModelException {
        String expected = "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+";
        String params[] = new String[]{"find", "users"};
        when(model.getColumnNameForFind(params)).
                thenReturn(new ArrayList<>(Arrays.asList("id", "firstname", "secondname", "password")));
        when(model.getColumnValuesForFind(params)).
                thenReturn(new ArrayList<>(Arrays.asList("1", "John", "Dou", "123")));
        assertEquals(expected, getActualValue(find, view, params));
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] params = new String[]{"find"};
        assertEquals("Недостаточно данных для запуска команды." +
                        "Укажите имя таблицы, которую собираетесь вывести на екран",
                getActualValue(find, view, params));
    }

    @Test
    public void testDoWorkWithException() throws CreatedInModelException {
        String expected = "Непредвиденная ошибка в работе с базой данных.\n" +
                "Причина: null";
        String[] params = new String[]{"find", "users"};
        when(model.getColumnNameForFind(params)).
                thenThrow(new CreatedInModelException(expected));
        when(model.getColumnValuesForFind(params)).
                thenThrow(new CreatedInModelException(expected));
        doThrow(new CreatedInModelException(expected)).
                when(model).delete(params);
        assertEquals(expected, getActualValue(find, view, params));
    }
}