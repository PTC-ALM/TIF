/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.integrity;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItemIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 *
 * @author pbowden
 */
public class IntegrityExceptionEx  extends IntegrityException 
{
    //private final static Logger logger = LoggerFactory.getLogger(MksApiExceptionEx.class);

    IntegrityExceptionEx(final String message, final String command, final int exitCode, final APIException cause, final String wrappedMessage)
    {
            super(message, cause);

            this.command = command;
            this.exitCode = exitCode;
            this.wrappedMessage = wrappedMessage;
    }

    public IntegrityExceptionEx(final String message, final String command, final int exitCode, final InterruptedException cause) 
    {
        super(message, cause);

        this.command = command;
        this.exitCode = exitCode;
        this.wrappedMessage = "No wrapped message";
    }

    private final String wrappedMessage;

    public String getWrappedMessage()
    {
            return wrappedMessage;
    }

    @Override
    public String getAppendableMessage()
    {
            return getAppendableMessage(".", ". ");
    }

    @Override
    public String getAppendableMessage(final String resultIfNullOrEmpty, final String prefixIfNotNull)
    {
        if (wrappedMessage == null || (wrappedMessage.length()==0))
                return resultIfNullOrEmpty;
        else
                return prefixIfNotNull + wrappedMessage;
    }

    public final static IntegrityExceptionEx create(final String prefix, final APIException apiException)
    {
        final InterpretedApiException augmented = interpretApiException(apiException);

        return new IntegrityExceptionEx(prefix + ' ' + augmented.message + " (command=" + augmented.command + ", exitCode=" + augmented.exitCode + ").", augmented.command, augmented.exitCode, apiException, augmented.message);
    }

    protected static final class InterpretedApiException
    {
            public InterpretedApiException(final APIException cause, final String message, final String command, final int exitCode)
            {
                    this.cause = cause;
                    this.message = message;
                    this.command = command;
                    this.exitCode = exitCode;
            }

            public final APIException cause;

            public final String message;

            public final String command;

            public int exitCode;

            @Override
            public String toString()
            {
                    return ReflectionToStringBuilder.toString(this);
            }
    }

    protected static InterpretedApiException interpretApiException(final APIException apiException)
    {
        /*
         * API Exceptions can be nested. Hence we will need to recurse the
         * exception hierarchy to dig for a conclusive message
         */
        final Response response = apiException.getResponse();

        /*
         * The following three variables will be filled in by the code below.
         */
        final String command;
        String message;
        int exitCode;

        if (response == null)
        {
                message = apiException.getMessage();

                /*
                 * If there is no response object, so we can't get the offending
                 * command.
                 */
                command = null;

                /*
                 * No way to get the exit code without a response (except, maybe the
                 * number prepended to the message and separated from it with a hash
                 * character is the return code).
                 */
                exitCode = -1;
        }
        else
        {
                /*
                 * If we get here, then there was a non-null response.
                 */

                command = response.getCommandString();

                try 
                {
                    exitCode = response.getExitCode();
                } catch (com.mks.api.response.InterruptedException ex) 
                {
                        //logger.warn("Could not retrieve the exit code for response '{}'.", response, e);
                        exitCode = -1;
                }

                /*
                 * In the event that there is a problem with one of the command's
                 * elements we have to dig deeper into the exception...
                 */

                final WorkItemIterator wit = response.getWorkItems();

                try
                {
                        while (wit.hasNext())
                                wit.next();

                        message = apiException.getMessage();
                }
                catch (final APIException e)
                {
                        //logger.debug("Accessing a work item in the exception's result yielded another exception. Propagating message from ");
                        //logger.debug("Original Exception: {}", apiException);
                        //logger.debug("Workitem Exception: {}", e);

                        /*
                         * This is the real api exception message.
                         */
                        message = e.getMessage();
                }
        }

        message = removeReturnCode(message);

        return new InterpretedApiException(apiException, message, command, exitCode);
    }

    private static String removeReturnCode(final String message)
    {
        if (message == null)
                return null;

        final int index = message.indexOf("##");

        if (index == -1)
                return message;

        return message.substring(index + 2);
    }

    private final String command;

    /**
     * Returns the executed command that caused the exception
     *
     * @return command Complete CLI Command String
     */
    public String getCommand()
    {
        return command;
    }

    private final int exitCode;

    /**
     * Returns the exit codes associated with executing the command
     *
     * @return exitCode API/CLI Exit Code
     */
    public int getExitCode()
    {
        return exitCode;
    }

}

