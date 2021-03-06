/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui;

import java.awt.Frame;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author pbowden
 */
public class FileViewFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form FileViewFrame
     */
    public FileViewFrame(Frame parent) {
        initComponents();
        
    }

    public void openFile(File file)
    {
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            String content = new String(chars);
            setText(content);
            reader.close();
            this.setTitle(file.getAbsolutePath());
        } catch (IOException e) 
        {
            this.text.setText("ERROR: Could not open file\n" + file.getAbsolutePath());
        }    
        
    }
    
    public void setText(String text)
    {
        this.text.setText(text);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollText = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Transformed template");

        text.setColumns(20);
        text.setFont(new java.awt.Font("Lucida Sans", 0, 10)); // NOI18N
        text.setRows(5);
        scrollText.setViewportView(text);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollText, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollText, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollText;
    private javax.swing.JTextArea text;
    // End of variables declaration//GEN-END:variables
}
