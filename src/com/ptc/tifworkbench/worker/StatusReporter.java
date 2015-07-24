/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.worker;

/**
 *
 * @author pbowden
 */
public interface StatusReporter 
{
    public static final String STATUS_PROP = "Status";
    public void reportStatus(int progress, String message);

    public static final String DETAIL_PROP = "Detail";
    public static final String FINISHED_PROP = "Finished";
    public void reportDetail(String message);
 
}
