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
public class StandardTriggers
{
    public static final String [] STANDARD_TRIGGERS = 
        {"Integrity - Historic Computation Fields - Daily",
         "Integrity - Historic Computation Fields - Weekly",
         "Integrity - Historic Computation Fields - Monthly"};

    private static List<String> standardTriggerList = null;
    
    public static List<String> getStandardTriggerList()
    {
        if(standardTriggerList == null) 
            standardTriggerList = new ArrayList<String>(Arrays.asList(STANDARD_TRIGGERS));
        return standardTriggerList;
    }
    
    
}
