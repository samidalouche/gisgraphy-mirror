package com.gisgraphy.domain.geoloc.importer;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;


public class ImporterConfigTest {
    
    @Test
    public void testSetOpenStreetMapDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator(){
	String OpenStreetMapDir = "Test";
	ImporterConfig importerConfig = new ImporterConfig();
	importerConfig.setOpenStreetMapDir(OpenStreetMapDir);
	Assert.assertTrue("setOpenStreetMapDir should add File separator",importerConfig.getOpenStreetMapDir().endsWith(File.separator));
	Assert.assertEquals(OpenStreetMapDir+File.separator,importerConfig.getOpenStreetMapDir());
    }
    
    @Test
    public void testSetGeonamesDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator(){
	String geonamesDir = "Test";
	ImporterConfig importerConfig = new ImporterConfig();
	importerConfig.setGeonamesDir(geonamesDir);
	Assert.assertTrue("setGeonamesDir should add File separator",importerConfig.getGeonamesDir().endsWith(File.separator));
	Assert.assertEquals(geonamesDir+File.separator,importerConfig.getGeonamesDir());
    }
    
    @Test
    public void testIsGeonamesImporterShouldBeTrueByDefault(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue("Geonames importer should be enabled by default ",importerConfig.isGeonamesImporterEnabled());
    }
    
    @Test
    public void testIsOpenstreetmapImporterShouldBeTrueByDefault(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue("OpenStreetMap importer should be enabled by default ",importerConfig.isOpenstreetmapImporterEnabled());
    }

}
