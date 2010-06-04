package com.gisgraphy.domain.geoloc.importer;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.repository.AbstractTransactionalTestCase;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.ImporterStatusDto;

public class GeonamesDatabaseIndexerTest extends AbstractTransactionalTestCase {
    
    public GeonamesDatabaseIndexer geonamesDatabaseIndexer;
    
    @Test
   public void testProcess(){
	geonamesDatabaseIndexer.process();
	assertEquals("statusMessage should be empty if the process is ok","", geonamesDatabaseIndexer.getStatusMessage());
	ImporterStatusDto status = new ImporterStatusDto(geonamesDatabaseIndexer);
	assertEquals(100, status.getPercent());
	assertNull("curentFileName should be null at the end of the process", geonamesDatabaseIndexer.getCurrentFileName());
	Assert.assertEquals("curentFileName should be null at the end of the process",GeonamesDatabaseIndexer.DEFAULT_CURRENT_FILENAME, status.getCurrentFileName());
    }
    
    @Test
    public void testShouldBeSkiped(){
	ImporterConfig importerConfig = new ImporterConfig();
	GeonamesDatabaseIndexer geonamesDatabaseIndexerTobeSkipped = new GeonamesDatabaseIndexer();
	geonamesDatabaseIndexerTobeSkipped.setImporterConfig(importerConfig);
	
	importerConfig.setGeonamesImporterEnabled(false);
	Assert.assertTrue(geonamesDatabaseIndexerTobeSkipped.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(true);
	Assert.assertFalse(geonamesDatabaseIndexerTobeSkipped.shouldBeSkipped());
    }

    
    public void setGeonamesDatabaseIndexer(GeonamesDatabaseIndexer geonamesDatabaseIndexer) {
        this.geonamesDatabaseIndexer = geonamesDatabaseIndexer;
    }
    
    @Test
    public void testResetStatusShouldReset() {
	GeonamesDatabaseIndexer indexer = new GeonamesDatabaseIndexer() {
			@Override
			protected void setup() {
				throw new RuntimeException();
			}
		};
		try {
			indexer.process();
			fail("The GeonamesDatabaseIndexer should have throws");
		} catch (RuntimeException ignore) {
		}
		Assert.assertTrue(indexer.getStatusMessage().length() > 0);
		Assert.assertEquals(ImporterStatus.ERROR, indexer.getStatus());
		Assert.assertNotNull("curentFileName should not be null if the process fail", indexer.getCurrentFileName());
		indexer.resetStatus();
		Assert.assertEquals(0, indexer.getNumberOfLinesToProcess());
		Assert.assertEquals(0, indexer.getTotalReadLine());
		Assert.assertEquals(0, indexer.getReadFileLine());
		Assert.assertEquals(ImporterStatus.WAITING, indexer.getStatus());
		Assert.assertEquals("", indexer.getStatusMessage());
		Assert.assertEquals("curentFileName should be null at the end of the reset",GeonamesDatabaseIndexer.DEFAULT_CURRENT_FILENAME, indexer.getCurrentFileName());
	}

}
