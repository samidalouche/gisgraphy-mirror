package com.gisgraphy.domain.geoloc.importer;

import java.io.File;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.helper.FileHelper;


public class GeonamesAlternateNamesExtracterTest {
	
	  @Test
	    public void testShouldBeSkipShouldReturnCorrectValue(){
		ImporterConfig importerConfig = new ImporterConfig();
		GeonamesAlternateNamesExtracter extracter = new GeonamesAlternateNamesExtracter();
		extracter.setImporterConfig(importerConfig);
		
		importerConfig.setGeonamesImporterEnabled(false);
		importerConfig.setImportGisFeatureEmbededAlternateNames(false);
		Assert.assertTrue(extracter.shouldBeSkipped());
		
		importerConfig.setGeonamesImporterEnabled(false);
		importerConfig.setImportGisFeatureEmbededAlternateNames(true);
		Assert.assertTrue(extracter.shouldBeSkipped());
		
		importerConfig.setGeonamesImporterEnabled(true);
		importerConfig.setImportGisFeatureEmbededAlternateNames(false);
		Assert.assertFalse(extracter.shouldBeSkipped());
		
		importerConfig.setGeonamesImporterEnabled(true);
		importerConfig.setImportGisFeatureEmbededAlternateNames(true);
		Assert.assertTrue(extracter.shouldBeSkipped());
	    }
	  
	  @Test
	  public void testLineIsAnAlternatNameForAdm2(){
		  GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter();
		  HashMap<Long, String> hashMap = new HashMap<Long, String>();
		  long existingFeatureId = 3L;
		hashMap.put(existingFeatureId, "");
		  extracter.adm2Map = hashMap;
		  Assert.assertTrue("the method should return true when the adm2 featureid exists",extracter.lineIsAnAlternatNameForAdm2(existingFeatureId));
		  Assert.assertFalse("the method should return false when the adm2 featureid doesn't exists",extracter.lineIsAnAlternatNameForAdm2(4L));
	  }

	  @Test
	  public void testLineIsAnAlternatNameForAdm1(){
		  GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter();
		  HashMap<Long, String> hashMap = new HashMap<Long, String>();
		  long existingFeatureId = 3L;
		  hashMap.put(existingFeatureId, "");
		  extracter.adm1Map = hashMap;
		  Assert.assertTrue("the method should return true when the adm2 featureid exists",extracter.lineIsAnAlternateNameForAdm1(existingFeatureId));
		  Assert.assertFalse("the method should return false when the adm2 featureid doesn't exists",extracter.lineIsAnAlternateNameForAdm1(4L));
	  }
	  
	  @Test
	  public void testLineIsAnAlternatNameForCountry(){
		  GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter();
		  HashMap<Long, String> hashMap = new HashMap<Long, String>();
		  long existingFeatureId = 3L;
		  hashMap.put(existingFeatureId, "");
		  extracter.countryMap = hashMap;
		  Assert.assertTrue("the method should return true when the adm2 featureid exists",extracter.lineIsAnAlternateNameForCountry(existingFeatureId));
		  Assert.assertFalse("the method should return false when the adm2 featureid doesn't exists",extracter.lineIsAnAlternateNameForCountry(4L));
	  }
	  
	  @Test
	  public void testInitFilesShouldCreateTheFilesAndWriter(){
		  GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter();
		  ImporterConfig importerConfig = new ImporterConfig();
		// create a temporary directory to download files
			File tempDir = FileHelper.createTempDir(this.getClass()
				.getSimpleName());
		  importerConfig.setGeonamesDir(tempDir.getAbsolutePath());
		  extracter.setImporterConfig(importerConfig);
		  
		  extracter.initFiles();
		  Assert.assertTrue("the adm1 file should have been created",extracter.adm1file.exists());
		  Assert.assertNotNull("the adm1 writer should not be null",extracter.adm1fileOutputStreamWriter!=null);
		  
		  Assert.assertTrue("the adm2 file should have been created",extracter.adm2file.exists());
		  Assert.assertNotNull("the adm2 writer should not be null",extracter.adm2fileOutputStreamWriter!=null);
		  
		  Assert.assertTrue("the country file should have been created",extracter.countryFile.exists());
		  Assert.assertNotNull("the country writer should not be null",extracter.countryfileOutputStreamWriter!=null);
		  
		  Assert.assertTrue("the featuresFile should have been created",extracter.featuresFile.exists());
		  Assert.assertNotNull("the featuresfileOutputStreamWriter writer should not be null",extracter.featuresfileOutputStreamWriter!=null);
		  
		  
	  }

	  
}
