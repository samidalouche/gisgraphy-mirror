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
package com.gisgraphy.domain;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.helper.FeatureClassCodeHelper;

public class FeatureClassCodeHelperTest extends
	AbstractIntegrationHttpSolrTestCase {

    @Test
    public void testIsCityWithNullValuesShouldNotThrow() {
	assertFalse(FeatureClassCodeHelper.isCity(null, null));
    }

    @Test
    public void testIsCityWithCorrectFeatureClassAndWrongFeatureCodeShouldReturnFalse() {
	assertFalse(FeatureClassCodeHelper.isCity("P", "ERR"));
    }

    @Test
    public void testIsCityWithIncorrectFeatureClassAndCorrectFeatureCodeShouldReturnFalse() {
	assertFalse(FeatureClassCodeHelper.isCountry("F", "PPL"));
    }

    @Test
    public void testIsCityIsCaseSensitiveForFeatureClass() {
	assertFalse(FeatureClassCodeHelper.isCity("p", "PPL"));
    }

    @Test
    public void testIsCityIsCaseSensitiveForFeatureCode() {
	assertFalse(FeatureClassCodeHelper.isCity("P", "ppl"));
    }

    @Test
    public void testIsCityShouldReturnTrueForCityFeatureClassCode() {
	assertTrue(FeatureClassCodeHelper.isCity("P", "PPL"));
    }

    @Test
    public void testIsCityShouldReturnFalseForNonCityFeatureClassCode() {
	assertFalse(FeatureClassCodeHelper.isCity("P", "PPLE"));
    }

    // iscountry
    @Test
    public void testIsCoutnryWithNullValuesShouldNotThrow() {
	assertFalse(FeatureClassCodeHelper.isCountry(null, null));
    }

    @Test
    public void testIsCountryWithCorrectFeatureClassAndWrongFeatureCodeShouldReturnFalse() {
	assertFalse(FeatureClassCodeHelper.isCountry("A", "ERR"));
    }

    @Test
    public void testIsCountryWithIncorrectFeatureClassAndCorrectFeatureCodeShouldReturnFalse() {
	assertFalse(FeatureClassCodeHelper.isCountry("ERR", "PCL"));
    }

    @Test
    public void testIsCountryIsCaseSensitiveForFeatureClass() {
	assertFalse(FeatureClassCodeHelper.isCountry("a", "PCL"));
    }

    @Test
    public void testIsCountryIsCaseSensitiveForFeatureCode() {
	assertFalse(FeatureClassCodeHelper.isCountry("A", "pcl"));
    }

    @Test
    public void testIsCountryShouldReturnTrueForCountryFeatureClassCode() {
	assertTrue(FeatureClassCodeHelper.isCountry("A", "PCL"));
	assertTrue(FeatureClassCodeHelper.isCountry("A", "PCLD"));
	assertTrue(FeatureClassCodeHelper.isCountry("A", "PCLF"));
	assertTrue(FeatureClassCodeHelper.isCountry("A", "PCLI"));
	assertTrue(FeatureClassCodeHelper.isCountry("A", "PCLS"));
    }

    @Test
    public void testIsCountryShouldReturnFalseForNonCountryFeatureClassCode() {
	assertFalse(FeatureClassCodeHelper.isCountry("P", "PCLIX"));
    }

    // isAdm

    @Test
    public void testIsAdmWithNullValuesShouldNotThrow() {
	assertFalse(FeatureClassCodeHelper.is_Adm(null, null));
    }

    @Test
    public void testIsAdmWithCorrectFeatureClassAndWrongFeatureCodeShouldReturnFalse() {
	assertFalse(FeatureClassCodeHelper.is_Adm("A", "ERR"));
    }

    @Test
    public void testIsAdmWithIncorrectFeatureClassAndCorrectFeatureCodeShouldReturnFalse() {
	assertFalse(FeatureClassCodeHelper.is_Adm("ERR", "ADM1"));
    }

    @Test
    public void testIsAdmIsCaseSensitiveForFeatureClass() {
	assertFalse(FeatureClassCodeHelper.is_Adm("a", "ADM1"));

    }

    @Test
    public void testIsAdmIsCaseSensitiveForFeatureCode() {
	assertFalse(FeatureClassCodeHelper.is_Adm("A", "adm1"));
    }

    @Test
    public void testIsAdmShouldReturnTrueForCountryFeatureClassCode() {
	assertTrue(FeatureClassCodeHelper.is_Adm("A", "ADM1"));
	assertTrue(FeatureClassCodeHelper.is_Adm("A", "ADM2"));
	assertTrue(FeatureClassCodeHelper.is_Adm("A", "ADM3"));
	assertTrue(FeatureClassCodeHelper.is_Adm("A", "ADM4"));
    }

    @Test
    public void testIsAdmShouldReturnFalseForNonCityFeatureClassCode() {
	assertFalse(FeatureClassCodeHelper.is_Adm("A", "ADM"));
    }

}
