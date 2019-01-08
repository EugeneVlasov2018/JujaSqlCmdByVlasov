package ua.com.juja.model;

import org.apache.log4j.Logger;
import ua.com.juja.model.exceptions.UnknowShitException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreModel implements Model {
    private Connection connectionToDatabase;

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    private static String getCurrentClassName() {
        return "ClassPostgreModel";
    }

    @Override
    public void connect(String[] responceToDb) throws UnknowShitException {
        String url = "jdbc:postgresql://localhost:5432/" + responceToDb[1];
        String user = responceToDb[2];
        String password = responceToDb[3];
        String jdbcDriver = "org.postgresql.Driver";
        connectionToDatabase = createConnection(url, user, password, jdbcDriver);
    }

    private Connection createConnection(String url, String user, String password, String jdbcDriver)
            throws UnknowShitException {

        try {
            Class.forName(jdbcDriver);
            connectionToDatabase = DriverManager.getConnection(url, user, password);
            return connectionToDatabase;
        } catch (SQLException e) {
            StringBuilder resultForView = new StringBuilder("Вы ввели: ");
            if (e.getSQLState().equalsIgnoreCase("3D000")) {
                resultForView.append("\nНеверную ссылку на базу");
            }
            if (e.getSQLState().equalsIgnoreCase("28P01")) {
                resultForView.append("\nНеверное имя пользователя или пароль");
            }
            resultForView.append("\nПопробуйте снова:P");
            throw new UnknowShitException(resultForView.toString());
        } catch (ClassNotFoundException a) {
            throw new UnknowShitException("Не найден драйвер подключения к базе\n" +
                    "Передайте разработчику, чтобы добавил либо джарник в либы, либо депенденс в мавен");
        }
    }

    @Override
    public void create(String[] params) throws UnknowShitException {

        StringBuilder sqlRequest = new StringBuilder("CREATE TABLE " + params[1] + " (id SERIAL, ");
        for (int i = 2; i < params.length; i++) {
            sqlRequest = sqlRequest.append(params[i]).append(" VARCHAR(255), ");
        }
        sqlRequest = sqlRequest.append("PRIMARY KEY (id))");
        workWithDbWithoutAnswer(sqlRequest.toString());
    }

    @Override
    public List<String> tables() throws UnknowShitException {
        ArrayList<String> tablenames = new ArrayList<String>();

        DatabaseMetaData databaseMetaData = null;
        ResultSet resultSet = null;

        try {
            if (connectionToDatabase == null) {
                throw new UnknowShitException("Вы попытались очистить таблицу, не подключившись к базе данных.\n" +
                        "Подключитесь к базе данных командой\n" +
                        "'connect|database|username|password'");
            }
            databaseMetaData = connectionToDatabase.getMetaData();
            resultSet = databaseMetaData.getTables(null, null, "%",
                    new String[]{"TABLE"});
            while (resultSet.next()) {
                tablenames.add(resultSet.getString("TABLE_NAME"));
            }
            if (tablenames.size() > 0) {
                return tablenames;
            } else {
                throw new UnknowShitException("В базе данных нет ни одной таблицы");
            }
        } catch (SQLException a) {
            throw new UnknowShitException(String.format("ошибка в работе с Базой данныхю причина: %s",a.getMessage()));
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                //do nothing
            }
        }
    }

    @Override
    public void clear(String[] params) throws UnknowShitException {
        String sqlRequest = "DELETE FROM " + params[1];
        workWithDbWithoutAnswer(sqlRequest);
    }

    @Override
    public void drop(String[] params) throws UnknowShitException {
        String sqlRequest = "DROP TABLE ".concat(params[1]);
        workWithDbWithoutAnswer(sqlRequest);

    }

    @Override
    public void exit() throws UnknowShitException {
        if (connectionToDatabase != null) {
            try {
                connectionToDatabase.close();
            } catch (SQLException e) {
                throw new UnknowShitException(String.format("Не удалось закрыть подключение к базе.\n" +
                        "Причина: %s", e.getMessage()));
            }
        }
    }

    @Override
    public void insert(String[] params) throws UnknowShitException {
        StringBuilder mainSqlRequest = new StringBuilder("INSERT INTO ");
        //вбиваем в запрос имя базы
        mainSqlRequest.append(params[1]).append(" (");
        List<String> columnNames = new ArrayList<String>();
        List<String> columnValues = new ArrayList<String>();
        //остальную часть запроса бросаем в 2 аррая,- для названия колонок
        //и для параметров
        for (int index = 2; index < params.length; index++) {
            if (index % 2 == 0) {
                columnNames.add(params[index].trim());
            } else {
                columnValues.add(params[index].trim());
            }
        }
        //добавляем к запросу имена колонок
        for (int index = 0; index < columnNames.size(); index++) {
            //ниже идет такой себе костыль. Чтобы отрефракторить, - лучше разобраться с .substring()
            if (index != columnNames.size() - 1)
                mainSqlRequest.append(columnNames.get(index)).append(", ");
            else
                mainSqlRequest.append(columnNames.get(index));
        }
        //добавляем к запросу имена параметров
        mainSqlRequest.append(")VALUES ('");
        for (int index = 0; index < columnValues.size(); index++) {
            //ниже идет такой себе костыль. Чтобы отрефракторить, - лучше разобраться с .substring()
            if (index != columnNames.size() - 1)
                mainSqlRequest.append(columnValues.get(index)).append("', '");
            else
                mainSqlRequest.append(columnValues.get(index)).append("'");
        }
        mainSqlRequest.append(")");

        workWithDbWithoutAnswer(mainSqlRequest.toString());

    }

    @Override
    public void update(String[] params) throws UnknowShitException {
        StringBuilder sqlRequestForWork = new StringBuilder("UPDATE ").append(params[1]).append(" SET ");
        for (int index = 4; index < params.length; index++) {
            if (index % 2 == 0)
                sqlRequestForWork.append(params[index] + " = '");
            else
                sqlRequestForWork.append(params[index] + "', ");
        }
        sqlRequestForWork.setLength(sqlRequestForWork.length() - 2);
        sqlRequestForWork.append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");

        workWithDbWithoutAnswer(sqlRequestForWork.toString());
    }

    @Override
    public void delete(String[] params) throws UnknowShitException {
        //Готовим запрос для удаления данных, подходящих под условия
        String sqlForWork = String.format("DELETE FROM %s WHERE %s ='%s'", params[1], params[2], params[3]);
        workWithDbWithoutAnswer(sqlForWork);
    }

    @Override
    public void workWithDbWithoutAnswer(String sqlRequest) throws UnknowShitException {

        if (connectionToDatabase == null) {
            throw new UnknowShitException("Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password'");
        } else {
            try (Statement statement = connectionToDatabase.createStatement()) {
                statement.execute(sqlRequest);
            } catch (SQLException e) {
                throw new UnknowShitException(String.format("Ошибка в работе с базой данных. Причина: %s", e.getMessage()));
            }
        }
    }

    @Override
    public List<String> getColumnNameForFind(String[] command) throws UnknowShitException {
        List<String> responceWithColumnNames = new ArrayList<>();
        String sqlRequest = String.format("SELECT * FROM %s", command[1]);
        responceWithColumnNames = getColumnNamesFromDB(sqlRequest);

        return responceWithColumnNames;
    }

    @Override
    public List<String> getColumnValuesForFind(String[] command) throws UnknowShitException {
        List<String> responceWithColumnValues = new ArrayList<>();
        String sqlRequest = String.format("SELECT * FROM %s", command[1]);
        responceWithColumnValues = getColumnValuesFromDB(sqlRequest);

        return responceWithColumnValues;
    }

    @Override
    public List<String> getColumnNameForUpdateOrDelete(String[] command) throws UnknowShitException {
        List<String> columnNames = new ArrayList<>();
        String sqlRequest = String.format("SELECT * FROM %s WHERE %s ='%s'", command[1], command[2], command[3]);
        columnNames = getColumnNamesFromDB(sqlRequest);
        return columnNames;
    }

    @Override
    public List<String> getColumnValuesForUpdateOrDelete(String[] command) throws UnknowShitException {
        List<String> columnValues = new ArrayList<>();
        String sqlRequest = String.format("SELECT * FROM %s WHERE %s ='%s'", command[1], command[2], command[3]);
        columnValues = getColumnValuesFromDB(sqlRequest);
        return columnValues;
    }

    @Override
    public List<String> getColumnNamesFromDB(String responceToDB) throws UnknowShitException {

        List<String> responceWithColumnNames = new ArrayList<>();
        if (connectionToDatabase == null) {
            throw new UnknowShitException("Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password'");
        }
        try (Statement statement = connectionToDatabase.createStatement();
             ResultSet resultSet = statement.executeQuery(responceToDB)) {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++)
                responceWithColumnNames.add(rsmd.getColumnName(i));

        } catch (SQLException a) {
            throw new UnknowShitException(String.format("Ошибка в работе с базой данных. Причина: %s",a.getMessage()));
        }
        return responceWithColumnNames;
    }

    @Override
    public List<String> getColumnValuesFromDB(String responceToDB) throws UnknowShitException {
        if (connectionToDatabase == null) {
            throw new UnknowShitException("Вы попытались выполнить работу, не подключившись к базе данных.\n" +
                    "Подключитесь к базе данных командой\n" +
                    "'connect|database|username|password'");
        }
        ArrayList<String> responceWithColumnValues = new ArrayList<>();
        try (Statement statement = connectionToDatabase.createStatement();
             ResultSet resultSet = statement.executeQuery(responceToDB)) {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int index = 1; index <= columnCount; index++)
                    responceWithColumnValues.add(resultSet.getString(index));
            }
        } catch (SQLException a) {
            throw new UnknowShitException(String.format("Ошибка в работе с базой данных. Причина: %s",a.getMessage()));
        }
        return responceWithColumnValues;
    }
}

