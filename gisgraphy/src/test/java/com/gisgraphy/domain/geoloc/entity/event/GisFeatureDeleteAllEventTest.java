package com.gisgraphy.domain.geoloc.entity.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.test.GeolocTestHelper;


public class GisFeatureDeleteAllEventTest {

    @Test
    public void GisFeatureDeleteAllEventCouldNothaveNullList() {
	try {
	    new GisFeatureDeleteAllEvent(null);
	    fail("we should not be able to create a GisFeatureDeleteAllEvent with a null List");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void GetGisFeatureShouldReturnTheConstructorOne() {
	GisFeature gisFeature = GeolocTestHelper.createGisFeature("name", 3F,
		4F, 2L);
	List<GisFeature> list = new ArrayList<GisFeature>();
	list.add(gisFeature);
	assertEquals("GetGisFeatures() should return the constructor one",
		gisFeature, new GisFeatureDeleteAllEvent(list)
			.getGisFeatures().get(0));
	
    }
    
}
