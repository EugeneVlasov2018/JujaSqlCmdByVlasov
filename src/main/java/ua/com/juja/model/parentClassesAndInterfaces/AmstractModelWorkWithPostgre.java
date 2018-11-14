package ua.com.juja.model.parentClassesAndInterfaces;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
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
            Statement statement = connectionToDb.createStatement();
            statement.execute(sqlRequest);
            statement.close();
        }
    }

    protected String requestWithAnswer(Connection connectionToDatabase, String sqlRequestForWork,String requestForTable)
            throws SQLException, NullableAnswerException, NullPointerException {

        List<String> arrayForTableNames = new ArrayList<>();
        List<String> arrayForTableValues = new ArrayList<>();

        Statement statement = connectionToDatabase.createStatement();
        ResultSet resultSet = statement.executeQuery(requestForTable);
        if(resultSet.isBeforeFirst()){
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i = 1; i<=columnCount;i++)
            arrayForTableNames.add(rsmd.getColumnName(i));

        while(resultSet.next()) {
            for (int i = 0; i < arrayForTableNames.size(); i++)
                arrayForTableValues.add(resultSet.getString(arrayForTableNames.get(i)));
        }
        statement.close();
        resultSet.close();
        return createTable(arrayForTableNames,arrayForTableValues);
        } else {
            throw new NullableAnswerException();
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

    /*protected String createTable(List<String> columnNames, List<String> columnValues) {
        int columnCount = columnNames.size();
        int columnWight = whatIsColumnWight(columnNames, columnValues);
        String table = buildTableHead(columnNames, columnWight, columnCount);
        table += buildTableBody(columnValues, columnCount, columnWight);
        return table;
    }*/

    protected String buildTableBody(List<String> columnValues, int columnCount, int columnWight) {
        String tableBody = "+";
        for (int i = 0; i <columnValues.size() ; i=i+columnCount) {
            for (int j = i; j <i+columnCount; j++) {
                tableBody = tableBody.format("%-"+columnWight+"s+", columnValues.get(j));
            }
            tableBody = tableBody+"\n"+buildBorder(columnCount,columnWight);
        }
        return tableBody;
    }

    protected String buildTableHead(List<String> columnNames, int columnWight,int columnCount) {
        String tableHeader = buildBorder(columnCount,columnWight)+"+";
        for (int i = 0; i <columnNames.size() ; i++) {
            tableHeader = tableHeader+tableHeader.format("%-"+columnWight+"s+", columnNames.get(i));
        }
        tableHeader+="\n"+buildBorder(columnCount,columnWight);
        return tableHeader;
    }


    protected String buildBorder(int columnCount, int columnWight) {
        String border = "+";
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnWight; j++) {
                border+="-";
            }
            border+="+";
        }
        border+="\n";
        return border;
    }

    protected int whatIsColumnWight(List<String> columnNames, List<String> columnValues) {
        int result = 0;
        for(String tmp:columnNames){
            if(result<tmp.length())
                result = tmp.length();
        }
        for(String tmp:columnValues){
            if(result<tmp.length())
                result = tmp.length();
        }
        result = result+2;
        return result;
    }
}
