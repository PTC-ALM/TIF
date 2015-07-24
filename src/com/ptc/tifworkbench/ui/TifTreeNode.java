/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author pbowden
 */
public class TifTreeNode extends DefaultMutableTreeNode
{
    private boolean isedit = false;

    public TifTreeNode(String name)
    {
        super(name);
    }
    public TifTreeNode(String name, boolean isedit)
    {
        super(name);
        this.isedit = isedit;
    }
    public boolean isEdit()
    {
        return isedit;
    }
    
}
