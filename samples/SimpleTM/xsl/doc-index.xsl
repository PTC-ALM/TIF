<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate documentation index page (index.html) from im-solution.dtd conformant
     XML.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
    <xsl:apply-templates select="im-solution"/>
</xsl:template>

<xsl:template match="im-solution">
    <html>
        <head>
            <link rel="stylesheet" type="text/css" href="main.css"/>
            <title><xsl:value-of select="@title"/></title>
        </head>
        <body>
          <table border="0" width="100%" cellpadding="0" cellspacing="5">
              <tr>
                <td width="15%" valign="top" class="sidenav">
                    <xsl:apply-templates select="types/type" mode="url"/>
                    <li><a href="#fields">Fields</a></li>
                        <ul>
                            <li><a href="#shorttext">Short text</a></li>
                            <li><a href="#longtext">Long text</a></li>
                            <li><a href="#integer">Integer / computed</a></li>
                            <li><a href="#float">Float / computed</a></li>
                            <li><a href="#pick">Pick</a></li>
                            <li><a href="#ibpl">IBPL</a></li>
                            <li><a href="#qbr">QBR</a></li>
                            <li><a href="#fva">FVA</a></li>
                            <li><a href="#relationship">Relationship</a></li>
                            <li><a href="#user">User</a></li>
                            <li><a href="#date">Date</a></li>
                            <li><a href="#relationships">Relationships</a></li>
                        </ul>    
                    <li><a href="#states">States</a></li>
                    <li><a href="#queries">Queries</a></li>
                    <li><a href="#triggers">Triggers</a></li>
                </td>
                <td valign="top">
                    <table border="0" width="100%" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="titlebar"><h1><a name="top"><xsl:value-of select="@title"/></a></h1></td>
                      </tr>
                      <tr>
                         <td colspan="2">
                             <xsl:apply-templates select="doc"/>
                             <h1><a name="types">Types</a></h1>
                             <xsl:apply-templates select="types/type" mode="doc"/>
                             <p class="smalllink"><a href="#top">Back to top</a></p>

                             <h1><a name="fields">Fields</a></h1>
                             
                             <h2><a name="shorttext">Short Text</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Max. length</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='shorttext']" mode="textfield"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="longtext">Long Text</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Max. length</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='longtext']" mode="textfield"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="integer">Integer numeric / computed</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Computation</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='integer']" mode="numeric"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="float">Float numeric / computed</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Computation</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='float']" mode="numeric"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="pick">Picks</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Values</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='pick']" mode="pick"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="ibpl">IBPLs</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Backing type</th><th>Field</th><th>States</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='ibpl']" mode="ibpl"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="qbr">QBRs</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Query</th><th>Correlation</th><th>Display</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='qbr']" mode="qbr"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="fva">FVAs</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Backed By</th><th>Backing Field</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='fva']" mode="fva"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="relationship">Relationships</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Relates</th><th>Reverse Name</th><th>Reverse Relates</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='relationship']" mode="relationship"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="users">Users</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Multivalued</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='user']" mode="user"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="dates">Dates</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="fields/field[@type='date']" mode="date"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h1><a name="queries">Queries</a></h1>
                             <xsl:apply-templates select="query"/>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             <h1><a name="triggers">Triggers</a></h1>
                             <xsl:apply-templates select="triggers/trigger"/>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                         </td>
                      </tr>   
                    </table>
                </td>
              </tr>
        </table>
        </body>
    </html>
</xsl:template>

<xsl:template match="type" mode="url">
    <li><a href="#{@name}"><xsl:value-of select="@name"/></a></li>
</xsl:template>

<xsl:template match="type" mode="doc">
    <hr/>
    <h2><a name="{@name}"><xsl:value-of select="@name"/></a></h2>
    <xsl:apply-templates select="doc"/>
    <h3>State transitions</h3>
    <table border="1" width="100%">
        <tr><th>State</th><th>Next</th><th>Mandatory fields</th></tr>
        <xsl:for-each select="states/state">
            <tr>
                <td valign="top"><xsl:value-of select="@name"/></td>
                <td valign="top">
                    <xsl:if test="not(next)">&#160;</xsl:if>
                    <ul>
                    <xsl:for-each select="next">
                        <li><xsl:value-of select="@name"/>:<xsl:value-of select="@group"/></li>
                    </xsl:for-each>
                    </ul>
                </td>    
                <td valign="top">
                    <xsl:if test="not(mandatory/field)">&#160;</xsl:if>
                    <ul>
                    <xsl:for-each select="mandatory/field">
                        <li><xsl:value-of select="@name"/></li>
                    </xsl:for-each>
                    </ul>
                </td>    
            </tr>         
        </xsl:for-each>
    </table>
    <h3>Visible fields</h3>
    <table border="1" width="100%">
        <tr><th>Field</th><th>Override Editability</th><th>Override Relevance</th></tr>
        <xsl:apply-templates select="fields" mode="visible"/>
    </table>
    <p class="smalllink"><a href="#top">Back to top</a></p>
    <xsl:if test="properties">
        <h3>Properties</h3>
        <table border="1" width="100%">
            <tr><th>Property</th><th>Value</th><th>Description</th></tr>
            <xsl:apply-templates select="properties/property"/>
        </table>
        <p class="smalllink"><a href="#top">Back to top</a></p>
    </xsl:if>
    
    <hr/>
