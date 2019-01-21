package ua.com.juja.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ua.com.juja.model.exceptions.UnknowShitException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PostgreModelTest {
    private Model model;
    private String[] responceToConnection = new String[]{"connect", "testforsql", "postgres", "root"};
    private Connection connection;


    @Before
    public void setUp() {
        model = new PostgreModel();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void connect() {
        Boolean isConnected;
        try {
            model.connect(responceToConnection);
            isConnected = true;
        } catch (UnknowShitException e) {
            isConnected = false;
        }
        assertTrue(isConnected);
    }

    @Test
    public void create() {
        boolean theTableIsCreated;
        String[] responceToDB = new String[]{"create", "usertest", "firstname", "secondname", "password"};
        try {
            model.connect(responceToConnection);
            model.create(responceToDB);
            theTableIsCreated = true;
        } catch (UnknowShitException e) {
            e.printStackTrace();
            theTableIsCreated = false;
        }
        connectToDBTest();
        deleteTableTest();

        assertTrue(theTableIsCreated);
    }

    @Test
    public void tables() {
        prepareDataBaseWithoutData();
        String expected = "usertest";
        String actual = "";
        try {
            model.connect(responceToConnection);
            actual = model.tables().get(0);
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        deleteTableTest();
        assertEquals(expected, actual);
    }

    @Test
    public void clear() {
        boolean isThisClear;
        String[] responceToDB = new String[]{"clear", "usertest"};
        prepareDataBaseWithoutData();
        try {
            model.connect(responceToConnection);
            model.clear(responceToDB);
            isThisClear = true;
        } catch (UnknowShitException e) {
            isThisClear = false;
            e.printStackTrace();
        }
        deleteTableTest();
        assertTrue(isThisClear);
    }

    @Test
    public void drop() {
        boolean isThisDroped;
        String[] responceToDB = new String[]{"drop", "usertest"};
        prepareDataBaseWithoutData();
        try {
            model.connect(responceToConnection);
            model.drop(responceToDB);
            isThisDroped = true;
        } catch (UnknowShitException e) {
            isThisDroped = false;
            e.printStackTrace();
        }
        assertTrue(isThisDroped);
    }

    @Test
    public void exit() {
        boolean modelIsExit;
        try {
            model.connect(responceToConnection);
            model.exit();
            modelIsExit = true;
        } catch (UnknowShitException e) {
            modelIsExit = false;
            e.printStackTrace();
        }
        assertTrue(modelIsExit);

    }

    @Test
    public void insert() {
        String[] sqlRequest = new String[]
                {"insert", "usertest", "firstname", "John", "secondname", "Dou", "password", "123"};
        boolean isInserted;
        prepareDataBaseWithoutData();
        try {
            model.connect(responceToConnection);
            model.insert(sqlRequest);
            isInserted = true;
        } catch (UnknowShitException e) {
            isInserted = false;
            e.printStackTrace();
        }
        deleteTableTest();
        assertTrue(isInserted);
    }

    @Test
    public void update() {
        String[] sqlRequest = new String[]{
                "update", "usertest",
                "password", "123",
                "firstname", "John2",
                "secondname", "Dou2",
                "password", "123456"};
        boolean isUpdated;
        prepareDataBaseWithData();
        try {
            model.connect(responceToConnection);
            model.update(sqlRequest);
            isUpdated = true;
        } catch (UnknowShitException e) {
            isUpdated = false;
            e.printStackTrace();
        }
        deleteTableTest();
        assertTrue(isUpdated);
    }

    @Test
    public void delete() {
        boolean isDeleted;
        String[] sqlRequest = new String[]{"delete", "usertest", "password", "123"};
        prepareDataBaseWithData();
        try {
            model.connect(responceToConnection);
            model.delete(sqlRequest);
            isDeleted = true;
        } catch (UnknowShitException e) {
            isDeleted = false;
            e.printStackTrace();
        }
        deleteTableTest();
        assertTrue(isDeleted);
    }

    @Test
    public void getColumnNameForFind() {
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        ArrayList<String> actual = new ArrayList<>();
        String[] sqlRequest = new String[]{"find", "usertest"};
        prepareDataBaseWithData();
        try {
            model.connect(responceToConnection);
            actual = (ArrayList<String>) model.getColumnNameForFind(sqlRequest);
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        deleteTableTest();
        assertEquals(expected, actual);
    }

    @Test
    public void getColumnValuesForFind() {
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
                "1", "John", "Dou", "123"));
        ArrayList<String> actual = new ArrayList<>();
        String[] sqlRequest = new String[]{"find", "usertest"};
        prepareDataBaseWithData();
        try {
            model.connect(responceToConnection);
            actual = (ArrayList<String>) model.getColumnValuesForFind(sqlRequest);
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        deleteTableTest();
        assertEquals(expected, actual);
    }

    @Test
    public void getColumnNameForUpdateOrDelete() {
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        ArrayList<String> actual = new ArrayList<>();
        String[] sqlRequest = new String[]{"delete", "usertest", "password", "123"};
        prepareDataBaseWithData();
        try {
            model.connect(responceToConnection);
            actual = (ArrayList<String>) model.getColumnNameForUpdateOrDelete(sqlRequest);
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        deleteTableTest();
        assertEquals(expected, actual);
    }

    @Test
    public void getColumnValuesForUpdateOrDelete() {
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(
                "1", "John", "Dou", "123"));
        ArrayList<String> actual = new ArrayList<>();
        String[] sqlRequest = new String[]{"delete", "usertest", "password", "123"};
        prepareDataBaseWithData();
        try {
            model.connect(responceToConnection);
            actual = (ArrayList<String>) model.getColumnValuesForUpdateOrDelete(sqlRequest);
        } catch (UnknowShitException e) {
            e.printStackTrace();
        }
        deleteTableTest();
        assertEquals(expected, actual);
    }

    private void connectToDBTest() {
        String url = "jdbc:postgresql://localhost:5432/testforsql";
        String user = "postgres";
        String password = "root";
        String jdbcDriver = "org.postgresql.Driver";
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTableTest() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE usertest");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableTest() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE usertest" +
                    " (id SERIAL, firstname VARCHAR (225), secondname VARCHAR (225), password VARCHAR(225))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDataIntoTableTest() {
        try (Statement statement = connection.createStatement()) {
            statement.execute
                    ("INSERT INTO usertest (firstname, secondname, password)" +
                            " VALUES ('John','Dou','123')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareDataBaseWithoutData() {
        connectToDBTest();
        createTableTest();
    }

    private void prepareDataBaseWithData() {
        connectToDBTest();
        createTableTest();
        insertDataIntoTableTest();
    }
}

