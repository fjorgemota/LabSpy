package views;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import config.Computer;
import config.Config;

import javax.swing.DefaultListModel;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author paladini
 */
public class ComputerManager extends javax.swing.JFrame {

    private DefaultListModel list_model = new DefaultListModel();
    private Config config;

    /**
     * Creates new form ComputerManager
     */
    public ComputerManager() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        config = Config.getInstance();
        computerList_list.setFixedCellHeight(30);
        computerList_list.setFont(computerList_list.getFont().deriveFont(14.0f));
        updateList();
        computerList_list.setModel(list_model);

    }

    private void updateList() {
        if (list_model.size() > 0) {
            list_model.removeAllElements();
        }
        
        for (Computer c : config.getComputers()) {
            list_model.addElement(c);
        }
        
        if(list_model.isEmpty()) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
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

        removeComputer_button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        computerList_list = new javax.swing.JList();
        addComputer_button = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        next = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LabSpy - Setup Process (1/3)");
        setResizable(false);

        removeComputer_button.setText("Remove");
        removeComputer_button.setEnabled(false);
        removeComputer_button.setMaximumSize(new java.awt.Dimension(50, 25));
        removeComputer_button.setMinimumSize(new java.awt.Dimension(50, 25));
        removeComputer_button.setPreferredSize(new java.awt.Dimension(50, 25));
        removeComputer_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeComputer_buttonMouseClicked(evt);
            }
        });

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

        addComputer_button.setText("Add Computer");
        addComputer_button.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addComputer_button.setPreferredSize(new java.awt.Dimension(150, 25));
        addComputer_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addComputer_buttonMouseClicked(evt);
            }
        });
        addComputer_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addComputer_buttonActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 204, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel1.setText("Add Computers");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("<html>In this step you should add all computers from the intended laboratory. To add a new computer (and install LabSpy into it) you just need to have the IP address from this computer and also enable SSH access for this machine. <br/><br/>You can add new machines later.");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        next.setText("Next");
        next.setEnabled(false);
        next.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nextMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addComputer_button, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeComputer_button, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                                .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeComputer_button, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addComputer_button, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addComputer_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addComputer_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addComputer_buttonActionPerformed

    private void computerList_listValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_computerList_listValueChanged
        if (!removeComputer_button.isEnabled()) {
            removeComputer_button.setEnabled(true);
        }
        
//        if(computerList_list.getSelectedValuesList().isEmpty()) {
//            next.setEnabled(false);
//        } else {
//            next.setEnabled(true);
//        }
    }//GEN-LAST:event_computerList_listValueChanged

    private void removeComputer_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeComputer_buttonMouseClicked
        List<Computer> selectedValues = computerList_list.getSelectedValuesList();

        for (Computer c : selectedValues) {
            config.removeComputer(c);
        }
        
        updateList();
    }//GEN-LAST:event_removeComputer_buttonMouseClicked

    private void addComputer_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addComputer_buttonMouseClicked
        String s = (String) JOptionPane.showInputDialog(null, "Insert the IP of the new machine.");
        
        Computer c = new Computer(s);
        config.addComputer(c);
        updateList();
    }//GEN-LAST:event_addComputer_buttonMouseClicked

    private void nextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextMouseClicked
        GeneralSettings gs = new GeneralSettings(list_model);
        gs.setVisible(true);
        dispose();
    }//GEN-LAST:event_nextMouseClicked

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
            java.util.logging.Logger.getLogger(ComputerManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ComputerManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ComputerManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ComputerManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ComputerManager cm = new ComputerManager();
                cm.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addComputer_button;
    private javax.swing.JList computerList_list;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton next;
    private javax.swing.JButton removeComputer_button;
    // End of variables declaration//GEN-END:variables
}
