package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ClearTest {

    private ModelInterface model;
    private Connection connectionToDB;
    private Command clear;
    private ViewInterface view;

    @Before
    public void setup() {
        model = Mockito.mock(ModelInterface.class);
        view = Mockito.mock(ViewInterface.class);
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
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals("Все данные из таблицы users были удалены", captor.getValue());

    }

    @Test
    public void testDoWorkWithoutParameters() {
        String[] commandForWork = new String[]{"clear"};
        clear.doWork(commandForWork, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals("Недостаточно данных для запуска команды. Укажите имя таблицы, " +
                "которое собираетесь очистить", captor.getValue());
    }


}