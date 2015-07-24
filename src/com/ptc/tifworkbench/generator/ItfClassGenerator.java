/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.generator;

import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author pbowden
 */
public class ItfClassGenerator extends JavaFileClassGenerator
{
    public ItfClassGenerator(ImSolution solution, String pkg, File dir)
    {
        super(solution, pkg, dir);
    }
    
    @Override
    public String getDescription()
    {
        return "Generate ITF wrapper classes.";
    }
        
    protected JavaClassWriter createClassWriter(String className, TypeDefinition tdef) throws FileNotFoundException, IOException
    {
        return new ItfJavaClassWriter(getPackageName(), className, tdef, getDirectory());    
    }
    
    
}
