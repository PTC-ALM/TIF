<?xml version="1.0" encoding="UTF-8"?>
<!-- Author: Paul Bowden -->
<!-- Generate documentation index page (index.html) from im-solution.dtd conformant
     XML.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:tif="http://www.ptc.com/integrity-solution" version="1.0">

<xsl:template match="/">
    <xsl:apply-templates select="tif:im-solution"/>
</xsl:template>

<xsl:template match="tif:im-solution">
    <html>
        <head>
            <link rel="stylesheet" type="text/css" href="main.css"/>
            <title><xsl:value-of select="@title"/></title>
        </head>
        <body>
          <table border="0" width="100%" cellpadding="0" cellspacing="5">
              <tr>
                <td width="15%" valign="top" class="sidenav">
                    <xsl:apply-templates select="tif:types/tif:type" mode="url"/>
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
                             <xsl:apply-templates select="tif:doc"/>
                             <h1><a name="types">Types</a></h1>
                             <xsl:apply-templates select="tif:types/tif:type" mode="doc"/>
                             <p class="smalllink"><a href="#top">Back to top</a></p>

                             <h1><a name="fields">Fields</a></h1>
                             
                             <h2><a name="shorttext">Short Text</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Max. length</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='shorttext']" mode="textfield"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="longtext">Long Text</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Max. length</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='longtext']" mode="textfield"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="integer">Integer numeric / computed</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Computation</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='integer']" mode="numeric"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="float">Float numeric / computed</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Computation</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='float']" mode="numeric"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="pick">Picks</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Values</th><th>Relevance</th><th>Editability</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='pick']" mode="pick"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="ibpl">IBPLs</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Backing type</th><th>Field</th><th>States</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='ibpl']" mode="ibpl"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="qbr">QBRs</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Query</th><th>Correlation</th><th>Display</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='qbr']" mode="qbr"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="fva">FVAs</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Backed By</th><th>Backing Field</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='fva']" mode="fva"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="relationship">Relationships</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Relates</th><th>Reverse Name</th><th>Reverse Relates</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='relationship']" mode="relationship"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="users">Users</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Multivalued</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='user']" mode="user"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h2><a name="dates">Dates</a></h2>
                             <table width="100%" border="1">
                                 <tr><th>Name</th><th>Editability</th><th>Relevance</th><th>Description</th></tr>
                                 <xsl:apply-templates select="tif:fields/tif:field[@type='date']" mode="date"/>
                             </table>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h1><a name="states">States</a></h1>
                             <xsl:apply-templates select="tif:states/tif:state" mode="definition"/>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h1><a name="queries">Queries</a></h1>
                             <xsl:apply-templates select="tif:queries/tif:query"/>
                             <p class="smalllink"><a href="#top">Back to top</a></p>
                             
                             <h1><a name="triggers">Triggers</a></h1>
                             <xsl:apply-templates select="tif:triggers/tif:trigger"/>
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

<xsl:template match="tif:type" mode="url">
    <li><a href="#{@name}"><xsl:value-of select="@name"/></a></li>
</xsl:template>

<xsl:template match="tif:type" mode="doc">
    <hr/>
    <h2><a name="{@name}"><xsl:value-of select="@name"/></a></h2>
    <xsl:apply-templates select="tif:doc"/>
    <h3>State transitions</h3>
    <table border="1" width="100%">
        <tr><th>State</th><th>Next</th><th>Mandatory fields</th></tr>
        <xsl:for-each select="tif:states/tif:state">
            <tr>
                <td valign="top"><xsl:value-of select="@name"/></td>
                <td valign="top">
                    <xsl:if test="not(tif:next)">&#160;</xsl:if>
                    <ul>
                    <xsl:for-each select="tif:next">
                        <li><xsl:value-of select="@name"/>:<xsl:value-of select="@group"/></li>
                    </xsl:for-each>
                    </ul>
                </td>    
                <td valign="top">
                    <xsl:if test="not(tif:mandatory/tif:field)">&#160;</xsl:if>
                    <ul>
                    <xsl:for-each select="tif:mandatory/tif:field">
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
        <xsl:apply-templates select="tif:fields" mode="visible"/>
    </table>
    <p class="smalllink"><a href="#top">Back to top</a></p>
    <xsl:if test="tif:properties">
        <h3>Properties</h3>
        <table border="1" width="100%">
            <tr><th>Property</th><th>Value</th><th>Description</th></tr>
            <xsl:apply-templates select="tif:properties/tif:property"/>
        </table>
        <p class="smalllink"><a href="#top">Back to top</a></p>
    </xsl:if>
    
    <hr/>
</xsl:template>


<xsl:template match="tif:field" mode="visible">
    <tr>
        <td>
            <xsl:value-of select="@name"/>
        </td>
        <td>
            <xsl:value-of select="tif:editability/text()"/>&#160;
        </td>
        <td>
            <xsl:value-of select="tif:relevance/text()"/>&#160;
        </td>    
    </tr>
</xsl:template>

<xsl:template match="tif:property">
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


