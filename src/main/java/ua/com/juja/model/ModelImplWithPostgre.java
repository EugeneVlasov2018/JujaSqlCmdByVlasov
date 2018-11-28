package ua.com.juja.model;

import ua.com.juja.model.exceptions.NullableAnswerException;
import ua.com.juja.model.exceptions.UnknowColumnNameException;
import ua.com.juja.model.exceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModelImplWithPostgre implements ModelInterface {

    @Override
    public void create(String[] params, Connection connectionToDatabase)
            throws UnknowTableException, NullPointerException {

        StringBuilder sqlRequest = new StringBuilder("CREATE TABLE " + params[1] + " (id SERIAL, ");
        for (int i = 2; i < params.length; i++) {
            sqlRequest = sqlRequest.append(params[i]).append(" VARCHAR(255), ");
        }
        sqlRequest = sqlRequest.append("PRIMARY KEY (id))");
        try {
            workWithDbWithoutAnswer(connectionToDatabase, sqlRequest.toString());
        } catch (SQLException e) {
            throw new UnknowTableException();
        }
    }

    @Override
    public ArrayList<String> tables(Connection connectionToDatabase)
            throws SQLException, NullPointerException, NullableAnswerException {
        ArrayList<String> tablenames = new ArrayList<String>();

        DatabaseMetaData databaseMetaData = null;
        ResultSet resultSet = null;

        try {
            if (connectionToDatabase == null) {
                throw new NullPointerException();
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
                throw new NullableAnswerException();
            }
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clear(String[] params, Connection connectionToDatabase)
            throws UnknowTableException, NullPointerException {
        String sqlRequest = "DELETE FROM " + params[1];
        try {
            workWithDbWithoutAnswer(connectionToDatabase, sqlRequest);
        } catch (SQLException sqlExc) {
            throw new UnknowTableException();
        }
    }

    @Override
    public void drop(String[] params, Connection connectionToDatabase)
            throws UnknowTableException, NullPointerException, SQLException {
        String sqlRequest = "DROP TABLE ".concat(params[1]);
        try {
            workWithDbWithoutAnswer(connectionToDatabase, sqlRequest);
        } catch (SQLException e) {
            if (e.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else {
                throw new SQLException();
            }
        }
    }

    @Override
    public void insert(String[] params, Connection connectionToDatabase) throws UnknowTableException,
            UnknowColumnNameException, NullPointerException {
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

        try {
            workWithDbWithoutAnswer(connectionToDatabase, mainSqlRequest.toString());
        } catch (SQLException e) {
            if (e.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else if (e.getSQLState().equals("42703")) {
                throw new UnknowColumnNameException();
            } else {
                // do nothing
            }
        }
    }

    @Override
    public void update(String[] params, Connection connectionToDatabase) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException, NullPointerException {
        //формирование запроса для изменения таблицы
        StringBuilder sqlRequestForWork = new StringBuilder("UPDATE ").append(params[1]).append(" SET ");
        for (int index = 4; index < params.length; index++) {
            if (index % 2 == 0)
                sqlRequestForWork.append(params[index] + " = '");
            else
                sqlRequestForWork.append(params[index] + "', ");
        }
        sqlRequestForWork.setLength(sqlRequestForWork.length() - 2);
        sqlRequestForWork.append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");


        try {
            workWithDbWithoutAnswer(connectionToDatabase, sqlRequestForWork.toString());
        } catch (SQLException e) {
            if (e.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else if (e.getSQLState().equals("02000")) {
                throw new NullableAnswerException();
            } else if (e.getSQLState().equals("42703")) {
                throw new UnknowColumnNameException();
            }
        }
    }

    @Override
    public void delete(String[] params, Connection connectionToDatabase) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException, NullPointerException, SQLException {
        //Готовим запрос для удаления данных, подходящих под условия
        String sqlForWork = String.format("DELETE FROM %s WHERE %s ='%s'", params[1], params[2], params[3]);
        try {
            workWithDbWithoutAnswer(connectionToDatabase, sqlForWork);
        } catch (SQLException a) {
            if (a.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else if (a.getSQLState().equals("02000")) {
                throw new NullableAnswerException();
            } else if (a.getSQLState().equals("42703")) {
                throw new UnknowColumnNameException();
            } else {
                throw new SQLException(a.getMessage());
            }
        }
    }

    @Override
    public void workWithDbWithoutAnswer(Connection connectionToDb, String sqlRequest) throws SQLException,
            NullPointerException {
        if (connectionToDb.equals(null)) {
            throw new NullPointerException();
        } else {
            try (Statement statement = connectionToDb.createStatement()) {
                statement.execute(sqlRequest);
            }
        }
    }

    @Override
    public ArrayList<String> getColumnNameForFind(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException {
        String sqlRequest = String.format("SELECT * FROM %s", command[1]);
        ArrayList<String> responceWithColumnNames = getColumnNamesFromDB(sqlRequest, connectionToDatabase);

        return responceWithColumnNames;
    }

    @Override
    public ArrayList<String> getColumnValuesForFind(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException {
        String sqlRequest = String.format("SELECT * FROM %s", command[1]);
        ArrayList<String> responceWithColumnValues = getColumnValuesFromDB(sqlRequest, connectionToDatabase);

        return responceWithColumnValues;
    }

    @Override
    public ArrayList<String> getColumnNameForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException {
        String sqlRequest = String.format("SELECT * FROM %s WHERE %s ='%s'", command[1], command[2], command[3]);
        ArrayList<String> columnNames = getColumnNamesFromDB(sqlRequest, connectionToDatabase);
        return columnNames;
    }

    @Override
    public ArrayList<String> getColumnValuesForUpdateOrDelete(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException {
        String sqlRequest = String.format("SELECT * FROM %s WHERE %s ='%s'", command[1], command[2], command[3]);
        ArrayList<String> columnValues = getColumnValuesFromDB(sqlRequest, connectionToDatabase);
        return columnValues;
    }

    @Override
    public ArrayList<String> getColumnNamesFromDB(String responceToDB, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException {

        ArrayList<String> responceWithColumnNames = new ArrayList<>();
        try (Statement statement = connectionToDatabase.createStatement();
             ResultSet resultSet = statement.executeQuery(responceToDB)) {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++)
                responceWithColumnNames.add(rsmd.getColumnName(i));

        } catch (SQLException a) {
            if (a.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else if (a.getSQLState().equals("42703")) {
                throw new UnknowColumnNameException();
            } else {
                throw new SQLException(a.getMessage());
            }
        }
        return responceWithColumnNames;
    }

    @Override
    public ArrayList<String> getColumnValuesFromDB(String responceToDB, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException,
            SQLException {

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
            if (a.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else if (a.getSQLState().equals("42703")) {
                throw new UnknowColumnNameException();
            } else {
                throw new SQLException(a.getMessage());
            }
        }
        return responceWithColumnValues;
    }
}

