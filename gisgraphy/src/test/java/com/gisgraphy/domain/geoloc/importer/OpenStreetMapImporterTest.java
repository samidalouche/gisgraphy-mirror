package com.gisgraphy.domain.geoloc.importer;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;



public class OpenStreetMapImporterTest extends AbstractIntegrationHttpSolrTestCase {
    
    private IGeonamesProcessor openStreetMapImporter;
    
    public void testImporterShouldImport(){
	openStreetMapImporter.process();
    }

    public void setOpenStreetMapImporter(IGeonamesProcessor openStreetMapImporter) {
        this.openStreetMapImporter = openStreetMapImporter;
    }

}
