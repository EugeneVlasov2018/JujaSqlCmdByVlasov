package ua.com.juja.model.parentClassesAndInterfaces;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import ua.com.juja.model.newExceptions.NullableAnswerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCreateRequestSenderAndResponseGetter {


    protected void requestWithoutAnswer(Connection connectionToDb, String sqlRequest) throws SQLException {
        Statement statement = connectionToDb.createStatement();
        statement.execute(sqlRequest);
        statement.close();
    }

    protected String requestWithAnswer(Connection connectionToDb, String sqlGetColNames, String sqlGetColValues, String adittionalSqlRequest)
            throws SQLException, NullableAnswerException {
        //Делаем 2 Аррая, - для имен колонок и их значений
        List<String> columnNames = new ArrayList<String>();
        List<String> columnValues = new ArrayList<String>();
        Statement statement = connectionToDb.createStatement();
        //Заполняем Аррай с именами колонок
        ResultSet resultSet = statement.executeQuery(sqlGetColNames);
        ResultSetMetaData rsMD = resultSet.getMetaData();
        int columnCount = rsMD.getColumnCount();
        for (int index = 1; index <= columnCount; index++) {
            columnNames.add(rsMD.getColumnName(index));
        }
        //Заполняем Аррай со значениями колонок
        resultSet = statement.executeQuery(sqlGetColValues);
        if(resultSet.isBeforeFirst()){
        while (resultSet.next()) {
                //ВСТАВИТЬ ПРОВЕРКУ НА НУЛЕВОЙ РЕЗАЛТСЕТ
                for (int index = 1; index <= columnCount; index++)
                    columnValues.add(resultSet.getString(index));
            }
            if (adittionalSqlRequest != null) {
                statement.execute(adittionalSqlRequest);
            } else {
                //do nothing;
            }
        }
        else throw new NullableAnswerException();
        resultSet.close();
        statement.close();
        //строим саму таблицу на основе 2х арраев
        return createTable(columnNames, columnValues);
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

    /*Этот метод можно не смотреть пока, - пытаюсь понять PrepareStatement - но пока не очень выходит)))
    protected String responseWithAnswerPrepSt (Connection connectionToDb, String sqlColumnsForTable,
                                               String sqlValuesForTable, String sqlForWorkWithDb,
                                               ArrayList<String> arrayColumnsForTable, ArrayList<String> arrayValuesForTable,
                                               ArrayList<String> arrayForWorkWithDB) throws SQLException {

        //Делаем 2 Аррая, - для имен колонок и их значений
        List<String> columnNames = new ArrayList<String>();
        List<String> columnValues = new ArrayList<String>();

        PreparedStatement statement = connectionToDb.prepareStatement(sqlColumnsForTable);
        for(int index = 1;index<=arrayColumnsForTable.size();index++){
            int indexForArray = index-1;
            statement.setString(index,arrayColumnsForTable.get(indexForArray));
        }
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int index = 1; index<= columnCount; index++){
                columnNames.add(rsmd.getColumnName(index));
            }
        }
        resultSet.close();
        statement.close();
//конец этапа
        /*
        PreparedStatement statement1 = connectionToDb.prepareStatement(sqlValuesForTable);
        for(int index = 1;index<=arrayValuesForTable.size();index++){
            int indexForArray = index-1;
            statement1.setString(index,arrayValuesForTable.get(indexForArray));
        }
        ResultSet rs1 = statement1.getResultSet();
        while (rs.next()){
            columnValues.add(rs1.getString(1));
        }
        rs1.close();
        statement1.close();

        PreparedStatement statement3 = connectionToDb.prepareStatement(sqlForWorkWithDb);
        for(int index = 1;index<=arrayForWorkWithDB.size();index++){
            int indexForArray = index-1;
            statement1.setString(index,arrayForWorkWithDB.get(indexForArray));
        }
        statement3.executeQuery().close();

        return null;
        //return createTable(columnNames,columnValues);
    }*/
}
