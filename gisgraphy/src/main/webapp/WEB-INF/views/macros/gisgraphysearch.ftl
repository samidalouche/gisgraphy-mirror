<#macro displayFulltextResults fulltextResponseDTO>
			<div id="searchResults">
			<div class="clear"><br/></div>
			<div class="bigText indented">${fulltextResponseDTO.numFound} <@s.text name="search.resultFound"/>. (<@s.text name="search.resultPaginateFromTo"><@s.param>${from}</@s.param><@s.param>${to}</@s.param></@s.text>).
			 <@s.text name="search.requestTime"/> ${fulltextResponseDTO.QTime/1000} <@s.text name="search.secondUnit"/>. <br/>
			 <@s.text name="search.MaxScore"><@s.param>${fulltextResponseDTO.maxScore}</@s.param></@s.text></div>
			<#if fulltextResponseDTO.results.size()!=0>
			<br/>
			<@s.url id="showAllOnMapsURL" value="fulltext/fulltextsearch" includeParams="all" forceAddSchemeHostAndPort="true" escapeAmp="false" />			
			&nbsp;&nbsp;<a href="http://maps.google.fr/maps?q=${showAllOnMapsURL?url('UTF-8')}%26format%3DATOM" target="_blank"><img src="/images/map_go.png" alt="map"/> <@s.text name="search.viewResultsOnMap"/></a>
				<#list fulltextResponseDTO.results as result>
	 			<div class="bodyResults">
					<div class="flag" >
						<img src="${result.country_flag_url}" alt="country flag"/>
					</div>
					<div class="resultblock">
					<div>
							<div class="resultheaderleft">
							<@s.url id="featureURL" action="displayfeature" includeParams="none" >
			  					<@s.param name="featureId" value="${result.feature_id?c}" />
			 				</@s.url>
							<a href="${featureURL}">${result.name} <#if result.country_name??>(${result.country_name})</#if></a>
							</div>
							<div class="resultheaderright"><@s.text name="${result.feature_class}_${result.feature_code}"/></div>
					</div>
					
					<div class="separator"><hr/></div>
					
					<div class="summary">
					<@s.text name="global.typeDescription"/> : <@s.text name="${result.feature_class}_${result.feature_code}"/><br/>
					<@s.text name="global.featureClassCode"/> : ${result.feature_class}.${result.feature_code}<br/>
					${result.fully_qualified_name}<br/>
					<@s.text name="global.latitude"/> : ${result.lat}; <@s.text name="global.longitude"/> : ${result.lng}<br/>
					<#if result.population??><@s.text name="global.population"/> : ${result.population};<br/></#if>
					<#if result.elevation??><@s.text name="global.elevation"/> : ${result.elevation} m<br/></#if>
					<img src="images/world_link.png" alt="Maps links " />&nbsp;<a href="${result.google_map_url}" class="greenlink" target="gisgraphyMap"><@s.text name="global.viewOnGoogleMap"/></a> | <a href="${result.yahoo_map_url}" class="greenlink" target="gisgraphyMap"><@s.text name="global.viewOnYahooMap"/></a>
					 <@s.url id="proximitySearchUrl" action="ajaxgeolocsearch" forceAddSchemeHostAndPort="true" method="search" includeParams="none" >
			  			<@s.param name="lat" value="${result.lat?c}" />
			  			<@s.param name="lng" value="${result.lng?c}" />
			 		</@s.url>
					 | <a href="${proximitySearchUrl}" class="greenlink"><@s.text name="global.findNearestCity"/></a>
					</div>
					</div>
					<div class="clear"></div>
					<br/><br/>
					</div>
				</#list> 
				<#if (from > 1)>
				<span style="float:left;padding-left:15px;"><@s.url id="previousURL" action="ajaxfulltextsearch" method="search" includeParams="all" >
			  			<@s.param name="from" value="${from?c}-${defaultNumberOfResultsPerPage?c}" />
			  			<@s.param name="to" value="${to?c}-${defaultNumberOfResultsPerPage?c}" />
			 		</@s.url><a href="${previousURL}" class="bigText strong" onclick="return updatePaginationPrevious();" alt="previous" >&lt;&lt;<@s.text name="global.previous"/></a></span>
			 	</#if>
			 	<#if ((from + fulltextResponseDTO.resultsSize) < fulltextResponseDTO.numFound)>
				<span style="float:right;padding-right:15px;"><@s.url id="nextURL" action="ajaxfulltextsearch!search" includeParams="all" >
			  			<@s.param name="from" value="${from?c}+${defaultNumberOfResultsPerPage?c}" />
			  			<@s.param name="to" value="${to?c}+${defaultNumberOfResultsPerPage?c}" />
			 		</@s.url><a href="${nextURL}" class="bigText strong" onclick="return updatePaginationNext();" alt="next"><@s.text name="global.next"/>&gt;&gt;</a></span>
			 	</#if>
			 		<div class="clear"><br/></div>
			</div>
				<#else>
			<br/>
			 <div>
			 <#if fulltextResponseDTO.collatedResult??>
			 <@s.url id="spellURL" action="ajaxfulltextsearch!search" includeParams="all" >
			  			<@s.param name="q" value="" />
			  			<@s.param name="from" value="1" />
			  			<@s.param name="to" value="${defaultNumberOfResultsPerPage?c}" />
			 </@s.url>
			 <br/>
			 <span class="spell"><@s.text name="search.spellChecking.proposalSentence"/></span> : <a href="${spellURL}&q=${fulltextResponseDTO.spellCheckProposal}" onclick="return executeSpellSearch('${fulltextResponseDTO.spellCheckProposal}');" alt="search.spellChecking.proposalSentence" class="spellLink">${fulltextResponseDTO.spellCheckProposal}</a> 
			<#if !(fulltextResponseDTO.collatedResult.equals(fulltextResponseDTO.spellCheckProposal.trim()))>,
 <a href="${spellURL}&q=${fulltextResponseDTO.collatedResult}" onclick="return executeSpellSearch('${fulltextResponseDTO.collatedResult}');" alt="search.spellChecking.proposalSentence" class="spellLink">${fulltextResponseDTO.collatedResult}</a> 
			</#if>
			 <br/>
			 <br/>
 			<br/>
			 <br/>
			 </div>
			 </#if>
			 <div class="bigText indented"><@s.text name="search.noresultMessage.part1"/><a href="http://www.geonames.org" target="geonames">Geonames page</a>. <@s.text name="search.noresultMessage.part2"/></div>
		</#if>
