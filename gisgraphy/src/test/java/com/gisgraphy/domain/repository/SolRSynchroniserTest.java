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
package com.gisgraphy.domain.repository;

import static com.gisgraphy.domain.valueobject.Pagination.paginate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.ZipCodeAware;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextFields;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextSearchException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.helper.URLUtils;
import com.gisgraphy.test.GeolocTestHelper;

public class SolRSynchroniserTest extends AbstractIntegrationHttpSolrTestCase {

    @Resource
    private ICityDao cityDao;

    @Resource
    private ICountryDao countryDao;

    @Resource
    private GeolocTestHelper geolocTestHelper;

    @Test
    public void testDeleteAllShouldResetTheIndex() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());

	this.solRSynchroniser.commit();
	QueryResponse resultsAfterRemove = searchInFulltextSearchEngine("my city");

	int resultsSize = resultsAfterRemove.getResults().size();
	assertEquals("The city hasn't been saved", 1, resultsSize);
	assertEquals("The city hasn't been saved", saved.getFeatureId(),
		resultsAfterRemove.getResults().get(0).getFieldValue(
			FullTextFields.FEATUREID.getValue()));

	this.solRSynchroniser.deleteAll();
	resultsAfterRemove = searchInFulltextSearchEngine("my city");
	assertTrue("the index is not reset ", resultsAfterRemove.getResults()
		.isEmpty());

    }

    @Test
    public void testSolrSynchroniserConstructorCanNotHaveNullParam() {
	try {
	    new SolRSynchroniser(null);
	    fail("SolrSynchroniser can not have null parameters");
	} catch (IllegalArgumentException e) {
	}
    }

    @Test
    public void testSavingAgisFeatureShouldSynchronize() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(1L);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	this.solRSynchroniser.commit();
	QueryResponse resultsAfterRemove = searchInFulltextSearchEngine("my city");

	int resultsSize = resultsAfterRemove.getResults().size();
	assertEquals("The city hasn't been saved", 1, resultsSize);
	assertEquals("The city hasn't been saved", saved.getFeatureId(),
		resultsAfterRemove.getResults().get(0).getFieldValue(
			FullTextFields.FEATUREID.getValue()));
    }

    /*
     * @Test public void
     * testSavingAgisFeatureShouldNotSynchronizeNonFullTextSearchable(){ City
     * city = new City(){ @Transient public boolean isFullTextSearchable() {
     * return false; } }; city.setFeatureId(1L); city.setName("name");
     * city.setLocation(GeolocTestHelper.createPoint(1.4F, 2.4F)); City saved =
     * this.cityDao.save(city); assertNotNull(saved);
     * assertNotNull(saved.getId()); this.solRSynchroniser.commit(); for (int
     * i=0;i<10000;i++){ } List<City> results =this.cityDao.listFromText("my
     * city", true); assertTrue("A non fulltextsearchable gisFeature should not
     * be synchronised ",results.isEmpty()); }
     */

    public void testCommitShouldReallyCommit() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(1L);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());

	QueryResponse searchResults = searchInFulltextSearchEngine("my city");

	assertTrue(
		"The fulltextSearchEngine should return an empty List before commit",
		searchResults.getResults().isEmpty());

	this.solRSynchroniser.commit();

	searchResults = searchInFulltextSearchEngine("my city");

	int resultsSize = searchResults.getResults().size();
	assertEquals("The city hasn't been saved", 1, resultsSize);
	assertEquals("The city hasn't been saved", saved.getFeatureId(),
		searchResults.getResults().get(0).getFieldValue(
			FullTextFields.FEATUREID.getValue()));

    }

    @Test
    public void testSavingAGisFeatureWithNullFeatureIdShouldNotSynchronize() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(null);
	try {
	    this.cityDao.save(city);
	    fail("A gisFeature with null gisFeature can not be saved");
	} catch (RuntimeException e) {

	}
    }

    @Test
    public void testSavingAGisFeatureWithNegativeFeatureIdShouldNotSynchronize() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(-1L);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	this.solRSynchroniser.commit();
	// for (int i = 0; i < 10000; i++) {
	// }
	List<City> results = this.cityDao.listFromText("my city", true);
	assertNotNull(results);
	assertTrue("a GisFeatureWith null featureId should not synchronize",
		results.isEmpty());
    }

    @Test
    public void testSavingAGisFeatureWithPointEqualsZeroShouldNotSynchronize() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 0F,
		0F);
	city.setFeatureId(1L);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	this.solRSynchroniser.commit();
	List<City> results = this.cityDao.listFromText("my city", true);
	assertNotNull(results);
	assertTrue("a GisFeatureWith null featureId should not synchronize",
		results.isEmpty());
    }

    @Test
    public void testDeleteAgisFeatureShouldSynchronize() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(1L);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	this.solRSynchroniser.commit();
	QueryResponse searchResults = searchInFulltextSearchEngine("my city");

	int resultsSize = searchResults.getResults().size();
	assertEquals("The city hasn't been saved", 1, resultsSize);
	assertEquals("The city hasn't been saved", saved.getFeatureId(),
		searchResults.getResults().get(0).getFieldValue(
			FullTextFields.FEATUREID.getValue()));

	this.cityDao.remove(saved);

	searchResults = searchInFulltextSearchEngine("my city");

	assertTrue(
		"The city hasn't been removed from the full text search engine",
		searchResults.getResults().isEmpty());
    }

    @Test
    public void testDeleteAlistOfGisFeaturesShouldSynchronize() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(1L);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	this.solRSynchroniser.commit();
	// for (int i = 0; i < 10000; i++) {
	// }
	QueryResponse searchResults = searchInFulltextSearchEngine("my city");

	int resultsSize = searchResults.getResults().size();
	assertEquals("The city hasn't been saved", 1, resultsSize);
	assertEquals("The city hasn't been saved", saved.getFeatureId(),
		searchResults.getResults().get(0).getFieldValue(
			FullTextFields.FEATUREID.getValue()));

	List<City> listToRemove = new ArrayList<City>();
	listToRemove.add(saved);
	this.cityDao.deleteAll(listToRemove);

	QueryResponse resultsAfterRemove = searchInFulltextSearchEngine("my city");

	resultsAfterRemove = searchInFulltextSearchEngine("my city");

	assertTrue(
		"The city hasn't been removed from the full text search engine",
		resultsAfterRemove.getResults().isEmpty());
    }

    @Test
    public void testDeleteallGisFeaturesOfAspecificPlaceTypeShouldSynchronize() {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(1L);
	City saved = this.cityDao.save(city);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	this.solRSynchroniser.commit();
	QueryResponse searchResults = searchInFulltextSearchEngine("my city");

	int resultsSize = searchResults.getResults().size();
	assertEquals("The city hasn't been saved", 1, resultsSize);
	assertEquals("The city hasn't been saved", saved.getFeatureId(),
		searchResults.getResults().get(0).getFieldValue(
			FullTextFields.FEATUREID.getValue()));

	Country country = GeolocTestHelper.createCountryForFrance();
	Country savedCountry = this.countryDao.save(country);
	assertNotNull(savedCountry);
	assertNotNull(savedCountry.getId());
	this.solRSynchroniser.commit();

	searchResults = searchInFulltextSearchEngine("france");

	resultsSize = searchResults.getResults().size();
	assertEquals("The country hasn't been saved", 1, resultsSize);
	assertEquals("The country hasn't been saved", savedCountry
		.getFeatureId(), searchResults.getResults().get(0)
		.getFieldValue(FullTextFields.FEATUREID.getValue()));

	this.cityDao.deleteAll();

	QueryResponse resultsAfterRemove = searchInFulltextSearchEngine("my city");

	assertTrue(
		"The city hasn't been removed from the full text search engine",
		resultsAfterRemove.getResults().isEmpty());

	searchResults = searchInFulltextSearchEngine("france");

	resultsSize = searchResults.getResults().size();
	assertEquals("The country should still be in the datastore", 1,
		resultsSize);
	assertEquals("The country should still be in the datastore",
		savedCountry.getFeatureId(), searchResults.getResults().get(0)
			.getFieldValue(FullTextFields.FEATUREID.getValue()));
    }

    public void testZipCodeShouldBeSynchronisedIfFeatureIsACity() {
	// create one city
	Long featureId = 1001L;
	City paris = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F,
		featureId);

	paris.setZipCode(50263);

	// save city and check it is saved
	ZipCodeAware saved = this.cityDao.save(paris);
	assertNotNull(this.cityDao.getByFeatureId(featureId));

	// commit changes
	this.solRSynchroniser.commit();

	// for (int i = 0; i < 10000; i++) {
	// }

	// test zipcode
	List<City> zipResults = this.cityDao.listFromText("50263", false);
	assertTrue(
		"zipcode should be found even if alternatenames are excluded'",
		zipResults.size() == 1);
	assertEquals(
		"zipcode should be found even if alternatenames are excluded'",
		saved, zipResults.get(0));

    }

    @Test
    public void testSynchronize() {
	Long featureId = 1001L;
	City paris = geolocTestHelper
		.createAndSaveCityWithFullAdmTreeAndCountry(featureId);
	// commit changes
	this.solRSynchroniser.commit();
	File tempDir = GeolocTestHelper.createTempDir(this.getClass()
		.getSimpleName());
	File file = new File(tempDir.getAbsolutePath()
		+ System.getProperty("file.separator") + "serialize.txt");

	OutputStream outputStream = null;
	try {
	    outputStream = new FileOutputStream(file);
	} catch (FileNotFoundException e1) {
	    fail();
	}

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.FULL)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, City.class, "fr");
	    fullTextSearchEngine.executeAndSerialize(fulltextQuery,
		    outputStream);
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}

	String content = "";
	try {
	    content = GeolocTestHelper.readFileAsString(file.getAbsolutePath());
	} catch (IOException e) {
	    fail("can not get content of file " + file.getAbsolutePath());
	}

	assertQ(
		"The query return incorrect values",
		content,
		"//*[@numFound='1']",
		"//*[@name='status'][.='0']"
		// name
		,
		"//*[@name='" + FullTextFields.NAME.getValue()
			+ "'][.='Saint-André']",
		"//*[@name='" + FullTextFields.NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_SUFFIX.getValue()
			+ "'][./str[1]][.='cityalternate']",
		"//*[@name='" + FullTextFields.NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue()
			+ "FR'][./str[1]][.='cityalternateFR']"

		,
		"//*[@name='" + FullTextFields.ADM3NAME.getValue()
			+ "'][.='admParent']"
		// adm1
		,
		"//*[@name='" + FullTextFields.ADM1CODE.getValue()
			+ "'][.='A1']",
		"//*[@name='" + FullTextFields.ADM1NAME.getValue()
			+ "'][.='admGrandGrandParent']",
		"//*[@name='" + FullTextFields.ADM1NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_SUFFIX.getValue()
			+ "'][./str[1]='admGGPalternate']",
		"//*[@name='" + FullTextFields.ADM1NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_SUFFIX.getValue()
			+ "'][./str[2]='admGGPalternate2']",
		"//*[@name='" + FullTextFields.ADM1NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue()
			+ "FR'][./str[1]][.='admGGPalternateFR']"
		// adm2
		,
		"//*[@name='" + FullTextFields.ADM2CODE.getValue()
			+ "'][.='B2']",
		"//*[@name='" + FullTextFields.ADM2NAME.getValue()
			+ "'][.='admGrandParent']",
		"//*[@name='" + FullTextFields.ADM2NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_SUFFIX.getValue()
			+ "'][./str[1]][.='admGPalternate']",
		"//*[@name='" + FullTextFields.ADM2NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue()
			+ "FR'][./str[1]][.='admGPalternateFR']"
		// adm3
		,
		"//*[@name='" + FullTextFields.ADM3CODE.getValue()
			+ "'][.='C3']"
		// country
		,
		"//*[@name='" + FullTextFields.COUNTRYCODE.getValue()
			+ "'][.='FR']",
		"//*[@name='" + FullTextFields.COUNTRYNAME.getValue()
			+ "'][.='France']",
		"//*[@name='" + FullTextFields.COUNTRYNAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_SUFFIX.getValue()
			+ "'][./str[1]][.='francia']",
		"//*[@name='" + FullTextFields.COUNTRYNAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue()
			+ "FR'][./str[1]][.='franciaFR']"

		// property
		, "//*[@name='" + FullTextFields.FEATURECLASS.getValue()
			+ "'][.='P']",
		"//*[@name='" + FullTextFields.FEATURECODE.getValue()
			+ "'][.='PPL']", "//*[@name='"
			+ FullTextFields.FEATUREID.getValue() + "'][.='1001']",
		"//*[@name='" + FullTextFields.FULLY_QUALIFIED_NAME.getValue()
			+ "'][.='" + paris.getFullyQualifiedName(false) + "']",
		"//*[@name='" + FullTextFields.LAT.getValue() + "'][.='2.0']",
		"//*[@name='" + FullTextFields.LONG.getValue() + "'][.='1.5']",
		"//*[@name='" + FullTextFields.PLACETYPE.getValue()
			+ "'][.='City']", "//*[@name='"
			+ FullTextFields.POPULATION.getValue()
			+ "'][.='10000000']", "//*[@name='"
			+ FullTextFields.ZIPCODE.getValue() + "'][.='50263']"

		, "//*[@name='" + FullTextFields.NAMEASCII.getValue()
			+ "'][.='ascii']",
		"//*[@name='" + FullTextFields.ELEVATION.getValue()
			+ "'][.='13456']", "//*[@name='"
			+ FullTextFields.GTOPO30.getValue() + "'][.='7654']",
		"//*[@name='" + FullTextFields.TIMEZONE.getValue()
			+ "'][.='Europe/Paris']"

		, "//*[@name='" + FullTextFields.COUNTRY_FLAG_URL.getValue()
			+ "'][.='"
			+ URLUtils.createCountryFlagUrl(paris.getCountryCode())
			+ "']", "//*[@name='"
			+ FullTextFields.GOOGLE_MAP_URL.getValue()
			+ "'][.='"
			+ URLUtils.createGoogleMapUrl(paris.getLocation(),
				paris.getName()) + "']", "//*[@name='"
			+ FullTextFields.YAHOO_MAP_URL.getValue() + "'][.='"
			+ URLUtils.createYahooMapUrl(paris.getLocation())
			+ "']");

	// delete temp dir
	assertTrue("the tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));

    }

    private QueryResponse searchInFulltextSearchEngine(String searchWords) {
	SolrQuery query = new SolrQuery();
	String namefield = FullTextFields.ALL_NAME.getValue();

	String queryString = "(" + namefield + ":\"" + searchWords + "\")";
	query.setQuery(queryString);
	query.setQueryType(Constants.SolrQueryType.advanced.toString());
	query.setFields(FullTextFields.FEATUREID.getValue());

	QueryResponse resultsAfterRemove = null;
	try {
	    resultsAfterRemove = solrClient.getServer().query(query);
	} catch (SolrServerException e) {
	    fail();
	}
	return resultsAfterRemove;
    }

}
