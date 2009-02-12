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

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.SpellCheckerConfig;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.servlet.FulltextServlet;
import com.gisgraphy.test.GeolocTestHelper;

public class FulltextQueryTest extends AbstractIntegrationHttpSolrTestCase {

    @Resource
    private ICountryDao countryDao;

    @Test
    public void testFulltextQueryStringPaginationOutputClassOfQextendsGisFeature() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("text", pagination,
		output, Adm.class, "fr");
	assertEquals(pagination, fulltextQuery.getPagination());
	assertEquals(output, fulltextQuery.getOutput());
	assertEquals(Adm.class, fulltextQuery.getPlaceType());
	assertEquals("text", fulltextQuery.getQuery());
	assertTrue(fulltextQuery.isOutputIndented());
	assertEquals("fr", fulltextQuery.getCountryCode());
    }

    @Test
    public void testFulltextQueryFromAnHttpServletRequest() {
	MockHttpServletRequest request = GeolocTestHelper
		.createMockHttpServletRequestForFullText();
	FulltextQuery query = new FulltextQuery(request);
	assertEquals(3, query.getFirstPaginationIndex());
	assertEquals(12, query.getLastPaginationIndex());
	assertEquals("the pagination should be limit to "
		+ FulltextServlet.DEFAULT_MAX_RESULTS,
		FulltextServlet.DEFAULT_MAX_RESULTS, query
			.getMaxNumberOfResults());
	assertEquals("FR", query.getCountryCode());
	assertEquals(OutputFormat.XML, query.getOutputFormat());
	assertEquals("FR", query.getOutputLanguage());
	assertEquals(OutputStyle.FULL, query.getOutputStyle());
	assertEquals(City.class, query.getPlaceType());
	assertEquals("query", query.getQuery());

	// test first pagination index
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.FROM_PARAMETER);
	query = new FulltextQuery(request);
	assertEquals("When no " + FulltextServlet.FROM_PARAMETER
		+ " is specified, the parameter should be "
		+ Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		.getFirstPaginationIndex());
	// with a wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FROM_PARAMETER, "-1");
	query = new FulltextQuery(request);
	assertEquals("When a wrong " + FulltextServlet.FROM_PARAMETER
		+ " is specified, the parameter should be "
		+ Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		.getFirstPaginationIndex());
	// with a non mumeric value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FROM_PARAMETER, "a");
	query = new FulltextQuery(request);
	assertEquals("When a wrong " + FulltextServlet.FROM_PARAMETER
		+ " is specified, the parameter should be "
		+ Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		.getFirstPaginationIndex());

	// test last pagination index
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.TO_PARAMETER);
	query = new FulltextQuery(request);
	assertEquals(
		"When no "
			+ FulltextServlet.TO_PARAMETER
			+ " is specified, the  parameter should be set to limit results to "
			+ FulltextServlet.DEFAULT_MAX_RESULTS,
		FulltextServlet.DEFAULT_MAX_RESULTS, query
			.getMaxNumberOfResults());
	// with a wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.TO_PARAMETER, "2");// to<from
	query = new FulltextQuery(request);
	assertEquals("When a wrong " + FulltextServlet.TO_PARAMETER
		+ " is specified, the numberOf results should be "
		+ Pagination.DEFAULT_MAX_RESULTS,
		Pagination.DEFAULT_MAX_RESULTS, query.getMaxNumberOfResults());
	assertEquals("a wrong to does not change the from value", 3, query
		.getFirstPaginationIndex());
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.TO_PARAMETER, "a");// to<from
	query = new FulltextQuery(request);
	assertEquals("a wrong to does not change the from value", 3, query
		.getFirstPaginationIndex());
	assertEquals("When a wrong " + FulltextServlet.TO_PARAMETER
		+ " is specified, the numberOf results should be "
		+ Pagination.DEFAULT_MAX_RESULTS,
		Pagination.DEFAULT_MAX_RESULTS, query.getMaxNumberOfResults());

	// test countrycode
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.COUNTRY_PARAMETER);
	query = new FulltextQuery(request);
	assertNull("When no " + FulltextServlet.COUNTRY_PARAMETER
		+ " is specified, the parameter should be set to null", query
		.getCountryCode());
	// with a wrong value
	// can not have a wrong value=>always a string

	// test outputFormat
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.FORMAT_PARAMETER);
	query = new FulltextQuery(request);
	assertEquals("When no " + FulltextServlet.FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getOutputFormat());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FORMAT_PARAMETER, "UNK");
	query = new FulltextQuery(request);
	assertEquals("When wrong " + FulltextServlet.FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getOutputFormat());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FORMAT_PARAMETER, "json");
	query = new FulltextQuery(request);
	assertEquals(FulltextServlet.FORMAT_PARAMETER
		+ " should be case insensitive  ", OutputFormat.JSON, query
		.getOutputFormat());

	// test language
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.LANG_PARAMETER);
	query = new FulltextQuery(request);
	assertNull(FulltextServlet.LANG_PARAMETER
		+ " should be null when not specified  ", query
		.getOutputLanguage());
	// with empty string
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.LANG_PARAMETER, " ");
	query = new FulltextQuery(request);
	assertEquals(FulltextServlet.LANG_PARAMETER
		+ " should be null when not specified  ",
		Output.DEFAULT_LANGUAGE_CODE, query.getOutputLanguage());

	// test uppercase
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.LANG_PARAMETER, "fr");
	query = new FulltextQuery(request);
	assertEquals(FulltextServlet.LANG_PARAMETER + " should be uppercase  ",
		"FR", query.getOutputLanguage());

	// test placetype
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.PLACETYPE_PARAMETER);
	query = new FulltextQuery(request);
	assertNull("When no " + FulltextServlet.PLACETYPE_PARAMETER
		+ " is specified, the  parameter should be set null ", query
		.getPlaceType());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.PLACETYPE_PARAMETER, "unk");
	query = new FulltextQuery(request);
	assertNull("When wrong " + FulltextServlet.PLACETYPE_PARAMETER
		+ " is specified, the  parameter should be set null ", query
		.getPlaceType());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FORMAT_PARAMETER, "city");
	query = new FulltextQuery(request);
	assertEquals(FulltextServlet.PLACETYPE_PARAMETER
		+ " should be case insensitive  ", City.class, query
		.getPlaceType());

	// test output style
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.STYLE_PARAMETER);
	query = new FulltextQuery(request);
	assertEquals("When no " + FulltextServlet.STYLE_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputStyle.getDefault(), OutputStyle.getDefault(), query
		.getOutputStyle());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.STYLE_PARAMETER, "UNK");
	query = new FulltextQuery(request);
	assertEquals("When wrong " + FulltextServlet.STYLE_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputStyle.getDefault(), OutputStyle.getDefault(), query
		.getOutputStyle());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.STYLE_PARAMETER, "medium");
	query = new FulltextQuery(request);
	assertEquals(FulltextServlet.STYLE_PARAMETER
		+ " should be case insensitive  ", OutputStyle.MEDIUM, query
		.getOutputStyle());

	// test indentation
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.INDENT_PARAMETER);
	query = new FulltextQuery(request);
	assertFalse("When no " + FulltextServlet.INDENT_PARAMETER
		+ " is specified, the  parameter should be set to false", query
		.isOutputIndented());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.INDENT_PARAMETER, "UNK");
	query = new FulltextQuery(request);
	assertFalse("When wrong " + FulltextServlet.INDENT_PARAMETER
		+ " is specified, the  parameter should be set to false", query
		.isOutputIndented());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.INDENT_PARAMETER, "True");
	query = new FulltextQuery(request);
	assertTrue(FulltextServlet.INDENT_PARAMETER
		+ " should be case insensitive  ", query.isOutputIndented());
	// test with on value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.INDENT_PARAMETER, "oN");
	query = new FulltextQuery(request);
	assertTrue(
		FulltextServlet.INDENT_PARAMETER
			+ " should be true for 'on' value (case insensitive and on value)  ",
		query.isOutputIndented());
	
	
	// test spellchecking
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.SPELLCHECKING_PARAMETER);
	query = new FulltextQuery(request);
	assertEquals("When no " + FulltextServlet.SPELLCHECKING_PARAMETER
		+ " is specified, the  parameter should be the default one",SpellCheckerConfig.activeByDefault, query
		.hasSpellChecking());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.SPELLCHECKING_PARAMETER, "UNK");
	query = new FulltextQuery(request);
	assertEquals("When wrong " + FulltextServlet.SPELLCHECKING_PARAMETER
		+ " is specified, the  parameter should be set to the default one",SpellCheckerConfig.activeByDefault, query
		.hasSpellChecking());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.SPELLCHECKING_PARAMETER, String.valueOf(!SpellCheckerConfig.activeByDefault).toUpperCase());
	query = new FulltextQuery(request);
	assertEquals(FulltextServlet.SPELLCHECKING_PARAMETER
		+ " should be case insensitive  ", !SpellCheckerConfig.activeByDefault, query.hasSpellChecking());
	// test with on value
	
	boolean savedSpellCheckingValue = SpellCheckerConfig.activeByDefault;
	try {
		SpellCheckerConfig.activeByDefault = false;
		request = GeolocTestHelper.createMockHttpServletRequestForFullText();
		request.setParameter(FulltextServlet.SPELLCHECKING_PARAMETER, "oN");
		query = new FulltextQuery(request);
		assertTrue(
			FulltextServlet.SPELLCHECKING_PARAMETER
				+ " should be true for 'on' value (case insensitive and on value)  ",
			query.hasSpellChecking());
	} catch (RuntimeException e) {
		//reset the last value
		SpellCheckerConfig.activeByDefault = savedSpellCheckingValue;
	}
	

	// test query
	// With no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.QUERY_PARAMETER);
	try {
	    query = new FulltextQuery(request);
	    fail("A null query should throw");
	} catch (RuntimeException e) {
	}
	// empty string
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.QUERY_PARAMETER, " ");
	try {
	    query = new FulltextQuery(request);
	    fail("An empty query should throw");
	} catch (RuntimeException e) {
	}
	// too long string
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.QUERY_PARAMETER, RandomStringUtils
		.random(FulltextQuery.QUERY_MAX_LENGTH) + 1);
	try {
	    query = new FulltextQuery(request);
	    fail("query must have a maximmum length of "
		    + FulltextQuery.QUERY_MAX_LENGTH);
	} catch (RuntimeException e) {
	}

    }

    @Test
    public void testFulltextQueryString() {
	FulltextQuery fulltextQuery = new FulltextQuery("text");
	assertEquals("text", fulltextQuery.getQuery());
	assertEquals(Output.DEFAULT_OUTPUT, fulltextQuery.getOutput());
	assertEquals(Pagination.DEFAULT_PAGINATION, fulltextQuery
		.getPagination());
	assertNull(fulltextQuery.getPlaceType());
	assertEquals("text", fulltextQuery.getQuery());
    }

    @Test
    public void testFulltextQueryWithNullQueryThrows() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	try {
	    new FulltextQuery(RandomStringUtils
		    .random(FulltextQuery.QUERY_MAX_LENGTH) + 1, pagination,
		    output, Adm.class, "FR");
	    fail("query must have a maximmum length of "
		    + FulltextQuery.QUERY_MAX_LENGTH);
	} catch (IllegalArgumentException e) {

	}

    }

    @Test
    public void testFulltextQueryWithTooLongQueryStringThrows() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	try {
	    new FulltextQuery(null, pagination, output, Adm.class, "FR");
	    fail("Null query should throws");
	} catch (IllegalArgumentException e) {

	}
    }

    @Test
    public void testFulltextQueryWithEmptyQueryThrows() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	try {
	    new FulltextQuery(" ", pagination, output, Adm.class, "FR");
	    fail("empty query should throws");
	} catch (IllegalArgumentException e) {

	}

	try {
	    new FulltextQuery(" ");
	    fail("Empty query should throws");
	} catch (RuntimeException e) {

	}
    }

    @Test
    public void testWithPaginationShouldBeSetToDefaultPaginationIfNull() {
	assertEquals(Pagination.DEFAULT_PAGINATION, new FulltextQuery("text")
		.withPagination(null).getPagination());
    }

    @Test
    public void testWithPaginationShouldSetThePagination() {
	assertEquals(5, new FulltextQuery("text").withPagination(
		paginate().from(5).to(23)).getPagination().getFrom());
	assertEquals(23, new FulltextQuery("text").withPagination(
		paginate().from(5).to(23)).getPagination().getTo());
    }

    @Test
    public void testWithOutputShouldBeSetToDefaultOutputIfNull() {
	assertEquals(Output.DEFAULT_OUTPUT, new FulltextQuery("text")
		.withOutput(null).getOutput());
    }

    @Test
    public void testWithOutputShouldSetTheOutput() {
	FulltextQuery fulltextQuery = new FulltextQuery("text");
	Pagination pagination = paginate().from(2).to(7);
	fulltextQuery.withPagination(pagination);
	assertEquals(pagination, fulltextQuery.getPagination());
    }

    @Test
    public void testWithPlaceTypeShouldBeSetToNullIfNull() {
	assertNull(new FulltextQuery("text").withPlaceType(null).getPlaceType());
    }

    public void testLimitToCountryCodeShouldSetTheCountryCode() {
	FulltextQuery fulltextQuery = new FulltextQuery("text")
		.limitToCountryCode("FR");
	assertEquals("FR", fulltextQuery.getCountryCode());
    }

    @Test
    public void testLimitToCountryCodeShouldBeSetToNull() {
	assertNull(new FulltextQuery("text").limitToCountryCode(null)
		.getCountryCode());
    }

    @Test
    public void testWithPlaceTypeShouldSetTheplaceType() {
	FulltextQuery fulltextQuery = new FulltextQuery("text");
	fulltextQuery.withPlaceType(Adm.class);
	assertEquals(Adm.class, fulltextQuery.getPlaceType());
    }

    @Test
    public void testToQueryStringShouldreturnCorrectParams() {
	Country france = GeolocTestHelper.createCountryForFrance();
	Country saved = countryDao.save(france);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, Adm.class, "fr");
	// split parameters
	HashMap<String, String> parameters = GeolocTestHelper.splitURLParams(
		fulltextQuery.toQueryString(), "&");
	// check parameters
	assertEquals(Output.OutputStyle.SHORT.getFieldList("FR"), parameters
		.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.JSON
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.typed.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertEquals("wrong query parameter found",
		"Saint-André France Admclass ", parameters
			.get(Constants.QUERY_PARAMETER));
    }

    @Test
    public void testToQueryStringShouldreturnCorrectParamsForGeoRSS() {
	Country france = GeolocTestHelper.createCountryForFrance();
	Country saved = countryDao.save(france);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.GEORSS)
		.withLanguageCode("FR").withStyle(OutputStyle.SHORT)
		.withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, Adm.class, "fr");
	// split parameters
	HashMap<String, String> parameters = GeolocTestHelper.splitURLParams(
		fulltextQuery.toQueryString(), "&");
	// check parameters
	assertEquals("wrong field list", Output.OutputStyle.MEDIUM
		.getFieldList("FR"), parameters.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.GEORSS
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong stylesheet", Constants.GEORSS_STYLESHEET,
		parameters.get(Constants.STYLESHEET_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.typed.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertEquals("wrong query parameter found",
		"Saint-André France Admclass ", parameters
			.get(Constants.QUERY_PARAMETER));
    }

    @Test
    public void testToQueryStringShouldreturnCorrectParamsForAtom() {
	Country france = GeolocTestHelper.createCountryForFrance();
	Country saved = countryDao.save(france);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.ATOM).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, Adm.class, "fr");
	// split parameters
	HashMap<String, String> parameters = GeolocTestHelper.splitURLParams(
		fulltextQuery.toQueryString(), "&");
	// check parameters
	assertEquals("wrong field list", Output.OutputStyle.MEDIUM
		.getFieldList("FR"), parameters.get(Constants.FL_PARAMETER));
	assertEquals("wrong indent parameter found", "on", parameters
		.get(Constants.INDENT_PARAMETER));
	assertEquals("wrong echoparams parameter found", "none", parameters
		.get(Constants.ECHOPARAMS_PARAMETER));
	assertEquals("wrong start parameter found", "2", parameters
		.get(Constants.START_PARAMETER));
	assertEquals("wrong rows parameter found", "8", parameters
		.get(Constants.ROWS_PARAMETER));
	assertEquals("wrong output format parameter found", OutputFormat.GEORSS
		.getParameterValue(), parameters
		.get(Constants.OUTPUT_FORMAT_PARAMETER));
	assertEquals("wrong stylesheet", Constants.ATOM_STYLESHEET, parameters
		.get(Constants.STYLESHEET_PARAMETER));
	assertEquals("wrong query type parameter found",
		Constants.SolrQueryType.typed.toString(), parameters
			.get(Constants.QT_PARAMETER));
	assertEquals("wrong query parameter found",
		"Saint-André France Admclass ", parameters
			.get(Constants.QUERY_PARAMETER));
    }
    
    @Test
    public void testToQueryStringShouldreturnCorrectParamsForSpellChecking() {
    	boolean savedSpellCheckingValue = SpellCheckerConfig.activeByDefault;
    	try {
    	Country france = GeolocTestHelper.createCountryForFrance();
	Country saved = countryDao.save(france);
	assertNotNull(saved);
	assertNotNull(saved.getId());
	SpellCheckerConfig.activeByDefault= true;
	SpellCheckerConfig.enabled = false;
	Pagination pagination = paginate().from(3).to(10);
	Output output = Output.withFormat(OutputFormat.ATOM).withLanguageCode(
		"FR").withStyle(OutputStyle.SHORT).withIndentation();
	FulltextQuery fulltextQuery = new FulltextQuery("Saint-André",
		pagination, output, Adm.class, "fr").withSpellChecking();
	// split parameters
	HashMap<String, String> parameters = GeolocTestHelper.splitURLParams(
		fulltextQuery.toQueryString(), "&");
	// check parameters
	assertTrue("the fulltextquery should have spellchecking enabled even if spellchecker is disabled", fulltextQuery.hasSpellChecking());
	assertTrue("spellchecker should not be listed if spellchecker is disabled", !parameters
		.containsKey(Constants.SPELLCHECKER_ENABLED_PARAMETER));
	//active spellchecker and re test
	SpellCheckerConfig.enabled = true;
	fulltextQuery = new FulltextQuery("Saint-André",
			pagination, output, Adm.class, "fr").withSpellChecking();
	parameters = GeolocTestHelper.splitURLParams(
			fulltextQuery.toQueryString(), "&");
	assertTrue("the fulltextquery should have spellchecking enabled when spellchecker is enabled", fulltextQuery.hasSpellChecking());
	assertEquals("spellchecker should be enabled", "true", parameters
			.get(Constants.SPELLCHECKER_ENABLED_PARAMETER));
	assertEquals("spellchecker should be enabled", String.valueOf(SpellCheckerConfig.collateResults), parameters
			.get(Constants.SPELLCHECKER_COLLATE_RESULTS_PARAMETER));
	assertEquals("spellchecker should be enabled",  String.valueOf(SpellCheckerConfig.numberOfSuggestion), parameters
			.get(Constants.SPELLCHECKER_NUMBER_OF_SUGGESTION_PARAMETER));
	assertEquals("spellchecker should be enabled", SpellCheckerConfig.spellcheckerDictionaryName.toString(), parameters
			.get(Constants.SPELLCHECKER_DICTIONARY_NAME_PARAMETER));
    	} catch (RuntimeException e) {
			SpellCheckerConfig.activeByDefault = savedSpellCheckingValue;
		}
    }
    
    @Test
    public void testQueryShouldHaveSpellcheckingCorrectDefaultValue(){
    	boolean savedSpellCheckingValue = SpellCheckerConfig.activeByDefault;
    	try {
			FulltextQuery query = new FulltextQuery("test");
			assertEquals(savedSpellCheckingValue, query.hasSpellChecking());
			SpellCheckerConfig.activeByDefault = ! SpellCheckerConfig.activeByDefault;
			query = new FulltextQuery("test2");
			assertEquals(SpellCheckerConfig.activeByDefault, query.hasSpellChecking());
		} catch (RuntimeException e) {
			SpellCheckerConfig.activeByDefault = savedSpellCheckingValue;
		}
    }

}
