<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate solution creation scripts from the solution XML.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:tif="http://www.ptc.com/integrity-solution" version="1.0">
                
<xsl:param name="is-server"/>
<xsl:param name="is-port"/>
<xsl:param name="is-user"/>
<xsl:param name="is-password"/>
<xsl:param name="is-root"/>
<xsl:param name="is-version"/>
<xsl:param name="env-root"/>

<xsl:param name="env-root-safe">
    <xsl:call-template name="escapeColon">
        <xsl:with-param name="cText" select="$env-root"/>
    </xsl:call-template>
</xsl:param>


<xsl:strip-space elements="description"/>
<xsl:strip-space elements="relates"/>

<xsl:template match="/">
    <xsl:apply-templates select="tif:im-solution"/>
</xsl:template>

<xsl:template match="tif:im-solution">
# Korn-shell script generated from TIF.
# 
# Class path for java utilities.
classpath="externalScripts/mksapi.jar;externalScripts/commons-io-2.4.jar;externalScripts/commons-cli-1.2.jar;externalScripts/commons-lang.jar;externalScripts/TifUtils.jar"

# Groups
<xsl:apply-templates select="tif:groups/tif:group"/>
# Users
<xsl:apply-templates select="tif:users/tif:user"/>
# States
<xsl:apply-templates select="tif:states/tif:state"/>

# Projects
<xsl:apply-templates select="tif:projects/tif:project"/>

# Create a temporary type to resolve FVAs
<xsl:call-template name="create-fva-temp"/>
# Create a temporary query to resolve QBRs
<xsl:call-template name="create-qbr-temp"/>

# Dynamic groups
<xsl:apply-templates select="tif:dynamic-groups/tif:dynamic-group"/>
<xsl:apply-templates select="tif:dynamic-groups/tif:edit-dynamic-group"/>

# Changes to standard fields
<xsl:apply-templates select="tif:fields/tif:standard-field"/>

# Fields
<xsl:apply-templates select="tif:fields/tif:field[not(@type='ibpl')]" mode="definition"/>
# Modify the reverse relationship fields and AFTER they have been created.
<xsl:apply-templates select="tif:fields/tif:field[@type='relationship']" mode="reverse-relationships"/>

# Edit existing fields
<xsl:apply-templates select="tif:fields/tif:edit-field"/>
<xsl:apply-templates select="tif:states/tif:edit-state"/>


#############################################################################
# Types
#############################################################################
<xsl:apply-templates select="tif:types/tif:type" mode="definitions"/>
# Issue backed pick lists
<xsl:apply-templates select="tif:fields/tif:field[@type='ibpl']" mode="ibpl"/>

# State transitions
<xsl:apply-templates select="tif:types/tif:type/tif:states" mode="transitions"/>
# Visible fields
<xsl:apply-templates select="tif:types/tif:type" mode="visible"/>

# Field value attributes - require ibpls to be created and appropriate fields visible on the types.
<xsl:apply-templates select="tif:fields/tif:field[@type='fva']"  mode="fva"/>

# Document model settings.
<xsl:apply-templates select="tif:types/tif:type/tif:document-model[@role='shareditem']"/>
<xsl:apply-templates select="tif:types/tif:type/tif:document-model[@role='node']"/>
<xsl:apply-templates select="tif:types/tif:type/tif:document-model[@role='segment']"/>

# Test management settings.
<xsl:apply-templates select="tif:types/tif:type/tif:test-management[@role='testSuite']"/>
<xsl:apply-templates select="tif:types/tif:type/tif:test-management[@role='testCase']"/>
<xsl:apply-templates select="tif:types/tif:type/tif:test-management[@role='testStep']"/>
<xsl:apply-templates select="tif:types/tif:type/tif:test-management[@role='testSession']"/>

## Field relevance rules.
<xsl:apply-templates select="tif:fields/tif:field[tif:relevance or tif:editability]" mode="options"/>
# Mandatory fields
<xsl:apply-templates select="tif:types/tif:type/tif:states[tif:state/tif:mandatory]" mode="mandatory-fields"/>
# Constraints
<xsl:apply-templates select="tif:types/tif:type/tif:constraints" />
# Properties.
<xsl:apply-templates select="tif:types/tif:type/tif:properties"/>
# Attributes.
<xsl:apply-templates select="tif:types/tif:type" mode="attributes" />


# Set the allowed types for the relationships AFTER the relevant types have been created..
<xsl:apply-templates select="tif:fields/tif:field[@type='relationship']"      mode="allowed-types"/>
<xsl:apply-templates select="tif:fields/tif:edit-field[@type='relationship']" mode="allowed-types"/>

# Apply the editability and relevance and default field overrides for types.
<xsl:apply-templates select="tif:types/tif:type/tif:fields/tif:field/tif:editability" mode="type-override"/>
<xsl:apply-templates select="tif:types/tif:type/tif:fields/tif:field/tif:relevance"   mode="type-override"/>
<xsl:apply-templates select="tif:types/tif:type/tif:fields/tif:field/tif:default"     mode="type-override"/>
<xsl:apply-templates select="tif:types/tif:type/tif:fields/tif:field/tif:description" mode="type-override"/>

#############################################################################
# Editing Existing Types
# Visible fields
<xsl:apply-templates select="tif:types/tif:edit-type" mode="visible"/>
<xsl:apply-templates select="tif:types/tif:edit-type/tif:states" mode="transitions"/>
<xsl:apply-templates select="tif:types/tif:edit-type/tif:states[tif:state/tif:mandatory]" mode="mandatory-fields"/>

# Set up the trace relationships in the document model.
<xsl:apply-templates select="tif:types/tif:type/tif:fields/tif:field/tif:traces"/>


#############################################################################
# Queries
#############################################################################
<xsl:apply-templates select="tif:queries/tif:query"/>

# Query backed relationships - can now set the actual queries and correlations.
<xsl:apply-templates select="tif:fields/tif:field[@type='qbr']" mode="qbr"/>
# Relationships with default queries and/or columns. Can now set this.
<xsl:apply-templates select="tif:fields/tif:field[@type='relationship' and (tif:query or tif:default-columns)]" mode="default-query"/>
<xsl:apply-templates select="tif:fields/tif:field[@type='relationship' and (tif:query or tif:default-columns)]/tif:reverse" mode="default-query"/>
# Calculations that may depend on other fields.
<xsl:apply-templates select="tif:fields/tif:field/tif:computation"/>

# Reports
<xsl:apply-templates select="tif:reports/tif:report"/>

# Charts
<xsl:apply-templates select="tif:charts/tif:chart"/>

# Dashboard
<xsl:apply-templates select="tif:dashboards/tif:dashboard"/>

# Triggers
<xsl:apply-templates select="tif:triggers/tif:trigger"/>
<xsl:apply-templates select="tif:triggers/tif:field-rule-trigger"/>



# END
</xsl:template>


<!-- **************** GROUP DEFINITIONS ******************-->
<xsl:template match="tif:group">
    <xsl:text>im creategroup </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>--name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** USER DEFINITIONS ******************-->
<xsl:template match="tif:user">
    <xsl:text>im createuser</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@full-name">
        <xsl:text> --fullName=&quot;</xsl:text><xsl:value-of select="@full-name"/><xsl:text>&quot;</xsl:text>
    </xsl:if>    
    <xsl:if test="@email">
        <xsl:text> --email=&quot;</xsl:text><xsl:value-of select="@email"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="@description">
        <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="@description"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** STATE DEFINITIONS ******************-->
