/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import com.ptc.tifworkbench.jaxbbinding.ImSolution;

/**
 *
 * @author pbowden
 */
public interface SolutionViewer 
{
    public void setSolution(ImSolution sol);
    public ImSolution getSolution();  
}
