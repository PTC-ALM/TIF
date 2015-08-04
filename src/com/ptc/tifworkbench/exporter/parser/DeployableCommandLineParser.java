/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter.parser;

import com.ptc.tifworkbench.exporter.DeployableCommand;

/**
 *
 * @author schamaillard
 */
public interface DeployableCommandLineParser {
    
    public DeployableCommand parse(final String commandLine);
    
}