<xsl:template match="tif:state">
    <xsl:if test="not(@name='Unspecified')">
        <xsl:text>im createstate </xsl:text>
        <xsl:call-template name="common-opts"/>
        <xsl:text>--name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
        <xsl:if test="not(string-length(@icon)=0)">
            <xsl:text> --image=&quot;</xsl:text><xsl:value-of select="$env-root"/><xsl:text>/images/</xsl:text><xsl:value-of select="@icon"/><xsl:text>&quot;</xsl:text>
        </xsl:if>
        <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
        <xsl:if test="tif:capabilities">
            <xsl:text> --capabilities=&quot;</xsl:text>
            <xsl:for-each select="tif:capabilities/tif:capability">
                <xsl:choose>
                    <xsl:when test="text()='timeTracking'">
                        <xsl:text>MKSIM:TimeTracking,</xsl:text>
                    </xsl:when>
                    <xsl:when test="text()='openChangePackages'">
                        <xsl:text>MKSSI:OpenChangePackages,</xsl:text>
                    </xsl:when>
                    <xsl:when test="text()='changePackagesUndeReview'">
                        <xsl:text>MKSSI:ChangePackagesUnderReview,</xsl:text>
                    </xsl:when>
                    <xsl:when test="text()='createTestResults'">
                        <xsl:text>MKSTM:ModifyTestResult,</xsl:text>
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>
            <xsl:text>&quot;</xsl:text>
        </xsl:if>
        
        <xsl:call-template name="cmd-end"/>
    </xsl:if>
</xsl:template>

<!-- **************** PROJECT DEFINITIONS ******************-->
<xsl:template match="tif:project">
    <xsl:text>im createproject </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>--name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@parent">
        <xsl:text> --parent=&quot;</xsl:text><xsl:value-of select="@parent"/><xsl:text>&quot; </xsl:text>
    </xsl:if>
    <xsl:if test="@openImage">
        <xsl:text> --openImage=&quot;</xsl:text><xsl:value-of select="$env-root"/><xsl:text>/images/</xsl:text><xsl:value-of select="@openImage"/><xsl:text>&quot; </xsl:text>
    </xsl:if>
    <xsl:if test="@closedImage">
        <xsl:text> --closedImage=&quot;</xsl:text><xsl:value-of select="$env-root"/><xsl:text>/images/</xsl:text><xsl:value-of select="@closedImage"/><xsl:text>&quot; </xsl:text>
    </xsl:if>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(@description)"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --permittedGroups=&quot;</xsl:text><xsl:value-of select="@permittedGroups"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template match="tif:update-project">
    <xsl:text>im editproject </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(@description)"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --permittedGroups=&quot;</xsl:text><xsl:value-of select="@permittedGroups"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** DYNAMIC GROUP DEFINITIONS ******************-->
<xsl:template match="tif:dynamic-group">
    <xsl:text>im createdynamicgroup </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>--name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --membership=&quot;</xsl:text>
    
    <xsl:for-each select="tif:project-membership">
        <xsl:value-of select="@name"/><xsl:text>=</xsl:text>
        <xsl:call-template name="dg-users"/>
        <xsl:call-template name="dg-groups"/>
        <xsl:text>;</xsl:text>
    </xsl:for-each>
    
    <xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>


<!-- **************** EDIT EXISTING DYNAMIC GROUPS ******************-->
<xsl:template match="tif:edit-dynamic-group">
    <xsl:text>im editdynamicgroup </xsl:text>
    <xsl:call-template name="common-opts"/>

    <xsl:text> --membership=&quot;</xsl:text>
    <xsl:for-each select="tif:project-membership">
        <xsl:value-of select="@name"/><xsl:text>=</xsl:text>
        <xsl:call-template name="dg-users"/>
        <xsl:call-template name="dg-groups"/>
        <xsl:text>;</xsl:text>
    </xsl:for-each>
    
    <xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>


<xsl:template name="dg-users">
    <xsl:if test="tif:dg-user">
        <xsl:text>u=</xsl:text>
        <xsl:for-each select="tif:dg-user">
            <xsl:value-of select="@name"/>
            <xsl:if test="position() != (last())">
                <xsl:text>,</xsl:text>
            </xsl:if>
        </xsl:for-each>
   </xsl:if>     
</xsl:template>

<xsl:template name="dg-groups">
    <xsl:if test="tif:dg-group">
        <xsl:text>g=</xsl:text>
        <xsl:for-each select="tif:dg-group">
            <xsl:value-of select="@name"/>
            <xsl:if test="position() != (last())">
                <xsl:text>,</xsl:text>
            </xsl:if>
        </xsl:for-each>
   </xsl:if>     
</xsl:template>


