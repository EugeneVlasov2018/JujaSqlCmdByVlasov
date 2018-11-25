package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class TablesTest {
    private ModelInterface model;
    private Command tables;
    private Connection connection;
    private ViewInterface view;

    @Before
    public void setup() {

        model = Mockito.mock(ModelInterface.class);
        tables = new Tables(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = tables.canProcess(new String[]{"tables"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = tables.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String[] params = new String[]{"fdsfds", "users"};
        tables.doWork(params, connection);
        Mockito.verify(model).tables(connection);

    }

}