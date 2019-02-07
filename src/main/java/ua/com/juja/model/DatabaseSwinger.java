package ua.com.juja.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DatabaseSwinger {
    private String propertiesPath;
    private String url;
    private String dbName;
    private String user;
    private String password;
    private String driver;

    public DatabaseSwinger(String propertiesPath) {
        this.propertiesPath = propertiesPath;
        getParametersFromFile();
    }

    private void getParametersFromFile(){
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream(propertiesPath)) {
            property.load(fis);

        } catch (FileNotFoundException e) {
            System.err.println("ОШИБКА!!! Файл настроек не найден");
        } catch (IOException e) {
            e.printStackTrace();
        }

        url = property.getProperty("db.url");
        dbName = property.getProperty("db.dbname");
        user = property.getProperty("db.user");
        password = property.getProperty("db.password");
        driver = property.getProperty("db.driver");
    }

    public String getUrl() {
        return url;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
