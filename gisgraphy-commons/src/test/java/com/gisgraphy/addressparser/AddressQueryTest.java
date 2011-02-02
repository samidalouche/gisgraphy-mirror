package com.gisgraphy.addressparser;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.serializer.OutputFormat;


public class AddressQueryTest {
    
    @Test
    public void toStringWithNullProperty(){
	AddressQuery query =new AddressQuery();
	query.toString();
    }
    
    @Test
    public void toStringShouldContainsAllData(){
	AddressQuery query =new AddressQuery();
	query.setAddress("foo");
	query.setCountry("bar");
	query.setOutputFormat(OutputFormat.XML);
	query.setIndent(true);
	query.setCallback("callfoo");
	String toString = query.toString();
	Assert.assertTrue(toString.contains("foo"));
	Assert.assertTrue(toString.contains("bar"));
	Assert.assertTrue(toString.contains("XML"));
	Assert.assertTrue(toString.contains("true"));
	Assert.assertTrue(toString.contains("callfoo"));
    }

}
