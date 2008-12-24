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
package com.gisgraphy.helper;

import java.util.List;

import junit.framework.TestCase;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.GisFeature;

public class IntrospectionHelperTest extends TestCase {

    public void testGetGisFeatureFieldsAsListShouldIgnoreAnnotedFields() {
	List<String> fields = IntrospectionHelper
		.getGisFeatureFieldsAsList(GisFeature.class);
	assertEquals(19, fields.size());
	assertTrue(fields.contains("featureId"));
	assertTrue(fields.contains("name"));
	assertTrue(fields.contains("asciiName"));
	assertTrue(fields.contains("location"));
	assertTrue(fields.contains("adm1Code"));
	assertTrue(fields.contains("adm2Code"));
	assertTrue(fields.contains("adm3Code"));
	assertTrue(fields.contains("adm4Code"));
	assertTrue(fields.contains("adm1Name"));
	assertTrue(fields.contains("adm2Name"));
	assertTrue(fields.contains("adm3Name"));
	assertTrue(fields.contains("adm4Name"));
	assertTrue(fields.contains("featureClass"));
	assertTrue(fields.contains("featureCode"));
	assertTrue(fields.contains("countryCode"));
	assertTrue(fields.contains("population"));
	assertTrue(fields.contains("elevation"));
	assertTrue(fields.contains("gtopo30"));
	assertTrue(fields.contains("timezone"));
    }

    public void testGetGisFeatureFieldsAsListShouldExploreSubClass() {
	List<String> fields = IntrospectionHelper
		.getGisFeatureFieldsAsList(City.class);
	assertEquals(20, fields.size());
	assertTrue(fields.contains("featureId"));
	assertTrue(fields.contains("name"));
	assertTrue(fields.contains("asciiName"));
	assertTrue(fields.contains("location"));
	assertTrue(fields.contains("adm1Code"));
	assertTrue(fields.contains("adm2Code"));
	assertTrue(fields.contains("adm3Code"));
	assertTrue(fields.contains("adm4Code"));
	assertTrue(fields.contains("adm1Name"));
	assertTrue(fields.contains("adm2Name"));
	assertTrue(fields.contains("adm3Name"));
	assertTrue(fields.contains("adm4Name"));
	assertTrue(fields.contains("featureClass"));
	assertTrue(fields.contains("featureCode"));
	assertTrue(fields.contains("countryCode"));
	assertTrue(fields.contains("population"));
	assertTrue(fields.contains("elevation"));
	assertTrue(fields.contains("gtopo30"));
	assertTrue(fields.contains("timezone"));
	assertTrue(fields.contains("zipCode"));
    }

    public void testGetGisFeatureFieldsAsArrayShouldIgnoreAnnotedFields() {
	String[] fields = IntrospectionHelper
		.getGisFeatureFieldsAsArray(GisFeature.class);
	assertEquals(19, fields.length);
    }

    public void testGetGisFeatureFieldsAsArrayShouldExploreSubClass() {
	String[] fields = IntrospectionHelper
		.getGisFeatureFieldsAsArray(City.class);
	assertEquals(20, fields.length);
    }

    public void testGetGisFeatureFieldsAsArrayShouldReturnTheSameValueForSecondCall() {
	String[] fields = IntrospectionHelper
		.getGisFeatureFieldsAsArray(GisFeature.class);
	fields = IntrospectionHelper
		.getGisFeatureFieldsAsArray(GisFeature.class);
	assertEquals(19, fields.length);
	fields = IntrospectionHelper.getGisFeatureFieldsAsArray(City.class);
	fields = IntrospectionHelper.getGisFeatureFieldsAsArray(City.class);
	assertEquals(20, fields.length);
    }

    public void testClearCache() {
	String[] fields = IntrospectionHelper
		.getGisFeatureFieldsAsArray(GisFeature.class);
	assertEquals(19, fields.length);
	IntrospectionHelper.clearCache();
	fields = IntrospectionHelper
		.getGisFeatureFieldsAsArray(GisFeature.class);
	assertEquals(19, fields.length);
    }

    public void testGetGisFeatureFieldsAsListShouldReturnTheSameValueForSecondCall() {
	List<String> fields = IntrospectionHelper
		.getGisFeatureFieldsAsList(GisFeature.class);
	fields = IntrospectionHelper
		.getGisFeatureFieldsAsList(GisFeature.class);
	assertEquals(19, fields.size());
	fields = IntrospectionHelper.getGisFeatureFieldsAsList(City.class);
	fields = IntrospectionHelper.getGisFeatureFieldsAsList(City.class);
	assertEquals(20, fields.size());

    }

}
