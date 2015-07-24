/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pbowden
 */
public class StandardStates 
{
    public static final String [] STANDARD_STATES = 
        {"Unspecified"};

    private static List<String> standardStateList = null;
    
    public static List<String> getStandardStateList()
    {
        if(standardStateList == null) 
            standardStateList = new ArrayList<String>(Arrays.asList(STANDARD_STATES));
        return standardStateList;
    }
    
    
}
