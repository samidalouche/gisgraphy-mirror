<#macro displayFulltextResults fulltextResponseDTO>
			<div id="searchResults">
			<div class="clear"><br/></div>
			<div class="bigText">${fulltextResponseDTO.numFound} <@s.text name="search.resultFound"/>. (<@s.text name="search.resultPaginateFromTo"><@s.param>${from}</@s.param><@s.param>${to}</@s.param></@s.text>).
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
							<a href="${featureURL}">${result.name} (${result.country_name})</a>
							</div>
							<div class="resultheaderright">${result.placetype}</div>
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
			 <div class="importantMessage"><@s.text name="No Result found"/>!!<br/><br/></div>
			 <div class="bigText"><@s.text name="search.noresultMessage.part1"/><a href="http://www.geonames.org" target="geonames">Geonames page</a>. <@s.text name="search.noresultMessage.part2"/></div>
		</#if>
</#macro>

<#macro displayGeolocResults geolocResponseDTO>
			<div id="searchResults">
				<div class="clear"><br/></div>
				<div class="bigText">${geolocResponseDTO.numFound} <@s.text name="search.resultFound"/>. (<@s.text name="search.resultPaginateFromTo"><@s.param>${from}</@s.param><@s.param>${to}</@s.param></@s.text>).
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
								<div class="resultheaderleft"><a href="${featureURL}">${result.name} (${result.countryCode})</a> : ${result.distance} meters</div>
								<div class="resultheaderright">${result.placeType}</div>
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
			
			<br/>
			  <div class="importantMessage">No results founds!!<br/><br/></div>
			 <div class="bigText"> If you want to add a place in the database : go to <a href="http://www.geonames.org" target="geonames">Geonames page</a>. The place will be include in the next Gisgraphy import.</div>
		</#if>
		</div>
</#macro>