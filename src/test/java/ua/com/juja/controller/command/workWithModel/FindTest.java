package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class FindTest {
    private ModelInterface model;
    private Connection connection;
    private Command find;

    @Before
    public void setup(){

        model = Mockito.mock(ModelInterface.class);
        find = new Find(model);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = find.canProcess(new String[]{"find"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = find.canProcess(new String[]{"fdsfds","users"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        //в принципе, его особо нет смысла тестить,- просто передает параметры в модель, но для покрытия кода гут
        String[] params = new String[]{"fdsfds", "users"};
        find.doWork(params, connection);
        Mockito.verify(model).find(params, connection);

    }
    
}