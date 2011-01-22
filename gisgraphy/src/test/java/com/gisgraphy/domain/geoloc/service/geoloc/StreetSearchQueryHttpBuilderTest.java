package com.gisgraphy.domain.geoloc.service.geoloc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.GeolocServlet;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.gisgraphy.servlet.StreetServlet;
import com.gisgraphy.test.GeolocTestHelper;


public class StreetSearchQueryHttpBuilderTest {

	
	 @Test
	    public void streetSearchQueryFromAnHttpServletRequest() {
		    MockHttpServletRequest request = GeolocTestHelper
			    .createMockHttpServletRequestForStreetGeoloc();
		    StreetSearchQuery query = buildQuery(request);
		    int firstPaginationIndex =3;
		    assertEquals(firstPaginationIndex, query.getFirstPaginationIndex());
		    assertEquals(StreetServlet.DEFAULT_MAX_RESULTS+firstPaginationIndex-1, query.getLastPaginationIndex());
		    assertEquals("the pagination should be limit to "
			    + StreetServlet.DEFAULT_MAX_RESULTS,
			    StreetServlet.DEFAULT_MAX_RESULTS, query
				    .getMaxNumberOfResults());
		    assertEquals(OutputFormat.XML, query.getOutputFormat());
		    assertEquals(null, query.getOutputLanguage());
		    assertEquals(OutputStyle.getDefault(), query.getOutputStyle());
		    assertEquals(City.class, query.getPlaceType());
		    assertEquals(1.0, query.getLatitude(),0.1);
		    assertEquals(2.0, query.getLongitude(),0.1);
		    assertEquals(10000D, query.getRadius(),0.000001);

		    // test first pagination index
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(GisgraphyServlet.FROM_PARAMETER);
		    query = buildQuery(request);
		    assertEquals("When no " + GisgraphyServlet.FROM_PARAMETER
			    + " is specified, the parameter should be "
			    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
			    .getFirstPaginationIndex());
		    // with a wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.FROM_PARAMETER, "-1");
		    query = buildQuery(request);
		    assertEquals("When a wrong " + GisgraphyServlet.FROM_PARAMETER
			    + " is specified, the parameter should be "
			    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
			    .getFirstPaginationIndex());
		    // with a non mumeric value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.FROM_PARAMETER, "a");
		    query = buildQuery(request);
		    assertEquals("When a wrong " + GisgraphyServlet.FROM_PARAMETER
			    + " is specified, the parameter should be "
			    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
			    .getFirstPaginationIndex());

		    // test last pagination index
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(GisgraphyServlet.TO_PARAMETER);
		    query = buildQuery(request);
		    // non specify
		    int expectedLastPagination = (StreetServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
		   	    assertEquals(
			           GisgraphyServlet.TO_PARAMETER
				    + " is wrong when no "+GisgraphyServlet.TO_PARAMETER+" is specified ",
				    expectedLastPagination, query
				    .getLastPaginationIndex());
		   	    
		   	 assertEquals("When no "
				    + GisgraphyServlet.TO_PARAMETER
				    + " is specified, the maxnumberOfResults should not be > "
				    + StreetServlet.DEFAULT_MAX_RESULTS,
				    StreetServlet.DEFAULT_MAX_RESULTS, query
					    .getMaxNumberOfResults());
		    // with a wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.TO_PARAMETER, "2");// to<from
		    query = buildQuery(request);
		    expectedLastPagination = (StreetServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
		    assertEquals( GisgraphyServlet.TO_PARAMETER
			    + " is wrong when wrong "+GisgraphyServlet.TO_PARAMETER+" is specified ",
			    expectedLastPagination, query
				    .getLastPaginationIndex());
		    assertEquals("When a wrong " + GisgraphyServlet.TO_PARAMETER
			    + " is specified, the maxnumberOfResults should not be > "
			    + StreetServlet.DEFAULT_MAX_RESULTS,
			    StreetServlet.DEFAULT_MAX_RESULTS, query
				    .getMaxNumberOfResults());
		    
		    assertEquals("a wrong to does not change the from value", 3, query
			    .getFirstPaginationIndex());
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    // non numeric
		    request.setParameter(GisgraphyServlet.TO_PARAMETER, "a");// to<from
		    query = buildQuery(request);
		    assertEquals("a wrong to does not change the from value", 3, query
			    .getFirstPaginationIndex());
		    expectedLastPagination = (StreetServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
		    assertEquals( GisgraphyServlet.TO_PARAMETER
			    + " is wrong when non numeric "+GisgraphyServlet.TO_PARAMETER+" is specified ",
			    expectedLastPagination, query
				    .getLastPaginationIndex());
		    assertEquals("When a wrong " + GisgraphyServlet.TO_PARAMETER
			    + " is specified, the maxnumberOfResults should not be > "
			    + StreetServlet.DEFAULT_MAX_RESULTS,
			    StreetServlet.DEFAULT_MAX_RESULTS, query
				    .getMaxNumberOfResults());

		    // test indentation
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(GisgraphyServlet.INDENT_PARAMETER);
		    query = buildQuery(request);
		    assertFalse("When no " + GisgraphyServlet.INDENT_PARAMETER
			    + " is specified, the  parameter should be set to false",
			    query.isOutputIndented());
		    // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "UNK");
		    query = buildQuery(request);
		    assertFalse("When wrong " + GisgraphyServlet.INDENT_PARAMETER
			    + " is specified, the  parameter should be set to false",
			    query.isOutputIndented());
		    // test case sensitive
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "true");
		    query = buildQuery(request);
		    assertTrue(GisgraphyServlet.INDENT_PARAMETER
			    + " should be case insensitive  ", query.isOutputIndented());
		    // test 'on' value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "oN");
		    query = buildQuery(request);
		    assertTrue(
			    GisgraphyServlet.INDENT_PARAMETER
				    + " should be true for 'on' value (case insensitive and on value)  ",
			    query.isOutputIndented());

		    // test outputFormat
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(GisgraphyServlet.FORMAT_PARAMETER);
		    query = buildQuery(request);
		    assertEquals("When no " + GisgraphyServlet.FORMAT_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + OutputFormat.getDefault(), OutputFormat.getDefault(),
			    query.getOutputFormat());
		    // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "UNK");
		    query = buildQuery(request);
		    assertEquals("When wrong " + GisgraphyServlet.FORMAT_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + OutputFormat.getDefault(), OutputFormat.getDefault(),
			    query.getOutputFormat());
		    // test case sensitive
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "json");
		    query = buildQuery(request);
		    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
			    + " should be case insensitive  ", OutputFormat.JSON, query
			    .getOutputFormat());
		    // test streeSearchMode
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(StreetServlet.STREET_SEARCH_MODE_PARAMETER);
		    query = buildQuery(request);
		    assertEquals(
			    "When no "
				    + StreetServlet.STREET_SEARCH_MODE_PARAMETER
				    + " is specified, the  parameter should be set to defaultValue",StreetSearchMode.getDefault(), query
				    .getStreetSearchMode());
		   // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.STREET_SEARCH_MODE_PARAMETER, "unk");
		    query = buildQuery(request);
		    assertEquals(
			    "When wrong "
				    + StreetServlet.STREET_SEARCH_MODE_PARAMETER
				    + " is specified, the  parameter should be set to the default value ",
			    StreetSearchMode.getDefault(),query
				    .getStreetSearchMode());
		    //test with good value 
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.STREET_SEARCH_MODE_PARAMETER, StreetSearchMode.CONTAINS.toString());
		    query = buildQuery(request);
		    assertEquals(
			    "When good "
				    + StreetServlet.STREET_SEARCH_MODE_PARAMETER
				    + " is specified, the  parameter should be set to the Equivalent streetSearchMode ",StreetSearchMode.CONTAINS,
			    query
				    .getStreetSearchMode());
		  //test case sensitivity
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.STREET_SEARCH_MODE_PARAMETER, StreetSearchMode.CONTAINS.toString().toLowerCase());
		    query = buildQuery(request);
		    assertEquals(
				     StreetServlet.STREET_SEARCH_MODE_PARAMETER
				    + " should be case sensitive ",StreetSearchMode.CONTAINS,
			    query
				    .getStreetSearchMode());
		    // test streettype
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(StreetServlet.STREETTYPE_PARAMETER);
		    query = buildQuery(request);
		    assertNull(
			    "When no "
				    + StreetServlet.STREETTYPE_PARAMETER
				    + " is specified, the  parameter should be set to null", query
				    .getStreetType());
		   // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.STREETTYPE_PARAMETER, "unk");
		    query = buildQuery(request);
		    assertNull(
			    "When wrong "
				    + StreetServlet.STREETTYPE_PARAMETER
				    + " is specified, the  parameter should be set to null ",
			    query
				    .getStreetType());
		    //test with good value 
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.STREETTYPE_PARAMETER, StreetType.BRIDLEWAY.toString());
		    query = buildQuery(request);
		    assertEquals(
			    "When good "
				    + StreetServlet.STREETTYPE_PARAMETER
				    + " is specified, the  parameter should be set to the Equivalent streettype ",StreetType.BRIDLEWAY,
			    query
				    .getStreetType());
		  //test case sensitivity
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.STREETTYPE_PARAMETER, StreetType.BRIDLEWAY.toString().toLowerCase());
		    query = buildQuery(request);
		    assertEquals(
				     StreetServlet.STREETTYPE_PARAMETER
				    + " should be case sensitive ",StreetType.BRIDLEWAY,
			    query
				    .getStreetType());
		    
		    // test oneWay
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(StreetServlet.ONEWAY_PARAMETER);
		    query = buildQuery(request);
		    assertNull("When no " + StreetServlet.ONEWAY_PARAMETER
			    + " is specified, the  parameter should be set to null",
			    query.getOneWay());
		    // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.ONEWAY_PARAMETER, "UNK");
		    query = buildQuery(request);
		    assertNull("When wrong " + StreetServlet.ONEWAY_PARAMETER
			    + " is specified, the  parameter should be set to false",
			    query.getOneWay());
		    // test case sensitive
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.ONEWAY_PARAMETER, "True");
		    query = buildQuery(request);
		    assertTrue(StreetServlet.ONEWAY_PARAMETER
			    + " should be case insensitive for true ", query.getOneWay());
		    
		 // test With false
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.ONEWAY_PARAMETER, "FaLse");
		    query = buildQuery(request);
		    assertFalse(StreetServlet.ONEWAY_PARAMETER
			    + " should be case insensitive for false  ", query.getOneWay());
		    // test 'on' value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.ONEWAY_PARAMETER, "oN");
		    query = buildQuery(request);
		    assertTrue(
			    StreetServlet.ONEWAY_PARAMETER
				    + " should be true for 'on' value (case insensitive and on value)  ",
				    query.getOneWay());
		    
		    
		    //name
		    //test With good value
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.NAME_PARAMETER, "prefix");
		    query = buildQuery(request);
		    assertEquals(StreetServlet.NAME_PARAMETER+" should be set when specified",request.getParameter(StreetServlet.NAME_PARAMETER), query.getName());
			   
			// empty string
			request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
			request.setParameter(StreetServlet.NAME_PARAMETER, " ");
			query = buildQuery(request);
			assertNull(StreetServlet.NAME_PARAMETER+" should be null when an empty String is specified", query.getName());
			
			// too long string
			request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
			request.setParameter(StreetServlet.NAME_PARAMETER, RandomStringUtils
				.random(StreetSearchQuery.NAME_MAX_LENGTH) + 1);
			try {
			    query = buildQuery(request);
			    fail("Name Prefix must have a maximmum length of "
				    + StreetSearchQuery.NAME_MAX_LENGTH);
			} catch (StreetSearchException e) {
			}
		    
		    
		    // test Point
		    // with missing lat
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(StreetServlet.LAT_PARAMETER);
		    try {
			query = buildQuery(request);
			fail("When there is no latitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // with empty lat
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LAT_PARAMETER, "");
		    try {
			query = buildQuery(request);
			fail("When there is empty latitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // With wrong lat
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LAT_PARAMETER, "a");
		    try {
			query = buildQuery(request);
			fail("A null lat should throw");
		    } catch (RuntimeException e) {
		    }
		    // With too small lat
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LAT_PARAMETER, "-92");
		    try {
			query = buildQuery(request);
			fail("latitude should not accept latitude < -90");
		    } catch (RuntimeException e) {
		    }

		    // With too high lat
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LAT_PARAMETER, "92");
		    try {
			query = buildQuery(request);
			fail("latitude should not accept latitude > 90");
		    } catch (RuntimeException e) {
		    }

		    // with missing long
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(StreetServlet.LONG_PARAMETER);
		    try {
			query = buildQuery(request);
			fail("When there is no latitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // with empty long
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LONG_PARAMETER, "");
		    try {
			query = buildQuery(request);
			fail("When there is empty longitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // With wrong Long
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LONG_PARAMETER, "a");
		    try {
			query = buildQuery(request);
			fail("A null lat should throw");
		    } catch (RuntimeException e) {
		    }

		    // with too small long
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LONG_PARAMETER, "-182");
		    try {
			query = buildQuery(request);
			fail("longitude should not accept longitude < -180");
		    } catch (RuntimeException e) {
		    }

		    // with too high long
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LONG_PARAMETER, "182");
		    try {
			query = buildQuery(request);
			fail("longitude should not accept longitude > 180");
		    } catch (RuntimeException e) {
		    }

		    // with long with comma
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LONG_PARAMETER, "10,3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept longitude with comma", 10.3D,
				query.getLongitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // with long with point
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LONG_PARAMETER, "10.3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept longitude with comma", 10.3D,
				query.getLongitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // with lat with comma
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LAT_PARAMETER, "10,3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept latitude with comma", 10.3D,
				query.getLatitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // with lat with point
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.LAT_PARAMETER, "10.3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept latitude with point", 10.3D,
				query.getLatitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // test radius
		    // with missing radius
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.removeParameter(StreetServlet.RADIUS_PARAMETER);
		    query = buildQuery(request);
		    assertEquals("When no " + StreetServlet.RADIUS_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
			    query.getRadius(), 0.1);
		    // With wrong radius
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.RADIUS_PARAMETER, "a");
		    query = buildQuery(request);
		    assertEquals("When wrong " + StreetServlet.RADIUS_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
			    query.getRadius(), 0.1);
		    // radius with comma
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.RADIUS_PARAMETER, "1,4");
		    query = buildQuery(request);
		    assertEquals("Radius should accept comma as decimal separator",
			    1.4D, query.getRadius(), 0.1);

		    // radius with point
		    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
		    request.setParameter(StreetServlet.RADIUS_PARAMETER, "1.4");
		    query = buildQuery(request);
		    assertEquals("Radius should accept point as decimal separator",
			    1.4D, query.getRadius(), 0.1);
		    
		    
		 // distanceField default value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    query = buildQuery(request);
		    assertTrue("By default distanceField should be true",
			     query.hasDistanceField());
		    
		 // distanceField case insensitive
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.DISTANCE_PARAMETER, "falSE");
		    query = buildQuery(request);
		    assertFalse("distanceField should be set when specified",
			     query.hasDistanceField());
		    
		    // distanceField with off value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.DISTANCE_PARAMETER, "oFF");
		    query = buildQuery(request);
		    assertFalse("distanceField should take off value into account",
				     query.hasDistanceField());
		    
		    // distanceField with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.DISTANCE_PARAMETER, "wrong value");
		    query = buildQuery(request);
		    assertTrue("distanceField should be kept to his default value if specified with wrong value",
				     query.hasDistanceField());
		    
		    //callback not set
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    query = buildQuery(request);
		    assertNull("callback should be null when not set",
			     query.getCallback());
		    
		    //callback set with non alpha value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.CALLBACK_PARAMETER, "doit(");
		    query = buildQuery(request);
		    assertNull("callback should not be set when not alphanumeric",
			     query.getCallback());
		    
		    //callback set with alpha value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.CALLBACK_PARAMETER, "doit");
		    query = buildQuery(request);
		    assertEquals("callback should not be set when alphanumeric",
			     "doit",query.getCallback());


	    }

	private StreetSearchQuery buildQuery(MockHttpServletRequest request) {
		return StreetSearchQueryHttpBuilder.getInstance().buildFromHttpRequest(request);
	}
}
