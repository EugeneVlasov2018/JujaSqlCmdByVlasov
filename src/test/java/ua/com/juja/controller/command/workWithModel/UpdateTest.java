package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class UpdateTest {
    private ModelInterface model;
    private Connection connectionToDB;
    private Command update;
    private ViewInterface view;

    @Before
    public void setup() {
        model = Mockito.mock(ModelInterface.class);
        view = Mockito.mock(ViewInterface.class);
        update = new Update(model, view);
    }

    @Test
    public void testCanProcess() {
        boolean canProcess = update.canProcess(new String[]{"update"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessFalse() {
        boolean canProcess = update.canProcess(new String[]{"fdsfds", "users"});
        assertFalse(canProcess);
    }

    @Ignore
    @Test
    public void testDoWork() {
        String expected = "Были изменены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+\n";
        String[] params = new String[]{"update", "users", "password", "123", "firstname", "John2", "secondname", "Dou2",
                "password", "123456"};
        //Mockito.when(model.update(params, connectionToDB)).thenReturn(expected);
        update.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutTableName() {
        String expected = "Недостаточно данных для запуска команды." +
                "Недостаточно данных для ее выполнения. Попробуйте еще раз.";
        String[] params = new String[]{"update"};
        update.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    public void testDoWorkWithoutParams() {
        String expected = "Ошибка в формате команды." +
                "Проверьте, указали ли вы таблицу, всем ли именам колонок соответствуют значения и наоборот";
        String[] params = new String[]{"update", "users", "password", "123", "firstname"};
        update.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

}