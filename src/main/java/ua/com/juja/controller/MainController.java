package ua.com.juja.controller;

import ua.com.juja.controller.command.*;
import ua.com.juja.controller.command.workWithModel.Connect;
import ua.com.juja.controller.command.workWithModel.Exit;
import ua.com.juja.controller.command.workInController.Help;
import ua.com.juja.controller.command.workInController.WrongCommand;
import ua.com.juja.controller.command.workWithModel.*;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

public class MainController {

    private Command[] command;
    private String[] commandForWork;
    private View view;


    public MainController(Model model, View view) {
        this.view = view;

        this.command = new Command[]{new Connect(view, model), new Clear(model, view),
                new Delete(model, view), new Drop(model, view), new Exit(view, model),
                new Find(model, view), new Help(view), new Insert(model, view),
                new Tables(model, view), new Update(model, view), new Create(model, view),
                new WrongCommand(view)};
    }

    public void beginWork() {
        view.write("Дорогой юзер, приветствую тебя в нашей МЕГАБАЗЕ)))\n" +
                "Для подключения к базе данных введи команду в формате:\n" +
                "connect|database|username|password");
        boolean flag = true;
        while (flag) {
            commandForWork = splitCommandOnArray();
            workWithCommand();
            flag = whatCommandIsWork(commandForWork);
        }
    }

    private String[] splitCommandOnArray() {
        String inputedInfo = view.read();
        String[] preResult = inputedInfo.split("\\|");
        String[] result = new String[preResult.length];
        for (int index = 0; index < preResult.length; index++) {
            result[index] = preResult[index].trim();
        }
        return result;
    }

    private void workWithCommand() {
        for (Command currentCommand : command) {
            if (currentCommand.canProcess(commandForWork)) {
                currentCommand.doWork(commandForWork);
                break;
            }
        }
    }

    private boolean whatCommandIsWork(String[] commandForWork) {
        if (commandForWork[0].equalsIgnoreCase("exit")) {
            return false;
        }
        return true;
    }
}
