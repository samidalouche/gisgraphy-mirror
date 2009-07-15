package com.gisgraphy.domain.geoloc.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.ISpellCheckerIndexer;
import com.gisgraphy.domain.repository.SolRSynchroniser;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.ImporterStatusDto;
import com.gisgraphy.test.GeolocTestHelper;


public class OpenStreetMapFileRetrieverTest {
    
    @Test
    public void processAndCheckGeonamesFileRetriever() {
	
	OpenStreetMapFileRetriever openStreetMapFileRetriever = new OpenStreetMapFileRetriever();

	
	ImporterConfig importerConfig = new ImporterConfig();

	importerConfig.setOpenstreetMapDownloadURL("http://download.gisgraphy.com/openstreetmap/");
	
	// create a temporary directory to download files
	File tempDir = GeolocTestHelper.createTempDir(this.getClass()
		.getSimpleName());

	// get files to download
	List<String> filesToDownload =new ArrayList<String>();
	String fileTobeDownload = "NU.tar.bz2";
	filesToDownload.add(fileTobeDownload);
	importerConfig.setFilesToDownload(fileTobeDownload);
	importerConfig.setRetrieveFiles(true);

	importerConfig.setOpenStreetMapDir(tempDir.getAbsolutePath());

	// check that the directory is ending with the / or \ according to the
	// System
	Assert.assertTrue("openstreetmapdir must ends with" + File.separator,
		importerConfig.getOpenStreetMapDir().endsWith(File.separator));
	
	openStreetMapFileRetriever.setImporterConfig(importerConfig);
	openStreetMapFileRetriever.process();

	// check that openStreetmapURL ends with '/' : normally "/" is added
	// if not
	Assert.assertTrue("openstreetmapDownloadURL must ends with '/' but was "
		+ importerConfig.getOpenstreetMapDownloadURL(), importerConfig
		.getOpenstreetMapDownloadURL().endsWith("/"));

	// check that files have been Downloaded
	File file = null;
	for (String fileToDownload : filesToDownload) {
	    file = new File(importerConfig.getOpenStreetMapDir() + fileToDownload);
	    if (importerConfig.isRetrieveFiles()) {
		Assert.assertTrue("Le fichier " + fileToDownload
			+ " have not been downloaded in "
			+ importerConfig.getOpenStreetMapDir(), file.exists());
	    } else {
		Assert.assertFalse("Le fichier " + fileToDownload
			+ " have been downloaded in "
			+ importerConfig.getOpenStreetMapDir()
			+ " even if the option retrievefile is"
			+ importerConfig.isRetrieveFiles(), file.exists());
	    }
	}

	// check that files have been untar
	for (String fileToDownload : filesToDownload) {
	    String fileNameWithCSVExtension = fileToDownload.substring(0,
		    (fileToDownload.length()) - 8)
		    + ".csv";
	    file = new File(importerConfig.getOpenStreetMapDir()
		    + fileNameWithCSVExtension);
	    if (importerConfig.isRetrieveFiles()) {
		Assert.assertTrue("Le fichier " + fileNameWithCSVExtension
			+ " have not been untar in "
			+ importerConfig.getOpenStreetMapDir(), file.exists());
	    } else {
		Assert.assertFalse("Le fichier " + fileToDownload
			+ " have been unzip in "
			+ importerConfig.getOpenStreetMapDir()
			+ " even if the option retrievefile is"
			+ importerConfig.isRetrieveFiles(), file.exists());
	    }
	}

	// delete temp dir
	Assert.assertTrue("the tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));

	

    }
    
    @Test
    public void StatusShouldBeEqualsToSkipedIfRetrieveFileIsFalse(){
	OpenStreetMapFileRetriever openStreetMapFileRetriever = new OpenStreetMapFileRetriever();
	ImporterConfig importerConfig = new ImporterConfig();
	importerConfig.setRetrieveFiles(false);
	openStreetMapFileRetriever.setImporterConfig(importerConfig);
	openStreetMapFileRetriever.process();
	Assert.assertEquals(ImporterStatus.SKIPED, openStreetMapFileRetriever.getStatus());
	ImporterStatusDto statusDto = new ImporterStatusDto(openStreetMapFileRetriever);
	Assert.assertEquals(0, statusDto.getPercent());
    }
    
   

}
