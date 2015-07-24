/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui;

import com.ptc.tifworkbench.integrity.IntegrityConnection;
import com.ptc.tifworkbench.model.DatabaseConnection;
import com.ptc.tifworkbench.model.TifEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author pbowden
 */
public class EnvironmentDialog extends javax.swing.JDialog implements DocumentListener, ActionListener
{
    private boolean edit = false;
    boolean result = false;
    private TifEnvironment env = null;
    private UpdateTableModel updateTableModel = new UpdateTableModel();
    
    /**
     * Creates new form EnvironmentDialog
     */
    public EnvironmentDialog(java.awt.Frame parent, boolean modal, boolean edit) 
    {
        super(parent, modal);
        initComponents();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/com/ptc/tifworkbench/ui/environment.png")).getImage());

        // Set the listeners for validation.
        envName.getDocument().addDocumentListener(this);
        envPrefix.getDocument().addDocumentListener(this);
        envPath.getDocument().addDocumentListener(this);
        iClient.getDocument().addDocumentListener(this);
        iServer.getDocument().addDocumentListener(this);
        iHost.getDocument().addDocumentListener(this);
        iPort.getDocument().addDocumentListener(this);
        iUser.getDocument().addDocumentListener(this);
        iPassword.getDocument().addDocumentListener(this);
        dbName.getDocument().addDocumentListener(this);
        dbServer.getDocument().addDocumentListener(this);
        dbRestore.getDocument().addDocumentListener(this);
        dbUser.getDocument().addDocumentListener(this);
        dbPassword.getDocument().addDocumentListener(this);
        osCombo.addActionListener(this);
                
        this.edit = edit;
        if(edit)
        {
            createBtn.setText("Save"); 
            envPath.setEditable(false);
            updatePanel.setEnabled(true);
            setupUpdateInfo();
            enableControls();
        }
        else
        {
            createBtn.setText("Create");    
            envPath.setEditable(true);
            updatePanel.setEnabled(false);
            String osText = System.getProperty("os.name");
            osCombo.getEditor().setItem(osText);
            enableControls();
        }
    }
    
    public boolean showDialog()
    {
        setVisible(true);
        return result;
    }
    
    public TifEnvironment getEnvironment()
    {
        return env;
    }
    
    public void setEnvironment(TifEnvironment env)
    {
        this.env = env;
        
        this.envName.setText(env.getName());
        this.envPrefix.setText(env.getPrefix());
        this.envPath.setText(env.getEnvDir().getAbsolutePath());
        this.iClient.setText(env.getClientDir().getAbsolutePath());
        this.iServer.setText(env.getServerDir().getAbsolutePath());
        this.iHost.setText(env.getConn().getHost());
        this.iPort.setText(""+env.getConn().getPort()); 
        this.iUser.setText(env.getConn().getUser()); 
        this.iPassword.setText(env.getConn().getPassword());
        this.dbName.setText(env.getDbConn().getDbName()); 
        this.dbServer.setText(env.getDbConn().getDbServer()); 
        this.dbRestore.setText(env.getDbConn().getRestorePoint()); 
        this.dbUser.setText(env.getDbConn().getDbUser()); 
        this.dbPassword.setText(env.getDbConn().getDbPassword());
        this.osCombo.getEditor().setItem(env.getOS());
        enableControls();
    }
    
    private void setEnvironmentFromGui()
    {
        env.setName(envName.getText());
        env.setPrefix(envPrefix.getText());

        env.setClientDir(new File(this.iClient.getText()));
        env.setServerDir(new File(this.iServer.getText()));
        IntegrityConnection conn = new IntegrityConnection(iHost.getText(), Integer.parseInt(iPort.getText()), 
                iUser.getText(), iPassword.getText());
        env.setConn(conn);
        DatabaseConnection dbconn = new DatabaseConnection(dbName.getText(), dbServer.getText(),
                dbRestore.getText(), dbUser.getText(), dbPassword.getText());
        env.setDbConn(dbconn);
        env.setOS((String)osCombo.getEditor().getItem());
    }
    
