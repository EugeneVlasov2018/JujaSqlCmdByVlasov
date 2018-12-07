package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowShitException;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.sql.Connection;

import static org.junit.Assert.*;

public class ClearTest {

    private Model model;
    private Connection connectionToDB;
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
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("Все данные из таблицы users были удалены", captor.getValue());

    }

    @Test
    public void testDoWorkWithoutParameters() {
        String[] commandForWork = new String[]{"clear"};
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("Недостаточно данных для запуска команды. Укажите имя таблицы, " +
                "которое собираетесь очистить", captor.getValue());
    }

    @Test
    public void testDoWorkWithException() {
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new UnknowShitException("MessageFromException")).when(model).clear(commandForWork, connectionToDB);
        } catch (UnknowShitException e) {
            //do nothing
        }
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("MessageFromException", captor.getValue());

    }

    //When .doWork() receives NullPointerException
    @Test
    public void testDoWorkWithNullPointerException() {
        String[] commandForWork = new String[]{"clear", "user"};
        try {
            doThrow(new NullPointerException()).when(model).clear(commandForWork, connectionToDB);
        } catch (NullPointerException e) {
            //do nothing
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view).write(captor.capture());
        assertEquals("Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'", captor.getValue());

    }
}