<!-- **************** FIELD DEFINITIONS ******************-->
<xsl:template match="tif:field" mode="definition">
    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --type=&quot;</xsl:text><xsl:value-of select="@type"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="tif:default">
        <xsl:text> --default=&quot;</xsl:text><xsl:value-of select="tif:default/text()"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="@displayPattern">
        <xsl:text> --displayPattern=&quot;</xsl:text><xsl:value-of select="@displayPattern"/><xsl:text>&quot;</xsl:text>
    </xsl:if>

    <!-- Do field type specific things. -->
    <xsl:choose>
        <xsl:when test="@type='pick'">
            <xsl:text> --picks=</xsl:text>
            <xsl:for-each select="tif:values/tif:value">
                <xsl:choose>
                    <xsl:when test="@index">
                        <xsl:call-template name="pick-value-offset"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="pick-value"/>
                    </xsl:otherwise>
                </xsl:choose>    
            </xsl:for-each>
            <xsl:if test="@multivalued='true'">
                <xsl:text> --multiValued</xsl:text>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='siproject'">
        </xsl:when>
		
		<xsl:when test="@type='group'">
		    <xsl:if test="@multivalued='true'">
                <xsl:text> --multiValued</xsl:text>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='logical'">
            <xsl:if test="tif:computation">
                <xsl:text> --storeToHistoryFrequency=</xsl:text><xsl:value-of select="tif:computation/@store"/>
				<xsl:text> --displayTrueAs=</xsl:text><xsl:value-of select="@displayTrueAs"/>
				<xsl:text> --displayFalseAs=</xsl:text><xsl:value-of select="@displayFalseAs"/>
                <xsl:text> --computation=&quot;true&quot;</xsl:text>
				<xsl:if test="@displayAs='dropdown'">
					<xsl:text> --displayAs=default</xsl:text>
				</xsl:if>    
				<xsl:if test="@displayAs='checkbox'">
					<xsl:text> --displayAs=checkbox</xsl:text>
				</xsl:if>  
				<xsl:if test="tif:computation/@mode='static'">
                    <xsl:text> --staticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="tif:computation/@mode='dynamic'">
                    <xsl:text> --nostaticComputation </xsl:text>
                </xsl:if>    
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='range'">
            <xsl:text> --ranges=</xsl:text>
            <xsl:for-each select="tif:ranges/tif:range">
                <xsl:call-template name="range-value"/>
            </xsl:for-each>
            <xsl:text> --associatedField=&quot;</xsl:text><xsl:value-of select="tif:ranges/@associated-field"/><xsl:text>&quot;</xsl:text>
        </xsl:when>

        <xsl:when test="@type='fva'">
            <xsl:choose>
                <xsl:when test="tif:backing-issue/@type='date'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.Created Date&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='shorttext'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.Summary&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='integer'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.FVA-temp-integer&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='logical'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.FVA-temp-logical&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='pick'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.FVA-temp-pick&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='longtext'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.FVA-temp-longtext&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='attachment'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.FVA-temp-attachment&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='relationship'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.FVA-temp-relationship&quot;</xsl:text>
                </xsl:when>
                <xsl:when test="tif:backing-issue/@type='siproject'">
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.FVA-temp-siproject&quot;</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text> --backedBy=&quot;FVA-temp-ibpl.Assigned User&quot;</xsl:text>
                </xsl:otherwise>
            </xsl:choose>    
            <xsl:if test="@display-rows">
                <xsl:text> --displayRows=</xsl:text><xsl:value-of select="@display-rows"/>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='date'">
            <xsl:if test="@showDateTime='true'">
                <xsl:text> --showDateTime</xsl:text>
            </xsl:if>
            <xsl:if test="@min">
                <xsl:text> --min=</xsl:text><xsl:value-of select="@min"/>
            </xsl:if>
            <xsl:if test="@max">
                <xsl:text> --max=</xsl:text><xsl:value-of select="@max"/>
            </xsl:if>
            <xsl:if test="tif:computation">
                <xsl:text> --storeToHistoryFrequency=</xsl:text><xsl:value-of select="tif:computation/@store"/>
                <xsl:text> --computation=&quot;now()&quot;</xsl:text>
                <xsl:if test="tif:computation/@mode='static'">
                    <xsl:text> --staticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="tif:computation/@mode='dynamic'">
                    <xsl:text> --nostaticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="tif:computation/@displayPattern">
                    <xsl:text> --displayPattern=&quot;</xsl:text><xsl:value-of select="tif:computation/@displayPattern"/><xsl:text>&quot;</xsl:text>
                </xsl:if>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='user'">
            <xsl:if test="@multivalued='true'">
                <xsl:text> --multiValued</xsl:text>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='integer'">
            <xsl:if test="@min">
                <xsl:text> --min=</xsl:text><xsl:value-of select="@min"/>
            </xsl:if>
            <xsl:if test="@max">
                <xsl:text> --max=</xsl:text><xsl:value-of select="@max"/>
            </xsl:if>
            <xsl:if test="@display='progress'">
                <xsl:text> --displayAsProgress</xsl:text>
            </xsl:if>
            <xsl:if test="tif:computation">
                <xsl:text> --storeToHistoryFrequency=</xsl:text><xsl:value-of select="tif:computation/@store"/>
                <xsl:text> --computation=&quot;1&quot;</xsl:text>
                <xsl:if test="tif:computation/@mode='static'">
                    <xsl:text> --staticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="tif:computation/@mode='dynamic'">
                    <xsl:text> --nostaticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="tif:computation/@displayPattern">
                    <xsl:text> --displayPattern=&quot;</xsl:text><xsl:value-of select="tif:computation/@displayPattern"/><xsl:text>&quot;</xsl:text>
                </xsl:if>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='float'">
            <xsl:if test="@min">
                <xsl:text> --min=</xsl:text><xsl:value-of select="@min"/>
            </xsl:if>
            <xsl:if test="@max">
                <xsl:text> --max=</xsl:text><xsl:value-of select="@max"/>
            </xsl:if>
            <xsl:if test="tif:computation">
                <xsl:text> --storeToHistoryFrequency=</xsl:text><xsl:value-of select="tif:computation/@store"/>
                <xsl:text> --computation=&quot;1.0&quot;</xsl:text>
                <xsl:if test="tif:computation/@mode='static'">
                    <xsl:text> --staticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="tif:computation/@mode='dynamic'">
                    <xsl:text> --nostaticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="tif:computation/@displayPattern">
                    <xsl:text> --displayPattern=&quot;</xsl:text><xsl:value-of select="tif:computation/@displayPattern"/><xsl:text>&quot;</xsl:text>
                </xsl:if>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='longtext'">
            <xsl:if test="@logging">
                <xsl:text> --loggingText=</xsl:text><xsl:value-of select="@logging"/>
            </xsl:if>
            <xsl:if test="@max-length">
                <xsl:text> --maxLength=</xsl:text><xsl:value-of select="@max-length"/>
            </xsl:if>
            <xsl:if test="@display-rows">
                <xsl:text> --displayRows=</xsl:text><xsl:value-of select="@display-rows"/>
            </xsl:if>
            <xsl:if test="@rich-content='true'">
                <xsl:text> --richContent</xsl:text>
            </xsl:if>
            <xsl:if test="@rich-content='false'">
                <xsl:text> --noRichContent</xsl:text>
            </xsl:if>
			<xsl:if test="@substituteParameters='true'">
                <xsl:text> --substituteParams</xsl:text>
            </xsl:if>
			<xsl:if test="@substituteParameters='false'">
                <xsl:text> --nosubstituteParams</xsl:text>
            </xsl:if>
			<xsl:if test="@text-index='true'">
                <xsl:text> --textIndex</xsl:text>
            </xsl:if>
			<xsl:if test="@text-index='false'">
                <xsl:text> --notextIndex</xsl:text>
            </xsl:if>
            <xsl:if test="@defaultAttachmentField">
                <xsl:text> --defaultAttachmentField=&quot;</xsl:text><xsl:value-of select="@defaultAttachmentField"/><xsl:text>&quot; </xsl:text>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='relationship'">
            <xsl:text> --displayStyle=</xsl:text><xsl:value-of select="@displayType"/>
			<xsl:if test="@trace='true'">
                <xsl:text> --trace</xsl:text>
            </xsl:if>
            <xsl:if test="@display-rows">
                <xsl:text> --displayRows=</xsl:text><xsl:value-of select="@display-rows"/>
            </xsl:if>
            <xsl:if test="@multivalued='true'">
                <xsl:text> --multiValued</xsl:text>
            </xsl:if>
            <xsl:if test="@showVariableHeightRows='true'">
                <xsl:text> --showTallRows</xsl:text>
            </xsl:if>
			<xsl:if test="@showVariableHeightRows='false'">
				<xsl:text> --noshowTallRows</xsl:text>
			</xsl:if>
			<xsl:if test="@cycleDetection='true'">
                <xsl:text> --cycleDetection</xsl:text>
            </xsl:if>
			<xsl:if test="@cycleDetection='false'">
				<xsl:text> --nocycleDetection</xsl:text>
			</xsl:if>
        </xsl:when>

        <xsl:when test="@type='qbr'">
            <xsl:text> --query=&quot;QbrDummy&quot;</xsl:text>
            <xsl:text> --correlation=&quot;&quot;</xsl:text>
			<xsl:text> --displayStyle=</xsl:text><xsl:value-of select="@displayType"/>
			<xsl:text> --displayRows=</xsl:text><xsl:value-of select="@display-rows"/>
			<xsl:if test="@showVariableHeightRows='true'">
				<xsl:text> --showTallRows</xsl:text>
			</xsl:if>
			<xsl:if test="@showVariableHeightRows='false'">
				<xsl:text> --noshowTallRows</xsl:text>
			</xsl:if>
        </xsl:when>

        <xsl:when test="@type='phase'">
            <xsl:text> --phases=&quot;</xsl:text>
            <xsl:for-each select="tif:phases/tif:phase">
                <xsl:value-of select="@name"/><xsl:text>:</xsl:text><xsl:value-of select="@states"/><xsl:text>;</xsl:text>
            </xsl:for-each>
            <xsl:text>&quot;</xsl:text>
        </xsl:when>


        <xsl:when test="@type='shorttext'">
            <xsl:if test="@max-length">
                <xsl:text> --maxLength=</xsl:text><xsl:value-of select="@max-length"/>
            </xsl:if>
			<xsl:if test="@substituteParameters='true'">
                <xsl:text> --substituteParams</xsl:text>
            </xsl:if>
			<xsl:if test="@substituteParameters='false'">
                <xsl:text> --nosubstituteParams</xsl:text>
            </xsl:if>
            <xsl:if test="tif:suggestion">
                <xsl:text> --suggestions=&quot;</xsl:text>
                    <xsl:for-each select="tif:suggestion">
                        <xsl:value-of select="text()"/>
                        <xsl:if test="position() != (last())">
                            <xsl:text>, </xsl:text>
                        </xsl:if>
                    </xsl:for-each>
                <xsl:text>&quot;</xsl:text>
            </xsl:if>
        </xsl:when>
    </xsl:choose>    

    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@display-name">
       <xsl:text> --displayName=&quot;</xsl:text><xsl:value-of select="@display-name"/><xsl:text>&quot;</xsl:text>
    </xsl:if>   
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** IBPL FIELD DEFINITIONS ******************-->
<xsl:template match="tif:field" mode="ibpl">
    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --type=ibpl</xsl:text>

    <xsl:text> --backingType=&quot;</xsl:text><xsl:value-of select="tif:backing-issue/@type"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --backingTextField=&quot;</xsl:text><xsl:value-of select="tif:backing-issue/@field"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --backingStates=&quot;</xsl:text><xsl:value-of select="tif:backing-issue/@states"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@multivalued='true'">
       <xsl:text> --multiValued</xsl:text>
    </xsl:if>
    <xsl:if test="@displayAsLink='true'">
       <xsl:text> --displayAsLink</xsl:text>
    </xsl:if>
	<xsl:if test="@displayAsLink='false'">
       <xsl:text> --nodisplayAsLink</xsl:text>
    </xsl:if>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** QBR FIELD DEFINITIONS ******************-->
