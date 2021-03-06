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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gisgraphy.test.GeolocTestHelper;

public class ImporterConfigTest {

	public static String accessiblePath;
	public static String pathNotAccessible;
	public static String filePath;
	public static File tempDir;

	@BeforeClass
	public static void setUpClass() throws IOException {
		accessiblePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ImporterConfig.class.getSimpleName() + "-" + System.currentTimeMillis();
		tempDir = new File(accessiblePath);
		tempDir.mkdir();

		pathNotAccessible = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + ImporterConfig.class.getSimpleName() + "-" + new Random().nextLong();

		filePath = tempDir.getAbsolutePath() + System.getProperty("file.separator") + "test.txt";
		File file = new File(filePath);
		file.createNewFile();
	}

	@AfterClass
	public static void TearDownClass() throws IOException {
		// delete temp dir
		assertTrue("the tempDir has not been deleted", GeolocTestHelper.DeleteNonEmptyDirectory(tempDir));
	}

	@Test
	public void isGeonamesDownloadDirectoryAccessible() {
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setGeonamesDir(accessiblePath);
		assertTrue(importerConfig.isGeonamesDownloadDirectoryAccessible());

		importerConfig.setGeonamesDir(pathNotAccessible);
		assertFalse(importerConfig.isGeonamesDownloadDirectoryAccessible());
		importerConfig.setGeonamesDir(filePath);
		assertFalse(importerConfig.isGeonamesDownloadDirectoryAccessible());
	}

	@Test
	public void isOpenStreetMapDownloadDirectoryAccessible() {
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapDir(accessiblePath);
		assertTrue(importerConfig.isOpenStreetMapDownloadDirectoryAccessible());

		importerConfig.setOpenStreetMapDir(pathNotAccessible);
		assertFalse(importerConfig.isOpenStreetMapDownloadDirectoryAccessible());
		importerConfig.setOpenStreetMapDir(filePath);
		assertFalse(importerConfig.isOpenStreetMapDownloadDirectoryAccessible());
	}

	@Test
	public void setOpenStreetMapDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator() {
		String OpenStreetMapDir = "Test";
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setOpenStreetMapDir(OpenStreetMapDir);
		Assert.assertTrue("setOpenStreetMapDir should add File separator", importerConfig.getOpenStreetMapDir().endsWith(File.separator));
		Assert.assertEquals(OpenStreetMapDir + File.separator, importerConfig.getOpenStreetMapDir());
	}

	@Test
	public void setGeonamesDirShouldAddFileSeparatorIfItDoesnTEndsWithFileSeparator() {
		String geonamesDir = "Test";
		ImporterConfig importerConfig = new ImporterConfig();
		importerConfig.setGeonamesDir(geonamesDir);
		Assert.assertTrue("setGeonamesDir should add File separator", importerConfig.getGeonamesDir().endsWith(File.separator));
		Assert.assertEquals(geonamesDir + File.separator, importerConfig.getGeonamesDir());
	}

	@Test
	public void isGeonamesImporterShouldBeTrueByDefault() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue("Geonames importer should be enabled by default ", importerConfig.isGeonamesImporterEnabled());
	}

	@Test
	public void isOpenstreetmapImporterShouldBeTrueByDefault() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue("OpenStreetMap importer should be enabled by default ", importerConfig.isOpenstreetmapImporterEnabled());
	}
	
	@Test
	public void isConfigCorrectForImport(){
		ImporterConfig importerConfig = new ImporterConfig();
		//set Correct values
		importerConfig.setGeonamesDir(accessiblePath);
		importerConfig.setOpenStreetMapDir(accessiblePath);
		importerConfig.setAcceptRegExString(ImporterConfig.DEFAULT_ACCEPT_REGEX);
		//test with bad geonamesDir
		importerConfig.setGeonamesDir(pathNotAccessible);
		Assert.assertFalse("when geonames dir is not ok the function should return false", importerConfig.isConfigCorrectForImport());
		importerConfig.setGeonamesDir(accessiblePath);
		//test with bad Openstreetmap dir
		importerConfig.setOpenStreetMapDir(pathNotAccessible);
		Assert.assertFalse("when openstreetmap dir is not ok the function should return false", importerConfig.isConfigCorrectForImport());
		importerConfig.setOpenStreetMapDir(accessiblePath);
		//test with bad regexp
		importerConfig.setAcceptRegExString("k[;l");
		Assert.assertFalse("when regexp string is not ok the function should return false", importerConfig.isConfigCorrectForImport());
		importerConfig.setAcceptRegExString(ImporterConfig.DEFAULT_ACCEPT_REGEX);
		//test when everything is correct
		assertTrue("when all the condition are ok the function should return true", importerConfig.isConfigCorrectForImport());
		
	}
	
	 @Test
	    public void testcreateImporterMetadataDirIfItDoesnTExistShouldCreateTheGeonamesDirIfItDoesnTExist(){
	    	ImporterConfig fakeImporterConfig = new ImporterConfig();
	    	String geonameDirPathThatDoesnTExist = System.getProperty("java.io.tmpdir")+File.separator+Math.abs(new Random().nextInt());
	    	fakeImporterConfig.setGeonamesDir(geonameDirPathThatDoesnTExist);
	    	fakeImporterConfig.createImporterMetadataDirIfItDoesnTExist();
	    	assertTrue("if the geonames directory doen't exists it should be created when the getAlreadyDoneFilePath method is called", new File(geonameDirPathThatDoesnTExist).exists());
	    }
	    

}
