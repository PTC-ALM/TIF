/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import com.ptc.tifworkbench.jaxbbinding.FieldDefinition;
import com.ptc.tifworkbench.jaxbbinding.FieldReference;
import com.ptc.tifworkbench.jaxbbinding.FieldType;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.StateDefinition;
import com.ptc.tifworkbench.jaxbbinding.StateReference;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author pbowden
 */
public class TifChecker 
{
    private StringBuilder report = new StringBuilder();
    private ImSolution sol = null;
    private Set<String> declaredFields = new HashSet<String>();
    private Set<String> usedFields = new HashSet<String>();
    private Set<String> declaredStates = new HashSet<String>();
    private Set<String> usedStates = new HashSet<String>();
    
    public TifChecker(String title, ImSolution sol)
    {
        this.sol = sol;
        readDeclaredFields();    
        readUsedFields();    
        readDeclaredStates();    
        readUsedStates();
        
        report.append(title).append("\n");
        reportFieldUsage();
        reportStateUsage();
    }
    
    public String getReport()
    {
        return report.toString();
    }

    private void readDeclaredFields() 
    {
        for(FieldDefinition fdef : sol.getFields().getField())
        {
            declaredFields.add(fdef.getName()); 
            if(fdef.getType()==FieldType.RELATIONSHIP)
            {
                declaredFields.add(fdef.getReverse().getName()); 
            }
        }
    }

    private void readUsedFields() 
    {
        for(TypeDefinition tdef : sol.getTypes().getType())
        {
            for(FieldReference fref : tdef.getFields().getField())
            {
                if(!usedFields.contains(fref.getName())) 
                    usedFields.add(fref.getName());    
            }
        }
    }

    private void readDeclaredStates() 
    {
        for(StateDefinition sdef : sol.getStates().getState())
        {
            declaredStates.add(sdef.getName());    
        }
    }

    private void readUsedStates() 
    {
        for(TypeDefinition tdef : sol.getTypes().getType())
        {
            for(StateReference sref : tdef.getStates().getState())
            {
                if(!usedStates.contains(sref.getName())) 
                    usedStates.add(sref.getName());    
            }
        }
    }

    private void reportFieldUsage() 
    {
        Set<String>declaredStandardFields = new HashSet(StandardFields.getStandardFieldList());
        declaredStandardFields.retainAll(declaredFields);
        if(declaredStandardFields.size() > 0)
        {
            report.append("Error. Declared standard fields: ").append(declaredStandardFields.size()).append("\n");
            for(String fieldName : declaredStandardFields)
                report.append("\t").append(fieldName).append("\n");   
        }
        declaredFields.removeAll(StandardFields.getStandardFieldList());
        usedFields.removeAll(StandardFields.getStandardFieldList());
        
        // Remove all the used fields from declared fields to find fields declared but not used.
        Set<String> unusedFields = new HashSet(declaredFields);
        unusedFields.removeAll(usedFields);
        report.append("Fields declared but not used in types: ").append(unusedFields.size()).append("\n");
        if(unusedFields.size() > 0)
        {
            for(String fieldName : unusedFields)
                report.append("\t").append(fieldName).append("\n");   
        }
        
        // Remove all the declared fields from used fields to find fields used but not declared.
        Set<String> undeclaredFields = new HashSet(usedFields);
        undeclaredFields.removeAll(declaredFields);
        report.append("Fields used in types but not declared: ").append(undeclaredFields.size()).append("\n");
        if(undeclaredFields.size() > 0)
        {
            for(String fieldName : undeclaredFields)
                report.append("\t").append(fieldName).append("\n");   
        }
    }

    private void reportStateUsage() 
    {
        // Remove all the used fields from declared fields to find fields declared but not used.
        Set<String> unusedStates = new HashSet(declaredStates);
        unusedStates.removeAll(usedStates);
        report.append("States declared but not used in types: ").append(unusedStates.size()).append("\n");
        if(unusedStates.size() > 0)
        {
            for(String stateName : unusedStates)
                report.append("\t").append(stateName).append("\n");   
        }
        
        // Remove all the declared fields from used fields to find fields used but not declared.
        Set<String> undeclaredStates = new HashSet(usedStates);
        undeclaredStates.removeAll(declaredStates);
        report.append("States used in types but not declared: ").append(undeclaredStates.size()).append("\n");
        if(undeclaredStates.size() > 0)
        {
            for(String stateName : undeclaredStates)
                report.append("\t").append(stateName).append("\n");   
        }
    }
}
