package ua.com.juja.controller.command.exceptions;

import org.apache.log4j.Logger;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class CommandIsWrongException extends Exception {
    private String message;
    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    public CommandIsWrongException(String message) {
        this.message = message;
        logger.warn(String.format("В процессе работы на уровне контроллера произошла ошибка. Причина возникновения: %s"
                , this.message));
    }

    public String getMessage() {
        return message;
    }
}
