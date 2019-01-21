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

public class TablesTest extends ActualValueGetter {
    private Model model;
    private Command tables;
    private View view;

    @Before
    public void setup() {

        model = mock(Model.class);
        view = mock(View.class);
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
    public void testDoWork() throws CreatedInModelException {
        String[] command = new String[]{"tables"};
        ArrayList<String> expectedFromTables = new ArrayList<>(Arrays.asList("users"));
        String expectedOnWiew = "[users]";
        doReturn(expectedFromTables).when(model).tables();
        assertEquals(expectedOnWiew, getActualValue(tables, view, command));
    }

    @Test
    public void testDoWorkWithException() throws CreatedInModelException {
        String[] command = new String[]{"tables"};
        doThrow(new CreatedInModelException("ExpectedMessageFromException")).when(model).tables();
        assertEquals("ExpectedMessageFromException", getActualValue(tables, view, command));
    }
}