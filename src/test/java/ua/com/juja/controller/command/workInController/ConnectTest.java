package ua.com.juja.controller.command.workInController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import java.sql.Connection;

import static org.junit.Assert.*;

public class ConnectTest {
    private View view;
    private Connection connection;

    @Before
    public void setup(){
        view = Mockito.mock(View.class);
    }

    @Test
    public void testCanProcess(){
        Command connect = new Connect(view);
        boolean canProcess = connect.canProcess(new String[]{"connect"});
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWrongCommand(){
        Command connect = new Connect(view);
        boolean canProcess = connect.canProcess(new String[]{"dsakjl"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork(){
        Command connect = new Connect(view);
        connect.doWork(new String[]{"connect","testforsql","postgres","root"},null);
        Mockito.verify(view).setMessage("База успешно подключена");
    }

    @Test
    public void testDoWorkWithFalseDBName(){
        Command connect = new Connect(view);
        connect.doWork(new String[]{"connect", "wrongDB", "postgres", "root"}, null);
        Mockito.verify(view).setMessage("Вы ввели: \n"+
                "Неверную ссылку на базу\n"+
                "Попробуйте снова:P");
    }

    @Test
    public void testDoWorkWithFalseUserName(){
        Command connect = new Connect(view);
        connect.doWork(new String[]{"connect", "testforsql", "wrongUser", "root"}, null);
        Mockito.verify(view).setMessage("Вы ввели: \n"+
                "Неверное имя пользователя или пароль\n"+
                "Попробуйте снова:P");
    }

    @Test
    public void testDoWorkWithFalsePassword(){
        Command connect = new Connect(view);
        connect.doWork(new String[]{"connect","testforsql","postgres","wrongPassword"},null);
        Mockito.verify(view).setMessage("Вы ввели: \n"+
                "Неверное имя пользователя или пароль\n"+
                "Попробуйте снова:P");
    }


    @Test
    public void testDoWorkWithNotEnoughParams() {
        Command connect = new Connect(view);
        connect.doWork(new String[]{"connect","testforsql","postgres"},null);
        Mockito.verify(view).setMessage("Недостаточно данных для запуска команды. Попробуйте еще раз");
    }

}