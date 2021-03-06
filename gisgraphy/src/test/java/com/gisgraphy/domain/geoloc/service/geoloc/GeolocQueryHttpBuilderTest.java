package com.gisgraphy.domain.geoloc.service.geoloc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.GeolocServlet;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.gisgraphy.test.GeolocTestHelper;


public class GeolocQueryHttpBuilderTest {
	
	   @Test
	    public void testBuildFromAnHttpServletRequest() {
		Class<? extends GisFeature> savedDefaultType = GisgraphyConfig.defaultGeolocSearchPlaceTypeClass;
		try {
		    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = Country.class;
		    MockHttpServletRequest request = GeolocTestHelper
			    .createMockHttpServletRequestForGeoloc();
		    GeolocQuery query = buildQuery(request);
		    int firstPaginationIndex = 3;
		    assertEquals(firstPaginationIndex, query.getFirstPaginationIndex());
		    assertEquals(GeolocServlet.DEFAULT_MAX_RESULTS+firstPaginationIndex-1, query.getLastPaginationIndex());
		    assertEquals("the pagination should be limit to "
			    + GeolocServlet.DEFAULT_MAX_RESULTS,
			    GeolocServlet.DEFAULT_MAX_RESULTS, query
				    .getMaxNumberOfResults());
		    assertEquals(OutputFormat.XML, query.getOutputFormat());
		    assertEquals(null, query.getOutputLanguage());
		    assertEquals(OutputStyle.getDefault(), query.getOutputStyle());
		    assertEquals(City.class, query.getPlaceType());
		    Assert.assertEquals(new Double(1.0), query.getLatitude());
		    assertEquals(new Double(2.0), query.getLongitude());
		    assertEquals(10000D, query.getRadius(),0.001);

		    // test first pagination index
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GisgraphyServlet.FROM_PARAMETER);
		    query = buildQuery(request);
		    assertEquals("When no " + GisgraphyServlet.FROM_PARAMETER
			    + " is specified, the parameter should be "
			    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
			    .getFirstPaginationIndex());
		    // with a wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.FROM_PARAMETER, "-1");
		    query = buildQuery(request);
		    assertEquals("When a wrong " + GisgraphyServlet.FROM_PARAMETER
			    + " is specified, the parameter should be "
			    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
			    .getFirstPaginationIndex());
		    // with a non mumeric value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.FROM_PARAMETER, "a");
		    query = buildQuery(request);
		    assertEquals("When a wrong " + GisgraphyServlet.FROM_PARAMETER
			    + " is specified, the parameter should be "
			    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
			    .getFirstPaginationIndex());

		    // test last pagination index
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GisgraphyServlet.TO_PARAMETER);
		    query = buildQuery(request);
		    // non specify
		    int expectedLastPagination = (GeolocServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
	   	    assertEquals(
		           GisgraphyServlet.TO_PARAMETER
			    + " is wrong when no "+GisgraphyServlet.TO_PARAMETER+" is specified ",
			    expectedLastPagination, query
			    .getLastPaginationIndex());
		    assertEquals(
			    "When no "
				    + GisgraphyServlet.TO_PARAMETER
				    + " is specified, the  parameter should be set to limit results to "
				    + GeolocServlet.DEFAULT_MAX_RESULTS,
				    GeolocServlet.DEFAULT_MAX_RESULTS, query
				    .getMaxNumberOfResults());
		    // with a wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.TO_PARAMETER, "2");// to<from
		    query = buildQuery(request);
		    expectedLastPagination = (GeolocServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
		    assertEquals( GisgraphyServlet.TO_PARAMETER
			    + " is wrong when wrong "+GisgraphyServlet.TO_PARAMETER+" is specified ",
			    expectedLastPagination, query
				    .getLastPaginationIndex());
		    assertEquals("When a wrong " + GisgraphyServlet.TO_PARAMETER
			    + " is specified, the numberOf results should be "
			    + GeolocServlet.DEFAULT_MAX_RESULTS,
			    GeolocServlet.DEFAULT_MAX_RESULTS, query
				    .getMaxNumberOfResults());
		    assertEquals("a wrong to does not change the from value", 3, query
			    .getFirstPaginationIndex());
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    // non numeric
		    request.setParameter(GisgraphyServlet.TO_PARAMETER, "a");// to<from
		    query = buildQuery(request);
		    assertEquals("a wrong to does not change the from value", 3, query
			    .getFirstPaginationIndex());
		    expectedLastPagination = (GeolocServlet.DEFAULT_MAX_RESULTS+query.getFirstPaginationIndex()-1);
		    assertEquals( GisgraphyServlet.TO_PARAMETER
			    + " is wrong when non numeric "+GisgraphyServlet.TO_PARAMETER+" is specified ",
			    expectedLastPagination, query
				    .getLastPaginationIndex());
		    assertEquals("When a wrong " + GisgraphyServlet.TO_PARAMETER
			    + " is specified, the numberOf results should be "
			    + GeolocServlet.DEFAULT_MAX_RESULTS,
			    GeolocServlet.DEFAULT_MAX_RESULTS, query
				    .getMaxNumberOfResults());

		    // test indentation
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GisgraphyServlet.INDENT_PARAMETER);
		    query = buildQuery(request);
		    assertEquals("When no " + GeolocServlet.INDENT_PARAMETER
			    + " is specified, the  parameter should be set to default",Output.DEFAULT_INDENTATION,
			    query.isOutputIndented());
		    // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.INDENT_PARAMETER, "UNK");
		    query = buildQuery(request);
		    assertEquals("When wrong " + GeolocServlet.INDENT_PARAMETER
			    + " is specified, the  parameter should be set to default",Output.DEFAULT_INDENTATION,
			    query.isOutputIndented());
		    // test case sensitive
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.INDENT_PARAMETER, "tRue");
		    query = buildQuery(request);
		    assertTrue(GeolocServlet.INDENT_PARAMETER
			    + " should be case insensitive  ", query.isOutputIndented());
		    // test 'on' value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "oN");
		    query = buildQuery(request);
		    assertTrue(
			    GisgraphyServlet.INDENT_PARAMETER
				    + " should be true for 'on' value (case insensitive and on value)  ",
			    query.isOutputIndented());

