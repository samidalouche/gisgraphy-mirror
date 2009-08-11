<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.geocoding.title"/></title>
<meta name="Description" content="Worldwide geocoding free webservices and street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.geocoding.title"/>"/>
<meta name="keywords" content="geocoding world worldwide street search java openstreetmap webservices postgis hibernate toponyms gazeteers"/>
<script src="/scripts/prototype.js" type="text/javascript"></script>
</head>
<body onunload="GUnload()">
<br/>
<noscript>
	<div class="tip yellowtip">
		<@s.text name="global.noscript.required"/>
	</div>
	<br/>
</noscript>
		 <@breadcrumbs.searchNavBar/>
<div class="clear"></div><div class="biggertext" style="line-height:1.5em;">The worldwide geocoding webservice is totally FREE and allow to find Lat/lng pair from an adress or a street. it uses (free) data from <span class="imgAlign"><a href="http://openstreetmap.org">openstreetMap <img src="/images/openstreetmap.png" alt="openstreetmap" class="imgAlign" style="width:30px"/></a> that are imported into a local database. you can see an example of use bellow</span>. You can find documentation on <a href="http://www.gisgraphy.com/documentation/index.htm#streetservice">how to use the webservice</a> and see how to <a href="http://www.gisgraphy.com/documentation/installation/index.htm" alt="install gisgraphy">download and install</a> Gisgraphy</div><br/><br/>
<div class="clear"></div>


		<script type="text/javascript">
			latlngArray = eval('${latLongJson}');
			updateLatLng = function(){
				if ($('ambiguouscity') != null){
					var indexDropDown = ($('ambiguouscity').selectedIndex);
						if (indexDropDown == 0){
							$('lat').value='',
							$('lng').value='';
						} else {
							$('lat').value = latlngArray[indexDropDown-1].lat;
							$('lng').value = latlngArray[indexDropDown-1].lng;
						}
				}
					setStreetNameCorrectState();
                        }

			setStreetNameCorrectState = function(){
				if ($('lat').value != '' && $('lng').value != '' ){
					streetNameAutocompleter.serviceUrl="/street/streetsearch?format=json&lat="+$('lat').value+"&lng="+$('lng').value+"&from=1&to=10";
					$('viewAllStreetLink').show();
					$('streetname').enable();

				} else {
					$('viewAllStreetLink').hide();
					$('streetname').disable();

				}
			}

	Event.observe(window, "load", function(){
			updateLatLng();
			setStreetNameCorrectState();
			});
			</script>
	<div class="center">
	<@s.form action="" id="streetsearch">
<br/>
<#if errorMessage!= ''><div class="error">${errorMessage}</div><br/><br/></#if>
<#if message!= ''><span class="biggertext">${message}</span><br/></#if>
<div class="forminstructions"><img src="/images/puce_1.gif" class="imagenumberlist" alt="puce_1"/><@s.text name="search.select.country"/> : </div>
         <span class="searchfield">
			<span class="searchfieldlabel">&nbsp;</span><@s.select label="country " listKey="iso3166Alpha2Code" listValue="name" name="countryCode" list="countries" headerValue="-- %{getText('search.select.country')} --" headerKey="" multiple="false" required="true" labelposition="top" theme="simple" id="country"/> 
	<br/><br/>
	</span>
	<div>
<br/><br/>

	<@gisgraphysearch.citySelector  onCityFound="updateLatLng" />
	</div>
		<div class="clear"></div>
		<br/>
		</span>
                <div class="clear"></div>

<div class="forminstructions"><img src="/images/puce_3.gif" class="imagenumberlist" alt="puce_3"/><span id="viewAllStreetLink" class="forminstructions"><a href=""><@s.text name="search.displaycity.streets"/></a>&nbsp;&nbsp;<span class="underline"><@s.text name="global.or"/></span>&nbsp;&nbsp;</span><@s.text name="search.street.search"/>&nbsp;(<@s.text name="global.autocomplete"/>) : </div>
		<@gisgraphysearch.streetNameAutoCompleter javascriptNameObject="streetNameAutocompleter"/>
		
<br/>
</div>
</@s.form>
<div>
<div class="clear"></div>
<br/><br/>
<div class="clear"></div>
<@gisgraphysearch.googleStreetView width="700" heigth="400" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />
<br/><br/>
<@gisgraphysearch.googleStreetPanorama width="700" heigth="300" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />

</div>

</body>
</html>