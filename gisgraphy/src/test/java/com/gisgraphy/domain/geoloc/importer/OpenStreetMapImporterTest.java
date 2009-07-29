package com.gisgraphy.domain.geoloc.importer;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;



public class OpenStreetMapImporterTest extends AbstractIntegrationHttpSolrTestCase {
    
    private IImporterProcessor openStreetMapImporter;
    
    public void testImporterShouldImport(){
	openStreetMapImporter.process();
	//todo osm test dao
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
    
    public void setOpenStreetMapImporter(IImporterProcessor openStreetMapImporter) {
        this.openStreetMapImporter = openStreetMapImporter;
    }

}
