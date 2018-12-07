package ua.com.juja.controller.command.workInController;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.SystemExitException;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class ExitTest {
    private View view = Mockito.mock(View.class);

    @Test
    public void canProcess() {
        Command exit = new Exit(view);
        boolean canProcess = exit.canProcess(new String[]{"exit"});
        assertTrue(canProcess);
    }

    @Test
    public void falseProcess() {
        Command exit = new Exit(view);
        boolean canProcess = exit.canProcess(new String[]{"dsad"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        Command exit = new Exit(view);
        try {
            exit.doWork(new String[]{"exit"});
            fail("Expected SystemExitException");
        }
        catch (SystemExitException a){
            //do nothing
        }
        Mockito.verify(view).write("Всего хорошего, до встречи снова))");
    }
}