/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.domain.repository;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.StringHelper;
import com.gisgraphy.test.GeolocTestHelper;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;


public class OpenStreetMapDaoTest extends AbstractIntegrationHttpSolrTestCase{

 IOpenStreetMapDao openStreetMapDao;
    
 @Test
 public void testCouldNotSaveNonUniqueGID(){
     OpenStreetMap streetOSM = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	openStreetMapDao.save(streetOSM);
	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
	
	OpenStreetMap streetOSM2 = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	try {
	    openStreetMapDao.save(streetOSM2);
	    openStreetMapDao.flushAndClear();
	    fail("we should not save street with non unique GID");
	} catch (DataIntegrityViolationException e) {
	  assertTrue("a ConstraintViolationException should be throw when saving an openstreetmap with a non unique gid ",e.getCause() instanceof ConstraintViolationException);
	}
	
	
 }
 
 
  @Test
  public void testGetNearestAndDistanceFromShouldNotAcceptNullStreetSearchModeIfNameIsNotNull() {
	 try {
		openStreetMapDao.getNearestAndDistanceFrom(GeolocHelper.createPoint(30.1F, 30.1F), 10000, 1, 1, null, null,"john keN",null);
		fail("getNearestAndDistanceFrom should not accept a null streetSearchmode if name is not null");
	} catch (IllegalArgumentException e) {
		//ok
	}
 }
 
    @Test
    public void testGetNearestAndDistanceFromShouldReturnValidDTO() {
	OpenStreetMap streetOSM = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	openStreetMapDao.save(streetOSM);
	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
	
	OpenStreetMap streetOSM2 = new OpenStreetMap();
	String[] wktLineStrings2={"LINESTRING (30 30, 40 40)"};
	
	MultiLineString shape2 = GeolocHelper.createMultiLineString(wktLineStrings2);
	streetOSM2.setShape(shape2);
	streetOSM2.setGid(2L);
	//Simulate middle point
	streetOSM2.setLocation(GeolocHelper.createPoint(30.11F, 30.11F));
	streetOSM2.setOneWay(false);
	streetOSM2.setStreetType(StreetType.FOOTWAY);
	streetOSM2.setName("John Kenedy");
	StringHelper.updateOpenStreetMapEntityForIndexation(streetOSM2);
	openStreetMapDao.save(streetOSM2);
	assertNotNull(openStreetMapDao.get(streetOSM2.getId()));
	
	int numberOfLineUpdated = openStreetMapDao.updateTS_vectorColumnForStreetNameSearch();
	assertEquals("It should have 4 lines updated : (streetosm +streetosm2) for partial + (streetosm +streetosm2) for fulltext",4, numberOfLineUpdated);
	
	
	Point searchPoint = GeolocHelper.createPoint(30.1F, 30.1F);
	
	List<StreetDistance> nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,null, null,null);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	//test distance
	Assert.assertEquals(GeolocHelper.distance(searchPoint, nearestStreet.get(0).getLocation()), nearestStreet.get(0).getDistance().longValue(),5);
	
