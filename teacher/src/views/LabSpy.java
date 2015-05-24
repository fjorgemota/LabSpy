/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import chat.Messenger;
import communication.ConnectorThread;
import config.Computer;
import config.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author paladini
 */
public class LabSpy extends javax.swing.JFrame implements ActionListener {
    static final String CONFIGURATION = "CONFIGURATION";
    static final String SCREEN = "SCREEN";
    static final String CHAT = "CHAT";
    static final String POWERDOWN = "POWERDOWN";
    static final String POWERUP = "POWERUP";
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

        JMenu overview = new JMenu("Overview");
        JMenu students = new JMenu("Messages");
        JMenu config = new JMenu("Functions");
        config.setMnemonic(KeyEvent.VK_A);
        config.getAccessibleContext().setAccessibleDescription("The functions of" +
                "the sistem!");
        //add functions for menu;
        bar.add(config);
        bar.add(overview);
        bar.add(students);
        JMenuItem itemConfig = new JMenuItem("Configuration");
        itemConfig.setActionCommand(CONFIGURATION);
        itemConfig.addActionListener(this);
        config.add(itemConfig);

        JMenuItem itemScreen = new JMenuItem("Screenshot");
        itemScreen.setActionCommand(SCREEN);
        itemScreen.addActionListener(this);
        overview.add(itemScreen);

        JMenuItem itemChat = new JMenuItem("Messenger");
        itemChat.setActionCommand(CHAT);
        itemChat.addActionListener(this);
        students.add(itemChat);

        JMenuItem itemPowerDown = new JMenuItem("Power off");
        itemPowerDown.setActionCommand(POWERDOWN);
        itemPowerDown.addActionListener(this);
        students.add(itemPowerDown);

        JMenuItem itemPowerUp = new JMenuItem("Power on");
        itemPowerUp.setActionCommand(POWERUP);
        itemPowerUp.addActionListener(this);
        students.add(itemPowerUp);


        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }

        try {

            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(classLoader.getResourceAsStream("imagens/SpyLabLogoLg.jpg")))));
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
        connectorThread.stop();
        g.stop();
        String command = actionEvent.getActionCommand();
        if (command.equals(CONFIGURATION)) {
            ComputerManager manager = new ComputerManager(configuration);
            manager.setVisible(true);
        } else if (command.equals(SCREEN)) { //This may change with the next modifications for client-server
            Thread connector = new Thread(connectorThread);
            connector.start();
            g.setVisible(true);

            Thread gridManagerView = new Thread(g);
            gridManagerView.start();

        } else if (command.equals(CHAT)) {
            Messenger c = new Messenger();
            c.setVisible(true);
            dispose();
        } else if (command.equals(POWERDOWN)) {
            JOptionPane.showMessageDialog(null, "Sending message for power off!");
        } else if (command.equals(POWERUP)) {
            JOptionPane.showMessageDialog(null, "Sending message for power on!");
        }
    }

}
