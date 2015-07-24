/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.generator;

import com.ptc.tifworkbench.jaxbbinding.FieldReference;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author pbowden
 */
public class BasicJavaClassWriter extends AbstractClassWriter 
{
    
    public BasicJavaClassWriter(String packageName, String className, TypeDefinition tdef, File directory) throws FileNotFoundException, IOException 
    {
        super(packageName, className, tdef, directory);
    }

    @Override
    protected void writeClassHeader(TypeDefinition tdef, String className, PrintStream ps) throws IOException 
    {
        println(0, "/* Generated class for " + className + " do not edit. */");
        println(0, "package " + getPackageName() + ";");
    }

    @Override
    protected void writeClassDeclaration(TypeDefinition tdef, String className, PrintStream ps) throws IOException 
    {
        println(0, "class " + className);
        println(0, "{");
    }

    @Override
    protected void writeClassMembers(TypeDefinition tdef, String className, PrintStream ps) throws IOException 
    {
    }

    @Override
    protected void writeConstructor(TypeDefinition tdef, String className, PrintStream ps) throws IOException 
    {
        println(1, "public " + className + "()");
        println(1, "{");
        println(1, "}");
    }

    @Override
    protected void writeFieldAccessors(FieldReference fref, PrintStream ps) throws IOException 
    {
        String aname = makeJavaName(fref.getName());
        println(1, "// Get the value of the field: " + fref.getName());
        println(1, "public String get" + aname + "()");
        println(1, "{");
        writeGetterBody(fref, ps);
        println(1, "}");
        println(1, "// Set the value of the field: " + fref.getName());
        println(1, "public void set" + aname + "(String value)");
        println(1, "{");
        writeSetterBody(fref, ps);
        println(1, "}");
    }

    protected void writeGetterBody(FieldReference fref, PrintStream ps) 
    {
        println(2, "");
    }

    protected void writeSetterBody(FieldReference fref, PrintStream ps) 
    {
        println(2, "");
    }
    
}
