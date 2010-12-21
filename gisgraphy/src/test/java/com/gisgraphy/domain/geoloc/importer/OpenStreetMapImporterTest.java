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
package com.gisgraphy.domain.geoloc.importer;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.service.IInternationalisationService;
import com.vividsolutions.jts.geom.Point;



public class OpenStreetMapImporterTest extends AbstractIntegrationHttpSolrTestCase {
    
    private OpenStreetMapImporter openStreetMapImporter;
    
    private IOpenStreetMapDao openStreetMapDao;
    
    static boolean setupIsCalled = false;
    
   
  
    @Test
    public void testRollback() throws Exception {
    	OpenStreetMapImporter openStreetMapImporter = new OpenStreetMapImporter();
    	IOpenStreetMapDao openStreetMapDao = EasyMock.createMock(IOpenStreetMapDao.class);
    	EasyMock.expect(openStreetMapDao.getPersistenceClass()).andReturn(OpenStreetMap.class);
    	EasyMock.expect(openStreetMapDao.deleteAll()).andReturn(5);
    	EasyMock.replay(openStreetMapDao);
    	openStreetMapImporter.setOpenStreetMapDao(openStreetMapDao);
    	List<NameValueDTO<Integer>> deleted = openStreetMapImporter
    		.rollback();
    	assertEquals(1, deleted.size());
    	assertEquals(5, deleted.get(0).getValue().intValue());
    	assertEquals("generatedId should be reset to 0",0L, OpenStreetMapImporter.generatedId,0.01);
	}
    
    @Test
    public void testImporterShouldImport(){
	OpenStreetMapImporter.generatedId = 0L;
	openStreetMapImporter.process();
	assertEquals(4L,openStreetMapDao.count());
	OpenStreetMap openStreetMap = openStreetMapDao.getByGid(1L);
	assertTrue("The oneWay attribute is not correct",openStreetMap.getOneWay());
	assertEquals("The countryCode is not correct ","FR",openStreetMap.getCountryCode());
	assertEquals("The streetType is not correct",StreetType.RESIDENTIAL, openStreetMap.getStreetType());
	assertEquals("The name is not correct","Bachlettenstrasse", openStreetMap.getName());
	assertEquals("The location->X is not correct ",((Point)GeolocHelper.convertFromHEXEWKBToGeometry("010100000006C82291A0521E4054CC39B16BC64740")).getX(), openStreetMap.getLocation().getX());
	assertEquals("The location->Y is not correct ",((Point)GeolocHelper.convertFromHEXEWKBToGeometry("010100000006C82291A0521E4054CC39B16BC64740")).getY(), openStreetMap.getLocation().getY());
	assertEquals("The length is not correct",0.00142246604529, openStreetMap.getLength());
	assertEquals("The shape is not correct ",GeolocHelper.convertFromHEXEWKBToGeometry("01020000000200000009B254CD6218024038E22428D9EF484075C93846B217024090A8AB96CFEF4840").toString(), openStreetMap.getShape().toString());
    }

   
    
    @Test
    public void testShouldBeSkipShouldReturnCorrectValue(){
	ImporterConfig importerConfig = new ImporterConfig();
	OpenStreetMapImporter openStreetMapImporter = new OpenStreetMapImporter();
	openStreetMapImporter.setImporterConfig(importerConfig);
	
	importerConfig.setOpenstreetmapImporterEnabled(false);
	Assert.assertTrue(openStreetMapImporter.shouldBeSkipped());
	
	importerConfig.setOpenstreetmapImporterEnabled(true);
	Assert.assertFalse(openStreetMapImporter.shouldBeSkipped());
		
    }
    
   
    
    @Test
    public void testProcessLineWithBadShapeShouldNotTryToSaveLine(){
	String line = "		010100000029F2C9850F79E4BFFCAEFE473CE14740	19406.7343711266	FR	8257014	road	false	BADSHAPE";
	OpenStreetMapImporter importer = new OpenStreetMapImporter();
	IOpenStreetMapDao dao = EasyMock.createMock(IOpenStreetMapDao.class);
	//now we simulate the fact that the dao should not be called
	EasyMock.expect(dao.save((OpenStreetMap)EasyMock.anyObject())).andThrow(new RuntimeException());
	EasyMock.replay(dao);
	importer.setOpenStreetMapDao(dao);
	importer.processData(line);
    }
    
    @Test
    public void testImportWithErrors(){
	OpenStreetMapImporter importer = createImporterThatThrows();
	try {
	    importer.process();
	    fail("The import should have failed");
	} catch (Exception ignore) {
	    //ok
	}
	Assert.assertNotNull("status message is not set",importer.getStatusMessage() );
	Assert.assertFalse("status message should not be empty",importer.getStatusMessage().trim().length()==0 );
	Assert.assertEquals(ImporterStatus.ERROR, importer.getStatus());
    }

