package com.gisgraphy.domain.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;


public class OpenStreetMapDaoTest extends AbstractIntegrationHttpSolrTestCase{

 IOpenStreetMapDao openStreetMapDao;
    
 //TODO OSM unique Gid

    @Test
    public void testGetNearestAndDistanceFromShouldReturnValidDTO() {
	OpenStreetMap streetOSM = new OpenStreetMap();
	String[] wktLineStrings={"LINESTRING (10 10, 20 20)"};
	MultiLineString shape = GeolocHelper.createMultiLineString(wktLineStrings);
	streetOSM.setShape(shape);
	streetOSM.setGid(1L);
	streetOSM.setOneWay("oneWay");
	streetOSM.setStreetType("streetType");
	streetOSM.setStreetType("peter martin");
	openStreetMapDao.save(streetOSM);
	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
	
	OpenStreetMap streetOSM2 = new OpenStreetMap();
	String[] wktLineStrings2={"LINESTRING (30 30, 40 40)"};
	
	MultiLineString shape2 = GeolocHelper.createMultiLineString(wktLineStrings2);
	streetOSM2.setShape(shape2);
	streetOSM2.setGid(2L);
	//Simulate middle point
	streetOSM2.setLocation(GeolocHelper.createPoint(30.11F, 30.11F));
	streetOSM2.setOneWay("oneWay");
	streetOSM2.setStreetType("streetType2");
	streetOSM2.setName("John Kenedy");
	openStreetMapDao.save(streetOSM2);
	assertNotNull(openStreetMapDao.get(streetOSM2.getId()));
	
	Point searchPoint = GeolocHelper.createPoint(30.1F, 30.1F);
	
	List<StreetDistance> nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,null, null);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	//test distance
	Assert.assertEquals(GeolocHelper.distance(searchPoint, nearestStreet.get(0).getLocation()), nearestStreet.get(0).getDistance().longValue(),5);
	
	//test streettype
	assertEquals(0,openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, "unknowstreetType",null, null).size());
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, "streetType2",null, null);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	//test name
	assertEquals(0,openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,null, "unknow name").size());
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"John");
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"john keN");
	assertEquals("the name should be case insensitive",1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"keN");
	assertEquals("only the beginning of the street name should match",0,nearestStreet.size());
	
	//test OneWay
	assertEquals(0,openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,"twoWay", null).size());
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,"oneWay",null);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	//test pagination
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 2, 2, null,null, null);
	assertEquals(0,nearestStreet.size());
	
	
    
    }


    /**
     * @param openStreetMapDao the openStreetMapDao to set
     */
    @Required
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }


    
}
