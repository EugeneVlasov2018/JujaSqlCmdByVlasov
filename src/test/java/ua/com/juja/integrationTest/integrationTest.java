package ua.com.juja.integrationTest;

import org.junit.BeforeClass;

import java.io.InputStream;
import java.io.PrintStream;


public class integrationTest {
    private static ConfigurableInputStream in;
    private static LogPrintStream out;

    @BeforeClass
    public static void setup() {

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }
}
