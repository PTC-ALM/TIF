/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

/**
 *
 * @author pbowden
 */
public class ReaderUtils 
{
    public static String stripQuotes(String str)
    {
        return str.replaceAll("\"", "");
    }
}
