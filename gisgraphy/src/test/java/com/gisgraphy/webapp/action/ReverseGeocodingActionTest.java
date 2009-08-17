package com.gisgraphy.webapp.action;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.GisgraphyConfig;


public class ReverseGeocodingActionTest {
    
    @Test
    public void testGetGoogleMapAPIKey(){
	 String savedGoogleMapAPIKey  = GisgraphyConfig .googleMapAPIKey;
	try {
           GeocodingAction action = new GeocodingAction();
	   GisgraphyConfig.googleMapAPIKey = null;
	   Assert.assertEquals("getGoogleMapAPIKey should return an empty string if the config value is null ","", action.getGoogleMapAPIKey());
	   String newKey = "new key";
	GisgraphyConfig.googleMapAPIKey=newKey;
	   Assert.assertEquals("getGoogleMapAPIKey should return the config value ",newKey, action.getGoogleMapAPIKey());
	   
	} finally {
	    GisgraphyConfig.googleMapAPIKey = savedGoogleMapAPIKey;
	}
    }

}
