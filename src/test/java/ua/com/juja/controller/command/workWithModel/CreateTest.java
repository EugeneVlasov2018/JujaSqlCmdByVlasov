package ua.com.juja.controller.command.workWithModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;

import static org.junit.Assert.*;

public class CreateTest {
    private ModelInterface model;
    private Connection connectionToDB;
    private Command create;
    private ViewInterface view;

    @Before
    public void setup() {
        model = Mockito.mock(ModelInterface.class);
        view = Mockito.mock(ViewInterface.class);
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
        String expected = "Таблица 'users' успешно создана\n";
        String[] params = new String[]{"create", "users", "firstname", "secondname", "password"};
        create.doWork(params, connectionToDB);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }

    @Test
    /* Вобщем тут у меня затык(((
    В принципе, я предполагаю, что этот затык будет со всеми тестами пакета "workWithModel"
    Суть проблемы, - каким образом замокать поведение model так, чтобы он не выбрасывал экзепшены???
    То есть его нормальное поведение.

    */
    public void testDoWorkWithoutParams() {
        String expected = "Недостаточно данных для запуска команды. Попробуйте еще раз";
        String[] params = new String[]{"create", "users"};
        create.doWork(params, connectionToDB);
        /*Тут сразу требуется обработка экзепшена
        Mockito.verify(model).create(params,connectionToDB);*/
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(view).setMessage(captor.capture());
        assertEquals(expected, captor.getValue());
    }
}