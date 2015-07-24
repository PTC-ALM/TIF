/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui;

import com.ptc.tifworkbench.integrity.IntegrityApi;
import com.ptc.tifworkbench.integrity.IntegrityConnection;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.model.XmlFormatter;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author pbowden
 */
public class TransformDialog extends javax.swing.JDialog {

    Frame theApp = null;
    ImSolution sol = null;
    FileViewFrame viewFrame=null;
    boolean result = false;
    /**
     * Creates new form TransformDialog
     */
    public TransformDialog(java.awt.Frame parent, boolean modal, ImSolution sol) {
        super(parent, modal);
        theApp = parent;
        this.sol = sol;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        transformPanel = new javax.swing.JPanel();
        templateLabel = new javax.swing.JLabel();
        transformLabel = new javax.swing.JLabel();
        transformText = new javax.swing.JTextField();
        transBrowseButton = new javax.swing.JButton();
        outputLabel = new javax.swing.JLabel();
        outputFile = new javax.swing.JTextField();
        setServerParams = new javax.swing.JCheckBox();
        transformMethodLabel = new javax.swing.JLabel();
        outputMethodCombo = new javax.swing.JComboBox();
        transformButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        transformPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Transform Template"));

        templateLabel.setText("Template:");

        transformLabel.setText("Transform");

        transformText.setText("transforms/script-gen.xsl");
        transformText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transformTextActionPerformed(evt);
            }
        });

        transBrowseButton.setText("...");
        transBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transBrowseButtonActionPerformed(evt);
            }
        });

        outputLabel.setText("Output file");

        outputFile.setText("trans.out");

        setServerParams.setSelected(true);
        setServerParams.setText("Set connection XSLT parameters");
        setServerParams.setToolTipText("As part of the transformation, set the XSLT paramters is-server, is-port, is-user and is-name to pass into the transformation. This information cna be used as part of the transformation, e.g. when generating scripts.");

        transformMethodLabel.setText("Transform output method");

        outputMethodCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text", "XML", "HTML", "Name" }));

        javax.swing.GroupLayout transformPanelLayout = new javax.swing.GroupLayout(transformPanel);
        transformPanel.setLayout(transformPanelLayout);
        transformPanelLayout.setHorizontalGroup(
            transformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transformPanelLayout.createSequentialGroup()
                .addGroup(transformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(templateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(transformPanelLayout.createSequentialGroup()
                        .addGroup(transformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(transformLabel)
                            .addComponent(outputLabel)
                            .addComponent(transformMethodLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(transformPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(transformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(outputFile)
                            .addGroup(transformPanelLayout.createSequentialGroup()
                                .addComponent(transformText, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(transBrowseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(transformPanelLayout.createSequentialGroup()
                                .addGroup(transformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(setServerParams)
                                    .addComponent(outputMethodCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        transformPanelLayout.setVerticalGroup(
            transformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transformPanelLayout.createSequentialGroup()
                .addComponent(templateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transformLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(transformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transformText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transBrowseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(setServerParams)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transformMethodLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outputMethodCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        transformButton.setText("Transform");
        transformButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transformButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transformPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(transformButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transformPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transformButton)
                    .addComponent(cancelButton))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-438)/2, (screenSize.height-334)/2, 438, 334);
    }// </editor-fold>//GEN-END:initComponents

    private void transBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transBrowseButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        XSLFileFilter filter = new XSLFileFilter();
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            String fname = chooser.getSelectedFile().getAbsolutePath();
            this.transformText.setText(fname);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_transBrowseButtonActionPerformed

    public boolean showDialog()
    {
        setVisible(true);
        return result;
    }
    public FileViewFrame getTransformView()
    {
        return viewFrame;   
    }
    
    
    private void transformButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transformButtonActionPerformed
        String fpath = this.transformText.getText();
        File ftx = new File(fpath);
        if(!ftx.exists())
        {
            JOptionPane.showMessageDialog(null, "Could not open the transformation file " +
                    ftx.getAbsolutePath(), "Error - cannot open file.", JOptionPane.ERROR_MESSAGE);
            result = false;
        }
        else
        {
            try
            {
                // Save our solution to a temporary file.
                File tmpFile = File.createTempFile("tif", ".tmp");
                
                XmlFormatter fmt = new XmlFormatter();
                
                FileOutputStream ostream = new FileOutputStream(tmpFile);
                fmt.marshal(sol, ostream);
                ostream.close();
                
                TransformerFactory factory = TransformerFactory.newInstance();
                Source xslt = new StreamSource(ftx);
                Transformer transformer = factory.newTransformer(xslt);
                if(setServerParams.isSelected())
                {
                    IntegrityApi api = IntegrityApi.getInstance();
                    api.clientConnect();
                    IntegrityConnection conn = api.getDefaultConnection();
                    transformer.setParameter("is-server", conn.getHost());
                    transformer.setParameter("is-port", "" + conn.getPort());
                    transformer.setParameter("is-user", conn.getUser());
                    transformer.setParameter("is-password", conn.getPassword());
                }
                String outputMethod = (String)outputMethodCombo.getSelectedItem();
                transformer.setOutputProperty("method", outputMethod.toLowerCase());
                
                Source text = new StreamSource(tmpFile);
                File fout = new File(outputFile.getText());
                transformer.transform(text, new StreamResult(fout));

                viewFrame = new FileViewFrame(theApp);
                viewFrame.openFile(fout);
                result = true;
                tmpFile.deleteOnExit();
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, "Error applying transformation " +
                        e.getMessage(), "Transformation error", JOptionPane.ERROR_MESSAGE);
                result = false;
                
            }
        }
        setVisible(false);
        dispose();
    }//GEN-LAST:event_transformButtonActionPerformed

    private void transformTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transformTextActionPerformed
        
    }//GEN-LAST:event_transformTextActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        this.dispose();
        result = false;
    }//GEN-LAST:event_cancelButtonActionPerformed

    public class XSLFileFilter extends FileFilter
    {
        @Override
        public boolean accept(File f)
        {
            if(f.isDirectory())return true;
        	int dot = f.getName().lastIndexOf(".");
            if(dot == -1) return false;
            String extn = f.getName().substring(dot, f.getName().length());
            return ".xsl".equalsIgnoreCase(extn) || ".xslt".equalsIgnoreCase(extn) ;
        }
        
        @Override
        public String getDescription()
        {
        	return "XSL Files";
        }
    }
    
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
            java.util.logging.Logger.getLogger(TransformDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransformDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransformDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransformDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TransformDialog dialog = new TransformDialog(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField outputFile;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JComboBox outputMethodCombo;
    private javax.swing.JCheckBox setServerParams;
    private javax.swing.JLabel templateLabel;
    private javax.swing.JButton transBrowseButton;
    private javax.swing.JButton transformButton;
    private javax.swing.JLabel transformLabel;
    private javax.swing.JLabel transformMethodLabel;
    private javax.swing.JPanel transformPanel;
    private javax.swing.JTextField transformText;
    // End of variables declaration//GEN-END:variables
}