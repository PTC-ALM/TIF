/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.ptc.tifworkbench.integrity.IntegrityException;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import com.ptc.tifworkbench.jaxbbinding.ConstraintDefinition;
import com.ptc.tifworkbench.jaxbbinding.ConstraintTargetDefinition;
import com.ptc.tifworkbench.jaxbbinding.ConstraintType;
import com.ptc.tifworkbench.jaxbbinding.ConstraintsDefinition;
import com.ptc.tifworkbench.jaxbbinding.DocumentModelDefinition;
import com.ptc.tifworkbench.jaxbbinding.DocumentRole;
import com.ptc.tifworkbench.jaxbbinding.FieldReference;
import com.ptc.tifworkbench.jaxbbinding.FieldsReference;
import com.ptc.tifworkbench.jaxbbinding.MandatoryFieldReference;
import com.ptc.tifworkbench.jaxbbinding.MandatoryFields;
import com.ptc.tifworkbench.jaxbbinding.NextState;
import com.ptc.tifworkbench.jaxbbinding.PropertiesDefinition;
import com.ptc.tifworkbench.jaxbbinding.PropertyDefinition;
import com.ptc.tifworkbench.jaxbbinding.SignificantEdits;
import com.ptc.tifworkbench.jaxbbinding.StateReference;
import com.ptc.tifworkbench.jaxbbinding.StatesReference;
import com.ptc.tifworkbench.jaxbbinding.TestManagementDefintion;
import com.ptc.tifworkbench.jaxbbinding.TestManagementDefintion.Testcase;
import com.ptc.tifworkbench.jaxbbinding.TestManagementDefintion.Testcase.ResultFields;
import com.ptc.tifworkbench.jaxbbinding.TestManagementDefintion.Testsession;
import com.ptc.tifworkbench.jaxbbinding.TestManagementDefintion.Testsession.Groups;
import com.ptc.tifworkbench.jaxbbinding.TestManagementRoleType;
import com.ptc.tifworkbench.jaxbbinding.TestResultModificationType;
import com.ptc.tifworkbench.jaxbbinding.TypeDefinition;
import com.ptc.tifworkbench.jaxbbinding.TypesDefinition;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author pbowden
 */
public class TypeReader extends AdminObjectReader
{
    public static final String [] IGNORE_TYPES = 
        {"FVA-temporary-ibpl-type", "FVA-temporary"};
    

    private TypesDefinition types;
    
    public TypeReader(TypesDefinition types, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.types=types;
    }
    
    @Override
    void read() throws IntegrityException 
    {
	log("Reading types.");
        List<String> ignore = new ArrayList<String>(Arrays.asList(IGNORE_TYPES));
        Command cmd = new Command(Command.IM, "types");
        List<String> names = new ArrayList<String>();
	try
        {
            Response resp = getApi().execute(cmd);

	    WorkItemIterator wkIt = resp.getWorkItems();
	    while(wkIt.hasNext())
	    {
	    	WorkItem wk = wkIt.next();
	    	String typeName = wk.getId();
		log("Read type: " + typeName);
		names.add(typeName);
	    }
        }catch(APIException ex)
        {
            throw IntegrityExceptionEx.create("Could not get list of types.", ex);
        }
        
        int numTypes = names.size();
        int count = 0;
        for(String typeName : names)
        {
            if(!ignore.contains(typeName))
            {
                int prog = (100 * count++)/numTypes;
                reportStatus(prog, "Read type: " + typeName);
                TypeDefinition td = getFactory().createTypeDefinition();
                td.setName(typeName);
                types.getType().add(td);
                try 
                {
                    readType(td, typeName);
                } catch(Exception e) 
                {
                    log("Failed to read type: " + e.getMessage());
                    log("                     " + e.getClass().getName());
                    System.out.println("Failed to read type " + typeName + ", " + e.getMessage());
                    e.printStackTrace(System.out);
                }
            }
        }
    }
    
    
    protected void readType(TypeDefinition tdef, String typeName) throws APIException
    {
        FieldsReference fref = getFactory().createFieldsReference();
        tdef.setFields(fref);
        StatesReference sref = getFactory().createStatesReference();
        tdef.setStates(sref);
        
        Command cmd = new Command(Command.IM, "viewtype");
        cmd.addOption(new Option("showProperties"));
        cmd.addSelection(typeName);
	Response resp = getApi().execute(cmd);
	WorkItem wk = resp.getWorkItem(typeName);
        readAttributes(tdef, wk);
        readChangePackageAttributes(tdef, wk);
        readProperties(tdef, wk);
        readVisibleFields(fref, tdef, wk);
        readStates(sref, wk);
        readDocumentModel(tdef, wk);
        readTestManagement(tdef, wk);
        readConstraints(tdef, wk);
    }
    
