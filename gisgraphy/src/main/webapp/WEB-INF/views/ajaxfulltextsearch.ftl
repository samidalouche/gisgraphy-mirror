<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.ajaxfulltext.title"/></title>
<meta name="Description" content="Free fulltext webservices demo for Geonames Data. Results are shown in an human readable way. Pagination, indentation, several languages are supported"/>
<meta name="keywords" content="fulltext java geonames ajax webservices lucene solr hibernate toponyms gazeteers"/>
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
	
	<@s.form action="ajaxfulltextsearch!search.html" method="get" id="fulltextsearch">
			<@breadcrumbs.searchNavBar/>
		<div id="simplesearch">
			<div id="searchleftblock">
				<@s.textfield name="q" required="true" size="30" theme="simple" id="searchTerms" maxlength="200"/>
				<div id="searchbuttonbar">
						<span id="searchexample">e.g. Paris, الرباط ,75000,  ... </span>
					<@s.submit title="Search" value="Search" theme="simple" onclick=" return updatePopupResults()" />
				</div>
			</div>
			<@breadcrumbs.fulltextSearchTooltip advancedSearchURLParam="ajaxfulltextsearch"/>
		</div>
		<div id="nonAjaxDisplayResults">
			<#if errorMessage!= ''>
			<div class="clear"><br/><br/></div>
				<div class="tip redtip">
					<div class="importantMessage"><@s.text name="global.error"/> : ${errorMessage}</div>
				</div>
			<#elseif displayResults>
			<div class="clear"><br/><br/></div>
		 		<@gisgraphysearch.displayFulltextResults fulltextResponseDTO=responseDTO/>
		 	</#if>
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
			<@s.select headerKey="" headerValue="--Any place--"  name="placetype" list="placetypes"  multiple="false" required="false"  labelposition="left" theme="simple"/>
			<br/>
		</span>
		<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.country"/> : </span><@s.select label="In " listKey="iso3166Alpha2Code" listValue="name" name="country" list="countries" headerValue="--All countries--" headerKey="" multiple="false" required="false" labelposition="left" theme="simple" /> 
			<br/>
		</span>
		 <div class="clear"></div>
		<span class="advancedsearchcat"><@s.text name="search.outputSpecs"/></span>
		<hr/>
		<span class="searchfield">
			<@s.url id="fulltextSearchServiceUrl" action="fulltextsearch" includeParams="all" namespace="" >
			 <@s.param name="advancedSearch" value="true" />
			</@s.url>
			<@s.text name="search.MoreOutputSpecsFulltext"><@s.param>${fulltextSearchServiceUrl}</@s.param><@s.param><@s.text name="search.fulltext.title"/></@s.param></@s.text>
		<@s.hidden size="5" maxlength="3" name="to" required="false"  theme="simple"/>
		<@s.hidden size="5" maxlength="3" name="from" required="false"  theme="simple"/>
		</span>
	</fieldset>
	</div>
	</@s.form>
</div>
<div id="popupResults"></div>

<script src="/scripts/prototype.js" type="text/javascript"></script>
<script src="/scripts/gisgraphyapi.js" type="text/javascript"></script>

<script type="text/javascript" >
	
		DEFAULT_NUMBER_OF_RESULTS_PER_PAGE=${defaultNumberOfResultsPerPage?c};

 	 	displayPopupResults = function(transport){
	 	 	 if (transport.responseText){
	 	 	 $('nonAjaxDisplayResults').update("");
		     	$('popupResults').update(transport.responseText);
		     	$('popupResults').show();
		     	 Event.observe('closePopupResultsPopupButton','click',closePopupResults);
		     	 Event.observe(document,'keydown',function(e){
		     	 	var code;
					if (!e) var e = window.event;
					if (e.keyCode) code = e.keyCode;
					else if (e.which) code = e.which;
					if (code=27) {
						closePopupResults();
					}
		     	 }
		     	 );
		     	 return false;
		   	 } else {
		      alert("No response from the server");
		      return true;
		     }
        }
        
        closePopupResults = function(){
        	$('popupResults').hide();
        	$('popupResults').update("");
        	$('fulltextsearch')['from'].value=1;
        	$('fulltextsearch')['to'].value=DEFAULT_NUMBER_OF_RESULTS_PER_PAGE
        	
        }
        
    updatePaginationNext= function(){
    $('fulltextsearch')['from'].value=parseInt($('fulltextsearch')['from'].value)+DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    $('fulltextsearch')['to'].value=parseInt($('fulltextsearch')['to'].value)+DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    return updatePopupResults();
    }
    
     updatePaginationPrevious = function(){
    $('fulltextsearch')['from'].value=parseInt($('fulltextsearch')['from'].value)-DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    $('fulltextsearch')['to'].value=parseInt($('fulltextsearch')['to'].value)-DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    return updatePopupResults();
    }
 	
 	
    updatePopupResults = function(){
    try {
     if (!checkParameters('fulltextsearch'))
     {
 	    return false;
     }
    var savedAction = $('fulltextsearch').action;
    $('fulltextsearch').action='/ajaxfulltextsearch!searchpopup.html';
    $('fulltextsearch').request(
    { onComplete: displayPopupResults ,
    onFailure : function(transport){
	  	alert("an error has occured");
	  } }
    );
    //restore overiden parameters
    $('fulltextsearch').action=savedAction;
    return false;
    }catch(e){
    alert("an error occured : " +e);
    return true;
    }
	}

doAjaxSearch = function(formName){
	var query = new GisgraphyQuery(formName);
	query.setParameter('format','JSON');
	query.setURL('/fulltext/fulltextsearch');
	query.execute();
	return false;
}
</script>
</body>
</html>