<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate MKS Domain scripts from the solution XML.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:tif="http://www.ptc.com/integrity-solution" version="1.0">
<xsl:param name="is-server"/>
<xsl:param name="is-port"/>
<xsl:param name="is-user"/>
<xsl:param name="is-password"/>
<xsl:param name="is-root"/>
<xsl:param name="soln-path"/>

<xsl:strip-space elements="description"/>
<xsl:strip-space elements="relates"/>

<xsl:template match="/">
    <xsl:apply-templates select="tif:im-solution"/>
</xsl:template>

<xsl:template match="tif:im-solution">
# Script generated from Integrity Manager XML solution.
# 
# Makes sure the user creating the solution can adminster the server. 
aa addaclentry <xsl:call-template name="common-opts"/> --acl=mks:im u=<xsl:value-of select="$is-user"/>:AdminServer
#
# Users
<xsl:apply-templates select="tif:users/tif:user"/>
# Groups
<xsl:apply-templates select="tif:groups/tif:group"/>


# END
</xsl:template>


<!-- **************** GROUP DEFINITIONS ******************-->
<xsl:template match="tif:group">
    <xsl:text>integrity createmksdomaingroup </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text>--name=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@email">
        <xsl:text> --email=&quot;</xsl:text><xsl:value-of select="@email"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="@description"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --members=&quot;</xsl:text>
    <xsl:for-each select="tif:member">
        <xsl:text>u=</xsl:text>
        <xsl:value-of select="@name"/>
        <xsl:if test="position() != (last())">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:for-each>
    <xsl:text>&quot; </xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- **************** USER DEFINITIONS ******************-->
<xsl:template match="tif:user">
    <xsl:text>integrity createmksdomainuser</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --loginID=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --fullName=&quot;</xsl:text><xsl:value-of select="@full-name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --email=&quot;</xsl:text><xsl:value-of select="@email"/><xsl:text>&quot;</xsl:text>
    <xsl:if test="@password">
        <xsl:text> --userPassword=&quot;</xsl:text><xsl:value-of select="@password"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:if test="not(@password)">
        <xsl:text> --userPassword=&quot;password&quot;</xsl:text>
    </xsl:if>
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


</xsl:stylesheet>    
