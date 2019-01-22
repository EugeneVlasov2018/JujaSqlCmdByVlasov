package ua.com.juja.integrationTest;

import org.junit.*;
import ua.com.juja.controller.Main;
import ua.com.juja.controller.MainController;
import ua.com.juja.model.Model;
import ua.com.juja.model.PostgreModel;
import ua.com.juja.view.ConsoleView;
import ua.com.juja.view.View;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


public class IntegrationTest {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;
    private Model model;
    private View view;
    private MainController mainController;
    private final static String commandForConnect = "connect|testforsql|postgres|root";

    @Before
    public void setup() {
        model = new PostgreModel();
        view = new ConsoleView();
        mainController = new MainController(model, view);
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    //тест, который показывает правильное взаимодействие всех комманд (когда все отрабатывает гуд)
    public void testWhenAllWorkGood(){
        //выводим весь список комманд
        in.setLine("help");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //создаем таблицу, в которую добавим данные
        in.setLine("create|users|firstname|secondname|password");
        //выводим список имен таблиц
        in.setLine("tables");
        //успешное добавление данных
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //выводим инфу на экран
        in.setLine("find|users");
        //меняем данную запись
        in.setLine("update|users|password|123|firstname|John2|secondname|Dou2|password|123456");
        //добавляем еще данные
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //выводим инфу на экран
        in.setLine("find|users");
        //удаляем одну из записей
        in.setLine("delete|users|password|123");
        //выводим инфу на экран
        in.setLine("find|users");
        //чистим всю таблицу
        in.setLine("clear|users");
        //выводим инфу на экран
        in.setLine("find|users");
        //грохаем таблицу, чтобы другие тесты норм работали
        in.setLine("drop|users");
        //выходим
        in.setLine("exit");
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //выводим весь список комманд
                "    сonnect\n" +
                "Команда для подключения к соответствующей БД\n" +
                "Формат команды: connect | database | username | password\n" +
                "где:\n" +
                " database - имя БД\n" +
                " username -  имя пользователя БД\n" +
                " password - пароль пользователя БД\n" +
                "---------------------------------------------------------------\n" +
                "    tables\n" +
                "Команда выводит список всех таблиц\n" +
                "Формат: tables\n" +
                "---------------------------------------------------------------\n" +
                "    clear\n" +
                "Команда очищает содержимое указанной (всей) таблицы\n" +
                "Формат: clear | tableName\n" +
                " где tableName - имя очищаемой таблицы\n" +
                "---------------------------------------------------------------\n" +
                "    drop\n" +
                "Команда удаляет заданную таблицу\n" +
                "Формат: drop | tableName\n" +
                " где tableName - имя удаляемой таблицы\n" +
                "---------------------------------------------------------------\n" +
                "    create\n" +
                "Команда создает новую таблицу с заданными полями\n" +
                "Формат: create | tableName | column1 | column2 | ... | columnN\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " column1 - имя первого столбца записи\n" +
                " column2 - имя второго столбца записи\n" +
                " columnN - имя n-го столбца записи\n" +
                " ---------------------------------------------------------------\n" +
                "    find\n" +
                "Команда для получения содержимого указанной таблицы\n" +
                "Формат: find | tableName\n" +
                " где tableName - имя таблицы\n" +
                "---------------------------------------------------------------\n" +
                "    insert\n" +
                "Команда для вставки одной строки в заданную таблицу\n" +
                "Формат: insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " column1 - имя первого столбца записи\n" +
                " value1 - значение первого столбца записи\n" +
                " column2 - имя второго столбца записи\n" +
                " value2 - значение второго столбца записи\n" +
                " columnN - имя n-го столбца записи\n" +
                " valueN - значение n-го столбца записи\n" +
                " Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "---------------------------------------------------------------\n" +
                "    update\n" +
                "Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1\n" +
                "Формат: update | tableName | column1 | value1 | column2 | value2\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " column1 - имя столбца записи которое проверяется\n" +
                " value1 - значение которому должен соответствовать столбец column1 для обновляемой записи\n" +
                "!!!!!\n" +
                " ВНИМАНИЕ!!! Значение должно быть со знаком >, < или =\n" +
                "!!!!!\n" +
                " column2 - имя обновляемого столбца записи\n" +
                " value2 - значение обновляемого столбца записи\n" +
                " columnN - имя n-го обновляемого столбца записи\n" +
                " valueN - значение n-го обновляемого столбца записи\n" +
                "---------------------------------------------------------------\n" +
                "    delete\n" +
                "Команда удаляет одну или несколько записей для которых соблюдается условие column = value\n" +
                "Формат: delete | tableName | column | value\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " Column - имя столбца записи которое проверяется\n" +
                " value - значение которому должен соответствовать столбец column1 для удаляемой записи\n" +
                "---------------------------------------------------------------\n" +
                "    help\n" +
                "Команда выводит в консоль список всех доступных команд\n" +
                "Формат: help (без параметров)\n" +
                "---------------------------------------------------------------\n" +
                "    exit\n" +
                "Команда для отключения от БД и выход из приложения\n" +
                "Формат: exit (без параметров)\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //создаем таблицу, в которую добавим данные
                "Таблица 'users' успешно создана\n" +
                //Выводим список таблиц
                "[users]\n" +
                //Добавляем данные
                "Все данные успешно добавлены\n" +
                //выводим содержимое таблицы
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+\n" +
                //меняем данные
                "Были изменены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+\n" +
                //добавляем еще данные
                "Все данные успешно добавлены\n" +
                //выводим данные на консоль
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John2    |Dou2      |123456  |\n" +
                "|2 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+\n" +
                //удаляем одну строку
                "Были удалены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|2 |John     |Dou       |123     |\n" +
                "+--+---------+----------+--------+\n" +
                //выводим данные в консоль
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                "|1 |John2    |Dou2      |123456  |\n" +
                "+--+---------+----------+--------+\n" +
                //чистим таблицу полностью
                "Все данные из таблицы users были удалены\n" +
                //убеждаемся, что таблица полностью пуста
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                //удаляем таблицу
                "Таблица users успешно удалена\n" +
                //выходим
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testExitFromProgram(){
        //given
        in.setLine("exit");
        //when
        //Main.main(null);
        mainController.beginWork();
        //then
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                "Всего хорошего, до встречи снова))\n", getData());
    }

    @Test
    public void testHelp(){
        in.setLine("help");
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //help
                "    сonnect\n" +
                "Команда для подключения к соответствующей БД\n" +
                "Формат команды: connect | database | username | password\n" +
                "где:\n" +
                " database - имя БД\n" +
                " username -  имя пользователя БД\n" +
                " password - пароль пользователя БД\n" +
                "---------------------------------------------------------------\n" +
                "    tables\n" +
                "Команда выводит список всех таблиц\n" +
                "Формат: tables\n" +
                "---------------------------------------------------------------\n" +
                "    clear\n" +
                "Команда очищает содержимое указанной (всей) таблицы\n" +
                "Формат: clear | tableName\n" +
                " где tableName - имя очищаемой таблицы\n" +
                "---------------------------------------------------------------\n" +
                "    drop\n" +
                "Команда удаляет заданную таблицу\n" +
                "Формат: drop | tableName\n" +
                " где tableName - имя удаляемой таблицы\n" +
                "---------------------------------------------------------------\n" +
                "    create\n" +
                "Команда создает новую таблицу с заданными полями\n" +
                "Формат: create | tableName | column1 | column2 | ... | columnN\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " column1 - имя первого столбца записи\n" +
                " column2 - имя второго столбца записи\n" +
                " columnN - имя n-го столбца записи\n" +
                " ---------------------------------------------------------------\n" +
                "    find\n" +
                "Команда для получения содержимого указанной таблицы\n" +
                "Формат: find | tableName\n" +
                " где tableName - имя таблицы\n" +
                "---------------------------------------------------------------\n" +
                "    insert\n" +
                "Команда для вставки одной строки в заданную таблицу\n" +
                "Формат: insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " column1 - имя первого столбца записи\n" +
                " value1 - значение первого столбца записи\n" +
                " column2 - имя второго столбца записи\n" +
                " value2 - значение второго столбца записи\n" +
                " columnN - имя n-го столбца записи\n" +
                " valueN - значение n-го столбца записи\n" +
                " Формат вывода: текстовое сообщение с результатом выполнения операции\n" +
                "---------------------------------------------------------------\n" +
                "    update\n" +
                "Команда обновит запись, установив значение column2 = value2, для которой соблюдается условие column1 = value1\n" +
                "Формат: update | tableName | column1 | value1 | column2 | value2\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " column1 - имя столбца записи которое проверяется\n" +
                " value1 - значение которому должен соответствовать столбец column1 для обновляемой записи\n" +
                "!!!!!\n" +
                " ВНИМАНИЕ!!! Значение должно быть со знаком >, < или =\n" +
                "!!!!!\n" +
                " column2 - имя обновляемого столбца записи\n" +
                " value2 - значение обновляемого столбца записи\n" +
                " columnN - имя n-го обновляемого столбца записи\n" +
                " valueN - значение n-го обновляемого столбца записи\n" +
                "---------------------------------------------------------------\n" +
                "    delete\n" +
                "Команда удаляет одну или несколько записей для которых соблюдается условие column = value\n" +
                "Формат: delete | tableName | column | value\n" +
                " где:\n" +
                " tableName - имя таблицы\n" +
                " Column - имя столбца записи которое проверяется\n" +
                " value - значение которому должен соответствовать столбец column1 для удаляемой записи\n" +
                "---------------------------------------------------------------\n" +
                "    help\n" +
                "Команда выводит в консоль список всех доступных команд\n" +
                "Формат: help (без параметров)\n" +
                "---------------------------------------------------------------\n" +
                "    exit\n" +
                "Команда для отключения от БД и выход из приложения\n" +
                "Формат: exit (без параметров)\n" +
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
        in.setLine(commandForConnect);
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                // wrongDBname
                "Вы ввели: \n" +
                "Неверную ссылку на базу\n" +
                "Попробуйте снова:P\n" +
                // wrong username
                "Вы ввели: \n" +
                "Неверное имя пользователя или пароль\n" +
                "Попробуйте снова:P\n" +
                // wrong password
                "Вы ввели: \n" +
                "Неверное имя пользователя или пароль\n" +
                "Попробуйте снова:P\n" +
                // wrong all
                "Вы ввели: \n" +
                "Неверное имя пользователя или пароль\n" +
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
        in.setLine(commandForConnect);
        //теперь все будет норм, база создастся
        in.setLine("create|users|firstname|secondname|password");
        //создаем ее повторно, чтобы сработала защита от дурака - выведется сообщение, что база уже есть
        in.setLine("create|users|firstname|secondname|password");
        //грохаем таблицу для того, чтобы каждый из тестов мог работать сам по себе
        in.setLine("drop|users");
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                "База успешно подключена\n" +
                "Таблица 'users' успешно создана\n" +
                "Ошибка в работе с базой данных. Причина: ERROR: relation \"users\" already exists\n" +
                "Таблица users успешно удалена\n" +
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testDeleteTable(){
        //удаление таблицы без подключения к базе
        in.setLine("drop|users");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //создаем таблицу, которую будем удалять
        in.setLine("create|users|firstname|secondname|password");
        //пробуем удалить несуществующую таблицу (неудача)
        in.setLine("drop|anotherTable");
        //грохаем таблицу, - все гут
        in.setLine("drop|users");
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //попітка удалить таблицу без коннекшена к БД
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //присоединенеие к БД
                "База успешно подключена\n" +
                //создание таблицы 'users'
                "Таблица 'users' успешно создана\n" +
                //попытка удаления несуществующей таблицы
                "Ошибка в работе с базой данных. Причина: ERROR: table \"anothertable\" does not exist\n" +
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
        in.setLine(commandForConnect);
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
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //добавление данных в таблицу без подключения к базе
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //попытка добавить данные в несуществующую таблицу (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: relation \"users\" does not exist\n" +
                "  Позиция: 13\n" +
                //создаем таблицу, в которую добавим данные
                "Таблица 'users' успешно создана\n" +
                //попытка добавить данные c неправильніми параметрами (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: column \"wrongcolumnname\" of relation \"users\" does not exist\n" +
                "  Позиция: 20\n" +
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
        in.setLine(commandForConnect);
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
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //измененние данных в таблице без подключения к базе (неудача)
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //изменение данных в несуществующей таблице (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: relation \"users\" does not exist\n" +
                "  Позиция: 15\n" +
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
                "Ошибка в работе с базой данных. Причина: ERROR: column \"wrongcolumnname\" does not exist\n" +
                "  Позиция: 27\n" +
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

    @Test
    public void testDeleteDataFromTable(){
        //удаление данных из таблицы без подключения к базе (неудача)
        in.setLine("delete|users|password|123");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //удаление данных из несуществующей таблицы (неудача)
        in.setLine("delete|users|password|123");
        //создаем таблицу, в которую добавим данные
        in.setLine("create|users|firstname|secondname|password");
        //добавляем данные
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //попытка удаления данных с неправильными параметрами в условии (вывод имен таблицы будет, но данные пусты)
        //возможно нужно переделать на выброс специальной ошибки, - рыть в AbstractModelWorkWithPostgre
        in.setLine("delete|users|password|1234");
        //попытка изменения данных с неправильным именем колонки в условии (неудача)
        in.setLine("delete|users|wrongColumnName|123");
        //правильное изменение данных (успех)
        in.setLine("delete|users|password|123");
        //грохаем таблицу, чтобы другие тесты норм работали
        in.setLine("drop|users");
        //выходим
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //Удаление данных без подключения к БД (фейл)
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //подключение к БД
                "База успешно подключена\n" +
                //удаление данных из несуществующей таблицы (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: relation \"users\" does not exist\n" +
                "  Позиция: 15\n" +
                //создаем таблицу, в которую добавим данные
                "Таблица 'users' успешно создана\n" +
                //добавляем данные
                "Все данные успешно добавлены\n" +
                //попытка удаления данных с неправильными параметрами в условии (вывод имен таблицы будет, но данные пусты)
                //возможно нужно переделать на выброс специальной ошибки, - рыть в AbstractModelWorkWithPostgre
                "Были удалены следующие строки:\n" +
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                //попытка изменения данных с неправильным именем колонки в условии (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: column \"wrongcolumnname\" does not exist\n" +
                "  Позиция: 27\n" +
                //правильное изменение данных (успех)
                "Были удалены следующие строки:\n" +
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

    @Test
    public void testDropTableFromDB(){
        //удаление данных из таблицы без подключения к базе (неудача)
        in.setLine("drop|users");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //удаление данных из несуществующей таблицы (неудача)
        in.setLine("drop|users");
        //создаем таблицу, которую потом удалим
        in.setLine("create|users|firstname|secondname|password");
        //удаление таблицы(успех)
        in.setLine("drop|users");
        //выходим
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //удаление данных из таблицы без подключения к базе (неудача)
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //удаление данных из несуществующей таблицы (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: table \"users\" does not exist\n" +
                //создаем таблицу, которую потом удалим
                "Таблица 'users' успешно создана\n" +
                //удаление таблицы(успех)
                "Таблица users успешно удалена\n" +
                //выходим
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testClearDataFromTable(){
        //очистка таблицы без подключения к базе (неудача)
        in.setLine("clear|users");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //очистка несуществующей таблицы (неудача)
        in.setLine("clear|users");
        //создаем таблицу, в которую добавим данные
        in.setLine("create|users|firstname|secondname|password");
        //очистка пустой таблицы (успех)
        in.setLine("clear|users");
        //добавляем данные
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //очистка заполненной таблицы (успех)
        in.setLine("clear|users");
        //грохаем таблицу, чтобы другие тесты норм работали
        in.setLine("drop|users");
        //выходим
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //очистка таблицы без подключения к базе (неудача)
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //очистка несуществующей таблицы (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: relation \"users\" does not exist\n" +
                "  Позиция: 13\n" +
                //создание таблицы, которую удалим
                "Таблица 'users' успешно создана\n" +
                //очистка пустой таблицы (успех)
                "Все данные из таблицы users были удалены\n" +
                //добавление данных в таблицу
                "Все данные успешно добавлены\n" +
                //очистка заполненной таблицы (успех)
                "Все данные из таблицы users были удалены\n" +
                //удаление таблицы
                "Таблица users успешно удалена\n" +
                //выход
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testFindDataInTable(){
        //отображение данных из таблицы без подключения к базе (неудача)
        in.setLine("find|users");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //отображение данных в несуществующей таблице (неудача)
        in.setLine("find|users");
        //создаем таблицу, в которую добавим данные
        in.setLine("create|users|firstname|secondname|password");
        //отображаем данные из пустой таблицы
        in.setLine("find|users");
        //добавляем данные
        in.setLine("insert|users|firstname|John|secondname|Dou|password|123");
        //отображаем данные из заполненной таблицы
        in.setLine("find|users");
        //грохаем таблицу, чтобы другие тесты норм работали
        in.setLine("drop|users");
        //выходим
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //отображение данных из таблицы без подключения к базе (неудача)
                "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //отображение данных в несуществующей таблице (неудача)
                "Ошибка в работе с базой данных. Причина: ERROR: relation \"users\" does not exist\n" +
                "  Позиция: 15\n" +
                //создаем таблицу, в которую добавим данные
                "Таблица 'users' успешно создана\n" +
                //отображаем данные из пустой таблицы
                "+--+---------+----------+--------+\n" +
                "|id|firstname|secondname|password|\n" +
                "+--+---------+----------+--------+\n" +
                //добавляем данные
                "Все данные успешно добавлены\n" +
                //отображаем данные из заполненной таблицы
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

    @Test
    public void testShowTableNamesOnDB(){
        //отображение имен таблиц без подключения к базе (неудача)
        in.setLine("tables");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //отображение имен таблиц из пустой БД
        in.setLine("tables");
        //создаем таблицу, которую будем отображать
        in.setLine("create|users|firstname|secondname|password");
        //отображаем таблицу из БД с инфой
        in.setLine("tables");
        //грохаем таблицу, чтобы другие тесты норм работали
        in.setLine("drop|users");
        //выходим
        in.setLine("exit");
        //Main.main(null);
        mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //отображение имен таблиц без подключения к базе (неудача)
                "Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //отображение имен таблиц из пустой БД
                "В базе данных нет ни одной таблицы\n" +
                //создаем таблицу, которую будем отображать
                "Таблица 'users' успешно создана\n" +
                //отображаем таблицу из БД с инфой
                "[users]\n" +
                //грохаем таблицу, чтобы другие тесты норм работали
                "Таблица users успешно удалена\n" +
                //выходим
                "Всего хорошего, до встречи снова))\n",getData());
    }

    @Test
    public void testWrongCommand(){
        //В принципе, оно срабатывает в любом случае, но перевое, - без коннекшена
        in.setLine("wrongCommand");
        //подключаемся к БД
        in.setLine(commandForConnect);
        //теперь с подключенной БД (пишем белеберду)
        in.setLine("dsaklkjn");
        //теперь просто пустая строка
        in.setLine("");
        //и строка, которая будет разбита на параметры но с неправильной командой
        in.setLine("ыфлордолр|testforsql|postgres|root");
        //выходим
        in.setLine("exit");
        Main.main(null);
        //mainController.beginWork();
        assertEquals("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password\n" +
                //В принципе, оно срабатывает в любом случае, но перевое, - без коннекшена
                "Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'\n" +
                //подключаемся к БД
                "База успешно подключена\n" +
                //теперь с подключенной БД (пишем белеберду)
                "Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'\n" +
                //теперь просто пустая строка
                "Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'\n" +
                //и строка, которая будет разбита на параметры но с неправильной командой
                "Вы ввели неизвестную команду.\n" +
                "Вызовите команду 'help'\n" +
                //выходим
                "Всего хорошего, до встречи снова))\n",getData());
    }


    private String getData() {
        try {
            return new String(out.toByteArray(),"UTF-8").replaceAll(System.lineSeparator(), "\n");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}