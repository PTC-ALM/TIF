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
public class StandardPhases 
{
    public static final String [] STANDARD_PHASES = 
        {"Out of Phase"};

    private static List<String> standardPhaseList = null;
    
    public static List<String> getStandardPhaseList()
    {
        if(standardPhaseList == null) 
            standardPhaseList = new ArrayList<String>(Arrays.asList(STANDARD_PHASES));
        return standardPhaseList;
    }
    
    
}