<xsl:template match="tif:field" mode="qbr">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --query=&quot;</xsl:text><xsl:value-of select="tif:query/text()"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --correlation=&quot;</xsl:text><xsl:value-of select="tif:correlation/text()"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** RELATIONSHIP DEFAULT QUERY. ******************-->
<xsl:template match="tif:field | tif:reverse" mode="default-query">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --defaultBrowseQuery=&quot;</xsl:text><xsl:value-of select="tif:query/text()"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="tif:default-columns">
        <xsl:text> --defaultColumns=&quot;</xsl:text>
        <xsl:call-template name="columnList">
           <xsl:with-param name="node" select="tif:default-columns" />
        </xsl:call-template>
            <xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template name="columnList">
    <xsl:param name="node"/>
    <xsl:for-each select="$node/tif:column">
        <xsl:value-of select="text()"/>
        <xsl:if test="position() != (last())">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:for-each>
</xsl:template>


<!-- **************** FVA FIELD DEFINITIONS ******************-->
<xsl:template match="tif:field" mode="fva">
    <xsl:text>im editfield</xsl:text>
    <xsl:text> --backedBy=&quot;</xsl:text><xsl:value-of select="tif:backing-issue/@relationship"/><xsl:text>.</xsl:text><xsl:value-of select="tif:backing-issue/@field"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** CALCULATIONS FOR CALCULATED FIELDS. ******************-->
<xsl:template match="tif:computation">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --computation=&quot;</xsl:text><xsl:call-template name="escapeQuote"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>


<!-- **************** FIELD DEFINITIONS ******************-->
<xsl:template match="tif:edit-field">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="default">
        <xsl:text> --default=&quot;</xsl:text><xsl:value-of select="tif:default/text()"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="editability">
        <xsl:text> --editabilityRule=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:editability/text())"/><xsl:text>&quot;</xsl:text>
    </xsl:if>

    <!-- Do field type specific things. -->
    <xsl:choose>
        
        <xsl:when test="@type='shared-category'">
            <xsl:text> --picks=&quot;</xsl:text>
            <xsl:for-each select="tif:values/tif:value">
                <xsl:choose>
                    <xsl:when test="@icon">
                        <xsl:value-of select="text()"/>
                        <xsl:text>:</xsl:text><xsl:value-of select="@index"/>
                        <xsl:text>:</xsl:text><xsl:value-of select="$env-root-safe"/><xsl:text>/images/</xsl:text><xsl:value-of select="@icon"/>
                        <xsl:text>:</xsl:text><xsl:value-of select="@class"/><xsl:text>,</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="text()"/>
                        <xsl:text>:</xsl:text><xsl:value-of select="@index"/>
                        <xsl:text>:none:</xsl:text><xsl:value-of select="@class"/><xsl:text>,</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            <xsl:text>&quot;</xsl:text>
        </xsl:when>

        <xsl:when test="@type='phase'">
             <xsl:text> --editEntry=&quot;</xsl:text>
             <xsl:value-of select="tif:phase/@name"/><xsl:text>=</xsl:text><xsl:value-of select="tif:phase/@states"/><xsl:text>&quot;</xsl:text>
         </xsl:when>

        <xsl:when test="@type='pick'">
            <xsl:text> --picks=</xsl:text>
            <xsl:for-each select="tif:values/tif:value">
                <xsl:call-template name="pick-value-offset"/>
            </xsl:for-each>
            <xsl:if test="@multivalued='true'">
                <xsl:text> --multiValued</xsl:text>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='range'">
            <xsl:text> --ranges=</xsl:text>
            <xsl:for-each select="tif:ranges/tif:range">
                <xsl:call-template name="range-value-offset"/>
            </xsl:for-each>
        </xsl:when>

        <xsl:when test="@type='integer'">
            <xsl:if test="@min">
                <xsl:text> --min=</xsl:text><xsl:value-of select="@min"/>
            </xsl:if>
            <xsl:if test="@max">
                <xsl:text> --max=</xsl:text><xsl:value-of select="@max"/>
            </xsl:if>
            <xsl:if test="@display='progress'">
                <xsl:text> --displayAsProgress</xsl:text>
            </xsl:if>
            <xsl:if test="computation">
                <xsl:text> --storeToHistoryFrequency=</xsl:text><xsl:value-of select="@store"/>
                <xsl:text> --computation=&quot;</xsl:text><xsl:value-of select="tif:computation/text()"/><xsl:text>&quot;</xsl:text>
                <xsl:if test="@mode='static'">
                    <xsl:text> --staticComputation </xsl:text>
                </xsl:if>    
                <xsl:if test="@mode='dynamic'">
                    <xsl:text> --nostaticComputation </xsl:text>
                </xsl:if>    
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='longtext'">
            <xsl:if test="@logging">
                <xsl:text> --loggingText=</xsl:text><xsl:value-of select="@logging"/>
            </xsl:if>
            <xsl:if test="@max-length">
                <xsl:text> --maxLength=</xsl:text><xsl:value-of select="@max-length"/>
            </xsl:if>
        </xsl:when>

        <xsl:when test="@type='shorttext'">
            <xsl:if test="@max-length">
                <xsl:text> --maxLength=</xsl:text><xsl:value-of select="@max-length"/>
            </xsl:if>
            <xsl:if test="tif:suggestion">
                <xsl:text> --suggestions=&quot;</xsl:text>
                    <xsl:for-each select="tif:suggestion">
                        <xsl:value-of select="text()"/>
                        <xsl:if test="position() != (last())">
                            <xsl:text>, </xsl:text>
                        </xsl:if>
                    </xsl:for-each>
                <xsl:text>&quot;</xsl:text>
            </xsl:if>
        </xsl:when>
    </xsl:choose>    

    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>



<xsl:template match="tif:standard-field">
    <xsl:text>im editfield</xsl:text>
    <xsl:if test="@description">
        <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="new-name">
        <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="tif:new-name/text()"/><xsl:text>&quot;</xsl:text>
        <xsl:text> --displayName=&quot;</xsl:text><xsl:value-of select="tif:new-name/text()"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:call-template name="common-opts"/>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- Generate a list of ranges of the form ranges=text:value;value:image, -->
