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

import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.Language;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.domain.repository.ILanguageDao;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
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
    
    @Resource
    private ICountryDao countryDao;
    
    @Resource
    private ILanguageDao languageDao;

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
	assertEquals(URLUtils.createGoogleMapUrl(city.getLocation(), city
		.getName()), result.getGoogle_map_url());
	assertEquals(URLUtils.createCountryFlagUrl(city.getCountryCode()),
		result.getCountry_flag_url());
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
    public void testUnmarshallSolrDocumentShouldReallyUnmarshallCountry() {
	Country country = geolocTestHelper
		.createFullFilledCountry();
	
	Language lang = new Language("french", "FR", "FRA");
	Language savedLang = languageDao.save(lang);
	Language retrievedLang = languageDao.get(savedLang.getId());
	assertEquals(savedLang, retrievedLang);

	country.addSpokenLanguage(lang);
	
	AlternateName alternateNameLocalized = new AlternateName("alternateFR",AlternateNameSource.ALTERNATENAMES_FILE);
	alternateNameLocalized.setLanguage("FR");
	AlternateName alternateName = new AlternateName("alternate",AlternateNameSource.ALTERNATENAMES_FILE);
	country.addAlternateName(alternateName);
	country.addAlternateName(alternateNameLocalized);
	countryDao.save(country);
	this.solRSynchroniser.commit();
	Pagination pagination = paginate().from(1).to(10);
	Output output = Output.withFormat(OutputFormat.XML).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	FulltextQuery query = new FulltextQuery(country.getName(), pagination,
		output, null, null);
	FulltextResultsDto response = this.fullTextSearchEngine
		.executeQuery(query);
	List<SolrResponseDto> results = response.getResults();
	assertNotNull(
		"There should have a result for a fulltextSearch for "
			+ country.getName()
			+ " and even If no results are return: an empty list should be return,  not null ",
		results);
	assertTrue("There should have a result for a fulltextSearch for "
		+ country.getName(), results.size() == 1);
	SolrResponseDto result = results.get(0);
	assertEquals(country.getName(), result.getName());
	assertEquals(country.getFeatureId(), result.getFeature_id());
	assertEquals(country.getFeatureClass(), result.getFeature_class());
	assertEquals(country.getFeatureCode(), result.getFeature_code());
	assertEquals(country.getAsciiName(), result.getName_ascii());
	assertEquals(country.getElevation(), result.getElevation());
	assertEquals(country.getGtopo30(), result.getGtopo30());
	assertEquals(country.getTimezone(), result.getTimezone());
	assertEquals(country.getFullyQualifiedName(false), result
		.getFully_qualified_name());
	assertEquals(country.getClass().getSimpleName(), result.getPlacetype());
	assertEquals(country.getPopulation(), result.getPopulation());
	assertEquals(country.getLatitude(), result.getLat());
	assertEquals(country.getLongitude(), result.getLng());
	assertEquals(URLUtils.createGoogleMapUrl(country.getLocation(), country
		.getName()), result.getGoogle_map_url());
	assertEquals(URLUtils.createCountryFlagUrl(country.getCountryCode()),
		result.getCountry_flag_url());
	assertEquals(URLUtils.createYahooMapUrl(country.getLocation()), result
		.getYahoo_map_url());

	assertEquals(1, result.getCountry_names_alternate().size());
	assertEquals(country.getAlternateNames().get(0).getName(),
		result.getCountry_names_alternate().get(0));

	assertEquals(1, result.getCountry_names_alternate_localized().size());
	assertEquals(alternateNameLocalized.getName(), result.getCountry_names_alternate_localized()
		.get(alternateNameLocalized.getLanguage()).get(0));
	assertEquals(country.getContinent(), result.getContinent());
	assertEquals(country.getCurrencyCode(), result.getCurrency_code());
	assertEquals(country.getCurrencyName(), result.getCurrency_name());
	assertEquals(country.getFipsCode(), result.getFips_code());
	assertEquals(country.getIso3166Alpha2Code(), result.getIsoalpha2_country_code());
	assertEquals(country.getIso3166Alpha3Code(), result.getIsoalpha3_country_code());
	assertEquals(country.getPostalCodeMask(), result.getPostal_code_mask());
	assertEquals(country.getPostalCodeRegex(), result.getPostal_code_regex());
	assertEquals(country.getPhonePrefix(), result.getPhone_prefix());
	assertEquals(country.getSpokenLanguages().get(0).getIso639LanguageName(), result.getSpoken_languages().get(0));
	assertEquals(country.getTld(), result.getTld());
	assertEquals(country.getCapitalName(), result.getCapital_name());
	assertEquals(country.getArea(), result.getArea());
	
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
