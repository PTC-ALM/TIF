// <b>Trace Status</b>
// <p>
// MKS ALM 2009 solution
// <p>
// Sets the value of the Trace Status picklist field.
// <p>
// The trigger should be set up as a rule-based pre-trigger, with rule type==node.
// <p>
// This trigger requires the following properties declared on the MKS Solution type:<br />
// <b>MKS.RQ.RelationshipTraceFieldName</b> The name of the "Is Related To" relationship.
// <b>MKS.RQ.TraceStatusFieldName</b> The name of the "Trace Status" pick list field.
//
// Copyright 2009 by MKS Inc. All rights reserved.
//

//Hardcoded fields
solutionName = "Requirements";

function append(appFound, appSuspect, appTitle)
{
	if (logging)
		eb.print("Appending " + appTitle + " for found=" + appFound + " and suspect=" + appSuspect);
	if (!appFound)
		return;
	if (started)
		pickListValue.append(" ");
	started = true;
	pickListValue.append(appTitle)
	if (appSuspect)
		pickListValue.append(" suspect");
}

function getPickListValue()
{
	var relTrcFieldName1 = sb.getSolutionPropertyValue(solutionName, "RelationshipTraceFieldName", false);
	var relTrcField = sb.getFieldBean(relTrcFieldName1);
	var relTrcFieldName2 = relTrcField.getPairedField();
	var relFound = false;
	var relSuspect = false;
	var upFound = false;
	var upSuspect = false;
	var downFound = false;
	var downSuspect = false;
	for (i=0; i<alltracesa.length; i++) {
		var fieldName = alltraces.get(alltracesa[i]);
		var srbs = delta.getNewRelationshipFieldBeans(fieldName);
		if (srbs.length>0) {
			var suspect = false;
			for (j=0; j<srbs.length && !suspect; j++)
				suspect = srbs[j].isSuspect();
			if (fieldName.equals(relTrcFieldName1) || fieldName.equals(relTrcFieldName2)) {
				relFound = true;
				if (suspect) relSuspect = true;
			} else if (alltracesa[i].isForwardRelationship()) {
				downFound = true;
				if (suspect) downSuspect = true;
			} else {
				upFound = true;
				if (suspect) upSuspect = true;
			}
		}
	}

	// The picklist values are:
	// none | [upstream] [suspect] [downstream] [suspect] [related] [suspect]
	// That's 27 values. Each should have its own icon.
	append(upFound, upSuspect, "upstream");
	append(downFound, downSuspect, "downstream");
	append(relFound, relSuspect, "related");
	if (!started)
		pickListValue = "none";
}

///START

eb = bsf.lookupBean("siEnvironmentBean");
rb = new java.util.PropertyResourceBundle(new java.io.FileReader(
	new java.io.File(eb.getScriptDirectory() + "/ALMRB.properties.js")));

sb = bsf.lookupBean("imServerBean");
delta = bsf.lookupBean("imIssueDeltaBean");

eb.setMessageCategory("MKSALM");
logging = eb.messageCheck();
if (logging)
	eb.print("TraceStatus trigger running for item " + delta.getID());

// Check for setup as 'pre' trigger.
if (eb.getEventName() != "Issue.changed.pre")
	eb.badConfiguration(rb.handleGetObject("ONLY_PRETRIGGER"));

deltaType = delta.getType();
tb = sb.getTypeBean(deltaType);
pickListFieldName = sb.getSolutionPropertyValue(solutionName, "TraceStatusFieldName", false);

if (!tb.isFieldVisibleToAnyone(pickListFieldName))
	// Perhaps this should be a configuration error.
	eb.exit("Field " + pickListFieldName + " not visible on type " + deltaType);

curval = delta.getFieldDisplayString(pickListFieldName);
found = (curval==null || curval.length()==0);
if (logging)
	eb.print("Current value of " + pickListFieldName + " is '" + curval +
		"', curval.length=" + curval.length() + ", found=" + found);

alltracesa = tb.getAllVisibleTraceFieldBeans();
// Map<bean,name>
alltraces = new java.util.HashMap(alltracesa.length);
for (i=0; i<alltracesa.length; i++)
	alltraces.put(alltracesa[i], alltracesa[i].getName());

fields = delta.getDeltaFieldNames();
updated = new java.util.HashSet();
for (i=0; i<fields.length && !found; i++)
	found = alltraces.containsValue(fields[i++]);

if (!found)
	eb.exit("Already initialized, no traces changed");

pickListValue = new java.lang.StringBuilder();
started = false;

getPickListValue();

if (logging)
	eb.print("Setting picklist value to " + pickListValue);

delta.setFieldValue(pickListFieldName, pickListValue);