<xsl:template name="range-value">
    <xsl:choose>
        <xsl:when test="@icon">
            <xsl:text>&quot;</xsl:text><xsl:value-of select="tif:value/text()"/><xsl:text>:</xsl:text><xsl:value-of select="@lower"/><xsl:text>;</xsl:text><xsl:value-of select="@upper"/><xsl:text>:../images/</xsl:text><xsl:value-of select="@icon"/><xsl:text>&quot;</xsl:text><xsl:text>,</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>&quot;</xsl:text><xsl:value-of select="tif:value/text()"/><xsl:text>:</xsl:text><xsl:value-of select="@lower"/><xsl:text>;</xsl:text><xsl:value-of select="@upper"/><xsl:text>&quot;</xsl:text><xsl:text>,</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template name="range-value-offset">
    <xsl:choose>
        <xsl:when test="@icon">
            <xsl:text>&quot;</xsl:text><xsl:value-of select="tif:value/text()"/><xsl:text>:</xsl:text><xsl:value-of select="@lower"/><xsl:text>;</xsl:text><xsl:value-of select="@upper"/><xsl:value-of select="@icon"/><xsl:text>&quot;</xsl:text><xsl:text>,</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>&quot;</xsl:text><xsl:value-of select="tif:value/text()"/><xsl:text>:</xsl:text><xsl:value-of select="@lower"/><xsl:text>;</xsl:text><xsl:value-of select="@upper"/><xsl:text>&quot;</xsl:text><xsl:text>,</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>


<!-- Generate a list of picks of the form picks=text:value:image,text:value:image, -->
<xsl:template name="pick-value">
    <xsl:choose>
        <xsl:when test="@icon">
            <xsl:text>&quot;</xsl:text><xsl:value-of select="text()"/><xsl:text>&quot;:</xsl:text><xsl:value-of select="position()"/><xsl:text>:&quot;</xsl:text><xsl:value-of select="$env-root-safe"/><xsl:text>/images/</xsl:text><xsl:value-of select="@icon"/><xsl:text>&quot;,</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>&quot;</xsl:text><xsl:value-of select="text()"/><xsl:text>&quot;:</xsl:text><xsl:value-of select="position()"/><xsl:text>,</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template name="pick-value-offset">
    <xsl:choose>
        <xsl:when test="@icon">
            <xsl:text>&quot;</xsl:text><xsl:value-of select="text()"/><xsl:text>&quot;:</xsl:text><xsl:value-of select="@index"/><xsl:text>:&quot;</xsl:text><xsl:value-of select="$env-root-safe"/><xsl:text>/images/</xsl:text><xsl:value-of select="@icon"/><xsl:text>&quot;,</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text>&quot;</xsl:text><xsl:value-of select="text()"/><xsl:text>&quot;:</xsl:text><xsl:value-of select="@index"/><xsl:text>,</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>


<!-- ********************** EDIT AN EXISTING STATE **************************** -->
<xsl:template match="tif:edit-state">
    <xsl:text>im editstate</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --image=&quot;</xsl:text><xsl:value-of select="$env-root"/><xsl:text>/images/</xsl:text><xsl:value-of select="@icon"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>
    

<!-- ********************** BACKWARD FIELD RELATIONSHIPS **************************** -->
<xsl:template match="tif:field" mode="reverse-relationships">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="tif:reverse/@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --displayName=&quot;</xsl:text><xsl:value-of select="tif:reverse/@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="concat('Backward ', @name)"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- ********************** ALLOWED TYPES FOR RELATIONSHIP FIELDS **************************** -->
<xsl:template match="tif:field | tif:edit-field" mode="allowed-types">
    <!-- Allowed types on the forward relationship. -->
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --allowedTypes=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:relates/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@multivalued='true'">
        <xsl:text> --multiValued --yes </xsl:text>
    </xsl:if>
    
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <!-- Allowed types on the reverse relationship. -->
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --allowedTypes=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:reverse/tif:relates/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="tif:reverse/@multivalued='true'">
        <xsl:text> --multiValued --yes </xsl:text>
    </xsl:if>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="tif:reverse/@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>

</xsl:template>


<!-- **************** FIELD RELEVANCE AND EDITABILITY RULES ******************-->
<xsl:template match="tif:field" mode="options">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:if test="tif:relevance">
        <xsl:choose>
            <xsl:when test="tif:relevance/@special='not-empty'">
                <xsl:text> --relevanceRule=&quot;(field[</xsl:text><xsl:value-of select="@name"/><xsl:text>]&lt;&gt;&quot;&quot;)&quot;</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text> --relevanceRule=&quot;</xsl:text><xsl:value-of select="normalize-space(translate(tif:relevance/text(),'&quot;',''))"/><xsl:text>&quot;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:if>
    <xsl:if test="tif:editability">
        <xsl:text> --editabilityRule=&quot;</xsl:text><xsl:value-of select="normalize-space(translate(tif:editability/text(),'&quot;',''))"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** TYPE OVERRRIDE FIELD RELEVANCE, EDITABILITY AND DEFAULT RULES ******************-->
