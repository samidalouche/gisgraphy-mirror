package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import junit.framework.Assert;

import org.junit.Test;


public class StreetSearchModeTest {
	
	@Test
	public void getDefaultShouldReturnCorrectValue(){
		Assert.assertEquals("the default street search mode is not the expected one",StreetSearchMode.FULLTEXT, StreetSearchMode.getDefault());
	}
	
	@Test
	public void geFromString(){
		Assert.assertEquals("the default street search mode must be return if searchmode can not be determined ",StreetSearchMode.getDefault(), StreetSearchMode.getFromString("foo"));
		Assert.assertEquals("getFromString ShouldBe case insensitive",StreetSearchMode.CONTAINS, StreetSearchMode.getFromString("conTaIns"));
		Assert.assertEquals("getFromString ShouldBe case insensitive",StreetSearchMode.FULLTEXT, StreetSearchMode.getFromString("fulltext"));
	}

}
