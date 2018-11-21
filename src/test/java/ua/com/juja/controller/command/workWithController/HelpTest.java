package ua.com.juja.controller.command.workWithController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.ViewInterface;

import static org.junit.Assert.*;

public class HelpTest {
    private ViewInterface view;

    @Before
    public void setup() {
        view = Mockito.mock(ViewInterface.class);
    }

    @Test
    public void testCanProcess(){
        Command help = new Help(view);
        boolean canProcess = help.canProcess(new String[]{"help"});
        assertTrue(canProcess);

    }

    @Test
    public void testCanProcessFalse(){
        Command help = new Help(view);
        boolean canProcess = help.canProcess(new String[]{"dasdcxz"});
        assertFalse(canProcess);
    }

    @Test
    public void testDoWork(){
        Command help = new Help(view);
        help.doWork(new String[]{"help"},null);
        Mockito.verify(view).setMessage("   сonnect\n" +
                "Команда для подключения к соответствующей БД\n" +
                "Формат команды: connect | database | username | password\n" +
                "где: database - имя БД\n" +
                "username -  имя пользователя БД\n" +
                "password - пароль пользователя БД\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    tables\n" +
                "Команда выводит список всех таблиц\n" +
                "Формат: tables (без параметров)\n" +
                "Формат вывода:\n" +
                "в любом удобном формате\n" +
                "например [table1, table2, table3]\n" +
                "    clear\n" +
                "Команда очищает содержимое указанной (всей) таблицы\n" +
                "Формат: clear | tableName\n" +
                "где tableName - имя очищаемой таблицы\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    drop\n" +
                "Команда удаляет заданную таблицу\n" +
                "Формат: drop | tableName\n" +
                "где tableName - имя удаляемой таблицы\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    create\n" +
                "Команда создает новую таблицу с заданными полями\n" +
                "Формат: create | tableName | column1 | column2 | ... | columnN\n" +
                "где: tableName - имя таблицы\n" +
                "column1 - имя первого столбца записи\n" +
                "column2 - имя второго столбца записи\n" +
                "columnN - имя n-го столбца записи\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    find\n" +
                "Команда для получения содержимого указанной таблицы\n" +
                "Формат: find | tableName\n" +
                "где tableName - имя таблицы\n" +
                "Формат вывода: табличка в консольном формате\n" +
                "+--------+---------+------------------+\n" +
                "+  col1  +  col2   +       col3       +\n" +
                "+--------+---------+------------------+\n" +
                "+  123   +  stiven +     pupkin       +\n" +
                "+  345   +  eva    +     pupkina      +\n" +
                "+--------+---------+------------------+\n" +
                "    insert\n" +
                "Команда для вставки одной строки в заданную таблицу\n" +
                "Формат: insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN\n" +
                "где: tableName - имя таблицы\n" +
                "column1 - имя первого столбца записи\n" +
                "value1 - значение первого столбца записи\n" +
                "column2 - имя второго столбца записи\n" +
                "value2 - значение второго столбца записи\n" +
                "columnN - имя n-го столбца записи\n" +
                "valueN - значение n-го столбца записи\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "    update\n" +
                "Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1\n" +
                "Формат: update | tableName | column1 | value1 | column2 | value2\n" +
                "где: tableName - имя таблицы\n" +
                "column1 - имя столбца записи которое проверяется\n" +
                "value1 - значение которому должен соответствовать столбец column1 для обновляемой записи\n" +
                "ВНИМАНИЕ!!! Занчение должно быть со знаком >, < или =\n" +
                "column2 - имя обновляемого столбца записи\n" +
                "value2 - значение обновляемого столбца записи\n" +
                "columnN - имя n-го обновляемого столбца записи\n" +
                "valueN - значение n-го обновляемого столбца записи\n" +
                "Формат вывода: табличный, как при find со старыми значениями обновленных записей.\n" +
                "    delete\n" +
                "Команда удаляет одну или несколько записей для которых соблюдается условие column = value\n" +
                "Формат: delete | tableName | column | value\n" +
                "где: tableName - имя таблицы\n" +
                "Column - имя столбца записи которое проверяется\n" +
                "value - значение которому должен соответствовать столбец column1 для удаляемой записи\n" +
                "Формат вывода: табличный, как при find со старыми значениями удаляемых записей.\n" +
                "    help\n" +
                "Команда выводит в консоль список всех доступных команд\n" +
                "Формат: help (без параметров)\n" +
                "Формат вывода: текст, описания команд с любым форматированием\n" +
                "    exit\n" +
                "Команда для отключения от БД и выход из приложения\n" +
                "Формат: exit (без параметров)\n" +
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n");
    }

}