<html>
<head>
<title><@s.text name="import.reset"/></title>
</head>
<body>
<br/>
			<div class="clear"><br/></div>
	 			<div>
	 			<#if resetFailed>
	 				<@s.text name="import.reset.failed"/> : ${failedMessage}
	 			<#else>
	 				<@s.text name="import.reset.done"/>
	 				<br><br/>
					<#if deletedObjectsInfo.size()!=0 >
						<@s.text name="import.reset.deleted"/>
		 				<br><br/>
		 				<ul>
							<#list DeletedObjectsInfo as deletedObject>
								<li>${deletedObject.name} : ${deletedObject.value}</li>
							</#list>
						</ul>
					<#else>
						<@s.text name="import.reset.nothing"/>
					</#if>
				</#if>
				</div>
			 <div class="clear"><br/></div>
</body>
</html>