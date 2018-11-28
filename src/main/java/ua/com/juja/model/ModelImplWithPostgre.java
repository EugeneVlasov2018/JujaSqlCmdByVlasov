package ua.com.juja.model;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import ua.com.juja.model.newExceptions.NullableAnswerException;
import ua.com.juja.model.newExceptions.UnknowColumnNameException;
import ua.com.juja.model.newExceptions.UnknowTableException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModelImplWithPostgre implements ModelInterface {

    @Override
    public void create(String[] params, Connection connectionToDatabase) throws UnknowTableException, NullPointerException {

        StringBuilder sqlRequest = new StringBuilder("CREATE TABLE " + params[1] + " (id SERIAL, ");
        for (int i = 2; i < params.length; i++) {
            sqlRequest = sqlRequest.append(params[i]).append(" VARCHAR(255), ");
        }
        sqlRequest = sqlRequest.append("PRIMARY KEY (id))");
        try {
            requestWithoutAnswer(connectionToDatabase, sqlRequest.toString());
        } catch (SQLException e){
            throw new UnknowTableException();
        }
    }

    @Override
    public ArrayList<String> tables(Connection connectionToDatabase) throws SQLException, NullPointerException,
            NullableAnswerException {
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
    public void clear(String[] params, Connection connectionToDatabase) throws UnknowTableException, NullPointerException {
        String sqlRequest = "DELETE FROM " + params[1];
        try{
            requestWithoutAnswer(connectionToDatabase, sqlRequest);
        } catch (SQLException sqlExc){
            throw new UnknowTableException();
        }
    }

    @Override
    public void drop(String[] params, Connection connectionToDatabase) throws UnknowTableException, NullPointerException,
            SQLException {
        String sqlRequest = "DROP TABLE ".concat(params[1]);
        try{
        requestWithoutAnswer(connectionToDatabase, sqlRequest);
        } catch (SQLException e) {
            if (e.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else {
                throw new SQLException();
            }
        }
    }

    @Override
    public String find(String[] params, Connection connectionToDatabase) throws UnknowTableException, NullPointerException {
        //формируем запрос
        StringBuilder sqlRequestForTable = new StringBuilder("SELECT * FROM ");
        sqlRequestForTable.append(params[1]);
        String answer = "";
        try {
            answer = requestWithAnswer(connectionToDatabase, sqlRequestForTable.toString(), sqlRequestForTable.toString());
        } catch (SQLException a){
            throw new UnknowTableException();
        }
        return answer;
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

        try{
            requestWithoutAnswer(connectionToDatabase, mainSqlRequest.toString());
        } catch (SQLException e){
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
    public String update(String[] params, Connection connectionToDatabase) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException, NullPointerException {
        //UPDATE table SET column1 = value1, column2 = value2 ,... WHERE condition;
        //Формирование запроса для получения данных для таблицы
        StringBuilder sqlRequestForTable = new StringBuilder("SELECT * FROM ").append(params[1]).
                append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");
        StringBuilder sqlRequestForWork = new StringBuilder("UPDATE ").append(params[1]).append(" SET ");
        for (int index = 4; index < params.length; index++) {
            if (index % 2 == 0)
                sqlRequestForWork.append(params[index] + " = '");
            else
                sqlRequestForWork.append(params[index] + "', ");
        }
        sqlRequestForWork.setLength(sqlRequestForWork.length() - 2);
        //Формирование запроса для работы метода
        sqlRequestForWork.append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");

        String answer = "";
        try {
            answer = "Были изменены следующие строки:\n" + requestWithAnswer(connectionToDatabase, sqlRequestForWork.toString(), sqlRequestForTable.toString());
        } catch (SQLException e){
            if (e.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            }
            else if (e.getSQLState().equals("02000")) {
                throw new NullableAnswerException();
            }
            else if(e.getSQLState().equals("42703")) {
                throw new UnknowColumnNameException();
            }
        }
        return answer;
    }

    @Override
    public String delete(String[] params, Connection connectionToDatabase) throws UnknowTableException,
            UnknowColumnNameException, NullableAnswerException,NullPointerException {
        //Готовим запрос для вывода таблицыю
        StringBuilder sqlReqForTable = new StringBuilder("SELECT * FROM ").append(params[1]).
                append(" WHERE ").append(params[2]).append(" ='" + params[3] + "'");
        //Готовим запрос для удаления данных, подходящих под условия
        StringBuilder sqlForWork = new StringBuilder("DELETE FROM ".concat(params[1].concat(" WHERE ").
                concat(params[2]).concat(" ='" + params[3] + "'")));
        String answer = "";
        try {
            answer =  "Были удалены следующие строки:\n"
                    + requestWithAnswer(connectionToDatabase, sqlForWork.toString(),
                    sqlReqForTable.toString());
        } catch (SQLException a) {
            if (a.getSQLState().equals("42P01")) {
                throw new UnknowTableException();
            } else if (a.getSQLState().equals("02000")) {
                throw new NullableAnswerException();
            } else if (a.getSQLState().equals("42703")) {
                throw new UnknowColumnNameException();
            }
        }
        return answer;
    }

    private void requestWithoutAnswer(Connection connectionToDb, String sqlRequest) throws SQLException,
            NullPointerException {
        if (connectionToDb.equals(null)) {
            throw new NullPointerException();
        } else {
            try (Statement statement = connectionToDb.createStatement()) {
                statement.execute(sqlRequest);
            }
        }
    }

    public String requestWithAnswer(Connection connectionToDatabase, String sqlRequestForWork, String requestForTable)
            throws NullPointerException, SQLException {
        if (connectionToDatabase == null) {
            throw new NullPointerException();
        } else {

            List<String> arrayForTableNames = new ArrayList<>();
            List<String> arrayForTableValues = new ArrayList<>();

            try (Statement statement = connectionToDatabase.createStatement();
                 ResultSet resultSet = statement.executeQuery(requestForTable)) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++)
                    arrayForTableNames.add(rsmd.getColumnName(i));

                while (resultSet.next()) {
                    for (int i = 0; i < arrayForTableNames.size(); i++)
                        arrayForTableValues.add(resultSet.getString(arrayForTableNames.get(i)));
                }

                requestWithoutAnswer(connectionToDatabase, sqlRequestForWork);

                return createTable(arrayForTableNames, arrayForTableValues);
            }
        }
    }

    protected String createTable(List<String> columnNames, List<String> columnValues) {
        //Приводим таблицу к определенному шаблону
        Table tableForOutput = new Table(columnNames.size(),
                BorderStyle.CLASSIC, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        //Забиваем наименования колонок в таблицу
        for (String header : columnNames) {
            tableForOutput.addCell(header);
        }
        //Заполняем таблицу данными
        for (String row : columnValues) {
            tableForOutput.addCell(row);
        }
        //возвращаем таблицу в виде строки
        return tableForOutput.render();
    }

    @Override
    public ArrayList<String> getColumnName(String[] command, Connection connectionToDatabase)
            throws UnknowTableException, UnknowColumnNameException, NullableAnswerException, NullPointerException {
        ArrayList<String> answer = new ArrayList<>();
        if (command.length > 2 && command[0].equalsIgnoreCase("tables")) {
            String requestToDB = String.format("SELECT * FROM %s", command[1]);
            try (Statement statement = connectionToDatabase.createStatement();
                 ResultSet resultSet = statement.executeQuery(requestToDB)) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for (int i = 1; i <= columnCount; i++)
                    answer.add(rsmd.getColumnName(i));
            } catch (SQLException a) {
                if (a.getSQLState().equals("42P01")) {
                    throw new UnknowTableException();
                } else if (a.getSQLState().equals("42703")) {
                    throw new UnknowColumnNameException();
                } else {
                    throw new SQLException();
                }
            }
        }
        return answer;
    }

    @Override
    public ArrayList<String> getColumnValues(String[] command, Connection connectionToDatabase) {
        return null;
    }
}

