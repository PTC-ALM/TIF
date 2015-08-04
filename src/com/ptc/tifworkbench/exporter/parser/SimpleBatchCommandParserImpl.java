/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter.parser;

import com.ptc.tifworkbench.exporter.BatchCommandImpl;
import com.ptc.tifworkbench.exporter.DeployableCommand;

/**
 *
 * @author schamaillard
 */
public class SimpleBatchCommandParserImpl implements DeployableCommandLineParser {

    public DeployableCommand parse(final String line) {
        DeployableCommand batchCommand = new BatchCommandImpl(line);
        return batchCommand;
    }
}
