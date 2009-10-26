package com.gisgraphy.domain.geoloc.importer;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.repository.AbstractTransactionalTestCase;
import com.gisgraphy.domain.valueobject.NameValueDTO;

public class OpenstreetmapDatabaseIndexerTest extends AbstractTransactionalTestCase {
    
    public IImporterProcessor openstreetmapDatabaseIndexer;
    
    @Test
    public void testProcess(){
	openstreetmapDatabaseIndexer.process();
	assertEquals("statusMessage should be null if the process is ok","", openstreetmapDatabaseIndexer.getStatusMessage());
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

    public void setOpenstreetmapDatabaseIndexer(OpenstreetmapDatabaseIndexer openstreetmapDatabaseIndexer) {
        this.openstreetmapDatabaseIndexer = openstreetmapDatabaseIndexer;
    }

    @Test
    public void testRollback() throws Exception {
    	List<NameValueDTO<Integer>> dtoList = openstreetmapDatabaseIndexer.rollback();
    	Assert.assertNotNull(dtoList);
		Assert.assertEquals(0, dtoList.size());
	}
    
    
    

}
