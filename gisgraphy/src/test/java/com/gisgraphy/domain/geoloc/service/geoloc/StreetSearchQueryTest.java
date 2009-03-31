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
import com.gisgraphy.servlet.FulltextServlet;
import com.gisgraphy.servlet.GeolocServlet;
import com.gisgraphy.test.GeolocTestHelper;
import com.vividsolutions.jts.geom.Point;

public class StreetSearchQueryTest extends TestCase {

    /**
     * a simple point to avoid creation of new one for each test
     */
    private static Point GENERIC_POINT = GeolocHelper.createPoint(3.2F, 2.5F);

    @Test
    public void testStreetSearchQueryPointRadiusPaginationOutputStreetType() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL).withIndentation();
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT, 3D, pagination,
		output, "streetType","oneWay","namePrefix");
	assertEquals(pagination, query.getPagination());
	assertEquals(output, query.getOutput());
	assertEquals(null, query.getPlaceType());
	assertEquals(GENERIC_POINT, query.getPoint());
	assertTrue(query.isOutputIndented());
	assertEquals(3D, query.getRadius());
	assertEquals("streetType", query.getStreetType());
	assertEquals("oneWay", query.getOneWay());
	assertEquals("namePrefix", query.getNamePrefix());
    }

    public void testStreetSearchQueryPointRadius() {
	    StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT, 3D,"streetType");
	    assertEquals(Pagination.DEFAULT_PAGINATION, query.getPagination());
	    assertEquals(Output.DEFAULT_OUTPUT, query.getOutput());
	    assertEquals(GENERIC_POINT, query.getPoint());
	    assertEquals(3D, query.getRadius());
	    assertEquals("streetType", query.getStreetType());
    }

    public void testStreetSearchQueryPoint() {
	    StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,"streetType");
	    assertEquals(Pagination.DEFAULT_PAGINATION, query.getPagination());
	    assertEquals(Output.DEFAULT_OUTPUT, query.getOutput());
	    assertEquals(GENERIC_POINT, query.getPoint());
	    assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius());
	    assertEquals("streetType", query.getStreetType());
    }

    @Test
    public void testStreetSearchQueryFromAnHttpServletRequest() {
	Class<? extends GisFeature> savedDefaultType = GisgraphyConfig.defaultGeolocSearchPlaceTypeClass;
	try {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = Country.class;
	    MockHttpServletRequest request = GeolocTestHelper
		    .createMockHttpServletRequestForStreetGeoloc();
	    StreetSearchQuery query = new StreetSearchQuery(request);
	    int firstPaginationIndex =3;
	    assertEquals(firstPaginationIndex, query.getFirstPaginationIndex());
	    assertEquals(StreetSearchQuery.MAX_RESULTS+firstPaginationIndex-1, query.getLastPaginationIndex());
	    assertEquals("the pagination should be limit to "
		    + StreetSearchQuery.MAX_RESULTS,
		    StreetSearchQuery.MAX_RESULTS, query
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
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.FROM_PARAMETER);
	    query = new StreetSearchQuery(request);
	    assertEquals("When no " + GeolocServlet.FROM_PARAMETER
		    + " is specified, the parameter should be "
		    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		    .getFirstPaginationIndex());
	    // with a wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.FROM_PARAMETER, "-1");
	    query = new StreetSearchQuery(request);
	    assertEquals("When a wrong " + GeolocServlet.FROM_PARAMETER
		    + " is specified, the parameter should be "
		    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		    .getFirstPaginationIndex());
	    // with a non mumeric value
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.FROM_PARAMETER, "a");
	    query = new StreetSearchQuery(request);
	    assertEquals("When a wrong " + GeolocServlet.FROM_PARAMETER
		    + " is specified, the parameter should be "
		    + Pagination.DEFAULT_FROM, Pagination.DEFAULT_FROM, query
		    .getFirstPaginationIndex());

	    // test last pagination index
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.TO_PARAMETER);
	    query = new StreetSearchQuery(request);
	    // non specify
	    assertEquals(
		    "When no "
			    + GeolocServlet.TO_PARAMETER
			    + " is specified, the  parameter should be set to limit results to "
			    + FulltextServlet.DEFAULT_MAX_RESULTS,
		    FulltextServlet.DEFAULT_MAX_RESULTS, query
			    .getMaxNumberOfResults());
	    // with a wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.TO_PARAMETER, "2");// to<from
	    query = new StreetSearchQuery(request);
	    assertEquals("When a wrong " + GeolocServlet.TO_PARAMETER
		    + " is specified, the numberOf results should be "
		    + Pagination.DEFAULT_MAX_RESULTS,
		    Pagination.DEFAULT_MAX_RESULTS, query
			    .getMaxNumberOfResults());
	    assertEquals("a wrong to does not change the from value", 3, query
		    .getFirstPaginationIndex());
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    // non numeric
	    request.setParameter(GeolocServlet.TO_PARAMETER, "a");// to<from
	    query = new StreetSearchQuery(request);
	    assertEquals("a wrong to does not change the from value", 3, query
		    .getFirstPaginationIndex());
	    assertEquals("When a wrong " + GeolocServlet.TO_PARAMETER
		    + " is specified, the numberOf results should be "
		    + Pagination.DEFAULT_MAX_RESULTS,
		    Pagination.DEFAULT_MAX_RESULTS, query
			    .getMaxNumberOfResults());

	    // test indentation
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.INDENT_PARAMETER);
	    query = new StreetSearchQuery(request);
	    assertFalse("When no " + GeolocServlet.INDENT_PARAMETER
		    + " is specified, the  parameter should be set to false",
		    query.isOutputIndented());
	    // with wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.INDENT_PARAMETER, "UNK");
	    query = new StreetSearchQuery(request);
	    assertFalse("When wrong " + GeolocServlet.INDENT_PARAMETER
		    + " is specified, the  parameter should be set to false",
		    query.isOutputIndented());
	    // test case sensitive
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.INDENT_PARAMETER, "true");
	    query = new StreetSearchQuery(request);
	    assertTrue(GeolocServlet.INDENT_PARAMETER
		    + " should be case insensitive  ", query.isOutputIndented());
	    // test 'on' value
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(FulltextServlet.INDENT_PARAMETER, "oN");
	    query = new StreetSearchQuery(request);
	    assertTrue(
		    FulltextServlet.INDENT_PARAMETER
			    + " should be true for 'on' value (case insensitive and on value)  ",
		    query.isOutputIndented());

	    // test outputFormat
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.FORMAT_PARAMETER);
	    query = new StreetSearchQuery(request);
	    assertEquals("When no " + GeolocServlet.FORMAT_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + OutputFormat.getDefault(), OutputFormat.getDefault(),
		    query.getOutputFormat());
	    // with wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.FORMAT_PARAMETER, "UNK");
	    query = new StreetSearchQuery(request);
	    assertEquals("When wrong " + GeolocServlet.FORMAT_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + OutputFormat.getDefault(), OutputFormat.getDefault(),
		    query.getOutputFormat());
	    // test case sensitive
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.FORMAT_PARAMETER, "json");
	    query = new StreetSearchQuery(request);
	    assertEquals(GeolocServlet.FORMAT_PARAMETER
		    + " should be case insensitive  ", OutputFormat.JSON, query
		    .getOutputFormat());

	    // test placetype
	    // with no value specified
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.PLACETYPE_PARAMETER);
	    query = new StreetSearchQuery(request);
	    assertEquals(
		    "When no "
			    + GeolocServlet.PLACETYPE_PARAMETER
			    + " is specified, the  parameter should be set to defaultGeolocSearchPlaceType ",
		    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass, query
			    .getPlaceType());
	    // with wrong value
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.PLACETYPE_PARAMETER, "unk");
	    query = new StreetSearchQuery(request);
	    assertEquals(
		    "When wrong "
			    + GeolocServlet.PLACETYPE_PARAMETER
			    + " is specified, the  parameter should be set to defaultGeolocSearchPlaceType ",
		    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass, query
			    .getPlaceType());
	    // test case sensitive
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.FORMAT_PARAMETER, "city");
	    query = new StreetSearchQuery(request);
	    assertEquals(GeolocServlet.PLACETYPE_PARAMETER
		    + " should be case insensitive  ", City.class, query
		    .getPlaceType());

	    // test Point
	    // with missing lat
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.LAT_PARAMETER);
	    try {
		query = new StreetSearchQuery(request);
		fail("When there is no latitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // with empty lat
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "");
	    try {
		query = new StreetSearchQuery(request);
		fail("When there is empty latitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // With wrong lat
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "a");
	    try {
		query = new StreetSearchQuery(request);
		fail("A null lat should throw");
	    } catch (RuntimeException e) {
	    }
	    // With too small lat
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "-92");
	    try {
		query = new StreetSearchQuery(request);
		fail("latitude should not accept latitude < -90");
	    } catch (RuntimeException e) {
	    }

	    // With too high lat
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "92");
	    try {
		query = new StreetSearchQuery(request);
		fail("latitude should not accept latitude > 90");
	    } catch (RuntimeException e) {
	    }

	    // with missing long
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.LONG_PARAMETER);
	    try {
		query = new StreetSearchQuery(request);
		fail("When there is no latitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // with empty long
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "");
	    try {
		query = new StreetSearchQuery(request);
		fail("When there is empty longitude, query should throw");
	    } catch (RuntimeException e) {
	    }
	    // With wrong Long
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "a");
	    try {
		query = new StreetSearchQuery(request);
		fail("A null lat should throw");
	    } catch (RuntimeException e) {
	    }

	    // with too small long
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "-182");
	    try {
		query = new StreetSearchQuery(request);
		fail("longitude should not accept longitude < -180");
	    } catch (RuntimeException e) {
	    }

	    // with too high long
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "182");
	    try {
		query = new StreetSearchQuery(request);
		fail("longitude should not accept longitude > 180");
	    } catch (RuntimeException e) {
	    }

	    // with long with comma
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "10,3");
	    try {
		query = new StreetSearchQuery(request);
		Assert.assertEquals(
			"request should accept longitude with comma", 10.3D,
			query.getLongitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // with long with point
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LONG_PARAMETER, "10.3");
	    try {
		query = new StreetSearchQuery(request);
		Assert.assertEquals(
			"request should accept longitude with comma", 10.3D,
			query.getLongitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // with lat with comma
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "10,3");
	    try {
		query = new StreetSearchQuery(request);
		Assert.assertEquals(
			"request should accept latitude with comma", 10.3D,
			query.getLatitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // with lat with point
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.LAT_PARAMETER, "10.3");
	    try {
		query = new StreetSearchQuery(request);
		Assert.assertEquals(
			"request should accept latitude with point", 10.3D,
			query.getLatitude().doubleValue(), 0.1);

	    } catch (RuntimeException e) {
	    }

	    // test radius
	    // with missing radius
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.removeParameter(GeolocServlet.RADIUS_PARAMETER);
	    query = new StreetSearchQuery(request);
	    assertEquals("When no " + GeolocServlet.RADIUS_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
		    query.getRadius(), 0.1);
	    // With wrong radius
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "a");
	    query = new StreetSearchQuery(request);
	    assertEquals("When wrong " + GeolocServlet.RADIUS_PARAMETER
		    + " is specified, the  parameter should be set to  "
		    + GeolocQuery.DEFAULT_RADIUS, GeolocQuery.DEFAULT_RADIUS,
		    query.getRadius(), 0.1);
	    // radius with comma
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "1,4");
	    query = new StreetSearchQuery(request);
	    assertEquals("Radius should accept comma as decimal separator",
		    1.4D, query.getRadius(), 0.1);

	    // radius with point
	    request = GeolocTestHelper.createMockHttpServletRequestForStreetGeoloc();
	    request.setParameter(GeolocServlet.RADIUS_PARAMETER, "1.4");
	    query = new StreetSearchQuery(request);
	    assertEquals("Radius should accept point as decimal separator",
		    1.4D, query.getRadius(), 0.1);

	} finally {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = savedDefaultType;
	}
    }

    @Test
    public void testStreetSearchQueryWithNullPointThrows() {
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
    public void testStreetSearchQueryWithWrongRadiusShouldBeSetWithDefault() {
	Pagination pagination = paginate().from(2).to(7);
	Output output = Output.withFormat(OutputFormat.JSON).withLanguageCode(
		"FR").withStyle(OutputStyle.FULL);
	// with negaive value
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT, -1, pagination,
		output, "streetType","oneWay","namePrefix");
	assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius());

	// with 0
	 query = new StreetSearchQuery(GENERIC_POINT, 0, pagination,
		output, "streetType","oneWay","namePrefix");
	assertEquals(GeolocQuery.DEFAULT_RADIUS, query.getRadius());

    }

    // TODO test withradius

    @Test
    public void testStreetWithPaginationShouldBeSetToDefaultPaginationIfNull() {
	assertEquals(Pagination.DEFAULT_PAGINATION, new StreetSearchQuery(
		GENERIC_POINT,"streetType").withPagination(null).getPagination());
    }

    @Test
    public void testWithPaginationShouldSetThePagination() {
	assertEquals(5, new StreetSearchQuery(GENERIC_POINT,"streetType").withPagination(
		paginate().from(5).to(23)).getPagination().getFrom());
	assertEquals(23, new StreetSearchQuery(GENERIC_POINT,"streetType").withPagination(
		paginate().from(5).to(23)).getPagination().getTo());
    }

    @Test
    public void testStreetWithOutputShouldBeSetToDefaultOutputIfNull() {
	assertEquals(Output.DEFAULT_OUTPUT, new GeolocQuery(GENERIC_POINT)
		.withOutput(null).getOutput());
    }

    @Test
    public void testWithOutputShouldSetTheOutput() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,"streetType");
	Pagination pagination = paginate().from(2).to(7);
	query.withPagination(pagination);
	assertEquals(pagination, query.getPagination());
    }

    @Test
    public void testWithStreetTypeShouldBeSetToNullIfNull() {
	assertNull(new StreetSearchQuery(GENERIC_POINT,"streeType").withStreetType(null)
		.getStreetType());
    }

    @Test
    public void testWithStreetTypeShouldSetTheStreetType() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,"StreetType");
	query.withStreetType("streetType2");
	assertEquals("streetType2", query.getStreetType());
    }

    @Test
    public void testWithOneWayShouldSetTheOneWay() {
	StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,"StreetType");
	query.withOneWay("oneWay");
	assertEquals("oneWay", query.getOneWay());
    }

   @Test
   public void testToString(){
       StreetSearchQuery query = new StreetSearchQuery(GENERIC_POINT,"StreetType");
       assertFalse("ToString should not contains GeolocQuery and should be overide",query.toString().contains("GeolocQuery"));
       
       
       
       
   }

}
