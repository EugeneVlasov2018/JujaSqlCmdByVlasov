package ua.com.juja.model;

import org.dbunit.*;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import org.junit.*;
import ua.com.juja.model.exceptions.CreatedInModelException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class PostgreModelTest {
    private static final DatabaseSwinger swinger = new DatabaseSwinger(
            "src\\test\\resourses\\testDBunit.properties");
    private PostgreModel model;
    private static Connection connection;
    private static IDatabaseTester databaseTester;

    @BeforeClass
    public static void init() throws ClassNotFoundException, FileNotFoundException, DataSetException {
        connection = createConnection();
        databaseTester = getDatabaseTester();

        IDataSet mainDataSet = new FlatXmlDataSetBuilder().build(
                new FileInputStream("src\\test\\resourses\\tablesForPostgreModelTest\\maintable.xml"));

        databaseTester.setDataSet(mainDataSet);

        createTestTable();
    }

    private static IDatabaseTester getDatabaseTester(){
        IDatabaseTester result = null;

        String url = swinger.getUrl()+swinger.getDbName();
        try {
            result = new JdbcDatabaseTester(
                    swinger.getDriver(),
                    url,
                    swinger.getUser(),
                    swinger.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Connection createConnection() {
        Connection resultConnection = null;

        String url = swinger.getUrl() + swinger.getDbName();
        String user = swinger.getUser();
        String password = swinger.getPassword();
        String JdbcDriver = swinger.getDriver();

        try {
            Class.forName(JdbcDriver);
            resultConnection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultConnection;
    }

    private static void createTestTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE usertest " +
                            "(id serial PRIMARY KEY," +
                            " firstname varchar(225)," +
                            " secondname varchar(225)," +
                            " password varchar(225))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void afterAllTests(){
        dropTable("usertest");
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void dropTable(String tableName) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE public."+tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        model = new PostgreModel(connection,swinger);
        databaseTester.onSetup();
    }

    @After
    public void TearDown() throws Exception {
        databaseTester.onTearDown();
    }

    @Test
    public void connect() {
        boolean isConnected = false;
        model = new PostgreModel(null, swinger);

        try {
            model.connect(createRequest());
            isConnected = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isConnected);
    }

    private String[] createRequest() {
        String database = swinger.getDbName();
        String user = swinger.getUser();
        String password = swinger.getPassword();
        return new String[]{"connect", database, user, password};
    }

    @Test
    public void create(){
        boolean isCreated = false;
        String[] command = new String[]{
                "create",
                "tablefortest",
                "firstcolumn"
        };

        try {
            model.create(command);
            isCreated = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }
        dropTable("tablefortest");

        assertTrue(isCreated);
    }

    @Test
    public void tables() throws CreatedInModelException {
        String expected = "[[usertest]]";
        assertEquals(expected, Arrays.asList(model.tables()).toString());
    }

    @Test
    public void drop() {
        boolean isDroped = false;
        String[] command = new String[]{"drop", "usertest"};
        try {
            model.drop(command);
            isDroped = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }
        createTestTable();
        assertTrue(isDroped);
    }

    @Test
    public void exit() {
        boolean modelIsExit = false;

        try {
            model.exit();
            modelIsExit = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }
        //Восстанавливаем коннекшн после выхода
        connection = createConnection();

        assertTrue(modelIsExit);
    }

    @Test
    public void clear() throws Exception {
        String[] command = new String[]{"clear", "usertest"};
        String expectedTablePath = "src\\test\\resourses\\tablesForPostgreModelTest\\tableforclear.xml";
        model.clear(command);
        comparsionOfTables(expectedTablePath);
    }

    private void comparsionOfTables(String expectedTablePath) throws Exception {
        IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = actualDataSet.getTable("usertest");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(expectedTablePath));
        ITable expectedTable = expectedDataSet.getTable("usertest");

        ITable filteredTable = DefaultColumnFilter.includedColumnsTable(
                actualTable,
                expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredTable);
    }

    @Test
    public void insert() throws Exception {
        String[] command = new String[]
                {"insert", "usertest", "firstname", "fn3", "secondname", "sn3", "password", "pass3"};
        model.insert(command);
        String expectedTablePath = "src\\test\\resourses\\tablesForPostgreModelTest\\tableforinsert.xml";
        comparsionOfTables(expectedTablePath);
    }

    @Test
    public void update() throws Exception {
        String[] command = new String[]{
                "update", "usertest",
                "password", "pass1",
                "firstname", "updated",
                "secondname", "updated",
                "password", "updated"};
        model.update(command);
        String expectedTablePath = "src\\test\\resourses\\tablesForPostgreModelTest\\tableforupdate.xml";
        comparsionOfTables(expectedTablePath);
    }

    @Test
    public void getColumnNameForFind() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        String[] command = new String[]{"find", "usertest"};

        ArrayList<String> actual = (ArrayList<String>) model.getColumnNameForFind(command);

        assertEquals(expected, actual);
    }

    @Test
    public void getColumnValuesForFind() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "fn1", "sn1", "pass1", "fn2", "sn2", "pass2"));
        String[] command = new String[]{"find", "usertest"};

        ArrayList<String> actual = (ArrayList<String>) model.getColumnValuesForFind(command);
        //удалил значения id, которые постоянно меняются, т.к autoincrement (костыль)
        actual.remove(0);
        actual.remove(3);

        assertEquals(expected, actual);
    }

    @Test
    public void getColumnNameForUpdateOrDelete() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        String[] command = new String[]{"delete", "usertest", "password", "pass1"};

        ArrayList<String> actual = (ArrayList<String>) model.getColumnNameForUpdateOrDelete(command);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetColumnValuesForUpdateOrDelete() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "fn1", "sn1", "pass1"));
        String[] command = new String[]{"delete", "usertest", "password", "pass1"};
        ArrayList<String> actual = (ArrayList<String>) model.getColumnValuesForUpdateOrDelete(command);
        //удалил значения id, которые постоянно меняются, т.к autoincrement (костыль)
        actual.remove(0);
        assertEquals(expected, actual);
    }

    @Test
    public void connectWithWrongCommand() {
        model = new PostgreModel(null, swinger);
        String[] command = getWrongCommand(1, "wrondDb");
        String expectedMessage = "Вы ввели: \n" +
                "Неверную ссылку на базу\n" +
                "Попробуйте снова:P";
        catchingException(command, expectedMessage);

    }

    @Test
    public void connectWithWrongUserOrPassword() {
        model = new PostgreModel(null, swinger);
        String[] command = getWrongCommand(2, "wrongUser");
        String expectedMessage = "Вы ввели: \n" +
                "Неверное имя пользователя или пароль\n" +
                "Попробуйте снова:P";
        catchingException(command, expectedMessage);
    }

    private String[] getWrongCommand(int indexInCommand, String data) {
        String[] result = new String[]{
                "connect",
                swinger.getDbName(),
                swinger.getUser(),
                swinger.getPassword()
        };
        result[indexInCommand] = data;
        return result;
    }

    @Test
    public void deleteWrongColumn() {
        String[] command = new String[]{"delete", "usertest", "wrongColumn", "pass1"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: " +
                "ERROR: column \"wrongcolumn\" does not exist\n" +
                "  Позиция: 28";
        catchingException(command, expectedMessage);
    }

    @Test
    public void deleteWrongTable() {
        String[] command = new String[]{"delete", "wrongtable", "password", "pass1"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: " +
                "ERROR: relation \"wrongtable\" does not exist\n" +
                "  Позиция: 13";
        catchingException(command, expectedMessage);
    }

    @Test
    public void dropNonExistentTable() {
        String[] command = new String[]{"drop","nonexistentTable"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина:" +
                " ERROR: table \"nonexistenttable\" does not exist";
        catchingException(command,expectedMessage);
    }

    @Test
    public void insertNonExistentTable(){
        String[] command = new String[]{"insert","nonexistentTable","password","password3"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина:" +
                " ERROR: relation \"nonexistenttable\" does not exist\n" +
                "  Позиция: 13";
        catchingException(command,expectedMessage);;
    }

    @Test
    public void insertNonExistentColumn(){
        String[] command = new String[]{"insert","usertest","wrongcolumn","password3"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина:" +
                " ERROR: column \"wrongcolumn\" of relation \"usertest\" does not exist\n" +
                "  Позиция: 23";
        catchingException(command,expectedMessage);;
    }

    private void catchingException(String[] command, String expectedMessage) {
        try {
            startMethod(command);
            Assert.fail("метод отработал корректно, - такого не должно быть");
        } catch (CreatedInModelException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    

    private void startMethod(String[] command) throws CreatedInModelException {
        if (command[0].equals("connect"))
            model.connect(command);
        else if (command[0].equals("create"))
            model.create(command);
        else if (command[0].equals("drop"))
            model.drop(command);
        else if (command[0].equals("insert"))
            model.insert(command);
        else if (command[0].equals("update"))
            model.update(command);
        else if (command[0].equals("clear"))
            model.clear(command);
        else if (command[0].equals("delete"))
            model.delete(command);
    }

}
