package ua.com.juja.integrationTest;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.controller.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;


public class integrationTest {
    private static ConfigurableInputStream in;
    private static LogOutputStream out;
    private static InputStream inStrean;

    @BeforeClass
    public static void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();
    }
    @Test
    public void testExit(){
        //given
        inStrean = new ByteArrayInputStream("exit".getBytes());
        System.setIn(inStrean);

        //when
        Main.main(new String[0]);

        //then
        assertEquals("", out.getLog());

    }

    @Test
    public void testHelp(){
        in.setLine("help");
        Main.main(new String[0]);
        assertEquals("",out.getLog());
    }
}
