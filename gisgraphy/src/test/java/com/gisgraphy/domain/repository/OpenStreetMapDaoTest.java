package com.gisgraphy.domain.repository;

import java.util.List;

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
	
	
	OpenStreetMap streetOSM2 = new OpenStreetMap();
	String[] wktLineStrings2={"LINESTRING (30 30, 40 40)"};
	MultiLineString shape2 = GeolocHelper.createMultiLineString(wktLineStrings2);
	streetOSM2.setShape(shape2);
	streetOSM2.setGid(2L);
	streetOSM2.setOneWay("oneWay");
	streetOSM2.setStreetType("streetType2");
	openStreetMapDao.save(streetOSM2);
	assertNotNull(openStreetMapDao.get(streetOSM2.getId()));
	Point searchPoint = GeolocHelper.createPoint(30.1F, 30.1F);
	assertEquals(0,openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, "unknowstreetType", null).size());
	List<StreetDistance> nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 100000000, 1, 1, null, null);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	
    
    }


    /**
     * @param openStreetMapDao the openStreetMapDao to set
     */
    @Required
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }


    
}