</#macro>

<#macro displayGeolocResults geolocResponseDTO>
			<div id="searchResults">
				<div class="clear"><br/></div>
				<div class="bigText indented">${geolocResponseDTO.numFound} <@s.text name="search.resultFound"/>. (<@s.text name="search.resultPaginateFromTo"><@s.param>${from}</@s.param><@s.param>${to}</@s.param></@s.text>).
				 <@s.text name="search.requestTime"/> ${geolocResponseDTO.QTime/1000}  <@s.text name="search.secondUnit"/>. </div>
				<#if geolocResponseDTO.result.size()!=0>
				<br/>
				<@s.url id="showAllOnMapsURL" value="geoloc/geolocsearch" includeParams="all" forceAddSchemeHostAndPort="true" escapeAmp="false" />			
			&nbsp;&nbsp;<a href="http://maps.google.fr/maps?q=${showAllOnMapsURL?url('UTF-8')}%26format%3DATOM" target="_blank"><img src="/images/map_go.png" alt="map"/> <@s.text name="search.viewResultsOnMap"/></a>
					<#list geolocResponseDTO.result as result>
	 				<div class="bodyResults">
						<div class="flag" >
							<img src="${result.country_flag_url}" alt=" country flag"/>
						</div>
						<div class="resultblock">
							<@s.url id="featureURL" action="displayfeature" includeParams="none" >
				  					<@s.param name="featureId" value="${result.featureId?c}" />
				 				</@s.url>
								<div class="resultheaderleft"><a href="${featureURL}">${result.name} (${result.countryCode})</a> : ${result.distance} <@s.text name="search.unit.meter"/></div>
								<div class="resultheaderright"><@s.text name="${result.featureClass}_${result.featureCode}"/></div>
						</div>
					
						<div class="separator"><hr/></div>
					
						<div class="summary">
						Type description: <@s.text name="${result.featureClass}_${result.featureCode}"/><br/>
						FeatureClass/Code : ${result.featureClass}.${result.featureCode}<br/>
						Latitude : ${result.lat}; 
						<br/>
						Longitude : ${result.lng}<br/>
						<#if result.population??>Population: ${result.population};<br/></#if>
						<#if result.elevation??>Elevation : ${result.elevation} m<br/></#if>
						<img src="images/world_link.png" alt="Maps links " />&nbsp;<a href="${result.google_map_url}" class="greenlink" target="gisgraphyMap"><@s.text name="global.viewOnGoogleMap"/></a> | <a href="${result.yahoo_map_url}" class="greenlink" target="gisgraphyMap"><@s.text name="global.viewOnYahooMap"/></a>
						</div>
					</div>
					<div class="clear"></div>
					<br/><br/>
				</#list>
				<#if (from > 1)>
				<span style="float:left;padding-left:15px;"><@s.url id="previousURL" action="ajaxgeolocsearch" method="search" includeParams="all" >
			  			<@s.param name="from" value="${from?c}-${defaultNumberOfResultsPerPage?c}" />
			  			<@s.param name="to" value="${to?c}-${defaultNumberOfResultsPerPage?c}" />
			 		</@s.url><a href="${previousURL}" class="bigText strong" onclick="return updatePaginationPrevious();" alt="previous">&lt;&lt;<@s.text name="global.previous"/></a></span>
			 	</#if>
			 	<#if defaultNumberOfResultsPerPage==geolocResponseDTO.numFound>
				<span style="float:right;padding-right:15px;"><@s.url id="nextURL" action="ajaxgeolocsearch" method="search" includeParams="all" >
			  			<@s.param name="from" value="${from?c}+${defaultNumberOfResultsPerPage?c}" />
			  			<@s.param name="to" value="${to?c}+${defaultNumberOfResultsPerPage?c}" />
			 		</@s.url><a href="${nextURL}" class="bigText strong" onclick="return updatePaginationNext();" alt="next"><@s.text name="global.next"/>&gt;&gt;</a></span>
			 	</#if>
			<#else>
			
			<br/><br/><br/>
			  <div class="importantMessage indented"><@s.text name="search.noResult"/>!!<br/><br/><br/><br/></div>
			 <div class="bigText indented"> <@s.text name="search.noresultMessage.part1"/><a href="http://www.geonames.org" target="geonames">Geonames page</a><@s.text name="search.noresultMessage.part2"/></div>
		</#if>
		</div>
