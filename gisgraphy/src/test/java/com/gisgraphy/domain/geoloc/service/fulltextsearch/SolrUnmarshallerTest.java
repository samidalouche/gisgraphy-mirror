/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
/**
 * 
 */
package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import static com.gisgraphy.domain.valueobject.Pagination.paginate;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.valueobject.FulltextResultsDto;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.SolrResponseDto;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.helper.URLUtils;
import com.gisgraphy.test.GeolocTestHelper;

public class SolrUnmarshallerTest extends AbstractIntegrationHttpSolrTestCase {

    @Resource
    private GeolocTestHelper geolocTestHelper;

    @Test
    public void testUnmarshallSolrDocumentShouldReallyUnmarshall() {
	Long featureId = 1002L;
	City city = geolocTestHelper
		.createAndSaveCityWithFullAdmTreeAndCountry(featureId);
	this.solRSynchroniser.commit();
	Pagination pagination = paginate().from(1).to(10);
	Output output = Output.withFormat(OutputFormat.XML).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	FulltextQuery query = new FulltextQuery(city.getName(), pagination,
		output, null, null);
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"There should have a result for a fulltextSearch for "
			+ city.getName()
			+ " and even If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("There should have a result for a fulltextSearch for "
		+ city.getName(), results.size() == 1);
	SolrResponseDto result = results.get(0);
	assertEquals(city.getName(), result.getName());
	assertEquals(city.getFeatureId(), result.getFeature_id());
	assertEquals(city.getFeatureClass(), result.getFeature_class());
	assertEquals(city.getFeatureCode(), result.getFeature_code());
	assertEquals(city.getAsciiName(), result.getName_ascii());
	assertEquals(city.getElevation(), result.getElevation());
	assertEquals(city.getGtopo30(), result.getGtopo30());
	assertEquals(city.getTimezone(), result.getTimezone());
	assertEquals(city.getFullyQualifiedName(false), result
		.getFully_qualified_name());
	assertEquals(city.getClass().getSimpleName(), result.getPlacetype());
	assertEquals(city.getPopulation(), result.getPopulation());
	assertEquals(city.getLatitude(), result.getLat());
	assertEquals(city.getLongitude(), result.getLng());
	assertEquals(city.getAdm1Code(), result.getAdm1_code());
	assertEquals(city.getAdm2Code(), result.getAdm2_code());
	assertEquals(city.getAdm3Code(), result.getAdm3_code());
	assertEquals(city.getAdm4Code(), result.getAdm4_code());
	assertEquals(city.getAdm1Name(), result.getAdm1_name());
	assertEquals(city.getAdm2Name(), result.getAdm2_name());
	assertEquals(city.getAdm3Name(), result.getAdm3_name());
	assertEquals(city.getAdm4Name(), result.getAdm4_name());
	assertEquals(city.getZipCode(), result.getZipcode());
	assertEquals(city.getCountry().getName(), result.getCountry_name());
	assertEquals(URLUtils.createGoogleMapUrl(city.getLocation(),city.getName()), result
		.getGoogle_map_url());
	assertEquals(URLUtils.createCountryFlagUrl(city.getCountryCode()), result
		.getCountry_flag_url());
	assertEquals(URLUtils.createYahooMapUrl(city.getLocation()), result
		.getYahoo_map_url());

	assertEquals(1, result.getName_alternates().size());
	assertEquals("cityalternate", result.getName_alternates().get(0));

	assertEquals(1, result.getName_alternates_localized().size());
	assertEquals("cityalternateFR", result.getName_alternates_localized()
		.get("FR").get(0));

	assertEquals(2, result.getAdm1_names_alternate().size());
	assertEquals(city.getAdm().getParent().getParent().getAlternateNames()
		.get(0).getName(), result.getAdm1_names_alternate().get(0));
	assertEquals("admGGPalternate2", result.getAdm1_names_alternate()
		.get(1));

	assertEquals(1, result.getAdm2_names_alternate().size());
	assertEquals(city.getAdm().getParent().getAlternateNames().get(0)
		.getName(), result.getAdm2_names_alternate().get(0));

	assertEquals(1, result.getCountry_names_alternate().size());
	assertEquals(city.getCountry().getAlternateNames().get(0).getName(),
		result.getCountry_names_alternate().get(0));

	assertEquals(1, result.getCountry_names_alternate_localized().size());
	assertEquals("franciaFR", result.getCountry_names_alternate_localized()
		.get("FR").get(0));

	assertEquals(1, result.getAdm1_names_alternate_localized().size());
	assertEquals("admGGPalternateFR", result
		.getAdm1_names_alternate_localized().get("FR").get(0));

	assertEquals(1, result.getAdm2_names_alternate_localized().size());
	assertEquals("admGPalternateFR", result
		.getAdm2_names_alternate_localized().get("FR").get(0));
    }

    @Test
    public void testUnmarshallQueryResponseShouldReturnAnEmptyListIfNoResultsAreFound() {
	FulltextQuery query = new FulltextQuery("fake");
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("If no results are return: an empty list should be return",
		results.size() == 0);
    }

}
