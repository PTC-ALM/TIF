/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import com.ptc.tifworkbench.jaxbbinding.DynamicGroupsDefinition;
import com.ptc.tifworkbench.jaxbbinding.FieldsDefinitions;
import com.ptc.tifworkbench.jaxbbinding.GroupsDefinition;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.ObjectFactory;
import com.ptc.tifworkbench.jaxbbinding.ProjectsDefinitions;
import com.ptc.tifworkbench.jaxbbinding.QueriesDefinition;
import com.ptc.tifworkbench.jaxbbinding.StatesDefinition;
import com.ptc.tifworkbench.jaxbbinding.TriggersDefinition;
import com.ptc.tifworkbench.jaxbbinding.TypesDefinition;
import com.ptc.tifworkbench.jaxbbinding.UsersDefinitions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBException;

/**
 *
 * @author pbowden
 */
public class TifUtils 
{
    public static ImSolution createEmptyTif()
    {
    	ObjectFactory factory = new ObjectFactory();
        ImSolution imsolution = factory.createImSolution();
          
        // USERS
        UsersDefinitions users = factory.createUsersDefinitions();
        imsolution.setUsers(users);
        // GROUPS
        GroupsDefinition groups = factory.createGroupsDefinition();
        imsolution.setGroups(groups);
        // DYNAMIC GROUPS
        DynamicGroupsDefinition dynGroups = factory.createDynamicGroupsDefinition();
        imsolution.setDynamicGroups(dynGroups);
        // PROJECTS
        ProjectsDefinitions projects = factory.createProjectsDefinitions();
        imsolution.setProjects(projects);
        // STATES
        StatesDefinition states = factory.createStatesDefinition();
        imsolution.setStates(states);
        // FIELDS
        FieldsDefinitions fields = factory.createFieldsDefinitions();
        imsolution.setFields(fields);
        // TYPES
        TypesDefinition types = factory.createTypesDefinition();
        imsolution.setTypes(types);
        // TRIGGERS
        TriggersDefinition triggers = factory.createTriggersDefinition();
        imsolution.setTriggers(triggers);
        // QUERIES
        QueriesDefinition queries = factory.createQueriesDefinition();
        imsolution.setQueries(queries);
        return imsolution;        
    }
    
    public static void writeTif(ImSolution sol, File tifFile) throws IOException, JAXBException
    {
        XmlFormatter fmt = new XmlFormatter();
        FileOutputStream ostream = new FileOutputStream(tifFile);
        fmt.marshal(sol, ostream);
        ostream.close();
    }
}
