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
public class StandardQueries 
{
    public static final String [] STANDARD_QUERIES = 
        {"Quick Query"};

    private static List<String> standardQueryList = null;
    
    public static List<String> getStandardQueryList()
    {
        if(standardQueryList == null) 
            standardQueryList = new ArrayList<String>(Arrays.asList(STANDARD_QUERIES));
        return standardQueryList;
    }
    
}