</#macro>

<#macro latlongsearchbox >
<div id="searchleftblock">
				Lat (&#x2195;) : <@s.textfield name="lat" maxlength="15" required="true" size="6" theme="simple" id="lat"/>
				<span class="spacer">Long (&#x2194;) : </span><@s.textfield name="lng" maxlength="15" required="true" size="6" theme="simple" />
				<div id="searchbuttonbar">
					<span id="searchexample">e.g. '3.5', '45.2', ... </span>
					<@s.submit title="Search" value="Search" theme="simple"  onclick="return doSearch()"/>
				</div>
			</div>
</#macro>

<#macro googleStreetPanorama width heigth googleMapAPIKey CSSClass >
<script src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=${googleMapAPIKey} "
            type="text/javascript"></script>
 			<div name="streetpanorama" id="streetpanorama" class="${CSSClass}"></div>
			<script type="text/javascript">
		  
		    var pano;
		    
		    function viewStreetPanorama(lat, lng) {
			$('streetpanorama').setStyle({ 
				width: '${width}px',
				height: '${heigth}px'
			});
		      var latlong = new GLatLng(lat,lng);
		      panoramaOptions = { latlng:latlong };
		      pano = new GStreetviewPanorama(document.getElementById("streetpanorama"), panoramaOptions);
		      GEvent.addListener(pano, "error", handleStreetPanoramaError);
		    }
		    
		    function handleStreetPanoramaError(errorCode) {
		      if (errorCode == GStreetviewPanorama.ErrorValues.FLASH_UNAVAILABLE) {
			alert("Error: Flash doesn't appear to be supported by your browser");
			return;
		      }
		      if (errorCode == GStreetviewPanorama.ErrorValues.NO_NEARBY_PANO) {
			return;
		      }
		else {
			alert ('An unknow error has occured on viewStreetPanorama : '+errorCode);		
		}
		    }  
		</script>

</#macro>

<#macro googleStreetView width heigth googleMapAPIKey CSSClass >
<script src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=${googleMapAPIKey} "
            type="text/javascript"></script>
 			<div name="streetview" id="streetview" class="${CSSClass}"></div>
			<script type="text/javascript">
		  
		    var map;
		    
		    function viewStreet(lat, lng) {
			// try {
       
     

			$('streetview').setStyle({ 
				width: '${width}px',
				height: '${heigth}px'
			});
		     var map = new GMap2(document.getElementById("streetview"));
			var latlong = new GLatLng(lat, lng);
			map.setCenter(latlong, 15);
			svOverlay = new GStreetviewOverlay();
			map.addOverlay(svOverlay);
			 var baseIcone = new google.maps.Icon();
			 baseIcone.iconSize=new google.maps.Size(12,20);
			 baseIcone.shadowSize=new google.maps.Size(20,22);
			 baseIcone.iconAnchor=new google.maps.Point(6,20);
			 baseIcone.infoWindowAnchor=new google.maps.Point(5,1);			
			iconeRouge = new google.maps.Icon(baseIcone, 'http://labs.google.com/ridefinder/images/mm_20_red.png', null, 'http://labs.google.com/ridefinder/images/mm_20_shadow.png');
			var marqueur = new google.maps.Marker(latlong, {icon: iconeRouge, title: "gisgraphy geocoding"});
			
			displayInfoWindowHTML = function() {
			var html = '<div id="EmplacementStreetView" class="googlemapInfoWindowHtml"><img src="/images/logos/logo_32.png" alt="free geocoding services" class="imgAlign"/><span  class="biggertext"><@s.text name="search.geocoding.services"/></span><hr/><span  class="biggertext">'+selectedStreetInformation.name+'</span><br/><br/>';
if (selectedStreetInformation.streetType != null){html= html + "<@s.text name="search.type.of.street"/> : "+selectedStreetInformation.streetType;}
 if (selectedStreetInformation.oneWay==true){html = html+ '<@s.text name="street.oneway" />'; } else { html = html +'<@s.text name="street.twoway" />';}
html= html +'<br/><br/> <@s.text name="global.latitude" /> : '+selectedStreetInformation.lat+'<br/><br/><@s.text name="global.longitude" /> : '+selectedStreetInformation.lng+'<br/><br/> <@s.text name="global.length" /> : '+(selectedStreetInformation.length*100000)+' m</div>';
			marqueur.openInfoWindowHtml(html);
			}
			
			
			google.maps.Event.addListener(marqueur, 'click', displayInfoWindowHTML); 
			displayInfoWindowHTML();
			map.addOverlay(marqueur);



		      GEvent.addListener(map, "error", handleStreetViewError);
		//	 } catch (e) {alert('error during viewStreet : ' +e }
		    }
		    
		    function handleStreetViewError(errorCode) {
		      if (errorCode == GStreetviewPanorama.ErrorValues.FLASH_UNAVAILABLE) {
			alert("Error: Flash doesn't appear to be supported by your browser");
			return;
		      }
		     if (errorCode == GStreetviewPanorama.ErrorValues.NO_NEARBY_PANO) {
			$('streetpanorama').innerHtml="<@s.text name="search.nostreetpanoramaavailable" />";
			alert ('no panoavailable');
			return;
		      }
		else {
			alert ('An unknow error has occured on viewStreetPanorama : '+errorCode);		
		}
		    }  
		</script>

</#macro>



<#macro citySelector onCityFound>
<#if (ambiguousCities?? &&  ambiguousCities.size() > 1 )>
		<div class="forminstructions"><img src="/images/puce_2.gif" class="imagenumberlist" alt="puce_2"/><@s.text name="search.choose.city"/> : </div><br/>
		<span class="searchfield">
		<span class="error"><@s.text name="search.city.ambiguous"/> ! </span>
		<br/><br/>
		<@s.select listKey="Feature_id" listValue="Fully_qualified_name" name="ambiguouscity" list="ambiguousCities" headerValue="-- %{getText('search.select.city')} --" headerKey="" multiple="false" required="true" labelposition="top" theme="simple" onchange="${onCityFound}();" id="ambiguouscity" />&nbsp;
		<@s.url id="chooseOtherCityUrl" action="geocoding_worldwide" includeParams="none" />
		<a href="${chooseOtherCityUrl}"><@s.text name="search.city.chooseOther" /></a>
		<br/>
		</span>
<#else>
		
		<#if cityFound>
			<br/>
			<div class="forminstructions"><img src="/images/puce_2.gif" class="imagenumberlist" alt="puce_2"/><@s.text name="search.selectedcity" /> : </div>			<span class="searchfield">
			<span class="searchfieldlabel">&nbsp; </span> <@s.textfield size="40" name="city" id="city"  value="${city}" theme="simple" disabled="true"/>&nbsp
			<@s.url id="chooseOtherCityUrl" action="geocoding_worldwide" includeParams="none" />
		<a href="${chooseOtherCityUrl}"><@s.text name="search.city.chooseOther" /></a>
		<#else>
			<div class="forminstructions"><img src="/images/puce_2.gif" class="imagenumberlist" alt="puce_2"/><@s.text name="search.choose.city"/> : </div>
			<#if (city?? && !countryCode??) ><span class="error"><@s.text name="search.nocityfound"/> '${city}' ! </span><br/><br/></#if>
			<script type="text/javascript">
				validateNonEmptyQuery= function(){
					if ($('city').value == ''){
						alert("<@s.text name="search.city.empty"/>");
						 return false;
					} else {
						 return true;
					}
				 }
		</script>
			<span class="searchfield">
			<span class="searchfieldlabel">&nbsp;</span><@s.textfield size="40" name="city" id="city" required="false"  theme="simple"/>
			<@s.submit title="Search" value="%{getText('search.city.validate.choice')}" theme="simple" id="streetsearchsubmitbutton" onclick="return validateNonEmptyQuery();"/>
		</span>
		</#if>
</#if>


</#macro>


<#macro streetNameAutoCompleter javascriptNameObject >
<link href="/scripts/autocomplete/styles.css" rel="stylesheet" type="text/css" />
<script src="/scripts/prototype.js" type="text/javascript"></script>
<script src="/scripts/autocomplete/autocomplete.js"></script>
<@s.hidden size="5" name="lat" required="false" id="lat"  theme="simple" /><@s.hidden size="5" name="lng" required="false" id="lng" theme="simple"/>
<span class="searchfield">
	<span class="searchfieldlabel">&nbsp;</span><div class="error streetautocompleteerror" id="streetNameAutocompletererror" >&nbsp;</div><br/>
	<span class="searchfieldlabel">&nbsp;</span><@s.textfield size="40" name="streetname" required="false" id="streetname"  theme="simple"/><span style="display:none;" id="loadingImg"><img src="/images/loading.gif" alt="loading" class="imgAlign" style="width:25px;"></span>
</span>
<br/>
<script type="text/javascript">
selectedStreetInformation = null;
${javascriptNameObject} = new Autocomplete('streetname', { serviceUrl: '/street/streetsearch?format=json"&from=1&to=10"', width: 340, deferRequestBy:400, minChars:1, onSelect: 
function(value, data){
	${javascriptNameObject}.streetResults.each(
		function(value, i) {
			if (value.gid == data){
				selectedStreetInformation = value;
				viewStreet(value.lat,value.lng);
				viewStreetPanorama(value.lat,value.lng);
				return false;
			}
	       }.bind(this));
      },
onSearching: function(){
		$('loadingImg').show();
		$('streetNameAutocompletererror').innerHTML="&nbsp;"
	},
onEndSearching: function(){
		$('loadingImg').hide()
	},
onNoResultsFound: function(){
		$('streetNameAutocompletererror').innerHTML="<@s.text name="search.noResult"/>&nbsp; !"
	},
onFailToRetrieve: function(){
		$('streetNameAutocompletererror').innerHTML="<@s.text name="search.error"><@s.param>( </@s.param></@s.text>"+${javascriptNameObject}.errorMessage;
	}
});
  
</script>
</#macro>