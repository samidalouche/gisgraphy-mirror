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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.test.GeolocTestHelper;

public class GisFeatureTest extends AbstractIntegrationHttpSolrTestCase {

    private ICountryDao countryDao;

    private ICityDao cityDao;

    @Test
    public void testAddAlternateNamesShouldAddChildrenAndNotReplace() {
	GisFeature gisFeature = GeolocTestHelper
		.createGisFeatureWithAlternateNames("toto", 3);
	List<AlternateName> alternateNames = new ArrayList<AlternateName>();
	AlternateName a1 = new AlternateName();
	AlternateName a2 = new AlternateName();
	alternateNames.add(a1);
	alternateNames.add(a2);
	gisFeature.addAlternateNames(alternateNames);
	assertEquals(5, gisFeature.getAlternateNames().size());
    }

    @Test
    public void testAddAlternateNamesShouldDoADoubleSet() {
	GisFeature gisFeature = GeolocTestHelper
		.createGisFeatureWithAlternateNames("toto", 3);
	List<AlternateName> alternateNames = new ArrayList<AlternateName>();
	AlternateName a1 = new AlternateName();
	AlternateName a2 = new AlternateName();
	alternateNames.add(a1);
	alternateNames.add(a2);
	gisFeature.addAlternateNames(alternateNames);
	assertEquals(5, gisFeature.getAlternateNames().size());
	for (AlternateName alternateName : gisFeature.getAlternateNames()) {
	    assertEquals(gisFeature, alternateName.getGisFeature());
	}
    }

    @Test
    public void testSetFeatureClassShouldAlwaysSetInUpperCase() {
	GisFeature gisFeature = new GisFeature();
	gisFeature.setFeatureClass("a");
	assertEquals("A", gisFeature.getFeatureClass());
    }

    @Test
    public void testSetFeatureCodeShouldAlwaysSetInUpperCase() {
	GisFeature gisFeature = new GisFeature();
	gisFeature.setFeatureCode("a");
	assertEquals("A", gisFeature.getFeatureCode());
    }

    @Test
    public void testSetFeatureClassWithNullValueShouldNotThrow() {
	GisFeature gisFeature = new GisFeature();
	try {
	    gisFeature.setFeatureClass(null);
	} catch (RuntimeException e) {
	    fail("setting a null feture class should not throw");
	}
    }

    @Test
    public void testSetFeatureCodeWithNullShouldnotThrow() {
	GisFeature gisFeature = new GisFeature();
	try {
	    gisFeature.setFeatureCode(null);
	} catch (RuntimeException e) {
	    fail("setting a null feature code should not throw");
	}
    }

    @Test
    public void testGetCountryShouldReturnTheCountryObject() {
	Country country = GeolocTestHelper.createCountryForFrance();
	Country savedCountry = this.countryDao.save(country);
	assertNotNull(savedCountry.getId());

	GisFeature gisFeature = GeolocTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);
	paris.setCountryCode("FR");

	// save city
	City savedParis = this.cityDao.save(paris);

	// chek city is well saved
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());

	Country retrievedCountry = savedParis.getCountry();
	assertNotNull(retrievedCountry);
	assertEquals(savedCountry, retrievedCountry);

    }

    @Test
    public void testGetCountryShouldReturnNullIfNoCountryCodeisSpecified() {
	Country country = GeolocTestHelper.createCountryForFrance();
	Country savedCountry = this.countryDao.save(country);
	assertNotNull(savedCountry.getId());

	GisFeature gisFeature = GeolocTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);
	paris.setCountryCode(null);

	// save city
	City savedParis = this.cityDao.save(paris);

	// chek city is well saved
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());

	Country retrievedCountry = savedParis.getCountry();
	assertNull(retrievedCountry);

    }

    @Test
    public void testGetCountryShouldReturnNullIfUnknowCountryCode() {
	Country country = GeolocTestHelper.createCountryForFrance();
	Country savedCountry = this.countryDao.save(country);
	assertNotNull(savedCountry.getId());

	GisFeature gisFeature = GeolocTestHelper.createCity("cityGisFeature",
		null, null, new Random().nextLong());
	City paris = new City(gisFeature);
	paris.setCountryCode("ER");

	// save city
	City savedParis = this.cityDao.save(paris);

	// chek city is well saved
	City retrievedParis = this.cityDao.get(savedParis.getId());
	assertNotNull(retrievedParis);
	assertEquals(paris.getId(), retrievedParis.getId());

	Country retrievedCountry = savedParis.getCountry();
	assertNull(retrievedCountry);

    }

    @Test
    public void testDistanceShouldRetrunCorrectDistance() {
	GisFeature point1 = new GisFeature();
	point1.setLocation(GeolocTestHelper.createPoint(48.867F, 2.333F));

	GisFeature point2 = new GisFeature();
	point2.setLocation(GeolocTestHelper.createPoint(49.017F, 2.467F));

	assertEquals(Math.round(point1.distance(point2.getLocation())), Math
		.round(point2.distance(point1.getLocation())));
	assertEquals(22, Math
		.round(point2.distance(point1.getLocation()) / 1000));
    }

    public void testDistanceShouldHaveCorrectParameters() {
	GisFeature point1 = new GisFeature();
	point1.setLocation(GeolocTestHelper.createPoint(0F, 0F));

	try {
	    point1.distance(null);
	    fail("Distance for a null feature must throws");
	} catch (RuntimeException e) {
	}

	GisFeature point2 = new GisFeature();
	// point2.setLocation(this.geolocTestHelper.createPoint(1F, 1F));

	try {
	    point1.distance(point2.getLocation());
	    fail("Distance with a null location must throws");
	} catch (RuntimeException e) {
	}

	point1.setLocation(null);
	try {
	    point1.distance(point2.getLocation());
	    fail("Distance with a null location must throws");
	} catch (RuntimeException e) {
	}

    }

    @Test
    public void testPopulateAcityShouldsetZipCode() {
	City city1 = GeolocTestHelper.createCity("name", 1.5F, 1.6F, 2L);
	city1.setZipCode(10000);
	City city2 = new City();
	city2.populate(city1);
	assertEquals("Populate a city with a city should set the zipcode",
		city1.getZipCode(), city2.getZipCode());

    }
    
    @Test
    public void testToStringShouldContainsTheClassName() {
	City city1 = GeolocTestHelper.createCity("name", 1.5F, 1.6F, 2L);
	city1.setZipCode(10000);
	assertTrue(city1.toString().startsWith(City.class.getSimpleName()));
	

    }
    
    

    /**
     * @param cityDao
     *                the cityDao to set
     */
    @Required
    public void setCityDao(ICityDao cityDao) {
	this.cityDao = cityDao;
    }

    /**
     * @param countryDao
     *                the countryDao to set
     */
    @Required
    public void setCountryDao(ICountryDao countryDao) {
	this.countryDao = countryDao;
    }

}
