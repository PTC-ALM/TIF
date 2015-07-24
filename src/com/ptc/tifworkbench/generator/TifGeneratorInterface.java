/*
 * A generator class that creates wrappers from TIF should implement this interface.
 * The generator class will automatically run on a separate worker thread created by the 
 * generator dialogue.
 */
package com.ptc.tifworkbench.generator;

import com.ptc.tifworkbench.worker.StatusReporter;

/**
 *
 * @author pbowden
 */
public interface TifGeneratorInterface 
{
    public void doGenerate() throws Exception;
    public void setReporter(StatusReporter reporter);

    public String getDescription();
    
}
