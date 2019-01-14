package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateTest {
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
    public void testDoWork() {
        String expected = "Были изменены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+";
        String[] params = new String[]{"update", "users", "password", "123"};
        try {
            when(model.getColumnNameForUpdateOrDelete(params)).
                    thenReturn(new ArrayList<String>(Arrays.asList("id", "firstname", "secondname", "password")));
        } catch (UnknowShitException e) {
            //do nothing
        }
        try {
            when(model.getColumnValuesForUpdateOrDelete(params)).
                    thenReturn(new ArrayList<String>(Arrays.asList("1", "John", "Dou", "123")));
        } catch (ua.com.juja.model.exceptions.UnknowShitException e) {
            e.printStackTrace();
        }

        update.doWork(params);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWhitoutParameters() {
        String[] commandWhitoutParameters = new String[]{"delete"};
        update.doWork(commandWhitoutParameters);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("Недостаточно данных для запуска команды." +
                "Недостаточно данных для ее выполнения. Попробуйте еще раз.", captor.getValue());
    }

    @Test
    public void testDoWorkWithException() {
        String[] params = new String[]{"update", "users", "password", "123"};
        try {
            when(model.getColumnNameForUpdateOrDelete(params)).
                    thenThrow(new UnknowShitException("ExpectedMessageFromException"));
            when(model.getColumnValuesForUpdateOrDelete(params)).
                    thenThrow(new UnknowShitException("ExpectedMessageFromException"));
            doThrow(new UnknowShitException("ExpectedMessageFromException")).
                    when(model).delete(params);
        } catch (UnknowShitException e) {
            //do nothing
        }
        update.doWork(params);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("ExpectedMessageFromException", captor.getValue());
    }
}