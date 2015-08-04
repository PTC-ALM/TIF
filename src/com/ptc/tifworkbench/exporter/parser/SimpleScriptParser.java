/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter.parser;

import com.ptc.tifworkbench.exporter.DeployableCommand;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schamaillard
 */
public class SimpleScriptParser {

    protected final File script;
    
    protected final DeployableCommandLineParserFactory commandLineParserFactory = new DeployableCommandLineParserFactory();

    public SimpleScriptParser(final File script) {
        this.script = script;
    }

    protected List<String> readScriptLines() throws Exception {
        List<String> scriptLines = new ArrayList<String>();
        FileReader fr = new FileReader(script);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            scriptLines.add(line);
        }
        br.close();
        fr.close();
        return scriptLines;
    }

    protected boolean isACommentLine(final String scriptLine) {
        // As batch...
        return scriptLine.startsWith("::") || scriptLine.startsWith("rem")
                || scriptLine.isEmpty();
    }

    protected DeployableCommand parseIntegrityCommand(final String scriptLine) {
        DeployableCommandLineParser parser = commandLineParserFactory
                .getParser(scriptLine);
        return parser.parse(scriptLine);
    }

    public List parse() throws Exception {
        List<DeployableCommand> parsedCommands = new ArrayList<DeployableCommand>();
        List<String> scriptLines = readScriptLines();
        for(String scriptLine : scriptLines){
            if (!isACommentLine(scriptLine)) {
                parsedCommands.add(parseIntegrityCommand(scriptLine));
            }
        }
        return parsedCommands;
    }

}
