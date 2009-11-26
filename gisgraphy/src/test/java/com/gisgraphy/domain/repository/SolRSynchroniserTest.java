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
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.Language;
import com.gisgraphy.domain.geoloc.entity.ZipCodeAware;
import com.gisgraphy.domain.geoloc.entity.event.GisFeatureDeleteAllEvent;
import com.gisgraphy.domain.geoloc.entity.event.GisFeatureDeletedEvent;
import com.gisgraphy.domain.geoloc.entity.event.GisFeatureStoredEvent;
import com.gisgraphy.domain.geoloc.entity.event.PlaceTypeDeleteAllEvent;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextFields;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextSearchException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.ISpellCheckerIndexer;
import com.gisgraphy.domain.geoloc.service.geoloc.GisgraphyCommunicationException;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.helper.FileHelper;
import com.gisgraphy.helper.URLUtils;
import com.gisgraphy.test.FeedChecker;
import com.gisgraphy.test.GeolocTestHelper;

public class SolRSynchroniserTest extends AbstractIntegrationHttpSolrTestCase {

    @Resource
    private ICityDao cityDao;

    @Resource
    private ICountryDao countryDao;
    
    @Resource
    private IAdmDao admDao;

    @Resource
    private GeolocTestHelper geolocTestHelper;
    
    @Resource
    private ILanguageDao languageDao;
    
    @Resource
    private ISpellCheckerIndexer spellCheckerIndexer;

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

