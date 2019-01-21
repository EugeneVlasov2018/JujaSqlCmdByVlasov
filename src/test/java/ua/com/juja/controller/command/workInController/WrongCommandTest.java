package ua.com.juja.controller.command.workInController;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

public class WrongCommandTest {
    private View view;

    @Test
    public void testDoWork(){
        view = mock(View.class);
        Command wrongCommand = new WrongCommand(view);
        wrongCommand.doWork(new String[]{"dsfkldjs"});
        verify(view).write("Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'");
    }

}