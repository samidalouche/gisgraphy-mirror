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
   				 <h1><a href="http://services.gisgraphy.com/"><img src="/images/logos/logo_70.png" alt="Free Geolocalisation Services" class="imgAlign"/><decorator:getProperty property="meta.heading"/></a></h1>
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
<strong>About</strong> : Gisgraphy is a TOTALLY FREE open source framework that provides importers, fulltext and find nearby (web)services. Data <a href="http://www.geonames.org/about.html" target="_blank">Geonames</a> and <a href="http://openstreetmap.org/" target="_blank">OpenStreetMap</a> (34 million entries) are also free. Results can be output in XML, Atom, RSS, JSON, PHP, Ruby, and Python. See<a href="http://www.gisgraphy.com/"> project 's home page</a>. <span style="color:#FF0000">IMPORTANT</span> : DON'T use this site to bench or download data. if we do this, your IP address will be list <a href="http://www.gisgraphy.com/abuse.txt">here</a> first, and it will be <span style="color:#ff0000">BLACKLISTED</span> if necessary.
</a>
</div>
                <decorator:body/>
        </div>
        </div>
	        <div id="footer" class="clearfix">
             <div class="divider"><div></div></div>
    <span class="left"><fmt:message key="webapp.version"/> |<span><a href="mailto:davidmasclet@gisgraphy.com?subject=feedback" class="underline red">We Love feedback</a> | </span>
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
<div id="otherlang" class="clearfix">
<br/><br/>
<fmt:message key="global.availablelang"/> : <span><a href="/?locale=en"><img src="/images/languages/EN_US.png"  alt="Gisgraphy in english"></a></span> |
<span><a href="/?locale=fr_fr"><img src="/images/languages/FR.png"  alt="Gisgraphy en francais"></a></span> |
<span><a href="/?locale=es"><img src="/images/languages/ES.png"  alt="Gisgraphy in spanish"></a></span> |
<span><a href="/?locale=de"><img src="/images/languages/DE.png"  alt="Gisgraphy in German"></a></span> |
<span><a href="/?locale=it"><img src="/images/languages/IT.png"  alt="Gisgraphy in Italian"></a></span> |
<span><a href="/?locale=cn"><img src="/images/languages/CN.png"  alt="Gisgraphy in Chinese"></a></span> |
<span><a href="/?locale=JP"><img src="/images/languages/JP.png"  alt="Gisgraphy in Japanese"></a></span>
<span><a href="/?locale=nl"><img src="/images/languages/NL.png"  alt="Gisgraphy in Deutsh"></a></span> |
<span><a href="/?locale=pt"><img src="/images/languages/PT.png"  alt="Gisgraphy in portuguese"></a></span> |
<span><a href="/?locale=no"><img src="/images/languages/NO.png"  alt="Gisgraphy in Norwegian"></a></span> |
<span><a href="/?locale=tr"><img src="/images/languages/TR.png"  alt="Gisgraphy in Turkish"></a></span> |
<span><a href="/?locale=kr"><img src="/images/languages/KR.png"  alt="Gisgraphy in Korean"></a></span> |
<br/> <a href="mailto:davidmasclet@gisgraphy.com?subject=help for translation">We need help for translation</a>
</div>
<script type="text/javascript">
_uacct = "<%= com.gisgraphy.domain.valueobject.GisgraphyConfig.googleanalytics_uacctcode %>";
urchinTracker();
</script>
</body>
</html>
