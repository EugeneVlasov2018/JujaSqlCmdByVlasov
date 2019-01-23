package ua.com.juja.controller.command.workWithModel;

import org.apache.log4j.Logger;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import ua.com.juja.controller.command.exceptions.CommandIsWrongException;

import java.util.List;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class CommandChekkerAndFormatter {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    protected boolean commandIsRight(int realSize, int expectedSize) throws CommandIsWrongException {
        if (realSize < expectedSize) {
            logger.info("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
            throw new CommandIsWrongException(String.format("Введенная команда неполная.\n" +
                    "Команда должна состоять из %s элементов, а у вас их %s\n" +
                    "Попробуйте еще раз", expectedSize, realSize));
        }
        return true;
    }

    protected boolean commandIsRight(int realSize, int expectedSize, int multiplier) throws CommandIsWrongException {
        if (realSize < expectedSize) {
            logger.info("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
            throw new CommandIsWrongException(String.format("Введенная команда неполная.\n" +
                    "Команда должна состоять из %s элементов, а у вас их %s\n" +
                    "Попробуйте еще раз", expectedSize, realSize));
        }
        if (!(realSize % multiplier == 0)) {
            logger.info("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
            throw new CommandIsWrongException("Введенная команда неполная.\n" +
                    "Проверьте, всем ли именам введенных вами колонок соответствуют значения.");
        }
        return true;
    }

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
