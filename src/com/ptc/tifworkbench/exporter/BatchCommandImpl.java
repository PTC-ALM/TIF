/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.exporter;

/**
 *
 * @author schamaillard
 */
public class BatchCommandImpl implements DeployableCommand {

    protected final String batchCommandLine;

    public BatchCommandImpl(final String batchCommandLine) {
        this.batchCommandLine = batchCommandLine;
    }

    @Override
    public void execute() throws DeployableCommandException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
