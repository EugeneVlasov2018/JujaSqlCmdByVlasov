package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class DropTest {
    private ModelInterface model;
    private Connection connection;
    private Command drop;

    @Before
    public void setup() {

        model = Mockito.mock(ModelInterface.class);
        drop = new Drop(model);
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
        String[] params = new String[]{"fdsfds", "users"};
        drop.doWork(params, connection);
        Mockito.verify(model).drop(params, connection);

    }

}