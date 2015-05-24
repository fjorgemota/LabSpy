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
    private Config config;

    /**
     * Creates new form LabSpy
     */
    public LabSpy() {
        super("LabSpy");
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

        JMenuItem itemScreen = new JMenuItem("Schreenshot");
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


        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("/home/podesta/Desktop/SpyLabLogoLg.jpg")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.pack();


    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        if (command.equals(CONFIGURATION)) {
            config = Config.getInstance();
            checkFirstTime();
        } else if (command.equals(SCREEN)) { //This may change with the next modifications for client-server
            Config configure = Config.getInstance();
            if (configure.getComputers().size() == 0) {
                configure.addComputer(new Computer("127.0.0.1"));
                //JOptionPane.showMessageDialog(null, "Configure your PC!");
            }
           // } else {

                ConnectorThread connectorThread = new ConnectorThread(configure);
                Thread connector = new Thread(connectorThread);
                connector.start();
                GridManager g = new GridManager(connectorThread);
                g.setVisible(true);
                while (true) {
                    g.update();
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
           // }
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

    private void checkFirstTime() {
        if (config.firstTime()) {
            FirstRun fr = new FirstRun();
            fr.setVisible(true);
            dispose();
        } else {
            this.setVisible(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LabSpy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LabSpy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LabSpy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LabSpy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LabSpy lab = new LabSpy();
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
