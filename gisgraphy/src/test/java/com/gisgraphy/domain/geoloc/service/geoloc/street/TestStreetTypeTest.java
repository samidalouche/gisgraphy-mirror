package com.gisgraphy.domain.geoloc.service.geoloc.street;

import org.junit.Assert;
import org.junit.Test;

public class TestStreetTypeTest {

    @Test
    public void testGetFromString() {
	for (StreetType streetType: StreetType.values()){
	    Assert.assertEquals("getFromString should return correct values",StreetType.valueOf(streetType.toString()), StreetType.getFromString(streetType.toString()));
	    Assert.assertEquals("getFromString should be case sensitive",StreetType.valueOf(streetType.toString()), StreetType.getFromString(streetType.toString().toUpperCase()));
	    Assert.assertEquals("getFromString should be case sensitive",StreetType.valueOf(streetType.toString()), StreetType.getFromString(streetType.toString().toLowerCase()));
	}
	Assert.assertEquals("getFromString should be case sensitive",StreetType.valueOf(StreetType.UNCLASSIFIED.toString()), StreetType.getFromString("UNcLAsSIfIED"));
	Assert.assertNull("null should be return when wrong streettype is given",StreetType.getFromString("unknow"));
    }

}
