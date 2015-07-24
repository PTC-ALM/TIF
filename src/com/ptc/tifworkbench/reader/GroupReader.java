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
import com.ptc.tifworkbench.jaxbbinding.GroupDefinition;
import com.ptc.tifworkbench.jaxbbinding.GroupsDefinition;
import com.ptc.tifworkbench.model.StandardGroups;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pbowden
 */
public class GroupReader extends AdminObjectReader   
{
    private GroupsDefinition groups;
    
    public GroupReader(GroupsDefinition groups, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.groups = groups;
    }
    
    @Override
    void read() throws IntegrityException 
    {
	log("Reading groups.");
        List<String> ignore = StandardGroups.getStandardGroupList();
        
        Command cmd = new Command(Command.IM, "groups");
        Option fopt = new Option("fields");
        fopt.add(Arrays.asList("name", "description", "email", "notificationRule"));
        cmd.addOption(fopt);
        try 
        {
	    Response resp = getApi().execute(cmd);
	    WorkItemIterator wkIt = resp.getWorkItems();
	    reportStatus(0, "Read groups");
            int count=0;
	    while(wkIt.hasNext())
	    {
	    	WorkItem wk = wkIt.next();
	    	String groupName = wk.getId();
                
                reportStatus(50, "Read group: " + groupName);
                count++;
		log("    Read group " + groupName);
                if(ignore.contains(groupName)) continue;
                
                GroupDefinition gdef = getFactory().createGroupDefinition();
                gdef.setName(groupName);
                String desc = wk.getField("description").getValueAsString();
                gdef.setDescription(makeSafe(desc));
                String rule = wk.getField("notificationRule").getValueAsString();
                gdef.setNotificationRule(makeSafe(rule));
                String email = wk.getField("email").getValueAsString();
                gdef.setEmail(makeSafe(email));
                // TODO - active. Decode binary.
                groups.getGroup().add(gdef);
	    }
            reportStatus(100, "Read " + count + " groups");
        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error reading list of users.", ex);
        }
    }
    
    protected int countWorkItems(WorkItemIterator wkit)
    {
        int count=0;
        while(wkit.hasNext()) count++;
        return count;
    }
    
}
