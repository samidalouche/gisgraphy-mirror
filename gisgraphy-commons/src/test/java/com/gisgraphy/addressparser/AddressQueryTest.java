package com.gisgraphy.addressparser;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.serializer.OutputFormat;


public class AddressQueryTest {
    
    @Test
    public void toStringWithNullProperty(){
	AddressQuery query =new AddressQuery("address","countrycode");
	query.toString();
    }
    
    @Test
    public void constructorWithFields(){
    	AddressQuery query =new AddressQuery("address","countrycode");
	Assert.assertEquals("address",query.getAddress());
	Assert.assertEquals("countrycode",query.getCountry());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorWithNullAddress(){
    	new AddressQuery(null,"countrycode");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorWithEmptyAddress(){
    	new AddressQuery(" ","countrycode");
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorWithNullCountrycode(){
    	new AddressQuery("address",null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorWithEmptyCountrycode(){
    	new AddressQuery("address"," ");
    }
    
    @Test
    public void toStringShouldContainsAllData(){
    AddressQuery query =new AddressQuery("foo","bar");
	query.setFormat(OutputFormat.XML);
	query.setIndent(true);
	query.setCallback("callfoo");
	String toString = query.toString();
	Assert.assertTrue(toString.contains("foo"));
	Assert.assertTrue(toString.contains("bar"));
	Assert.assertTrue(toString.contains("XML"));
	Assert.assertTrue(toString.contains("true"));
	Assert.assertTrue(toString.contains("callfoo"));
    }
    
    @Test
    public void defaultOutputFormat(){
    AddressQuery query =new AddressQuery("foo","bar");
   Assert.assertEquals(OutputFormat.getDefault(), query.getFormat());
    }
    
    @Test
    public void callbackNotAlphanumerique(){
    AddressQuery query =new AddressQuery("foo","bar");
    query.setCallback("doIt(");
   Assert.assertNull(query.getCallback());
    }
    
    @Test
    public void callbackAlphanumerique(){
    AddressQuery query =new AddressQuery("foo","bar");
    query.setCallback("doIt");
   Assert.assertEquals("doIt",query.getCallback());
    }
    
  

}
