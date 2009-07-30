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

<#macro googleStreetPanorama width heigth googleMapAPIKey CSSClass >
<script src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=${googleMapAPIKey} "
            type="text/javascript"></script>
 			<div name="streetview" id="streetview" style="width: ${width}px; height: ${heigth}px" class="${CSSClass}"></div>
			<script type="text/javascript">
		  
		    var pano;
		    
		    function viewStreet(lat, lng) {
		      var latlong = new GLatLng(lat,lng);
		      panoramaOptions = { latlng:latlong };
		      pano = new GStreetviewPanorama(document.getElementById("streetview"), panoramaOptions);
		      GEvent.addListener(pano, "error", handleNoFlash);
		    }
		    
		    function handleNoFlash(errorCode) {
		      if (errorCode == FLASH_UNAVAILABLE) {
			alert("Error: Flash doesn't appear to be supported by your browser");
			return;
		      }
		    }  
		</script>

</#macro>


<#macro citySelector onCityFound>
<#if (ambiguousCities?? &&  ambiguousCities.size() > 1 )>
		<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.city.ambiguous"/> : </span><@s.select listKey="Feature_id" listValue="Fully_qualified_name" name="ambiguouscity" list="ambiguousCities" headerValue="--select a City--" headerKey="" multiple="false" required="true" labelposition="top" theme="simple" onchange="${onCityFound}();" id="ambiguouscity" /> 
			
			<br/>
		</span>
		
		<#else>
		<span class="searchfield">
			<span class="searchfieldlabel">city : </span><@s.textfield size="5" name="city" required="false"  theme="simple"/>
			<script type="text/javascript">
			if ($('city').value != ''){
				${onCityFound}();
			}
			</script>
			<br/>
		</span>
		</#if>


</#macro>


<#macro streetNameAutoCompleter>
<link href="/scripts/autocomplete/styles.css" rel="stylesheet" type="text/css" />
<script src="/scripts/prototype.js" type="text/javascript"></script>
<script src="/scripts/autocomplete/autocomplete.js"></script>
<@s.hidden size="5" name="lat" required="false" id="lat"  theme="simple"/><@s.hidden size="5" name="lng" required="false" id="lng" theme="simple"/>
<span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="search.street.streetname"/> : </span><@s.textfield size="40" name="streetname" required="false" id="streetname"  theme="simple"/>
			<br/>
</span>
<script type="text/javascript">

streetNameAutocompleter = new Autocomplete('streetname', { serviceUrl: '/street/streetsearch?format=json"&from=1&to=10"', width: 340, deferRequestBy:400, minChars:1, onSelect: 
function(value, data){
	streetNameAutocompleter.streetResults.each(
		function(value, i) {
			if (value.gid == data){
				viewStreet(value.lat,value.lng);
				return false
			}
	       }.bind(this));
      }
});
  
</script>
</#macro>
