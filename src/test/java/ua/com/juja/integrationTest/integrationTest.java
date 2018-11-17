package ua.com.juja.integrationTest;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class integrationTest {
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;

    @BeforeClass
    public static void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }
    @Test

    public void testExit(){
        //given
        in.setLine("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                "Всего хорошего, до встречи снова))\n", getData());
    }

    private String getData() {
        try {
            return new String(out.toByteArray(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testHelp(){
        in.setLine("help");
        in.setLine("exit");
        Main.main(new String[0]);
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //help
                "   сonnect\n" +
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
                "Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "\n" +
                //exit
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testConnectToDb(){
        //присоединяемся с неправильными параметрами (сначала имя базы, потом юзер, потом пароль, потом все вместе
        in.setLine("connect|wrongDbName|postgres|root");
        in.setLine("connect|testforsql|wrongUserName|root");
        in.setLine("connect|testforsql|postgres|wrongPassword");
        in.setLine("connect|wrongDbName|wrongUserName|wrongPassword");
        //присоединяемся правильно
        in.setLine("connect|jujasqlcmd|postgres|root");
        in.setLine("exit");
        Main.main(new String[0]);
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                // wrongDBname
                "Вы ввели: \n" +
                "Неверную ссылку на базу\n" +
                "Попробуйте снова:P\n" +
                // wrong username
                "Вы ввели: \n" +
                "Неверное имя пользователя\n" +
                "Попробуйте снова:P\n" +
                // wrong password
                "Вы ввели: \n" +
                "Неверный пароль\n" +
                "Попробуйте снова:P\n" +
                // wrong all
                "Вы ввели: \n" +
                "Неверную ссылку на базу\n" +
                "Неверное имя пользователя\n" +
                "Неверный пароль\n" +
                "Попробуйте снова:P\n" +
                //all is correct
                "База успешно подключена\n" +
                //exit
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testCreateTable() {
        //создание таблицы без подключения к базе
        in.setLine("create|users|firstname|secondname|password");
        //создаем базу, присоединившись к базе
        in.setLine("connect|jujasqlcmd|postgres|root");
        //теперь все будет норм, база создастся
        in.setLine("create|users|firstname|secondname|password");
        //создаем ее повторно, чтобы сработала защита от дурака - выведется сообщение, что база уже есть
        in.setLine("create|users|firstname|secondname|password");
        //грохаем таблицу для того, чтобы каждый из тестов мог работать сам по себе
        in.setLine("drop|users");
        in.setLine("exit");
        Main.main(new String[0]);
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                "Вы попытались создать таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password\n" +
                "База успешно подключена\n" +
                "Таблица 'users' успешно создана\n" +
                "Таблица с таким именем уже существует. Введите команду 'tables'чтобы увидеть существующие таблицы\n" +
                "Таблица users успешно удалена\n" +
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testDeleteTable(){
        //удаление таблицы без подключения к базе
        in.setLine("drop|users");
        //подключаемся к БД
        in.setLine("connect|jujasqlcmd|postgres|root");
        //создаем таблицу, которую будем удалять
        in.setLine("create|users|firstname|secondname|password");
        //пробуем удалить несуществующую таблицу (неудача)
        in.setLine("drop|anotherTable");
        //грохаем таблицу, - все гут
        in.setLine("drop|users");
        in.setLine("exit");
        Main.main(new String[0]);
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //попітка удалить таблицу без коннекшена к БД
                "Вы попытались удалить таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //присоединенеие к БД
                "База успешно подключена\n" +
                //создание таблицы 'users'
                "Таблица 'users' успешно создана\n" +
                //попытка удаления несуществующей таблицы
                "Вы попытались удалить несуществующую таблицу.\n" +
                "Введите команду 'tables', чтобы увидеть все созданные таблицы\n" +
                //успешное удаление таблицы 'users'
                "Таблица users успешно удалена\n" +
                //exit
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testInsertDataToTable(){
        //добавление данных в таблицу без подключения к базе
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //подключаемся к БД
        in.setLine("connect|jujasqlcmd|postgres|root");
        //попытка добавить данные в несуществующую таблицу (неудача)
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //создаем таблицу, в которую добавим данные
        in.setLine("create|users|firstname|secondname|password");
        //
        in.setLine("insert|users|wrongColumnName|John|secondname|Dou|password|123");
        //успешное добавление данных
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //грохаем таблицу, чтобы другие тесты норм работали
        in.setLine("drop|users");
        in.setLine("exit");
        Main.main(new String[0]);
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //добавление данных в таблицу без подключения к базе
                "Вы попытались вставить информацию в таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "connect|database|username|password\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //попытка добавить данные в несуществующую таблицу (неудача)
                "Проблемно выполнить запрос из за неверного формата данной команды\n" +
                "Проверьте правильность введенных:\n" +
                "Имени таблицы\n" +
                "Названий колонок\n" +
                //создаем таблицу, в которую добавим данные
                "Таблица 'users' успешно создана\n" +
                //попытка добавить данные c неправильніми параметрами (неудача)
                "Проблемно выполнить запрос из за неверного формата данной команды\n" +
                "Проверьте правильность введенных:\n" +
                "Имени таблицы\n" +
                "Названий колонок\n" +
                //удачное добавление данных
                "Все данные успешно добавлены\n" +
                //удаление таблицы
                "Таблица users успешно удалена\n" +
                //выход
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testUpdateDataToTable(){
        //измененние данных в таблице без подключения к базе (неудача)
        in.setLine("update|users|password|123|firstname|John2|secondname|Dou2|password|123456");
        //подключаемся к БД
        in.setLine("connect|jujasqlcmd|postgres|root");
        //изменение данных в несуществующей таблице (неудача)
        in.setLine("update|users|password|123|firstname|John2|secondname|Dou2|password|123456");
        //создаем таблицу, в которую добавим данные
        in.setLine("create|users|firstname|secondname|password");
        //добавляем данные
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //попытка изменения данных с неправильными параметрами в условии (вывод имен таблицы будет, но данные пусты)
        //возможно нужно переделать на выброс специальной ошибки, - рыть в AbstractModelWorkWithPostgre
        in.setLine("update|users|password|wrongParams|firstname|John2|secondname|Dou2|password|123456");
        //попытка изменения данных с неправильным именем колонки в условии (неудача)
        in.setLine("update|users|wrongColumnName|123|firstname|John2|secondname|Dou2|password|123456");
        //правильное изменение данных (успех)
        in.setLine("update|users|password|123|firstname|John2|secondname|Dou2|password|123456");
        //грохаем таблицу, чтобы другие тесты норм работали
        in.setLine("drop|users");
        //выходим
        in.setLine("exit");
        Main.main(new String[0]);
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //измененние данных в таблице без подключения к базе (неудача)
                "Вы попытались обновить данные в таблице, не подключившись к базе данных. Сначала подключитесь\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //изменение данных в несуществующей таблице (неудача)
                "Ошибка в работе с базой данных. Причина:\n" +
                "Таблицы 'users' не сущетвует. Переформулируйте запрос\n" +
                //создаем таблицу, в которую добавим данные
                "Таблица 'users' успешно создана\n" +
                //добавляем данные
                "Все данные успешно добавлены\n" +
                //попытка изменения данных с неправильными параметрами в условии (вывод имен таблицы будет, но данные пусты)
                //возможно нужно переделать на выброс специальной ошибки, - рыть в AbstractModelWorkWithPostgre
                "Были изменены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                //попытка изменения данных с неправильным именем колонки в условии (неудача)
                "Ошибка в работе с базой данных. Причина:\n" +
                "Среди параметров, которые нужно изменить, введено несуществующее имя колонки.\n" +
                "Переформулируйте запрос.\n" +
                //правильное изменение данных (успех)
                "Были изменены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+\n" +
                //грохаем таблицу, чтобы другие тесты норм работали
                "Таблица users успешно удалена\n" +
                //выходим
                "Всего хорошего, до встречи снова))\n",getData());
    }
}
