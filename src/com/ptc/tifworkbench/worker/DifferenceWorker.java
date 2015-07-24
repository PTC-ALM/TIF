/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.worker;

import com.ptc.tifworkbench.model.SolutionDifferencer;
import com.ptc.tifworkbench.model.SolutionViewer;
import javax.swing.SwingWorker;

/**
 *
 * @author pbowden
 */
public class DifferenceWorker extends SwingWorker<Integer, Status> implements StatusReporter
{
    private Status currStatus;
    private SolutionDifferencer diff;
    private SolutionViewer viewer;
    
    public DifferenceWorker(SolutionDifferencer diff, SolutionViewer viewer)
    {
        currStatus = new Status(0, "Differencing solutions.");
        this.diff=diff;
        this.viewer=viewer;
    }
    
    @Override
    protected void done()
    {
        viewer.setSolution(diff.getDifferenceSolution()); 
        this.firePropertyChange(StatusReporter.FINISHED_PROP, "Finished", "Done");

    }
    
    @Override
    protected Integer doInBackground() throws Exception 
    {
        try
        {
            diff.doDifference();
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
        this.firePropertyChange(StatusReporter.DETAIL_PROP, "", message);
    }
}