	//test streettype
	assertEquals(0,openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, StreetType.UNCLASSIFIED,null, null,null).size());
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, StreetType.FOOTWAY,null, null,null);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	//test name in full text
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"keN",StreetSearchMode.FULLTEXT);
	assertEquals("the street name should not match if a part of the name is given and street search mode is "+StreetSearchMode.FULLTEXT,0,nearestStreet.size());
	
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"Kenedy",StreetSearchMode.FULLTEXT);
	assertEquals("the street name should  match if a name is given with an entire word and street search mode is "+StreetSearchMode.FULLTEXT,1,nearestStreet.size());
	
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"Kenedy john",StreetSearchMode.FULLTEXT);
	assertEquals("the street name should match if a name is given with more than one entire word and street search mode is "+StreetSearchMode.FULLTEXT,1,nearestStreet.size());
	
	
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"Kenedy smith",StreetSearchMode.FULLTEXT);
	assertEquals("the street name should not match if only one word is given and street search mode is "+StreetSearchMode.FULLTEXT,0,nearestStreet.size());
	
	
	//test name with contains
	assertEquals(0,openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,null, "unknow name",StreetSearchMode.CONTAINS).size());
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"John",StreetSearchMode.CONTAINS);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"john keN",StreetSearchMode.CONTAINS);
	assertEquals("the name should be case insensitive",1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"keN",StreetSearchMode.CONTAINS);
	assertEquals("the street name should match if a part of the name is given and street search mode is "+StreetSearchMode.CONTAINS,1,nearestStreet.size());

	
	//test OneWay
	assertEquals(0,openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,true, null,null).size());
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null,false,null,null);
	assertEquals(1,nearestStreet.size());
	assertEquals(streetOSM2.getGid(),nearestStreet.get(0).getGid());
	
	//test pagination
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 2, null,null, null,null);
	assertEquals(2,nearestStreet.size());
	
	//test Order
	nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 2, null,null, null,null);
	assertEquals(2,nearestStreet.size());
	Double firstDist = nearestStreet.get(0).getDistance();
	Double secondDist = nearestStreet.get(1).getDistance();
	assertTrue("result should be sorted by distance : "+firstDist +"  should be < " +secondDist ,firstDist < secondDist);
    
    }


   


   
    
    @Test
    public void testGetByGidShouldRetrieveIfEntityExists(){
	OpenStreetMap streetOSM = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	openStreetMapDao.save(streetOSM);
	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
	
	OpenStreetMap retrieveOSM = openStreetMapDao.getByGid(streetOSM.getGid());
	assertNotNull("getByGid should not return null if the entity exists",retrieveOSM);
	assertEquals("getByGid should return the entity if the entity exists",streetOSM, retrieveOSM);
	
    }
    
    @Test
    public void testSaveShouldsaveLongName(){
	OpenStreetMap streetOSM = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	String longString = RandomStringUtils.random(StringHelper.MAX_STRING_INDEXABLE_LENGTH+1,new char[] {'e'});
	Assert.assertEquals("the string to test is not of the expected size the test will fail",StringHelper.MAX_STRING_INDEXABLE_LENGTH+1, longString.length());
	streetOSM.setName(longString);
	openStreetMapDao.save(streetOSM);
	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
	
	OpenStreetMap retrieveOSM = openStreetMapDao.getByGid(streetOSM.getGid());
	assertNotNull("getByGid should not return null if the entity exists",retrieveOSM);
	assertEquals("getByGid should return the entity if the entity exists",streetOSM, retrieveOSM);
	
    }
    
    @Test
    public void testGetByGidShouldReturnNullIfEntityDoesnTExist(){
	OpenStreetMap retrieveOSM = openStreetMapDao.getByGid(1L);
	assertNull("getByGid should return null if the entity doesn't exist",retrieveOSM);
	
    }
    
    @Test
    public void testGetByGidShouldThrowsIfGidIsNull(){
	try {
	    openStreetMapDao.getByGid(null);
	    fail("getByGid should throws if gid is null");
	} catch (RuntimeException e) {
	    	//ok
	}
	
    }
    


    
    @Test
    public void testUpdateTS_vectorColumnForStreetNameSearch(){
    	OpenStreetMap streetOSM = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
    	openStreetMapDao.save(streetOSM);
    	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
    	
    	
    	    	
    	Point searchPoint = GeolocHelper.createPoint(30.1F, 30.1F);
    	
    	List<StreetDistance> nearestStreet = openStreetMapDao.getNearestAndDistanceFrom(searchPoint, 10000, 1, 1, null, null,"John",StreetSearchMode.CONTAINS);
    	assertEquals(0,nearestStreet.size());
    	
    	int numberOfLineUpdated = openStreetMapDao.updateTS_vectorColumnForStreetNameSearch();
    	assertEquals("It should have 2 lines updated : (streetosm +streetosm2) for partial + (streetosm +streetosm2) for fulltext",2, numberOfLineUpdated);
    	
    }
    
    @Test
    public void testCreateIndexesShouldNotThrow(){
    	openStreetMapDao.createIndexes();
    }
    

    
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }
    
}
