/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui.tree;


import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.ui.TifTreeModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;



public class TifCheckTreePanel extends JPanel 
{

    private static final long serialVersionUID = 1L;

    ImageIcon newIcon = new ImageIcon(getClass().getResource("/com/ptc/tifworkbench/ui/new.png"));
    ImageIcon editIcon = new ImageIcon(getClass().getResource("/com/ptc/tifworkbench/ui/edit.png"));
    ImageIcon tifIcon = new ImageIcon(getClass().getResource("/com/ptc/tifworkbench/ui/tif.gif"));
    ImageIcon elIcon = new ImageIcon(getClass().getResource("/com/ptc/tifworkbench/ui/el.gif"));
    ImageIcon elIconEx = new ImageIcon(getClass().getResource("/com/ptc/tifworkbench/ui/elex.gif"));

    protected JTree  m_tree;
    protected DefaultTreeModel m_model;

    AddCheckBoxToTree AddCh = new AddCheckBoxToTree();

    private AddCheckBoxToTree.CheckTreeManager checkTreeManager;


    protected TreePath m_clickedPath;

    public TifCheckTreePanel()
    {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                new IconData(tifIcon, null, "TIF"));

        DefaultMutableTreeNode node;
        File[] roots = File.listRoots();
        for (int k=0; k<roots.length; k++)
        {
            node = new DefaultMutableTreeNode(new IconData(elIcon, null, new FileNode(roots[k])));
            top.add(node);
            node.add(new DefaultMutableTreeNode(true));
        }

        m_model = new DefaultTreeModel(top);

        m_tree = new JTree(m_model){
            @Override
            public String getToolTipText(MouseEvent ev) 
            {
                if(ev == null)
                    return null;
                TreePath path = m_tree.getPathForLocation(ev.getX(), 
                        ev.getY());
                if (path != null)
                {
                    FileNode fnode = getFileNode(getTreeNode(path));
                    if (fnode==null)
                        return null;
                    File f = fnode.getFile();
                    return (f==null ? null : f.getPath());
                }
                return null;
            }
        };

        ToolTipManager.sharedInstance().registerComponent(m_tree);

        m_tree.putClientProperty("JTree.lineStyle", "Angled");

        TreeCellRenderer renderer = new IconCellRenderer();
        m_tree.setCellRenderer(renderer);

        m_tree.addTreeExpansionListener(new DirExpansionListener());

