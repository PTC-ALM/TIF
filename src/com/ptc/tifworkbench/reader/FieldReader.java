/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.mks.api.Command;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.ptc.tifworkbench.integrity.IntegrityException;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import com.ptc.tifworkbench.jaxbbinding.BackingIssueReference;
import com.ptc.tifworkbench.jaxbbinding.ColumnsDefinition;
import com.ptc.tifworkbench.jaxbbinding.ComputationDefinition;
import com.ptc.tifworkbench.jaxbbinding.DisplayAsType;
import com.ptc.tifworkbench.jaxbbinding.DisplayType;
import com.ptc.tifworkbench.jaxbbinding.FieldDefinition;
import com.ptc.tifworkbench.jaxbbinding.FieldType;
import com.ptc.tifworkbench.jaxbbinding.FieldsDefinitions;
import com.ptc.tifworkbench.jaxbbinding.PhaseDefinition;
import com.ptc.tifworkbench.jaxbbinding.PhasesDefinition;
import com.ptc.tifworkbench.jaxbbinding.ReverseRelationship;
import com.ptc.tifworkbench.jaxbbinding.StoreToHistoryFrequencyType;
import com.ptc.tifworkbench.jaxbbinding.Suggestion;
import com.ptc.tifworkbench.jaxbbinding.Suggestions;
import com.ptc.tifworkbench.jaxbbinding.ValueDefinition;
import com.ptc.tifworkbench.jaxbbinding.ValuesDefinition;
import com.ptc.tifworkbench.model.StandardFields;
import com.ptc.tifworkbench.model.StandardPhases;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pbowden
 */
class FieldReader extends AdminObjectReader
{
    private FieldsDefinitions fields;
    
    public FieldReader(FieldsDefinitions fields, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.fields=fields;
    }
    
    @Override
    public void read()throws IntegrityException
    {
    	HashMap<String, FieldDefinition> visited = new HashMap<String, FieldDefinition>(); // fields we have visited
        List<String> ignore = StandardFields.getStandardFieldList();
        
	log("Reading fields.");
	reportStatus(0, "Reading field definitions.");
        Command cmd = new Command(Command.IM, "fields");
	Response resp;
        List<String> names = new ArrayList<String>();
        try 
        {
            resp = getApi().execute(cmd);
	    
            WorkItemIterator wkIt = resp.getWorkItems();
            while(wkIt.hasNext())
            {
                 WorkItem wk = wkIt.next();
                 String fieldName = wk.getId();
                 log("    Found: " + fieldName + (ignore.contains(fieldName)?" IGNORE":""));
                 names.add(fieldName);
            }
        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error reading list of fields", ex);
        }
        
        int count=0;
        int numFields=names.size();
	for(String fieldName : names)
	{
	    int prog = (100 * count++)/numFields;
	    reportStatus(prog, "Read field: " + fieldName);
            if (!visited.containsKey(fieldName) && !ignore.contains(fieldName)) 
            {   
                try 
                {
                    // if we have not already been to this field...
                      FieldDefinition fdef = getFactory().createFieldDefinition();
                      
                      fdef.setName(fieldName);
                      // One special case. We always edit the standard Shared Category field.
                      if("Shared Category".equals(fieldName))
                          fields.getEditField().add(fdef);
                      else
                          fields.getField().add(fdef);
                      readField(fdef, fieldName);
                      visited.put(fieldName, fdef); // record the fact that we've been to this field.
                      //if (!fld.equals(fieldName)) 
                      //{ // if we read another field as well (for relationships)...
                      //        visited.put(fld,null); // ...record that field too.
                      // }
                } catch (Exception ex) 
                {
                    log("Error reading field " + fieldName);
                    log(ex.getMessage());
                    // TODO: surface the errors to the UI.
                }
            }
	}
        // Join relationships in forward/backward pairs. Remove one of the pairs from the
        // list of definitions.
        fixupRelationships(visited);
    }
    
