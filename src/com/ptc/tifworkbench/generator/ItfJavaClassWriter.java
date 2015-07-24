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
public class ItfJavaClassWriter extends BasicJavaClassWriter 
{

    public ItfJavaClassWriter(String packageName, String className, TypeDefinition tdef, File directory)
            throws FileNotFoundException, IOException 
    {
        super(packageName, className, tdef, directory);
    }
    
    @Override
    protected void writeClassMembers(TypeDefinition tdef, String className, PrintStream ps) throws IOException 
    {
        println(1, "// ITF type.");
        println(1, "private Type itfType=null;");
    }

    @Override
    protected void writeConstructor(TypeDefinition tdef, String className, PrintStream ps) throws IOException 
    {
        println(1, "public " + className + "()");
        println(1, "{");
        println(2, "itfType = new Type(true, \"" + tdef.getName() + "\");");
        println(1, "}");
    }
    
    @Override
    protected void writeGetterBody(FieldReference fref, PrintStream ps) 
    {
        println(2, "Item.CreateParams params = new Item.CreateParams(itfType);");
    }

    @Override
    protected void writeSetterBody(FieldReference fref, PrintStream ps) 
    {
        println(2, "Item.CreateParams params = new Item.CreateParams(itfType);");
        println(2, "params.setFieldAndValue( new ShortTextField(false,\"Summary\"), new ShortTextFieldValue( value ) );");
        println(2, "Item newItem = imOpsFactory.getItemOps().create(params);");
    }
    
    
}
