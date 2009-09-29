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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.test.GeolocTestHelper;

public class AdmExtracterTest extends TestCase {

    public void testRollback() {
	AdmExtracter admExtracter = new AdmExtracter();
	ImporterConfig importerConfig = new ImporterConfig();
	File tempDir = GeolocTestHelper.createTempDir(this.getClass()
		.getSimpleName());
	// create adm file
	for (int i = 1; i <= 4; i++) {
	    File adm = new File(tempDir.getAbsolutePath()
		    + System.getProperty("file.separator") + "ADM" + i + ".txt");
	    try {
		assertTrue(adm.createNewFile());
	    } catch (IOException e) {
		fail("Can not create file " + adm.getAbsolutePath());
	    }
	}

	importerConfig.setGeonamesDir(tempDir.getAbsolutePath());
	importerConfig.setAdm1FileName("ADM1.txt");
	importerConfig.setAdm2FileName("ADM2.txt");
	importerConfig.setAdm3FileName("ADM3.txt");
	importerConfig.setAdm4FileName("ADM4.txt");

	admExtracter.setImporterConfig(importerConfig);
	List<NameValueDTO<Integer>> deleted = admExtracter.rollback();
	assertEquals(4, deleted.size());
	for (int i = 1; i <= 4; i++) {
	    assertFalse("The adm" + i + " file should have been deleted",
		    new File(tempDir.getAbsolutePath()
			    + System.getProperty("file.separator") + "ADM" + i
			    + ".txt").exists());
	}

	// delete temp dir
	assertTrue("The tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));
    }
    
    @Test
    public void testShouldBeSkipShouldReturnCorrectValue(){
	ImporterConfig importerConfig = new ImporterConfig();
	AdmExtracter extracter = new AdmExtracter();
	extracter.setImporterConfig(importerConfig);
	
	importerConfig.setGeonamesImporterEnabled(false);
	Assert.assertTrue(extracter.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(true);
	Assert.assertFalse(extracter.shouldBeSkipped());
		
    }

}
