<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate solution creation scripts from the solution XML.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:param name="is-server"/>
<xsl:param name="is-port"/>
<xsl:param name="is-user"/>
<xsl:param name="is-password"/>
<xsl:param name="is-root"/>
<xsl:param name="safe-is-root"/>
<xsl:param name="is-version"/>
<xsl:param name="impl-path"/>

<xsl:strip-space elements="description"/>

<xsl:template match="/">
    <xsl:apply-templates select="im-solution"/>
</xsl:template>

<xsl:template match="im-solution">
# Script generated from Integrity Manager XML solution.
# 
if [ $1 == 'drop' ]
then
    # Drop Source sandboxes.
    <xsl:apply-templates select="source/source-project/sandbox" mode="drop"/>
else
    # Create Source projects and sandboxes.
    <xsl:apply-templates select="source/source-project" mode="create"/>
fi


# END
</xsl:template>

 

<!-- **************** Source setup ******************-->
<xsl:template match="source-project" mode="create">

    <xsl:call-template name="createProject"/>
    <xsl:apply-templates select="sub-project" />
    <xsl:apply-templates select="sandbox" mode="create"/>
</xsl:template>

<xsl:template name="createProject">
    <xsl:text>si createproject</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> &quot;/</xsl:text><xsl:value-of select="@name"/><xsl:text>/project.pj&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template match="sub-project">
    <xsl:text>si createsubproject</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --cpid=:bypass --project=&quot;/</xsl:text>
    <xsl:for-each select="ancestor::sub-project | ancestor::source-project">
        <xsl:value-of select="@name"/><xsl:text>/</xsl:text>
    </xsl:for-each>    
    <xsl:text>project.pj&quot; </xsl:text>
    
    <xsl:text>&quot;/</xsl:text>
    <xsl:for-each select="ancestor::sub-project | ancestor::source-project">
        <xsl:value-of select="@name"/><xsl:text>/</xsl:text>
    </xsl:for-each>    
    <xsl:value-of select="@name"/><xsl:text>/project.pj&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>

    <xsl:apply-templates select="sub-project" />
</xsl:template>

<xsl:template match="sandbox"  mode="create">
    <xsl:text>    si createsandbox</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> -Y --project=&quot;/</xsl:text><xsl:value-of select="../@name"/><xsl:text>/project.pj&quot;</xsl:text>
    <xsl:if test="@devpath">
        <xsl:text> --devpath=&quot;</xsl:text><xsl:value-of select="@devpath"/><xsl:text>&quot;</xsl:text>
    </xsl:if>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="$impl-path"/>/<xsl:value-of select="@path"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
    
    <xsl:if test="add-members">
        <xsl:text>    cp -r </xsl:text><xsl:value-of select="$impl-path"/>/<xsl:value-of select="add-members/@from-path"/><xsl:text>/* </xsl:text><xsl:value-of select="@path"/><xsl:text>
    </xsl:text>
    
        <xsl:text>si add</xsl:text>
        <xsl:call-template name="common-opts"/>
        <xsl:text> --sandbox=&quot;</xsl:text><xsl:value-of select="$impl-path"/>/<xsl:value-of select="@path"/><xsl:text>/project.pj&quot;</xsl:text>
        <xsl:text> --recurse --createSubprojects --cpID=:none --exclude=file:*.pj </xsl:text>
        <xsl:value-of select="$impl-path"/>/<xsl:value-of select="@path"/><xsl:text>/*</xsl:text>
        <xsl:call-template name="cmd-end"/>
    </xsl:if>
</xsl:template>

<xsl:template match="checkpoint" mode="create">
    <xsl:text>si checkpoint</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --project=&quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --label=&quot;</xsl:text><xsl:value-of select="@label"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --description=&quot;</xsl:text><xsl:value-of select="text()"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<xsl:template match="create-devpath" mode="create">
    <xsl:text>si createdevpath</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --project=&quot;</xsl:text><xsl:value-of select="../@name"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --projectRevision=&quot;</xsl:text><xsl:value-of select="@revision"/><xsl:text>&quot;</xsl:text>
    <xsl:text> --devpath=&quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
</xsl:template>

<!-- ********************************************* -->
<xsl:template match="sandbox"  mode="drop">
    <xsl:text>si dropsandbox</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> -Y  </xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="$impl-path"/>/<xsl:value-of select="@path"/><xsl:text>/project.pj&quot;</xsl:text>
    <xsl:call-template name="cmd-end"/>
    <xsl:text>    rm  -f -r &quot;</xsl:text><xsl:value-of select="$impl-path"/>/<xsl:value-of select="@path"/><xsl:text>&quot;</xsl:text>
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