    @Test
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
    public void testDeleteAllGisFeaturesOfASpecificPlaceTypeShouldRetryOnFailure() throws SolrServerException, IOException {
	
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.deleteByQuery(((String)EasyMock.anyObject()))).andThrow(new SolrServerException("exception"));
	expect(mockSolrServer.deleteByQuery(((String)EasyMock.anyObject()))).andReturn(null);
	expect(mockSolrServer.commit(true, true)).andReturn(null);
	expect(mockSolrServer.optimize(true,true)).andReturn(null);
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	fakeSolrsynchroniser.handleEvent(new PlaceTypeDeleteAllEvent(City.class));
	EasyMock.verify(mockSolrServer);
    }
    
    @Test
    public void testDeleteAllGisFeaturesOfASpecificPlaceTypeShouldFailWhenMaxNumberOfRetryIsReached() throws SolrServerException, IOException {
	
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.deleteByQuery(((String)EasyMock.anyObject()))).andStubThrow(new SolrServerException("exception"));
	expect(mockSolrServer.commit(true, true)).andReturn(null);
	expect(mockSolrServer.optimize(true,true)).andReturn(null);
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	try {
	    fakeSolrsynchroniser.handleEvent(new PlaceTypeDeleteAllEvent(City.class));
	    fail("The solrSynchroniser should have throw");
	} catch (GisgraphyCommunicationException ignore) {
	}
    }
    
    @Test
    public void testDeleteAFeatureShouldRetryOnFailure() throws SolrServerException, IOException {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(2L);
	
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.deleteById("2")).andThrow(new SolrServerException("exception"));
	expect(mockSolrServer.deleteById("2")).andReturn(null);
	expect(mockSolrServer.commit(true, true)).andReturn(null);
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	fakeSolrsynchroniser.handleEvent(new GisFeatureDeletedEvent(city));
	EasyMock.verify(mockSolrServer);
    }
    
    @Test
    public void testDeleteAFeatureShouldFailWhenMaxNumberOfRetryIsReached() throws SolrServerException, IOException {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(2L);
	
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.deleteById("2")).andStubThrow(new SolrServerException("exception"));
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakesolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	try {
	    fakesolrsynchroniser.handleEvent(new GisFeatureDeletedEvent(city));
	    fail("The solrSynchroniser should have throw");
	} catch (GisgraphyCommunicationException ignore) {
	}

    }
    
    
    @Test
    public void testSaveAFeatureShouldRetryOnFailure() throws SolrServerException, IOException {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(2L);
	
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.add(((SolrInputDocument)EasyMock.anyObject()))).andThrow(new SolrServerException("exception"));
	expect(mockSolrServer.add(((SolrInputDocument)EasyMock.anyObject()))).andReturn(null);
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	fakeSolrsynchroniser.handleEvent(new GisFeatureStoredEvent(city));
	EasyMock.verify(mockSolrServer);
    }
    
    @Test
    public void testSaveAFeatureShouldFailWhenMaxNumberOfRetryIsReached() throws SolrServerException, IOException {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(2L);
	
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.add(((SolrInputDocument)EasyMock.anyObject()))).andStubThrow(new SolrServerException("exception"));
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakesolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	try {
	    fakesolrsynchroniser.handleEvent(new GisFeatureStoredEvent(city));
	    fail("The solrSynchroniser should have throw");
	} catch (GisgraphyCommunicationException ignore) {
	}

    }
    
    
    @Test
    public void testDeleteAListOfFeatureShouldRetryOnFailure() throws SolrServerException, IOException {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(2L);
	
	SolrServer mockSolrServer = EasyMock.createMock(SolrServer.class);
	expect(mockSolrServer.deleteById("2")).andThrow(new SolrServerException("exception"));
	expect(mockSolrServer.deleteById("2")).andReturn(null);
	expect(mockSolrServer.commit(true, true)).andReturn(null);
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	List<City> listOfFeature = new ArrayList<City>();
	listOfFeature.add(city);
	fakeSolrsynchroniser.handleEvent(new GisFeatureDeleteAllEvent(listOfFeature));
	EasyMock.verify(mockSolrServer);
    }
    
    @Test
    public void testCommitShouldRetryOnFailure() throws SolrServerException, IOException {
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.commit(true,true)).andThrow(new SolrServerException("exception"));
	expect(mockSolrServer.commit(true, true)).andReturn(null);
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	Assert.assertTrue("When a commit is success it must return true",fakeSolrsynchroniser.commit());
	EasyMock.verify(mockSolrServer);
    }
    
    @Test
    public void testOptimizeShouldRetryOnFailure() throws SolrServerException, IOException {
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.optimize(true,true)).andThrow(new SolrServerException("exception"));
	expect(mockSolrServer.optimize(true, true)).andReturn(null);
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	fakeSolrsynchroniser.optimize();
	EasyMock.verify(mockSolrServer);
    }
    
    @Test
    public void testOptimizeShouldFailWhenMaxNumberOfRetryIsReached() throws SolrServerException, IOException {
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.optimize(true,true)).andStubThrow(new SolrServerException("exception"));
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	try {
	    fakeSolrsynchroniser.optimize();
	} catch (GisgraphyCommunicationException ignore) {
	}
    }
    
    @Test
    public void testCommitShouldReturnFalseWhenMaxNumberOfRetryIsReached() throws SolrServerException, IOException {
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.commit(true,true)).andStubThrow(new SolrServerException("exception"));
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	assertFalse("When a commit fail it must return false",fakeSolrsynchroniser.commit());
	   
    }
    
    
    @Test
    public void testDeleteAListOfFeatureShouldFailWhenMaxNumberOfRetryIsReached() throws SolrServerException, IOException {
	City city = GeolocTestHelper.createCityAtSpecificPoint("my city", 1.5F,
		1.6F);
	city.setFeatureId(2L);
	
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.deleteById("2")).andStubThrow(new SolrServerException("exception"));
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	List<City> listOfFeature = new ArrayList<City>();
	listOfFeature.add(city);
	try {
	    fakeSolrsynchroniser.handleEvent(new GisFeatureDeleteAllEvent(listOfFeature));
	    fail("The solrSynchroniser should have throw");
	} catch (GisgraphyCommunicationException e) {
	}
    }
    
    
    
    @Test
    public void testDeleteAllShouldRetryOnFailure() throws SolrServerException, IOException {
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.deleteByQuery("*:*")).andThrow(new SolrServerException("exception"));
	expect(mockSolrServer.deleteByQuery("*:*")).andReturn(null);
	expect(mockSolrServer.commit(true, true)).andReturn(null);
	expect(mockSolrServer.optimize(true,true)).andReturn(null);
	EasyMock.replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	fakeSolrsynchroniser.deleteAll();
	EasyMock.verify(mockSolrServer);
    }
    
    @Test
    public void testDeleteAllShouldFailWhenMaxNumberOfRetryIsReached() throws SolrServerException, IOException {
	SolrServer mockSolrServer = createMock(SolrServer.class);
	expect(mockSolrServer.deleteByQuery("*:*")).andStubThrow((new SolrServerException("exception")));
	replay(mockSolrServer);
	
	IsolrClient mockSolrClient = createMock(IsolrClient.class);
	expect(mockSolrClient.getServer()).andStubReturn(mockSolrServer);
	replay(mockSolrClient);
	
	ISolRSynchroniser fakeSolrsynchroniser = new SolRSynchroniser(mockSolrClient);
	
	try {
	    fakeSolrsynchroniser.deleteAll();
	    fail("The solrSynchroniser should have throw");
	} catch (GisgraphyCommunicationException ignore) {
	}
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
    public void testDeleteAllGisFeaturesOfASpecificPlaceTypeShouldSynchronize() {
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
    
    @Test
    public void testZipCodeShouldBeSynchronisedIfFeatureIsACity() {
	// create one city
	Long featureId = 1001L;
	City paris = GeolocTestHelper.createCity("Saint-André", 1.5F, 2F,
		featureId);

	paris.setZipCode("50263");

	// save city and check it is saved
	ZipCodeAware saved = this.cityDao.save(paris);
	assertNotNull(this.cityDao.getByFeatureId(featureId));

	// commit changes
	this.solRSynchroniser.commit();


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
	//buildIndex
	Map<String,Boolean> spellChekerResultMap = spellCheckerIndexer.buildAllIndex();
	for (String key : spellChekerResultMap.keySet()){
	    assertTrue(spellChekerResultMap.get(key).booleanValue());
	}
	File tempDir = FileHelper.createTempDir(this.getClass()
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
		    pagination, output, City.class, "fr").withSpellChecking();
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

	FeedChecker.assertQ(
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
		"//*[@name='" + FullTextFields.LAT.getValue() + "'][.='2.5']",
		"//*[@name='" + FullTextFields.LONG.getValue() + "'][.='1.5']",
		"//*[@name='" + FullTextFields.PLACETYPE.getValue()
			+ "'][.='City']", "//*[@name='"
			+ FullTextFields.POPULATION.getValue()
			+ "'][.='10000000']", "//*[@name='"
			+ FullTextFields.ZIPCODE.getValue() + "'][.='50263']"

		, "//*[@name='" + FullTextFields.NAMEASCII.getValue()
			+ "'][.='ascii']",
		"//*[@name='" + FullTextFields.ELEVATION.getValue()
			+ "'][.='13456']"
		, "//*[@name='"
			+ FullTextFields.GTOPO30.getValue() + "'][.='7654']",
		"//*[@name='" + FullTextFields.TIMEZONE.getValue()
			+ "'][.='Europe/Paris']"

		, "//*[@name='" + FullTextFields.COUNTRY_FLAG_URL.getValue()
			+ "'][.='"
			+ URLUtils.createCountryFlagUrl(paris.getCountryCode())
			+ "']"
		, "//*[@name='"
			+ FullTextFields.GOOGLE_MAP_URL.getValue()
			+ "'][.='"
			+ URLUtils.createGoogleMapUrl(paris.getLocation(),
				paris.getName()) + "']", "//*[@name='"
			+ FullTextFields.YAHOO_MAP_URL.getValue() + "'][.='"
			+ URLUtils.createYahooMapUrl(paris.getLocation())
			+ "']"
		,//spellchecker fields
		"//*[@name='" + FullTextFields.SPELLCHECK.getValue()
			+ "']"
		,"//*[@name='" + FullTextFields.SPELLCHECK_SUGGESTIONS.getValue()
			+ "']"
		,"//*[./arr[1]/str[1]/.='saint']"
		,"//*[./arr[1]/str[1]/.='andré']"
		,"//*[./arr[1]/str[1]/.='france']"
	
	);

	// delete temp dir
	assertTrue("the tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));

    }
    
    
    @Test
    public void testSynchronizeAcountryShouldSynchronizeCountrySpecificFields() {
	Country country = GeolocTestHelper
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
	
	String CountryName = "France";
	country.setName(CountryName);
	countryDao.save(country);
	// commit changes
	this.solRSynchroniser.commit();
	File tempDir = FileHelper.createTempDir(this.getClass()
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
	    FulltextQuery fulltextQuery = new FulltextQuery(CountryName,
		    pagination, output, Country.class,null).withoutSpellChecking();
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

	FeedChecker.assertQ(
		"The query return incorrect values",
		content,
		"//*[@numFound='1']",
		"//*[@name='status'][.='0']",
		"//*[@name='" + FullTextFields.CONTINENT.getValue()
			+ "'][.='"+country.getContinent()+"']",
		"//*[@name='" + FullTextFields.CURRENCY_CODE.getValue()
			+ "'][.='"+country.getCurrencyCode()+"']",
		"//*[@name='" + FullTextFields.CURRENCY_NAME.getValue()
			+ "'][.='"+country.getCurrencyName()+"']",
		"//*[@name='" + FullTextFields.CURRENCY_CODE.getValue()
			+ "'][.='"+country.getCurrencyCode()+"']",
		"//*[@name='" + FullTextFields.FIPS_CODE.getValue()
			+ "'][.='"+country.getFipsCode()+"']",
		"//*[@name='" + FullTextFields.ISOALPHA2_COUNTRY_CODE.getValue()
			+ "'][.='"+country.getIso3166Alpha2Code()+"']",
		"//*[@name='" + FullTextFields.ISOALPHA3_COUNTRY_CODE.getValue()
			+ "'][.='"+country.getIso3166Alpha3Code()+"']",
		"//*[@name='" + FullTextFields.POSTAL_CODE_MASK.getValue()
			+ "'][.='"+country.getPostalCodeMask()+"']",
		"//*[@name='" + FullTextFields.POSTAL_CODE_REGEX.getValue()
			+ "'][.='"+country.getPostalCodeRegex()+"']",
		"//*[@name='" + FullTextFields.PHONE_PREFIX.getValue()
			+ "'][.='"+country.getPhonePrefix()+"']",
		"//*[@name='" + FullTextFields.SPOKEN_LANGUAGES.getValue()
			+ "'][./str[1]][.='"+country.getSpokenLanguages().get(0).getIso639LanguageName()+"']",
		"//*[@name='" + FullTextFields.TLD.getValue()
			+ "'][.='"+country.getTld()+"']",
		"//*[@name='" + FullTextFields.AREA.getValue()
			+ "'][.='"+country.getArea()+"']",
		"//*[@name='" + FullTextFields.NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_SUFFIX.getValue()
			+ "'][./str[1]][.='"+alternateName.getName()+"']",
		"//*[@name='" + FullTextFields.NAME.getValue()
			+ FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue()
			+ "FR'][./str[1]][.='"+alternateNameLocalized.getName()+"']"
			
	
	);

	// delete temp dir
	assertTrue("the tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));

    }
    
    @Test
    public void testSynchronizeAcountryShouldSynchronizeAdmSpecificFields() {
    
    Adm adm = GeolocTestHelper
	.createAdm("AdmName", "FR", "A1", "B2", null, null, null, 2);

        admDao.save(adm);

        this.solRSynchroniser.commit();
        File tempDir = FileHelper.createTempDir(this.getClass()
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
	    FulltextQuery fulltextQuery = new FulltextQuery(adm.getName(),
		    pagination, output, Adm.class,null).withoutSpellChecking();
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

	FeedChecker.assertQ("The query return incorrect values",
		content,
		"//*[@numFound='1']",
		"//*[@name='status'][.='0']"
		// name
		,
		"//*[@name='" + FullTextFields.LEVEL.getValue()
			+ "'][.='"+adm.getLevel()+"']",
		"//*[@name='" + FullTextFields.ADM1CODE.getValue()
			+ "'][.='"+adm.getAdm1Code()+"']",
		"//*[@name='" + FullTextFields.ADM2CODE.getValue()
			+ "'][.='"+adm.getAdm2Code()+"']"
		
	);
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
