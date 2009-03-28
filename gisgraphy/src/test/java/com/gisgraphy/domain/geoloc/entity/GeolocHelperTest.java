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
package com.gisgraphy.domain.geoloc.entity;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.SRID;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeolocHelperTest {

    @Test
    public void testCreatePointWithFloat() {
	Assert.assertNotNull(GeolocHelper.createPoint(54.2F, -46.5F));
    }

    @Test
    public void testCreatePointWithWrongLatitudeShouldThrowsAnIllegalArgumentException() {
	try {
	    GeolocHelper.createPoint(54.2F, -95F);
	    Assert.fail("createPoint should only accept -90 < lattitude < 90");
	} catch (IllegalArgumentException e) {
	    // ok
	}

	try {
	    GeolocHelper.createPoint(54.2F, +95F);
	    Assert.fail("createPoint should only accept -90 < lattitude < 90");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void testCreatePointWithWrongLongitudeShouldThrowsAnIllegalArgumentException() {
	try {
	    GeolocHelper.createPoint(-180.5F, 35F);
	    Assert.fail("createPoint should only accept -180 < longitude < 180");
	} catch (IllegalArgumentException e) {
	    // ok
	}

	try {
	    GeolocHelper.createPoint(180.5F, +95F);
	    Assert.fail("createPoint should only accept -180 < longitude < 180");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void testDistanceShouldReturnCorrectDistance() {
	Point point1 = GeolocHelper.createPoint(48.867F, 2.333F);

	Point point2 = GeolocHelper.createPoint(49.017F, 2.467F);

	Assert.assertEquals(Math.round(GeolocHelper.distance(point1, point2)), Math
		.round(GeolocHelper.distance(point2, point1)));
	Assert.assertEquals(22313, Math.round(GeolocHelper.distance(point1, point2)));
    }

    @Test
    public void testGetClassEntityFromStringShouldReturnCorrectClass() {
	// typic
	Class<? extends GisFeature> clazz = GeolocHelper
		.getClassEntityFromString("City");
	Assert.assertNotNull(
		"getClassEntityFromString does not return a correct class",
		clazz);
	Assert.assertEquals(City.class, clazz);

	// not existing
	clazz = GeolocHelper.getClassEntityFromString("nothing");
	Assert.assertNull(clazz);

	// case insensitive
	clazz = GeolocHelper.getClassEntityFromString("city");
	Assert.assertNotNull("getClassEntityFromString should be case insensitive",
		clazz);
	Assert.assertEquals(City.class, clazz);

	// case insensitive
	clazz = GeolocHelper.getClassEntityFromString("gisfeature");
	Assert.assertNotNull("getClassEntityFromString should be case insensitive",
		clazz);
	Assert.assertEquals(GisFeature.class, clazz);

	// with null
	clazz = GeolocHelper.getClassEntityFromString(null);
	Assert.assertNull(clazz);
    }

    @Test
    public void testParseInternationalDoubleShouldAcceptPointOrCommaasdecimalSeparator() {
	try {
	    Assert.assertEquals(
		    "parseInternationalDouble should accept point as a decimal separator ",
		    3.2F, GeolocHelper.parseInternationalDouble("3.2"),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept comma as a decimal separator ",
		    3.2F, GeolocHelper.parseInternationalDouble("3,2"),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept numeric value without comma or point ",
		    3.0F, GeolocHelper.parseInternationalDouble("3"),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept numeric value that ends with point ",
		    3.0F, GeolocHelper.parseInternationalDouble("3."),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept numeric value that ends with comma ",
		    3.0F, GeolocHelper.parseInternationalDouble("3,"),0.1);
	} catch (ParseException e) {
	    Assert.fail();
	}
    }
    
    @Test
    public void testCreateMultiLineStringFromString(){
	//test with negative and Float values
	String[] wktLineStrings={"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40.5 40, 50 -50)"};
	MultiLineString MultiLineString = GeolocHelper.createMultiLineString(wktLineStrings);
	Assert.assertNotNull(MultiLineString);
	Assert.assertEquals("wrong SRID",SRID.WGS84_SRID.getSRID(), MultiLineString.getSRID());
	Assert.assertTrue("createMultiLineStringFromString is not of MultiLineString type", MultiLineString instanceof MultiLineString);
    }

    @Test
    public void testCreateMultiLineStringFromStringShouldThrowIllegalArgumentExceptionIfStringsAreNotCorrect(){
	String[] wktLineStrings={"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40 40, 50 )"};
	
	try {
	    GeolocHelper.createMultiLineString(wktLineStrings);
	    Assert.fail("createMultiLineStringFromString should throw illegalArgumentException if StringAreNotCorrect");
	} catch (Exception e) {
	   //ok
	}
	
    }
    
    @Test
    public void testCreatePolygonBoxShouldNotAcceptWrongParameters(){
	
	
	try {
	    GeolocHelper.createPolygonBox(4F, 3F, 0F);
	    Assert.fail("distance can not be 0");
	} catch (IllegalArgumentException e) {
	    //ok
	}
	
	try {
	    GeolocHelper.createPolygonBox(4F, 3F, -1F);
	    Assert.fail("distance can not be <0");
	} catch (IllegalArgumentException e) {
	    //ok
	}
	
    }
    
    @Test
    public void testCreatePolygonBox(){
	Float distance = 5F;
	    Polygon polygon = GeolocHelper.createPolygonBox(10F, 30F, distance);
	    Assert.assertEquals(SRID.WGS84_SRID.getSRID(), polygon.getSRID());
	    Polygon polygon2 = GeolocHelper.createPolygonBox(1F, 13F, distance);
	    Assert.assertEquals("Area of polygon calculate for the same distance should be equals",polygon.getArea(), polygon2.getArea(),0.01);
	    
	    
	Coordinate[] coordinates = polygon.getCoordinates();
	Assert.assertEquals(5,coordinates.length);
	Point pointBottomLeft = GeolocHelper.createPoint(Double.valueOf(coordinates[0].x).floatValue(), Double.valueOf(coordinates[0].y).floatValue());
	Point pointBottomRight = GeolocHelper.createPoint(Double.valueOf(coordinates[1].x).floatValue(), Double.valueOf(coordinates[1].y).floatValue());
	Point pointTopRight = GeolocHelper.createPoint(Double.valueOf(coordinates[2].x).floatValue(), Double.valueOf(coordinates[2].y).floatValue());
	Point poitnTopLeft = GeolocHelper.createPoint(Double.valueOf(coordinates[3].x).floatValue(), Double.valueOf(coordinates[3].y).floatValue());
		
		
		
		Assert.assertEquals("The first point should be the same as the last one",coordinates[0].x, coordinates[4].x,0.01);
		Assert.assertEquals("The first point should be the same as the last one",coordinates[0].y, coordinates[4].y,0.01);
		Assert.assertEquals(2*distance,GeolocHelper.distance(pointBottomLeft, pointBottomRight),1);
		Assert.assertEquals(2*distance,GeolocHelper.distance(pointBottomRight, pointTopRight),1);
		Assert.assertEquals(2*distance,GeolocHelper.distance(poitnTopLeft, pointBottomLeft),1);
	
    }

    
}
