package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class UpdateTest {
    private ModelInterface model;
    private Connection connection;
    private Command update;

    @Before
    public void setup() {

        model = Mockito.mock(ModelInterface.class);
        update = new Update(model);
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
        String[] params = new String[]{"fdsfds", "users"};
        update.doWork(params, connection);
        Mockito.verify(model).update(params, connection);

    }

}