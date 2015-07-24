<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate the template manifest file.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:tif="http://www.ptc.com/integrity-solution" 
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:xdt="http://www.w3.org/2005/xpath-datatypes"
                xmlns:err="http://www.w3.org/2005/xqt-errors"
                version="1.0">
                
<xsl:param name="is-server"/>
<xsl:param name="is-port"/>
<xsl:param name="is-user"/>
<xsl:param name="is-password"/>
<xsl:param name="is-root"/>
<xsl:param name="is-version"/>
<xsl:param name="env-root"/>


<xsl:template match="/">
    <xsl:apply-templates select="tif:im-solution"/>
</xsl:template>

<xsl:template match="tif:im-solution">
# Solution
#Types
<xsl:apply-templates select="tif:types/tif:type"/>
# Fields
<xsl:apply-templates select="tif:fields/tif:field"/>
# States
<xsl:apply-templates select="tif:states/tif:state"/>
# Triggers
<xsl:apply-templates select="tif:triggers/tif:trigger"/>
#Queries
<xsl:apply-templates select="tif:queries/tif:query"/>

</xsl:template>

<xsl:template match="tif:type">
<xsl:text>type.</xsl:text><xsl:value-of select="position()"/><xsl:text>=</xsl:text>
<xsl:value-of select="@name"/><xsl:text>.</xsl:text><xsl:value-of select="base-uri()"/><xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="tif:field">
<xsl:text>field.</xsl:text><xsl:value-of select="position()"/><xsl:text>=</xsl:text>
<xsl:value-of select="@name"/><xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="tif:state">
<xsl:text>state.</xsl:text><xsl:value-of select="position()"/><xsl:text>=</xsl:text>
<xsl:value-of select="@name"/><xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="tif:trigger">
<xsl:text>trigger.</xsl:text><xsl:value-of select="position()"/><xsl:text>=</xsl:text>
<xsl:value-of select="@name"/><xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="tif:query">
<xsl:text>query.</xsl:text><xsl:value-of select="position()"/><xsl:text>=</xsl:text>
<xsl:value-of select="@name"/><xsl:text>
</xsl:text>
</xsl:template>


</xsl:stylesheet>
