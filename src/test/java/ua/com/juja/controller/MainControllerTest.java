package ua.com.juja.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ua.com.juja.controller.command.Command;
import ua.com.juja.integrationTest.ConfigurableInputStream;
import ua.com.juja.model.Model;
import ua.com.juja.view.ConsoleView;
import ua.com.juja.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MainControllerTest {
    private ConfigurableInputStream in;
    private Model model;
    private View view;
    private MainController mainController;

    @Before
    public void setUp() throws Exception {
        in = new ConfigurableInputStream();
        model = mock(Model.class);
        view = new ConsoleView();
        mainController = new MainController(model, view);
        System.setIn(in);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void beginWork() {
        in.setLine("Stringline|forTest|howDoWork|splitCommandOnArray()");
        String[] expected = new String[]{"Stringline", "forTest", "howDoWork", "splitCommandOnArray()"};
        mainController.beginWork();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    }
}