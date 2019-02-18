package ua.com.juja.model;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import ua.com.juja.model.exceptions.CreatedInModelException;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;



public class PostgreModelTest extends DBTestCase {
    private PostgreModel model;
    private String[] requestToConnection;
    private Connection connection;
    private DatabaseSwinger swinger;

    public PostgreModelTest(String name) {
        super(name);

        swinger = new DatabaseSwinger("src\\test\\resourses\\testDBunit.properties");
        requestToConnection = createRequest();
        connection = createConnection();
        createTestTable();

        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_DRIVER_CLASS, swinger.getDriver());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_CONNECTION_URL, swinger.getUrl() + swinger.getDbName());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_USERNAME, swinger.getUser());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_PASSWORD, swinger.getPassword());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_SCHEMA, swinger.getSchema());
    }

    private void createTestTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE public.usertest " +
                            "(id serial PRIMARY KEY," +
                            " firstname varchar(225)," +
                            " secondname varchar(225)," +
                            " password varchar(225))");
        } catch (SQLException e) {
            e.printStackTrace();
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

    private Connection createConnection() {
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

    private String[] createRequest() {
        String database = swinger.getDbName();
        String user = swinger.getUser();
        String password = swinger.getPassword();
        return new String[]{"connect", database, user, password};
    }

    private void deleteThisTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE public.tablefortest");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void comparsionOfTables(String expectedTablePath) throws Exception {
        IDataSet actualDataSet = getConnection().createDataSet();
        ITable actualTable = actualDataSet.getTable("usertest");

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(expectedTablePath));
        ITable expectedTable = expectedDataSet.getTable("usertest");

        ITable filteredTable = DefaultColumnFilter.includedColumnsTable(
                actualTable,
                expectedTable.getTableMetaData().getColumns());

        Assertion.assertEquals(expectedTable, filteredTable);
    }

    private void catchingException(String[] command, String expectedMessage) {
        try {
            startMethod(command);
            Assert.fail("метод отработал корректно, - такого не должно быть");
        } catch (CreatedInModelException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }


    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream(
                "src\\test\\resourses\\tablesForPostgreModelTest\\maintable.xml"));
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        model = new PostgreModel(connection, swinger);
        return DatabaseOperation.CLEAN_INSERT;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE_ALL;
    }

    public void testConnect() {
        boolean isConnected = false;
        model = new PostgreModel(null, swinger);

        try {
            model.connect(requestToConnection);
            isConnected = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isConnected);
    }


    public void testCreate() {
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
        deleteThisTable();

        assertTrue(isCreated);
    }

    public void testTables() throws CreatedInModelException {
        String expected = "[[usertest]]";
        assertEquals(expected, Arrays.asList(model.tables()).toString());
    }


    public void testClear() throws Exception {
        String[] command = new String[]{"clear", "usertest"};
        String expectedTablePath = "src\\test\\resourses\\tablesForPostgreModelTest\\tableforclear.xml";
        model.clear(command);
        comparsionOfTables(expectedTablePath);
    }

    public void testDdrop() {
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


    public void testExit() {
        boolean modelIsExit = false;

        try {
            model.exit();
            modelIsExit = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(modelIsExit);
    }


    public void testInsert() throws Exception {
        String[] command = new String[]
                {"insert", "usertest", "firstname", "fn3", "secondname", "sn3", "password", "pass3"};
        model.insert(command);
        String expectedTablePath = "src\\test\\resourses\\tablesForPostgreModelTest\\tableforinsert.xml";
        comparsionOfTables(expectedTablePath);
    }


    public void testUpdate() throws Exception {
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


    public void testDelete() throws Exception {
        String[] command = new String[]{"delete", "usertest", "password", "pass1"};
        model.delete(command);
        String expectedTablePath = "src\\test\\resourses\\tablesForPostgreModelTest\\tablefordelete.xml";
        comparsionOfTables(expectedTablePath);
    }


    public void testGetColumnNameForFind() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        String[] command = new String[]{"find", "usertest"};
        ArrayList<String> actual = (ArrayList<String>) model.getColumnNameForFind(command);
        assertEquals(expected, actual);
    }


    public void testGetColumnValuesForFind() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "fn1", "sn1", "pass1", "fn2", "sn2", "pass2"));
        String[] command = new String[]{"find", "usertest"};
        ArrayList<String> actual = (ArrayList<String>) model.getColumnValuesForFind(command);
        //удалил значения id, которые постоянно меняются, т.к autoincrement (костыль)
        actual.remove(0);
        actual.remove(3);
        assertEquals(expected, actual);
    }


    public void testGetColumnNameForUpdateOrDelete() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "id", "firstname", "secondname", "password"));
        String[] command = new String[]{"delete", "usertest", "password", "pass1"};
        ArrayList<String> actual = (ArrayList<String>) model.getColumnNameForUpdateOrDelete(command);
        assertEquals(expected, actual);
    }


    public void testGetColumnValuesForUpdateOrDelete() throws CreatedInModelException {
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "fn1", "sn1", "pass1"));
        String[] command = new String[]{"delete", "usertest", "password", "pass1"};
        ArrayList<String> actual = (ArrayList<String>) model.getColumnValuesForUpdateOrDelete(command);
        //удалил значения id, которые постоянно меняются, т.к autoincrement (костыль)
        actual.remove(0);
        assertEquals(expected, actual);
    }

    public void testConnectWithWrongCommand() {
        model = new PostgreModel(null, swinger);
        String[] command = getWrongCommand(1, "wrondDb");
        String expectedMessage = "Вы ввели: \n" +
                "Неверную ссылку на базу\n" +
                "Попробуйте снова:P";
        catchingException(command, expectedMessage);

    }


    public void testConnectWithWrongUserOrPassword() {
        model = new PostgreModel(null, swinger);
        String[] command = getWrongCommand(2, "wrongUser");
        String expectedMessage = "Вы ввели: \n" +
                "Неверное имя пользователя или пароль\n" +
                "Попробуйте снова:P";
        catchingException(command, expectedMessage);
    }

    public void testDeleteWrongColumn() {
        String[] command = new String[]{"delete", "usertest", "wrongColumn", "pass1"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: " +
                "ERROR: column \"wrongcolumn\" does not exist\n" +
                "  Позиция: 28";
        catchingException(command, expectedMessage);
    }

    public void testDeleteWrongTable() {
        String[] command = new String[]{"delete", "wrongtable", "password", "pass1"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина: " +
                "ERROR: relation \"wrongtable\" does not exist\n" +
                "  Позиция: 13";
        catchingException(command, expectedMessage);
    }

    public void testDropNonExistentTable() {
        String[] command = new String[]{"drop","nonexistentTable"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина:" +
                " ERROR: table \"nonexistenttable\" does not exist";
        catchingException(command,expectedMessage);
    }

    public void testInsertNonExistentTable(){
        String[] command = new String[]{"insert","nonexistentTable","password","password3"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина:" +
                " ERROR: relation \"nonexistenttable\" does not exist\n" +
                "  Позиция: 13";
        catchingException(command,expectedMessage);;
    }

    public void testInsertNonExistentColumn(){
        String[] command = new String[]{"insert","usertest","wrongcolumn","password3"};
        String expectedMessage = "Ошибка в работе с базой данных. Причина:" +
                " ERROR: column \"wrongcolumn\" of relation \"usertest\" does not exist\n" +
                "  Позиция: 23";
        catchingException(command,expectedMessage);;
    }
}