<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<html>
<head>
<title><@s.text name="search.fulltext.title"/></title>
<meta name="Description" content="Free fulltext webservices for Geonames Data. Results can be output in XML, json, PHP, ruby, python, Atom, RSS/GeoRSS. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.fulltext.title"/>"/>
<meta name="keywords" content="fulltext java geonames webservices lucene solr hibernate toponyms gazeteers"/>
</head>
<body>
<br/>
<div id="gissearch">
<noscript>
<div class="tip yellowtip">
<@s.text name="global.noscript"/>
</div>
<br/>
</noscript>
	
	<@s.form action="/fulltext/fulltextsearch" method="get" id="fulltextsearch">
			<@breadcrumbs.searchNavBar/>
		<div id="simplesearch">
			<div id="searchleftblock">
				<@s.textfield name="q" required="true" size="30" theme="simple" maxlength="200" />
				<div id="searchbuttonbar">
						<span id="searchexample">e.g. Paris, الرباط ,75000,  ... </span>
					<@s.submit title="Search" value="Search" theme="simple"/>
				</div>
			</div>
			<@breadcrumbs.fulltextSearchTooltip advancedSearchURLParam="fulltextsearch"/>
	</div>
		<br/>
		 <div class="clear"></div>
		 <@breadcrumbs.opensearchFulltext/>
		 <@s.if test="advancedSearch">
			<div id="advancedsearch" >
		</@s.if>
		<@s.else>
    		<div id="advancedsearch" style="display:none;" >
		</@s.else>
	<fieldset >
		<legend>&nbsp; <@s.text name="search.advanced"/> &nbsp; </legend>
		<span class="advancedsearchcat"><@s.text name="search.moreCriteria"/></span>
		<hr/>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.placetype"/> : </span>
			<@s.select headerKey="" headerValue="--any place--"  name="placetype" list="placetypes"  multiple="false" required="false"  labelposition="left" theme="simple"/>
			<br/>
		</span>
		<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.country"/> : </span><@s.select label="In " listKey="iso3166Alpha2Code" listValue="name" name="country" list="countries" headerValue="--All countries--" headerKey="" multiple="false" required="false" labelposition="left" theme="simple" /> 
			<br/>
		</span>
		<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.spellChecking"/> : </span><@s.checkbox label="spellchecking" labelposition="left" name="spellchecking" theme="simple" />
		</span>
		<div class="clear"></div>
		<span class="advancedsearchcat"><@s.text name="search.outputSpecs"/></span>
		<hr/>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.language"/> : </span><@s.select name="lang" list="languages" headerValue="--No specific--" headerKey="" multiple="false" required="false" labelposition="left" theme="simple"/>
			<@s.text name="search.language.info"/>.<@s.text name="search.language.style.info"/>.
		</span>
		<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.format"/> : </span><@s.select label="Output in " name="format" list="formats" multiple="false" required="false" labelposition="left" theme="simple" />
		</span>
		<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.verbosity"/> : </span><@s.select label="With a verbosity " name="style" list="verbosityModes" multiple="false" required="false" labelposition="left" theme="simple" />
		</span>
		<br/>
		<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.indent"/> : </span><@s.checkbox label="Indent output" labelposition="left" name="indent" theme="simple" />
		</span>
		<div class="clear"></div>
		<span class="advancedsearchcat"><@s.text name="search.paginationSpecs"/></span>&nbsp;&nbsp;<@s.text name="search.pagination.info"/>
		<hr/>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.pagination.from"/> : </span><@s.textfield size="5" maxlength="3" name="from" required="false"  theme="simple"/> 
		</span>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.pagination.to"/> : </span><@s.textfield size="50" maxlength="3" name="to" required="false"  theme="simple"/> 
		</span>
		
	</fieldset>
	</div>
	</@s.form>
	<script src="/scripts/prototype.js" type="text/javascript"></script>
</div>
</body>
</html>