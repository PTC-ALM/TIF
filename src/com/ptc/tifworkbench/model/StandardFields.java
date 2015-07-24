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
public class StandardFields 
{
    // Note: does not include Shared Category as this is a field commonly edited
    // and we need to know what's been done.
    public static final String [] STANDARD_FIELDS = 
        {"Type", "ID", "Created By", "Created Date", "Modified By", "Modified Date",
         "Summary", "State", "Assigned User", "Assigned Group", "Project", "Forward Relationships",
         "Backward Relationships", "Signed By", "Signature Comment", "Attachments",
         "Contains", "Contained By", "References", "Referenced By", "Reference Mode",
         "Root ID", "Input Revision Date", "Document ID", "Subsegment Name",
         "Referenced Item Type", "Tests", "Tests For", "Shared Test Steps", "Test Cases",
         "Category", "Parameters", "Parameter Values", "Shares",
         "Shared By", "Test Steps", "Tests As Of Date", "Bookmarks", "Referenced Bookmarks",
         "Revision", "Revision Increment Date", "Significant Edit Date", "Significant Change Since Item Revision",
         "Item Significant Edit Date on Shared Item", "Review Sessions", "Review Session For",
         "Comment Text", "In Session", "Comment For", "Is Approval Comment", "Selection Index"};

    private static List<String> standardFieldList = null;
    
    public static List<String> getStandardFieldList()
    {
        if(standardFieldList == null) 
            standardFieldList = new ArrayList<String>(Arrays.asList(STANDARD_FIELDS));
        return standardFieldList;
    }
}
