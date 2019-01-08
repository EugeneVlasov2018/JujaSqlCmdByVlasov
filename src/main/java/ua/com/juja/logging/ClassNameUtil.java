package ua.com.juja.logging;

import org.apache.log4j.Logger;

public class ClassNameUtil {
    /*метод-костыль для получения имени класса для логгера. Подсмотрел у Головача
      суть, - бросаем экзепшен и в обработке получаем имя класса, в котором был вызван этот метод
      профит, - можем копипастить получение логгера только одной строкой
      private static final Logger logger = Logger.getLogger(getCurrentClassName());
    */
    public static String getCurrentClassName() {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            return e.getStackTrace()[1].getClassName();
        }
    }
}
