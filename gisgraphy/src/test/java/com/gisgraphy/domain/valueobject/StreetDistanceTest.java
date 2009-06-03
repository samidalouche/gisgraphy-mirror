package com.gisgraphy.domain.valueobject;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.StreetDistance.StreetDistanceBuilder;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;


public class StreetDistanceTest {

    @Test
    public void builderShouldSetValuesAndUpdateCalculatedFields(){
	Point point = GeolocHelper.createPoint(45F, 56F);
	StreetDistance streetDistance = StreetDistanceBuilder.streetDistance()
	.withCountryCode("fr")
	.withDistance(3D)
	.withGid(123L)
	.withLength(124.4D)
	.withLocation(point)
	.withName("name")
	.withOneWay(true)
	.withStreetType(StreetType.FOOTWAY).build();
	Assert.assertEquals("countryCode Should be upperCased","FR",streetDistance.getCountryCode());
	Assert.assertEquals(3D,streetDistance.getDistance());
	Assert.assertEquals(123L,streetDistance.getGid().longValue());
	Assert.assertEquals(point,streetDistance.getLocation());
	Assert.assertEquals("name",streetDistance.getName());
	Assert.assertEquals(Boolean.TRUE,streetDistance.getOneWay());
	Assert.assertEquals(StreetType.FOOTWAY,streetDistance.getStreetType());
	Assert.assertEquals("calculated fields should be process",45F,streetDistance.getLng().floatValue());
	Assert.assertEquals("calculated fields should be process",56F,streetDistance.getLat().floatValue());
    }
    
}
