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
package com.gisgraphy.domain.geoloc.service.geoloc;

import static com.gisgraphy.domain.valueobject.Pagination.paginate;
import static org.junit.Assert.assertTrue;
import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.servlet.GeolocServlet;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.gisgraphy.test.GeolocTestHelper;
import com.vividsolutions.jts.geom.Point;

public class GeolocQueryTest extends TestCase {

    /**
     * a simple point to avoid creation of new one for each test
     */
    private static Point GENERIC_POINT = GeolocHelper.createPoint(3.2F, 2.5F);

    @Test
    public void testGeolocQueryPointRadiusPaginationOutputClassOfQextendsGisFeature() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	GeolocQuery query = new GeolocQuery(GENERIC_POINT, 3D, pagination,
		output, Adm.class);
	assertEquals(pagination, query.getPagination());
	assertEquals(output, query.getOutput());
	assertEquals(Adm.class, query.getPlaceType());
	assertEquals(GENERIC_POINT, query.getPoint());
	assertTrue(query.isOutputIndented());
	assertEquals(3D, query.getRadius());
    }

    public void testGeolocQueryPointRadius() {
	Class<? extends GisFeature> savedDefaultType = GisgraphyConfig.defaultGeolocSearchPlaceTypeClass;
	try {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = Country.class;
	    GeolocQuery query = new GeolocQuery(GENERIC_POINT, 3D);
	    assertEquals(Pagination.DEFAULT_PAGINATION, query.getPagination());
	    assertEquals(Output.DEFAULT_OUTPUT, query.getOutput());
	    assertEquals(GisgraphyConfig.defaultGeolocSearchPlaceTypeClass,
		    query.getPlaceType());
	    assertEquals(GENERIC_POINT, query.getPoint());
	    assertEquals(3D, query.getRadius());
	} finally {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = savedDefaultType;
	}
    }

    public void testGeolocQueryPoint() {
	Class<? extends GisFeature> savedDefaultType = GisgraphyConfig.defaultGeolocSearchPlaceTypeClass;
	try {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = Country.class;
	    GeolocQuery query = new GeolocQuery(GENERIC_POINT);
	    assertEquals(Pagination.DEFAULT_PAGINATION, query.getPagination());
	    assertEquals(Output.DEFAULT_OUTPUT, query.getOutput());
	    assertEquals(GisgraphyConfig.defaultGeolocSearchPlaceTypeClass,
		    query.getPlaceType());
	    assertEquals(GENERIC_POINT, query.getPoint());
	    assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius());
	} finally {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = savedDefaultType;
	}
    }

    @Test
    public void testGeolocQueryFromAnHttpServletRequest() {
	Class<? extends GisFeature> savedDefaultType = GisgraphyConfig.defaultGeolocSearchPlaceTypeClass;
	try {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = Country.class;
	    MockHttpServletRequest request = GeolocTestHelper
		    .createMockHttpServletRequestForGeoloc();
	    GeolocQuery query = new GeolocQuery(request);
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
	    assertEquals(1.0, query.getLatitude());
	    assertEquals(2.0, query.getLongitude());
	    assertEquals(10000D, query.getRadius());

	    // test first pagination index
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.removeParameter(GisgraphyServlet.FROM_PARAMETER);
	    query = new GeolocQuery(request);
	    assertEquals("When no " + GisgraphyServlet.FROM_PARAMETER
		    + " is specified, the parameter should be "
		    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		    .getFirstPaginationIndex());
	    // with a wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GisgraphyServlet.FROM_PARAMETER, "-1");
	    query = new GeolocQuery(request);
	    assertEquals("When a wrong " + GisgraphyServlet.FROM_PARAMETER
		    + " is specified, the parameter should be "
		    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		    .getFirstPaginationIndex());
	    // with a non mumeric value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GisgraphyServlet.FROM_PARAMETER, "a");
	    query = new GeolocQuery(request);
	    assertEquals("When a wrong " + GisgraphyServlet.FROM_PARAMETER
		    + " is specified, the parameter should be "
		    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		    .getFirstPaginationIndex());

	    // test last pagination index
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.removeParameter(GisgraphyServlet.TO_PARAMETER);
	    query = new GeolocQuery(request);
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
	    query = new GeolocQuery(request);
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
	    query = new GeolocQuery(request);
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
	    query = new GeolocQuery(request);
	    assertFalse("When no " + GeolocServlet.INDENT_PARAMETER
		    + " is specified, the  parameter should be set to false",
		    query.isOutputIndented());
	    // with wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.INDENT_PARAMETER, "UNK");
	    query = new GeolocQuery(request);
	    assertFalse("When wrong " + GeolocServlet.INDENT_PARAMETER
		    + " is specified, the  parameter should be set to false",
		    query.isOutputIndented());
	    // test case sensitive
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.INDENT_PARAMETER, "tRue");
	    query = new GeolocQuery(request);
	    assertTrue(GeolocServlet.INDENT_PARAMETER
		    + " should be case insensitive  ", query.isOutputIndented());
	    // test 'on' value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GisgraphyServlet.INDENT_PARAMETER, "oN");
	    query = new GeolocQuery(request);
	    assertTrue(
		    GisgraphyServlet.INDENT_PARAMETER
			    + " should be true for 'on' value (case insensitive and on value)  ",
		    query.isOutputIndented());

	    // test outputFormat
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.removeParameter(GisgraphyServlet.FORMAT_PARAMETER);
	    query = new GeolocQuery(request);
	    assertEquals("When no " + GisgraphyServlet.FORMAT_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + OutputFormat.getDefault(), OutputFormat.getDefault(),
		    query.getOutputFormat());
	    // with wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "UNK");
	    query = new GeolocQuery(request);
	    assertEquals("When wrong " + GisgraphyServlet.FORMAT_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + OutputFormat.getDefault(), OutputFormat.getDefault(),
		    query.getOutputFormat());
	    // test case sensitive
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "json");
	    query = new GeolocQuery(request);
	    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
		    + " should be case insensitive  ", OutputFormat.JSON, query
		    .getOutputFormat());

	    // test placetype
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.removeParameter(GeolocServlet.PLACETYPE_PARAMETER);
	    query = new GeolocQuery(request);
	    assertEquals(
		    "When no "
			    + GeolocServlet.PLACETYPE_PARAMETER
			    + " is specified, the  parameter should be set to defaultGeolocSearchPlaceType ",
		    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass, query
			    .getPlaceType());
	    // with wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.PLACETYPE_PARAMETER, "unk");
	    query = new GeolocQuery(request);
	    assertEquals(
		    "When wrong "
			    + GeolocServlet.PLACETYPE_PARAMETER
			    + " is specified, the  parameter should be set to defaultGeolocSearchPlaceType ",
		    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass, query
			    .getPlaceType());
	    // test case sensitive
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GisgraphyServlet.FORMAT_PARAMETER, "city");
	    query = new GeolocQuery(request);
	    assertEquals(GeolocServlet.PLACETYPE_PARAMETER
		    + " should be case insensitive  ", City.class, query
		    .getPlaceType());

	    // test Point
	    // with missing lat
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.removeParameter(GeolocServlet.LAT_PARAMETER);
	    try {
		query = new GeolocQuery(request);
		fail("When there is no latitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // with empty lat
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "");
	    try {
		query = new GeolocQuery(request);
		fail("When there is empty latitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // With wrong lat
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "a");
	    try {
		query = new GeolocQuery(request);
		fail("A null lat should throw");
	    } catch (RuntimeException e) {
	    }
	    // With too small lat
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "-92");
	    try {
		query = new GeolocQuery(request);
		fail("latitude should not accept latitude < -90");
	    } catch (RuntimeException e) {
	    }

	    // With too high lat
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "92");
	    try {
		query = new GeolocQuery(request);
		fail("latitude should not accept latitude > 90");
	    } catch (RuntimeException e) {
	    }

	    // with missing long
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.removeParameter(GeolocServlet.LONG_PARAMETER);
	    try {
		query = new GeolocQuery(request);
		fail("When there is no latitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // with empty long
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "");
	    try {
		query = new GeolocQuery(request);
		fail("When there is empty longitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // With wrong Long
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "a");
	    try {
		query = new GeolocQuery(request);
		fail("A null lat should throw");
	    } catch (RuntimeException e) {
	    }

	    // with too small long
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "-182");
	    try {
		query = new GeolocQuery(request);
		fail("longitude should not accept longitude < -180");
	    } catch (RuntimeException e) {
	    }

	    // with too high long
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "182");
	    try {
		query = new GeolocQuery(request);
		fail("longitude should not accept longitude > 180");
	    } catch (RuntimeException e) {
	    }

	    // with long with comma
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "10,3");
	    try {
		query = new GeolocQuery(request);
		Assert.assertEquals(
			"request should accept longitude with comma", 10.3D,
			query.getLongitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // with long with point
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "10.3");
	    try {
		query = new GeolocQuery(request);
		Assert.assertEquals(
			"request should accept longitude with comma", 10.3D,
			query.getLongitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // with lat with comma
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "10,3");
	    try {
		query = new GeolocQuery(request);
		Assert.assertEquals(
			"request should accept latitude with comma", 10.3D,
			query.getLatitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // with lat with point
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "10.3");
	    try {
		query = new GeolocQuery(request);
		Assert.assertEquals(
			"request should accept latitude with point", 10.3D,
			query.getLatitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // test radius
	    // with missing radius
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.removeParameter(GeolocServlet.RADIUS_PARAMETER);
	    query = new GeolocQuery(request);
	    assertEquals("When no " + GeolocServlet.RADIUS_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
		    query.getRadius(), 0.1);
	    // With wrong radius
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "a");
	    query = new GeolocQuery(request);
	    assertEquals("When wrong " + GeolocServlet.RADIUS_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
		    query.getRadius(), 0.1);
	    // radius with comma
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "1,4");
	    query = new GeolocQuery(request);
	    assertEquals("Radius should accept comma as decimal separator",
		    1.4D, query.getRadius(), 0.1);

	    // radius with point
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "1.4");
	    query = new GeolocQuery(request);
	    assertEquals("Radius should accept point as decimal separator",
		    1.4D, query.getRadius(), 0.1);
	    
	 // distanceField default value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    query = new GeolocQuery(request);
	    assertTrue("By default distanceField should be true",
		     query.hasDistanceField());
	    
	 // distanceField case insensitive
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.DISTANCE_PARAMETER, "falSE");
	    query = new GeolocQuery(request);
	    assertFalse("distanceField should be set when specified",
		     query.hasDistanceField());
	    
	    // distanceField with off value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.DISTANCE_PARAMETER, "oFF");
	    query = new GeolocQuery(request);
	    assertFalse("distanceField should take off value into account",
		     query.hasDistanceField());
	    
	 // distanceField with wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	    request.setParameter(GeolocServlet.DISTANCE_PARAMETER, "wrong value");
	    query = new StreetSearchQuery(request);
	    assertTrue("distanceField should be kept to his default value if specified with wrong value",
			     query.hasDistanceField());

	} finally {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = savedDefaultType;
	}
    }

    @Test
    public void testGeolocQueryWithNullPointThrows() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	try {
	    new GeolocQuery(null, 3D, pagination, output, Adm.class);
	    fail("Query with null point should throws");
	} catch (IllegalArgumentException e) {

	}

	try {
	    new GeolocQuery(null, 5D);
	    fail("Query with null point should throws");
	} catch (RuntimeException e) {

	}
    }

    @Test
    public void testGeolocQueryWithWrongRadiusShouldBeSetWithDefault() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	// with negaive value
	GeolocQuery query = new GeolocQuery(GENERIC_POINT, -1, pagination,
		output, Adm.class);
	assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius());

	// with 0
	query = new GeolocQuery(GENERIC_POINT, 0, pagination, output, Adm.class);
	assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius());

    }

    @Test
    public void testEquals(){
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	GeolocQuery query = new GeolocQuery(GeolocHelper.createPoint(3.2F, 2.5F), 5, pagination,
		output, Adm.class);
	GeolocQuery queryWithDifferentLongitude = new GeolocQuery(GeolocHelper.createPoint(3.3F, 2.5F), 5, pagination,
		output, Adm.class);
	assertFalse("query should not be considered equals if longitude is not the same",query.equals(queryWithDifferentLongitude));
	
	GeolocQuery queryWithDifferentLat = new GeolocQuery(GeolocHelper.createPoint(3.2F, 2.6F), 5, pagination,
		output, Adm.class);
	assertFalse("query should not be considered equals if latitude is not the same",query.equals(queryWithDifferentLat));
	
	GeolocQuery queryEquals = new GeolocQuery(GeolocHelper.createPoint(3.2F, 2.5F), 5, pagination,
		output, Adm.class);
	assertEquals(query, queryEquals);
	
	
    }
    
    
    @Test
    public void testWithPaginationShouldBeSetToDefaultPaginationIfNull() {
	assertEquals(Pagination.DEFAULT_PAGINATION, new GeolocQuery(
		GENERIC_POINT).withPagination(null).getPagination());
    }

    @Test
    public void testWithPaginationShouldSetThePagination() {
	assertEquals(5, new GeolocQuery(GENERIC_POINT).withPagination(
		paginate().from(5).to(23)).getPagination().getFrom());
	assertEquals(23, new GeolocQuery(GENERIC_POINT).withPagination(
		paginate().from(5).to(23)).getPagination().getTo());
    }

    @Test
    public void testWithOutputShouldBeSetToDefaultOutputIfNull() {
	assertEquals(Output.DEFAULT_OUTPUT, new GeolocQuery(GENERIC_POINT)
		.withOutput(null).getOutput());
    }

    @Test
    public void testWithOutputShouldSetTheOutput() {
	GeolocQuery query = new GeolocQuery(GENERIC_POINT);
	Pagination pagination = paginate().from(2).to(7);
	query.withPagination(pagination);
	assertEquals(pagination, query.getPagination());
    }

    @Test
    public void testWithPlaceTypeShouldBeSetToNullIfNull() {
	assertNull(new GeolocQuery(GENERIC_POINT).withPlaceType(null)
		.getPlaceType());
    }

    @Test
    public void testWithPlaceTypeShouldSetTheplaceType() {
	GeolocQuery query = new GeolocQuery(GENERIC_POINT);
	query.withPlaceType(Adm.class);
	assertEquals(Adm.class, query.getPlaceType());
    }

    @Test
    public void testPlaceTypeShouldHaveADefaultValue() {
	Class<? extends GisFeature> savedDefaultType = GisgraphyConfig.defaultGeolocSearchPlaceTypeClass;
	try {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = Country.class;
	    GeolocQuery query = new GeolocQuery(GENERIC_POINT);
	    assertEquals(GisgraphyConfig.defaultGeolocSearchPlaceTypeClass,
		    query.getPlaceType());
	    query = new GeolocQuery(GENERIC_POINT, 10);
	    assertEquals(GisgraphyConfig.defaultGeolocSearchPlaceTypeClass,
		    query.getPlaceType());
	} finally {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = savedDefaultType;
	}
    }
    
    @Test
    public void testDistanceField(){
	GeolocQuery query = new GeolocQuery(GENERIC_POINT);
	Assert.assertTrue("by default distanceField should be true", query.hasDistanceField());
	query.withDistanceField(false);
	assertFalse("distance field setter is broken", query.hasDistanceField());
    }


}
