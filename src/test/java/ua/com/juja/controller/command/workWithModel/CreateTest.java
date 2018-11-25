package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class CreateTest {
    private ModelInterface model;
    private Connection connection;
    private Command create;
    private ViewInterface view;

    @Before
    public void setup() {

        model = Mockito.mock(ModelInterface.class);
        create = new Create(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = create.canProcess(new String[]{"create"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = create.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String[] params = new String[]{"fdsfds", "users"};
        create.doWork(params, connection);
        Mockito.verify(model).create(params, connection);

    }

}