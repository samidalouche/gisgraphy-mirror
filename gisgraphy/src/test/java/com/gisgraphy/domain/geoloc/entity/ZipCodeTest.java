package com.gisgraphy.domain.geoloc.entity;

import junit.framework.Assert;

import org.junit.Test;


public class ZipCodeTest {

	private String code ="code";
	
	@Test
	public void testToString(){
		ZipCode zipCode = new ZipCode(code);
		Assert.assertEquals("toString should return the code ",code, zipCode.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testZipCodeMustHaveACode(){
		new ZipCode(null);
	}
	
	@Test
	public void testEquals(){
		ZipCode zipcode1 = new ZipCode(code);
		ZipCode zipcodeEquals = new ZipCode(code);
		ZipCode zipcodeNotEquals = new ZipCode(code+code);
		Assert.assertTrue("Equals must be base on the code",zipcode1.equals(zipcodeEquals));
		Assert.assertFalse("Equals must be base on the code",zipcode1.equals(zipcodeNotEquals));
		
	}
}
