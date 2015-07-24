/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.generator;

import com.ptc.tifworkbench.jaxbbinding.FieldReference;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author pbowden
 */
public class SolutionClassGenerator extends JavaFileClassGenerator 
{
    public SolutionClassGenerator(ImSolution solution, String pkg, File dir)
    {
        super(solution, pkg, dir);
    }
    
    @Override
    public String getDescription()
    {
        return "Generate simple wrapper classes.";
    }

}
