/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.ui;

import com.ptc.tifworkbench.jaxbbinding.ConstraintDefinition;
import com.ptc.tifworkbench.jaxbbinding.ConstraintType;
import com.ptc.tifworkbench.jaxbbinding.DynamicGroupDefinition;
import com.ptc.tifworkbench.jaxbbinding.FieldDefinition;
import com.ptc.tifworkbench.jaxbbinding.FieldReference;
import com.ptc.tifworkbench.jaxbbinding.GroupDefinition;
import com.ptc.tifworkbench.jaxbbinding.ImSolution;
import com.ptc.tifworkbench.jaxbbinding.MandatoryFieldReference;
import com.ptc.tifworkbench.jaxbbinding.NextState;
import com.ptc.tifworkbench.jaxbbinding.ProjectDefinition;
import com.ptc.tifworkbench.jaxbbinding.PropertyDefinition;
import com.ptc.tifworkbench.jaxbbinding.QueryDefinition;
import com.ptc.tifworkbench.jaxbbinding.StateDefinition;
import com.ptc.tifworkbench.jaxbbinding.StateReference;
import com.ptc.tifworkbench.jaxbbinding.TestManagementRoleType;
import com.ptc.tifworkbench.jaxbbinding.TriggerDefinition;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import com.ptc.tifworkbench.jaxbbinding.UserDefinition;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
 
    
/**
 *
 * @author pbowden
 */
public class TifTreeModel  
{
    // TODO: refactor this with generics for the model translation.
    // Even better, work out how we can get JAXB to wrap the classes with a visitor.
    
    public static DefaultTreeModel createTree(ImSolution sol)
    {
        DefaultMutableTreeNode top =
            new DefaultMutableTreeNode("Template");
        createNodes(top, sol);
        
        return new DefaultTreeModel(top);
    }
    
    protected static void createNodes(DefaultMutableTreeNode top, ImSolution sol)
    {
        try
        {
            top.add(makeStates(new DefaultMutableTreeNode("States"), sol));    
            top.add(makeFields(new DefaultMutableTreeNode("Fields"), sol));    
            top.add(makeTypes(new DefaultMutableTreeNode("Types"), sol));    
            top.add(makeTriggers(new DefaultMutableTreeNode("Triggers"), sol));    
            top.add(makeQueries(new DefaultMutableTreeNode("Queries"), sol));
            top.add(makeDynamicGroups(new DefaultMutableTreeNode("DynamicGroups"), sol));
            top.add(makeUsers(new DefaultMutableTreeNode("Users"), sol));
            top.add(makeGroups(new DefaultMutableTreeNode("Groups"), sol));
            top.add(makeProjects(new DefaultMutableTreeNode("Projects"), sol));
        }catch(Exception e)
        {
            System.out.println("Could not create SIF tree.");
            e.printStackTrace(System.out);
        }
    }
    protected static DefaultMutableTreeNode makeStates(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getStates() == null) return node;
        TifNodeFactory<StateDefinition> fact =
                new TifNodeFactory<StateDefinition>(sol.getStates().getState(), sol.getStates().getEditState())
                {
                    @Override
                    public String getKey(StateDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void addAttributes(TifTreeNode node, StateDefinition def)
                    {
                        safeAddNode("Description", def.getDescription(), node);
                    }
                };
        fact.createNodes(node);
        return node;
    }
    protected static DefaultMutableTreeNode makeFields(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getFields() == null) return node;
        TifNodeFactory<FieldDefinition> fact =
                new TifNodeFactory<FieldDefinition>(sol.getFields().getField(), sol.getFields().getEditField())
                {
                    @Override
                    public String getKey(FieldDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void addAttributes(TifTreeNode node, FieldDefinition def)
                    {
                        safeAddNode("Description", def.getDescription(), node);
                        if(def.getType() != null)
                            safeAddNode("Type", def.getType().value(), node);
                        else
                            safeAddNode("Type", "WARNING:UNKNOWN", node);
                        safeAddNode("Editability", def.getEditability(), node);
                        safeAddNode("Relevance", def.getRelevance(), node);
                        safeAddNode("Default", def.getDefault(), node);
                        safeAddNode("Max", def.getMax(), node);
                        safeAddNode("Min", def.getMin(), node);
                        if(def.getComputation() != null)
                            safeAddNode("Computation", def.getComputation().getValue(), node);
                    }
                };
        fact.createNodes(node);
        return node;
    }
    protected static DefaultMutableTreeNode makeTypes(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getTypes() == null) return node;
        List<TypeDefinition> allTypes = new ArrayList<TypeDefinition>(sol.getTypes().getType());
        allTypes.addAll(sol.getTypes().getEditType());

