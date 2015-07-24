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
import com.ptc.tifworkbench.jaxbbinding.CapabilitiesDefinition;
import com.ptc.tifworkbench.jaxbbinding.CapabilityDefinition;
import com.ptc.tifworkbench.jaxbbinding.StateDefinition;
import com.ptc.tifworkbench.jaxbbinding.StatesDefinition;
import com.ptc.tifworkbench.model.StandardStates;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pbowden
 */
public class StateReader extends AdminObjectReader 
{
    private StatesDefinition states;
    
    public StateReader(StatesDefinition states, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.states=states;
    }
    
    @Override
    public void read()throws IntegrityException
    {
        try
        {
            getApi().log("Reading state.");
            List<String> ignore = StandardStates.getStandardStateList();
            Command cmd = new Command(Command.IM, "states");
            Response resp = getApi().execute(cmd);

            List<String> names = new ArrayList<String>();
            WorkItemIterator wkIt = resp.getWorkItems();
            reportStatus(0, "Reading states");
            while(wkIt.hasNext())
            {
                WorkItem wk = wkIt.next();
                String stateName = wk.getId();
                names.add(stateName);
            }
            int numFields = names.size();
            int count = 0;
            for(String stateName : names)
            {
                int prog = (100 * count++)/numFields;
                reportStatus(prog, "Read state: " + stateName);
                if(ignore.contains(stateName)) continue;

                log("Reading state " + stateName);
                
                StateDefinition state = getFactory().createStateDefinition();
                state.setName(stateName);
                states.getState().add(state);
                readField(state, stateName);
            }
        }catch(APIException apiEx)
        {
            throw IntegrityExceptionEx.create("Error reading states", apiEx);
        }catch(Exception ex)
        {
            throw new IntegrityException("Error reading states", ex);
        }
    }

    protected void readField(StateDefinition state, String fieldName) throws Exception
    {
        Command cmd = new Command(Command.IM, "viewstate");
        cmd.addSelection(fieldName);
        Response resp = getApi().execute(cmd);

        WorkItem wk = resp.getWorkItem(fieldName);
        log("   set description");
        String desc = getSafeField("description", wk);
        state.setDescription(desc);
        state.setCapabilities(getCapabilities(wk));
    }
    
    protected CapabilitiesDefinition getCapabilities(WorkItem wk)
    {
        CapabilitiesDefinition colsdef = getFactory().createCapabilitiesDefinition();
        ItemList caps = (ItemList)wk.getField("capabilities").getList();
        if(caps != null)
        {
            Iterator capsIterator = caps.getItems();
            while(capsIterator.hasNext())
            {
                Item item = (Item)capsIterator.next();
                String cap = item.getId();
                log("    Read capability " + cap);
                CapabilityDefinition capdef = getFactory().createCapabilityDefinition();
                capdef.setCapability(cap);
                colsdef.getCapability().add(capdef);
            }
        }
        return colsdef;        
    }
    
    
}
