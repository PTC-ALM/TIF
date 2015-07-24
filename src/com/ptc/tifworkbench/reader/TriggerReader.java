/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.mks.api.Command;
import com.mks.api.response.APIException;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.ptc.tifworkbench.integrity.IntegrityException;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import com.ptc.tifworkbench.jaxbbinding.TriggerDefinition;
import com.ptc.tifworkbench.jaxbbinding.TriggerType;
import com.ptc.tifworkbench.jaxbbinding.TriggersDefinition;
import com.ptc.tifworkbench.model.StandardTriggers;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pbowden
 */
public class TriggerReader extends AdminObjectReader
{
    private TriggersDefinition triggers;
    
    public TriggerReader(TriggersDefinition triggers, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.triggers=triggers;
    }
    
    @Override
    void read() throws IntegrityException 
    {
	log("Reading triggers.");
        List<String> ignore = StandardTriggers.getStandardTriggerList();
        
        Command cmd = new Command(Command.IM, "triggers");
        List<String> names = new ArrayList<String>();
        try 
        {
	    Response resp = getApi().execute(cmd);
	    WorkItemIterator wkIt = resp.getWorkItems();
	    reportStatus(0, "Read number of triggers.");
	    while(wkIt.hasNext())
	    {
	    	WorkItem wk = wkIt.next();
	    	String triggerName = wk.getId();
		log("    Read Trigger: " + triggerName + (ignore.contains(triggerName)?"IGNORE":""));
	        names.add(triggerName);
	    }
        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error reading list of fields", ex);
        }
            
        int numFields = names.size();
        int count = 0;
        for(String triggerName : names)
        {
            int prog = (100 * count++)/numFields;
            reportStatus(prog, "Read trigger: " + triggerName);
            if(ignore.contains(triggerName)) continue;
            
            TriggerDefinition tdef = getFactory().createTriggerDefinition();
            tdef.setName(triggerName);
            triggers.getTrigger().add(tdef);
            try
            {
                readTrigger(tdef);
            }catch(Exception ex)
            {
               log("Error reading trigger " + triggerName);
               log(ex.getMessage());
               // TODO: surface the errors to the UI.
            }
        }
    }
    
    protected void readTrigger(TriggerDefinition tdef) throws Exception
    {
	log("Reading trigger " + tdef.getName());
        Command cmd = new Command(Command.IM, "viewtrigger");
        cmd.addSelection(tdef.getName());
        Response resp = getApi().execute(cmd);

        WorkItem wk = resp.getWorkItem(tdef.getName());
        TriggerType ttype = TriggerType.fromValue(getSafeField("type", wk));
        log("Trigger type=" + ttype.value());
        tdef.setType(ttype);
        tdef.setDescription(getSafeField("description", wk));
        
        if(specified("runAs", wk))
           tdef.setRunAs(getSafeField("runAs", wk));
        if(specified("frequency", wk))
           tdef.setFrequency(getSafeField("frequency", wk));
        if(specified("query", wk))
           tdef.setQuery(getSafeField("query", wk));
        if(specified("script", wk))
           tdef.setScript(getSafeField("script", wk));
        if(specified("scriptTiming", wk))
           tdef.setScriptTiming(getSafeField("scriptTiming", wk));
        if(specified("rule", wk))
           tdef.setRule(ReaderUtils.stripQuotes(getSafeField("rule", wk)));
        if(specified("scriptParams", wk))
             tdef.setParams(getScriptParameters(wk));
        
        //getAssignments(wk, fieldEl);
    }
    
    private String getScriptParameters(WorkItem wk)
    {
        StringBuilder buff = new StringBuilder();
        ItemList wklist = (ItemList)wk.getField("scriptParams").getList();   
        for(Object o : wklist)
        {
            Item item = (Item)o;
            String name = item.getId();
            String value = item.getField("value").getValueAsString();
            buff.append(name).append("=").append(value).append(";");
        }
        return buff.toString();
    }
}