<!-- xsl:template match="tif:related">
    <li><xsl:value-of select="@name"/>, direction <xsl:value-of select="@direction"/></li>
</xsl:template -->

<xsl:template match="tif:fields">
    <h3>Visible fields</h3>
    <xsl:apply-templates select="tif:field"/>
</xsl:template>

<xsl:template match="tif:states">
    <h3>States</h3>
    <table border="1" class="list">
        <tr><th>State</th><th>Links to (allowed groups)</th><th>Description</th></tr>
        <xsl:apply-templates select="tif:state"/>
    </table>
</xsl:template>

<xsl:template match="tif:state">
    <tr>
        <td><xsl:value-of select="@name"/></td> 
        <td><xsl:value-of select="tif:next/@name"/>(<xsl:value-of select="next/@group"/>)</td>
        <td><xsl:value-of select="tif:description/text()"/></td>
    </tr>
</xsl:template>


<!-- **************** Display the different type of fields. ******************-->

<!-- **************** Short text ******************-->
<xsl:template match="tif:field" mode="textfield">
    <tr>
        <td><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@max-length"/>&#160;</td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="tif:field" mode="numeric">
    <tr>
        <td><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="tif:computation/text()"/>&#160;</td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="tif:field" mode="pick">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><ul>
            <xsl:for-each select="tif:values/tif:value">
                <li><xsl:value-of select="text()"/></li>
            </xsl:for-each>
            </ul>
        </td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>


<xsl:template match="tif:field" mode="ibpl">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="tif:backing-issue/@type"/></td>
        <td><xsl:value-of select="tif:backing-issue/@field"/></td>
        <td><xsl:value-of select="tif:backing-issue/@states"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="tif:field" mode="qbr">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="tif:query/text()"/></td>
        <td><xsl:value-of select="tif:correlation/text()"/></td>
        <td><xsl:value-of select="@display"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="tif:field" mode="fva">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@backedBy"/></td>
        <td><xsl:value-of select="@backingField"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="tif:field" mode="relationship">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="tif:relates/text()"/></td>
        <td><xsl:value-of select="tif:reverse/@name"/></td>
        <td><xsl:value-of select="tif:reverse/tif:relates/text()"/></td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>


<xsl:template match="tif:field" mode="user">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@multivalued"/>&#160;</td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>

<xsl:template match="tif:field" mode="date">
    <tr>
        <td valign="top"><b><xsl:value-of select="@name"/></b></td>
        <td><xsl:value-of select="@relevance"/>&#160;</td>
        <td><xsl:apply-templates select="@editability"/>&#160;</td>
        <td><xsl:apply-templates select="tif:description/text()"/>&#160;</td>
    </tr>
</xsl:template>




<!-- xsl:template match="tif:description">
    <p><xsl:apply-templates/></p>
</xsl:template -->

<!-- xsl:template match="tif:question">
    <p class="alert"><xsl:apply-templates/></p>
</xsl:template -->

<!-- xsl:template match="list">
    <table border="1" class="list">
        <xsl:if test="@depends-on">
            <tr><th><xsl:value-of select="@depends-on"/>=<xsl:value-of select="@type-selector"/></th></tr>
        </xsl:if>
        <xsl:apply-templates select="tif:value"/>
    </table>
</xsl:template -->

<!-- xsl:template match="tif:value">
    <tr>
        <td>
            <xsl:value-of select="text()"/>
        </td>
    </tr>
</xsl:template -->


<xsl:template match="tif:doc">
    <xsl:apply-templates/>
</xsl:template>




<xsl:template match="tif:state" mode="definition">
    <h3><xsl:value-of select="@name"/></h3>
    <table border="1" width="100%">
      <tr><td>Description</td><td><xsl:value-of select="tif:description"/></td></tr>
      <tr><td>Capabilities</td><td>
        <ul>
          <xsl:for-each select="tif:capabilities/tif:capability">
                   <li><xsl:value-of select="@capability"/></li>
          </xsl:for-each>
        </ul>
      </td></tr>
    </table>
</xsl:template>



<xsl:template match="tif:trigger">
    <h3><xsl:value-of select="@name"/> (<xsl:value-of select="@type"/>,<xsl:value-of select="@timing"/>)</h3>
    <table border="1" width="100%">
      <tr><td>Description</td><td><xsl:value-of select="tif:description"/></td></tr>
      <tr><td>Script</td><td><xsl:value-of select="tif:script"/></td></tr>
      <tr><td>Rule</td><td><xsl:value-of select="tif:rule"/></td></tr>
      <tr><td>Params</td><td><xsl:value-of select="tif:params"/></td></tr>
    </table>
</xsl:template>


<xsl:template match="tif:query">
    <h3><xsl:value-of select="@name"/></h3>
    <table border="1" width="100%">
      <tr><td>Shared to</td><td><xsl:value-of select="@share-groups"/></td></tr>
      <tr><td>Definition</td><td><xsl:value-of select="tif:definition"/></td></tr>
      <tr><td>Description</td><td><xsl:value-of select="tif:description"/></td></tr>
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
