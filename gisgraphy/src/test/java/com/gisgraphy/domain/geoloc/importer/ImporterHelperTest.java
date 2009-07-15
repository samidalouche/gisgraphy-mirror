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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.gisgraphy.test.GeolocTestHelper;

public class ImporterHelperTest extends TestCase {

    @Test
    public void testVirtualizeADMDshouldChangeADMDTOADMXAccordingToTheAdmcodes() {
	String[] fields = new String[19];
	fields[6] = "A";
	fields[7] = "ADMD";
	fields[10] = "A1";
	fields[11] = "B2";
	fields[12] = "C3";
	assertEquals("ADM3", ImporterHelper.virtualizeADMD(fields)[7]);
	fields[11] = null;
	fields[7] = "ADMD";
	assertEquals("ADM1", ImporterHelper.virtualizeADMD(fields)[7]);
	fields[7] = "PPL";
	assertEquals("PPL", ImporterHelper.virtualizeADMD(fields)[7]);
    }

    @Test
    public void testcorrectLastAdmCodeIfPossibleShouldReallyCorrect() {
	String[] fieldsWrongFeatureClass = { "123", "1", "2", "3", "4", "5",
		"B", "ADM1", "8", "9", "", "11", "12", "13", "14", "15", "16",
		"17", "18" };
	String[] fieldsWrongFeatureCode = { "123", "1", "2", "3", "4", "5",
		"A", "PPL", "8", "9", "", "11", "12", "13", "14", "15", "16",
		"17", "18" };
	String[] fieldsWrongFeatureId = { "", "1", "2", "3", "4", "5", "A",
		"ADM1", "8", "9", "", "11", "12", "13", "14", "15", "16", "17",
		"18" };
	String[] fieldsAdm1 = { "123", "1", "2", "3", "4", "5", "A", "ADM1",
		"8", "9", "", "11", "12", "13", "14", "15", "16", "17", "18" };
	String[] fieldsAdm2 = { "123", "1", "2", "3", "4", "5", "A", "ADM2",
		"8", "9", "10", "", "", "", "14", "15", "16", "17", "18" };
	String[] fieldsAdm3 = { "123", "1", "2", "3", "4", "5", "A", "ADM3",
		"8", "9", "10", "11", "", "", "14", "15", "16", "17", "18" };
	String[] fieldsAdm4 = { "123", "1", "2", "3", "4", "5", "A", "ADM4",
		"8", "9", "10", "11", "12", "", "14", "15", "16", "17", "18" };
	String[] fieldsAdm1Bis = { "123", "1", "2", "3", "4", "5", "A", "ADM1",
		"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };
	String[] fieldsAdm2Bis = { "123", "1", "2", "3", "4", "5", "A", "ADM2",
		"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };
	String[] fieldsAdm3Bis = { "123", "1", "2", "3", "4", "5", "A", "ADM3",
		"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };
	String[] fieldsAdm4Bis = { "123", "1", "2", "3", "4", "5", "A", "ADM4",
		"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };

	assertTrue("".equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsWrongFeatureClass)[10]));
	assertTrue("".equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsWrongFeatureCode)[10]));
	assertTrue("".equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsWrongFeatureId)[10]));

	assertTrue(fieldsAdm1[0].equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm1)[10]));
	assertTrue(fieldsAdm2[0].equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm2)[11]));
	assertTrue(fieldsAdm3[0].equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm3)[12]));
	assertTrue(fieldsAdm4[0].equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm4)[13]));

	assertTrue("10".equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm1Bis)[10]));
	assertTrue("11".equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm2Bis)[11]));
	assertTrue("12".equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm3Bis)[12]));
	assertTrue("13".equals(ImporterHelper
		.correctLastAdmCodeIfPossible(fieldsAdm4Bis)[13]));

    }

    @Test
    public void testIsDirectoryAccessible() {
	File tempDir = null;
	try {
	    String accessiblePath = System.getProperty("java.io.tmpdir")
		    + System.getProperty("file.separator")
		    + getClass().getSimpleName() + "-"
		    + System.currentTimeMillis();
	    tempDir = new File(accessiblePath);
	    tempDir.mkdir();
	    assertTrue(ImporterHelper.isDirectoryAccessible(accessiblePath));
	    String pathNotAccessible = System.getProperty("java.io.tmpdir")
		    + System.getProperty("file.separator")
		    + getClass().getSimpleName() + "-"
		    + new Random().nextLong();
	    // System.err.println("#########"+pathNotAccessible);
	    assertFalse(ImporterHelper.isDirectoryAccessible(pathNotAccessible));
	    String filePath = tempDir.getAbsolutePath()
		    + System.getProperty("file.separator") + "test.txt";
	    File file = new File(filePath);
	    try {
		file.createNewFile();
	    } catch (IOException e) {
		fail("Can not create file " + filePath);
	    }
	    assertFalse(ImporterHelper.isDirectoryAccessible(filePath));
	} finally {
	    // delete temp dir
	    assertTrue("the tempDir has not been deleted", GeolocTestHelper
		    .DeleteNonEmptyDirectory(tempDir));
	}

    }

    @Test
    public void testCompileRegexShouldCompile() {
	List<Pattern> patterns = ImporterHelper
		.compileRegex("P[.]PPL[A-Z&&[^QW]];P[.]PPL$;P[.]STLMT$");
	assertEquals(3, patterns.size());
    }

    @Test
    public void testCompileRegexShouldReturnEmptyListForEmptyString() {
	List<Pattern> patterns = ImporterHelper.compileRegex("");
	assertEquals(0, patterns.size());
    }

    @Test
    public void testCompileRegexShouldReturnNullIfRegexAreIncorect() {
	List<Pattern> patterns = ImporterHelper
		.compileRegex("P[.]PPL[A-Z&&[^QW]];P[.]PPL$;P[.]STLMT$;*");
	assertNull(patterns);
    }

    @Test
    public void testFormatSeconds() {
	// test value and space
	assertTrue(ImporterHelper.formatSeconds(1000000).contains("277 "));
	assertTrue(ImporterHelper.formatSeconds(1000000).contains(" 46 "));
	assertTrue(ImporterHelper.formatSeconds(1000000).contains(" 40 "));
	// test display vfor value '0'
	assertTrue("minut should not be displayed if 0 : "
		+ ImporterHelper.formatSeconds(3600), !ImporterHelper
		.formatSeconds(3600).contains("minut"));
	assertTrue("second should not be displayed if 0 : "
		+ ImporterHelper.formatSeconds(3600), !ImporterHelper
		.formatSeconds(3600).contains("second"));
	assertTrue("hour should not be displayed if 0 : "
		+ ImporterHelper.formatSeconds(100), !ImporterHelper
		.formatSeconds(100).contains(" hour "));
	// test singular and space
	assertTrue("hour should be singular when less or equals 1 hour : "
		+ ImporterHelper.formatSeconds(3600), ImporterHelper
		.formatSeconds(3600).contains("hour "));
	assertTrue("minut should be singular when less or equals 1 minut : "
		+ ImporterHelper.formatSeconds(60), ImporterHelper
		.formatSeconds(60).contains(" minut"));
	assertTrue("second should be singular when less or equals 1 second : "
		+ ImporterHelper.formatSeconds(1), ImporterHelper
		.formatSeconds(1).contains(" second"));
	// plural and space
	assertTrue("hour should be plural when greater 1 hour : "
		+ ImporterHelper.formatSeconds(3600), ImporterHelper
		.formatSeconds(7200).contains(" hours"));
	assertTrue("minut should be singular when greater 1 minut : "
		+ ImporterHelper.formatSeconds(60), ImporterHelper
		.formatSeconds(120).contains(" minuts "));
	assertTrue("second should be singular when greater 1 minut : "
		+ ImporterHelper.formatSeconds(1), ImporterHelper
		.formatSeconds(2).contains(" seconds"));
    }
    
    @Test
    public void testListCountryFilesToImport() throws IOException{
	
	// create a temporary directory to download files
	File tempDir = GeolocTestHelper.createTempDir(this.getClass()
		.getSimpleName());

	try {
	    String tempDirectoryPath = tempDir.getAbsolutePath();
	    
	    File goodFilePattern1 = new File(tempDirectoryPath+File.separator+"FR.txt");
	    goodFilePattern1.createNewFile();
	    
	    File goodFilePattern2 = new File(tempDirectoryPath+File.separator+"OK.txt");
	    goodFilePattern2.createNewFile();
	    
	    File goodFilePatternUS = new File(tempDirectoryPath+File.separator+"US.1.txt");
	    goodFilePatternUS.createNewFile();
	    
	    File goodFilePatternUSForGeonames = new File(tempDirectoryPath+File.separator+"US.txt");
	    goodFilePatternUSForGeonames.createNewFile();
	    
	    File goodFilePatternUS2 = new File(tempDirectoryPath+File.separator+"US.12.txt");
	    goodFilePatternUS2.createNewFile();
	    
	    File badFilePatternWithLowerCase = new File(tempDirectoryPath+File.separator+"Ko.txt");
	    badFilePatternWithLowerCase.createNewFile();
	    
	    File badFilePatternWithLowerCase2 = new File(tempDirectoryPath+File.separator+"kO.txt");
	    badFilePatternWithLowerCase2.createNewFile();
	    
	    File badFilePatternWithExcludedName = new File(tempDirectoryPath+File.separator+ImporterHelper.EXCLUDED_README_FILENAME);
	    badFilePatternWithExcludedName.createNewFile();
	    
	    File badFilePatternWithALLCountriesPattern = new File(tempDirectoryPath+File.separator+ImporterHelper.ALLCOUTRY_FILENAME);
	    badFilePatternWithALLCountriesPattern.createNewFile();
	    
	    assertEquals("9 files must be created ",9,tempDir.listFiles().length);

	    
	    File [] fileToBeImported = ImporterHelper.listCountryFilesToImport(tempDirectoryPath);
	    assertEquals(1, fileToBeImported.length);
	    assertEquals("When "+ImporterHelper.ALLCOUTRY_FILENAME+ "is present, only this file should be return",  ImporterHelper.ALLCOUTRY_FILENAME, fileToBeImported[0].getName());

	    assertTrue(" the ImporterHelper.ALLCOUTRY_FILENAME can not be deleted ",badFilePatternWithALLCountriesPattern.delete());
	    
	    fileToBeImported = ImporterHelper.listCountryFilesToImport(tempDirectoryPath);
	    assertEquals("Only UppercaseFile with two letters and US.NUMBER.txt should be listed ",5, fileToBeImported.length);
	}finally {
	    assertTrue("the tempDir has not been deleted", GeolocTestHelper
			.DeleteNonEmptyDirectory(tempDir));
	}
	
	
	
	
    }
    
    @Test
    public void testListCountryFilesToImportShouldNotReturnNullIfThereIsNoFile(){
	Assert.assertNotNull(ImporterHelper.listCountryFilesToImport(""));
    }
    
    @Test
    public void testListZipFilesShouldNotReturnNullIfThereIsNoFile(){
	Assert.assertNotNull(ImporterHelper.listZipFiles(""));
    }
    
    @Test
    public void testListTarFilesShouldNotReturnNullIfThereIsNoFile(){
	Assert.assertNotNull(ImporterHelper.listZipFiles(""));
    }
    
    
}
