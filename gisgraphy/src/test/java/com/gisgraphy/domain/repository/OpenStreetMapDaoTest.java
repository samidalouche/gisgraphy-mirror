package com.gisgraphy.domain.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.MultiLineString;

import junit.framework.TestCase;


public class OpenStreetMapDaoTest extends AbstractIntegrationHttpSolrTestCase{

 IOpenStreetMapDao openStreetMapDao;
    

    @Test
    public void testGetNearestAndDistanceFromShouldReturnValidDTO() {
	OpenStreetMap streetOSM = new OpenStreetMap();
	String[] wktLineStrings={"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40 40, 50 50)"};
	MultiLineString shape = GeolocHelper.createMultiLineStringFromString(wktLineStrings);
	streetOSM.setShape(shape);
	streetOSM.setGid(1L);
	streetOSM.setOneWay("oneWay");
	streetOSM.setStreetType("streetType");
	openStreetMapDao.save(streetOSM);
	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
	
	
    
    }


    /**
     * @param openStreetMapDao the openStreetMapDao to set
     */
    @Required
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }


    
}
