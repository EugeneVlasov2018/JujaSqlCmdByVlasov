package ua.com.juja.controller.command.workInController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

public class WrongCommandTest {
    private View view;

    @Before
    public void setup(){
        view = Mockito.mock(View.class);
    }

    @Test
    public void testDoWork(){
        Command wrongCommand = new WrongCommand(view);
        wrongCommand.doWork(new String[]{"dsfkldjs"},null);
        Mockito.verify(view).setMessage("Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'");
    }

}