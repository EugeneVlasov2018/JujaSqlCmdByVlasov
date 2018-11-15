package ua.com.juja.integrationTest;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.controller.Main;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;


public class integrationTest {
    private static ConfigurableInputStream in;
    private static LogOutputStream out;

    @BeforeClass
    public static void setup() {
        in = new ConfigurableInputStream();
        out = new LogOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }
    @Test
    //тестим не каждый метод по отдельности, а сценарии
    public void testExit(){
        //given
        in.setLine("exit");
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
