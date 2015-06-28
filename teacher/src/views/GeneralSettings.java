package views;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import config.*;
import others.Computer;

import javax.swing.DefaultListModel;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;

/**
 * TODO: Currently disabling "next" button when enable "same_password_checkbox". Not listening for username_field and password_field changes.
 * @author paladini
 */
public class GeneralSettings extends javax.swing.JFrame {

    private DefaultListModel list_model = new DefaultListModel();
    private Config config;

    /**
     * Creates new form ComputerManager
     */
    public GeneralSettings(DefaultListModel model, Config config) {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.config = config;
        
        computerList_list.setFixedCellHeight(30);
        computerList_list.setFont(computerList_list.getFont().deriveFont(14.0f));
        computerList_list.setModel(model);
        
        username_field.setToolTipText("Your SSH root user.");
        password_field.setToolTipText("The root password");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        computerList_list = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        next = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        same_password_checkbox = new javax.swing.JCheckBox();
        username_field = new javax.swing.JTextField();
        username_label = new javax.swing.JLabel();
        password_label = new javax.swing.JLabel();
        password_field = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LabSpy - Setup Process (1/2)");
        setResizable(false);

        computerList_list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        computerList_list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                computerList_listValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(computerList_list);

        jPanel1.setBackground(new java.awt.Color(102, 204, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel1.setText("Remote Install - Settings");

        next.setText("Install");
        next.setEnabled(false);
        next.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nextMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("<html>Please, select below the computers you want to install LabSpy. Note that if you select a computer that already has LabSpy installed, the old installation will be overwritten.");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        back.setText("Back");
        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backMouseClicked(evt);
            }
        });

        same_password_checkbox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        same_password_checkbox.setText("Set same username / password for all computers (SSH root user)");
        same_password_checkbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                same_password_checkboxItemStateChanged(evt);
            }
        });

        username_field.setText("root");
        username_field.setEnabled(false);

        username_label.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        username_label.setText("Username:");
        username_label.setEnabled(false);

        password_label.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        password_label.setText("Password:");
        password_label.setEnabled(false);

        password_field.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(205, 205, 205)
                        .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(same_password_checkbox)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(username_label)
                                .addComponent(username_field, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(57, 57, 57)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(password_label)
                                .addComponent(password_field, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(same_password_checkbox)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username_label)
                    .addComponent(password_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(password_field)
                    .addComponent(username_field, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void computerList_listValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_computerList_listValueChanged
        if (computerList_list.getSelectedValuesList().isEmpty() || 
                (same_password_checkbox.isSelected() && 
                (username_field.getText().isEmpty() || password_field.getPassword().length == 0) )) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
        }
    }//GEN-LAST:event_computerList_listValueChanged

    private void backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backMouseClicked
        ComputerManager cm = new ComputerManager(config);
        cm.setVisible(true);
        dispose();
    }//GEN-LAST:event_backMouseClicked

    private void same_password_checkboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_same_password_checkboxItemStateChanged
        if(same_password_checkbox.isSelected()) {
            username_label.setEnabled(true);
            password_label.setEnabled(true);
            username_field.setEnabled(true);
            password_field.setEnabled(true);
             if (computerList_list.getSelectedValuesList().isEmpty() || username_field.getText().isEmpty() || password_field.getPassword().length == 0) {
                next.setEnabled(false);
            } else {
                 next.setEnabled(true);
             }
        } else {
            username_label.setEnabled(false);
            password_label.setEnabled(false);
            username_field.setEnabled(false);
            password_field.setEnabled(false);
            if (!computerList_list.getSelectedValuesList().isEmpty()) {
                next.setEnabled(true);
            } else {
                next.setEnabled(false);
            }
        }
    }//GEN-LAST:event_same_password_checkboxItemStateChanged

    private void nextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextMouseClicked
        List<Computer> list = computerList_list.getSelectedValuesList();
        InstallViewer iv = null;
        if (same_password_checkbox.isSelected()) {
            iv = new InstallViewer(list, username_field.getText(), Arrays.toString(password_field.getPassword()));
        } else {
            iv = new InstallViewer(list);
        }
        iv.start();
        dispose();
    }//GEN-LAST:event_nextMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JList computerList_list;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton next;
    private javax.swing.JPasswordField password_field;
    private javax.swing.JLabel password_label;
    private javax.swing.JCheckBox same_password_checkbox;
    private javax.swing.JTextField username_field;
    private javax.swing.JLabel username_label;
    // End of variables declaration//GEN-END:variables
}
