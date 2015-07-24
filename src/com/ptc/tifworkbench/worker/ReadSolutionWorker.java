/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.worker;

import com.ptc.tifworkbench.integrity.IntegrityApi;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import com.ptc.tifworkbench.model.SolutionViewer;
import com.ptc.tifworkbench.reader.SolutionReader;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author pbowden
 * TODO: tidy up the Integer return type. Can we make use of this rather than the done() 
 * method.
 */
public class ReadSolutionWorker extends SwingWorker<Integer, Status> implements StatusReporter
{
    private Status currStatus;
    private SolutionReader reader;
    private SolutionViewer viewer;
    private JDialog progressDlg;
    
    public ReadSolutionWorker(SolutionReader reader, SolutionViewer viewer, JDialog progressDlg)
    {
        currStatus = new Status(0, "Reading solution");  
        this.reader = reader;
        this.viewer = viewer;
        this.progressDlg = progressDlg;
    }
    
    
    @Override
    protected void done()
    {
        try
        {
            viewer.setSolution(reader.getSolution());
            progressDlg.setVisible(false);
            progressDlg.dispose();
            get();
        }catch (ExecutionException e) 
        {
            String msg = String.format("<html><body><p style='width: 300px;'> %s </body></html>", 
                    e.getCause().getMessage());
            JOptionPane.showMessageDialog(null,
                msg, "Error reading solution", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) 
        {
            System.out.println("Interrupted");
        } catch (CancellationException e) 
        {
            String msg = String.format("Read cancelled...");
            JOptionPane.showMessageDialog(null,
                msg, "Reading solution", JOptionPane.INFORMATION_MESSAGE);
            try {
                IntegrityApi.getInstance().cancelCommand();
            } catch (IntegrityExceptionEx ex) {
                System.out.println("Could not interrupt running Integrity command. " + 
                        ex.getMessage());
            }
        }
    }
    
    @Override
    protected Integer doInBackground() throws Exception 
    {
        reader.readSolution();
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
