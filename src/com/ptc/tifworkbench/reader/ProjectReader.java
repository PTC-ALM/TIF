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
import com.ptc.tifworkbench.jaxbbinding.ProjectDefinition;
import com.ptc.tifworkbench.jaxbbinding.ProjectsDefinitions;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.Arrays;

/**
 *
 * @author pbowden
 */
public class ProjectReader extends AdminObjectReader   
{
    private ProjectsDefinitions projects;
    
    public ProjectReader(ProjectsDefinitions projects, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.projects = projects;
    }
    
    @Override
    void read() throws IntegrityException 
    {
	log("Reading projects.");
        Command cmd = new Command(Command.IM, "projects");
        Option fopt = new Option("fields");
        fopt.add(Arrays.asList("name", "description", "parent", "permittedGroups"));
        cmd.addOption(fopt);
        try 
        {
	    Response resp = getApi().execute(cmd);
	    WorkItemIterator wkIt = resp.getWorkItems();
	    reportStatus(0, "Read projects");
            int count=0;
	    while(wkIt.hasNext())
	    {
	    	WorkItem wk = wkIt.next();
	    	String projectName = wk.getId();
                if(projectName.startsWith("/")) 
                    projectName = projectName.substring(1);
		log("    Read project " + projectName);
                reportStatus(50, "Read project: " + projectName);
                count++;
                        
                ProjectDefinition pdef = getFactory().createProjectDefinition();
                pdef.setName(projectName);
                String desc = wk.getField("description").getValueAsString();
                pdef.setDescription(makeSafe(desc));
                String groups = wk.getField("permittedGroups").getValueAsString();
                pdef.setPermittedGroups(makeSafe(groups));
                String parent = wk.getField("parent").getValueAsString();
                pdef.setParent(makeSafe(parent));
                projects.getProject().add(pdef);
	    }
            reportStatus(100, "Read " + count + " projects");

        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error reading list of users.", ex);
        }
    }
    
}
