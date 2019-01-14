package ua.com.juja.controller.command.workInController;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.SystemExitException;
import ua.com.juja.controller.command.workWithModel.Exit;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class ExitTest {
    private View view;
    private Model model;
    Command exit;

    @Before
    public void setUp() {
        view = mock(View.class);
        model = mock(Model.class);
        exit = new Exit(view, model);
    }

    @Test
    public void canProcess() {
        boolean canProcess = exit.canProcess(new String[]{"exit"});
        assertTrue(canProcess);
    }

    @Test
    public void falseProcess() {
        boolean canProcess = exit.canProcess(new String[]{"dsad"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        try {
            exit.doWork(new String[]{"exit"});
        }
        catch (SystemExitException a){
            //do nothing
        }
        verify(view).write("Всего хорошего, до встречи снова))");
    }
}