    protected void readVisibleFields(FieldsReference fref, TypeDefinition tdef, WorkItem wk)
    {
        ItemList visibleFields = (ItemList)wk.getField("visibleFields").getList();
        if(visibleFields == null)return;
        Iterator<Item> itemIterator = visibleFields.getItems();
        while(itemIterator.hasNext()) 
        {
            Item item = (Item)itemIterator.next();
            String fieldName = item.getId();
            log("    Read visible field: " + fieldName);
            FieldReference fieldRef = getFactory().createFieldReference();
            fieldRef.setName(fieldName);
            fref.getField().add(fieldRef);
            String overrideName = "none";
            try
            {
                overrideName = "relevance";
                String relOverride = getOverride(tdef.getName(), fieldName, overrideName);
                if(relOverride != null && relOverride.length() > 0)
                    fieldRef.setRelevance(relOverride);
                
                overrideName = "editability";
                String editOverride = getOverride(tdef.getName(), fieldName, overrideName);
                if(editOverride != null && editOverride.length() > 0)
                    fieldRef.setEditability(editOverride);
                
                overrideName = "default";
                String defaultOverride = getDefaultOverride(tdef.getName(), fieldName);
                if(defaultOverride != null && defaultOverride.length() > 0)
                    fieldRef.setDefault(defaultOverride);
                
                overrideName = "description";
                String descOverride = getDescriptionOverride(tdef.getName(), fieldName);
                if(descOverride != null && descOverride.length() > 0)
                    fieldRef.setDescription(descOverride);
            }catch(Exception e)
            {
                log("Error reading override " + overrideName + " for field " + fieldName + " in type " + tdef.getName());
            }
        }
    }
    
    protected void readStates(StatesReference sref, WorkItem wk)
    {
        ItemList states = (ItemList)wk.getField("stateTransitions").getList();
        if(states == null)return;
        Iterator<Item> itemIterator = states.getItems();
        while(itemIterator.hasNext())
        {
            Item item = (Item)itemIterator.next();
            String stateName = item.getId();
            log("    Read state field: " + stateName);
            StateReference stateRef = getFactory().createStateReference();
            stateRef.setName(stateName);
            sref.getState().add(stateRef);
            
            readMandatoryFieldsForState(stateRef, wk, stateName);
            readTransitionsForState(stateRef, item, wk, stateName);
        }
    }
    protected void readAttributes(TypeDefinition tdef, WorkItem wk)
    {
        tdef.setCanLabel(getBooleanField("labelEnabled", wk)); 
        tdef.setBacksProject(getBooleanField("backsProject", wk)); 
        tdef.setHaveRevisions(getBooleanField("revisionEnabled", wk)); 
        tdef.setCanDelete(getBooleanField("deleteItemEnabled", wk)); 
        tdef.setShowWorkflow(getBooleanField("showWorkflow", wk)); 
        tdef.setShowHistory(getBooleanField("showHistory", wk)); 
        tdef.setTimeTracking(getBooleanField("timeTrackingEnabled", wk)); 
        tdef.setPhase(getSafeField("phaseField", wk)); // Actually in the Item ID.
        tdef.setDescription(getSafeField("description", wk));
        tdef.setPresentation(getSafeField("viewPresentation", wk));
    }
    
    protected void readChangePackageAttributes(TypeDefinition tdef, WorkItem wk)
    {
        tdef.setCpPolicy(getSafeField("createCPPolicy", wk)); 
        tdef.setAllowCP(getBooleanField("allowChangePackages", wk)); 
    }
    
