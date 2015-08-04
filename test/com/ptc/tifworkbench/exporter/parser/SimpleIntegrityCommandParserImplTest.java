/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter.parser;

import com.ptc.tifworkbench.exporter.IntegrityCommandImpl;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author schamaillard
 */
public class SimpleIntegrityCommandParserImplTest {

    protected DeployableCommandLineParserFactory parserFactory;

    @Before
    public void setUp() throws Exception {
        this.parserFactory = new DeployableCommandLineParserFactory();
    }
    
    protected SimpleIntegrityCommandParserImpl getParser(final String commandLine){
        return (SimpleIntegrityCommandParserImpl) this.parserFactory.getParser(commandLine);
    }
    
    protected IntegrityCommandImpl getIntegrityCommand(final String commandLine){
        return (IntegrityCommandImpl) this.getParser(commandLine).parse(commandLine);
    }

    @Test
    public void testCreateCommandLine() {
        String line = "im createstate --hostname=localhost --port=7001 --user=administrator --password=password --name=Done";
        IntegrityCommandImpl command = this.getIntegrityCommand(line);
        String prefix = "im";
        String commandName = "createstate";
        Map<String,String> options = new HashMap<String,String>();
        options.put("hostname", "localhost");
        options.put("port", "7001");
        options.put("user", "administrator");
        options.put("password", "password");
        options.put("name", "Done");
        String selection = null;
        boolean success = command.isEqualsToCommandLine(prefix, commandName,
                options, selection);
        Assert.assertTrue(success);
    }

    @Test
    public void testEditCommandLine() {
        String line = "im editfield --hostname=localhost --port=7001 --user=administrator --password=password --displayName=\"Shared Test Method\" \"Shared Test Method\"";
        IntegrityCommandImpl command = this.getIntegrityCommand(line);
        String prefix = "im";
        String commandName = "editfield";
        Map options = new HashMap();
        options.put("hostname", "localhost");
        options.put("port", "7001");
        options.put("user", "administrator");
        options.put("password", "password");
        options.put("displayName", "Shared Test Method");
        String selection = "Shared Test Method";
        boolean success = command.isEqualsToCommandLine(prefix, commandName,
                options, selection);
        Assert.assertTrue(success);
    }

    @Test
    public void testTestAddConstraintCommandLine() {
        String line = "im edittype --hostname=localhost --port=7001 --user=administrator --password=password --addFieldRelationship=constraintrule=\"(((field[\\\"State\\\"] != \\\"\\\") and (field[\\\"State\\\"] != \\\"Draft\\\")) and ((field[\\\"Artifact Category\\\"] != \\\"Heading\\\") and (field[\\\"Artifact Category\\\"] != \\\"Comment\\\"))):Cyber-Security=:all,mandatory,description=Cyber-Security is mandatory for meaningful from proposed\" \"System Requirement\"";
        IntegrityCommandImpl command = this.getIntegrityCommand(line);
        String prefix = "im";
        String commandName = "edittype";
        Map options = new HashMap();
        options.put("hostname", "localhost");
        options.put("port", "7001");
        options.put("user", "administrator");
        options.put("password", "password");
        options.put(
                "addFieldRelationship",
                "constraintrule=\"(((field[\\\"State\\\"] != \\\"\\\") and (field[\\\"State\\\"] != \\\"Draft\\\")) and ((field[\\\"Artifact Category\\\"] != \\\"Heading\\\") and (field[\\\"Artifact Category\\\"] != \\\"Comment\\\"))):Cyber-Security=:all,mandatory,description=Cyber-Security is mandatory for meaningful from proposed\"");
        String selection = "System Requirement";
        boolean success = command.isEqualsToCommandLine(prefix, commandName,
                options, selection);
        Assert.assertTrue(success);
    }

}
