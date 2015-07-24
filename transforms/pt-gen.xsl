<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- A script to set the presentation templates.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:tif="http://www.ptc.com/integrity-solution" version="1.0">
<xsl:param name="is-server"/>
<xsl:param name="is-port"/>
<xsl:param name="is-user"/>
<xsl:param name="is-password"/>
<xsl:param name="is-root"/>
<xsl:param name="soln-path"/>


<xsl:template match="/">
    <xsl:apply-templates select="tif:im-solution"/>
</xsl:template>

<xsl:template match="tif:im-solution">
# Script generated from Integrity Manager XML solution.
# Set the presentation templates
classpath="externalScripts/mksapi.jar;externalScripts/commons-io-2.4.jar;externalScripts/commons-cli-1.2.jar;externalScripts/commons-lang.jar;externalScripts/TifUtils.jar"


if [[ $1 == "get" ]] 
then
    <xsl:apply-templates select="tif:types/tif:type"          mode="get" />
    <xsl:apply-templates select="tif:types/tif:edit-type"     mode="get" />
elif [[ $1 == "put" ]]
then
    <xsl:apply-templates select="tif:types/tif:type"          mode="put" />
    <xsl:apply-templates select="tif:types/tif:edit-type"     mode="put" />
else
    echo "Error: parameter must be get or put"
fi

# END
</xsl:template>


<!-- ***************************************************** -->
<!-- * GET the presentation templates from the database. * -->
<!-- ***************************************************** -->
<xsl:template match="tif:type | tif:edit-type" mode="get" >
  <xsl:if test="@presentation">
    <xsl:text>
    
    # Get and processtemplate (new). We have to delete the contents of the directory as getdbfile won't overwrite.
    rm templates/raw/*.xml
    </xsl:text>

    <xsl:text>integrity getdbfile --forceConfirm=yes</xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --output=</xsl:text><xsl:value-of select="$soln-path"/><xsl:text>/templates/raw/</xsl:text><xsl:value-of select="@presentation"/><xsl:text>.xml</xsl:text>
         <xsl:text> data/im/issue/templates/</xsl:text><xsl:value-of select="@presentation"/><xsl:text>.xml</xsl:text>
    <xsl:call-template name="cmd-end"/>
    <xsl:text>
    </xsl:text>

    <xsl:text>
    java -classpath $classpath com.ptc.tif.util.ProcessPresentationTemplate -getTemplate \
              -user </xsl:text><xsl:value-of select="$is-user"/><xsl:text> \
              -password </xsl:text><xsl:value-of select="$is-password"/><xsl:text> \
              -hostname </xsl:text><xsl:value-of select="$is-server"/><xsl:text> \
              -port </xsl:text><xsl:value-of select="$is-port"/><xsl:text> \
              -templateDir &quot;</xsl:text><xsl:value-of select="$soln-path"/><xsl:text>/templates&quot; \
              -template &quot;</xsl:text><xsl:value-of select="@presentation"/><xsl:text>.xml&quot;</xsl:text>
  </xsl:if>
</xsl:template>



<!-- ***************************************************** -->
<!-- * PUT the presentation templates into the database. * -->
<!-- ***************************************************** -->
<xsl:template match="tif:type | tif:edit-type" mode="put" >
  <xsl:if test="@presentation">
    <xsl:text>
    rm templates/raw/*.xml</xsl:text>
    <xsl:text>
    java -classpath $classpath com.ptc.tif.util.ProcessPresentationTemplate -putTemplate \
              -user </xsl:text><xsl:value-of select="$is-user"/><xsl:text> \
              -password </xsl:text><xsl:value-of select="$is-password"/><xsl:text> \
              -hostname </xsl:text><xsl:value-of select="$is-server"/><xsl:text> \
              -port </xsl:text><xsl:value-of select="$is-port"/><xsl:text> \
              -templateDir &quot;</xsl:text><xsl:value-of select="$soln-path"/><xsl:text>/templates&quot;\
              -template &quot;</xsl:text><xsl:value-of select="@presentation"/><xsl:text>.xml&quot;</xsl:text>
    <xsl:text>
    
    </xsl:text>

    <xsl:text>integrity putdbfile </xsl:text>
    <xsl:call-template name="common-opts"/>
    <xsl:text> --input=</xsl:text><xsl:value-of select="$soln-path"/><xsl:text>/templates/raw/</xsl:text><xsl:value-of select="@presentation"/><xsl:text>.xml</xsl:text>
         <xsl:text> data/im/issue/templates/</xsl:text><xsl:value-of select="@presentation"/><xsl:text>.xml</xsl:text>
    <xsl:call-template name="cmd-end"/>
    <xsl:text>
    </xsl:text>

    <xsl:text>im edittype </xsl:text>
    <xsl:call-template name="common-opts"/>
        <xsl:text> --viewPresentation=&quot;</xsl:text><xsl:value-of select="@presentation"/><xsl:text>&quot;</xsl:text>
        <xsl:text> --editPresentation=&quot;</xsl:text><xsl:value-of select="@presentation"/><xsl:text>&quot;</xsl:text>
        <xsl:text> --printPresentation=&quot;</xsl:text><xsl:value-of select="@presentation"/><xsl:text>&quot;</xsl:text>
    <xsl:text> &quot;</xsl:text><xsl:value-of select="@name"/><xsl:text>&quot;</xsl:text>
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
