/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schamaillard
 */
public class DeployableCommandLineParserFactory {

    private final DeployableCommandLineParser integrityCommandLineParser;
    
    private final DeployableCommandLineParser batchCommandLineParser;

    public DeployableCommandLineParserFactory() {
        this.integrityCommandLineParser = new SimpleIntegrityCommandParserImpl();
        this.batchCommandLineParser = new SimpleBatchCommandParserImpl();
    }

    protected static List<String> getIntegrityCommandPrefixes() {
        List<String> prefixes = new ArrayList<String>();
        prefixes.add("aa");
        prefixes.add("im");
        prefixes.add("integrity");
        prefixes.add("si");
        prefixes.add("rq");
        return prefixes;
    }

    protected boolean isAnIntegrityCLI(final String scriptLineToParse) {
        for(String integrityCommandPrefix : getIntegrityCommandPrefixes()){
            if(scriptLineToParse.startsWith(integrityCommandPrefix + " "))
                return true;
        }
        return false;
    }

    public DeployableCommandLineParser getParser(final String scriptLineToParse) {
        if(this.isAnIntegrityCLI(scriptLineToParse)){
            return this.integrityCommandLineParser;
        }
        else{
            return this.batchCommandLineParser;
        }
    }
}
