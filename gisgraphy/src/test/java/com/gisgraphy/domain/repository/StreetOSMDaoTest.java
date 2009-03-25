package com.gisgraphy.domain.repository;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.StreetOSM;
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.MultiLineString;

public class StreetOSMDaoTest extends AbstractTransactionalTestCase{
    
    IStreetOSMDao streetOSMDao;
    

    @Test
    public void testGetNearestAndDistanceFromShouldReturnValidDTO() {
	StreetOSM streetOSM = new StreetOSM();
	String[] wktLineStrings={"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40 40, 50 50)"};
	MultiLineString shape = GeolocHelper.createMultiLineStringFromString(wktLineStrings);
	streetOSM.setShape(shape);
	streetOSM.setGid(1L);
	streetOSM.setOneWay("oneWay");
	streetOSM.setStreetType("streetType");
	streetOSMDao.save(streetOSM);
	assertNotNull(streetOSMDao.get(streetOSM.getId()));
	
	
    
    }


    /**
     * @param streetOSMDao the streetOSMDao to set
     */
    public void setStreetOSMDao(StreetOSMDao streetOSMDao) {
        this.streetOSMDao = streetOSMDao;
    }

}
