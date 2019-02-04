package ua.com.juja.model;

import org.junit.*;
import ua.com.juja.model.exceptions.CreatedInModelException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.*;

public class PostgreModelTest {
    private Model model;
    private static String[] responceToConnection = new String[4];
    private static Connection connection;

    @BeforeClass
    public static void databaseSetUp() {
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream("" +
                "src\\test\\resourses\\tetsDB.properties")) {
            property.load(fis);

        } catch (FileNotFoundException e) {
            System.err.println("ОШИБКА!!! Файл настроек не найден");
        } catch (IOException e) {
            e.printStackTrace();
        }

        responceToConnection[0] = "connect";
        responceToConnection[1] = property.getProperty("db.dbname");
        responceToConnection[2] = property.getProperty("db.user");
        responceToConnection[3] = property.getProperty("db.password");

    }

    @Before
    public void setUp() {
        connectToDBTest();
        model = new PostgreModel(connection);
    }

    private static void connectToDBTest() {
        String url = String.format("jdbc:postgresql://localhost:5432/%s",responceToConnection[1]);
        String user = responceToConnection[2];
        String password = responceToConnection[3];
        String jdbcDriver = "org.postgresql.Driver";
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTableTest() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE usertest");
        } catch (SQLException e) {
            System.err.println("Если вы получили это исключение, протестировав ТОЛЬКО model.exit(), - не страшно.\n" +
                    "В процессе теста вы и так закрыли коннекшн");
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Если вы получили это исключение, протестировав ТОЛЬКО model.exit(), - не страшно.\n" +
                    "В процессе теста вы и так закрыли коннекшн");
            e.printStackTrace();
        }
    }

    @Test
    public void connect() {
        boolean allRight = false;
        model = new PostgreModel();
        try {
            model.connect(responceToConnection);
            allRight = true;
        } catch (CreatedInModelException e) {
            //do nothing
        }


        assertTrue(allRight);
    }

    @Test
    public void create() {
        boolean theTableIsCreated = false;
        String[] responceToDB = new String[]{"create", "usertest", "firstname", "secondname", "password"};

        try {
            model.create(responceToDB);
            theTableIsCreated = true;
        } catch (CreatedInModelException e) {
            //do nothing;
        }

        assertTrue(theTableIsCreated);
        deleteTableTest();
    }

    @Test
    public void tables() {
        createTableTest();
        String expected = "[[usertest]]";
        String actual = "";

        try {
            actual = Arrays.asList(model.tables()).toString();
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertEquals(expected, actual);
        deleteTableTest();
    }

    @Test
    public void clear() {
        boolean isThisClear = false;
        String[] responceToDB = new String[]{"clear", "usertest"};
        createTableTest();

        try {
            model.clear(responceToDB);
            isThisClear = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isThisClear);
        deleteTableTest();
    }

    @Test
    public void drop() {
        boolean isThisDroped=false;
        String[] responceToDB = new String[]{"drop", "usertest"};
        createTableTest();

        try {
            model.drop(responceToDB);
            isThisDroped = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isThisDroped);
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

        assertTrue(modelIsExit);
    }

    @Test
    public void insert() {
        String[] sqlRequest = new String[]
                {"insert", "usertest", "firstname", "John", "secondname", "Dou", "password", "123"};
        boolean isInserted = false;
        createTableTest();

        try {
            model.insert(sqlRequest);
            isInserted = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isInserted);
        deleteTableTest();
    }

    @Test
    public void update() {
        String[] sqlRequest = new String[]{
                "update", "usertest",
                "password", "123",
                "firstname", "John2",
                "secondname", "Dou2",
                "password", "123456"};
        boolean isUpdated = false;
        createTableTest();
        insertDataIntoTableTest();

        try {
            model.update(sqlRequest);
            isUpdated = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isUpdated);
        deleteTableTest();
    }

    @Test
    public void delete() {
        boolean isDeleted = false;
        String[] sqlRequest = new String[]{"delete", "usertest", "password", "123"};
        createTableTest();
        insertDataIntoTableTest();

        try {
            model.delete(sqlRequest);
            isDeleted = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isDeleted);
        deleteTableTest();
    }

    @Test
    public void getColumnNameForFind() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        ArrayList<String> actual;
        String[] sqlRequest = new String[]{"find", "usertest"};
        createTableTest();
        insertDataIntoTableTest();

        actual = (ArrayList<String>) model.getColumnNameForFind(sqlRequest);

        assertEquals(expected, actual);
        deleteTableTest();
    }


    @Test
    public void getColumnValuesForFind() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "1", "John", "Dou", "123"));
        ArrayList<String> actual;
        String[] sqlRequest = new String[]{"find", "usertest"};
        createTableTest();
        insertDataIntoTableTest();

        actual = (ArrayList<String>) model.getColumnValuesForFind(sqlRequest);

        assertEquals(expected, actual);
        deleteTableTest();
    }


    @Test
    public void getColumnNameForUpdateOrDelete() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        ArrayList<String> actual;
        String[] sqlRequest = new String[]{"delete", "usertest", "password", "123"};
        createTableTest();
        insertDataIntoTableTest();

        actual = (ArrayList<String>) model.getColumnNameForUpdateOrDelete(sqlRequest);

        assertEquals(expected, actual);
        deleteTableTest();
    }

    @Test
    public void getColumnValuesForUpdateOrDelete() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "1", "John", "Dou", "123"));
        ArrayList<String> actual;
        String[] sqlRequest = new String[]{"delete", "usertest", "password", "123"};
        createTableTest();
        insertDataIntoTableTest();

        actual = (ArrayList<String>) model.getColumnValuesForUpdateOrDelete(sqlRequest);

        assertEquals(expected, actual);
        deleteTableTest();
    }

    private static void createTableTest() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE usertest " +
                    " (id SERIAL, firstname VARCHAR (225), secondname VARCHAR (225), password VARCHAR(225))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDataIntoTableTest() {
        try (Statement statement = connection.createStatement()) {
            statement.execute
                    ("INSERT INTO usertest (firstname, secondname, password)" +
                            " VALUES ('John','Dou','123')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