<xsl:template match="tif:editability" mode="type-override">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --overrideForType=&quot;</xsl:text><xsl:value-of select="../../../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --editabilityRule=&quot;</xsl:text><xsl:value-of select="normalize-space(translate(text(),'&quot;',''))"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template match="tif:relevance" mode="type-override">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --overrideForType=&quot;</xsl:text><xsl:value-of select="../../../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:choose>
        <xsl:when test="@special='not-empty'">
            <xsl:text> --relevanceRule=&quot;(field[</xsl:text><xsl:value-of select="../@name"/><xsl:text>]&lt;&gt;&quot;&quot;)&quot;</xsl:text>
        </xsl:when>
        <xsl:otherwise>
            <xsl:text> --relevanceRule=&quot;</xsl:text><xsl:value-of select="normalize-space(translate(text(),'&quot;',''))"/><xsl:text>&quot;</xsl:text>
        </xsl:otherwise>
    </xsl:choose>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template match="tif:default" mode="type-override">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --overrideForType=&quot;</xsl:text><xsl:value-of select="../../../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --default=&quot;</xsl:text><xsl:value-of select="normalize-space(text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template match="tif:description" mode="type-override">
    <xsl:text>im editfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --overrideForType=&quot;</xsl:text><xsl:value-of select="../../../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>




<!-- **************** TYPE DEFINITIONS ******************-->
<xsl:template match="tif:type" mode="definitions">
    <xsl:text>im createtype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>--name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="not(string-length(@icon)=0)">
        <xsl:text> --image=&quot;</xsl:text><xsl:value-of select="$env-root"/><xsl:text>/images/</xsl:text><xsl:value-of select="@icon"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>




<!-- **************** STATE TRANSITIONS AND EDITABILITY  ******************-->
<xsl:template match="tif:states" mode="transitions">
    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:call-template name="state-transitions"/>
    <xsl:if test="../tif:editability">
        <xsl:text> --editabilityRule=&quot;</xsl:text><xsl:value-of select="normalize-space(../tif:editability/text())"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template name="state-transitions">
    <xsl:text> --stateTransitions &quot;</xsl:text>
    <xsl:for-each select="tif:state/tif:next">
        <xsl:text></xsl:text><xsl:value-of select="../@name"/><xsl:text>:</xsl:text><xsl:value-of select="@name"/><xsl:text>:</xsl:text><xsl:value-of select="@group"/>
        <xsl:if test="position() != (last())">
            <xsl:text>; </xsl:text>
        </xsl:if>
    </xsl:for-each>
    <xsl:text>&quot;</xsl:text>
</xsl:template>

<!-- **************** Type Properties  ******************-->
<xsl:template match="tif:properties">
    <xsl:if test="tif:property">
        <xsl:text>im edittype </xsl:text>
        <xsl:call-template name="common-opts"/>
        <xsl:text> --properties=&quot;</xsl:text>
        <xsl:for-each select="tif:property">
            <xsl:value-of select="@name"/><xsl:text>:</xsl:text><xsl:call-template name="escapePropNew"><xsl:with-param name="pStr" select="@value"/></xsl:call-template><xsl:text>:</xsl:text><xsl:value-of select="tif:description/text()"/><xsl:text>;</xsl:text>
        </xsl:for-each>
        <xsl:text>&quot; &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
        <xsl:call-template name="cmd-end"/>
    </xsl:if>
</xsl:template>

<!-- **************** Test Management  ******************-->
<xsl:template match="tif:test-management">
    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>  --testRole=&quot;</xsl:text><xsl:value-of select="@role"/><xsl:text>&quot; </xsl:text>
    <xsl:if test="@relateTestResults='true'">
        <xsl:text> --enableTestResultRelationship </xsl:text>
    </xsl:if>
    <xsl:if test="tif:testcase/@enableTestSteps='true'">
        <xsl:text> --enableTestSteps </xsl:text>
    </xsl:if>
    <xsl:if test="tif:testcase/tif:result-fields">
        <xsl:text> --testCaseResultFields=&quot;</xsl:text>
        <xsl:call-template name="fieldList">
           <xsl:with-param name="node" select="tif:testcase/tif:result-fields" />
        </xsl:call-template>
        <xsl:text>&quot; </xsl:text>
    </xsl:if>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
    
</xsl:template>


<xsl:template name="fieldList">
    <xsl:param name="node"/>
    <xsl:for-each select="$node/tif:field">
        <xsl:value-of select="text()"/>
        <xsl:if test="position() != (last())">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:for-each>
</xsl:template>



<!-- **************** Document model  ******************-->
<xsl:template match="tif:document-model">
    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>  --enableBranch --enableCopyTree --enableLabel --documentClass=&quot;</xsl:text><xsl:value-of select="@role"/><xsl:text>&quot; </xsl:text>
    <xsl:text> --associatedType=&quot;</xsl:text><xsl:value-of select="@associated-type"/><xsl:text>&quot; </xsl:text>
    <xsl:text> --significantFields=&quot;</xsl:text>
    <xsl:for-each select="tif:significant-edits/tif:field">
        <xsl:value-of select="@name"/><xsl:text>,</xsl:text>
    </xsl:for-each>
    <xsl:text>&quot; &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
    
</xsl:template>

<!-- Is this redundant?? -->
    <xsl:if test="tif:shared-item/@default-category">
        <xsl:text>im editfield </xsl:text>
        <xsl:call-template name="common-opts"/>
        <xsl:text> --overrideForType=&quot;</xsl:text><xsl:value-of select="tif:shared-item/@name"/><xsl:text>&quot;</xsl:text>
        <xsl:text> --default=&quot;</xsl:text><xsl:value-of select="tif:shared-item/@default-category"/><xsl:text>&quot;</xsl:text>
        <xsl:text> &quot;Shared Category&quot;</xsl:text>
        <xsl:call-template name="cmd-end"/>
    </xsl:if>

<xsl:template match="tif:traces">
    <xsl:for-each select="tif:trace">
        <xsl:text>java -classpath $classpath com.ptc.tif.util.UpdateRelationship  \
                  -user </xsl:text><xsl:value-of select="$is-user"/><xsl:text> \
                  -password </xsl:text><xsl:value-of select="$is-password"/><xsl:text> \
                  -hostname </xsl:text><xsl:value-of select="$is-server"/><xsl:text> \
                  -port </xsl:text><xsl:value-of select="$is-port"/><xsl:text> \
                  -relationship &quot;</xsl:text><xsl:value-of select="../../@name"/><xsl:text>&quot; \
                  -source &quot;</xsl:text><xsl:value-of select="../../../../@name"/><xsl:text>&quot; \
                  -target &quot;</xsl:text><xsl:value-of select="@target"/><xsl:text>&quot;
        </xsl:text>
     
        
        <!-- property of the form: name="MKS.RQ.trace.<Package>" value="Models:<Requirement>"-->
        <xsl:text>java -classpath $classpath com.ptc.tif.util.UpdateTypeProperty \
                  -user </xsl:text><xsl:value-of select="$is-user"/><xsl:text> \
                  -password </xsl:text><xsl:value-of select="$is-password"/><xsl:text> \
                  -hostname </xsl:text><xsl:value-of select="$is-server"/><xsl:text> \
                  -port </xsl:text><xsl:value-of select="$is-port"/><xsl:text> \
                  -type &quot;MKS Solution&quot; \
                  -propName &quot;MKS.RQ.trace.&lt;</xsl:text><xsl:value-of select="../../../../@name"/><xsl:text>&gt;&quot; \
                  -propValue &quot;</xsl:text><xsl:value-of select="../../@name"/><xsl:text>:&lt;</xsl:text><xsl:value-of select="@target"/><xsl:text>&gt;&quot; \
                  -propDesc &quot;</xsl:text><xsl:value-of select="../@description"/><xsl:text>&quot;
        </xsl:text>
         

        <!-- If this is a self-trace, update the selftrace property. -->
        <xsl:if test="@target = ../../../../@name">
            <xsl:text>java -classpath $classpath com.ptc.tif.util.UpdateTypeProperty \
                      -hostname </xsl:text><xsl:value-of select="$is-server"/><xsl:text> \
                      -port </xsl:text><xsl:value-of select="$is-port"/><xsl:text> \
                      -user </xsl:text><xsl:value-of select="$is-user"/><xsl:text> \
                      -password </xsl:text><xsl:value-of select="$is-password"/><xsl:text> \
                      -type &quot;MKS Solution&quot; \
                      -propName &quot;MKS.RQ.selftrace&quot; \
                      -propValue &quot;&lt;</xsl:text><xsl:value-of select="@target"/><xsl:text>&gt;:</xsl:text><xsl:value-of select="../../@name"/><xsl:text>&quot; \
                      -propDesc &quot;</xsl:text><xsl:value-of select="../@description"/><xsl:text>&quot;
           </xsl:text>

        </xsl:if>
    </xsl:for-each>
</xsl:template>


<!-- **************** MANDATORY FIELDS******************-->
<xsl:template match="tif:states" mode="mandatory-fields">
    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:call-template name="mandatory-fields"/>
    
    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template name="mandatory-fields">
    <xsl:text> --mandatoryFields &quot;</xsl:text>
    <xsl:for-each select="tif:state/tif:mandatory">
        <xsl:value-of select="../@name"/><xsl:text>:</xsl:text>
            <xsl:for-each select="tif:field">
                <xsl:value-of select="@name"/>
                <xsl:if test="position() != (last())">
                    <xsl:text>,</xsl:text>
                </xsl:if>
            </xsl:for-each>
        <xsl:if test="position() != (last())">
            <xsl:text>; </xsl:text>
        </xsl:if>
    </xsl:for-each>
    <xsl:text>&quot;</xsl:text>
</xsl:template>


<!-- **************** TYPE ATTRIBUTES *****************-->
<xsl:template match="tif:type" mode="attributes">
    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <!--*** Attributes ***-->
    <xsl:if test="@phase">
        <xsl:text> --phaseField=&quot;</xsl:text><xsl:value-of select="@phase"/><xsl:text>&quot;</xsl:text> 
    </xsl:if>
    <xsl:if test="@time-tracking">
        <xsl:text> --enableTimeTracking </xsl:text> 
    </xsl:if>
    <xsl:if test="@show-workflow='true'">
        <xsl:text> --showWorkflow </xsl:text> 
    </xsl:if>
    <xsl:if test="@canLabel='true'">
        <xsl:text> --enableLabel </xsl:text> 
    </xsl:if>
    <xsl:if test="@canDelete='true'">
        <xsl:text> --enableDeleteItem </xsl:text> 
    </xsl:if>
    <xsl:if test="@haveRevisions='true'">
        <xsl:text> --enableRevision </xsl:text> 
    </xsl:if>
    <xsl:if test="@backsProject='true'">
        <xsl:text> --enableProjectBacking </xsl:text> 
    </xsl:if>
    <xsl:if test="@show-history='true'">
        <xsl:text> --showHistory </xsl:text> 
    </xsl:if>
    <xsl:if test="@allowCP='true'">
        <xsl:text> --allowChangePackages --createCPPolicy=&quot;</xsl:text><xsl:value-of select="@cp-policy"/><xsl:text>&quot;</xsl:text> 
    </xsl:if>
    
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** VISIBLE FIELDS ******************-->
<xsl:template match="tif:type | tif:edit-type" mode="visible">
    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:call-template name="visible-fields"/>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template name="visible-fields">
    <xsl:text> --visibleFields &quot;</xsl:text>
    <xsl:for-each select="tif:fields/tif:field">
        <xsl:text></xsl:text><xsl:value-of select="@name"/><xsl:text>:everyone</xsl:text>
        <xsl:if test="position() != (last())">
            <xsl:text>;</xsl:text>
        </xsl:if>
    </xsl:for-each>
    <xsl:text>&quot;</xsl:text>
</xsl:template>


<!-- **************** CONSTRAINTS ******************-->
<xsl:template match="tif:constraints">
    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --fieldRelationships=&quot;</xsl:text>
    <xsl:for-each select="tif:constraint">
        <xsl:call-template name="makeconstraint"/>
        <xsl:if test="position() != (last())">
            <xsl:text>;</xsl:text>
        </xsl:if>
    </xsl:for-each>
    <xsl:text>&quot;</xsl:text>

    <xsl:text> &quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template name="makeconstraint">
    <!-- Add the field relationships for custom fields. -->
    <!-- Relationships for the normal fields. -->
    <xsl:choose>
        <xsl:when test="@type='basic'">
           <xsl:value-of select="tif:source/@name" /><xsl:text>=</xsl:text>
           <xsl:call-template name="valueList">
               <xsl:with-param name="node" select="tif:source" />
           </xsl:call-template>
           <xsl:text>:</xsl:text><xsl:value-of select="tif:target/@name"/><xsl:text>=</xsl:text>
           <xsl:call-template name="valueList">
               <xsl:with-param name="node" select="tif:target" />
           </xsl:call-template>
        </xsl:when>

        <xsl:when test="@type='fieldRelationship'">
           <xsl:value-of select="tif:source/@name" /><xsl:text>=</xsl:text>
           <xsl:call-template name="valueList">
               <xsl:with-param name="node" select="tif:source" />
           </xsl:call-template>
           <xsl:text>:</xsl:text><xsl:value-of select="tif:target/@name"/><xsl:text>=</xsl:text>
           <xsl:call-template name="valueList">
               <xsl:with-param name="node" select="tif:target" />
           </xsl:call-template>
        </xsl:when>

        <xsl:when test="@type='rule'">
           <xsl:text>rule=</xsl:text><xsl:value-of select="tif:rule/text()"/>
           <xsl:text>:</xsl:text><xsl:value-of select="tif:target/@name"/><xsl:text>=</xsl:text>
           <xsl:call-template name="valueList">
               <xsl:with-param name="node" select="tif:target" />
           </xsl:call-template>
        </xsl:when>

        <xsl:when test="@type='ibpl'">
           <xsl:text>rule=</xsl:text><xsl:value-of select="tif:rule/text()"/>
           <xsl:text>:</xsl:text><xsl:value-of select="tif:target/@name"/>
        </xsl:when>
    </xsl:choose>    

</xsl:template>

<xsl:template name="valueList">
    <xsl:param name="node"/>
    <xsl:for-each select="$node/tif:value">
        <xsl:value-of select="text()"/>
        <xsl:if test="position() != (last())">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:for-each>
</xsl:template>

<xsl:template name="field-rules">
    <xsl:for-each select="tif:fields/tif:field/tif:rule">
        <xsl:text>rule=</xsl:text><xsl:value-of select="text()"/><xsl:text>;</xsl:text>
    </xsl:for-each>
</xsl:template>

<!-- **************** QUERY DEFINITIONS ******************-->
<xsl:template match="tif:query">
    <xsl:text>im createquery</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="not(string-length(@icon)=0)">
        <xsl:text> --image=&quot;</xsl:text><xsl:value-of select="$env-root"/><xsl:text>/images/</xsl:text><xsl:value-of select="@icon"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:text> --queryDefinition=&quot;</xsl:text>       
		<xsl:call-template name="escapeQuote">
			<xsl:with-param name="qText" select="tif:definition/text()" />
		</xsl:call-template>
	<xsl:text>&quot;</xsl:text>
    <xsl:text> --sharedTo=&quot;</xsl:text><xsl:value-of select="@share-groups"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@admin='yes'">
        <xsl:text> --sharedAdmin</xsl:text>
    </xsl:if>
    <xsl:if test="tif:description">
        <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="tif:default-columns">
        <xsl:text> --fields=&quot;</xsl:text>
            <xsl:call-template name="nameList">
               <xsl:with-param name="node" select="tif:default-columns" />
           </xsl:call-template>
        <xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template name="nameList">
    <xsl:param name="node"/>
    <xsl:for-each select="$node/tif:column">
        <xsl:value-of select="@name"/>
        <xsl:if test="position() != (last())">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:for-each>
</xsl:template>



<!-- **************** REPORT DEFINITIONS ******************-->
<xsl:template match="tif:report">
    <xsl:text>im createreport</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
	<xsl:text> --sharedTo=&quot;</xsl:text><xsl:value-of select="@share-groups"/><xsl:text>&quot;</xsl:text>
	<xsl:text> --query=&quot;</xsl:text><xsl:value-of select="tif:query/text()"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --recipeParams=&quot;</xsl:text><xsl:value-of select="tif:recipe-params/text()"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@admin='yes'">
        <xsl:text> --sharedAdmin</xsl:text>
    </xsl:if>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** CHART DEFINITIONS ******************-->
<xsl:template match="tif:chart">
    <xsl:text>im createchart</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --chartType=&quot;</xsl:text><xsl:value-of select="@type"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --chartTitle=&quot;</xsl:text><xsl:value-of select="@chart-title"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --fieldValues=&quot;</xsl:text><xsl:value-of select="@field-values"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@display-description='true'"> 
            <xsl:text> --displayDescription</xsl:text>
    </xsl:if>
    <xsl:text> --query=&quot;</xsl:text><xsl:value-of select="@query"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --graphStyle=&quot;</xsl:text><xsl:value-of select="@graph-style"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --sharedTo=&quot;</xsl:text><xsl:value-of select="@share-groups"/><xsl:text>&quot;</xsl:text>
    <xsl:choose>
    <xsl:when test="@type='distribution'">
        <xsl:if test="@grouping-values">
            <xsl:text> --groupingValues=&quot;</xsl:text><xsl:value-of select="@grouping-values"/><xsl:text>&quot;</xsl:text>
        </xsl:if>
    </xsl:when>
        <xsl:when test="@type='trend'">
            <xsl:if test="@start-date">
                <xsl:text> --startDate=</xsl:text><xsl:value-of select="@start-date"/>
            </xsl:if>
            <xsl:if test="@end-date">
                <xsl:text> --endDate=</xsl:text><xsl:value-of select="@end-date"/>
            </xsl:if>
            <xsl:if test="@trend-step">
                <xsl:text> --trendStep=</xsl:text><xsl:value-of select="@trend-step"/>
            </xsl:if>
            <xsl:if test="@nois3d='true'"> 
                <xsl:text> --nois3D</xsl:text>
            </xsl:if>
        </xsl:when>    
    </xsl:choose>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** DASHBOARD DEFINITIONS ******************-->
<xsl:template match="tif:dashboard">
    <xsl:text>im createdashboard</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --layoutFile=&quot;</xsl:text><xsl:value-of select="$is-root"/><xsl:value-of select="@layout-file"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --sharedTo=&quot;</xsl:text><xsl:value-of select="@shared-groups"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** TRIGGER DEFINITIONS ******************-->
<xsl:template match="tif:trigger">
    <xsl:text>im createtrigger </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>--name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --type=&quot;</xsl:text><xsl:value-of select="@type"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --position=</xsl:text><xsl:value-of select="position()"/>
    <xsl:text> --script=&quot;</xsl:text><xsl:value-of select="tif:script/text()"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --scriptParams=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:params/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@type='rule'">
        <xsl:text> --scriptTiming=&quot;</xsl:text><xsl:value-of select="tif:script-timing/text()"/><xsl:text>&quot;</xsl:text>
        <xsl:text> --rule=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:rule/text())"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="@type='scheduled'">
        <xsl:text> --frequency=&quot;</xsl:text><xsl:value-of select="tif:frequency/text()"/><xsl:text>&quot;</xsl:text>
        <xsl:text> --runAs=&quot;</xsl:text><xsl:value-of select="tif:run-as/text()"/><xsl:text>&quot;</xsl:text>
        <xsl:text> --query=&quot;</xsl:text><xsl:value-of select="tif:query/text()"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="assignment">
        <xsl:text> --assign=&quot;</xsl:text><xsl:value-of select="tif:assignment/text()"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template match="tif:field-rule-trigger">
    <xsl:text>set -- $(im diag </xsl:text><xsl:call-template name="common-opts"/><xsl:text> --diag=runsql --param=&quot;select ID from Fields where Name='</xsl:text><xsl:value-of select="@field"/><xsl:text>'&quot;)
