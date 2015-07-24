/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.generator;

import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.worker.StatusReporter;

/**
 *
 * @author pbowden
 */
public abstract class AbstractClassGenerator implements TifGeneratorInterface
{
    private ImSolution solution;
    private StatusReporter reporter;
    
    public AbstractClassGenerator(ImSolution solution)
    {
        this.solution=solution;
    }
    
    @Override
    public void setReporter(StatusReporter reporter)
    {
        this.reporter = reporter;
    }

    protected ImSolution getSolution()
    {
        return solution;
    }
    
    @Override
    abstract public String getDescription();
    @Override
    abstract public void doGenerate() throws Exception;

    protected void report(int prog, String mess)
    {
        if(reporter != null)
            reporter.reportStatus(prog, mess);
    }


}