		    // test outputFormat
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GisgraphyServlet.FORMAT_PARAMETER);
		    query = buildQuery(request);
		    assertEquals("When no " + GisgraphyServlet.FORMAT_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + OutputFormat.getDefault(), OutputFormat.getDefault(),
			    query.getOutputFormat());
		    // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "UNK");
		    query = buildQuery(request);
		    assertEquals("When wrong " + GisgraphyServlet.FORMAT_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + OutputFormat.getDefault(), OutputFormat.getDefault(),
			    query.getOutputFormat());
		    // test case sensitive
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "json");
		    query = buildQuery(request);
		    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
			    + " should be case insensitive  ", OutputFormat.JSON, query
			    .getOutputFormat());
		    // test with unsupported value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "unsupported");
		    query = buildQuery(request);
		    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
			    + " should set default if not supported  ", OutputFormat.getDefault(), query
			    .getOutputFormat());

		    // test placetype
		    // with no value specified
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GeolocServlet.PLACETYPE_PARAMETER);
		    query = buildQuery(request);
		    assertEquals(
			    "When no "
				    + GeolocServlet.PLACETYPE_PARAMETER
				    + " is specified, the  parameter should be set to defaultGeolocSearchPlaceType ",
			    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass, query
				    .getPlaceType());
		    // with wrong value
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.PLACETYPE_PARAMETER, "unk");
		    query = buildQuery(request);
		    assertEquals(
			    "When wrong "
				    + GeolocServlet.PLACETYPE_PARAMETER
				    + " is specified, the  parameter should be set to defaultGeolocSearchPlaceType ",
			    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass, query
				    .getPlaceType());
		    // test case sensitive
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "city");
		    query = buildQuery(request);
		    assertEquals(GeolocServlet.PLACETYPE_PARAMETER
			    + " should be case insensitive  ", City.class, query
			    .getPlaceType());

		    // test Point
		    // with missing lat
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GeolocServlet.LAT_PARAMETER);
		    try {
			query = buildQuery(request);
			fail("When there is no latitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // with empty lat
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LAT_PARAMETER, "");
		    try {
			query = buildQuery(request);
			fail("When there is empty latitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // With wrong lat
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LAT_PARAMETER, "a");
		    try {
			query = buildQuery(request);
			fail("A null lat should throw");
		    } catch (RuntimeException e) {
		    }
		    // With too small lat
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LAT_PARAMETER, "-92");
		    try {
			query = buildQuery(request);
			fail("latitude should not accept latitude < -90");
		    } catch (RuntimeException e) {
		    }

		    // With too high lat
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LAT_PARAMETER, "92");
		    try {
			query = buildQuery(request);
			fail("latitude should not accept latitude > 90");
		    } catch (RuntimeException e) {
		    }

		    // with missing long
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GeolocServlet.LONG_PARAMETER);
		    try {
			query = buildQuery(request);
			fail("When there is no latitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // with empty long
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LONG_PARAMETER, "");
		    try {
			query = buildQuery(request);
			fail("When there is empty longitude, query should throw");
		    } catch (RuntimeException e) {
		    }
		    // With wrong Long
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LONG_PARAMETER, "a");
		    try {
			query = buildQuery(request);
			fail("A null lat should throw");
		    } catch (RuntimeException e) {
		    }

		    // with too small long
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LONG_PARAMETER, "-182");
		    try {
			query = buildQuery(request);
			fail("longitude should not accept longitude < -180");
		    } catch (RuntimeException e) {
		    }

		    // with too high long
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LONG_PARAMETER, "182");
		    try {
			query = buildQuery(request);
			fail("longitude should not accept longitude > 180");
		    } catch (RuntimeException e) {
		    }

		    // with long with comma
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LONG_PARAMETER, "10,3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept longitude with comma", 10.3D,
				query.getLongitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // with long with point
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LONG_PARAMETER, "10.3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept longitude with comma", 10.3D,
				query.getLongitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // with lat with comma
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LAT_PARAMETER, "10,3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept latitude with comma", 10.3D,
				query.getLatitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // with lat with point
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.LAT_PARAMETER, "10.3");
		    try {
			query = buildQuery(request);
			Assert.assertEquals(
				"request should accept latitude with point", 10.3D,
				query.getLatitude().doubleValue(), 0.1);

		    } catch (RuntimeException e) {
		    }

		    // test radius
		    // with missing radius
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.removeParameter(GeolocServlet.RADIUS_PARAMETER);
		    query = buildQuery(request);
		    assertEquals("When no " + GeolocServlet.RADIUS_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
			    query.getRadius(), 0.1);
		    // With wrong radius
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "a");
		    query = buildQuery(request);
		    assertEquals("When wrong " + GeolocServlet.RADIUS_PARAMETER
			    + " is specified, the  parameter should be set to  "
			    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
			    query.getRadius(), 0.1);
		    // radius with comma
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "1,4");
		    query = buildQuery(request);
		    assertEquals("Radius should accept comma as decimal separator",
			    1.4D, query.getRadius(), 0.1);

		    // radius with point
		    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
		    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "1.4");
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

		} finally {
		    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = savedDefaultType;
		}
	    }

	private GeolocQuery buildQuery(MockHttpServletRequest request) {
		return GeolocQueryHttpBuilder.getInstance().buildFromHttpRequest(request);
	}

}