</xsl:text>
    <xsl:text>fieldID=$2
              export fieldID
</xsl:text>
    <xsl:text>im createtrigger </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>--name=&quot;__mks__ $fieldID </xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:description/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --script=&quot;</xsl:text><xsl:value-of select="tif:script/text()"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --scriptParams=&quot;</xsl:text><xsl:value-of select="normalize-space(tif:params/text())"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --scriptTiming=&quot;</xsl:text><xsl:value-of select="@timing"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>


<xsl:template name="create-fva-temp">
    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-logical&quot;</xsl:text>
    <xsl:text> --type=logical</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-integer&quot;</xsl:text>
    <xsl:text> --type=integer</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-longtext&quot;</xsl:text>
    <xsl:text> --type=longtext</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-attachment&quot;</xsl:text>
    <xsl:text> --type=attachment</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-pick&quot;</xsl:text>
    <xsl:text> --type=pick </xsl:text>
    <xsl:text> --picks=&quot;Yes:1,No:2&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-relationship&quot;</xsl:text>
    <xsl:text> --type=relationship</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-siproject&quot;</xsl:text>
    <xsl:text> --type=siproject</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:text>im createtype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temporary-ibpl-type&quot;</xsl:text>
    <xsl:text> --description=&quot;A temporary type to allow us to create FVAs. An IBPL on FVA_temporary points to this to resolve FVA fields.&quot;</xsl:text>
    <xsl:text> --visibleFields=&quot;Summary:everyone;State:everyone;Assigned User:everyone;FVA-temp-logical:everyone;FVA-temp-integer:everyone;FVA-temp-pick:everyone;FVA-temp-longtext:everyone;FVA-temp-attachment:everyone;FVA-temp-relationship:everyone;FVA-temp-siproject:everyone&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>


    <xsl:text>im createfield</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temp-ibpl&quot;</xsl:text>
    <xsl:text> --type=ibpl</xsl:text>
    <xsl:text> --backingType=&quot;FVA-temporary-ibpl-type&quot;</xsl:text>
    <xsl:text> --backingTextField=&quot;Summary&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>


    <xsl:text>im createtype </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;FVA-temporary&quot;</xsl:text>
    <xsl:text> --description=&quot;A temporary type to allow us to create FVAs.&quot;</xsl:text>
    <xsl:text> --visibleFields=&quot;Summary:everyone;State:everyone;Assigned User:everyone;FVA-temp-ibpl:everyone;FVA-temp-attachment:everyone;FVA-temp-relationship:everyone&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>