    private void setupUpdateInfo()
    {
        zipCombo.removeAllItems();
        for(File f : TifEnvironment.getEnvironmentZips())
        {
            zipCombo.addItem(f);
        }
        envFileTable.setModel(updateTableModel);
        envFileTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        envFileTable.getColumnModel().getColumn(0).setMaxWidth(100);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tabPanel = new javax.swing.JPanel();
        envTabs = new javax.swing.JTabbedPane();
        envPropsPanel = new javax.swing.JPanel();
        envNameLbl = new javax.swing.JLabel();
        envPreLbl = new javax.swing.JLabel();
        envDirLbl = new javax.swing.JLabel();
        envName = new javax.swing.JTextField();
        envPrefix = new javax.swing.JTextField();
        envPath = new javax.swing.JTextField();
        envPathBrowseBtn = new javax.swing.JButton();
        osCombo = new javax.swing.JComboBox();
        osLabel = new javax.swing.JLabel();
        integrityPanel = new javax.swing.JPanel();
        ihostLbl = new javax.swing.JLabel();
        iportLbl = new javax.swing.JLabel();
        iuserLbl = new javax.swing.JLabel();
        ipasswordLbl = new javax.swing.JLabel();
        iserverLbl = new javax.swing.JLabel();
        iclientLbl = new javax.swing.JLabel();
        iHost = new javax.swing.JTextField();
        iPort = new javax.swing.JTextField();
        iUser = new javax.swing.JTextField();
        iPassword = new javax.swing.JPasswordField();
        iServer = new javax.swing.JTextField();
        iClient = new javax.swing.JTextField();
        iserverBrowseBtn = new javax.swing.JButton();
        iclientBrowseBtn = new javax.swing.JButton();
        databasePanel = new javax.swing.JPanel();
        dbServerLbl = new javax.swing.JLabel();
        dbNameLbl = new javax.swing.JLabel();
        dbRestoreLbl = new javax.swing.JLabel();
        dbUserLbl = new javax.swing.JLabel();
        dbPasswordLbl = new javax.swing.JLabel();
        dbServer = new javax.swing.JTextField();
        dbName = new javax.swing.JTextField();
        dbRestore = new javax.swing.JTextField();
        dbUser = new javax.swing.JTextField();
        dbPassword = new javax.swing.JPasswordField();
        updatePanel = new javax.swing.JPanel();
        updateLabel1 = new javax.swing.JLabel();
        zipCombo = new javax.swing.JComboBox();
        filePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        envFileTable = new javax.swing.JTable();
        updateBtn = new javax.swing.JButton();
        selAllBtn = new javax.swing.JButton();
        selNoneBtn = new javax.swing.JButton();
        btnPanel = new javax.swing.JPanel();
        createBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(null);

        tabPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        envNameLbl.setText("Name");

        envPreLbl.setText("Prefix");

        envDirLbl.setText("Directory");

        envPrefix.setText("TIF");

        envPathBrowseBtn.setText("...");
        envPathBrowseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                envPathBrowseBtnActionPerformed(evt);
            }
        });

        osCombo.setEditable(true);
        osCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Windows 7", "Windows Server 2008", "Windows Server 2008 R2", "Windows 8", "Windows 2003", "Linux", " ", " ", " ", " " }));

        osLabel.setText("Operating System");

        javax.swing.GroupLayout envPropsPanelLayout = new javax.swing.GroupLayout(envPropsPanel);
        envPropsPanel.setLayout(envPropsPanelLayout);
        envPropsPanelLayout.setHorizontalGroup(
            envPropsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(envPropsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(envPropsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(envPropsPanelLayout.createSequentialGroup()
                        .addComponent(envPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(envPathBrowseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(envPropsPanelLayout.createSequentialGroup()
                        .addGroup(envPropsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(envNameLbl)
                            .addComponent(envPreLbl)
                            .addComponent(envDirLbl)
                            .addComponent(envName, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(envPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(osLabel)
                            .addComponent(osCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 151, Short.MAX_VALUE)))
                .addContainerGap())
        );
        envPropsPanelLayout.setVerticalGroup(
            envPropsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(envPropsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(envNameLbl)
                .addGap(3, 3, 3)
                .addComponent(envName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(envPreLbl)
                .addGap(1, 1, 1)
                .addComponent(envPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(envDirLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(envPropsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(envPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(envPathBrowseBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(osLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(osCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        envTabs.addTab("Environment", envPropsPanel);

        ihostLbl.setText("Host");

        iportLbl.setText("Port");

        iuserLbl.setText("User");

        ipasswordLbl.setText("Password");

        iserverLbl.setText("Server Directory");

        iclientLbl.setText("Client Directory");

        iHost.setText("myhost");

        iPort.setText("7001");

        iUser.setText("administrator");

        iPassword.setText("password");

        iServer.setText("c:/Program Files/Integrity/IntegrityServer10");

        iClient.setText("c:/Program Files (x86)/Integrity/IntegrityClient10");

        iserverBrowseBtn.setText("...");

        iclientBrowseBtn.setText("...");

        javax.swing.GroupLayout integrityPanelLayout = new javax.swing.GroupLayout(integrityPanel);
        integrityPanel.setLayout(integrityPanelLayout);
        integrityPanelLayout.setHorizontalGroup(
            integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(integrityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(integrityPanelLayout.createSequentialGroup()
                        .addComponent(iServer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iserverBrowseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(integrityPanelLayout.createSequentialGroup()
                        .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(integrityPanelLayout.createSequentialGroup()
                                .addComponent(ihostLbl)
                                .addGap(166, 166, 166)
                                .addComponent(iportLbl))
                            .addComponent(iserverLbl)
                            .addComponent(iclientLbl)
                            .addGroup(integrityPanelLayout.createSequentialGroup()
                                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(iUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(iHost, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(iuserLbl, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ipasswordLbl)
                                    .addComponent(iPort, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                    .addComponent(iPassword))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(integrityPanelLayout.createSequentialGroup()
                        .addComponent(iClient, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iclientBrowseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        integrityPanelLayout.setVerticalGroup(
            integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(integrityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ihostLbl)
                    .addComponent(iportLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iuserLbl)
                    .addComponent(ipasswordLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(iserverLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iserverBrowseBtn))
                .addGap(18, 18, 18)
                .addComponent(iclientLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(integrityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iClient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iclientBrowseBtn))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        envTabs.addTab("Integrity", integrityPanel);

        dbServerLbl.setText("Database Server");

        dbNameLbl.setText("Database Name");

        dbRestoreLbl.setText("Restore Point");

        dbUserLbl.setText("Admin User");

        dbPasswordLbl.setText("Admin Password");

        dbServer.setText("sqlexpress");
        dbServer.setToolTipText("For example: myserver\\\\sqlexpress");

        dbName.setText("tif");
        dbName.setToolTipText("The name of the database you'll be using.");

        dbRestore.setText("cleandb.bak");

        dbUser.setText("sa");

        dbPassword.setText("mks123");

        javax.swing.GroupLayout databasePanelLayout = new javax.swing.GroupLayout(databasePanel);
        databasePanel.setLayout(databasePanelLayout);
        databasePanelLayout.setHorizontalGroup(
            databasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(databasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dbRestore, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dbServerLbl)
                    .addComponent(dbNameLbl)
                    .addComponent(dbRestoreLbl)
                    .addGroup(databasePanelLayout.createSequentialGroup()
                        .addComponent(dbUserLbl)
                        .addGap(130, 130, 130)
                        .addComponent(dbPasswordLbl))
                    .addGroup(databasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(dbName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                        .addComponent(dbServer, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(databasePanelLayout.createSequentialGroup()
                        .addComponent(dbUser, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(dbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        databasePanelLayout.setVerticalGroup(
            databasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dbServerLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(dbNameLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dbRestoreLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbRestore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(databasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dbUserLbl)
                    .addComponent(dbPasswordLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(databasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        envTabs.addTab("Database", databasePanel);

        updateLabel1.setText("Update using environment files in");

        zipCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "tif-template1.0.6.zip", "tif-template1.0.5.zip" }));

        filePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Environment files"));

        envFileTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(envFileTable);

        javax.swing.GroupLayout filePanelLayout = new javax.swing.GroupLayout(filePanel);
        filePanel.setLayout(filePanelLayout);
        filePanelLayout.setHorizontalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        filePanelLayout.setVerticalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );

        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        selAllBtn.setText("Select All");
        selAllBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selAllBtnActionPerformed(evt);
            }
        });

        selNoneBtn.setText("Select None");
        selNoneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selNoneBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updatePanelLayout = new javax.swing.GroupLayout(updatePanel);
        updatePanel.setLayout(updatePanelLayout);
        updatePanelLayout.setHorizontalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updatePanelLayout.createSequentialGroup()
                        .addComponent(selAllBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selNoneBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                        .addComponent(updateBtn))
                    .addGroup(updatePanelLayout.createSequentialGroup()
                        .addComponent(updateLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(zipCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        updatePanelLayout.setVerticalGroup(
            updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zipCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(updatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateBtn)
                    .addComponent(selAllBtn)
                    .addComponent(selNoneBtn))
                .addContainerGap())
        );

        envTabs.addTab("Update", updatePanel);

        javax.swing.GroupLayout tabPanelLayout = new javax.swing.GroupLayout(tabPanel);
        tabPanel.setLayout(tabPanelLayout);
        tabPanelLayout.setHorizontalGroup(
            tabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(envTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        tabPanelLayout.setVerticalGroup(
            tabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(envTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        createBtn.setText("Create");
        createBtn.setEnabled(false);
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnPanelLayout = new javax.swing.GroupLayout(btnPanel);
        btnPanel.setLayout(btnPanelLayout);
        btnPanelLayout.setHorizontalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelBtn)
                .addContainerGap())
        );
        btnPanelLayout.setVerticalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn)
                    .addComponent(createBtn))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-433)/2, (screenSize.height-389)/2, 433, 389);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        this.setVisible(false);
        this.dispose();
        result = false;
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void createBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBtnActionPerformed
        if(edit)
            doEditEnv();
        else
            doCreateEnv();
    }//GEN-LAST:event_createBtnActionPerformed

    private void doCreateEnv() 
    {
       try
       {
            env = TifEnvironment.createEnvironment(new File(this.envPath.getText()));
            setEnvironmentFromGui();
            env.writeEnvironment();
            env.createNewTif();
            result = true;
       } catch (Exception ex) 
       {
           JOptionPane.showMessageDialog(null, "Could not create a new environment: " +
                    envPath.getText() + "\n" + 
                    ex.getMessage(), "Environment error", JOptionPane.ERROR_MESSAGE);
            
            result = false;
       }
       finally
       {
            this.setVisible(false);
            this.dispose();   
       }
    }
    
    private void doEditEnv() 
    {
       try 
       {
            setEnvironmentFromGui();
            env.writeEnvironment();
            result = true;
       } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not create / edit environment" +
                    envPath.getText() + "\n" + 
                    ex.getMessage(), "Environment error", JOptionPane.ERROR_MESSAGE);
            
            result = false;
       }
       finally
       {
            this.setVisible(false);
            this.dispose();   
      }
    }
    
    private void envPathBrowseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_envPathBrowseBtnActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            File path = chooser.getSelectedFile();
            envPath.setText(path.getAbsolutePath());
        }
    }//GEN-LAST:event_envPathBrowseBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        System.out.println("Updating environment");
        File envZip = (File)zipCombo.getSelectedItem();
        try
        {
            List<TifEnvironment.EnvStaticFiles> selFiles = new ArrayList<TifEnvironment.EnvStaticFiles>();
            for(int i=0; i<updateTableModel.getRowCount(); i++)
            {
                if(updateTableModel.isRowSelected(i))
                {
                    System.out.println("File " + updateTableModel.getValueAt(i, 1) + " is selected");
                    selFiles.add((TifEnvironment.EnvStaticFiles)updateTableModel.getValueAt(i, 2));
                }
            }
            env.updateEnvironment(selFiles, envZip);
        } catch (Exception ex) 
        {
           JOptionPane.showMessageDialog(null, "Could not update environment: " +
                    envPath.getText() + " with " + envZip.getName() + "\n" + 
                    ex.getMessage(), "Environment error", JOptionPane.ERROR_MESSAGE);
            
            result = false;
        }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void selAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selAllBtnActionPerformed
        for(int i=0; i<updateTableModel.getRowCount(); i++)
        {
            updateTableModel.setRowSelected(i, Boolean.TRUE);
        }
        updateTableModel.fireTableDataChanged();
    }//GEN-LAST:event_selAllBtnActionPerformed

    private void selNoneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selNoneBtnActionPerformed
        for(int i=0; i<updateTableModel.getRowCount(); i++)
        {
            updateTableModel.setRowSelected(i, Boolean.FALSE);
        }
        updateTableModel.fireTableDataChanged();
    }//GEN-LAST:event_selNoneBtnActionPerformed

    private void enableControls()
    {
        if(isSet(envName) && isSet(envPrefix) && isSet(envPath) &&
           isSet(iClient) && isSet(iServer) && isSet(iHost) && 
           isSet(iPort) && isSet(iUser) && isSet(iPassword) && 
           isSet(dbName) && isSet(dbServer) && isSet(dbRestore) && 
           isSet(dbUser) && isSet(dbPassword))
        {
            createBtn.setEnabled(true);
        }
        else
        {
            createBtn.setEnabled(false);
        }
    }
    
    private boolean isSet(JTextField field)
    {
        return (field.getText() != null) && (field.getText().length() > 0);
    }

    
    class UpdateTableModel extends AbstractTableModel 
    {
        private String[] columnNames = {"Update", "File"};
        private Object [][] data;
        
        public UpdateTableModel()
        {
            super();
            int len = TifEnvironment.EnvStaticFiles.values().length;
            data = new Object[len][3];
            for(int i=0; i<len; i++)
            {
                data[i][0]= Boolean.TRUE;
                data[i][1] = TifEnvironment.EnvStaticFiles.values()[i].toString();
                data[i][2] = TifEnvironment.EnvStaticFiles.values()[i];
            }
            
        }
        @Override
        public int getColumnCount() 
        {
            return 2;
        }

        @Override
        public int getRowCount() 
        {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) 
        {
            return col==0;
        }
        
        public void setRowSelected(int row, Boolean sel)
        {
            if(row < getRowCount() && row >= 0)
                data[row][0] = sel;
        }
        public Boolean isRowSelected(int row)
        {
            if(row < getRowCount() && row >= 0)
                return (Boolean)data[row][0];
            else
                return false;
        }
        @Override
        public void setValueAt(Object value, int row, int col) 
        {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
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
            java.util.logging.Logger.getLogger(EnvironmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EnvironmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EnvironmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EnvironmentDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EnvironmentDialog dialog = new EnvironmentDialog(new javax.swing.JFrame(), true, false);
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
    private javax.swing.JPanel btnPanel;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton createBtn;
    private javax.swing.JPanel databasePanel;
    private javax.swing.JTextField dbName;
    private javax.swing.JLabel dbNameLbl;
    private javax.swing.JPasswordField dbPassword;
    private javax.swing.JLabel dbPasswordLbl;
    private javax.swing.JTextField dbRestore;
    private javax.swing.JLabel dbRestoreLbl;
    private javax.swing.JTextField dbServer;
    private javax.swing.JLabel dbServerLbl;
    private javax.swing.JTextField dbUser;
    private javax.swing.JLabel dbUserLbl;
    private javax.swing.JLabel envDirLbl;
    private javax.swing.JTable envFileTable;
    private javax.swing.JTextField envName;
    private javax.swing.JLabel envNameLbl;
    private javax.swing.JTextField envPath;
    private javax.swing.JButton envPathBrowseBtn;
    private javax.swing.JLabel envPreLbl;
    private javax.swing.JTextField envPrefix;
    private javax.swing.JPanel envPropsPanel;
    private javax.swing.JTabbedPane envTabs;
    private javax.swing.JPanel filePanel;
    private javax.swing.JTextField iClient;
    private javax.swing.JTextField iHost;
    private javax.swing.JPasswordField iPassword;
    private javax.swing.JTextField iPort;
    private javax.swing.JTextField iServer;
    private javax.swing.JTextField iUser;
    private javax.swing.JButton iclientBrowseBtn;
    private javax.swing.JLabel iclientLbl;
    private javax.swing.JLabel ihostLbl;
    private javax.swing.JPanel integrityPanel;
    private javax.swing.JLabel ipasswordLbl;
    private javax.swing.JLabel iportLbl;
    private javax.swing.JButton iserverBrowseBtn;
    private javax.swing.JLabel iserverLbl;
    private javax.swing.JLabel iuserLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox osCombo;
    private javax.swing.JLabel osLabel;
    private javax.swing.JButton selAllBtn;
    private javax.swing.JButton selNoneBtn;
    private javax.swing.JPanel tabPanel;
    private javax.swing.JButton updateBtn;
    private javax.swing.JLabel updateLabel1;
    private javax.swing.JPanel updatePanel;
    private javax.swing.JComboBox zipCombo;
    // End of variables declaration//GEN-END:variables


    @Override
    public void insertUpdate(DocumentEvent e) {
        enableControls();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        enableControls();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        enableControls();
    }
    
    /**
     * Actions from the os combobox
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }
    
}
