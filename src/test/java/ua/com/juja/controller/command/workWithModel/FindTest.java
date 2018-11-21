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

    @Before
    public void setup(){
        model = Mockito.mock(ModelInterface.class);
    }

    @Test
    public void canProcess(){
        Command find = new Find(model);
        boolean canProcess = find.canProcess(new String[]{"find","users"});
        assertTrue(canProcess);
    }

    @Test
    public void canProcessFalse(){
        Command find = new Find(model);
        boolean canProcess = find.canProcess(new String[]{"fdsfds","users"});
        assertFalse(canProcess);
    }
    
}