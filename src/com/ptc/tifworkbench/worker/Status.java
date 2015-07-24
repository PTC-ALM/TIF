/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.worker;

/**
 *
 * @author pbowden
 */
public class Status 
{
    private int _progress = 0;
    private String _mess = "";
    
    public Status(int progress, String mess)
    {
        _progress = progress;
        _mess = mess;
    }

    public void setProgress(int progress)
    {
            _progress = progress;
    }
    public void setMessage(String mess)
    {
            _mess = mess;
    }

    public int getProgress()
    {
            return _progress;
    }
    public String getMessage()
    {
            return _mess;
    }
    
}
