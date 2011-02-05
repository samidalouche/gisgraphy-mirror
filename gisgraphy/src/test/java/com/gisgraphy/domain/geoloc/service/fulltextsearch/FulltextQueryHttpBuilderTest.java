package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.SpellCheckerConfig;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.FulltextServlet;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.gisgraphy.test.GeolocTestHelper;


public class FulltextQueryHttpBuilderTest {

    @Test
    public void testFulltextQueryFromAnHttpServletRequest() {
	MockHttpServletRequest request = GeolocTestHelper
		.createMockHttpServletRequestForFullText();
	FulltextQuery query = buildQuery(request);
	int firstPaginationIndex = 3;
	assertEquals(firstPaginationIndex, query.getFirstPaginationIndex());
	assertEquals(FulltextServlet.DEFAULT_MAX_RESULTS+firstPaginationIndex-1, query.getLastPaginationIndex());
	assertEquals("the pagination should be limit to "
		+ FulltextServlet.DEFAULT_MAX_RESULTS,
		FulltextServlet.DEFAULT_MAX_RESULTS, query
			.getMaxNumberOfResults());
	assertEquals("FR", query.getCountryCode());
	assertEquals(OutputFormat.XML, query.getOutputFormat());
	assertEquals("FR", query.getOutputLanguage());
	assertEquals(OutputStyle.FULL, query.getOutputStyle());
	assertEquals(City.class, query.getPlaceType()[0]);
	assertEquals("query", query.getQuery());
	
	//test trim
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FROM_PARAMETER, " "+request.getParameter(FulltextServlet.QUERY_PARAMETER)+" ");
	query = buildQuery(request);
	Assert.assertTrue("query parameter shoud be trimed",!query.getQuery().endsWith(" "));
	Assert.assertTrue("query parameter shoud be trimed",!query.getQuery().startsWith(" "));

