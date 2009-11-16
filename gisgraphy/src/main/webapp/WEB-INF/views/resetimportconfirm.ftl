<html>
<head>
<title><@s.text name="import.reset"/></title>
</head>
<body>
<br/>
			<div class="clear"><br/></div>
	 			<div>
	 			<@s.text name="import.reset.explanation"/>
	 			<br><br/>
					<#if !confirmed >
						<@s.text name="import.reset.confirm.explanation"/>
						<br/><br/>
                         <@s.url id="resetImportConfirmURL" action="resetimport" method="confirm"/>
						<@s.form action="${resetImportConfirmURL}" method="get" id="confirmreset">
							<@s.submit title="Confirm you want to reset" value="Confirm you want to reset" theme="simple" />
						</@s.form>
					<#else>
					<@s.text name="import.reset.confirmed"/>
					<br/><br/>
                          <@s.url id="resetImportResetURL" action="resetimport" method="reset"/>
						<@s.form action="${resetImportResetURL}" method="get" id="reset">
							<@s.submit title="Reset the imported data" value="Reset the imported data" theme="simple" />
						</@s.form>
					</#if> 
				</div>
			 <div class="clear"><br/></div>
</body>
</html>