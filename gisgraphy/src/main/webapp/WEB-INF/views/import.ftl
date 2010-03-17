<html>
<head>
<title><@s.text name="import.import"/></title>
</head>
<body>



<div class="tip yellowtip">
<@s.text name="import.free.disk.space">
	<@s.param>45 GO</@s.param>
</@s.text>
</div>
<#if !DownloadDirectoryAccessible>
<div class="tip redtip">
<@s.text name="import.directory.not.accesible"><@s.param>Geonames</@s.param><@s.param>${importerConfig.getOpenStreetMapDir()}</as.param></@s.text> 
</div>
</#if>

<#if !openStreetMapDownloadDirectoryAccessible>
<div class="tip redtip">
<@s.text name="import.directory.not.accesible"><@s.param>Openstreetmap</@s.param><@s.param>${importerConfig.getOpenStreetMapDir()}</as.param></@s.text> 
</div>
</#if>

<@s.text name="import.config.sentence"/> :<br/><br/>
<fieldset>
<legend><@s.text name="global.dataset"/></legend>
<@s.text name="import.dataset.choose" /><br/><br/>
The Geonames importer is :&nbsp;<@s.if test="geonamesImporterEnabled">
<span style="color:#00FF00">ENABLED</span><br/><br/><div style="margin-left: 100px;" >
<@s.form action="importconfirm!disableGeonamesImporter.html" method="get" id="disableGeonames">
<@s.submit title="Disable Geonames" value="Disable Geonames" theme="simple" /></@s.form></div></@s.if>
<@s.else><span style="color:#FF0000">DISABLED</span><br/><br/><div style="margin-left: 100px;" > 
<@s.form action="importconfirm!enableGeonamesImporter.html" method="get" id="enableGeonames">
<@s.submit title="Enable Geonames" value="Enable Geonames" theme="simple" /></@s.form></div></@s.else>
<br/><br/><br/>

The OpenStreetMap importer is :&nbsp;<@s.if test="OpenStreetMapImporterEnabled"><span style="color:#00FF00">ENABLED</span><br/><br/><div style="margin-left: 100px;" > <@s.form action="importconfirm!disableOpenStreetMapImporter.html" method="get" id="disableopenstreetmap"><@s.submit title="Disable OpenStreetMap" value="Disable OpenStreetMap" theme="simple" /></@s.form></div></@s.if>
<@s.else><span style="color:#FF0000">DISABLED</span><br/><br/><div style="margin-left: 100px;" ><@s.form action="importconfirm!enableOpenStreetMapImporter.html" method="get" id="enableopenstreetmap"><@s.submit title="Enable OpenStreetMap" value="Enable OpenStreetMap" theme="simple" /></@s.form></div> </@s.else>
</fieldset>

<#if !fulltextSearchEngineAlive>
<div class="tip redtip">
<@s.text name="import.fulltextEngineNotReachable" >
<@s.param>${FulltextSearchEngineURL}</@s.param>
</@s.text>
</div></#if>

<br/><br/>
<#if !regexpCorrects><div class="tip redtip">
<@s.text name="import.incorrectRegexp"/>
</div></#if>

<fieldset>
<legend><@s.text name="import.options"/></legend>
<ul>
<@s.iterator value="configValuesMap.keySet()" id="item" >
<li class="listspace">${item} = ${configValuesMap[item]}<br/></li>
</@s.iterator>

</ul>
</fieldset>
<br/>
<@s.text name="import.option.moreinfos" />
<br/><br/>
<@s.text name="import.confirm.sentence"/><br/><br/>                                                                                                                        
<@s.url id="importUrl" action="import" includeParams="all" /> 

<@s.form action="${importUrl}" method="get" id="runImport"><@s.submit  value="%{getText('menu.admin.import')} >>" theme="simple" /></@s.form>
&nbsp; &nbsp;
 <@s.form action="mainMenu.html" method="get" id="dontRunImport"><@s.submit  value="<< %{getText('menu.admin.dontImport')}" theme="simple"  onclick="history.back();return false" /></@s.form>

<br/>
<div class="tip greentip">
<@s.text name="gisgraphy.ask.for.dump">
	<@s.param>davidmasclet@gisgraphy.com</@s.param>
</@s.text>
</div>
<br/><br/>
</br>
</body>
</html>