	// test first pagination index
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.FROM_PARAMETER);
	query = buildQuery(request);
	assertEquals("When no " + FulltextServlet.FROM_PARAMETER
		+ " is specified, the parameter should be "
		+ Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		.getFirstPaginationIndex());
	// with a wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FROM_PARAMETER, "-1");
	query = buildQuery(request);
	assertEquals("When a wrong " + FulltextServlet.FROM_PARAMETER
		+ " is specified, the parameter should be "
		+ Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		.getFirstPaginationIndex());
	// with a non mumeric value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FROM_PARAMETER, "a");
	query = buildQuery(request);
	assertEquals("When a wrong " + FulltextServlet.FROM_PARAMETER
		+ " is specified, the parameter should be "
		+ Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		.getFirstPaginationIndex());

	// test last pagination index
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.TO_PARAMETER);
	query = buildQuery(request);
	 int expectedLastPagination = (FulltextServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
	    assertEquals(
	           GisgraphyServlet.TO_PARAMETER
		    + " is wrong when no "+GisgraphyServlet.TO_PARAMETER+" is specified ",
		    expectedLastPagination, query
		    .getLastPaginationIndex());
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
	query = buildQuery(request);
	 expectedLastPagination = (FulltextServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
	    assertEquals( GisgraphyServlet.TO_PARAMETER
		    + " is wrong when wrong "+GisgraphyServlet.TO_PARAMETER+" is specified ",
		    expectedLastPagination, query
			    .getLastPaginationIndex());
	assertEquals("When a wrong " + FulltextServlet.TO_PARAMETER
		+ " is specified, the number of results should be "
		+ FulltextServlet.DEFAULT_MAX_RESULTS,
		FulltextServlet.DEFAULT_MAX_RESULTS, query.getMaxNumberOfResults());
	assertEquals("a wrong to does not change the from value", 3, query
		.getFirstPaginationIndex());
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	//non numeric
	request.setParameter(FulltextServlet.TO_PARAMETER, "a");
	query = buildQuery(request);
	expectedLastPagination = (FulltextServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
	assertEquals( GisgraphyServlet.TO_PARAMETER
		    + " is wrong when non numeric "+GisgraphyServlet.TO_PARAMETER+" is specified ",
		    expectedLastPagination, query
			    .getLastPaginationIndex());
	assertEquals("a wrong to does not change the from value", 3, query
		.getFirstPaginationIndex());
	assertEquals("When a wrong " + FulltextServlet.TO_PARAMETER
		+ " is specified, the numberOf results should be "
		+ FulltextServlet.DEFAULT_MAX_RESULTS,
		FulltextServlet.DEFAULT_MAX_RESULTS, query.getMaxNumberOfResults());

	// test countrycode
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.COUNTRY_PARAMETER);
	query = buildQuery(request);
	Assert.assertNull("When no " + FulltextServlet.COUNTRY_PARAMETER
		+ " is specified, the parameter should be set to null", query
		.getCountryCode());
	// with a wrong value
	// can not have a wrong value=>always a string

	// test outputFormat
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.FORMAT_PARAMETER);
	query = buildQuery(request);
	assertEquals("When no " + FulltextServlet.FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getOutputFormat());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FORMAT_PARAMETER, "UNK");
	query = buildQuery(request);
	assertEquals("When wrong " + FulltextServlet.FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getOutputFormat());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.FORMAT_PARAMETER, "json");
	query = buildQuery(request);
	assertEquals(FulltextServlet.FORMAT_PARAMETER
		+ " should be case insensitive  ", OutputFormat.JSON, query
		.getOutputFormat());
	//with unsupported value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "unsupported");
    query = buildQuery(request);
    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
	    + " should set default if not supported  ", OutputFormat.getDefault(), query
	    .getOutputFormat());
	// test language
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.LANG_PARAMETER);
	query = buildQuery(request);
	assertNull(FulltextServlet.LANG_PARAMETER
		+ " should be null when not specified  ", query
		.getOutputLanguage());
	// with empty string
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.LANG_PARAMETER, " ");
	query = buildQuery(request);
	assertEquals(FulltextServlet.LANG_PARAMETER
		+ " should be null when not specified  ",
		Output.DEFAULT_LANGUAGE_CODE, query.getOutputLanguage());

	// test uppercase
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.LANG_PARAMETER, "fr");
	query = buildQuery(request);
	assertEquals(FulltextServlet.LANG_PARAMETER + " should be uppercase  ",
		"FR", query.getOutputLanguage());

	// test placetype
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.PLACETYPE_PARAMETER);
	query = buildQuery(request);
	assertNull("When no " + FulltextServlet.PLACETYPE_PARAMETER
		+ " is specified, the  parameter should be set null ", query
		.getPlaceType());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.PLACETYPE_PARAMETER);
	request.setParameter(FulltextServlet.PLACETYPE_PARAMETER, "unk");
	query = buildQuery(request);
	assertNull("When wrong " + FulltextServlet.PLACETYPE_PARAMETER
		+ " is specified, the  parameter should be set null ", query
		.getPlaceType()[0]);
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.PLACETYPE_PARAMETER, "ciTy");
	query = buildQuery(request);
	assertEquals(FulltextServlet.PLACETYPE_PARAMETER
		+ " should be case insensitive  ", City.class, query
		.getPlaceType()[0]);
	
	// test with multipleplacetype
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.PLACETYPE_PARAMETER, new String[]{"city","adm"});
	query = buildQuery(request);
	assertEquals(FulltextServlet.PLACETYPE_PARAMETER
		+ " should accept several placetype  ",2, query
		.getPlaceType().length);
	List<Class<? extends GisFeature>> placetypeList = Arrays.asList(query.getPlaceType());

	assertTrue(FulltextServlet.PLACETYPE_PARAMETER
			+ " should accept several placetype  ", placetypeList.contains(City.class));
	assertTrue(FulltextServlet.PLACETYPE_PARAMETER
			+ " should accept several placetype  ", placetypeList.contains(Adm.class));
	
	// test with multipleplacetype with wrong values
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.PLACETYPE_PARAMETER, new String[]{"city","unk"});
	query = buildQuery(request);
	assertEquals(FulltextServlet.PLACETYPE_PARAMETER
		+ " should accept several placetype  ",2, query
		.getPlaceType().length);
	placetypeList = Arrays.asList(query.getPlaceType());

	assertTrue(FulltextServlet.PLACETYPE_PARAMETER
			+ " should accept several placetype  ", placetypeList.contains(City.class));
	assertTrue(FulltextServlet.PLACETYPE_PARAMETER
			+ " should accept several placetype  ", placetypeList.contains(null));

	// test output style
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.STYLE_PARAMETER);
	query = buildQuery(request);
	assertEquals("When no " + FulltextServlet.STYLE_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputStyle.getDefault(), OutputStyle.getDefault(), query
		.getOutputStyle());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.STYLE_PARAMETER, "UNK");
	query = buildQuery(request);
	assertEquals("When wrong " + FulltextServlet.STYLE_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputStyle.getDefault(), OutputStyle.getDefault(), query
		.getOutputStyle());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.STYLE_PARAMETER, "medium");
	query = buildQuery(request);
	assertEquals(FulltextServlet.STYLE_PARAMETER
		+ " should be case insensitive  ", OutputStyle.MEDIUM, query
		.getOutputStyle());

	// test indentation
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(GisgraphyServlet.INDENT_PARAMETER);
	query = buildQuery(request);
	assertEquals("When no " + GisgraphyServlet.INDENT_PARAMETER
		+ " is specified, the  parameter should be set to default", Output.DEFAULT_INDENTATION
		,query.isOutputIndented());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "UNK");
	query = buildQuery(request);
	assertEquals("When wrong " + GisgraphyServlet.INDENT_PARAMETER
		+ " is specified, the  parameter should be set to default",Output.DEFAULT_INDENTATION,
		query.isOutputIndented());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "True");
	query = buildQuery(request);
	assertTrue(GisgraphyServlet.INDENT_PARAMETER
		+ " should be case insensitive  ", query.isOutputIndented());
	// test with on value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "oN");
	query = buildQuery(request);
	assertTrue(
		GisgraphyServlet.INDENT_PARAMETER
			+ " should be true for 'on' value (case insensitive and on value)  ",
		query.isOutputIndented());
	
	
	// test spellchecking
	// with no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.SPELLCHECKING_PARAMETER);
	query = buildQuery(request);
	assertEquals("When no " + FulltextServlet.SPELLCHECKING_PARAMETER
		+ " is specified, the  parameter should be the default one",SpellCheckerConfig.activeByDefault, query
		.hasSpellChecking());
	// with wrong value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.SPELLCHECKING_PARAMETER, "UNK");
	query = buildQuery(request);
	assertEquals("When wrong " + FulltextServlet.SPELLCHECKING_PARAMETER
		+ " is specified, the  parameter should be set to the default one",SpellCheckerConfig.activeByDefault, query
		.hasSpellChecking());
	// test case sensitive
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.SPELLCHECKING_PARAMETER, String.valueOf(!SpellCheckerConfig.activeByDefault).toUpperCase());
	query = buildQuery(request);
	assertEquals(FulltextServlet.SPELLCHECKING_PARAMETER
		+ " should be case insensitive  ", !SpellCheckerConfig.activeByDefault, query.hasSpellChecking());
	// test with on value
	
	boolean savedSpellCheckingValue = SpellCheckerConfig.activeByDefault;
	try {
		SpellCheckerConfig.activeByDefault = false;
		request = GeolocTestHelper.createMockHttpServletRequestForFullText();
		request.setParameter(FulltextServlet.SPELLCHECKING_PARAMETER, "oN");
		query = buildQuery(request);
		assertTrue(
			FulltextServlet.SPELLCHECKING_PARAMETER
				+ " should be true for 'on' value (case insensitive and on value)  ",
			query.hasSpellChecking());
	} catch (RuntimeException e) {
	    Assert.fail(e.getMessage());
	} finally {
	    //reset the last value
		SpellCheckerConfig.activeByDefault = savedSpellCheckingValue;
	}
	

	// test query
	//test with good value
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	query = buildQuery(request);
	assertEquals("query should be set when specified",request.getParameter(FulltextServlet.QUERY_PARAMETER), query.getQuery());
	
	
	// With no value specified
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.removeParameter(FulltextServlet.QUERY_PARAMETER);
	try {
	    query = buildQuery(request);
	    fail("A null query should throw");
	} catch (RuntimeException e) {
	}
	// empty string
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.QUERY_PARAMETER, " ");
	try {
	    query = buildQuery(request);
	    fail("An empty query should throw");
	} catch (RuntimeException e) {
	}
	// too long string
	request = GeolocTestHelper.createMockHttpServletRequestForFullText();
	request.setParameter(FulltextServlet.QUERY_PARAMETER, RandomStringUtils
		.random(FulltextQuery.QUERY_MAX_LENGTH) + 1);
	try {
	    query = buildQuery(request);
	    fail("query must have a maximmum length of "
		    + FulltextQuery.QUERY_MAX_LENGTH);
	} catch (RuntimeException e) {
	}

    }

	private FulltextQuery buildQuery(MockHttpServletRequest request) {
		return FulltextQueryHttpBuilder.getInstance().buildFromRequest(request);
	}
	
}
