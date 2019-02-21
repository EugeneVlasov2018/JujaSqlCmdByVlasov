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
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class PostgreModelTest {
    private PostgreModel model;

    private static Properties properties;
    private static Connection connection;
    private static IDatabaseTester databaseTester;

    private static final String MAINTABLE = "src\\test\\resourses\\tablesForPostgreModelTest\\maintable.xml";
    private static final String EXPECTED_TABLE_CLEAR =
            "src\\test\\resourses\\tablesForPostgreModelTest\\tableforclear.xml";
    private static final String EXPECTED_TABLE_INSERT =
            "src\\test\\resourses\\tablesForPostgreModelTest\\tableforinsert.xml";
    private static final String EXPECTED_TABLE_DELETE =
            "src\\test\\resourses\\tablesForPostgreModelTest\\tablefordelete.xml";
    private static final String EXPECTED_TABLE_UPDATE =
            "src\\test\\resourses\\tablesForPostgreModelTest\\tableforupdate.xml";
    private static final String TEST_PROPERTIES = "src\\test\\resourses\\testDB.properties";

    @BeforeClass
    public static void init() throws FileNotFoundException, DataSetException {
        properties = getProperties(TEST_PROPERTIES);
        connection = createConnection();
        databaseTester = getDatabaseTester();

        IDataSet mainDataSet = new FlatXmlDataSetBuilder().build(
                new FileInputStream(MAINTABLE));

        databaseTester.setDataSet(mainDataSet);

        createTestTable();
    }

    private static Properties getProperties(String propertiesPath) {
        Properties result = new Properties();

        try (FileInputStream fis = new FileInputStream(propertiesPath)) {
            result.load(fis);

        } catch (FileNotFoundException e) {
            System.err.println("ОШИБКА!!! Файл настроек не найден");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static IDatabaseTester getDatabaseTester(){
        IDatabaseTester result = null;

        String url = properties.getProperty("db.url") + properties.getProperty("db.dbname");
        try {
            result = new JdbcDatabaseTester(
                    properties.getProperty("db.driver"),
                    url,
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Connection createConnection() {
        Connection resultConnection = null;

        try {
            Class.forName(properties.getProperty("db.driver"));
            resultConnection = DriverManager.getConnection(
                    properties.getProperty("db.url") + properties.getProperty("db.dbname"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password"));
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
        dropTable("USERTEST");
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
        model = new PostgreModel(connection, properties);
        databaseTester.onSetup();
    }

    @After
    public void TearDown() throws Exception {
        databaseTester.onTearDown();
    }

    @Test
    public void connect() {
        boolean isConnected = false;
        model = new PostgreModel(null, properties);

        try {
            model.connect(createRequest());
            isConnected = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isConnected);
    }

    private String[] createRequest() {

        return new String[]{
                "connect",
                properties.getProperty("db.name"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")};
    }

    @Test
    public void delete() throws Exception {
        String[] command = new String[]{"delete", "usertest","password","pass1"};
        model.delete(command);
        comparsionOfTables(EXPECTED_TABLE_DELETE);
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
        String expected = "[[USERTEST]]";
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
        model.clear(command);
        comparsionOfTables(EXPECTED_TABLE_CLEAR);
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
        comparsionOfTables(EXPECTED_TABLE_INSERT);
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
        comparsionOfTables(EXPECTED_TABLE_UPDATE);
    }

    @Test
    public void getColumnNameForFind() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "ID", "FIRSTNAME", "SECONDNAME", "PASSWORD"));
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
                "ID", "FIRSTNAME", "SECONDNAME", "PASSWORD"));
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

    //Даный тест ВООБЩЕ не отработает в H2 inMemory, - просто создаст новую ДБ
    //с именем wrongDB, но в связку с другими sql-базами выстрелит на ура
    @Ignore
    @Test
    public void connectWithWrongCommand() {
        model = new PostgreModel(null, properties);
        String[] command = getWrongCommand(1, "wrondDb");
        String expectedMessage = "Вы ввели: \n" +
                "Неверную ссылку на базу\n" +
                "Попробуйте снова:P";
        catchingException(command, expectedMessage);

    }

    @Test
    public void workWhithoutConnection(){
        model = new PostgreModel(null,properties);
        String[] command = new String[]{"drop", "usertest"};
        String expectedMessage = "Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                "Подключитесь к базе данных командой\n" +
                "'connect|database|username|password'";
        catchingException(command,expectedMessage);
    }

    @Test
    public void connectWithWrongUserOrPassword() {
        model = new PostgreModel(null, properties);
        String[] command = getWrongCommand(2, "wrongUser");
        String expectedMessage = "Вы ввели: \n" +
                "Попробуйте снова:P";
        catchingException(command, expectedMessage);
    }

    private String[] getWrongCommand(int indexInCommand, String data) {
        String[] result = new String[]{
                "connect",
                properties.getProperty("db.dbname"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        };
        result[indexInCommand] = data;
        return result;
    }

    @Test
    public void deleteWrongColumn() {
        String[] command = new String[]{"delete", "usertest", "wrongColumn", "pass1"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: Столбец \"WRONGCOLUMN\" не найден\n" +
                "Column \"WRONGCOLUMN\" not found; SQL statement:\n" +
                "DELETE FROM usertest WHERE wrongColumn ='pass1' [42122-192]";
        catchingException(command, expectedMessage);
    }

    @Test
    public void deleteWrongTable() {
        String[] command = new String[]{"delete", "wrongtable", "password", "pass1"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: Таблица \"WRONGTABLE\" не найдена\n" +
                "Table \"WRONGTABLE\" not found; SQL statement:\n" +
                "DELETE FROM wrongtable WHERE password ='pass1' [42102-192]";
        catchingException(command, expectedMessage);
    }

    @Test
    public void dropNonExistentTable() {
        String[] command = new String[]{"drop","nonexistentTable"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: Таблица \"NONEXISTENTTABLE\" не найдена\n" +
                "Table \"NONEXISTENTTABLE\" not found; SQL statement:\n" +
                "DROP TABLE nonexistentTable [42102-192]";
        catchingException(command,expectedMessage);
    }

    @Test
    public void insertNonExistentTable(){
        String[] command = new String[]{"insert","nonexistentTable","password","password3"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: Таблица \"NONEXISTENTTABLE\" не найдена\n" +
                "Table \"NONEXISTENTTABLE\" not found; SQL statement:\n" +
                "INSERT INTO nonexistentTable (password)VALUES ('password3') [42102-192]";
        catchingException(command, expectedMessage);
    }

    @Test
    public void insertNonExistentColumn(){
        String[] command = new String[]{"insert","usertest","wrongcolumn","password3"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: Столбец \"WRONGCOLUMN\" не найден\n" +
                "Column \"WRONGCOLUMN\" not found; SQL statement:\n" +
                "INSERT INTO usertest (wrongcolumn)VALUES ('password3') [42122-192]";
        catchingException(command, expectedMessage);
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