        m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); 
        m_tree.setShowsRootHandles(true); 
        m_tree.setEditable(false);


        checkTreeManager = AddCh.new CheckTreeManager(m_tree, null);
        JScrollPane treeScroll = new JScrollPane();
        treeScroll.setViewportView(m_tree);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        
        //this.add(s);
    }

    public void setModel(ImSolution sol)
    {
        this.m_tree.setModel(TifTreeModel.createTree(sol));
    }

    
    DefaultMutableTreeNode getTreeNode(TreePath path)
    {
        return (DefaultMutableTreeNode)(path.getLastPathComponent());
    }

    FileNode getFileNode(DefaultMutableTreeNode node)
    {
        if (node == null)
            return null;
        Object obj = node.getUserObject();
        if (obj instanceof IconData)
            obj = ((IconData)obj).getObject();
        if (obj instanceof FileNode)
            return (FileNode)obj;
        else
            return null;
    }

    public AddCheckBoxToTree.CheckTreeManager getCheckTreeManager() {
        return checkTreeManager;
    }

    // Make sure expansion is threaded and updating the tree model
    // only occurs within the event dispatching thread.
    class DirExpansionListener implements TreeExpansionListener
    {
        @Override
        public void treeExpanded(TreeExpansionEvent event)
        {
            final DefaultMutableTreeNode node = getTreeNode(
                    event.getPath());
            final FileNode fnode = getFileNode(node);

            Thread runner = new Thread() 
            {
                @Override
                public void run() 
                {
                    if (fnode != null && fnode.expand(node)) 
                    {
                        Runnable runnable = new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                m_model.reload(node);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                }
            };
            runner.start();
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {}
    }


    class IconCellRenderer extends JLabel implements TreeCellRenderer
    {
        protected Color m_textSelectionColor;
        protected Color m_textNonSelectionColor;
        protected Color m_bkSelectionColor;
        protected Color m_bkNonSelectionColor;
        protected Color m_borderSelectionColor;

        protected boolean m_selected;

        public IconCellRenderer()
        {
            super();
            m_textSelectionColor = UIManager.getColor(
                    "Tree.selectionForeground");
            m_textNonSelectionColor = UIManager.getColor(
                    "Tree.textForeground");
            m_bkSelectionColor = UIManager.getColor(
                    "Tree.selectionBackground");
            m_bkNonSelectionColor = UIManager.getColor(
                    "Tree.textBackground");
            m_borderSelectionColor = UIManager.getColor(
                    "Tree.selectionBorderColor");
            setOpaque(false);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, 
                Object value, boolean sel, boolean expanded, boolean leaf, 
                int row, boolean hasFocus) 

        {
            DefaultMutableTreeNode node = 
                    (DefaultMutableTreeNode)value;
            Object obj = node.getUserObject();
            setText(obj.toString());

            if (obj instanceof Boolean)
                setText("Retrieving data...");

            if (obj instanceof IconData)
            {
                IconData idata = (IconData)obj;
                if (expanded)
                    setIcon(idata.getExpandedIcon());
                else
                    setIcon(idata.getIcon());
            }
            else
                setIcon(null);

            setFont(tree.getFont());
            setForeground(sel ? m_textSelectionColor : 
                m_textNonSelectionColor);
            setBackground(sel ? m_bkSelectionColor : 
                m_bkNonSelectionColor);
            m_selected = sel;
            return this;
        }

        @Override
        public void paintComponent(Graphics g) 
        {
            Color bColor = getBackground();
            Icon icon = getIcon();

            g.setColor(bColor);
            int offset = 0;
            if(icon != null && getText() != null) 
                offset = (icon.getIconWidth() + getIconTextGap());
            g.fillRect(offset, 0, getWidth() - 1 - offset,
                    getHeight() - 1);

            if (m_selected) 
            {
                g.setColor(m_borderSelectionColor);
                g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);
            }

            super.paintComponent(g);
        }
    }

    class IconData 
    {
        protected Icon   m_icon;
        protected Icon   m_expandedIcon;
        protected Object m_data;

        public IconData(Icon icon, Object data)
        {
            m_icon = icon;
            m_expandedIcon = null;
            m_data = data;
        }

        public IconData(Icon icon, Icon expandedIcon, Object data)
        {
            m_icon = icon;
            m_expandedIcon = expandedIcon;
            m_data = data;
        }

        public Icon getIcon() 
        { 
            return m_icon;
        }

        public Icon getExpandedIcon() 
        { 
            return m_expandedIcon!=null ? m_expandedIcon : m_icon;
        }

        public Object getObject() 
        { 
            return m_data;
        }

        @Override
        public String toString() 
        { 
            return m_data.toString();
        }
    }

    class FileNode 
    {
        protected File m_file;

        public FileNode(File file)
        {
            m_file = file;
        }

        public File getFile() 
        { 
            return m_file;
        }

        public String toString() 
        { 
            return m_file.getName().length() > 0 ? m_file.getName() : 
                m_file.getPath();
        }

        public boolean expand(DefaultMutableTreeNode parent){
            DefaultMutableTreeNode flag = (DefaultMutableTreeNode)parent.getFirstChild();
            if (flag==null)    // No flag
                return false;
            Object obj = flag.getUserObject();
            if (!(obj instanceof Boolean))
                return false;      // Already expanded

            parent.removeAllChildren();  // Remove Flag

            File[] files = listFiles();
            if (files == null)
                return true;

            Vector<FileNode> v = new Vector<FileNode>();

            for (int k=0; k<files.length; k++){
                File f = files[k];
                if (!(f.isDirectory()))
                    continue;

                FileNode newNode = new FileNode(f);

                boolean isAdded = false;
                for (int i=0; i<v.size(); i++)
                {
                    FileNode nd = (FileNode)v.elementAt(i);
                    if (newNode.compareTo(nd) < 0)
                    {
                        v.insertElementAt(newNode, i);
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded)
                    v.addElement(newNode);
            }

            for (int i=0; i<v.size(); i++){
                FileNode nd = (FileNode)v.elementAt(i);
                IconData idata = new IconData(elIcon, elIconEx, nd);
                DefaultMutableTreeNode node = new 
                        DefaultMutableTreeNode(idata);
                parent.add(node);

                if (nd.hasSubDirs())
                    node.add(new DefaultMutableTreeNode( 
                            new Boolean(true) ));
            }

            return true;
        }

        public boolean hasSubDirs(){
            File[] files = listFiles();
            if (files == null)
                return false;
            for (int k=0; k<files.length; k++)
            {
                if (files[k].isDirectory())
                    return true;
            }
            return false;
        }

        public int compareTo(FileNode toCompare){ 
            return  m_file.getName().compareToIgnoreCase(
                    toCompare.m_file.getName() ); 
        }

        protected File[] listFiles(){
            if (!m_file.isDirectory())
                return null;
            try
            {
                return m_file.listFiles();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Error reading directory "+m_file.getAbsolutePath(),"Warning", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }
    }
 }