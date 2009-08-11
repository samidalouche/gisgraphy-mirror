<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.geocoding.reverse.title"/></title>
<meta name="Description" content="Worldwide Reverse geocoding free webservices and street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.geocoding.reverse.title"/>"/>
<meta name="keywords" content=" reverse geocoding world worldwide street search free java openstreetmap webservices "/>
</head>
<body>
<br/>
<div id="gissearch">
<noscript>
	<div class="tip yellowtip">
	<@s.text name="global.noscript.required"/>
	</div>
	<br/>
</noscript>
	
			<@breadcrumbs.searchNavBar/>
	<@s.form action="reversegeocodingsearch!search.html" method="get" id="reversegeocoding">
		<div id="simplesearch">
			<@gisgraphysearch.latlongsearchbox/>
			<@breadcrumbs.geolocSearchTooltip advancedSearchURLParam="ajaxgeolocsearch"/>
	</div>
	<div class="clear"><br/></div>
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
        	$('geolocsearch')['from'].value=1;
        	$('geolocsearch')['to'].value=DEFAULT_NUMBER_OF_RESULTS_PER_PAGE
        }
        
 	
 	
    updatePopupResults = function(){
    try {
     if (!checkParameters('geolocsearch'))
     {
 	    return false;
     }
    var savedAction = $('reverse_geocoding').action;
    $('geolocsearch').action='/reverse_geocoding_worldwide!searchpopup.html';
    $('geolocsearch').request(
    { onComplete: displayPopupResults ,onFailure : function(transport){
	  	alert("an error has occured");
	  } }
    );
    //restore overiden parameters
    $('reverse_geocoding').action=savedAction;
    return false;
    }catch(e){
    alert("an error occured : " +e);
    return true;
    }
	}

</script>
</body>
</html>