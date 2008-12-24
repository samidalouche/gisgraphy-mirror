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

import junit.framework.TestCase;

import org.junit.Test;

import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;

public class GeolocHelperTest extends TestCase {

    @Test
    public void testCreatePointWithFloat() {
	assertNotNull(GeolocHelper.createPoint(54.2F, -46.5F));
    }

    @Test
    public void testCreatePointWithWrongLatitudeShouldThrowsAnIllegalArgumentException() {
	try {
	    GeolocHelper.createPoint(54.2F, -95F);
	    fail("createPoint should only accept -90 < lattitude < 90");
	} catch (IllegalArgumentException e) {
	    // ok
	}

	try {
	    GeolocHelper.createPoint(54.2F, +95F);
	    fail("createPoint should only accept -90 < lattitude < 90");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void testCreatePointWithWrongLongitudeShouldThrowsAnIllegalArgumentException() {
	try {
	    GeolocHelper.createPoint(-180.5F, 35F);
	    fail("createPoint should only accept -180 < longitude < 180");
	} catch (IllegalArgumentException e) {
	    // ok
	}

	try {
	    GeolocHelper.createPoint(180.5F, +95F);
	    fail("createPoint should only accept -180 < longitude < 180");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void testDistanceShouldReturnCorrectDistance() {
	Point point1 = GeolocHelper.createPoint(48.867F, 2.333F);

	Point point2 = GeolocHelper.createPoint(49.017F, 2.467F);

	assertEquals(Math.round(GeolocHelper.distance(point1, point2)), Math
		.round(GeolocHelper.distance(point2, point1)));
	assertEquals(22313, Math.round(GeolocHelper.distance(point1, point2)));
    }

    @Test
    public void testGetClassEntityFromStringShouldReturnCorrectClass() {
	// typic
	Class<? extends GisFeature> clazz = GeolocHelper
		.getClassEntityFromString("City");
	assertNotNull(
		"getClassEntityFromString does not return a correct class",
		clazz);
	assertEquals(City.class, clazz);

	// not existing
	clazz = GeolocHelper.getClassEntityFromString("nothing");
	assertNull(clazz);

	// case insensitive
	clazz = GeolocHelper.getClassEntityFromString("city");
	assertNotNull("getClassEntityFromString should be case insensitive",
		clazz);
	assertEquals(City.class, clazz);

	// case insensitive
	clazz = GeolocHelper.getClassEntityFromString("gisfeature");
	assertNotNull("getClassEntityFromString should be case insensitive",
		clazz);
	assertEquals(GisFeature.class, clazz);

	// with null
	clazz = GeolocHelper.getClassEntityFromString(null);
	assertNull(clazz);
    }

    public void testParseInternationalDoubleShouldAcceptPointOrCommaasdecimalSeparator() {
	try {
	    assertEquals(
		    "parseInternationalDouble should accept point as a decimal separator ",
		    3.2F, GeolocHelper.parseInternationalDouble("3.2"));
	    assertEquals(
		    "parseInternationalDouble should accept comma as a decimal separator ",
		    3.2F, GeolocHelper.parseInternationalDouble("3,2"));
	    assertEquals(
		    "parseInternationalDouble should accept numeric value without comma or point ",
		    3.0F, GeolocHelper.parseInternationalDouble("3"));
	    assertEquals(
		    "parseInternationalDouble should accept numeric value that ends with point ",
		    3.0F, GeolocHelper.parseInternationalDouble("3."));
	    assertEquals(
		    "parseInternationalDouble should accept numeric value that ends with comma ",
		    3.0F, GeolocHelper.parseInternationalDouble("3,"));
	} catch (ParseException e) {
	    fail();
	}
    }

}
