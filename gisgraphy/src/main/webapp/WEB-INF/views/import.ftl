<html>
<head>
<title><@s.text name="import.import"/></title>
</head>
<body>
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

<@s.text name="import.config.sentence"/> :<br/>

The Geonames importer is :&nbsp;<@s.if test="geonamesImporterEnabled"><span style="color:#00FF00">ENABLED</span><br/><br/><div style="margin-left: 100px;" ><@s.form action="importconfirm!disableGeonamesImporter.html" method="get" id="disableGeonames"><@s.submit title="Disable Geonames" value="Disable Geonames" theme="simple" /></@s.form></div></@s.if><@s.else><span style="color:#FF0000">DISABLED</span><br/><br/><div style="margin-left: 100px;" > <@s.form action="importconfirm!enableGeonamesImporter.html" method="get" id="enableGeonames"><@s.submit title="Enable Geonames" value="Enable Geonames" theme="simple" /></@s.form></div></@s.else>
<br/><br/><br/>

The OpenStreetMap importer is :&nbsp;<@s.if test="OpenStreetMapImporterEnabled"><span style="color:#00FF00">ENABLED</span><br/><br/><div style="margin-left: 100px;" > <@s.form action="importconfirm!disableOpenStreetMapImporter.html" method="get" id="disableopenstreetmap"><@s.submit title="Disable OpenStreetMap" value="Disable OpenStreetMap" theme="simple" /></@s.form></div></@s.if>
<@s.else><span style="color:#FF0000">DISABLED</span><br/><br/><div style="margin-left: 100px;" ><@s.form action="importconfirm!enableOpenStreetMapImporter.html" method="get" id="enableopenstreetmap"><@s.submit title="Enable OpenStreetMap" value="Enable OpenStreetMap" theme="simple" /></@s.form></div> </@s.else>
<br/><br/>

<ul>
<br/>
<li> geonamesDir : ${importerConfig.getGeonamesDir()}</li>
<li>Fulltext engine URL : ${FulltextSearchEngineURL}</li><#if !fulltextSearchEngineAlive>
<div class="tip redtip">
<@s.text name="import.fulltextEngineNotReachable" >
<@s.param>${FulltextSearchEngineURL}</@s.param>
</@s.text>
</div></#if>
<li> geonamesFilesToDownload : ${importerConfig.getGeonamesFilesToDownload()}</li>
<li> openStreetMapFilesToDownload : ${importerConfig.getOpenStreetMapFilesToDownload()}</li>
<#if !regexpCorrects><div class="tip redtip">
<@s.text name="import.incorrectRegexp"/>
</div></#if>
<li> acceptRegExString : ${importerConfig.getAcceptRegExString()}</li>
<li> retrieveFiles : ${importerConfig.isRetrieveFiles().toString()}</li>
<li> importGisFeatureEmbededAlternateNames : ${importerConfig.isImportGisFeatureEmbededAlternateNames().toString()}</li>
<li> geonamesDownloadURL : ${importerConfig.getGeonamesDownloadURL()}</li>
<li> adm1ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm1ExtracterStrategyIfAlreadyExists()}</li>
<li> adm2ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm2ExtracterStrategyIfAlreadyExists()}</li>
<li> adm3ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm3ExtracterStrategyIfAlreadyExists()}</li>
<li> adm4ExtracterStrategyIfAlreadyExists : ${importerConfig.getAdm4ExtracterStrategyIfAlreadyExists()}</li>
<li> syncAdmCodesWithLinkedAdmOnes : ${importerConfig.isSyncAdmCodesWithLinkedAdmOnes().toString()}</li>
<li> tryToDetectAdmIfNotFound : ${importerConfig.isTryToDetectAdmIfNotFound().toString()}</li>
<li> missingRequiredFieldThrows : ${importerConfig.isMissingRequiredFieldThrows().toString()}</li>
<li> wrongNumberOfFieldsThrows : ${importerConfig.isWrongNumberOfFieldsThrows().toString()}</li>
<li> adm1FileName : ${importerConfig.getAdm1FileName()}</li>
<li> adm2FileName : ${importerConfig.getAdm2FileName()}</li>
<li> adm3FileName : ${importerConfig.getAdm3FileName()}</li>
<li> adm4FileName : ${importerConfig.getAdm4FileName()}</li>
<li> countriesFileName : ${importerConfig.getCountriesFileName()}</li>
<li> languageFileName : ${importerConfig.getLanguageFileName()}</li>
<li> alternateNamesFileName : ${importerConfig.getAlternateNamesFileName()}</li>
</ul>
<br/>
<@s.text name="import.option.moreinfos" />
<br/><br/>
<@s.text name="import.confirm.sentence"/><br/><br/>                                                                                                                        
<@s.url id="importUrl" action="import" includeParams="all" />   
<a href="${importUrl}"><@s.text name="menu.admin.import"/></a>
<br/><br/><a href="mainMenu.html" onclick="history.back();return false">&#171; <@s.text name="menu.admin.dontImport"/></a>
</br>
</body>
</html>
