<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate solution data scripts from data definition XML.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:param name="is-server"/>
<xsl:param name="is-port"/>
<xsl:param name="is-user"/>
<xsl:param name="is-password"/>
<xsl:param name="is-root"/>
<xsl:param name="safe-is-root"/>

<xsl:strip-space elements="description"/>
<xsl:strip-space elements="relates"/>

<xsl:template match="/">
    <xsl:apply-templates select="im-data"/>
</xsl:template>

<xsl:template match="im-data">
# Create sample data set..
# 
# Issues
<xsl:apply-templates select="items/item"/>


# END
</xsl:template>


<!-- **************** ISSUE DEFINITIONS ******************-->
<xsl:template match="item">
    <!-- Create an issue and record its ID. We can use this later to create relationships. -->
    <xsl:if test="@save-id">
        <xsl:value-of select="@save-id"/><xsl:text>=$(im createissue </xsl:text>
        <xsl:call-template name="common-opts"/>
        <xsl:text> --type=&quot;</xsl:text><xsl:value-of select="@type"/><xsl:text>&quot;</xsl:text>
        <xsl:for-each select="field">
            <xsl:text> --field=&quot;</xsl:text><xsl:value-of select="@name"/>=<xsl:value-of select="text()"/><xsl:text>&quot;</xsl:text>
        </xsl:for-each>
        <xsl:if test="@parent">
            <xsl:text> --addRelationships="</xsl:text><xsl:value-of select="@relationship"/><xsl:text>:$</xsl:text><xsl:value-of select="@parent"/><xsl:text>"</xsl:text>
        
        </xsl:if>
        <xsl:text> --displayIdOnly 2&gt;&amp;1)
</xsl:text>

        <xsl:text>print "Created </xsl:text><xsl:value-of select="@type"/><xsl:text> issue $</xsl:text><xsl:value-of select="@save-id"/><xsl:text> and saved ID" >>deploy.log
</xsl:text>
    </xsl:if>
    <xsl:if test="not(@save-id)">
        <xsl:text>im createissue </xsl:text>
        <xsl:call-template name="common-opts"/>
        <xsl:text> --type=&quot;</xsl:text><xsl:value-of select="@type"/><xsl:text>&quot;</xsl:text>
        <xsl:for-each select="field">
            <xsl:text> --field=&quot;</xsl:text><xsl:value-of select="@name"/>=<xsl:value-of select="text()"/><xsl:text>&quot;</xsl:text>
        </xsl:for-each>
        <xsl:if test="@parent">
            <xsl:text> --addRelationships="</xsl:text><xsl:value-of select="@relationship"/><xsl:text>:$</xsl:text><xsl:value-of select="@parent"/><xsl:text>"</xsl:text>
        
        </xsl:if>
        <xsl:call-template name="cmd-end"/>
    </xsl:if>
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


</xsl:stylesheet>    