    private OpenStreetMapImporter createImporterThatThrows() {
	OpenStreetMapImporter importer = new OpenStreetMapImporter(){
	    @Override
	    public boolean shouldBeSkipped() {
	        return false;
	    }
	    
	    @Override
	    public long getNumberOfLinesToProcess() {
	        return 2L;
	    }
	    
	    @Override
	    protected void tearDown() {
	       return;
	    }
	};
	
	//ImporterConfig config = new ImporterConfig();
	//config.setOpenStreetMapDir(this.openStreetMapImporter.importerConfig.getOpenStreetMapDir());
	IOpenStreetMapDao dao = EasyMock.createNiceMock(IOpenStreetMapDao.class);
	//now we simulate the fact that the dao should not be called
	EasyMock.expect(dao.save((OpenStreetMap)EasyMock.anyObject())).andThrow(new RuntimeException("message"));
	EasyMock.replay(dao);
	importer.setOpenStreetMapDao(dao);
	importer.setImporterConfig(this.openStreetMapImporter.importerConfig);
	importer.setTransactionManager(openStreetMapImporter.transactionManager);
	return importer;
    }
    
    @Test
    public void testSetup(){
	Long savedGeneratedId = OpenStreetMapImporter.generatedId;
    	OpenStreetMapImporter.generatedId = 1000L;
    	
    	IOpenStreetMapDao openStreetMapDao = createMock(IOpenStreetMapDao.class);
    	openStreetMapDao.createSpatialIndexes();
    	replay(openStreetMapDao);
    	
    	IInternationalisationService internationalisationService = createMock(IInternationalisationService.class);
    	expect(internationalisationService.getString((String)anyObject())).andStubReturn("localizedString");
    	replay(internationalisationService);
    	
    	
    	OpenStreetMapImporter importer = new OpenStreetMapImporter();
    	importer.setOpenStreetMapDao(openStreetMapDao);
    	importer.setInternationalisationService(internationalisationService);
    	
    	importer.setup();
    	long generatedId = OpenStreetMapImporter.generatedId;
    	assertEquals("The generatedId should be reset to 0 before import",0L, generatedId);
    	assertEquals("statusMessage should be set to empty string at the end  of the setup method","", importer.getStatusMessage());
    	
    	
    	EasyMock.verify(openStreetMapDao);
    	
    	//restore the last value
    	OpenStreetMapImporter.generatedId = savedGeneratedId;
    }
    
    @Test
    public void testSetupIsCalled(){
    	
    	OpenStreetMapImporterTest.setupIsCalled = false;
    	OpenStreetMapImporter importer = new OpenStreetMapImporter(){
    		@Override
    		protected void setup() {
    			OpenStreetMapImporterTest.setupIsCalled = true;
    		}
    		@Override
    		protected void tearDown() {
    			return;
    		}
    		
    		@Override
    		public long getNumberOfLinesToProcess() {
    			return 0;
    		}
    		
    		@Override
    		public boolean shouldBeSkipped() {
    			return false;
    		}
    		
    		@Override
    		protected File[] getFiles() {
    			return new File[]{};
    		}
    	};
    	importer.process();
    	assertTrue(OpenStreetMapImporterTest.setupIsCalled);
    }
    
    
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }

    
    public void setOpenStreetMapImporter(OpenStreetMapImporter openStreetMapImporter) {
        this.openStreetMapImporter = openStreetMapImporter;
    }
    
    @Test
    public void testTearDown(){
	if (GisgraphyConfig.PARTIAL_SEARH_EXPERIMENTAL){
	OpenStreetMapImporter importer = new OpenStreetMapImporter(){
	    //simulate an error
	    public boolean shouldBeSkipped() {throw new RuntimeException("errormessage");};
	};
	IOpenStreetMapDao dao = createMock(IOpenStreetMapDao.class);
	dao.clearPartialSearchName();
	EasyMock.replay(dao);
	importer.setOpenStreetMapDao(dao);
	
	IInternationalisationService internationalisationService = createMock(IInternationalisationService.class);
    	expect(internationalisationService.getString("import.openstreetmap.cleanDatabase")).andStubReturn("localizedString");
    	replay(internationalisationService);
    	importer.setInternationalisationService(internationalisationService);
	
	    try {
		importer.process();
	    } catch (Exception ignore) {
		
	    }
	    Assert.assertTrue(importer.getStatusMessage().contains("errormessage"));
	    org.easymock.EasyMock.verify(dao);
    }
    }
}
