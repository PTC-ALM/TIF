/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pbowden
 */
public abstract class CompoundTifDifference<E> 
{
    // D is the definitions container, E is the element in the container.
    // d1 is the source, d2 is the target. We want to make d1 equivalent to d2
    private List<E> d1, d2, dAddDiff, dEditDiff;
    
    public CompoundTifDifference(List<E> d1, List<E> d2, List<E> dAddDiff, List<E> dEditDiff)
    {
        this.d1=d1;
        this.d2=d2;
        this.dAddDiff=dAddDiff;
        this.dEditDiff=dEditDiff;
    }

    abstract public String getKey(E val);
    abstract public void subCompare(E val1, E val2, List<E> diff);
    abstract public void logMessage(String message);
    
    public void compareDefinitions()
    {
        HashMap<String, E> map1 = createDiffHash(d1);
        if(!d1.equals(d2))
        {
            for(E def : d2)
            {
                logMessage("  Checking: " + getKey(def));
                // Is f1 in s1?
                if(!map1.containsKey(getKey(def))) 
                {
                    // Not in s1, add it.
                    dAddDiff.add(def);
                    logMessage("    ** Adding: " + getKey(def));
                }
                else 
                {
                    E def1 = map1.get(getKey(def));
                    // Is f2==f1? 
                    if(!def.equals(def1))
                    {
                        logMessage("    ** Changed: " + getKey(def));
                        // Different. Add an empty element and poulate it with the 
                        // sub comparison method.
                        //dDiff.add(def);
                        subCompare(map1.get(getKey(def)), def, dEditDiff);
                    }

                }
            }
        }
    }
    protected HashMap<String, E> createDiffHash(List<E> defs)
    {
        HashMap<String, E> map = new HashMap<String, E>(); 
        for(E def : defs)
        {
            map.put(getKey(def), def);
        }
        return map;
    }

    
}
