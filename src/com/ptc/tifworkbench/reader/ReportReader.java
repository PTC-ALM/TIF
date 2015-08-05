/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.reader;

import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.ptc.tifworkbench.integrity.IntegrityException;
import com.ptc.tifworkbench.integrity.IntegrityExceptionEx;
import com.ptc.tifworkbench.jaxbbinding.ReportDefinition;
import com.ptc.tifworkbench.jaxbbinding.ReportsDefinition;
import com.ptc.tifworkbench.jaxbbinding.YesNo;
import com.ptc.tifworkbench.worker.StatusReporter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schamaillard
 */
public class ReportReader extends AdminObjectReader {

    private ReportsDefinition reports;

    public ReportReader(ReportsDefinition reportsDefinition, StatusReporter reporter) throws IntegrityExceptionEx {
        super(reporter);
        this.reports = reportsDefinition;
    }

    @Override
    void read() throws IntegrityException {
        log("Reading reports");
        Command imReports = new Command(Command.IM, "reports");
        List<String> reportNames = new ArrayList<String>();
        try {
            Response response = getApi().execute(imReports);
            WorkItemIterator wkIt = response.getWorkItems();
            reportStatus(0, "Read number of reports");
            while (wkIt.hasNext()) {
                WorkItem wk = wkIt.next();
                String reportName = wk.getId();
                log("   Read report: " + reportName);
                reportNames.add(reportName);
            }
        } catch (APIException apie) {
            throw IntegrityExceptionEx.create("Error reading list of reports", apie);
        }
        int numReports = reportNames.size();
        int count = 0;
        for (String reportName : reportNames) {
            int prog = (100 * count++) / numReports;
            reportStatus(prog, "Read report: " + reportName);
            ReportDefinition rdef = getFactory().createReportDefinition();
            rdef.setName(reportName);
            reports.getReport().add(rdef);
            try {
                readReport(rdef);
            } catch (Exception e) {
                log("Error reading report " + reportName);
                log(e.getMessage());
            }
        }
    }

    protected void readReport(final ReportDefinition rdef) throws Exception {
        log("Reading report " + rdef.getName());
        Command imViewReport = new Command(Command.IM, "viewreport");
        imViewReport.addOption(new Option("showRecipeParams"));
        imViewReport.addSelection(rdef.getName());
        Response response = getApi().execute(imViewReport);
        WorkItem wk = response.getWorkItem(rdef.getName());

        rdef.setDescription(getSafeField("description", wk));
        rdef.setQuery(getSafeField("query", wk));
        rdef.setShareGroups(getShareGroups(wk));
        rdef.setRecipeParams(this.getRecipeParamsCLISpec(wk));
        if (getBooleanField("isAdmin", wk)) {
            rdef.setAdmin(YesNo.YES);
        } else {
            rdef.setAdmin(YesNo.NO);
        }
    }
    
    protected String getRecipeParamsCLISpec(final WorkItem wk) throws Exception {
        Field recipeParamsField = wk.getField("recipeParams");
        Item recipeParams = recipeParamsField.getItem();
        String cliSpec = recipeParams.getField("cliSpec").getValueAsString();
        if(cliSpec == null)
            cliSpec = new String("");
        return cliSpec;
    }

}
