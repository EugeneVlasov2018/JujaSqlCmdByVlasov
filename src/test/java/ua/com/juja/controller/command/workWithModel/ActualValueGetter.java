package ua.com.juja.controller.command.workWithModel;

import org.mockito.ArgumentCaptor;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import static org.mockito.Mockito.verify;

public class ActualValueGetter {
    protected String getActualValue(Command commandClass, View view, String[] commandForWork) {
        commandClass.doWork(commandForWork);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        return captor.getValue();
    }
}
