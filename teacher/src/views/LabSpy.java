/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import chat.Messenger;
import communication.ConnectorThread;
import config.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 *
 * @author paladini
 */
public class LabSpy extends javax.swing.JFrame implements ActionListener {
    private static final String CONFIGURATION = "CONFIGURATION";
    private static final String SCREEN = "SCREEN";
    private static final String CHAT = "CHAT";

    private static final String POWERDOWN = "POWERDOWN";
    private static final String POWERUP = "POWERUP";
    private Config configuration;
    private ConnectorThread connectorThread;
    private GridManager g;

    /**
     * Creates new form LabSpy
     */
    public LabSpy() {
        super("LabSpy");
        configuration = Config.getInstance();

        connectorThread = new ConnectorThread(configuration);
        g = new GridManager(connectorThread);
        //initComponents();
        //setLocationRelativeTo(null);

        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(700, 700);

        this.setVisible(true);
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        //this.update();
        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);

        JMenu config = new JMenu("Functions");
        config.setMnemonic(KeyEvent.VK_A);
        config.getAccessibleContext().setAccessibleDescription("The functions of" +
                "the sistem!");
        //add functions for menu;
        bar.add(config);
        JMenuItem itemConfig = new JMenuItem("Configuration");
        itemConfig.setActionCommand(CONFIGURATION);
        itemConfig.addActionListener(this);
        config.add(itemConfig);

        JMenuItem itemOverview = new JMenuItem("Overview");
        itemOverview.setActionCommand(SCREEN);
        itemOverview.addActionListener(this);
        bar.add(itemOverview);




        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }

        try {

            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(classLoader.getResourceAsStream("imagens/labspy400x400.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pack();


        if (configuration.firstTime()) {
            FirstRun firstRun = new FirstRun();
            firstRun.setVisible(true);
        }


    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        g.stop();
        String command = actionEvent.getActionCommand();
        if (command.equals(CONFIGURATION)) {
            ComputerManager manager = new ComputerManager(configuration);
            manager.setVisible(true);
        } else if (command.equals(SCREEN)) { //This may change with the next modifications for client-server
            connectorThread.stop();
            Thread connector = new Thread(connectorThread);
            connector.start();
            g.setVisible(true);

            Thread gridManagerView = new Thread(g);
            gridManagerView.start();

        } else if (command.equals(CHAT)) {
            Messenger c = new Messenger();
            c.setVisible(true);
            dispose();

        }
    }

}
