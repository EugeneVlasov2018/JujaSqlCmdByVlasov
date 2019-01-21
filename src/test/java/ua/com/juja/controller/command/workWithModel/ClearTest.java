package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class ClearTest extends ActualValueGetter {

    private Model model;
    private Command clear;
    private View view;

    @Before
    public void setup() {
        model = mock(Model.class);
        view = mock(View.class);
        clear = new Clear(model, view);
    }


    @Test
    public void testCanProcess() {
        boolean canProcess = clear.canProcess(new String[]{"clear"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = clear.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String[] commandForWork = new String[]{"clear", "users"};
        assertEquals("Все данные из таблицы users были удалены",
                getActualValue(clear, view, commandForWork));

    }

    @Test
    public void testDoWorkWithoutParameters() {
        String[] commandForWork = new String[]{"clear"};
        assertEquals("Недостаточно данных для запуска команды. Укажите имя таблицы, " +
                "которое собираетесь очистить", getActualValue(clear, view, commandForWork));
    }

    @Test
    public void testDoWorkWithException() throws UnknowShitException {
        String[] commandForWork = new String[]{"clear", "user"};
        doThrow(new UnknowShitException("MessageFromException")).when(model).clear(commandForWork);
        assertEquals("MessageFromException", getActualValue(clear, view, commandForWork));
    }
}