<html>
<head>
	<title><@s.text name="import.import"/></title>
  	<meta http-equiv="refresh" content="60;url=<@s.url id='thisUrl' action='import' />"/>
</head>

<body>
    <p style="border: 1px solid silver; padding: 5px; background: #ffd; text-align: center;margin-left:auto;margin-right:auto;">
       
      <@s.if test="importerManager.isInProgress()">
      <@s.text name="import.processingRequest"/>
     <br/><img src="/images/loading.gif" width=20px /> <@s.text name="import.wait.importSince"/> ${importerManager.getFormatedTimeElapsed()}<br/> 
     <br/><@s.text name="import.time.info"/>
       
    </@s.if>
     <@s.if test="importerManager.isAlreadyDone()">
     <br/><@s.text name="import.took.time"/> ${importerManager.getFormatedTimeElapsed()}  
    </@s.if>
    
    </p>
   
    <table style="width:100%;border:1px solid;">
    <tr>
    <th><@s.text name="import.importer.label" /></th>
    <th><@s.text name="import.status.label" /></th>
    <@s.iterator value="importerStatusDtoList" >
     <tr>
         <td> <img src="/images/<@s.property value='status' />.png" alt="<@s.property value='status' />" title="<@s.property value='status' />"/><@s.property value="processorName" /><br/></td>
         <td>
	         <@s.property value="percent" />% :
		         <table style="width:100px;border:1px solid;padding:0px;margin:0px;vertical-align:middle;" ><tr>
		         <td style="width:${percent}%;background-color:#00DD00;padding:0px;margin:0px"> </td>
		         <td></td>
		         </tr>
		         </table>
	         <@s.if test="status.toString().equals('PROCESSING')">
	         <@s.property value="numberOfLineProcessed" /> / <@s.property value="numberOfLineToProcess" /> (<@s.property value="numberOfLinelefts" /> left(s))
	         <br/><@s.text name="import.currently.sentence" /> <@s.property value="currentFileName" /> <@s.text name="import.line.sentence" /> <@s.property value="currentLine" />
	         </@s.if>
	         <@s.if test="status.toString().equals('ERROR')">
	         <@s.property value="numberOfLineProcessed" /> / <@s.property value="numberOfLineToProcess" /> (<@s.property value="numberOfLinelefts" /> left(s))
	         <br/><span style="color:#FF0000">Error : <@s.property value="statusMessage" /></span>
	         </@s.if>
         </td>
     </tr>
 </@s.iterator>
 </table>

    
	</p>
	<@s.text name="global.legend"/> :<br/>
	<blockquote><@s.iterator value="statusEnumList" var="statusEnumValue">
		<img src="/images/<@s.property />.png" alt="<@s.property/>" title="<@s.property />"/> : <@s.property/>&nbsp;&nbsp;;<br/>
	</@s.iterator>
	</blockquote>
	<br/><br/>
    <p/>
    <@s.url id="thisUrl" action="import" includeParams="all" />
    <@s.text name="import.refreshText"/> <a href="${thisUrl}"><@s.text name="global.refresh"/></a>.
	
</body>
</html>