</xsl:template>


<xsl:template match="field" mode="visible">
    <tr>
        <td>
            <xsl:value-of select="@name"/>
        </td>
        <td>
            <xsl:value-of select="editability/text()"/>&#160;
        </td>
        <td>
            <xsl:value-of select="relevance/text()"/>&#160;
        </td>    
    </tr>
</xsl:template>

<xsl:template match="property">
    <tr>
        <td>
            <xsl:value-of select="@name"/>
        </td>
        <td>
            <xsl:value-of select="@value"/>&#160;
        </td>
        <td>
            <xsl:value-of select="@description"/>&#160;
        </td>    
    </tr>
</xsl:template>


<!-- xsl:template match="related">
    <li><xsl:value-of select="@name"/>, direction <xsl:value-of select="@direction"/></li>
</xsl:template -->

<xsl:template match="fields">
    <h3>Visible fields</h3>
    <xsl:apply-templates select="field"/>
</xsl:template>

<xsl:template match="states">
    <h3>States</h3>
    <table border="1" class="list">
        <tr><th>State</th><th>Links to (allowed groups)</th><th>Description</th></tr>
        <xsl:apply-templates select="state"/>
    </table>
</xsl:template>

<xsl:template match="state">
    <tr>
        <td><xsl:value-of select="@name"/></td> 
        <td><xsl:value-of select="next/@name"/>(<xsl:value-of select="next/@group"/>)</td>
        <td><xsl:value-of select="description/text()"/></td>
    </tr>
</xsl:template>


<!-- **************** Display the different type of fields. ******************-->

<!-- **************** Short text ******************-->
<xsl:template match="field" mode="textfield">
    <tr>
        <td><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@max-length"/>&#160;</td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="field" mode="numeric">
    <tr>
        <td><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="computation/text()"/>&#160;</td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="field" mode="pick">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><ul>
            <xsl:for-each select="values/value">
                <li><xsl:value-of select="text()"/></li>
            </xsl:for-each>
            </ul>
        </td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>


<xsl:template match="field" mode="ibpl">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="backing-issue/@type"/></td>
        <td><xsl:value-of select="backing-issue/@field"/></td>
        <td><xsl:value-of select="backing-issue/@states"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="field" mode="qbr">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="query/text()"/></td>
        <td><xsl:value-of select="correlation/text()"/></td>
        <td><xsl:value-of select="@display"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="field" mode="fva">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@backedBy"/></td>
        <td><xsl:value-of select="@backingField"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="field" mode="relationship">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="relates/text()"/></td>
        <td><xsl:value-of select="reverse/@name"/></td>
        <td><xsl:value-of select="reverse/relates/text()"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>


<xsl:template match="field" mode="user">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@multivalued"/>&#160;</td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="field" mode="date">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>




<!-- xsl:template match="description">
    <p><xsl:apply-templates/></p>
</xsl:template -->

<!-- xsl:template match="question">
    <p class="alert"><xsl:apply-templates/></p>
</xsl:template -->

<!-- xsl:template match="list">
    <table border="1" class="list">
        <xsl:if test="@depends-on">
            <tr><th><xsl:value-of select="@depends-on"/>=<xsl:value-of select="@type-selector"/></th></tr>
        </xsl:if>
        <xsl:apply-templates select="value"/>
    </table>
</xsl:template -->

<!-- xsl:template match="value">
    <tr>
        <td>
            <xsl:value-of select="text()"/>
        </td>
    </tr>
</xsl:template -->


<xsl:template match="doc">
    <xsl:apply-templates/>
</xsl:template>





<xsl:template match="trigger">
    <h3><xsl:value-of select="@name"/> (<xsl:value-of select="@type"/>,<xsl:value-of select="@timing"/>)</h3>
    <table border="1" width="100%">
      <tr><td>Description</td><td><xsl:value-of select="description"/></td></tr>
      <tr><td>Script</td><td><xsl:value-of select="script"/></td></tr>
      <tr><td>Rule</td><td><xsl:value-of select="rule"/></td></tr>
      <tr><td>Params</td><td><xsl:value-of select="params"/></td></tr>
    </table>
</xsl:template>


<xsl:template match="query">
    <h3><xsl:value-of select="@name"/></h3>
    <table border="1" width="100%">
      <tr><td>Shared to</td><td><xsl:value-of select="@share-groups"/></td></tr>
      <tr><td>Definition</td><td><xsl:value-of select="@definition"/></td></tr>
    </table>
</xsl:template>




<!-- General HTML tags -->

<xsl:template match="h1">
    <h1><xsl:value-of select="text()"/></h1>
</xsl:template>

<xsl:template match="h2">
    <h2><xsl:value-of select="text()"/></h2>
</xsl:template>

<xsl:template match="h3">
    <h3><xsl:value-of select="text()"/></h3>
</xsl:template>

<xsl:template match="p">
    <p><xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="ul">
    <ul><xsl:apply-templates select="li"/></ul>
</xsl:template>

<xsl:template match="li">
    <li><xsl:apply-templates/></li>
</xsl:template>

<xsl:template match="b">
    <b><xsl:value-of select="text()"/></b>
</xsl:template>

<xsl:template match="hr">
    <hr/>
</xsl:template>




</xsl:stylesheet>    