    /**
     * Given the Element representing a field, build the rest of the field structure.
     * @param typeEl
     * @return
     */
    protected void readField(FieldDefinition fdef, String fieldName) throws Exception
    {
    	//String returnField = new String(); - avoid side-effects
    	
    	log("Reading field " + fieldName);
        try 
        {
            Command cmd = new Command(Command.IM, "viewfield");
	    cmd.addSelection(fieldName);
	    Response resp = getApi().execute(cmd);
		
            WorkItem wk = resp.getWorkItem(fieldName);
            Field typeFld = wk.getField("type");

            // All the attribs that may be on a field.
            FieldType ftype = FieldType.fromValue(getSafeField("type", wk));
            log("Field type=" + ftype.value());
            
            fdef.setType(ftype);
            fdef.setDescription(getSafeField("description", wk));
            fdef.setDisplayName(getSafeField("displayName", wk));

            if(specified("editabilityRule", wk))
                fdef.setEditability(getSafeField("editabilityRule", wk));
            if(specified("relevanceRule", wk))
                fdef.setRelevance(getSafeField("relevanceRule", wk));
            if(specified("multivalued", wk))
                fdef.setMultivalued(getBooleanField("multivalued", wk));
            if(specified("maxLength", wk))
                fdef.setMaxLength(getIntegerField("maxLength", wk));
            if(specified("loggingText", wk))
                fdef.setLogging(getLoggingField("loggingText", wk));
            if(specified("displayRows", wk))
                fdef.setDisplayRows(getIntegerField("displayRows", wk));
            if(specified("default", wk))
                fdef.setDefault(getSafeField("default", wk));
            if(specified("min", wk))
                fdef.setMin(getSafeField("min", wk));
            if(specified("max", wk))
                fdef.setMax(getSafeField("max", wk)); 
            if(specified("richContent", wk))
                fdef.setRichContent(getBooleanField("richContent", wk));
            if(specified("defaultAttachmentField", wk))
                fdef.setDefaultAttachmentField(getSafeField("defaultAttachmentField", wk));
            if(specified("displayPattern", wk))
                fdef.setDisplayPattern(getSafeField("displayPattern", wk));
            if(specified("showDateTime", wk))
                fdef.setIncludeTime(getBooleanField("showDateTime", wk));
            if(specified("displayTrueAs", wk))
                fdef.setDisplayTrueAs(getSafeField("displayTrueAs", wk));
            if(specified("displayFalseAs", wk))
                fdef.setDisplayFalseAs(getSafeField("displayFalseAs", wk));
            if(specified("displayAsProgress", wk))
            	fdef.setDisplayAsProgress(getBooleanField("displayAsProgress", wk));
            if(specified("displayAsLink", wk))
                fdef.setDisplayAsLink(getBooleanField("displayAsLink", wk));
            if(specified("cycleDetection",wk))
                fdef.setCycleDetection(getBooleanField("cycleDetection", wk));
            if(specified("showTallRows",wk))
                fdef.setShowVariableHeightRows(getBooleanField("showTallRows", wk));
            if(specified("suggestions",wk))
                readSuggestions(fdef,wk);
            if(specified("paramSubstitution",wk))
                fdef.setSubstituteParameters(getBooleanField("paramSubstitution", wk));
            if(specified("displayAs",wk)){
                String val = getSafeField("displayAs",wk);
                DisplayAsType displayAs = (val.equals("Checkbox")) ? DisplayAsType.CHECKBOX : DisplayAsType.DROPDOWN;
                fdef.setDisplayAs(displayAs);
            }
            if(specified("displayStyle",wk)){
                String val = getSafeField("displayStyle",wk);
                DisplayType displayStyle = (val.equals("table")) ? DisplayType.TABLE : DisplayType.CSV;
                fdef.setDisplayType(displayStyle);
            }
            if(specified("storeToHistoryFrequency",wk)){
                String val = getSafeField("storeToHistoryFrequency",wk);
                StoreToHistoryFrequencyType frequencyType = StoreToHistoryFrequencyType.fromValue(val);
                fdef.setStoreToHistoryFrequency(frequencyType);
            }
          
            readComputation(fdef, wk);
            if (FieldType.PICK.equals(ftype)) 
            {
                ValuesDefinition vdefs = getFactory().createValuesDefinition();
                fdef.setValues(vdefs);
                readPickValues(vdefs, wk);
                log("Writing a pick field.");
            } 
            else if (FieldType.IBPL.equals(ftype)) 
            {
                BackingIssueReference bref = getFactory().createBackingIssueReference();
                bref.setField(getSafeField("backingTextFormat", wk));
                bref.setStates(getSafeField("backingStates", wk));
                bref.setType(getSafeField("backingType", wk));
                fdef.setBackingIssue(bref);
                log("Writing a IBPL field.");
            } 
            else if (FieldType.FVA.equals(ftype)) 
            {
                BackingIssueReference bref = getFactory().createBackingIssueReference();
                Field backedByFld = wk.getField("backedBy").getItem().getField("backedBy"); //sic
                String rel = backedByFld.getItem().getField("backingRelationship").getValueAsString();
                String fld = backedByFld.getItem().getId();
                String backedFieldType = backedByFld.getItem().getField("type").getValueAsString();
                
                bref.setRelationship(rel);
                bref.setField(fld);
                bref.setType(backedFieldType);
                fdef.setBackingIssue(bref);
                log("Writing a FVA field");
            } 
            else if (FieldType.QBR.equals(ftype)) 
            {
                fdef.setQuery(getSafeField("query", wk));
                fdef.setCorrelation(getSafeField("correlation", wk));
                log("Writing a QBR field");
            } 
            else if (FieldType.PHASE.equals(ftype)) 
            {
                PhasesDefinition pdefs = getFactory().createPhasesDefinition();
                fdef.setPhases(pdefs);
                log("Writing a phase field.");
                readPhaseValues(pdefs, wk);
            } 
            
            else if (FieldType.RELATIONSHIP.equals(ftype)) 
            {
                log("Writing a relationship field.");
                readBasicRelationshipFields(fdef, wk);
                
                ReverseRelationship rev = getFactory().createReverseRelationship();
                String pairedField = getSafeField("pairedField", wk);
                rev.setName(pairedField);
                fdef.setReverse(rev);
                
            } 
            else 
            {
                //returnField = typeFld.getString();
            }
        } catch(Exception e) 
        {
        	log("Failed to read field " + fieldName);
        	log("Exception message: " + e.getMessage());
        	throw new IntegrityException("Error reading field: " + fieldName, e);
        }
        //return returnField;
    }    
    
