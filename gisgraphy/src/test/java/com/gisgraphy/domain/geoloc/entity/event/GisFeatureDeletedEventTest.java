package com.gisgraphy.domain.geoloc.entity.event;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.test.GeolocTestHelper;

public class GisFeatureDeletedEventTest {

    @Test
    public void testGisFeatureDeletedEventCouldNothaveNullGisFeature() {
	try {
	    new GisFeatureDeletedEvent(null);
	    fail("we should not be able to create a gisFeatureDeletedEvent with a null gisFeature");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void testGetGisFeatureShouldReturnTheConstructorOne() {
	GisFeature gisFeature = GeolocTestHelper.createGisFeature("name", 3F,
		4F, 2L);
	assertEquals("GetGisFeature() should return the constructor one",
		gisFeature, new GisFeatureDeletedEvent(gisFeature)
			.getGisFeature());
    }
}
