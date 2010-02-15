package com.gisgraphy.domain.geoloc.importer;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.repository.AbstractTransactionalTestCase;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.repository.OpenStreetMapDao;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.NameValueDTO;

public class OpenstreetmapDatabaseIndexerTest extends AbstractTransactionalTestCase {
    
    public IImporterProcessor openstreetmapDatabaseIndexer;
    
    @Test
    public void testProcess(){
	openstreetmapDatabaseIndexer.process();
	assertEquals("statusMessage should be empty if the process is ok","", openstreetmapDatabaseIndexer.getStatusMessage());
    }
    
    
    
    @Test
    public void testShouldBeSkiped(){
	ImporterConfig importerConfig = new ImporterConfig();
	OpenstreetmapDatabaseIndexer openstreetmapDatabaseIndexerTobeSkipped = new OpenstreetmapDatabaseIndexer();
	openstreetmapDatabaseIndexerTobeSkipped.setImporterConfig(importerConfig);
	
	importerConfig.setOpenstreetmapImporterEnabled(false);
	Assert.assertTrue(openstreetmapDatabaseIndexerTobeSkipped.shouldBeSkipped());
	
	importerConfig.setOpenstreetmapImporterEnabled(true);
	Assert.assertFalse(openstreetmapDatabaseIndexerTobeSkipped.shouldBeSkipped());
    }

  

    @Test
    public void testRollback() throws Exception {
    	List<NameValueDTO<Integer>> dtoList = openstreetmapDatabaseIndexer.rollback();
    	Assert.assertNotNull(dtoList);
		Assert.assertEquals(0, dtoList.size());
	}
    
    @Test
    public void testGetCurrentFileNameShouldReturnTheClassName(){
	OpenstreetmapDatabaseIndexer openstreetmapDatabaseIndexerToTest = new OpenstreetmapDatabaseIndexer();
	openstreetmapDatabaseIndexerToTest.setOpenStreetMapDao(new OpenStreetMapDao());
	assertEquals(OpenStreetMap.class.getSimpleName(), openstreetmapDatabaseIndexerToTest.getCurrentFileName());
    }
    
    @Test
    public void testResetStatusShouldReset(){
	OpenstreetmapDatabaseIndexer openstreetmapDatabaseIndexerToTest = new OpenstreetmapDatabaseIndexer(){
	    @Override
	    protected void setup() {
	        throw new RuntimeException();
	    }
	};
	openstreetmapDatabaseIndexerToTest.process();
	Assert.assertTrue(openstreetmapDatabaseIndexerToTest.getStatusMessage().length()>0);
	Assert.assertEquals(ImporterStatus.ERROR,openstreetmapDatabaseIndexerToTest.getStatus());
	openstreetmapDatabaseIndexerToTest.resetStatus();
	Assert.assertTrue(openstreetmapDatabaseIndexerToTest.getStatusMessage().length() ==0);
	Assert.assertEquals(ImporterStatus.WAITING,openstreetmapDatabaseIndexerToTest.getStatus());
    }
    
    public void setOpenstreetmapDatabaseIndexer(OpenstreetmapDatabaseIndexer openstreetmapDatabaseIndexer) {
        this.openstreetmapDatabaseIndexer = openstreetmapDatabaseIndexer;
    }
    

}
