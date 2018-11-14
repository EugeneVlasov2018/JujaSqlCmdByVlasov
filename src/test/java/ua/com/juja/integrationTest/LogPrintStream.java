package ua.com.juja.integrationTest;

import java.io.IOException;
import java.io.OutputStream;


public class LogPrintStream extends OutputStream {

    private StringBuilder log;

    @Override
    public void write(int b) throws IOException {
        log.append((char)b);

    }
}
