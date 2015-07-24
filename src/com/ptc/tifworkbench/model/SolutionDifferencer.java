/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

import com.ptc.tifworkbench.jaxbbinding.FieldDefinition;
import com.ptc.tifworkbench.jaxbbinding.FieldsDefinitions;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.ObjectFactory;
import com.ptc.tifworkbench.jaxbbinding.QueriesDefinition;
import com.ptc.tifworkbench.jaxbbinding.QueryDefinition;
import com.ptc.tifworkbench.jaxbbinding.StateDefinition;
import com.ptc.tifworkbench.jaxbbinding.StatesDefinition;
import com.ptc.tifworkbench.jaxbbinding.TriggerDefinition;
import com.ptc.tifworkbench.jaxbbinding.TriggersDefinition;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import com.ptc.tifworkbench.jaxbbinding.TypesDefinition;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.List;

/**
 * Calculate a difference model that will make S1 equivalent to S2.
 * @author pbowden
 */
public class SolutionDifferencer 
{
    private ImSolution diffSol, s1, s2;
    private ObjectFactory factory = new ObjectFactory();
    private StatusReporter reporter;

    public SolutionDifferencer(ImSolution s1, ImSolution s2)
    {
        this.s1=s1;
        this.s2=s2;
    }
    
    public ImSolution getDifferenceSolution()
    {
        assert(diffSol != null);
        return diffSol;
    }
    
    public void setReporter(StatusReporter reporter)
    {
        this.reporter = reporter;
    }

    public void doDifference()
    {
        this.diffSol = factory.createImSolution();
        diffSol.setUsers(factory.createUsersDefinitions());
    	diffSol.setGroups(factory.createGroupsDefinition());
        diffSol.setProjects(factory.createProjectsDefinitions());

        if(!s1.equals(s2))
        {
            report(0, "Differencing");
            
            diffStates();
            diffFields();
            diffTriggers();
            diffQueries();
            diffTypes();
            
            report(100, "Done differencing solutions.");
        }
        else
            report(100, "Solutions are identical");
    }
    
