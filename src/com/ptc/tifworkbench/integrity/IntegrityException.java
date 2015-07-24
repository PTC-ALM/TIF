/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.integrity;

import com.mks.api.response.Response;

/**
 *
 * @author pbowden
 */
public class IntegrityException extends Exception 
{

    public IntegrityException(final String message)
    {
        super(message);
    }

    public IntegrityException(final String message, final Throwable cause)
    {
         super(message, cause);
    }

    public IntegrityException(final Response response, final String message)
    {
        super(createMessage(response, message));
    }

    private static String createMessage(final Response response, final String message)
    {
        if (response == null)
        {
                //logger.warn("The passed-in response object was null.");
                return message;
        }

        if (response.getCommandString() == null)
        {
                //logger.warn("The passed-in response object " + response + " returned a null command string.");
                return message;
        }

        return message + " (offending command: " + response.getCommandName() + ")";
    }

    public String getAppendableMessage()
    {
        return getAppendableMessage(".", ". ");
    }

    public String getAppendableMessage(final String resultIfNullOrEmpty, final String prefixIfNotNull)
    {
        if (getMessage() == null || (getMessage().length()==0))
                return resultIfNullOrEmpty;
        else
                return prefixIfNotNull + getMessage();
    }

}

