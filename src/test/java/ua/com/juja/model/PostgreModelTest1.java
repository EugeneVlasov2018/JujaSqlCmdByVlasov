package ua.com.juja.model;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;
import ua.com.juja.model.exceptions.CreatedInModelException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

public class PostgreModelTest1 extends DBTestCase {
    private PostgreModel model;
    private String[] requestToConnection;
    private Connection connection;
    private DatabaseSwinger swinger;

    public PostgreModelTest1(String name) {
        super(name);

        swinger = new DatabaseSwinger("src\\test\\resourses\\testDB1.properties");
        requestToConnection = createRequest();
        connection = createConnection();

        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_DRIVER_CLASS,swinger.getDriver());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_CONNECTION_URL,swinger.getUrl()+swinger.getDbName());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_USERNAME,swinger.getUser());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_PASSWORD,swinger.getPassword());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.
                DBUNIT_SCHEMA,swinger.getSchema());
    }

    private Connection createConnection() {
        Connection resultConnection = null;
        String url = swinger.getUrl()+swinger.getDbName();
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
        return new String[]{"connect",database,user,password};
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src\\test\\resourses\\usertest.xml"));
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        model = new PostgreModel(connection,swinger);
        return DatabaseOperation.REFRESH;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE_ALL;
    }

    public void testConnect() {
        boolean isConnected = false;
        model = new PostgreModel(null,swinger);

        try {
            model.connect(requestToConnection);
            isConnected = true;
        } catch (CreatedInModelException e) {
            e.printStackTrace();
        }

        assertTrue(isConnected);
    }


    public void testCreate() {
    }


    public void testTables() {
    }


    public void testClear() {
    }


    public void testDdrop() {
    }


    public void testExit() {
    }


    public void testInsert() {
    }


    public void testUpdate() {
    }


    public void testDelete() {
    }


    public void testGetColumnNameForFind() {
    }


    public void testGetColumnValuesForFind() {
    }


    public void testGetColumnNameForUpdateOrDelete() {
    }


    public void testGetColumnValuesForUpdateOrDelete() {
    }

}