package ua.com.juja.controller.command.workInController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class HelpTest {
    private View view;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
    }

    @Test
    public void testCanProcess(){
        Command help = new Help(view);
        boolean canProcess = help.canProcess(new String[]{"help"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse(){
        Command help = new Help(view);
        boolean canProcess = help.canProcess(new String[]{"dasdcxz"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork(){
        Command help = new Help(view);
        help.doWork(new String[]{"help"},null);
        Mockito.verify(view).write("");
    }

}