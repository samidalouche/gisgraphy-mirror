<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.street.title"/></title>
<meta name="Description" content="free webservices for street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.street.title"/>"/>
<meta name="keywords" content="street search java openstreetmap webservices postgis hibernate toponyms gazeteers"/>
<script src="/scripts/prototype.js" type="text/javascript"></script>
</head>
<body onunload="GUnload()">
<br/>
<noscript>
	<div class="tip yellowtip">
		<@s.text name="global.noscript"/>
	</div>
	<br/>
</noscript>
		 <@breadcrumbs.searchNavBar/>
<div class="clear"></div><div class="biggertext" style="line-height:1.5em;">The street webservice is Free and offer the possibility to search for street name all over the world. The data come from <span class="imgAlign"><a href="http://openstreetmap.org">openstreetMap <img src="/images/openstreetmap.png" alt="openstreetmap" class="imgAlign" style="width:30px"/></a> and are imported into a local database in order to be used by the gisgraphy webService. Here is an example of use of the street webservice</span>. You can find documentation on how to use the webservice <a href="http://www.gisgraphy.com/documentation/index.htm#streetservice">here</a> and see how to download and install gisgraphy : <a href="http://www.gisgraphy.com/documentation/installation/index.htm" alt="install gisgraphy">here</a> </div><br/><br/>
<div class="clear"></div>


		<script type="text/javascript">
			latlngArray = eval('${latLongJson}')
			updateLatLng = function(){
				if ($('ambiguouscity') != null){
					var indexDropDown = ($('ambiguouscity').selectedIndex);
						if (indexDropDown == 0){
							$('lat').value='',
							$('lng').value='';
						} else {
							$('lat').value = latlngArray[indexDropDown-1].lat;
							$('lng').value = latlngArray[indexDropDown-1].lng;
							streetNameAutocompleter.serviceUrl="/street/streetsearch?format=json&lat="+$('lat').value+"&lng="+$('lng').value+"&from=1&to=10";
						}
				}
					setStreetNameCorrectState();
                        }

			setStreetNameCorrectState = function(){
				if ($('lat').value !='' && $('lng').value != '' ){
					$('streetname').enable();
				} else {
					$('streetname').disable();
				}
			}

		setCityCorrectState = function(){
				if ($('city') != null){
					if  ($('country') != null && $('country').value == '' ){
						$('city').disable();
					} else {
						$('city').enable();
					}
				}
			}
	Event.observe(window, "load", function(){
			updateLatLng();
			setCityCorrectState();
			setStreetNameCorrectState();
			});
			</script>
	<div class="center">
	<@s.form action="" id="streetsearch">
<br/>
<#if errorMessage!= ''><div class="error">${errorMessage}</div><br/><br/></#if>
<#if message!= ''><span class="biggertext">${message}</span><br/></#if>
<div class="forminstructions"><@s.text name="search.select.country"/> : </div>
<br/>
         <span class="searchfield">
			<span class="searchfieldlabel"><@s.text name="global.country"/> : </span><@s.select label="country " listKey="iso3166Alpha2Code" listValue="name" name="countryCode" list="countries" headerValue="-- %{getText('search.select.country')} --" headerKey="" multiple="false" required="true" labelposition="top" theme="simple" id="country" onchange="setCityCorrectState();"/> 
	<br/><br/>
	</span>
	<div>
<br/><br/>
<div class="forminstructions"><@s.text name="search.choose.city"/> : </div>
<br/>
	<@gisgraphysearch.citySelector  onCityFound="updateLatLng" />
	</div>
		<div class="clear"></div>
		<br/>
		</span>
                <div class="clear"></div>
<div class="forminstructions"><@s.text name="search.street.search"/> : </div><br/>
		<@gisgraphysearch.streetNameAutoCompleter/>
<#--<@s.submit title="Search" value="Search" theme="simple" id="streetsearchsubmitbutton"/>-->
</div>
</@s.form>
<div>
<div class="clear"></div>
<br/><br/>
<div class="clear"></div>
<@gisgraphysearch.googleStreetView width="700" heigth="300" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />
<br/><br/>
<@gisgraphysearch.googleStreetPanorama width="700" heigth="300" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />

<br/><br/>

<br/><br/><br/>

</div>

</body>
</html>