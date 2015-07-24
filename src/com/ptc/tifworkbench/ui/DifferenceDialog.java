/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui;

import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.model.SolutionDifferencer;
import com.ptc.tifworkbench.worker.DifferenceWorker;
import com.ptc.tifworkbench.worker.Status;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author pbowden
 */
public class DifferenceDialog extends javax.swing.JDialog implements ActionListener{

    private TifViewFrame view;
    private PrintWriter reportWriter;
    /**
     * Creates new form DifferenceDialog
     */
    public DifferenceDialog(java.awt.Frame parent, boolean modal, TifViewFrame view) {
        super(parent, modal);
        initComponents();
        this.view = view;
        
    }

    public String getDescription()
    {
        return "Difference";
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        diffProgress = new javax.swing.JProgressBar();
        cancelBtn = new javax.swing.JButton();
        diffBtn = new javax.swing.JButton();
        combo1 = new javax.swing.JComboBox();
        complbl = new javax.swing.JLabel();
        withlbl = new javax.swing.JLabel();
        combo2 = new javax.swing.JComboBox();
        checkReport = new javax.swing.JCheckBox();
        txtReport = new javax.swing.JTextField();
        btnReportBrowse = new javax.swing.JButton();
        summaryTextScroll = new javax.swing.JScrollPane();
        summaryText = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select two definitions to difference");

        diffProgress.setStringPainted(true);

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        diffBtn.setText("Difference");
        diffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diffBtnActionPerformed(evt);
            }
        });

        combo1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo1ActionPerformed(evt);
            }
        });

        complbl.setText("Compare template");

        withlbl.setText("with");

        combo2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo2ActionPerformed(evt);
            }
        });

        checkReport.setText("Generate detail report");
        checkReport.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                stateReportChanged(evt);
            }
        });

        txtReport.setText("report.diff");
        txtReport.setEnabled(false);

        btnReportBrowse.setText("...");
        btnReportBrowse.setEnabled(false);
        btnReportBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportBrowseActionPerformed(evt);
            }
        });

        summaryText.setEditable(false);
        summaryText.setColumns(20);
        summaryText.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        summaryText.setLineWrap(true);
        summaryText.setRows(3);
        summaryText.setText("Generate difference to make Template 1 equivalent to Template 2");
        summaryText.setWrapStyleWord(true);
        summaryText.setAutoscrolls(false);
        summaryTextScroll.setViewportView(summaryText);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(combo1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(combo2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(diffProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(summaryTextScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(complbl)
                            .addComponent(withlbl)
                            .addComponent(checkReport))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(diffBtn)
                        .addGap(18, 18, 18)
                        .addComponent(cancelBtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtReport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReportBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(complbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(withlbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summaryTextScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkReport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtReport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnReportBrowse))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(diffProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelBtn)
                            .addComponent(diffBtn))
                        .addContainerGap())))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-445)/2, (screenSize.height-326)/2, 445, 326);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(true);
        this.dispose();
        
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void diffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diffBtnActionPerformed
        
        if(!checkReportFile()) return;
        
        ImSolution sol1 = ((TifViewFrame)combo1.getSelectedItem()).getSolution();
        ImSolution sol2 = ((TifViewFrame)combo2.getSelectedItem()).getSolution();
        SolutionDifferencer diff = new SolutionDifferencer(sol1, sol2);
        DifferenceWorker diffWorker = new DifferenceWorker(diff, view);
        diff.setReporter(diffWorker);

        // The worker will fire Status property events when the differencer sets 
        // any new status.
        diffWorker.addPropertyChangeListener(new PropertyChangeListener() {
           public  void propertyChange(PropertyChangeEvent evt) {
             if (StatusReporter.STATUS_PROP.equals(evt.getPropertyName())) 
             {
                 Status stat = (Status)evt.getNewValue();
                 diffProgress.setValue(stat.getProgress());
                 diffProgress.setString(stat.getMessage());
             }
             if (StatusReporter.DETAIL_PROP.equals(evt.getPropertyName())) 
             {
                 String message = (String)evt.getNewValue();
                 if(reportWriter != null)
                     reportWriter.println(message);
             }
             if (StatusReporter.FINISHED_PROP.equals(evt.getPropertyName())) 
             {
                 if(reportWriter != null)
                     reportWriter.close();
                 cancelBtn.setText("Close");
                 diffBtn.setEnabled(false);
             }
           }
        });

        diffWorker.execute();
    }//GEN-LAST:event_diffBtnActionPerformed

    private void stateReportChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_stateReportChanged
        this.txtReport.setEnabled(this.checkReport.isSelected());
        this.btnReportBrowse.setEnabled(this.checkReport.isSelected());
    }//GEN-LAST:event_stateReportChanged

    private void btnReportBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportBrowseActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            String fname = chooser.getSelectedFile().getName();
            this.txtReport.setText(fname);
        }
    }//GEN-LAST:event_btnReportBrowseActionPerformed

    private void combo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo1ActionPerformed
        setSummaryText();
    }//GEN-LAST:event_combo1ActionPerformed

    private void combo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo2ActionPerformed
        setSummaryText();
    }//GEN-LAST:event_combo2ActionPerformed

    private void setSummaryText()
    {
        summaryText.setText("Generate difference to make " + combo1.getSelectedItem().toString() +
                " equivalent to " + combo2.getSelectedItem().toString());
    }
    /**
     * Check that the report file exists and that we can open it.
     */
    private boolean checkReportFile()
    {
        if(this.checkReport.isSelected())
        {
            String fpath = this.txtReport.getText();
            File fout = new File(fpath);
            try
            {
                reportWriter = new PrintWriter(new FileWriter(fout));
            }catch(Exception ex)
            {
               JOptionPane.showMessageDialog(null, "Could not open the report file " +
                       fout.getAbsolutePath(), "Error - cannot open file.", JOptionPane.ERROR_MESSAGE);
               return false;
            }
        }
        return true;
    }
    public String getReportPath()
    {
        return this.txtReport.getText();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReportBrowse;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JCheckBox checkReport;
    private javax.swing.JComboBox combo1;
    private javax.swing.JComboBox combo2;
    private javax.swing.JLabel complbl;
    private javax.swing.JButton diffBtn;
    private javax.swing.JProgressBar diffProgress;
    private javax.swing.JTextArea summaryText;
    private javax.swing.JScrollPane summaryTextScroll;
    private javax.swing.JTextField txtReport;
    private javax.swing.JLabel withlbl;
    // End of variables declaration//GEN-END:variables

    public void setChoices(List<TifViewFrame>choices) {
        DefaultComboBoxModel model1 = new DefaultComboBoxModel();
        DefaultComboBoxModel model2 = new DefaultComboBoxModel();
        for(TifViewFrame view : choices) 
        {
            
            model1.addElement(view);
            model2.addElement(view);
        }
        this.combo1.setModel(model1);
        this.combo2.setModel(model2);
        if(model2.getSize() > 1)
            combo2.setSelectedIndex(1);
        combo1.addActionListener(this);
        combo2.addActionListener(this);
        setSummaryText();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

}