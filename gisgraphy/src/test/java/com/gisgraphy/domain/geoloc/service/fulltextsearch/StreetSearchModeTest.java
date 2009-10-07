package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import junit.framework.Assert;

import org.junit.Test;


public class StreetSearchModeTest {
	
	@Test
	public void getDefaultShouldReturnCorrectValue(){
		Assert.assertEquals("the default street search mode is not the expected one",StreetSearchMode.FULLTEXT, StreetSearchMode.getDefault());
	}

}
