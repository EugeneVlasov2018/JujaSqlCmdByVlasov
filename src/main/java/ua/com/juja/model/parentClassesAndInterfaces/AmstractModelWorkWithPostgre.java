package ua.com.juja.model.parentClassesAndInterfaces;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import org.postgresql.util.PSQLException;
import ua.com.juja.model.newExceptions.NullableAnswerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AmstractModelWorkWithPostgre implements ModelInterface {


    protected void requestWithoutAnswer(Connection connectionToDb, String sqlRequest) throws SQLException,
            NullPointerException {
        if (connectionToDb.equals(null)) {
            throw new NullPointerException();
        } else {
            try(Statement statement = connectionToDb.createStatement()) {
                statement.execute(sqlRequest);
            }
        }
    }

    protected String requestWithAnswer(Connection connectionToDatabase, String sqlRequestForWork, String requestForTable)
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
}
