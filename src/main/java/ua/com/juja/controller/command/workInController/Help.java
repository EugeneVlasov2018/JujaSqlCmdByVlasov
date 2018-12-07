package ua.com.juja.controller.command.workInController;

import ua.com.juja.controller.command.Command;
import ua.com.juja.view.View;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

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
    public void doWork(String[] command, Connection connection) {
        try(BufferedReader buffReader = new BufferedReader(new FileReader("resourses\\Help.txt"))){
            String message;
            while ((message = buffReader.readLine())!=null) {
                view.write(message);
            }

        } catch (IOException x){
            view.write(String.format("Ошибка в процессе выполнения 'help'. Причина: %s", x.getMessage()));
        }

    }

    @Override
    public Connection getConnection() {
        return null;
    }
}

