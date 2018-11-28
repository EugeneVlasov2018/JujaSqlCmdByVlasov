package ua.com.juja.controller.command.workWithController;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.controller.command.exceptions.SystemExitException;
import ua.com.juja.view.ViewInterface;

import static org.junit.Assert.*;

public class ExitTest {
    private ViewInterface view = Mockito.mock(ViewInterface.class);

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
            exit.doWork(new String[]{"exit"}, null);
            fail("Expected SystemExitException");
        }
        catch (SystemExitException a){
            //do nothing
        }
        Mockito.verify(view).setMessage("Всего хорошего, до встречи снова))");
    }
}