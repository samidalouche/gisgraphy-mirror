package com.gisgraphy.domain.geoloc.importer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.springframework.test.AssertThrows;

import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.helper.FileHelper;


public class GeonamesAlternateNamesExtracterTest {
    
    public boolean initFilesIsCalled = false;
	
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

	  
	  @Test
	  public void testSetup(){
	      ICountryDao countryDao = createMockCountryDao();
	      
	      IAdmDao admDao = createMockAdmDao();
	      
	      
	      GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter(){
		

		@Override
		protected void initFiles() {
		   initFilesIsCalled = true;
		}
	      };
	      extracter.setAdmDao(admDao);
	      extracter.setCountryDao(countryDao);
	      
	      extracter.setup();
	      assertTrue("initfiles should be called in setup",initFilesIsCalled);
	      assertEquals("the adm1 map has not the expected number of element ",2,extracter.adm1Map.size());
	      assertTrue("the adm1 map is missing a value",extracter.adm1Map.containsKey(3L));
	      assertTrue("the adm1 map is missing a value",extracter.adm1Map.containsKey(4L));
	      
	      assertEquals("the adm2 map has not the expected number of element ",2,extracter.adm2Map.size());
	      assertTrue("the adm2 map is missing a value",extracter.adm2Map.containsKey(5L));
	      assertTrue("the adm2 map is missing a value",extracter.adm2Map.containsKey(6L));
	      
	      assertEquals("the countries map has not the expected number of element ",2,extracter.countryMap.size());
	      assertTrue("the countries map is missing a value",extracter.countryMap.containsKey(1L));
	      assertTrue("the countries map is missing a value",extracter.countryMap.containsKey(2L));
	      verify(countryDao);
	      verify(admDao);
	      
	  }

	private IAdmDao createMockAdmDao() {
	    IAdmDao admDao = createMock(IAdmDao.class);
	      List<Long> adm1Ids = new ArrayList<Long>();
	      adm1Ids.add(3L);
	      adm1Ids.add(4L);
	      List<Long> adm2Ids = new ArrayList<Long>();
	      adm2Ids.add(5L);
	      adm2Ids.add(6L);
	      expect(admDao.listFeatureIdByLevel(1)).andReturn(adm1Ids);
	      expect(admDao.listFeatureIdByLevel(2)).andReturn(adm2Ids);
	      replay(admDao);
	    return admDao;
	}

	private ICountryDao createMockCountryDao() {
	    ICountryDao countryDao = EasyMock.createMock(ICountryDao.class);
	      List<Long> countriesIds = new ArrayList<Long>();
	      countriesIds.add(1L);
	      countriesIds.add(2L);
	      expect(countryDao.listFeatureIds()).andStubReturn(countriesIds);
	      replay(countryDao);
	    return countryDao;
	}
	  
	  @Test
	  public void testProcessDataWithNonNumericFeatureIdShouldIgnoreTheLine(){
	      GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter();
	      extracter.processData("1	nonnumeric	FR	alterantony	1	1");
	  }
	  
	  @Test
	  public void testProcessDataWithMissingAlternateNameIDShouldIgnoreTheLine(){
	      GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter();
	      extracter.processData("	nonnumeric	FR	alterantony	1	1");
	  }
	  
	  @Test
	  public void testProcessDataWithCountryFeatureIDShouldIgnoreTheLine() throws IOException{
	      String line = "1	1	FR	alterantony	1	1";
	      GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter(){
		  @Override
		protected boolean lineIsAnAlternateNameForCountry(Long featureId) {
		   return true;
		}
	      };
	      OutputStreamWriter mockWriter = EasyMock.createMock(OutputStreamWriter.class);
	      mockWriter.write(line);
	      mockWriter.flush();
	      replay(mockWriter);
	      extracter.countryfileOutputStreamWriter= mockWriter;
	      extracter.setCountryDao(createMockCountryDao());
	      extracter.processData(line);
	      verify(mockWriter);
	  }
	  
	  @Test
	  public void testProcessDataWithAdm1FeatureIDShouldIgnoreTheLine() throws IOException{
	      String line = "1	1	FR	alterantony	1	1";
	      GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter(){
		  @Override
		protected boolean lineIsAnAlternateNameForCountry(Long featureId) {
		   return false;
		}
		
		  @Override
		protected boolean lineIsAnAlternateNameForAdm1(Long featureId) {
		    return true;
		}
		  
	      };
	      OutputStreamWriter mockWriter = EasyMock.createMock(OutputStreamWriter.class);
	      mockWriter.write(line);
	      mockWriter.flush();
	      replay(mockWriter);
	      extracter.adm1fileOutputStreamWriter= mockWriter;
	      extracter.setCountryDao(createMockCountryDao());
	      extracter.processData(line);
	      verify(mockWriter);
	  }
	  
	  @Test
	  public void testProcessDataWithAdm2FeatureIDShouldIgnoreTheLine() throws IOException{
	      String line = "1	1	FR	alterantony	1	1";
	      GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter(){
		  @Override
		protected boolean lineIsAnAlternateNameForCountry(Long featureId) {
		   return false;
		}
		
		  @Override
		protected boolean lineIsAnAlternateNameForAdm1(Long featureId) {
		    return false;
		}
		  
		  @Override
		protected boolean lineIsAnAlternatNameForAdm2(Long featureId) {
		    return true;
		}
	      };
	      OutputStreamWriter mockWriter = EasyMock.createMock(OutputStreamWriter.class);
	      mockWriter.write(line);
	      mockWriter.flush();
	      replay(mockWriter);
	      extracter.adm2fileOutputStreamWriter= mockWriter;
	      extracter.setCountryDao(createMockCountryDao());
	      extracter.processData(line);
	      verify(mockWriter);
	  }
	  
	  @Test
	  public void testProcessDataWithGenericFeatureIDShouldIgnoreTheLine() throws IOException{
	      String line = "1	1	FR	alterantony	1	1";
	      GeonamesAlternateNamesExtracter extracter = new  GeonamesAlternateNamesExtracter(){
		  @Override
		protected boolean lineIsAnAlternateNameForCountry(Long featureId) {
		   return false;
		}
		
		  @Override
		protected boolean lineIsAnAlternateNameForAdm1(Long featureId) {
		    return false;
		}
		  
		  @Override
		protected boolean lineIsAnAlternatNameForAdm2(Long featureId) {
		    return false;
		}
	      };
	      OutputStreamWriter mockWriter = EasyMock.createMock(OutputStreamWriter.class);
	      mockWriter.write(line);
	      mockWriter.flush();
	      replay(mockWriter);
	      extracter.featuresfileOutputStreamWriter= mockWriter;
	      extracter.setCountryDao(createMockCountryDao());
	      extracter.processData(line);
	      verify(mockWriter);
	  }
	  
}
