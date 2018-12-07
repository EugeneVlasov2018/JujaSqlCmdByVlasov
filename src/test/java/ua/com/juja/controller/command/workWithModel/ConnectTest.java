package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class ConnectTest {
    private View view;
    private Model model;
    private Command connect;

    @Before
    public void setup(){
        view = mock(View.class);
        model = mock(Model.class);
        connect = new Connect(view, model);
    }

    @Test
    public void testCanProcess(){
        boolean canProcess = connect.canProcess(new String[]{"connect"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWrongCommand(){
        boolean canProcess = connect.canProcess(new String[]{"dsakjl"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork(){
        connect.doWork(new String[]{"connect", "testforsql", "postgres", "root"});
        verify(view).write("База успешно подключена");
    }

    @Test
    public void testDoWorkWithException() throws UnknowShitException {
        String[] command = new String[]{"connect", "testforsql", "wrongUser", "root"};
        doThrow(new UnknowShitException("messageFromException")).
                when(model).connect(command);
        connect.doWork(command);
        verify(view).write("messageFromException");
    }

    @Test
    public void testDoWorkWithNotEnoughParams() {
        Command connect = new Connect(view, model);
        connect.doWork(new String[]{"connect", "testforsql", "postgres"});
        verify(view).write("Недостаточно данных для запуска команды. Попробуйте еще раз");
    }

}