/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.generator;

import com.ptc.tifworkbench.jaxbbinding.FieldReference;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.CharBuffer;

/**
 *
 * @author pbowden
 */
public abstract class AbstractClassWriter implements JavaClassWriter
{
    private String indentStr = "  ";
    private PrintStream ps;
    private TypeDefinition tdef;
    private String className;
    private String packageName;
    
    public AbstractClassWriter(String packageName, String className, TypeDefinition tdef, File dir) throws FileNotFoundException, IOException
    {
        File fjava = new File(dir, className + ".java");
        fjava.createNewFile();
        FileOutputStream fos = new FileOutputStream(fjava);
        ps = new PrintStream(fos);
        this.tdef=tdef;
        this.className = className;
        this.packageName = packageName;
    }
    
    @Override
    public void writeClass() throws Exception
    {
        writeClassHeader(tdef, className, ps);
        writeClassDeclaration(tdef, className, ps);
        writeClassMembers(tdef, className, ps);
        writeConstructor(tdef, className, ps);
        
        for(FieldReference fref : tdef.getFields().getField())
        {
            writeFieldAccessors(fref, ps);    
        }
        writeClassEnd(tdef, className, ps);
        ps.close();
    }

    public String getIndentStr()
    {
        return indentStr;
    }
    public void setIndentStr(String indentStr)
    {
        this.indentStr = indentStr;
    }
    
    public String getPackageName()
    {
        return packageName;
    }
    
    abstract protected void writeClassHeader(TypeDefinition tdef, String className, PrintStream ps) throws IOException;
    abstract protected void writeClassDeclaration(TypeDefinition tdef, String className, PrintStream ps) throws IOException;
    abstract protected void writeClassMembers(TypeDefinition tdef, String className, PrintStream ps) throws IOException;
    abstract protected void writeConstructor(TypeDefinition tdef, String className, PrintStream ps) throws IOException;
    abstract protected void writeFieldAccessors(FieldReference fref, PrintStream ps) throws IOException;

    protected void writeClassEnd(TypeDefinition tdef, String className, PrintStream ps) throws IOException
    {
        println(0, "}");
    }
    
    protected void println(String str)
    {
        ps.println(str);
    }

    protected void println(int indent, String str)
    {
        ps.println(getIndentStr(indent) + str);
    }
    protected String makeJavaName(String fname)
    {
        return fname.replaceAll("\\s", "");
    }
    
    protected String getIndentStr(int indent)
    {
        if(indent <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<indent; i++)
            sb.append(getIndentStr());
        return sb.toString();
    }
    
}