    protected void readProperties(TypeDefinition tdef, WorkItem wk)
    {
        ItemList properties = (ItemList)wk.getField("properties").getList();
        if(properties != null)
        {
            Iterator propIterator = properties.getItems();
            PropertiesDefinition props = getFactory().createPropertiesDefinition();
            tdef.setProperties(props);
            while(propIterator.hasNext())
            {
                Item item = (Item)propIterator.next();
                String itemName = item.getId();
                log("    Read property: " + itemName);
                PropertyDefinition prop = getFactory().createPropertyDefinition();
                prop.setName(itemName);
                prop.setValue(getSafeField("value", item));
                String desc = getSafeField("description", item);
                // Make the description safe. Should this be done in the transform?
                // We want TIF to be as accurate as possible.
                desc = desc.replaceAll("\n", "");
                desc = desc.replaceAll("\r", "");
                desc = desc.replaceAll("\"", "");
                desc = desc.replaceAll(":", ",");
                desc = desc.replaceAll(";", ",");
                prop.setDescription(desc);
                props.getProperty().add(prop);
            }
        }
    }
    
    protected void readDocumentModel(TypeDefinition tdef, WorkItem wk)
    {
        String docRole = wk.getField("documentClass").getValueAsString();
        if(DocumentRole.NONE.value().equals(docRole))
            return;
        DocumentModelDefinition docDef = getFactory().createDocumentModelDefinition();
        tdef.setDocumentModel(docDef);
        docDef.setRole(DocumentRole.fromValue(docRole));
        if(DocumentRole.SHAREDITEM.value().equals(docRole))
            return;
        String assocType = wk.getField("associatedType").getItem().getId();
        docDef.setAssociatedType(assocType);
        
        Field sigEditsFld = wk.getField("significantEdit");
        List<Item> itemList = sigEditsFld.getList();
        if(itemList.size() > 0)
        {
            SignificantEdits sigEdits = getFactory().createSignificantEdits();
            for(Item item : itemList)
            {
                FieldReference fref = getFactory().createFieldReference();
                fref.setName(item.getId());
                sigEdits.getField().add(fref);
            }
            docDef.setSignificantEdits(sigEdits);
        }

    }
    
    
    protected void readTestManagement(TypeDefinition tdef, WorkItem wk)
    {
        String testRole = wk.getField("testRole").getValueAsString();
        if(TestManagementRoleType.NONE.value().equals(testRole))
            return;
        TestManagementDefintion testDef = getFactory().createTestManagementDefintion();
        tdef.setTestManagement(testDef);
        testDef.setRole(TestManagementRoleType.fromValue(testRole));
        if(TestManagementRoleType.TEST_CASE.value().equals(testRole))
        {
            Testcase tc = getFactory().createTestManagementDefintionTestcase();
            tc.setEnableTestSteps(getBooleanField("testStepsEnabled", wk));
            
            ResultFields resFlds = getFactory().createTestManagementDefintionTestcaseResultFields();
            Field resFld = wk.getField("testCaseResultFields");
            List<Item> itemList = resFld.getList();
            for(Item item : itemList)
                resFlds.getField().add(item.getId());
            tc.setResultFields(resFlds);
            testDef.setTestcase(tc);
        }
        if(TestManagementRoleType.TEST_SESSION.value().equals(testRole))
        {
            Testsession sess = getFactory().createTestManagementDefintionTestsession();
            String modPolicy = wk.getField("modifyTestResultPolicy").getValueAsString();
            if(modPolicy.equals(TestResultModificationType.ANYONE.value()))
            {
                sess.setResultModBy(TestResultModificationType.ANYONE);
            }
            else if(modPolicy.startsWith(TestResultModificationType.GROUPS.value()))
            {
                sess.setResultModBy(TestResultModificationType.GROUPS);
                Groups groups = getFactory().createTestManagementDefintionTestsessionGroups();
                sess.setGroups(groups);
                List<String> grpList = Arrays.asList(modPolicy.split("=")[1].split(","));
                for(String grp : grpList)
                   groups.getGroup().add(grp);  
            }
            else if(modPolicy.startsWith(TestResultModificationType.USER_FIELD.value()))
            {
                sess.setResultModBy(TestResultModificationType.USER_FIELD);
                String fldName = modPolicy.split("=")[1];
                sess.setFieldname(fldName);
            }
            else if(modPolicy.startsWith(TestResultModificationType.GROUP_FIELD.value()))
            {
                sess.setResultModBy(TestResultModificationType.GROUP_FIELD);
                String fldName = modPolicy.split("=")[1];
                sess.setFieldname(fldName);
            }
                
            testDef.setTestsession(sess);
        }
    }
    
    
    protected void readConstraints(TypeDefinition tdef, WorkItem wk)
    {
        List<Item> constrItems = wk.getField("fieldRelationships").getList();
        if(constrItems.size() == 0)
            return;

        ConstraintsDefinition constraints = getFactory().createConstraintsDefinition();
        tdef.setConstraints(constraints);
        
        // Each item is a separate constraint.
        for(Item item : constrItems)
        {
            String model = item.getModelType();
            ConstraintType constrType = getConstraintType(model);
            
            String srcField = item.getId();
            // List of the target fields constraining this source field. 
            // The ID of each item in targetFields field is the constrained field.
            
            List<Item> targetItems = getConstraintTargetFields(item, constrType);
            for(Item targetItem : targetItems)
            {
                ConstraintDefinition constDef = getFactory().createConstraintDefinition();
                constDef.setType(constrType);
                boolean mandatory = false;
                String targetName = "";
                List<String> targetValues = null;
                if("Rule".equals(model) || "ConstraintRule".equals(model))
                {
                    mandatory = item.getField("mandatory").getBoolean();
                    targetName = item.getField("targetField").getItem().getId();
                    String rule = item.getField("rule").getValueAsString();
                    constDef.setRule(rule);
                    targetValues = getConstraintValueList(item, "targetValues");
                }
                else // Basic or field relationship.
                {
                    mandatory = targetItem.getField("mandatory").getBoolean();
                    targetName = targetItem.getId();
                    List<String> sourceValues = getConstraintValueList(targetItem, "sourceValues");
                    ConstraintDefinition.Source constrSource = getFactory().createConstraintDefinitionSource();
                    constrSource.setName(srcField);
                    constrSource.getValue().addAll(sourceValues);
                    constDef.setSource(constrSource);
                    targetValues = getConstraintValueList(targetItem, "targetValues");
                }
                
                ConstraintTargetDefinition constrTarget = getFactory().createConstraintTargetDefinition();
                constDef.setTarget(constrTarget);
                constrTarget.setMandatory(mandatory);
                constrTarget.setName(targetName);
                if(targetValues != null)
                    constrTarget.getValue().addAll(targetValues);
                
                constraints.getConstraint().add(constDef);
            }
        }
    }
    
