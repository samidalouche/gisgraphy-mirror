<html>
<head>
<title><@s.text name="import.import"/></title>
</head>
<body>
<div class="tip yellowtip">
<@s.text name="import.free.disk.space">
	<@s.param>30 GO</@s.param>
</@s.text>
</div>
<#if !DownloadDirectoryAccessible>
<div class="tip redtip">
<u>IMPORTANT</u> : The download directory '${importerConfig.getGeonamesDir()}' is not accessible, you ve probably not started Gisgraphy in the correct directory or this directory is not 'writable'.<br/><br/>
<u>Example</u> : If you've specified a relative path : It is relative from the directory you were when you launch Gisgraphy.
<br/><br/>If the download directory is './data/import/', you must cd to the directory where the 'data' directory is (not the import one !) and then launch Gisgraphy.<a href="http://www.gisgraphy.com/documentation/index.htm#installation"> more</a>
<br><br/>Now, you must shutdown Gisgraphy and restart it from the correct directory to run the importer. 
</div>
</#if>

<#if !openStreetMapDownloadDirectoryAccessible>
<div class="tip redtip">
<u>IMPORTANT</u> : The openstreetmap download directory '${importerConfig.getOpenStreetMapDir()}' is not accessible, you ve probably not started Gisgraphy in the correct directory or this directory is not 'writable'.<br/><br/>
<u>Example</u> : If you've specified a relative path : It is relative from the directory you were when you launch Gisgraphy.
<br/><br/>If the download directory is './data/import/', you must cd to the directory where the 'data' directory is (not the import one !) and then launch Gisgraphy.<a href="http://www.gisgraphy.com/documentation/index.htm#installation"> more</a>
<br><br/>Now, you must shutdown Gisgraphy and restart it from the correct directory to run the importer. 
</div>
</#if>

<@s.text name="import.config.sentence"/> :<br/><br/>
<fieldset>
<legend><@s.text name="global.dataset"/></legend>
<@s.text name="import.dataset.choose" /><br/><br/>
The Geonames importer is :&nbsp;<@s.if test="geonamesImporterEnabled"><span style="color:#00FF00">ENABLED</span><br/><br/><div style="margin-left: 100px;" ><@s.form action="importconfirm!disableGeonamesImporter.html" method="get" id="disableGeonames"><@s.submit title="Disable Geonames" value="Disable Geonames" theme="simple" /></@s.form></div></@s.if><@s.else><span style="color:#FF0000">DISABLED</span><br/><br/><div style="margin-left: 100px;" > <@s.form action="importconfirm!enableGeonamesImporter.html" method="get" id="enableGeonames"><@s.submit title="Enable Geonames" value="Enable Geonames" theme="simple" /></@s.form></div></@s.else>
<br/><br/><br/>

The OpenStreetMap importer is :&nbsp;<@s.if test="OpenStreetMapImporterEnabled"><span style="color:#00FF00">ENABLED</span><br/><br/><div style="margin-left: 100px;" > <@s.form action="importconfirm!disableOpenStreetMapImporter.html" method="get" id="disableopenstreetmap"><@s.submit title="Disable OpenStreetMap" value="Disable OpenStreetMap" theme="simple" /></@s.form></div></@s.if>
<@s.else><span style="color:#FF0000">DISABLED</span><br/><br/><div style="margin-left: 100px;" ><@s.form action="importconfirm!enableOpenStreetMapImporter.html" method="get" id="enableopenstreetmap"><@s.submit title="Enable OpenStreetMap" value="Enable OpenStreetMap" theme="simple" /></@s.form></div> </@s.else>
</fieldset>

<br/><br/>
<fieldset>
<legend><@s.text name="import.options"/></legend>
<ul>
<br/>
<li class="listspace"> geonamesDir : ${importerConfig.getGeonamesDir()}</li>
<li class="listspace">Fulltext engine URL : ${FulltextSearchEngineURL}</li><#if !fulltextSearchEngineAlive>
<div class="tip redtip">
<@s.text name="import.fulltextEngineNotReachable" >
<@s.param>${FulltextSearchEngineURL}</@s.param>
</@s.text>
</div></#if>
<li class="listspace" > geonamesFilesToDownload : ${importerConfig.getGeonamesFilesToDownload()}</li>
<li class="listspace"> openStreetMapFilesToDownload : ${importerConfig.getOpenStreetMapFilesToDownload()}</li>
<#if !regexpCorrects><div class="tip redtip">
<@s.text name="import.incorrectRegexp"/>
</div></#if>
<li class="listspace"> acceptRegExString : ${importerConfig.getAcceptRegExString()}</li>
<li class="listspace"> retrieveFiles : ${importerConfig.isRetrieveFiles().toString()}</li>
<li class="listspace"> importGisFeatureEmbededAlternateNames : ${importerConfig.isImportGisFeatureEmbededAlternateNames().toString()}</li>
<li class="listspace"> geonamesDownloadURL : ${importerConfig.getGeonamesDownloadURL()}</li>
<li class="listspace"> adm1ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm1ExtracterStrategyIfAlreadyExists()}</li>
<li class="listspace"> adm2ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm2ExtracterStrategyIfAlreadyExists()}</li>
<li class="listspace"> adm3ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm3ExtracterStrategyIfAlreadyExists()}</li>
<li class="listspace"> adm4ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm4ExtracterStrategyIfAlreadyExists()}</li>
<li class="listspace" > syncAdmCodesWithLinkedAdmOnes : ${importerConfig.isSyncAdmCodesWithLinkedAdmOnes().toString()}</li>
<li class="listspace"> tryToDetectAdmIfNotFound : ${importerConfig.isTryToDetectAdmIfNotFound().toString()}</li>
<li class="listspace"> missingRequiredFieldThrows : ${importerConfig.isMissingRequiredFieldThrows().toString()}</li>
<li class="listspace"> wrongNumberOfFieldsThrows : ${importerConfig.isWrongNumberOfFieldsThrows().toString()}</li>
<li class="listspace"> adm1FileName : ${importerConfig.getAdm1FileName()}</li>
<li class="listspace"> adm2FileName : ${importerConfig.getAdm2FileName()}</li>
<li class="listspace"> adm3FileName : ${importerConfig.getAdm3FileName()}</li>
<li class="listspace"> adm4FileName : ${importerConfig.getAdm4FileName()}</li>
<li class="listspace"> countriesFileName : ${importerConfig.getCountriesFileName()}</li>
<li class="listspace"> languageFileName : ${importerConfig.getLanguageFileName()}</li>
<li class="listspace"> alternateNamesFileName : ${importerConfig.getAlternateNamesFileName()}</li>
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