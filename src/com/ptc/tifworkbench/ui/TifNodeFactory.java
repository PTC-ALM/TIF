/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author pbowden
 */
public abstract class TifNodeFactory<E>
{
        // D is the definitions container, E is the element in the container.
    private List<E> eAdd, eEdit;

    public TifNodeFactory(List<E> eAdd, List<E> eEdit)
    {
        this.eAdd=eAdd;
        this.eEdit=eEdit;
    }

    abstract public String getKey(E val);
    abstract public void addAttributes(TifTreeNode node, E val);

    public void createNodes(DefaultMutableTreeNode node)
    {
        for(E def : eAdd)
        {
            TifTreeNode newNode = new TifTreeNode(getKey(def), false);
            addAttributes(newNode, def);
            node.add(newNode);
        }
        if(eEdit == null)return;
        for(E def : eEdit)
        {
            TifTreeNode newNode = new TifTreeNode(getKey(def), true);
            addAttributes(newNode, def);
            node.add(newNode);
        }
    }


}
