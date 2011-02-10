package com.gisgraphy.addressparser;

import org.junit.Assert;
import org.junit.Test;


public class AddressTest {

    
    @Test
    public void setEmptyExtarinfoShouldNotSet(){
	Address address  =new Address();
	address.setExtraInfo(" ");
	Assert.assertNull(address.getExtraInfo());
	address.setExtraInfo("");
	Assert.assertNull(address.getExtraInfo());
	address.setExtraInfo(null);
	Assert.assertNull(address.getExtraInfo());
	
	address.setExtraInfo("foo");
	Assert.assertEquals("foo",address.getExtraInfo());
	
    }
}
