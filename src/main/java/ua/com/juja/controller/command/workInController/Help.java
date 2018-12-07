package ua.com.juja.controller.command.workInController;

import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Scanner;

public class Help implements Command {
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
            String message;
            while (scanner.hasNext()) {
                view.write(scanner.nextLine());
            }
        } catch (IOException x){
            view.write(String.format("Ошибка в процессе выполнения 'help'. Причина: %s", x.getMessage()));
        }
    }
}