    private List<Item> getConstraintTargetFields(Item item, ConstraintType constrType)
    {
        List<Item> ret = new ArrayList<Item>();
        if(constrType==ConstraintType.RULE || constrType==ConstraintType.IBPL)
        {
            Item tgt = item.getField("targetField").getItem();
            ret.add(tgt);  
        }
        if(constrType==ConstraintType.BASIC || constrType==ConstraintType.FIELD_RELATIONSHIP)
        {
            List<Item> tgt = item.getField("targetFields").getList();
            ret.addAll(tgt);  
        }
        return ret;
    }
    
    private ConstraintType getConstraintType(String ctype)
    {
        if("Rule".equals(ctype)) return ConstraintType.IBPL;
        if("ConstraintRule".equals(ctype)) return ConstraintType.RULE;
        return ConstraintType.FIELD_RELATIONSHIP;
    }
    
    // Some BASIC constraints have the targetValue(s) field set to null and the only
    // indication of the target value in the cliSpec field - this is used for dynamic 
    // values associated with the Assigned User field, etc: hasPermissions. 
    // The only way we can access this is parsing the cliSpec for the stuff after the 
    // last equals.
    protected List<String> getConstraintValueList(Item item, String fname)
    {
        List<String> ret = new ArrayList<String>();
        try
        {
            if(item.getField(fname) == null || item.getField(fname).getList() == null) // Fall back to the cliSpec
            {
                 String cli = item.getField("cliSpec").getValueAsString();
                 if(cli != null && cli.lastIndexOf("=") != -1)
                 {
                     int idx = cli.lastIndexOf("=");
                     String val = cli.substring(idx+1, cli.length()-1);
                     ret.add(val);
                 }
            }
            else
            {
                Field fld = item.getField(fname);
                List itemList = fld.getList();
                // ret.addAll(fld.getAll()); fails when the target fields are users.
                for(Object it : itemList)
                {
                    String val = it.toString();
                    ret.add(val);
                }
            }
        }
        catch(NoSuchElementException e)
        {
            // Non-fatal error. Just return the empty list.
        }
        return ret;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // TODO: we can do all these three in one call.
    
    protected String getOverride(String typeName, String fieldName, String override) throws Exception 
    {
        Command cmd = new Command(Command.IM, "viewfield");
        cmd.addOption(new Option("overrideForType", typeName));
        cmd.addSelection(fieldName);
        Response res = getApi().execute(cmd);
    	try
        {
            return res.getWorkItem(fieldName).getField(override + "Rule").getValueAsString();
        }catch(Exception e)
        {
            return "";
        }
    }
    
    protected String getDescriptionOverride(String typeName, String fieldName) throws Exception 
    {
    	Command cmd = new Command(Command.IM, "viewfield");
    	cmd.addOption(new Option("overrideForType", typeName));
    	cmd.addSelection(fieldName);
    	Response res = getApi().execute(cmd);
    	try
        {
            return  res.getWorkItem(fieldName).getField("description").getValueAsString();
        }catch(Exception e)
        {
	    return "";
        }
    }

    // If we can't find a valid default we just assume it doesn't exist and return nothing.
    protected String getDefaultOverride(String typeName, String fieldName) throws Exception 
    {
    	Command cmd = new Command(Command.IM, "viewfield");
    	cmd.addOption(new Option("overrideForType", typeName));
    	cmd.addSelection(fieldName);
    	Response res = getApi().execute(cmd);
    	try
        {
            return  res.getWorkItem(fieldName).getField("default").getValueAsString();
        }catch(Exception e)
        {
	    return "";
        }
    }

    // Mandatory fields are reported a little oddly. They are a list from the top workitem
    // rather than sub-items of the state.
    protected void readMandatoryFieldsForState(StateReference stateRef, WorkItem wk, String state) 
    {
    	ItemList mandFields = (ItemList)wk.getField("mandatoryFields").getList();
    	if (mandFields != null && mandFields.contains(state)) 
        {
            ItemList theFields = (ItemList)mandFields.getItem(state).getField("fields").getList();
            if (theFields != null) 
            {
                MandatoryFields mf = getFactory().createMandatoryFields();
                stateRef.setMandatory(mf);
                Iterator<Item> theItems = theFields.getItems(); 
                while (theItems.hasNext()) 
                {
                        Item item = theItems.next();
                        MandatoryFieldReference mrf = getFactory().createMandatoryFieldReference();
                        mrf.setName(item.getId());
                        mf.getField().add(mrf);
                }
            }
    	}
    }
    
    
    protected void readTransitionsForState(StateReference stateRef, Item item, WorkItem wk, String state) 
    {
        Field transitions = item.getField("targetStates");
        ItemList targetList = (ItemList)transitions.getList();
        Iterator targetIt = targetList.getItems();
        while(targetIt.hasNext())
        {
            Item target = (Item)targetIt.next();
            String targetState = target.getId();
            log("    Read target state field: " + targetState);
            NextState next = getFactory().createNextState();
            stateRef.getNext().add(next);
            next.setName(targetState);
            // Get the permitted groups.
            StringBuffer groupString = new StringBuffer();
            Field groups = target.getField("permittedGroups");
            ItemList groupList = (ItemList)groups.getList();
            Iterator groupIt = groupList.getItems();
            while(groupIt.hasNext())
            {
                Item group = (Item)groupIt.next();
                String groupName = group.getId();
                log("    Read permitted group: " + groupName);
                groupString.append(groupName + ",");
            }
            if (groupString.charAt(groupString.length()-1) == ',') 
                    groupString.deleteCharAt(groupString.length()-1); // snip the last comma.
            
            next.setGroup(groupString.toString());
        }
    }
    
    

}
