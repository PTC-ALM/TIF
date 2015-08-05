/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.ptc.tifworkbench.integrity.IntegrityException;
import com.ptc.tifworkbench.jaxbbinding.DynamicGroupsDefinition;
import com.ptc.tifworkbench.jaxbbinding.FieldsDefinitions;
import com.ptc.tifworkbench.jaxbbinding.GroupsDefinition;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.ObjectFactory;
import com.ptc.tifworkbench.jaxbbinding.ProjectsDefinitions;
import com.ptc.tifworkbench.jaxbbinding.QueriesDefinition;
import com.ptc.tifworkbench.jaxbbinding.ReportsDefinition;
import com.ptc.tifworkbench.jaxbbinding.StatesDefinition;
import com.ptc.tifworkbench.jaxbbinding.TriggersDefinition;
import com.ptc.tifworkbench.jaxbbinding.TypesDefinition;
import com.ptc.tifworkbench.jaxbbinding.UsersDefinitions;
import com.ptc.tifworkbench.worker.StatusReporter;


/**
 *
 * @author pbowden
 */
public class SolutionReader 
{ 
       private ImSolution imsolution = null;
       private ObjectFactory factory = new ObjectFactory();
       private StatusReporter reporter;
       
       public SolutionReader(StatusReporter reporter)
       {
           this.reporter=reporter;
       }
       public SolutionReader()
       {
           this.reporter=null;
       }
       
       public void setReporter(StatusReporter reporter)
       {
           this.reporter = reporter;
       }
       
       public void readSolution() throws InterruptedException, IntegrityException
       {
    	  this.imsolution = factory.createImSolution();
          
          // USERS
          UsersDefinitions users = factory.createUsersDefinitions();
          imsolution.setUsers(users);
	  UserReader userReader = new UserReader(users, reporter); 
	  userReader.read();
          
          // GROUPS
          GroupsDefinition groups = factory.createGroupsDefinition();
          imsolution.setGroups(groups);
	  GroupReader groupReader = new GroupReader(groups, reporter); 
	  groupReader.read();
          
          // DYNAMIC GROUPS
          DynamicGroupsDefinition dynGroups = factory.createDynamicGroupsDefinition();
          imsolution.setDynamicGroups(dynGroups);
	  DynamicGroupReader dynGroupReader = new DynamicGroupReader(dynGroups, reporter); 
	  dynGroupReader.read();
          
          // PROJECTS
          ProjectsDefinitions projects = factory.createProjectsDefinitions();
          imsolution.setProjects(projects);
	  ProjectReader projectReader = new ProjectReader(projects, reporter); 
	  projectReader.read();
          

          // STATES
          StatesDefinition states = factory.createStatesDefinition();
          imsolution.setStates(states);
	  StateReader stateReader = new StateReader(states, reporter); 
	  stateReader.read();

          // FIELDS
          FieldsDefinitions fields = factory.createFieldsDefinitions();
          imsolution.setFields(fields);
	  FieldReader fieldReader = new FieldReader(fields, reporter); 
	  fieldReader.read();
          
          // TYPES
          TypesDefinition types = factory.createTypesDefinition();
          imsolution.setTypes(types);
	  TypeReader typeReader = new TypeReader(types, reporter); 
	  typeReader.read();
          
          // TRIGGERS
          TriggersDefinition triggers = factory.createTriggersDefinition();
          imsolution.setTriggers(triggers);
	  TriggerReader triggerReader = new TriggerReader(triggers, reporter); 
	  triggerReader.read();
          
          // QUERIES
          QueriesDefinition queries = factory.createQueriesDefinition();
          imsolution.setQueries(queries);
	  QueryReader queryReader = new QueryReader(queries, reporter); 
	  queryReader.read();
          
          // REPORTS
          ReportsDefinition reports = factory.createReportsDefinition();
          imsolution.setReports(reports);
          ReportReader reportReader = new ReportReader(reports, reporter);
          reportReader.read();
          
          report(100, "Done reading from Integrity");
       }
       
       public ImSolution getSolution()
       {
           return imsolution;
       }
       

       protected void report(int prog, String mess)
       {
           if(reporter != null)
               reporter.reportStatus(prog, mess);
       }
}
