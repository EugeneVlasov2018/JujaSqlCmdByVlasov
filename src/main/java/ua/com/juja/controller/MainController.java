package ua.com.juja.controller;

import org.apache.log4j.Logger;
import ua.com.juja.controller.command.*;
import ua.com.juja.controller.command.workWithModel.Connect;
import ua.com.juja.controller.command.workWithModel.Exit;
import ua.com.juja.controller.command.workInController.Help;
import ua.com.juja.controller.command.workInController.WrongCommand;
import ua.com.juja.controller.command.workWithModel.*;
import ua.com.juja.model.Model;
import ua.com.juja.view.View;

import java.util.Arrays;

import static ua.com.juja.logging.ClassNameUtil.getCurrentClassName;

public class MainController {

    private static final Logger logger = Logger.getLogger(getCurrentClassName());
    private Command[] command;
    private View view;
    private String[] commandForWork;


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
            logger.info("Отработал метод commandForWork(), началось распознавание конкретной комманды");
            workWithCommand();
            logger.info("Отработал метод workWithCommand()");
            flag = whatCommandIsWork(commandForWork);
            logger.info("Проверили состояние флага методом whatCommandIsWork");
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
                logger.info(String.format("Команда '%s' запускает работу класса %s",
                        Arrays.asList(commandForWork).toString(), currentCommand.getClass().getName()));
                currentCommand.doWork(commandForWork);
                break;
            }
        }
    }

    private boolean whatCommandIsWork(String[] commandForWork) {
        return !commandForWork[0].equalsIgnoreCase("exit");
    }
}
