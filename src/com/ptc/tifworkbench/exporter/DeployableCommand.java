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
public interface DeployableCommand {
    
    /***
     * Execute the deployable command against the target system.
     * @throws DeployableCommandException 
     */
    public void execute() throws DeployableCommandException;
    
}
