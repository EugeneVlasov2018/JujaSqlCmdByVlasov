package ua.com.juja.model.exceptions;

import org.apache.log4j.Logger;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class CreatedInModelException extends Exception {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());

    private String message;

    public CreatedInModelException(String message) {
        this.message = message;
        logger.warn(String.format("В процессе работы на уровне модели произошла ошибка. Причина возникновения: %s", message));
    }

    public String getMessage(){
        return message;
    }
}
