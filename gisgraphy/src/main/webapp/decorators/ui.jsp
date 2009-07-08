<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ page session="false" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <%@ include file="/common/meta.jsp" %>
        <title><decorator:title/> | <fmt:message key="webapp.name"/> Free Geolocalisation Services</title>
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/ui/theme.css'/>" />
		<link rel="search" type="application/opensearchdescription+xml" title="Gisgraphy" href="/static/gisgraphy_opensearch_fulltext.xml"/>
        <decorator:head/>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
    <div id="page">
        <div id="header" class="clearfix">
            <div id="branding">
   				 <h1><a href="http://services.gisgraphy.com/"><img src="/images/logos/logo_70.png" alt="Free Geolocalisation Services"/><fmt:message key="webapp.name"/> Free Geolocalisation Services</a></h1>
    			 <div id="tagline"><fmt:message key="webapp.tagline"/></div>
			</div>
        </div>
		<div id="content" class="clearfix">
		<div class="divider"><div></div></div>
            <div id="main">
            <br/>
      <script type="text/javascript"><!--
		google_ad_client = "pub-7203216364107204";
		google_ad_slot = "0481202012";
		google_ad_width = 468;
		google_ad_height = 15;
		//-->
	</script>
	<span style="float:left;width:70px;">&nbsp;</span>
	<span style="margin-left:30px;">
<script type="text/javascript" src="http://pagead2.googlesyndication.com/pagead/show_ads.js"></script>
</span><div class="clear"><br/></div>
<div class="tip bluetip">
<strong>About</strong> : Gisgraphy is a TOTALLY FREE open source framework that provides importers, fulltext and find nearby (web)services. <a href="http://www.geonames.org/about.html" target="_blank">Data</a> (8 million entries) are also free. Results can be output in XML, Atom, RSS, JSON, PHP, Ruby, and Python. See<a href="http://www.gisgraphy.com/"> project 's home page. IMPORTANT : DON'T use this site to bench or download data. if we do this, your IP address will be list here first, and she will be BLACKLISTED if necessary.  <span style="color:#FF0000">IMPORTANT</span> : DON'T use this site to bench or download data. if we do this, your IP address will be list <a href="http://www.gisgraphy.com/abuse.txt">here</a> first, and she will be <span style="color:#ff0000">BLACKLISTED</span> if necessary.
</a>
</div>
                <decorator:body/>
        </div>
        </div>
	        <div id="footer" class="clearfix">
             <div class="divider"><div></div></div>
    <span class="left"><fmt:message key="webapp.version"/> |
        <span id="validators">
            <a href="http://validator.w3.org/check?uri=referer">XHTML Valid</a> |
            <a href="http://jigsaw.w3.org/css-validator/validator-uri.html">CSS Valid</a>
        </span>
    </span>
     <span class="right">
        <a href="http://services.gisgraphy.com/public/donate.html" target="_blank"><img src="https://www.paypal.com/en_US/i/btn/btn_donate_SM.gif" alt="donate" class="donateBtn"/></a> | <a href="/public/termsAndConditions.html"><fmt:message key="termsAndConditions"/></a> | <a href="<fmt:message key="company.url"/>"><fmt:message key="company.name"/> Project</a>  | <a href="mailto:<fmt:message key="company.mail"/>">Contact</a> | <a href="http://davidmasclet.gisgraphy.com">Blog</a> 
    </span>

        </div>
    </div>
    <script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "<fmt:message key="googleanalytics._uacctcode"/>";
urchinTracker();
</script>
</body>
</html>
