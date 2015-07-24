/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.ptc.tifworkbench.integrity.IntegrityException;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import com.ptc.tifworkbench.jaxbbinding.UserDefinition;
import com.ptc.tifworkbench.jaxbbinding.UsersDefinitions;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.Arrays;

/**
 *
 * @author pbowden
 */
public class UserReader extends AdminObjectReader   
{
    private UsersDefinitions users;
    
    public UserReader(UsersDefinitions users, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.users = users;
    }
    
    @Override
    void read() throws IntegrityException 
    {
	log("Reading users.");
        Command cmd = new Command(Command.IM, "users");
        Option fopt = new Option("fields");
        fopt.add(Arrays.asList("name", "description", "fullname", "email", "notificationRule"));
        cmd.addOption(fopt);
        try 
        {
	    Response resp = getApi().execute(cmd);
	    WorkItemIterator wkIt = resp.getWorkItems();
	    reportStatus(0, "Read users");
            int count=0;
	    while(wkIt.hasNext())
	    {
	    	WorkItem wk = wkIt.next();
	    	String userName = wk.getId();
		log("    Read user " + userName);
                reportStatus(50, "Read user: " + userName);
                count++;
                UserDefinition udef = getFactory().createUserDefinition();
                udef.setName(userName);
                String desc = wk.getField("description").getValueAsString();
                udef.setDescription(makeSafe(desc));
                String rule = wk.getField("notificationRule").getValueAsString();
                udef.setNotificationRule(makeSafe(rule));
                String fullname = wk.getField("fullname").getValueAsString();
                udef.setFullName(makeSafe(fullname));
                String email = wk.getField("email").getValueAsString();
                udef.setEmail(makeSafe(email));
                // TODO - active. Decode binary.
                users.getUser().add(udef);
	    }
            reportStatus(100, "Read " + count + " users");

        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error reading list of users.", ex);
        }
    }
    
}
