<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate the template manifest file.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
    <xsl:apply-templates select="im-solution"/>
</xsl:template>

<xsl:template match="im-solution">
     <xsl:apply-templates select="types/type"/>
     <xsl:apply-templates select="fields/field"/>
     <xsl:apply-templates select="states/state"/>
     <xsl:apply-templates select="triggers/trigger"/>
     <xsl:apply-templates select="queries/query"/>

</xsl:template>

<xsl:template match="type">
type=<xsl:value-of select="@name"/>
</xsl:template>

<xsl:template match="field">
field=<xsl:value-of select="@name"/>
</xsl:template>

<xsl:template match="state">
state=<xsl:value-of select="@name"/>
</xsl:template>

<xsl:template match="trigger">
trigger=<xsl:value-of select="@name"/>
</xsl:template>

<xsl:template match="query">
query=<xsl:value-of select="@name"/>
</xsl:template>


</xsl:stylesheet>
