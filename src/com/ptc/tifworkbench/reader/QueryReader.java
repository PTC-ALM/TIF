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
import com.ptc.tifworkbench.jaxbbinding.ColumnsDefinition;
import com.ptc.tifworkbench.jaxbbinding.QueriesDefinition;
import com.ptc.tifworkbench.jaxbbinding.QueryDefinition;
import com.ptc.tifworkbench.jaxbbinding.YesNo;
import com.ptc.tifworkbench.model.StandardQueries;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pbowden
 */
public class QueryReader extends AdminObjectReader
{
    private QueriesDefinition queries;
    
    public QueryReader(QueriesDefinition queries, StatusReporter reporter) throws IntegrityExceptionEx
    {
        super(reporter);
        this.queries = queries;
    }
    
    @Override
    void read() throws IntegrityException 
    {
        List<String> ignore = StandardQueries.getStandardQueryList();
	log("Reading queries.");
        Command cmd = new Command(Command.IM, "queries");
        List<String> names = new ArrayList<String>();
        try 
        {
	    Response resp = getApi().execute(cmd);
	    WorkItemIterator wkIt = resp.getWorkItems();
	    reportStatus(0, "Read number of queries.");
	    while(wkIt.hasNext())
	    {
	    	WorkItem wk = wkIt.next();
	    	String queryName = wk.getId();
		log("    Read query: " + queryName + (ignore.contains(queryName) ? "IGNORE":""));
	        names.add(queryName);
	    }
        } catch (APIException ex) 
        {
            throw IntegrityExceptionEx.create("Error reading list of fields", ex);
        }
            
        int numFields = names.size();
        int count = 0;
        for(String queryName : names)
        {
            int prog = (100 * count++)/numFields;
            reportStatus(prog, "Read query: " + queryName);
            if(ignore.contains(queryName)) continue;

            QueryDefinition qdef = getFactory().createQueryDefinition();
            qdef.setName(queryName);
            queries.getQuery().add(qdef);
            try
            {
                readQuery(qdef);
            }catch(Exception ex)
            {
               log("Error reading query " + queryName);
               log(ex.getMessage());
               // TODO: surface the errors to the UI.
            }
        }
    }
    
    protected void readQuery(QueryDefinition qdef) throws Exception
    {
	log("Reading query " + qdef.getName());
        Command cmd = new Command(Command.IM, "viewquery");
        cmd.addSelection(qdef.getName());
        Response resp = getApi().execute(cmd);

        WorkItem wk = resp.getWorkItem(qdef.getName());
        if(getBooleanField("isAdmin", wk))
            qdef.setAdmin(YesNo.YES);
        else
            qdef.setAdmin(YesNo.NO);
            
        qdef.setDescription(getSafeField("description", wk));
        qdef.setDefinition(getSafeField("queryDefinition", wk));
        qdef.setShareGroups(getShareGroups(wk)); 
        qdef.setDefaultColumns(getDefaultColumns(wk));
    }
        
    protected ColumnsDefinition getDefaultColumns(WorkItem wk)
    {
        ColumnsDefinition colsdef = getFactory().createColumnsDefinition();
        ItemList fields = (ItemList)wk.getField("fields").getList();
        if(fields != null)
        {
            Iterator fieldIterator = fields.getItems();
            while(fieldIterator.hasNext())
            {
                Item item = (Item)fieldIterator.next();
                String fieldName = item.getId();
                log("    Read default column " + fieldName);
                colsdef.getColumn().add(fieldName);
            }
        }
        return colsdef;        
    }
    
}
