package ua.com.juja.controller.command.workWithModel;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandWithTableInResponce {
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
