package ua.com.juja.controller.command.workWithController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.ViewInterface;

import static org.junit.Assert.*;

public class WrongCommandTest {
    private ViewInterface view;

    @Before
    public void setup(){
        view = Mockito.mock(ViewInterface.class);
    }

    @Test
    public void testDoWork(){
        Command wrongCommand = new WrongCommand(view);
        wrongCommand.doWork(new String[]{"dsfkldjs"},null);
        Mockito.verify(view).setMessage("Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'");
    }

}