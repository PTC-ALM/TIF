/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import com.ptc.tifworkbench.TifWorkbenchApplication;
import com.ptc.tifworkbench.integrity.IntegrityConnection;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author pbowden
 */
public class TifEnvironment 
{
    public static final String BUILD_DOT_PROPS = "build.properties";
    private File envDir;
    private File propsFile;
    private PropertiesConfiguration props;
        
    public static final String SOL_PATH_PROP   = "dir.im.solution.path";

    public static final String NAME_PROP   = "project.name";
    public static final String PREFIX_PROP = "project.title";
    public static final String SERVER_PROP = "is.server.install";
    public static final String CLIENT_PROP = "is.client.install";
    
    public static final String HOST_PROP      = "is.server";
    public static final String PORT_PROP      = "is.port";
    public static final String IUSER_PROP     = "is.user";
    public static final String IPASSWORD_PROP = "is.password";

    public static final String DBNAME_PROP     = "sql.db.name";
    public static final String DBSERVER_PROP   = "sql.db.server";
    public static final String DBRESTORE_PROP  = "sql.db.restore.point";
    public static final String DBUSER_PROP     = "sql.user";
    public static final String DBPASSWORD_PROP = "sql.password";
    
    public static final String OS_PROP = "exec.current.os";
    
    
    /** Enum of TIF support files that may be updated with each release but
     *  are not expected to be user-modifiable. */
    public enum EnvStaticFiles {
        BuildXml("build.xml"), 
        ScriptGen("xsl/script-gen.xsl"), 
        PtGen("xsl/pt-gen.xsl"),
        DocIndex("xsl/doc-index.xsl"),
        DomainGen("xsl/mks-domain-gen.xsl"), 
        TifUtils("externalScripts/TifUtils.jar"),
        DbRestore("externalScripts/restoreDB.ksh"),
        CommonCli("externalScripts/commons-cli-1.2.jar"),
        CommonsIo("externalScripts/commons-io-2.4.jar"),
        CommonsLan("externalScripts/commons-lang.jar"),
        MksApi("externalScripts/mksapi.jar");
        
        private String fname;
        private EnvStaticFiles(String fname){this.fname=fname;}
        @Override
        public String toString() {return fname;}
        public String directory()
        {
            return fname.lastIndexOf("/")==-1 ? "" : fname.substring(0, fname.lastIndexOf("/"));
        }
    };
    
   
    
    public TifEnvironment(File propsFile) throws org.apache.commons.configuration.ConfigurationException
    {
        this.propsFile = propsFile;
        this.envDir = propsFile.getParentFile();
        try 
        {
            props = new PropertiesConfiguration(propsFile);
            props.setProperty(SOL_PATH_PROP, makePropPath(envDir));
        } 
        catch (ConfigurationException ex) {
            System.out.println("Could not open environment: " + propsFile.getAbsolutePath());
            ex.printStackTrace(System.out);
        }
        
    }

    /**
     * Create a new environment in the specified directory.
     * Check the directory does not exist.
     * Create the new directory
     * Unzip the tif environment template
     * 
     * @throws Exception 
     */
    public static TifEnvironment createEnvironment(File envDir) throws Exception
    {
        File propsFile = new File(envDir, BUILD_DOT_PROPS);
        
        if(propsFile.exists())
            throw new Exception("Cannot create a new environment in " + envDir.getAbsolutePath() +
                    " already contains a file: build.properties");
        if(!envDir.exists())
            if(!envDir.mkdirs())
                throw new Exception("Cannot create a new environment in " + envDir.getAbsolutePath() +
                        ". Cannot create directory.");
            
        String source = "resources/tif-template" + TifWorkbenchApplication.TIF_VERSION + ".zip";
        ZipFile zipFile;
        try 
        {
            zipFile = new ZipFile(source);
            zipFile.extractAll(envDir.getAbsolutePath());
            TifEnvironment newEnv = new TifEnvironment(propsFile);
            return newEnv;
        } catch (ZipException ex) 
        {
            throw new Exception("Could not unzip new environment using:" + source, ex);
        }
    }
    
    /**
     * Create an empty TIF file prefixed with the environment prefix.
     */
    public void createNewTif() throws Exception
    {
        ImSolution imsolution = TifUtils.createEmptyTif();
        File tifOut = new File(this.getEnvDir(), this.getPrefix() + "-solution.xml");
        TifUtils.writeTif(imsolution, tifOut);          
    }
    
    public void writeEnvironment() throws IOException
    {
        try 
        {
            props.save();
        } catch (ConfigurationException ex) {
            System.out.println("Could not open environment: " + propsFile.getAbsolutePath());
            ex.printStackTrace(System.out);
        }
    }
    
