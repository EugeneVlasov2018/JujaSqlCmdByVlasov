package ua.com.juja.controller.command.workInController;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class Help implements Command {
    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private View view;

    public Help(View view) {
        this.view = view;

    }

    @Override
    public boolean canProcess(String[] command) {
        return command[0].equalsIgnoreCase("help");
    }

    @Override
    public void doWork(String[] command) {
        try (Scanner scanner = new Scanner(new FileReader("src\\main\\resourses\\Help.txt"))) {
            while (scanner.hasNext()) {
                view.write(scanner.nextLine());
            }
            logger.info("отработал корректно, пользовотелю выведена информация о командах");
        } catch (IOException x){
            logger.warn(String.format("Поймана ошибка в doWork(). стек-трейс ошибки:\n%s", x.getStackTrace()));
            view.write(String.format("Ошибка в процессе выполнения 'help'. Причина:\n%s", x.getMessage()));
        }
    }
}

