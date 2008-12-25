package com.gisgraphy.domain.geoloc.entity.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.test.GeolocTestHelper;


public class GisFeatureStoredEventTest {
    
    @Test
    public void GisFeatureStoredEventCouldNothaveNullGisFeature() {
	try {
	    new GisFeatureStoredEvent(null);
	    fail("we should not be able to create a gisFeatureStoredEvent with a null gisFeature");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void GetGisFeatureShouldReturnTheConstructorOne() {
	GisFeature gisFeature = GeolocTestHelper.createGisFeature("name", 3F,
		4F, 2L);
	assertEquals("GetGisFeature() should return the constructor one",
		gisFeature, new GisFeatureStoredEvent(gisFeature)
			.getGisFeature());
    }

}
