package ua.com.juja.controller.command;

import ua.com.juja.controller.command.exceptions.CommandIsWrongException;

public class CommandChekker {
    protected boolean commandIsRight(int realSize, int expectedSize) throws CommandIsWrongException {
        if (realSize < expectedSize) {
            throw new CommandIsWrongException(String.format("Введенная команда неполная.\n" +
                    "Команда должна состоять из %s элементов, а у вас их %s\n" +
                    "Попробуйте еще раз", expectedSize, realSize));
        }
        return true;
    }
}
