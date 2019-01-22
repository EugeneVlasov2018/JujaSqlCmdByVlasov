package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class CommandWithTableInResponce {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    protected String createTable(List<String> columnNames, List<String> columnValues) {
        logger.debug("Таблица для ответа приводится к шаблону");
        Table tableForOutput = new Table(columnNames.size(),
                BorderStyle.CLASSIC, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        logger.debug("заполняется заголовок таблицы (имена колонок)");
        for (String header : columnNames) {
            tableForOutput.addCell(header);
        }
        logger.debug("заполняется заголовок таблицы (имена колонок)");
        for (String row : columnValues) {
            tableForOutput.addCell(row);
        }
        //возвращаем таблицу в виде строки
        logger.debug(String.format("возвращается такая таблица:\n%s", tableForOutput.render()));
        return tableForOutput.render();
    }
}
