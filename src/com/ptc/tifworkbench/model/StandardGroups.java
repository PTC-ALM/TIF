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
public class StandardGroups 
{
    public static final String [] STANDARD_GROUPS = 
        {"everyone"};

    private static List<String> standardGroupList = null;
    
    public static List<String> getStandardGroupList()
    {
        if(standardGroupList == null) 
            standardGroupList = new ArrayList<String>(Arrays.asList(STANDARD_GROUPS));
        return standardGroupList;
    }
    
}
