package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import ua.com.juja.controller.command.Command;
import ua.com.juja.model.Model;
import ua.com.juja.model.exceptions.CreatedInModelException;
import ua.com.juja.view.View;

import static org.junit.Assert.*;

public class CreateTest extends ActualValueGetter {
    private Model model;
    private Command create;
    private View view;

    @Before
    public void setup() {
        model = mock(Model.class);
        view = mock(View.class);
        create = new Create(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = create.canProcess(new String[]{"create"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = create.canProcess(new String[]{"fdsfds"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork() {
        String expected = "Таблица 'users' успешно создана";
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        assertEquals(expected, getActualValue(create, view, params));
    }

    @Test
    public void testDoWorkWithoutParams() {
        String expected = "Введенная команда неполная.\n" +
                "Команда должна состоять из 3 элементов, а у вас их 2\n" +
                "Попробуйте еще раз";
        String[] params = new String[]{"create", "users"};
        assertEquals(expected, getActualValue(create, view, params));
    }

    @Test
    public void testWithUnknowTableException() throws CreatedInModelException {
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        doThrow(new CreatedInModelException("MessageFromException")).when(model).create(params);
        assertEquals("MessageFromException", getActualValue(create, view, params));
    }
}