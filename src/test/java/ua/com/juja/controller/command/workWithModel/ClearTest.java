package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.CreatedInModelException;
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
        assertEquals("Введенная команда неполная.\n" +
                "Команда должна состоять из 2 элементов, а у вас их 1\n" +
                "Попробуйте еще раз", getActualValue(clear, view, commandForWork));
    }

    @Test
    public void testDoWorkWithException() throws CreatedInModelException {
        String[] commandForWork = new String[]{"clear", "user"};
        doThrow(new CreatedInModelException("MessageFromException")).when(model).clear(commandForWork);
        assertEquals("MessageFromException", getActualValue(clear, view, commandForWork));
    }
}