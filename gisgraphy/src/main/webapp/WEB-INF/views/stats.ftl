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
<br/> 
Others :
<ul>
<li><a href="https://www.google.com/analytics/settings/?et=reset&hl=en" target="statsgis">Google analytics</a></li>
<li><a href="https://www.google.com/adsense/report/overview" target="statsgis">Google adsense</a></li>
<li><a href="http://code.google.com/p/gisgraphy/downloads/list" target="statsgis">Google Code (Download)</a></li>
<li><a href="https://www.google.com/webmasters/tools/siteoverview?hl=fr&pli=1" target="statsgis">Google webmaster tools</a></li>
<li><a href="http://www.google.fr/search?as_q=gisgraphy&hl=fr&num=10&btnG=Recherche+Google&as_epq=&as_oq=&as_eq=&lr=&cr=&as_ft=i&as_filetype=&as_qdr=w&as_occt=any&as_dt=i&as_sitesearch=&as_rights=&safe=off" target="statsgis">New links to gisgraphy</a></li>
</ul>
 <br/>
</body>
</html>
