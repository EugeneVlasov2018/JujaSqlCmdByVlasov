package ua.com.juja.controller;

import ua.com.juja.controller.command.*;
import ua.com.juja.controller.command.workWithController.Connect;
import ua.com.juja.controller.command.workWithController.Exit;
import ua.com.juja.controller.command.workWithController.Help;
import ua.com.juja.controller.command.workWithController.WrongCommand;
import ua.com.juja.controller.command.workWithModel.*;
import ua.com.juja.model.newExceptions.SystemExitException;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;
import ua.com.juja.view.ViewInterface;

import java.sql.Connection;
import java.util.Scanner;

public class MainController {

    private Command[] command;
    //Переменная модели
    private ModelInterface model;
    private String[] commandForWork;
    private Connection connection;
    private ViewInterface view;


    public MainController(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
        this.command = new Command[] {new Connect(this.view),new Clear(this.model),new Delete(this.model),
                new Drop(this.model), new Exit(this.view), new Find(this.model),new Help(this.view),
                new Insert(this.model),new Tables(this.model),new Update(this.model), new Create(this.model),
                new WrongCommand(this.view)};
    }

    public void beginWork() {
        System.out.println("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password");
        while (true) {
            //Запускаем метод, получающий строку (команду к выполнению)
            commandForWork = splitCommandOnArray();
            //определяем, какая именно команда была введена.
            //если вошла команда 'exit', флаг поменяется на фолс и цикл будет прерван, -
            //корректное завершение программы
            decouplingCommand();
        }
    }

    //Служебный метод приема строки и разбивки на массив строк
    private String[] splitCommandOnArray() {
        Scanner scan = new Scanner(System.in);
        String inputedInfo = scan.nextLine();
        //кусок метода, обрезающий пробелы в блоке команд
        String[] preResult = inputedInfo.split("\\|");
        String[] result = new String[preResult.length];
        for (int index = 0;index<preResult.length;index++) {
            result[index] = preResult[index].trim();
        }
        return result;
    }

    //Служебный метод для определения, какую соманду выполнять (в MVC - какой метод модели запускаем)
    private void decouplingCommand() {
        try {
            workWithCommand();
        } catch (SystemExitException e){
            //do nothing;
        }
    }

    private void workWithCommand() {
        if (command[0].canProcess(commandForWork)) {
            command[0].doWork(commandForWork, null);
            connection = command[0].getConnection();
        } else if (command[1].canProcess(commandForWork)) {
            command[1].doWork(commandForWork, connection);
        } else if (command[2].canProcess(commandForWork)) {
            command[2].doWork(commandForWork, connection);
        } else if (command[3].canProcess(commandForWork)) {
            command[3].doWork(commandForWork, connection);
        } else if (command[4].canProcess(commandForWork)) {
            command[4].doWork(null, null);
        } else if (command[5].canProcess(commandForWork)) {
            command[5].doWork(commandForWork, connection);
        } else if (command[6].canProcess(commandForWork)) {
            command[6].doWork(null, null);
        } else if (command[7].canProcess(commandForWork)) {
            command[7].doWork(commandForWork, connection);
        } else if (command[8].canProcess(commandForWork)) {
            command[8].doWork(null, connection);
        } else if (command[9].canProcess(commandForWork)) {
            command[9].doWork(commandForWork, connection);
        } else if (command[10].canProcess(commandForWork)) {
            command[10].doWork(commandForWork, connection);
        } else if (command[11].canProcess(commandForWork)) {
            command[11].doWork(commandForWork, connection);
        } else {
            command[12].doWork(null, null);
        }
    }
}