    protected void diffStates()
    {
        report(10, "State definitions");
        StatesDefinition diffStateDefs = factory.createStatesDefinition();
        diffSol.setStates(diffStateDefs);
        SimpleTifDifference<StateDefinition> sd =
                new SimpleTifDifference<StateDefinition>(s1.getStates().getState(), 
                                                         s2.getStates().getState(), 
                                                         diffStateDefs.getState(),
                                                         diffStateDefs.getEditState())
                {
                    @Override
                    public String getKey(StateDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void logMessage(String message)
                    {
                        log(message);   
                    }
                };
        sd.compareDefinitions();
    }
    
    protected void diffFields()
    {
        report(20, "Field definitions");
        FieldsDefinitions diffFieldDefs = factory.createFieldsDefinitions();
        diffSol.setFields(diffFieldDefs);
        SimpleTifDifference<FieldDefinition> fd =
                new SimpleTifDifference<FieldDefinition>(s1.getFields().getField(), 
                                                         s2.getFields().getField(), 
                                                         diffFieldDefs.getField(),
                                                         diffFieldDefs.getEditField())
                {
                    @Override
                    public String getKey(FieldDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void logMessage(String message)
                    {
                        log(message);   
                    }
                };
        fd.compareDefinitions();
    }
    
    protected void diffTriggers()
    {
        report(30, "Trigger definitions");
        TriggersDefinition diffTriggerDefs = factory.createTriggersDefinition();
        diffSol.setTriggers(diffTriggerDefs);
        SimpleTifDifference<TriggerDefinition> td =
                new SimpleTifDifference<TriggerDefinition>(s1.getTriggers().getTrigger(), 
                                                           s2.getTriggers().getTrigger(), 
                                                           diffTriggerDefs.getTrigger(),
                                                           diffTriggerDefs.getEditTrigger())
                {
                    @Override
                    public String getKey(TriggerDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void logMessage(String message)
                    {
                        log(message);   
                    }
                };
        td.compareDefinitions();
    }
    
    protected void diffQueries()
    {
        report(40, "Query definitions");
        QueriesDefinition diffQueryDefs = factory.createQueriesDefinition();
        diffSol.setQueries(diffQueryDefs);
        SimpleTifDifference<QueryDefinition> qd =
                new SimpleTifDifference<QueryDefinition>(s1.getQueries().getQuery(), 
                                                         s2.getQueries().getQuery(), 
                                                         diffQueryDefs.getQuery(),
                                                         diffQueryDefs.getEditQuery())
                {
                    @Override
                    public String getKey(QueryDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void logMessage(String message)
                    {
                        log(message);   
                    }
                };
        qd.compareDefinitions();
    }
    
    /**
     * Things to diff within types:
     * Properties
     * Visible fields / editability / relevance (these can be done in the same diff)
     * State transitions / mandatory fields (need to be done in one lump)
     * Domain declarations
     */
    protected void diffTypes()
    {
        report(50, "Type definitions");
        TypesDefinition diffTypeDefs = factory.createTypesDefinition();
        diffSol.setTypes(diffTypeDefs);
        
        CompoundTifDifference<TypeDefinition> td =
                new CompoundTifDifference<TypeDefinition>(s1.getTypes().getType(), 
                                                          s2.getTypes().getType(), 
                                                          diffTypeDefs.getType(),
                                                          diffTypeDefs.getEditType())
                {
                    @Override
                    public String getKey(TypeDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void subCompare(TypeDefinition td1, TypeDefinition td2, 
                        List<TypeDefinition> diffEdit)
                    {
                        diffTypeComponents(td1, td2, diffEdit);
                    }
                    @Override
                    public void logMessage(String message)
                    {
                        log(message);   
                    }
                };
        td.compareDefinitions();
    }    
    
    protected void diffTypeComponents(TypeDefinition td1, TypeDefinition td2, List<TypeDefinition> diff)
    {
        TypeDefinition tdDiff = factory.createTypeDefinition();
        tdDiff.setName(td1.getName());
        diff.add(tdDiff);
        diffTypeAttributes(td1, td2, tdDiff);
        diffTypeProperties(td1, td2, tdDiff);
        diffTypeVisibleFields(td1, td2, tdDiff);
        diffTypeStates(td1, td2, tdDiff);
        diffTypeDocumentModel(td1, td2, tdDiff);
        diffTypeTestModel(td1, td2, tdDiff);
        diffTypeConstraints(td1, td2, tdDiff);
    }
    
    
    /**
     * If any properties are different we have to re-specify <b>all</b> of them
     * and not only the ones that are different.
     * @param td1
     * @param td2
     * @param tdDiff 
     */
    protected void diffTypeAttributes(TypeDefinition td1, TypeDefinition td2,  TypeDefinition tdDiff)
    {
        report(55, "    " + td1.getName() + " attributes.");
        if(td1.isBacksProject() != td2.isBacksProject())
        {
            log("        Changing attribute 'backs project': " + td2.isBacksProject());
            tdDiff.setBacksProject(td2.isBacksProject());
        }
        if(td1.isAllowCP() != td2.isAllowCP())
        {
            log("        Changing attribute 'allows change package': " + td2.isAllowCP());
            tdDiff.setAllowCP(td2.isAllowCP());
        }
        if(td1.isCanDelete() != td2.isCanDelete())
        {
            log("        Changing attribute 'can delete': " + td2.isCanDelete());
            tdDiff.setCanDelete(td2.isCanDelete());
        }
        if(td1.isCanLabel() != td2.isCanLabel())
        {
            log("        Changing attribute 'can label': " + td2.isCanLabel());
            tdDiff.setCanLabel(td2.isCanLabel());
        }
        if(td1.isHaveRevisions() != td2.isHaveRevisions())
        {
            log("        Changing attribute 'has revisions': " + td2.isHaveRevisions());
            tdDiff.setHaveRevisions(td2.isHaveRevisions());
        }
        if(td1.isShowHistory() != td2.isShowHistory())
        {
            log("        Changing attribute 'show history': " + td2.isShowHistory());
            tdDiff.setShowHistory(td2.isShowHistory());
        }
        if(td1.isShowWorkflow() != td2.isShowWorkflow())
        {
            log("        Changing attribute 'show workflow': " + td2.isShowWorkflow());
            tdDiff.setShowWorkflow(td2.isShowWorkflow());
        }
        if(td1.isTimeTracking() != td2.isTimeTracking())
        {
            log("        Changing attribute 'can track time': " + td2.isTimeTracking());
            tdDiff.setTimeTracking(td2.isTimeTracking());
        }
        
    }
    
    
    /**
     * If any properties are different we have to re-specify <b>all</b> of them
     * and not only the ones that are different.
     * @param td1
     * @param td2
     * @param tdDiff 
     */
    protected void diffTypeProperties(TypeDefinition td1, TypeDefinition td2,  TypeDefinition tdDiff)
    {
        report(60, "    " + td1.getName() + " property definitions.");
        String name = td1.getName();
        if(td1.getProperties() == null || td1.getProperties().getProperty().isEmpty())
        {
            // No properties in the source type: if we have td2 just copy them in.
            if(td2.getProperties() != null)
            {
                log("         ** No type properties in source solution. Adding from target solution.");
                tdDiff.setProperties(td2.getProperties());
            }
        }
        else if(td2.getProperties() == null || td2.getProperties().getProperty().isEmpty())
        {
            log("         ** No type properties in target solution. Removing from the source solution.");
            tdDiff.setProperties(null);
        }
        else if(!td1.getProperties().equals(td2.getProperties()))
        {
            log("         ** Type properties differ. Editing");
            tdDiff.setProperties(td2.getProperties());
        }
        /* We can log more details if required.
        PropertiesDefinition diffPropsDefs = factory.createPropertiesDefinition();
        tdDiff.setProperties(diffPropsDefs);
        SimpleTifDifference<PropertyDefinition> pd =
                new SimpleTifDifference<PropertyDefinition>(td1.getProperties().getProperty(), 
                                                         td2.getProperties().getProperty(), 
                                                         diffPropsDefs.getProperty())
                {
                    @Override
                    public String getKey(PropertyDefinition d)
                    {
                        return d.getName();
                    }
                };
        pd.compareDefinitions();
        */
    }

    protected void diffTypeVisibleFields(TypeDefinition td1, TypeDefinition td2,  TypeDefinition tdDiff)
    {
        report(70, "    " + td1.getName() + " visible fields.");
        if(!td1.getFields().equals(td2.getFields()))
        {
            log("     ** Visible differ. Adding");
            tdDiff.setFields(td2.getFields());
        }
    }
    
    protected void diffTypeStates(TypeDefinition td1, TypeDefinition td2,  TypeDefinition tdDiff)
    {
        report(80, "    " + td1.getName() + " state definitions.");
        if(!td1.getStates().equals(td2.getStates()))
        {
            log("     ** State transitions differ. Adding");
            tdDiff.setStates(td2.getStates());
        }
    }

    

    protected void diffTypeDocumentModel(TypeDefinition td1, TypeDefinition td2,  TypeDefinition tdDiff)
    {
        report(85, "    " + td1.getName() + " document model.");
        if(td1.getDocumentModel() == null)
        {
            if(td2.getDocumentModel() != null)
            {
                log("     ** No type document in source solution. Adding from target solution.");
                tdDiff.setDocumentModel(td2.getDocumentModel());
                return;
            }
        }
        if(td2.getDocumentModel() == null)
        {
            log("     ** No type document model in target solution. Removing from the source solution.");
            tdDiff.setDocumentModel(null);
            return;
        }
        
        if(!td1.getDocumentModel().equals(td2.getDocumentModel()))
        {
            log("     ** Document models differ. Adding");
            tdDiff.setDocumentModel(td2.getDocumentModel());
        }
    }
    
    protected void diffTypeTestModel(TypeDefinition td1, TypeDefinition td2,  TypeDefinition tdDiff)
    {
        report(90, "    " + td1.getName() + " test model.");
        if(td1.getTestManagement() == null)
        {
            if(td2.getTestManagement() != null)
            {
                log("     ** No type test management in source solution. Adding from target solution.");
                tdDiff.setTestManagement(td2.getTestManagement());
                return;
            }
        }
        if(td2.getTestManagement() == null)
        {
            log("     ** No type test management model in target solution. Removing from the source solution.");
            tdDiff.setTestManagement(null);
            return;
        }
        if(!td1.getTestManagement().equals(td2.getTestManagement()))
        {
            log("     ** Test management models differ. Adding");
            tdDiff.setTestManagement(td2.getTestManagement());
        }
    }
    
    protected void diffTypeConstraints(TypeDefinition td1, TypeDefinition td2,  TypeDefinition tdDiff)
    {
        report(95, "    " + td1.getName() + " constraints.");
        if(td1.getConstraints() == null)
        {
            if(td2.getConstraints() != null)
            {
                log("     ** No type constraints in source solution. Adding from target solution.");
                tdDiff.setConstraints(td2.getConstraints());
                return;
            }
        }
        if(td2.getConstraints() == null)
        {
            log("     ** No type constraints in target solution. Removing from the source solution.");
            tdDiff.setConstraints(null);
            return;
        }
        if(!td1.getConstraints().equals(td2.getConstraints()))
        {
            log("     ** Constraints differ. Adding");
            tdDiff.setConstraints(td2.getConstraints());
        }
    }
    
    
    protected void report(int prog, String mess)
    {
        if(reporter != null)
        {
            reporter.reportStatus(prog, mess);
            log(mess);
        }
    }

    protected void log(String message)
    {
        if(reporter != null)
            reporter.reportDetail(message);
    }
    
}