</xsl:template>

<xsl:template name="create-qbr-temp">
    <xsl:text>im createquery</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --name=&quot;QbrDummy&quot;</xsl:text>
    <xsl:text> --sharedAdmin --queryDefinition=&quot;(field[ID]=0)&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>



<!-- **************** HELPERS ******************-->

<xsl:template name="common-opts">
    <xsl:text> --batch</xsl:text>
    <xsl:text> --hostname=&quot;</xsl:text><xsl:value-of select="$is-server"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --port=&quot;</xsl:text><xsl:value-of select="$is-port"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --user=&quot;</xsl:text><xsl:value-of select="$is-user"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --password=&quot;</xsl:text><xsl:value-of select="$is-password"/><xsl:text>&quot; </xsl:text>
</xsl:template>

<xsl:template name="cmd-end">
    <xsl:text> >>deploy.log 2>>deploy.log
</xsl:text>
</xsl:template>

<xsl:template name="redirect">
    <xsl:text> 2&gt;&gt;ids.txt</xsl:text>
</xsl:template>



<xsl:template name="escapeQuote">
      <xsl:param name="qText" select="."/>

      <xsl:if test="string-length($qText) >0">
       <xsl:value-of select=
        "substring-before(concat($qText, '&quot;'), '&quot;')"/>

       <xsl:if test="contains($qText, '&quot;')">
        <xsl:text>\"</xsl:text>

        <xsl:call-template name="escapeQuote">
          <xsl:with-param name="qText" select=
          "substring-after($qText, '&quot;')"/>
        </xsl:call-template>
       </xsl:if>
      </xsl:if>
</xsl:template>

<xsl:template name="escapeColon">
      <xsl:param name="cText" select="."/>

      <xsl:if test="string-length($cText) >0">
       <xsl:value-of select=
        "substring-before(concat($cText, ':'), ':')"/>

       <xsl:if test="contains($cText, ':')">
        <xsl:text>\:</xsl:text>

        <xsl:call-template name="escapeColon">
          <xsl:with-param name="cText" select=
          "substring-after($cText, ':')"/>
        </xsl:call-template>
       </xsl:if>
      </xsl:if>
</xsl:template>


<xsl:template name="escapePropNew">
   <xsl:param name="pStr" select="."/>
   <xsl:param name="pspecChars">;:"</xsl:param>

   <xsl:if test="string-length($pStr)">
     <xsl:variable name="vchar1" select="substring($pStr,1,1)"/>

      <xsl:choose>
          <xsl:when test="contains($pspecChars, $vchar1)">
              <xsl:value-of select="concat('\', $vchar1)"/>
          </xsl:when>
          <xsl:otherwise>
              <xsl:value-of select="$vchar1"/>
          </xsl:otherwise>
      </xsl:choose>

      <xsl:call-template name="escapePropNew">
       <xsl:with-param name="pStr" select="substring($pStr,2)"/>
       <xsl:with-param name="pspecChars" select="$pspecChars"/>
      </xsl:call-template>
    </xsl:if>
</xsl:template>    
    
</xsl:stylesheet>    
