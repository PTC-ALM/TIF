/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.ptc.tifworkbench.integrity.IntegrityApi;
import com.ptc.tifworkbench.integrity.IntegrityConnection;
import com.ptc.tifworkbench.integrity.IntegrityException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pbowden
 */
public class RelationshipFieldReader 
{
    private ArrayList<String> baseArgs = null;
    private String cliDir = null;
    private boolean multi = false;
    private String defaultQuery="";
    private Integer displayRows = new Integer(10);
    private String defaultCols = "";

    private List<String>lines;

    public RelationshipFieldReader(String fieldName, IntegrityApi api) throws IntegrityException
    {
        IntegrityConnection conn = api.getDefaultConnection();

        baseArgs = new ArrayList<String>();
        baseArgs.add("im");
        baseArgs.add("viewfield");
        baseArgs.add("--hostname=" + conn.getHost());
        baseArgs.add("--port=" + conn.getPort());
        baseArgs.add("--user=" + conn.getUser());
        baseArgs.add("--password=" + conn.getPassword());
        baseArgs.add("--batch");

        File imexe = findExecutableOnPath("im.exe");
        cliDir = new String(imexe.getParent());

        getRelationshipProperties(fieldName);
    }

    /**
     * Get the misc relationship properties: multivalued, default browse query, 
     * @param field
     */
    private void getRelationshipProperties(String field)
    {
        String[] toks = null;
        lines = null;
        String text = null;
        ArrayList<String> args = new ArrayList<String>(baseArgs);

        args.add(field);
        text = slurp(args, cliDir);
        lines = new ArrayList<String>(Arrays.asList(text.split("\n")));

        for (int i=0; i<lines.size(); i++) 
        {
            String s=lines.get(i); // Just makes look-ahead easier.
            if (s.toLowerCase().startsWith("is multi-valued"))
            {
                    toks = s.split(": ");
                    multi = toks[1].trim().equals("true");
            }
            if (s.toLowerCase().startsWith("default browse query"))
            {
                    toks = s.split(": ");
                    defaultQuery = toks[1].trim();
            }
            if (s.toLowerCase().startsWith("display rows"))
            {
                    toks = s.split(": ");
                    displayRows = Integer.parseInt(toks[1].trim());
            }
            if (s.toLowerCase().startsWith("default columns"))
            {
                    toks = s.split(": ");
                    defaultCols = lines.get(i+1).trim(); // this is safe.
            }
        }
    }

    public Integer getDisplayRows()
    {
        return displayRows;
    }
    public boolean getMultiValued()
    {
        return multi;
    }

    public String getDefaultQuery()
    {
        return defaultQuery;
    }
    public String getDefaultColumns()
    {
        return defaultCols;
    }

    public String getPairedField() 
    {
        String[] toks = null;
        String result = new String();
        for (String s : lines) 
        {
                if (s.toLowerCase().startsWith("paired field")){
                        toks = s.split(": ");
                        result = toks[1].trim();
                        break;
                }
        }
        return result;
    }

    public String getAllowedTypes()
    {
        String[] toks = null;
        String theline = null;
        String result = new String();
        ArrayList<String> types = new ArrayList<String>();

        Iterator<String>i = lines.iterator();
        while (i.hasNext()) {
                theline = i.next();
                if (theline.toLowerCase().startsWith("allowed types")) {
                        //System.out.println("** theline: |" + theline + "|"); // XXX: DEBUG
                        if (theline.equalsIgnoreCase("allowed types: ")) { // 2009 behaviour --al 20100119
                                theline = i.next(); // skip to next line if there's nothing.
                                while (theline.startsWith("\t")) {
                                        types.add(theline.substring(1).trim()); // add the line and strip the tab
                                        theline = i.next();
                                }
                                break;
                        } else {
                                toks = theline.split(": ");
                                if (toks.length > 1) {
                                        types.add(toks[1].trim());
                                        if (i.hasNext()) {
                                                theline = i.next();
                                                while (theline.startsWith("\t")) {
                                                        types.add(theline.trim());
                                                        theline = i.next();
                                                }
                                                break;
                                        }
                                }	
                        }
                }
        }
        i = types.iterator();
        while (i.hasNext()) {
                result = result.concat(i.next());
                if (i.hasNext()) {
                        result = result.concat(";");
                }
        }
        return result.trim();
    }

    private String slurp(ArrayList<String> command, String cwd) 
    {
        ProcessBuilder pb = null;
        Process p = null;
        BufferedReader br = null;
        String out = new String();
        String output = new String();

        pb = new ProcessBuilder(command);
        pb.directory(new File(cwd));
        pb.redirectErrorStream(true);
        try {
                p = pb.start();
        } catch (IOException ioe) {
                System.err.println("*  RelationshipFieldReader.slurp(): IOException when starting process: " + ioe.getMessage());
                return output;
        }
        br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
                while ((out = br.readLine()) != null) {
                        output = output.concat(out + "\n");
                }
        } catch (IOException ioe) {
                System.err.println("*  RelationshipFieldReader.slurp(): IOException while reading output: " + ioe.getMessage());
                return output;
        }
        try {
                br.close();
        } catch (IOException ioe) {
                System.err.println("*  RelationshipFieldReader.slurp(): IOException when closing BufferedReader: " + ioe.getMessage());
        }
        return output;
    }
    
    private static File findExecutableOnPath(String executableName)  
    {  
        String systemPath = System.getenv("PATH");  
        String[] pathDirs = systemPath.split(File.pathSeparator);  
   
        File fullyQualifiedExecutable = null;  
        for (String pathDir : pathDirs)  
        {  
            File file = new File(pathDir, executableName);  
            if (file.isFile())  
            {  
                fullyQualifiedExecutable = file;  
                break;  
            }  
        }  
        return fullyQualifiedExecutable;  
    }  
    
}