    /** Get the list of environment zip files in the resources directory.
     *  Guarantees date ordered, most recent first.
     */
    public static List<File> getEnvironmentZips()
    {
        File resourcesDir = new File(System.getProperty("user.dir") + "/resources");
        File [] resources = resourcesDir.listFiles(new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                String fname = pathname.getName();
                return fname.startsWith("tif-template") && fname.endsWith(".zip");
            }
        });
        List<File> ret = Arrays.asList(resources);
        Comparator<File> comparator = new Comparator<File>() {
            public int compare(File c1, File c2) {
                return (int)(c2.lastModified() - c1.lastModified());
            }
        };        
        Collections.sort(ret, comparator);
        return ret;
    }
    
    /**
     * Iterate over the list of environment files in selFiles and unzip them
     * into this environment.
     * @param selFiles
     * @param envZip
     * @throws Exception 
     */
    public void updateEnvironment(List<EnvStaticFiles>selFiles, File envZip) throws Exception
    {
        ZipFile zipFile;
        EnvStaticFiles procFile=null;
        try 
        {
            zipFile = new ZipFile(envZip);
            for(EnvStaticFiles fext : selFiles) // Must declare loop var in the for.
            {
                procFile = fext;
                zipFile.extractFile(fext.toString(), envDir.getAbsolutePath());
            }
        } catch (ZipException ex) 
        {
            String mess  = "Error unzipping environment:\n" + ex.getMessage();
            if(procFile != null)
                mess = "Error unzipping file " + procFile.toString() + "\n" + ex.getMessage();
            throw new Exception(mess, ex);
        }
    }
    
    /**
     * @return the envDir
     */
    public File getEnvDir() 
    {
        return envDir;
    }

    /**
     * @return the envDir
     */
    public File getPropertiesFile() 
    {
        return propsFile;
    }

    /**
     * @return the name
     */
    public String getName() 
    {
        return props.getString(NAME_PROP);
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) 
    {
       props.setProperty(NAME_PROP, name);
    }

    /**
     * @return the prefix
     */
    public String getPrefix() 
    {
        return props.getString(PREFIX_PROP);
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) 
    {
        props.setProperty(PREFIX_PROP, prefix);
    }

    /**
     * @return the serverDir
     */
    public File getServerDir() 
    {
        return new File(props.getString(SERVER_PROP));
    }

    /**
     * @param serverDir the serverDir to set
     */
    public void setServerDir(File serverDir) 
    {
        props.setProperty(SERVER_PROP, makePropPath(serverDir));
    }

    /**
     * @return the clientDir
     */
    public File getClientDir() 
    {
        return new File(props.getString(CLIENT_PROP));
    }

    /**
     * @param clientDir the clientDir to set
     */
    public void setClientDir(File clientDir) 
    {
        props.setProperty(CLIENT_PROP, makePropPath(clientDir));
    }

    /**
     * @return the clientDir
     */
    public String getOS() 
    {
        return props.getString(OS_PROP);
    }

    /**
     * @param clientDir the clientDir to set
     */
    public void setOS(String os) 
    {
        props.setProperty(OS_PROP, os);
    }

    /**
     * @return the conn
     */
    public IntegrityConnection getConn() 
    {
        IntegrityConnection conn = 
                new IntegrityConnection(props.getString(HOST_PROP),
                                        props.getInt(PORT_PROP),
                                        props.getString(IUSER_PROP),
                                        props.getString(IPASSWORD_PROP));
        return conn;
    }
    
    /**
     * @param conn the conn to set
     */
    public void setConn(IntegrityConnection conn) 
    {
        props.setProperty(HOST_PROP, conn.getHost());
        props.setProperty(PORT_PROP, new Integer(conn.getPort()).toString());
        props.setProperty(IUSER_PROP, conn.getUser());
        props.setProperty(IPASSWORD_PROP, conn.getPassword());
    }

       /**
     * @return the dbConn
     */
    public DatabaseConnection getDbConn() 
    {
        DatabaseConnection conn = new DatabaseConnection(
                props.getString(DBNAME_PROP),
                props.getString(DBSERVER_PROP),
                props.getString(DBRESTORE_PROP),
                props.getString(DBUSER_PROP),
                props.getString(DBPASSWORD_PROP)
                );
        return conn;
    }

    /**
     * @param dbConn the dbConn to set
     */
    public void setDbConn(DatabaseConnection conn) {
        props.setProperty(DBNAME_PROP, conn.getDbName());
        props.setProperty(DBSERVER_PROP, conn.getDbServer());
        props.setProperty(DBRESTORE_PROP, conn.getRestorePoint());
        props.setProperty(DBUSER_PROP, conn.getDbUser());
        props.setProperty(DBPASSWORD_PROP, conn.getDbPassword());
    }

    private String makePropPath(File path)
    {
        String str = path.getAbsolutePath();
        return str.replace("\\", "/");
    }
    @Override
    public String toString()
    {
        return "Name: " + this.getName() + "\n" +
               "Prefix: " + this.getPrefix() + "\n" + 
               "Directory:\n" + this.getEnvDir().getAbsolutePath();
    }


}