    protected void readSuggestions(final FieldDefinition fdef, final WorkItem wk){
        Suggestions fdefSuggestions = getFactory().createSuggestions();
        Field suggestionsField = wk.getField("suggestions");
        List<?> suggestions = suggestionsField.getList();
        for(Object value : suggestions){
            Suggestion suggestion = getFactory().createSuggestion();
            suggestion.setName(value.toString());
            fdefSuggestions.getSuggestion().add(suggestion);
        }
        fdef.setSuggestions(fdefSuggestions);
    }
    

    protected void readBasicRelationshipFields(FieldDefinition fdef, WorkItem wk)
    {
            fdef.setMultivalued(getBooleanField("isMultiValued", wk));
            fdef.setVariableHeightRows(false); // Not supported by the API yet.
            fdef.setDisplayRows(getIntegerField("displayRows", wk));
            if(getSafeField("defaultBrowseQuery", wk).length()>0)
                fdef.setQuery(getSafeField("defaultBrowseQuery", wk));
            fdef.setRelates(getListOfListAsString("allowedTypes", wk));
            fdef.setTrace(getBooleanField("trace", wk));

            List<String> cols = getStringList("defaultColumns", wk);
            if(cols.size() > 0)
            {
                ColumnsDefinition colsDef = getFactory().createColumnsDefinition();
                for(String colName : cols)                
                    colsDef.getColumn().add(colName);
                fdef.setDefaultColumns(colsDef);
            }
    }
    
    protected void fixupRelationships(HashMap<String, FieldDefinition>fmap)
    {
        List<FieldDefinition>fixed = new ArrayList<FieldDefinition>();
        List<FieldDefinition>todelete = new ArrayList<FieldDefinition>();
        for(FieldDefinition fdef : fields.getField()) 
        {
            if(fdef.getType() == FieldType.RELATIONSHIP) 
            {
                FieldDefinition pairedField = fmap.get(fdef.getReverse().getName());
                if(fixed.contains(pairedField)) // We've already processed this fields partner
                {
                    todelete.add(fdef);
                }
                else
                {
                    fdef.getReverse().setDisplay(pairedField.getDisplayName());
                    fdef.getReverse().setRelates(pairedField.getRelates());
                    fdef.getReverse().setMultivalued(pairedField.isMultivalued());
                    fdef.getReverse().setTrace(pairedField.isTrace());
                    fixed.add(fdef);
                }
            }
        }
        for(FieldDefinition delDef : todelete)
        {
            fields.getField().remove(delDef);
        }
        
    }
    
    protected void readComputation(FieldDefinition fdef, WorkItem wk)
    {
    	if(specified("computation", wk)) 
        {
    	    String val = wk.getField("computation").getString();
            val = val.replaceAll("\n", "");
            val = val.replaceAll("\r", "");
    	    if (val != null && val.trim().length() > 0) 
            { 
                ComputationDefinition comp = getFactory().createComputationDefinition();
                comp.setMode(getBooleanField("staticComputation", wk) ? "static" : "dynamic");
                comp.setStore(getSafeField("storeToHistoryFrequency", wk));
                comp.setValue(val);
                fdef.setComputation(comp);
    	    }
        }
    }
    
    protected void readPickValues(ValuesDefinition vdef, WorkItem wk) 
    {
    	List itList = wk.getField("picks").getList();
    	if (itList != null) 
        { 
	    Iterator iter = itList.iterator();
            while(iter.hasNext())
            {
                Item item = (Item)iter.next();
                String id = item.getId();
                if(id != null && id.trim().length()>0)
                {
	    	    ValueDefinition vd = getFactory().createValueDefinition();
                    id = escapeChar(id, ",");
	    	    id = escapeChar(id, ":");
	    	    id = escapeChar(id, ";"); // need to escape characters per 44896 --al 20100122
                    int index = getIntegerField("value", item);
                    vd.setIndex(index);
                    
                    vd.setValue(id);
                    vdef.getValue().add(vd);
                }
            }
        }
    }
    protected void readPhaseValues(PhasesDefinition pdef, WorkItem wk) 
    {
        List<String> ignore = StandardPhases.getStandardPhaseList();
    	
        List itList = wk.getField("phases").getList();
    	if (itList != null) 
        { 
	    Iterator iter = itList.iterator();
            while(iter.hasNext())
            {
                Item item = (Item)iter.next();
                String id = item.getId();
                if(ignore.contains(id)) continue;
                
                PhaseDefinition pd = getFactory().createPhaseDefinition();
                pd.setName(id);
                pdef.getPhase().add(pd);
                List states = item.getField("states").getList();
                StringBuilder buff = new StringBuilder();
                for(Object o : states)
                {
                    String state = o.toString();
                    buff.append(state).append(",");
                }
                buff.setLength(buff.length()-1);
                pd.setStates(buff.toString());
            }
        }
    }


}
