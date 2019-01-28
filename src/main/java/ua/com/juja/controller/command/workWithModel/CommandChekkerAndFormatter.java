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

    protected boolean commandIsRight(int realSize, int minimalExpectedSize) throws CommandIsWrongException {
        if (realSize < minimalExpectedSize) {
            logger.info("Комманда к выполнению корректно не прошла проверку, " +
                    "метод doWork() отработал, процесс вернулся в MainController");
            throw new CommandIsWrongException(String.format("Введенная команда неполная.\n" +
                    "Команда должна состоять из %s элементов, а у вас их %s\n" +
                    "Попробуйте еще раз", minimalExpectedSize, realSize));
        }
        return true;
    }

    protected boolean commandIsRight(int realSize, int minimalExpectedSize, int multiplier) throws CommandIsWrongException {
        if (realSize < minimalExpectedSize) {
            logger.info("Комманда к выполнению корректно не прошла проверку");
            throw new CommandIsWrongException(String.format("Введенная команда неполная.\n" +
                    "Команда должна состоять из %s элементов, а у вас их %s\n" +
                    "Попробуйте еще раз", minimalExpectedSize, realSize));
        }
        if (!(realSize % multiplier == 0)) {
            logger.info("Комманда к выполнению корректно не прошла проверку");
            throw new CommandIsWrongException("Введенная команда неполная.\n" +
                    "Проверьте, всем ли именам введенных вами колонок соответствуют значения.");
        }
        return true;
    }

    protected String createTable(List<String> columnNames, List<String> columnValues) {
        logger.info("Таблица для ответа приводится к шаблону");
        Table tableForOutput = new Table(columnNames.size(),
                BorderStyle.CLASSIC, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        logger.info("заполняется заголовок таблицы (имена колонок)");
        for (String header : columnNames) {
            tableForOutput.addCell(header);
        }
        logger.info("заполняется заголовок таблицы (имена колонок)");
        for (String row : columnValues) {
            tableForOutput.addCell(row);
        }
        logger.info(String.format("возвращается такая таблица:\n%s", tableForOutput.render()));
        return tableForOutput.render();
    }
}
