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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class UpdateTest extends ActualValueGetter {
    private Model model;
    private Command update;
    private View view;

    @Before
    public void setup() {
        model = mock(Model.class);
        view = mock(View.class);
        update = new Update(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = update.canProcess(new String[]{"update"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = update.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() throws CreatedInModelException {
        String expected = "Были изменены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+";
        String[] params = new String[]{"update", "users", "password", "123"};
        when(model.getColumnNameForUpdateOrDelete(params)).
                thenReturn(new ArrayList<>(Arrays.asList("id", "firstname", "secondname", "password")));
        when(model.getColumnValuesForUpdateOrDelete(params)).
                thenReturn(new ArrayList<>(Arrays.asList("1", "John", "Dou", "123")));
        assertEquals(expected, getActualValue(update, view, params));
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] params = new String[]{"delete"};
        assertEquals("Недостаточно данных для запуска команды." +
                        "Недостаточно данных для ее выполнения. Попробуйте еще раз.",
                getActualValue(update, view, params));
    }

    @Test
    public void testDoWorkWithException() throws CreatedInModelException {
        String[] params = new String[]{"update", "users", "password", "123"};
        when(model.getColumnNameForUpdateOrDelete(params)).
                thenThrow(new CreatedInModelException("ExpectedMessageFromException"));
        when(model.getColumnValuesForUpdateOrDelete(params)).
                thenThrow(new CreatedInModelException("ExpectedMessageFromException"));
        doThrow(new CreatedInModelException("ExpectedMessageFromException")).
                when(model).delete(params);
        assertEquals("ExpectedMessageFromException", getActualValue(update, view, params));
    }
}