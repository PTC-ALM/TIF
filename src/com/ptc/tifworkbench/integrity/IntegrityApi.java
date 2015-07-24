/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.integrity;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Option;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.mks.api.util.MKSLogger;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pbowden
 */
public class IntegrityApi 
{
    private IntegrationPointFactory ipf = IntegrationPointFactory.getInstance();
    private CmdRunner _cmdRunner;
    private MKSLogger _log; 
    private File logFile = null;
    
    private static IntegrityApi _instance = null;
    
	public static IntegrityApi getInstance()throws IntegrityExceptionEx
    {
        if(_instance==null)
        {
        	_instance = new IntegrityApi();
        }
        return _instance;
    }
    private IntegrityApi()throws IntegrityExceptionEx
    {
        logFile = new File("tifworkbench.log");
        _log = new MKSLogger(logFile);
        _log.configure(MKSLogger.GENERAL, MKSLogger.MESSAGE | MKSLogger.EXCEPTION, 10);
        _log.configure(MKSLogger.ERROR, MKSLogger.MESSAGE | MKSLogger.EXCEPTION, 10);
        
        IntegrationPoint intPt;
        try {
            intPt = ipf.createLocalIntegrationPoint(4,10);
            _log.message("Created logger and local integration point.");
            intPt.setAutoStartIntegrityClient(true);
            Session sess = intPt.getCommonSession();
            _cmdRunner = sess.createCmdRunner();
            _log.message("Got the command runner.");
        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error creating API connection", ex);
        }
    }

    public void clientConnect() throws IntegrityExceptionEx
    {
        clientConnect(null, -1, null);
    }
    public void clientConnect(String hostname, int port) throws IntegrityExceptionEx
    {
        clientConnect(hostname, port, null);
    }

    
    public void cancelCommand() throws IntegrityExceptionEx
    {
        try 
        {
            _log.message("Interrupting command ");
            _cmdRunner.interrupt();
        } catch (APIException apiEx) 
        {
            _log.message("Exception: " + apiEx.getMessage());
            throw IntegrityExceptionEx.create("Error interrupting a command.", apiEx);
        }
    }
    public void clientConnect(String host, int port, String user) throws IntegrityExceptionEx
    {
        _log.message("Connecting via the client.");
        try
        {
            // We need to null the default connection to force the client to 
            // give us the gui to pick a connection.
            unsetRunnerDefaultConnection();
            Command cmd = new Command(Command.IM, "connect");
            cmd.addOption(new Option("gui"));
            cmd.addOption(new Option("status", "gui"));
            if (host != null)
                cmd.addOption(new Option("hostname", host));
            if (port != -1)
                cmd.addOption(new Option("port", ""+port));
            if (user != null)
                cmd.addOption(new Option("user", user));

            _log.message("Running connect command.");
            Response resp = _cmdRunner.execute(cmd);
            _log.message("Command executed.");
            IntegrityConnection conn = getDefaultConnection();
            setRunnerDefaultFromConnection(conn);
        }
        catch (com.mks.api.response.APIException apiEx)
        {
            _log.message("Exception: " + apiEx.getMessage());
            throw IntegrityExceptionEx.create("Error connecting to Integrity server via a client integration point.", apiEx);
        }
        _log.message("Connected to " + host + ":" + port);
    }
    
    public void clientConnectSilent(String host, int port, String user, String password) throws IntegrityExceptionEx
    {
        _log.message("Connecting via the client.");
        try
        {
            // We need to null the default connection to force the client to 
            // give us the gui to pick a connection.
            unsetRunnerDefaultConnection();
            Command cmd = new Command(Command.IM, "connect");
            cmd.addOption(new Option("gui"));
            if (host != null)
                cmd.addOption(new Option("hostname", host));
            if (port != -1)
                cmd.addOption(new Option("port", ""+port));
            if (user != null)
                cmd.addOption(new Option("user", user));
            if (password != null)
                cmd.addOption(new Option("password", password));

            _log.message("Running connect command.");
            Response resp = _cmdRunner.execute(cmd);
            _log.message("Command executed.");
            IntegrityConnection conn = getDefaultConnection();
            setRunnerDefaultFromConnection(conn);
        }
        catch (com.mks.api.response.APIException apiEx)
        {
            _log.message("Exception: " + apiEx.getMessage());
            throw IntegrityExceptionEx.create("Error connecting to Integrity server via a client integration point.", apiEx);
        }
        _log.message("Connected to " + host + ":" + port);
    }

    public void unsetRunnerDefaultConnection()
    {
        _cmdRunner.setDefaultHostname(null);
        _cmdRunner.setDefaultPort(-1);
        _cmdRunner.setDefaultUsername(null);
        _cmdRunner.setDefaultPassword(null);
    }
    
    public File getLogFile()
    {
        return logFile;
    }
        /**
     * Get the current default connection from the Integrity client.
     */
    public IntegrityConnection getDefaultConnection() throws IntegrityExceptionEx
    {
        try 
        {
            Command cmd = new Command(Command.IM, "servers");
            Response resp = _cmdRunner.execute(cmd);
            if (resp.getWorkItemListSize() > 0) // Connected to something.
            {
                WorkItemIterator wkit = resp.getWorkItems();
                boolean found = false;
                while (wkit.hasNext() && !found)
                {
                    WorkItem wk = wkit.next();
                    if (wk.getField("default").getBoolean().booleanValue())
                    {
                        String h = wk.getField("hostname").getString();
                        int p = wk.getField("portnumber").getInteger().intValue();
                        String u = wk.getField("username").getString();
                        _log.message("Found default connection " + u + "@" + h + ":" + p);
                        return new IntegrityConnection(h, p, u);
                    }
                }
            }
            return null;
        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Could not get the default api connection", ex);
        }
    }


    /**
     * Set the command runners defaults to those found in the connection.
     * The connection is typically derived from a call to getDefaultConnection.
     */
    public void setRunnerDefaultFromConnection(IntegrityConnection conn)
    {
        _log.message("Setting command runner defaults " + conn.toString());
        _cmdRunner.setDefaultHostname(conn.getHost());
        _cmdRunner.setDefaultPort(conn.getPort());
        _cmdRunner.setDefaultUsername(conn.getUser());
    }


    public Response execute(Command cmd)throws APIException
    {
	return _cmdRunner.execute(cmd);
    }
    public void log(String mess)
    {
    	_log.message(mess);
    }
    
}