        for(TypeDefinition type : allTypes)
        {
            DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(type.getName());
            
            if(type.getProperties() != null)
            {
                DefaultMutableTreeNode propsNode = new DefaultMutableTreeNode("properties");
                typeNode.add(propsNode);
                for(PropertyDefinition prop : type.getProperties().getProperty())
                {
                    DefaultMutableTreeNode propNode = new DefaultMutableTreeNode(prop.getName());
                    safeAddNode("Value", prop.getValue(), propNode);   
                    safeAddNode("Description", prop.getDescription(), propNode);   
                    propsNode.add(propNode);
                }
            }
            if(type.getFields() != null)
            {
                DefaultMutableTreeNode fldsNode = new DefaultMutableTreeNode("fields");
                typeNode.add(fldsNode);
                for(FieldReference fld : type.getFields().getField())
                {
                    DefaultMutableTreeNode fldNode = new DefaultMutableTreeNode(fld.getName());
                    safeAddNode("Editability", fld.getEditability(), fldNode);   
                    safeAddNode("Relevance", fld.getRelevance(), fldNode);   
                    safeAddNode("Description", fld.getDescription(), fldNode);   
                    fldsNode.add(fldNode);
                }
            }
            
            if(type.getStates() != null)
            {
                DefaultMutableTreeNode statesNode = new DefaultMutableTreeNode("states");
                typeNode.add(statesNode);
                for(StateReference state : type.getStates().getState())
                {
                    DefaultMutableTreeNode stateNode = new DefaultMutableTreeNode(state.getName());
                    for(NextState nextState : state.getNext())
                        safeAddNode("Next", nextState.getName() + " [" + nextState.getGroup() + "]", stateNode); 
                    if(state.getMandatory() != null)
                    {
                        DefaultMutableTreeNode mandatoryNode = new DefaultMutableTreeNode("Mandatory fields");
                        stateNode.add(mandatoryNode);
                        for(MandatoryFieldReference mandField : state.getMandatory().getField())
                            safeAddNode("Mandatory field", mandField.getName(), mandatoryNode); 
                    }
                    statesNode.add(stateNode);
                }
            }
            if(type.getDocumentModel() != null)
            {
                DefaultMutableTreeNode docmodelNode = new DefaultMutableTreeNode("Document model");
                typeNode.add(docmodelNode);
                DefaultMutableTreeNode roleNode = new DefaultMutableTreeNode("Role: " + type.getDocumentModel().getRole().value());
                DefaultMutableTreeNode assocNode = new DefaultMutableTreeNode("Associated type: " + type.getDocumentModel().getAssociatedType());
                docmodelNode.add(roleNode);
                docmodelNode.add(assocNode);
                
            }
            if(type.getTestManagement() != null)
            {
                if(type.getTestManagement().getRole() != null && type.getTestManagement().getRole() != TestManagementRoleType.NONE)
                {
                    DefaultMutableTreeNode tmNode = new DefaultMutableTreeNode("Test Management");
                    DefaultMutableTreeNode roleNode = new DefaultMutableTreeNode("Role: " + type.getTestManagement().getRole().value());
                    tmNode.add(roleNode);
                    typeNode.add(tmNode);
                }
            }
            if(type.getConstraints() != null)
            {
                DefaultMutableTreeNode constraintsNode = new DefaultMutableTreeNode("Constraints");
                typeNode.add(constraintsNode);
                for(ConstraintDefinition constraint : type.getConstraints().getConstraint())
                {
                    StringBuffer buff = new StringBuffer();
                    buff.append(constraint.getType().value() + " ");
                    if(constraint.getType() == ConstraintType.IBPL || constraint.getType() == ConstraintType.RULE )
                        buff.append(constraint.getRule() + ":");
                    else
                        buff.append(constraint.getSource().getName() + "=" + constraint.getSource().getValue() + ":");
                    buff.append(constraint.getTarget().getName());
                    if(constraint.getTarget().getValue().size() > 0)
                        buff.append("=" + constraint.getTarget().getValue().toString());
                    DefaultMutableTreeNode constrNode = new DefaultMutableTreeNode(buff.toString());
                    constraintsNode.add(constrNode);
                }
                
            }
            node.add(typeNode);
        }
        return node;
    }

    protected static DefaultMutableTreeNode makeTriggers(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getTriggers() == null) return node;
        TifNodeFactory<TriggerDefinition> fact =
                new TifNodeFactory<TriggerDefinition>(sol.getTriggers().getTrigger(), sol.getTriggers().getEditTrigger())
                {
                    @Override
                    public String getKey(TriggerDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void addAttributes(TifTreeNode node, TriggerDefinition def)
                    {
                        safeAddNode("Description", def.getDescription(), node);
                        safeAddNode("Frequency", def.getFrequency(), node);
                        safeAddNode("Parameters", def.getParams(), node);
                        safeAddNode("Query", def.getQuery(), node);
                        safeAddNode("Rule", def.getRule(), node);
                        safeAddNode("Run As", def.getRunAs(), node);
                        safeAddNode("Script", def.getScript(), node);
                        safeAddNode("Timing", def.getScriptTiming(), node);
                    }
                };
        fact.createNodes(node);
        return node;
    }

    protected static DefaultMutableTreeNode makeQueries(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getQueries() == null) return node;
        TifNodeFactory<QueryDefinition> fact =
                new TifNodeFactory<QueryDefinition>(sol.getQueries().getQuery(), sol.getQueries().getEditQuery())
                {
                    @Override
                    public String getKey(QueryDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void addAttributes(TifTreeNode node, QueryDefinition def)
                    {
                        safeAddNode("Description", def.getDescription(), node);
                        safeAddNode("Definition", def.getDefinition(), node);
                        safeAddNode("Share", def.getShareGroups(), node);
                    }
                };
        fact.createNodes(node);
        return node;
    }

    protected static DefaultMutableTreeNode makeDynamicGroups(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getStates() == null) return node;
        if(sol.getDynamicGroups() != null)
        {
            TifNodeFactory<DynamicGroupDefinition> fact =
                    new TifNodeFactory<DynamicGroupDefinition>(sol.getDynamicGroups().getDynamicGroup(), null)
                    {
                        @Override
                        public String getKey(DynamicGroupDefinition d)
                        {
                            return d.getName();
                        }
                        @Override
                        public void addAttributes(TifTreeNode node, DynamicGroupDefinition def)
                        {
                            safeAddNode("Description", def.getDescription(), node);
                        }
                    };
            fact.createNodes(node);
        }
        return node;
    }
    
    protected static DefaultMutableTreeNode makeUsers(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getStates() == null) return node;
        TifNodeFactory<UserDefinition> fact =
                new TifNodeFactory<UserDefinition>(sol.getUsers().getUser(), null)
                {
                    @Override
                    public String getKey(UserDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void addAttributes(TifTreeNode node, UserDefinition def)
                    {
                        safeAddNode("Description", def.getDescription(), node);
                        safeAddNode("Email", def.getEmail(), node);
                        safeAddNode("FullName", def.getFullName(), node);
                        safeAddNode("NotificationRule", def.getNotificationRule(), node);
                    }
                };
        fact.createNodes(node);
        return node;
    }
    
    protected static DefaultMutableTreeNode makeGroups(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getStates() == null) return node;
        TifNodeFactory<GroupDefinition> fact =
                new TifNodeFactory<GroupDefinition>(sol.getGroups().getGroup(), null)
                {
                    @Override
                    public String getKey(GroupDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void addAttributes(TifTreeNode node, GroupDefinition def)
                    {
                        safeAddNode("Description", def.getDescription(), node);
                        safeAddNode("Email", def.getEmail(), node);
                        safeAddNode("NotificationRule", def.getNotificationRule(), node);
                    }
                };
        fact.createNodes(node);
        return node;
    }
    
    protected static DefaultMutableTreeNode makeProjects(DefaultMutableTreeNode node, ImSolution sol)
    {
        if(sol.getStates() == null) return node;
        TifNodeFactory<ProjectDefinition> fact =
                new TifNodeFactory<ProjectDefinition>(sol.getProjects().getProject(), null)
                {
                    @Override
                    public String getKey(ProjectDefinition d)
                    {
                        return d.getName();
                    }
                    @Override
                    public void addAttributes(TifTreeNode node, ProjectDefinition def)
                    {
                        safeAddNode("Description", def.getDescription(), node);
                        safeAddNode("Parent", def.getParent(), node);
                        safeAddNode("PermittedGroups", def.getPermittedGroups(), node);
                    }
                };
        fact.createNodes(node);
        return node;
    }
    
    
    
    protected static void safeAddNode(String prefix, String name, DefaultMutableTreeNode parent)
    {
        if(exists(name)) 
        {
            DefaultMutableTreeNode n = new DefaultMutableTreeNode(prefix + ":" + name);
            parent.add(n);
        }
    }

    protected static boolean exists(String prop)
    {
        return ((prop != null) && (prop.length()>0));
    }
    
        
   
}
