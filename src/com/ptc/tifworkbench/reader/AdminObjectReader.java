/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.WorkItem;
import com.ptc.tifworkbench.integrity.IntegrityApi;
import com.ptc.tifworkbench.integrity.IntegrityException;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import com.ptc.tifworkbench.jaxbbinding.LoggingType;
import com.ptc.tifworkbench.jaxbbinding.ObjectFactory;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pbowden
 */
public abstract class AdminObjectReader 
{
    private StatusReporter reporter;
    private ObjectFactory factory = new ObjectFactory();
    private IntegrityApi api;
    
    public AdminObjectReader(StatusReporter reporter) throws IntegrityExceptionEx
    {
        this.reporter=reporter;
        api = IntegrityApi.getInstance();
    }
    
    abstract void read() throws IntegrityException;
    
    public StatusReporter getReporter()
    {
        return reporter;
    }
    public IntegrityApi getApi()
    {
        return api;
    }
    public ObjectFactory getFactory()
    {
        return factory;
    }
    
    public void reportStatus(int prog, String mess)
    {
        reporter.reportStatus(prog, mess);
    }
    protected String getSafeField(String fld, WorkItem wk)
    {
        if(!wk.contains(fld)) return "";
        Field field = wk.getField(fld); 
     	if((field != null) && (field.getValueAsString() != null) && (field.getValueAsString().length() > 0))
     	{
            return field.getValueAsString();
     	}
        return "";
    }
    protected Boolean getBooleanField(String fld, WorkItem wk)
    {
        Field field = wk.getField(fld); 
     	if((field != null) && (field.getValueAsString() != null) && (field.getValueAsString().length() > 0))
     	{
            return field.getValueAsString().equalsIgnoreCase("true");
     	}
        return false;
    }
    
    protected int getIntegerField(String fld, WorkItem wk)
    {
        Field field = wk.getField(fld); 
     	if((field != null) && (field.getValueAsString() != null) && (field.getValueAsString().length() > 0))
     	{
            return Integer.parseInt(field.getValueAsString());
     	}
        return 0;
    }
    
    protected int getIntegerField(String fld, Item item)
    {
        Field field = item.getField(fld); 
     	if((field != null) && (field.getValueAsString() != null) && (field.getValueAsString().length() > 0))
     	{
            return Integer.parseInt(field.getValueAsString());
     	}
        return 0;
    }
    
    protected String getSafeField(String fld, Item item)
    {
        String value = item.getField(fld).getValueAsString();
        return value==null ? "" : value;
    }

    protected LoggingType getLoggingField(String fld, Item item)
    {
        Field field = item.getField(fld);
        if(field == null)
            return LoggingType.NONE;
        String value = field.getValueAsString();
        if(value == null)
            return LoggingType.NONE;
        if(LoggingType.MOST_RECENT_FIRST.value().equals(value))
            return LoggingType.MOST_RECENT_FIRST;
        if(LoggingType.MOST_RECENT_LAST.value().equals(value))
            return LoggingType.MOST_RECENT_LAST;
        
        return LoggingType.NONE;
    }

    /**
     * Lists separated by ; items in list separated by ,
     * @param fld
     * @param wk
     * @return 
     */
    protected String getListOfListAsString(String fld, WorkItem wk)
    {
        StringBuilder buff = new StringBuilder();
        if(!wk.contains(fld)) return "";
        List<Item> startRels = wk.getField(fld).getList();
        if(startRels == null) return "";
        // Loop over the start relationships.
        for(Item item : startRels)
        {
            buff.append(item.getId()).append(":");
            List<Item> endRels = item.getField("to").getList();
            for(Item endItem : endRels)
            {
                buff.append(endItem.getId()).append(",");
            }
            buff.setCharAt(buff.length()-1, ';');
        }
        buff.deleteCharAt(buff.length()-1);
        return buff.toString();
    }

    protected List<String> getStringList(String fld, WorkItem wk)
    {
        List<String>ret = new ArrayList<String>();
        if(!wk.contains(fld)) return ret;
        List<String> list = wk.getField(fld).getList();
        
        return list;
    }
    
    protected boolean specified(String fld, WorkItem wk)
    {
        if(!wk.contains(fld))
            return false;
        else
        {
            if(wk.getField(fld) != null && wk.getField(fld).getValueAsString() != null)
               return (wk.getField(fld).getValueAsString().length() > 0);
            return false;
        }
    }
        // since it's not trivial to escape characters with built-in java functions, here's one that'll do it. --al 20100124
    protected String escapeChar(String source, String search) 
    {
    	StringBuilder sb = null;
    	String[] toks = null;
    	if (source == null || source.trim().length() == 0 || search == null) 
            return source;
    	
    	toks = source.split(search);
    	if (toks.length > 1) 
        {
            sb = new StringBuilder();
            for (int x = 0; x < toks.length; x++) 
            {
                    sb.append(toks[x]);
                    if (x < toks.length - 1) { // make sure we don't append the escaped char to the end...
                            sb.append("\\");
                            sb.append(search);
                    }
            }
            return sb.toString();
    	}  // if there was only one token, then there's nothing to replace.
    	return source;
    }

    public String makeSafe(String str)
    {
        if(str == null)return "";
        else return str;
    }
    
    
    public void log(String mess)
    {
        api.log(mess);
    }
    
    
}
