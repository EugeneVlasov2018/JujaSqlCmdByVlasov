package ua.com.juja.logging;

public class ClassNameUtil {
    /*метод-костыль для получения имени класса для логгера.
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
