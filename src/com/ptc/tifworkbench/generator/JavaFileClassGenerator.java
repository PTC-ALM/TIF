/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.generator;

import com.ptc.tifworkbench.jaxbbinding.FieldReference;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 *
 * @author pbowden
 */
public abstract class JavaFileClassGenerator extends AbstractClassGenerator
{
    private String pkg;
    private File dir;
    
    public JavaFileClassGenerator(ImSolution solution, String pkg, File dir)
    {
        super(solution);
        this.pkg = pkg;
        
        String pkgPath = dir.getAbsolutePath() + File.separator + pkg.replace('.', File.separatorChar);
        File pkgDir = new File(pkgPath);
        if(!pkgDir.exists())
            pkgDir.mkdirs();
        this.dir = pkgDir;
        
    }
    
    public String getPackageName()
    {
        return pkg;
    }
    public File getDirectory()
    {
        return dir;
    }
    
    @Override
    public String getDescription()
    {
        return "Generate simple wrapper classes.";
    }


    @Override
    public void doGenerate() throws Exception
    {
        report(0, "Generating classes");
        List<TypeDefinition> types = getSolution().getTypes().getType();
        int count = 0;
        for(TypeDefinition type : types)
        {
            String className = makeClassName(type.getName());
            report(count*100/types.size(), className);

            JavaClassWriter jcw = createClassWriter(className, type);
            jcw.writeClass();
            
            count++;
        }
        report(100, "Generated " + count + " classes");
    }
    
    protected JavaClassWriter createClassWriter(String className, TypeDefinition tdef) throws FileNotFoundException, IOException
    {
        return new BasicJavaClassWriter(pkg, className, tdef, dir);    
    }
    
    protected PrintStream createJavaFile(String className) throws FileNotFoundException, IOException
    {
        File fjava = new File(dir, className + ".java");
        fjava.createNewFile();
        FileOutputStream fos = new FileOutputStream(fjava);
        PrintStream ps = new PrintStream(fos);
        return ps;
    }
    
    
    
    protected String makeClassName(String tname)
    {
        return tname.replaceAll("\\s", "");
    }
    
}
