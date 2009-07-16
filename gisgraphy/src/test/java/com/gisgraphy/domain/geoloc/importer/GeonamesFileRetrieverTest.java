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

public class GeonamesFileRetrieverTest extends TestCase {

    @Test
    public void testRollback() {
	GeonamesFileRetriever geonamesFileRetriever = new GeonamesFileRetriever();
	ImporterConfig importerConfig = new ImporterConfig();
	File tempDir = GeolocTestHelper.createTempDir(this.getClass()
		.getSimpleName());
	File file = new File(tempDir.getAbsolutePath()
		+ System.getProperty("file.separator") + "FR.zip");
	try {
	    assertTrue(file.createNewFile());
	} catch (IOException e) {
	    fail("Can not create file " + file.getAbsolutePath());
	}

	importerConfig.setGeonamesDir(tempDir.getAbsolutePath());
	importerConfig.setFilesToDownload("FR.zip");
	geonamesFileRetriever.setImporterConfig(importerConfig);
	List<NameValueDTO<Integer>> list = geonamesFileRetriever.rollback();
	assertEquals(1, list.size());
	assertFalse("The importable file should have been deleted", file
		.exists());

	// delete temp dir
	assertTrue("The tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));

    }
    
    @Test
    public void testShouldBeSkipShouldReturnCorrectValue(){
	ImporterConfig importerConfig = new ImporterConfig();
	GeonamesFileRetriever geonamesFileRetriever = new GeonamesFileRetriever();
	geonamesFileRetriever.setImporterConfig(importerConfig);
	
	importerConfig.setGeonamesImporterEnabled(false);
	importerConfig.setRetrieveFiles(false);
	Assert.assertTrue(geonamesFileRetriever.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(false);
	importerConfig.setRetrieveFiles(true);
	Assert.assertTrue(geonamesFileRetriever.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(true);
	importerConfig.setRetrieveFiles(false);
	Assert.assertTrue(geonamesFileRetriever.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(true);
	importerConfig.setRetrieveFiles(true);
	Assert.assertFalse(geonamesFileRetriever.shouldBeSkipped());
	
	
	
    }

}
