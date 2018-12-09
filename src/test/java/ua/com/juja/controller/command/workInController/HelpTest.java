package ua.com.juja.controller.command.workInController;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class HelpTest {
    private View view;
    private Command help;

    @Before
    public void setup() {
        view = mock(View.class);
        help = new Help(view);
    }

    @Test
    public void testCanProcess(){
        boolean canProcess = help.canProcess(new String[]{"help"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse(){
        boolean canProcess = help.canProcess(new String[]{"dasdcxz"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork(){
        help.doWork(new String[]{"help"});
        verify(view).write("    сonnect");
    }
}