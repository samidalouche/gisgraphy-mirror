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
package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import static com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery.ONLY_CITY_PLACETYPE;
import static com.gisgraphy.domain.valueobject.Pagination.paginate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.ZipCode;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.ISpellCheckerIndexer;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.SpellCheckerConfig;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
import com.gisgraphy.domain.valueobject.FulltextResultsDto;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.fulltext.service.exception.FullTextSearchException;
import com.gisgraphy.helper.FileHelper;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.service.IStatsUsageService;
import com.gisgraphy.stats.StatsUsageType;
import com.gisgraphy.test.FeedChecker;
import com.gisgraphy.test.GeolocTestHelper;

public class FulltextSearchEngineTest extends
	AbstractIntegrationHttpSolrTestCase {

    private ICityDao cityDao;
    
    private IAdmDao admDao;

    @Resource
    IStatsUsageService statsUsageService;

    @Resource
    private ISpellCheckerIndexer spellCheckerIndexer;

    @Test
    public void testIsAlive() {
	assertTrue(fullTextSearchEngine.isAlive());
	FullTextSearchEngine fullTextSearchEngineTobadUrl = new FullTextSearchEngine(
		new MultiThreadedHttpConnectionManager());
	IsolrClient mockSolClient = EasyMock.createMock(IsolrClient.class);
	EasyMock.expect(mockSolClient.isServerAlive()).andReturn(false);
	EasyMock.replay(mockSolClient);
	fullTextSearchEngineTobadUrl.setSolrClient(mockSolClient);

	assertFalse(fullTextSearchEngineTobadUrl.isAlive());
	EasyMock.verify(mockSolClient);

	FullTextSearchEngine fullTextSearchEngineWithNullSolrClient = new FullTextSearchEngine(
		new MultiThreadedHttpConnectionManager());
	fullTextSearchEngineWithNullSolrClient.setSolrClient(null);
	assertFalse(fullTextSearchEngineWithNullSolrClient.isAlive());
    }

    @Test
    public void testGetUrl() {
	String urlOfSolrClient = "URLOfSolRclient";
	assertTrue(fullTextSearchEngine.isAlive());
	FullTextSearchEngine fullTextSearchEngineTest = new FullTextSearchEngine(
		new MultiThreadedHttpConnectionManager());
	IsolrClient mockSolClient = EasyMock.createMock(IsolrClient.class);
	EasyMock.expect(mockSolClient.getURL()).andReturn(urlOfSolrClient);
	EasyMock.replay(mockSolClient);
	fullTextSearchEngineTest.setSolrClient(mockSolClient);

	assertEquals(urlOfSolrClient, fullTextSearchEngineTest.getURL());
	EasyMock.verify(mockSolClient);

    }

    @Test
    public void testConstructorCanNotHaveNullParam() {
	try {
	    new FullTextSearchEngine(null);
	    fail("FullTextSearchEngine does not accept null MultiThreadedHttpConnectionManager");
	} catch (IllegalArgumentException e) {

	} catch (FullTextSearchException e) {
	    fail("FullTextSearchEngine does not accept null MultiThreadedHttpConnectionManager and must throws an exception of type IllegalArgumentException, not FullTextSearchException");
	}

    }

    @Test
    public void testExecuteAndSerializeCanNotHaveNullParam() {
	try {
	    fullTextSearchEngine.executeAndSerialize(null,
		    new ByteArrayOutputStream());
	    fail("executeAndSerialize does not accept null query");
	} catch (IllegalArgumentException e) {

	} catch (FullTextSearchException e) {
	    fail("executeAndSerialize does not accept null query and must throws an IllegalArgumentException, not FullTextSearchException");
	}

	try {
	    fullTextSearchEngine.executeAndSerialize(new FulltextQuery(""),
		    null);
	    fail("executeAndSerialize does not accept null OutputStream");
	} catch (IllegalArgumentException e) {
	} catch (FullTextSearchException e) {
	    fail("executeAndSerialize does not accept null OutputStream and must throws an IllegalArgumentException, not FullTextSearchException");

	}
    }

    @Test
    public void testExecuteQueryWithAnNullQuerythrows() {
	try {
	    fullTextSearchEngine.executeQuery(null);
	    fail("executeAndSerialize does not accept null query");
	} catch (IllegalArgumentException e) {

	} catch (FullTextSearchException e) {
	    fail("executeAndSerialize does not accept null query and must throws an IllegalArgumentException, not FullTextSearchException");
	}
    }

    @Test
    public void testExecuteQueryToStringWithANullQuerythrows() {
	try {
	    fullTextSearchEngine.executeQueryToString(null);
	    fail("executeAndSerialize does not accept null query");
	} catch (IllegalArgumentException e) {

	} catch (FullTextSearchException e) {
	    fail("executeAndSerialize does not accept null query and must throws an IllegalArgumentException, not FullTextSearchException");
	}
    }

    @Test
    public void testExecuteQueryToDatabaseObjectsShouldReturnHibernateObjects() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    List<? extends GisFeature> result = fullTextSearchEngine
		    .executeQueryToDatabaseObjects(fulltextQuery);
	    assertEquals(1, result.size());
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }

    @Test
    public void testExecuteQueryToDatabaseObjectsShouldNotAcceptNullQuery() {
	try {
	     fullTextSearchEngine
		    .executeQueryToDatabaseObjects(null);
	    fail("executeQueryToDatabaseObject should not accept null query");
	} catch (IllegalArgumentException e) {

	}
    }

    @Test
    public void testExecuteAndSerializeShouldSerialize() {
	File tempDir = FileHelper.createTempDir(this.getClass()
		.getSimpleName());
	File file = new File(tempDir.getAbsolutePath()
		+ System.getProperty("file.separator") + "serialize.txt");

	Long featureId = 1001L;
	GisFeature gisFeature = GeolocTestHelper.createCity("Saint-André",
		1.5F, 2F, featureId);
	AlternateName alternateName = new AlternateName();
	alternateName.setName("alteré");
	alternateName.setGisFeature(gisFeature);
	alternateName.setSource(AlternateNameSource.ALTERNATENAMES_FILE);
	gisFeature.addAlternateName(alternateName);
	City paris = new City(gisFeature);
	paris.addZipCode(new ZipCode("50263"));

	// save cities and check it is saved
	this.cityDao.save(paris);
	assertNotNull(this.cityDao.getByFeatureId(featureId));
	// commit changes
	this.solRSynchroniser.commit();

	OutputStream outputStream = null;
	try {
	    outputStream = new FileOutputStream(file);
	} catch (FileNotFoundException e1) {
	    fail();
	}

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
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
	FeedChecker.assertQ("The query return incorrect values", content,
		"//*[@numFound='1']", "//*[@name='status'][.='0']",
		"//*[@name='" + FullTextFields.FULLY_QUALIFIED_NAME.getValue()
			+ "'][.='" + paris.getFullyQualifiedName(false) + "']");

	// delete temp dir
	assertTrue("the tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));

    }

    @Test
    public void testExecuteQueryToString() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='1']", "//*[@name='status'][.='0']",
		    "//*[@name='"
			    + FullTextFields.FULLY_QUALIFIED_NAME.getValue()
			    + "'][.='" + city.getFullyQualifiedName(false)
			    + "']");
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }
    
    @Test
    public void testExecuteQueryWithMultiplePlacetype() {
	City city = GeolocTestHelper.createCity("paris", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	Adm adm = GeolocTestHelper.createAdm("paris", "FR", "A1",
			null, null, null, null, 1);
	this.admDao.save(adm);
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("paris",
		    pagination, output, new Class[]{City.class,Adm.class}, "fr");
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='2']", "//*[@name='status'][.='0']"
		    );
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }
    
    @Test
    public void testExecuteQueryWithMultiplePlacetypewithNullPlacetypeAtEnd() {
	City city = GeolocTestHelper.createCity("paris", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	Adm adm = GeolocTestHelper.createAdm("paris", "FR", "A1",
			null, null, null, null, 1);
	this.admDao.save(adm);
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("paris",
		    pagination, output, new Class[]{City.class,null}, "fr");
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='1']", "//*[@name='status'][.='0']"
		    );
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }
    
    @Test
    public void testExecuteQueryWithMultiplePlacetypewithNullPlacetypeAtBeginning() {
	City city = GeolocTestHelper.createCity("paris", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	Adm adm = GeolocTestHelper.createAdm("paris", "FR", "A1",
			null, null, null, null, 1);
	this.admDao.save(adm);
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("paris",
		    pagination, output, new Class[]{null,City.class,null}, "fr");
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='1']", "//*[@name='status'][.='0']"
		    );
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }
    
    @Test
    public void testExecuteQueryToStringWithFeatureIDShouldWorkWithSpace() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("1001 ",
		    pagination, output,ONLY_CITY_PLACETYPE, "fr");
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='1']", "//*[@name='status'][.='0']",
		    "//*[@name='"
			    + FullTextFields.FULLY_QUALIFIED_NAME.getValue()
			    + "'][.='" + city.getFullyQualifiedName(false)
			    + "']");
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }
    
    @Test
    public void testExecuteQueryToStringWithFeatureID() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("1001",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='1']", "//*[@name='status'][.='0']",
		    "//*[@name='"
			    + FullTextFields.FULLY_QUALIFIED_NAME.getValue()
			    + "'][.='" + city.getFullyQualifiedName(false)
			    + "']");
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }
    
    @Test
    public void testSearchShouldBeMinusSignInsensitive() {
	City city = GeolocTestHelper.createCity("Saint André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='1']", "//*[@name='status'][.='0']",
		    "//*[@name='"
			    + FullTextFields.FULLY_QUALIFIED_NAME.getValue()
			    + "'][.='" + city.getFullyQualifiedName(false)
			    + "']");
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }

    @Test
    public void testExecuteQueryToStringShouldTakeSpellCheckerIntoAccount() {
	City city = GeolocTestHelper.createCity("Saint Andre", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();
	//buildIndex
	Map<String, Boolean> spellChekerResultMap = spellCheckerIndexer
		.buildAllIndex();
	for (String key : spellChekerResultMap.keySet()) {
	    assertTrue(spellChekerResultMap.get(key).booleanValue());
	}

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr").withSpellChecking();
	    fulltextQuery.toQueryString();
	    String result = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    FeedChecker.assertQ("The query return incorrect values", result,
		    "//*[@numFound='1']", "//*[@name='status'][.='0']",
		    "//*[@name='"
			    + FullTextFields.FULLY_QUALIFIED_NAME.getValue()
			    + "'][.='" + city.getFullyQualifiedName(false)
			    + "']", "//*[@name='"
			    + FullTextFields.SPELLCHECK.getValue() + "']",
		"//*[@name='" + FullTextFields.SPELLCHECK_SUGGESTIONS.getValue()
				+ "']"
			,"//*[@name='" + FullTextFields.SPELLCHECK_SUGGESTIONS.getValue()
				+ "'][./lst[1][@name='andré'][./arr[1]/str[1]/.='andre']]"

	    );
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }

    @Test
    public void testExecuteQueryToStringForPHP() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.PHP)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    // don't know how to test php syntax
	    String response = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    assertTrue(response.startsWith("array"));
	    assertTrue(!response.contains("error"));
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }

    @Test
    public void testExecuteQueryToStringForAtom() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.ATOM)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    // don't know how to test php syntax
	    String response = fullTextSearchEngine
		    .executeQueryToString(fulltextQuery);
	    assertTrue(!response.contains("error"));
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }

    @Test
    public void testExecuteQuery() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();

	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    FulltextResultsDto results = fullTextSearchEngine
		    .executeQuery(fulltextQuery);
	    Assert.assertTrue("Qtime should be set", results.getQTime() != 0);
	    Assert.assertEquals("resultSize should have a correct value", 1,
		    results.getResultsSize());
	    Assert.assertEquals("resultSize should be equals to result.size()",
		    results.getResults().size(), results.getResultsSize());
	    Assert.assertEquals("numFound should be set ", 1, results
		    .getNumFound());
	    Assert.assertTrue("maxScore should be set ",
		    results.getMaxScore() != 0);
	    Assert.assertEquals("The results are not correct", "Saint-André",
		    results.getResults().get(0).getName());
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }
    
    @Test
    public void testExecuteQueryWithSpellCheckingAndWithSpellResults() {
	City city = GeolocTestHelper.createCity("Saint-Andre", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();
	//buildIndex
	Map<String, Boolean> spellChekerResultMap = spellCheckerIndexer
		.buildAllIndex();
	for (String key : spellChekerResultMap.keySet()) {
	    assertTrue(spellChekerResultMap.get(key).booleanValue());
	}
	boolean collatedSavedvalue = SpellCheckerConfig.collateResults; 
	boolean enabledSavedvalue = SpellCheckerConfig.enabled; 
	try {
	    SpellCheckerConfig.collateResults = true;
	    SpellCheckerConfig.enabled = true;
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr").withSpellChecking();
	    FulltextResultsDto result = fullTextSearchEngine
		    .executeQuery(fulltextQuery);
	    Map<String, Suggestion> suggestionMap = result.getSuggestionMap();
	    assertNotNull("suggestionMap should never be null",suggestionMap);
	    assertEquals(1, suggestionMap.size());
	    String[] keys =  suggestionMap.keySet().toArray(new String[1]);
	    assertEquals("andré", keys[0]);
	    assertNotNull(suggestionMap.get(keys[0]));
	    assertEquals("andre", result.getSpellCheckProposal());
	    assertNotNull(result.getCollatedResult());
	    assertFalse(result.getCollatedResult().startsWith(" "));
	    assertFalse(result.getCollatedResult().endsWith(" "));
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	} finally {
	    SpellCheckerConfig.collateResults = collatedSavedvalue;
	    SpellCheckerConfig.enabled = enabledSavedvalue;
	}
    }
    
    @Test
    public void testExecuteQueryWithSpellCheckingAndWithSpellResultsAndWithoutCollate() {
	City city = GeolocTestHelper.createCity("Saint-Andre", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();
	//buildIndex
	Map<String, Boolean> spellChekerResultMap = spellCheckerIndexer
		.buildAllIndex();
	for (String key : spellChekerResultMap.keySet()) {
	    assertTrue(spellChekerResultMap.get(key).booleanValue());
	}
	boolean collatedSavedvalue = SpellCheckerConfig.collateResults; 
	boolean enabledSavedvalue = SpellCheckerConfig.enabled; 
	try {
	    SpellCheckerConfig.collateResults = false;
	    SpellCheckerConfig.enabled = true;
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr").withSpellChecking();
	    FulltextResultsDto result = fullTextSearchEngine
		    .executeQuery(fulltextQuery);
	    Map<String, Suggestion> suggestionMap = result.getSuggestionMap();
	    assertNotNull("suggestionMap should never be null",suggestionMap);
	    assertEquals(1, suggestionMap.size());
	    String[] keys =  suggestionMap.keySet().toArray(new String[1]);
	    assertEquals("andré", keys[0]);
	    assertNotNull(suggestionMap.get(keys[0]));
	    assertEquals("andre", result.getSpellCheckProposal());
	    assertNull(result.getCollatedResult());
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	} finally {
	    SpellCheckerConfig.collateResults = collatedSavedvalue;
	    SpellCheckerConfig.enabled = enabledSavedvalue;
	}
    }
    
    @Test
    public void testExecuteQueryWithSpellCheckingAndWithSpellResultsWithoutSpellCheckingEnabled() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();
	//buildIndex
	Map<String, Boolean> spellChekerResultMap = spellCheckerIndexer
		.buildAllIndex();
	for (String key : spellChekerResultMap.keySet()) {
	    assertTrue(spellChekerResultMap.get(key).booleanValue());
	}
	boolean collatedSavedvalue = SpellCheckerConfig.collateResults; 
	boolean enabledSavedvalue = SpellCheckerConfig.enabled; 
	try {
	    SpellCheckerConfig.collateResults = true;
	    SpellCheckerConfig.enabled = false;
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr").withSpellChecking();
	    FulltextResultsDto result = fullTextSearchEngine
		    .executeQuery(fulltextQuery);
	    Map<String, Suggestion> suggestionMap = result.getSuggestionMap();
	    assertEquals("suggestionMap should not have elements if there is no results ", 0 ,suggestionMap.size());
	    assertNotNull("suggestionMap should never be null",suggestionMap);
	    assertNull(result.getSpellCheckProposal());
	    assertNull(result.getCollatedResult());
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	} finally {
	    SpellCheckerConfig.collateResults = collatedSavedvalue;
	    SpellCheckerConfig.enabled = enabledSavedvalue;
	}
    }

    @Test
    public void testExecuteQueryWithSpellCheckingAndWithOutSpellResults() {
	City city = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F, 1001L);
	this.cityDao.save(city);
	assertNotNull(this.cityDao.getByFeatureId(1001L));
	// commit changes
	this.solRSynchroniser.commit();
	//buildIndex
	Map<String, Boolean> spellChekerResultMap = spellCheckerIndexer
		.buildAllIndex();
	for (String key : spellChekerResultMap.keySet()) {
	    assertTrue(spellChekerResultMap.get(key).booleanValue());
	}
	boolean collatedSavedvalue = SpellCheckerConfig.collateResults; 
	boolean enabledSavedvalue = SpellCheckerConfig.enabled; 
	try {
	    SpellCheckerConfig.collateResults = true;
	    SpellCheckerConfig.enabled = true;
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("noSpellresults",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr").withSpellChecking();
	    FulltextResultsDto result = fullTextSearchEngine
		    .executeQuery(fulltextQuery);
	    Map<String, Suggestion> suggestionMap = result.getSuggestionMap();
	    assertNotNull("suggestionMap should never be null",suggestionMap);
	    assertEquals("suggestionMap should not have elements if there is no results ", 0,suggestionMap.size());
	    assertNull(result.getSpellCheckProposal());
	    assertNull(result.getCollatedResult());
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	} finally {
	    SpellCheckerConfig.collateResults = collatedSavedvalue;
	    SpellCheckerConfig.enabled = enabledSavedvalue;
	}
    }

    
    @Test
    public void testExecuteQueryWithNoResults() {
	try {
	    Pagination pagination = paginate().from(1).to(10);
	    Output output = Output.withFormat(OutputFormat.XML)
		    .withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		    .withIndentation();
	    FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		    pagination, output, ONLY_CITY_PLACETYPE, "fr");
	    FulltextResultsDto results = fullTextSearchEngine
		    .executeQuery(fulltextQuery);
	    Assert.assertTrue("Qtime should be set", results.getQTime() != 0);
	    Assert.assertEquals(
		    "resultSize  be equals to 0 when no results are found", 0,
		    results.getResultsSize());
	    Assert.assertEquals("resultSize should be equals to result.size()",
		    results.getResults().size(), results.getResultsSize());
	    Assert
		    .assertEquals(
			    "numFound should be equals to 0 when no results are found ",
			    0, results.getNumFound());
	    Assert
		    .assertEquals(
			    "maxScore should  be equals to 0 when no results are found ",
			    0F, results.getMaxScore());
	    Assert.assertEquals(
		    "The results should not be null, bt an empty List", 0,
		    results.getResults().size());
	} catch (FullTextSearchException e) {
	    fail("error during search : " + e.getMessage());
	}
    }

    @Test
    public void testStatsShouldBeIncreaseForAllCall() {
	statsUsageService.resetUsage(StatsUsageType.FULLTEXT);
	Pagination pagination = paginate().from(1).to(10);
	Output output = Output.withFormat(OutputFormat.XML).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, ONLY_CITY_PLACETYPE, "fr");
	fullTextSearchEngine.executeQueryToDatabaseObjects(fulltextQuery);
	assertEquals(new Long(1), statsUsageService
		.getUsage(StatsUsageType.FULLTEXT));
	fullTextSearchEngine.executeAndSerialize(fulltextQuery,
		new ByteArrayOutputStream());
	assertEquals(new Long(2), statsUsageService
		.getUsage(StatsUsageType.FULLTEXT));
	fullTextSearchEngine.executeQuery(fulltextQuery);
	assertEquals(new Long(3), statsUsageService
		.getUsage(StatsUsageType.FULLTEXT));
	fullTextSearchEngine.executeQueryToString(fulltextQuery);
	assertEquals(new Long(4), statsUsageService
		.getUsage(StatsUsageType.FULLTEXT));
    }

    @Test
    public void testReturnFields() {
	// the fields are tested in solrsynchroniserTest, we don't want to
	// duplicate tests
    }

    public void setCityDao(ICityDao cityDao) {
	this.cityDao = cityDao;
    }

	public void setAdmDao(IAdmDao admDao) {
		this.admDao = admDao;
	}

}
