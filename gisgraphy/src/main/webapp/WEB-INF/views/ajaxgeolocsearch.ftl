<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.ajaxgeolocsearch.title"/></title>
<meta name="Description" content="free geoloc webservices demo for Geonames Data. Results are shown in an human readable way. Pagination, indentation, several languages are supported"/>
<meta name="keywords" content="geoloc find nearby GPS java geonames ajax webservices postgis hibernate toponyms gazeteers"/>
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
	
	<@s.form action="ajaxgeolocsearch!search.html" method="get" id="geolocsearch">
			<@breadcrumbs.searchNavBar/>
		<div id="simplesearch">
			<div id="searchleftblock">
				Lat (&#x2195;) : <@s.textfield name="lat" maxlength="15" required="true" size="6" theme="simple" id="lat"/>
				<span class="spacer">Long (&#x2194;) : </span><@s.textfield name="lng" maxlength="15" required="true" size="6" theme="simple" />
				<div id="searchbuttonbar">
					<span id="searchexample">e.g. '3.5', '45.2', ... </span>
					<@s.submit title="Search" value="Search" theme="simple"  onclick="return updatePopupResults()"/>
				</div>
			</div>
			<@breadcrumbs.geolocSearchTooltip advancedSearchURLParam="ajaxgeolocsearch"/>
	</div>
	<div class="clear"><br/></div>
	<div class="biggertext"><@s.text name="search.geoloc.defaultPlaceType.part1"/></div>
	<div class="biggertext"><@s.text name="search.geoloc.defaultPlaceType.part2"/></div>
	<div id="nonAjaxDisplayResults">
			<#if errorMessage!= ''>
			<div class="clear"><br/><br/></div>
				<div class="tip redtip">
					<div class="importantMessage">Error : ${errorMessage}</div>
				</div>
			<#elseif displayResults>
			<div class="clear"><br/><br/></div>
		 		<@gisgraphysearch.displayGeolocResults geolocResponseDTO=responseDTO/>
		 	</#if>
		 </div>
	
		
		 <div class="clear"><br/></div>
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
			<span class="searchfieldlabel"><@s.text name="global.radius"/> (<@s.text name="global.unit"/>) : </span>
			<@s.textfield name="radius" required="true" size="10" theme="simple" />
			<br/>
		</span>
		<div class="clear"></div>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.placetype"/> : </span>
			<@s.select headerKey="" headerValue="--Default--"  name="placetype" list="placetypes"  multiple="false" required="false"  labelposition="left" theme="simple"/>
			<br/>
		</span>
		<br/>
		<div class="clear"></div>
		<span class="advancedsearchcat"><@s.text name="search.paginationSpecs"/></span>
		<hr/>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.pagination.from"/> : </span><@s.textfield size="5" maxlength="3" name="from" required="false"  theme="simple"/> 
		</span>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.pagination.to"/> : </span><@s.textfield size="5"  name="to" maxlength="3" required="false"  theme="simple"/> 
		</span>
		<br/>
		<div class="clear"></div>
		<span class="advancedsearchcat"><@s.text name="search.outputSpecs"/></span>
		<hr/>
		<div class="clear"></div>
		<span class="searchfield">
			<@s.url id="geolocSearchServiceUrl" action="geolocsearch" includeParams="all" namespace="">
			 <@s.param name="advancedSearch" value="true" />
			</@s.url>
			<@s.text name="search.MoreOutputSpecsGeoloc"><@s.param>${geolocSearchServiceUrl}</@s.param><@s.param><@s.text name="search.geolocsearch.title"/></@s.param></@s.text>
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

 	updatePaginationNext= function(){
    $('geolocsearch')['from'].value=parseInt($('geolocsearch')['from'].value)+DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    $('geolocsearch')['to'].value=parseInt($('geolocsearch')['to'].value)+DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    return updatePopupResults();
    }
    
     updatePaginationPrevious = function(){
    $('geolocsearch')['from'].value=parseInt($('geolocsearch')['from'].value)-DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    $('geolocsearch')['to'].value=parseInt($('geolocsearch')['to'].value)-DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
    return updatePopupResults();
    }

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
        	$('geolocsearch')['from'].value=1;
        	$('geolocsearch')['to'].value=DEFAULT_NUMBER_OF_RESULTS_PER_PAGE
        }
        
 	
 	
    updatePopupResults = function(){
    try {
     if (!checkParameters('geolocsearch'))
     {
 	    return false;
     }
    var savedAction = $('geolocsearch').action;
    $('geolocsearch').action='/ajaxgeolocsearch!searchpopup.html';
    $('geolocsearch').request(
    { onComplete: displayPopupResults ,onFailure : function(transport){
	  	alert("an error has occured");
	  } }
    );
    //restore overiden parameters
    $('geolocsearch').action=savedAction;
    return false;
    }catch(e){
    alert("an error occured : " +e);
    return true;
    }
	}

</script>
</body>
</html>