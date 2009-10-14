package com.gisgraphy.domain.geoloc.importer;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.repository.AbstractTransactionalTestCase;

public class OpenstreetmapDatabaseIndexerTest extends AbstractTransactionalTestCase {
    
    public OpenstreetmapDatabaseIndexer openstreetmapDatabaseIndexer;
    
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
	
	importerConfig.setGeonamesImporterEnabled(false);
	Assert.assertTrue(openstreetmapDatabaseIndexerTobeSkipped.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(true);
	Assert.assertFalse(openstreetmapDatabaseIndexerTobeSkipped.shouldBeSkipped());
    }

    public void setOpenstreetmapDatabaseIndexer(OpenstreetmapDatabaseIndexer openstreetmapDatabaseIndexer) {
        this.openstreetmapDatabaseIndexer = openstreetmapDatabaseIndexer;
    }

    
    
    
    

}
