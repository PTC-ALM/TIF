/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.worker;

import com.ptc.tifworkbench.generator.TifGeneratorInterface;
import javax.swing.SwingWorker;

/**
 *
 * @author pbowden
 */
public class ClassGenWorker extends SwingWorker<Integer, Status> implements StatusReporter
{
    private Status currStatus;
    private TifGeneratorInterface gen;
    
    public ClassGenWorker(TifGeneratorInterface classgen)
    {
        currStatus = new Status(0, "Generating classes");
        this.gen = classgen;
    }
    @Override
    protected void done()
    {
    }
    
    @Override
    protected Integer doInBackground() throws Exception 
    {
        try
        {
            gen.doGenerate();
         }catch(Exception ex)
        {
        	publish(new Status(0, "Error: " + ex.getMessage()));
        	return new Integer(-1);
        }
        return 1;
    }

    @Override
    public void reportStatus(int progress, String message) 
    {
        Status stat = new Status(progress, message);
        this.firePropertyChange(StatusReporter.STATUS_PROP, currStatus, stat);
        currStatus = stat;
    }
    @Override
    public void reportDetail(String message) 
    {
        this.firePropertyChange(StatusReporter.DETAIL_PROP, message, message);
    }
    
}
