<html>
<head>
	<title><@s.text name="stats.title"/></title>
</head>

<body>
<@s.text name="stats.text"/> :
<br/>
<ul>
    <@s.iterator value="statsUsages" >
     <li><@s.property value='statsUsageType' /> <@s.text name="stats.called"/> <@s.property value='usage' /> <@s.text name="stats.times"/></li>
 </@s.iterator>
 </ul>
 <br/>
</body>
</html>
