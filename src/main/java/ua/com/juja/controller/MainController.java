package ua.com.juja.controller;

import ua.com.juja.model.ModelImplWithPostgre;
import ua.com.juja.model.parentClassesAndInterfaces.ModelInterface;

import java.util.Scanner;

public class MainController {

    //Переменная модели
    private ModelInterface model = new ModelImplWithPostgre();
    //коннекшн введен переменной контроллера, т.к. он будет постоянно передаваться в команды
    private String[] commandForWork;
    //флаг, обеспечивающий выход из бесконечного цикла ввода информации
    static boolean flag = true;


    public static void main(String[] args) {
        MainController controller = new MainController();

        System.out.println("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password");

        while (flag) {
            //Запускаем метод, получающий строку (команду к выполнению)
            controller.commandForWork = controller.splitCommandOnArray();
            //определяем, какая именно команда была введена.
            //если вошла команда 'exit', флаг поменяется на фолс и цикл будет прерван, -
            //корректное завершение программы
            controller.decouplingCommand();
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
        if (commandForWork[0].toLowerCase().equals("connect")) {
            model.сonnect(commandForWork);
        } else if (commandForWork[0].equalsIgnoreCase("clear")) {
            model.clear(commandForWork);
        } else if (commandForWork[0].equalsIgnoreCase("create")) {
            model.create(commandForWork);
        } else if (commandForWork[0].trim().equalsIgnoreCase("delete")) {
            model.delete(commandForWork);
        } else if (commandForWork[0].trim().equalsIgnoreCase("drop")) {
            model.drop(commandForWork);
        } else if (commandForWork[0].trim().equalsIgnoreCase("exit")) {
            model.exit();
            flag = false;
        } else if (commandForWork[0].trim().equalsIgnoreCase("find")) {
            model.find(commandForWork);
        } else if (commandForWork[0].equalsIgnoreCase("help")) {
            model.help();
        } else if (commandForWork[0].equalsIgnoreCase("tables")) {
            model.tables();
        } else if (commandForWork[0].trim().equalsIgnoreCase("update")) {
            model.update(commandForWork);
        } else if (commandForWork[0].trim().equalsIgnoreCase("insert")) {
            model.insert(commandForWork);
        } else {
            model.wrongCommand();
        }
    }
}
