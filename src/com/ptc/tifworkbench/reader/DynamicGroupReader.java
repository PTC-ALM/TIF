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
import com.ptc.tifworkbench.jaxbbinding.DynamicGroupDefinition;
import com.ptc.tifworkbench.jaxbbinding.DynamicGroupsDefinition;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pbowden
 */
public class DynamicGroupReader extends AdminObjectReader
{
    private DynamicGroupsDefinition dynGroups;
    
    public DynamicGroupReader(DynamicGroupsDefinition dynGroups, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.dynGroups = dynGroups;
    }
    
    @Override
    void read() throws IntegrityException 
    {
	log("Reading dynamic groups.");
        Command cmd = new Command(Command.IM, "dynamicgroups");
        Option fopt = new Option("fields");
        fopt.add(Arrays.asList("membership"," name", "description", "image"));
        cmd.addOption(fopt);
        List<String> names = new ArrayList<String>();
        try 
        {
	    Response resp = getApi().execute(cmd);
	    WorkItemIterator wkIt = resp.getWorkItems();
	    reportStatus(0, "Read dynamic groups.");
            int count=0;
	    while(wkIt.hasNext())
	    {
	    	WorkItem wk = wkIt.next();
	    	String groupName = wk.getId();
		log("    Read dynamic group: " + groupName);
                reportStatus(50, "Read dynamic group: " + groupName);
                count++;
                DynamicGroupDefinition gdef = getFactory().createDynamicGroupDefinition();
                gdef.setName(groupName);
                String desc = wk.getField("description").getValueAsString();
                gdef.setDescription(makeSafe(desc));
                dynGroups.getDynamicGroup().add(gdef);
	    }
            reportStatus(100, "Read " + count + " dynamic groups");

        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error reading list of dynamic groups.", ex);
        }
    }
        
}
