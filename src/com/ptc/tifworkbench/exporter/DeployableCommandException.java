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
public class DeployableCommandException extends Exception {
    
    public DeployableCommandException(final Exception e){
        super(e);
    }
    
    public DeployableCommandException(final String message){
        super(message);
    }
    
}
