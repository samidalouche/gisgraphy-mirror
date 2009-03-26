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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.classextension.EasyMock;
import org.hibernate.FlushMode;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.Forest;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.Language;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.repository.IAlternateNameDao;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.domain.repository.IGisDao;
import com.gisgraphy.domain.repository.IGisFeatureDao;
import com.gisgraphy.domain.repository.IImporterStatusListDao;
import com.gisgraphy.domain.repository.ILanguageDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.valueobject.AlternateNameSource;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GISSource;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.ImporterStatusDto;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.test.GeolocTestHelper;

@Ignore
public class ImporterManagerTest extends AbstractIntegrationHttpSolrTestCase {

    private static final String ADM1_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS = "admin1CodesWithWrongNumberOfFields.txt";

    private static final String ADM2_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS = "admin2CodesWithWrongNumberOfFields.txt";

    private static final String ADM3_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS = "admin3CodesWithWrongNumberOfFields.txt";

    private static final String ADM4_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS = "admin4CodesWithWrongNumberOfFields.txt";

    private static final String ADM1_FILENAME_WITH_MISSING_FIELDS = "admin1CodesWithMissingFields.txt";

    private static final String ADM2_FILENAME_WITH_MISSING_FIELDS = "admin2CodesWithMissingFields.txt";

    private static final String ADM3_FILENAME_WITH_MISSING_FIELDS = "admin3CodesWithMissingFields.txt";

    private static final String ADM4_FILENAME_WITH_MISSING_FIELDS = "admin4CodesWithMissingFields.txt";

    private static final String ALTERNATENAME_FILENAME_WITH_MISSING_FIELDS = "alternateNamesWithMissingFields.txt";

    private static final String ADM2_FILENAME_WITH_BAD_FORMAT_GISFEATUREID = "admin2CodesWithBadFormatGisFeatureID.txt";

    private IGeonamesProcessor geonamesFileRetriever;

    private IGeonamesProcessor admExtracter;

    private IGeonamesProcessor geonamesAdm1Importer;

    private IGeonamesProcessor geonamesAdm2Importer;

    private IGeonamesProcessor geonamesAdm3Importer;

    private IGeonamesProcessor geonamesAdm4Importer;

    private IGeonamesProcessor geonamesFeatureImporter;

    private IGeonamesProcessor geonamesLanguageImporter;

    private IGeonamesProcessor geonamesCountryImporter;

    private IGeonamesProcessor geonamesAlternateNamesImporter;

    private ImporterConfig importerConfig;

    private IAdmDao admDao;

    private ICityDao cityDao;

    private IGisFeatureDao gisFeatureDao;

    private ILanguageDao languageDao;

    private ICountryDao countryDao;

    @Autowired
    @Qualifier("forestDao")
    private IGisDao<Forest> forestDao;

    private IAlternateNameDao alternateNameDao;

    /*
     * don't know why on windows, we must do this and sometimes no (encoding
     * problems
     */
    private String toUTF8(String text) {
	// return EncodingHelper.toUTF8(text);
	return text;
    }

    // test reset

    @SuppressWarnings("unchecked")
    @Test
    public void testResetImportShouldSetAlreadyDoneToFalseAndReturnInfoOnDeletedObjects() {
	ImporterManager fakeimporterManager = new ImporterManager();
	ICityDao mockCityDao = EasyMock.createMock(ICityDao.class);
	mockCityDao.setFlushMode(FlushMode.COMMIT);
	EasyMock.expectLastCall();
	EasyMock.replay(mockCityDao);

	IGisFeatureDao mockGisDao = EasyMock.createMock(IGisFeatureDao.class);
	mockGisDao.setFlushMode(FlushMode.COMMIT);
	EasyMock.expectLastCall();
	EasyMock.replay(mockGisDao);
	IImporterStatusListDao fakeimporterStatusListDao = EasyMock
		.createMock(IImporterStatusListDao.class);
	EasyMock.expect(fakeimporterStatusListDao.delete()).andReturn(true);
	EasyMock.expect(
		fakeimporterStatusListDao
			.saveOrUpdate((List<ImporterStatusDto>) EasyMock
				.anyObject())).andReturn(
		new ArrayList<ImporterStatusDto>());
	EasyMock.expect(fakeimporterStatusListDao.get()).andReturn(
		new ArrayList<ImporterStatusDto>());
	EasyMock.replay(fakeimporterStatusListDao);

	ImporterConfig mockImporterConfig = EasyMock
		.createMock(ImporterConfig.class);
	IGeonamesProcessor processor1 = EasyMock
		.createMock(IGeonamesProcessor.class);
	ISolRSynchroniser mockSolRSynchroniser = EasyMock
		.createMock(ISolRSynchroniser.class);
	mockSolRSynchroniser.deleteAll();
	EasyMock.expectLastCall();
	processor1.process();
	EasyMock.expectLastCall();
	EasyMock.expect(processor1.rollback()).andReturn(
		new ArrayList<NameValueDTO<Integer>>());
	EasyMock.expect(processor1.getCurrentFileName()).andReturn(
		"currentFileName");
	EasyMock.expect(processor1.getStatus()).andReturn(
		ImporterStatus.PROCESSED);
	EasyMock.expect(processor1.getStatusMessage()).andReturn("message");
	EasyMock.expect(processor1.getTotalReadLine()).andReturn(3);
	EasyMock.expect(processor1.getReadFileLine()).andReturn(1);
	EasyMock.expect(processor1.getNumberOfLinesToProcess()).andReturn(5);
	EasyMock.replay(processor1);
	List<IGeonamesProcessor> processors = new ArrayList<IGeonamesProcessor>();
	processors.add(processor1);
	fakeimporterManager.setImporterStatusListDao(fakeimporterStatusListDao);
	fakeimporterManager.setImporters(processors);
	fakeimporterManager.setImporterConfig(mockImporterConfig);
	fakeimporterManager.setSolRSynchroniser(mockSolRSynchroniser);

	IGisDao<? extends GisFeature>[] daoList = new IGisDao[2];
	daoList[0] = mockCityDao;
	daoList[1] = mockGisDao;
	fakeimporterManager.setIDaos(daoList);
	assertFalse(fakeimporterManager.isAlreadyDone());
	assertFalse(fakeimporterManager.isInProgress());
	fakeimporterManager.importAll();
	assertTrue(fakeimporterManager.isAlreadyDone());
	assertFalse(fakeimporterManager.isInProgress());
	fakeimporterManager.resetImport();
	assertFalse(fakeimporterManager.isAlreadyDone());
	assertFalse(fakeimporterManager.isInProgress());
    }

    @Test
    public void testImportAdm2WithbadGisFeatureIdFormatShouldNotThrows() {
	// save option
	String savedAdmFileName = importerConfig.getAdm2FileName();
	AdmExtracterStrategyOptions admStrategy = importerConfig
		.getAdm2ExtracterStrategyIfAlreadyExists();
	// set option

	this.importerConfig
		.setAdm2FileName(ADM2_FILENAME_WITH_BAD_FORMAT_GISFEATUREID);
	this.importerConfig
		.setAdm2ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions.skip);

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	try {
	    this.geonamesAdm2Importer.process();
	    commitAndOptimize();
	} catch (GeonamesProcessorException e) {
	    fail("adm2 importer with wrong featureId format should not throw");

	} finally {
	    // restore option
	    importerConfig.setAdm2FileName(savedAdmFileName);
	    importerConfig.setAdm2ExtracterStrategyIfAlreadyExists(admStrategy);
	}
    }

    @Test
    public void testImportWithoutEmbededAlternateNamesShouldProcessAndIgnoreEmbededAlternateNames() {
	try {
	    this.importerConfig.setImportGisFeatureEmbededAlternateNames(false);
	    importall();

	} catch (GeonamesProcessorException e) {
	    e.printStackTrace();
	    fail(e.getCause() + " : " + e.getMessage());
	}

    }

    @Test
    public void testImportWhithEmbededAlternateNamesShouldProcessAndIgnoreAlternateNamesFiles() {
	try {
	    this.importerConfig.setImportGisFeatureEmbededAlternateNames(true);
	    importall();
	} catch (GeonamesProcessorException e) {
	    e.printStackTrace();
	    fail(e.getCause() + " : " + e.getMessage());
	}

    }

    //
    @Test
    /*
     * test retrieveFiles Option to false
     */
    public void testFileRetrieverShouldNotRetrieveAndUnzipFilesIfRetrieveFileOptionIsFalse() {
	// save option
	boolean savedOption = importerConfig.isRetrieveFiles();
	// set option
	this.importerConfig.setRetrieveFiles(false);

	try {
	    processAndCheckGeonamesFileRetriever();
	} catch (GeonamesProcessorException e) {
	    fail("an error occurred during file retriever with retrieveFile Option to false ");

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedOption);
	}
    }

    @Test
    /*
     * test retrieveFiles Option to true
     */
    public void testFileRetrieverShouldRetrieveAndUnzipFilesIfRetrieveFileOptionIsTrue() {
	// save option
	boolean savedOption = importerConfig.isRetrieveFiles();
	// set option
	this.importerConfig.setRetrieveFiles(true);

	try {
	    processAndCheckGeonamesFileRetriever();
	} catch (GeonamesProcessorException e) {
	    fail("an error occurred during file retriever with retrieveFiles Option to true ");

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedOption);
	}
    }

    // test isTryToDetectAdmIfNotFound and SyncAdmCodesWithLinkedAdmOnes options
    @Test
    public void testImportWithIsTryToDetectAdmIfNotFoundOptionToTrueAndSyncAdmCodesWithLinkedAdmOnesToFalseShouldImport() {
	// save option
	boolean savedTTDOption = importerConfig.isTryToDetectAdmIfNotFound();
	boolean savedSACWLAOOption = importerConfig
		.isSyncAdmCodesWithLinkedAdmOnes();

	// set options
	importerConfig.setTryToDetectAdmIfNotFound(true);
	importerConfig.setSyncAdmCodesWithLinkedAdmOnes(false);

	try {
	    this.importerConfig.setImportGisFeatureEmbededAlternateNames(false);
	    importall();

	} catch (GeonamesProcessorException e) {
	    e.printStackTrace();
	    fail(e.getCause() + " : " + e.getMessage());
	} finally {
	    // restore option
	    importerConfig.setTryToDetectAdmIfNotFound(savedTTDOption);
	    importerConfig.setSyncAdmCodesWithLinkedAdmOnes(savedSACWLAOOption);
	}

    }

    @Test
    public void testImportWithIsTryToDetectAdmIfNotFoundOptionToTrueAndSyncAdmCodesWithLinkedAdmOnesToTrueShouldImport() {
	// save option
	boolean savedTTDOption = importerConfig.isTryToDetectAdmIfNotFound();
	boolean savedSACWLAOOption = importerConfig
		.isSyncAdmCodesWithLinkedAdmOnes();

	// set options
	importerConfig.setTryToDetectAdmIfNotFound(true);
	importerConfig.setSyncAdmCodesWithLinkedAdmOnes(true);

	try {
	    this.importerConfig.setImportGisFeatureEmbededAlternateNames(false);
	    importall();

	} catch (GeonamesProcessorException e) {
	    e.printStackTrace();
	    fail(e.getCause() + " : " + e.getMessage());
	} finally {
	    // restore option
	    importerConfig.setTryToDetectAdmIfNotFound(savedTTDOption);
	    importerConfig.setSyncAdmCodesWithLinkedAdmOnes(savedSACWLAOOption);
	}

    }

    @Test
    public void testImportWithIsTryToDetectAdmIfNotFoundOptionToFalseShouldImport() {
	// save option
	boolean savedTTDOption = importerConfig.isTryToDetectAdmIfNotFound();

	// set options
	importerConfig.setTryToDetectAdmIfNotFound(false);

	try {
	    this.importerConfig.setImportGisFeatureEmbededAlternateNames(false);
	    importall();

	} catch (GeonamesProcessorException e) {
	    e.printStackTrace();
	    fail(e.getCause() + " : " + e.getMessage());
	} finally {
	    // restore option
	    importerConfig.setTryToDetectAdmIfNotFound(savedTTDOption);
	}

    }

    @Test
    public void testExportAdmWithWrongNumberOfFieldsThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedOption = importerConfig.isWrongNumberOfFieldsThrows();
	// set option
	this.importerConfig.setWrongNumberOfFieldsThrows(true);
	assertTrue("WrongNumberOfFieldsThrows has been set to true but is "
		+ importerConfig.isWrongNumberOfFieldsThrows(), importerConfig
		.isWrongNumberOfFieldsThrows());
	processAndCheckGeonamesFileRetriever();
	try {
	    processAndCheckAdmExtracter();
	    fail("the wrongNumberOfFieldsThrows option should be set to true and is "
		    + importerConfig.isWrongNumberOfFieldsThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "The cause of the exception is not of type WrongNumberOfFieldsException",
		    e.getCause().getClass(), WrongNumberOfFieldsException.class);

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedOption);
	}
    }

    @Test
    public void testImportAdm1WithWrongNumberOfFieldsThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedWNOFOption = importerConfig.isWrongNumberOfFieldsThrows();
	String savedAdmFileName = importerConfig.getAdm1FileName();
	AdmExtracterStrategyOptions admStrategy = importerConfig
		.getAdm1ExtracterStrategyIfAlreadyExists();
	// set option

	this.importerConfig
		.setAdm1FileName(ADM1_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS);
	this.importerConfig
		.setAdm1ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions.skip);

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	this.importerConfig.setWrongNumberOfFieldsThrows(true);
	try {
	    processAndCheckGeonamesAdm1Importer();
	    fail("the wrongNumberOfFieldsThrows option should be set to true and is "
		    + importerConfig.isWrongNumberOfFieldsThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "The cause of the exception is not of type WrongNumberOfFieldsException",
		    e.getCause().getClass(), WrongNumberOfFieldsException.class);

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedWNOFOption);
	    importerConfig.setAdm1FileName(savedAdmFileName);
	    importerConfig.setAdm1ExtracterStrategyIfAlreadyExists(admStrategy);
	}
    }

    @Test
    public void testImportAdm2WithWrongNumberOfFieldsThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedWNOFOption = importerConfig.isWrongNumberOfFieldsThrows();
	String savedAdmFileName = importerConfig.getAdm2FileName();
	AdmExtracterStrategyOptions admStrategy = importerConfig
		.getAdm2ExtracterStrategyIfAlreadyExists();
	// set option

	this.importerConfig
		.setAdm2FileName(ADM2_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS);
	this.importerConfig
		.setAdm2ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions.skip);

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	this.importerConfig.setWrongNumberOfFieldsThrows(true);
	try {
	    processAndCheckGeonamesAdm2Importer();
	    fail("the wrongNumberOfFieldsThrows option should be set to true and is "
		    + importerConfig.isWrongNumberOfFieldsThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "the cause of the exception is not of type WrongNumberOfFieldsException",
		    e.getCause().getClass(), WrongNumberOfFieldsException.class);

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedWNOFOption);
	    importerConfig.setAdm2FileName(savedAdmFileName);
	    importerConfig.setAdm2ExtracterStrategyIfAlreadyExists(admStrategy);
	}
    }

    @Test
    public void testImportAdm3WithWrongNumberOfFieldsThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedWNOFOption = importerConfig.isWrongNumberOfFieldsThrows();
	String savedAdmFileName = importerConfig.getAdm3FileName();
	AdmExtracterStrategyOptions admStrategy = importerConfig
		.getAdm3ExtracterStrategyIfAlreadyExists();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	// set option
	this.importerConfig
		.setAdm3FileName(ADM3_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS);
	this.importerConfig.setWrongNumberOfFieldsThrows(true);
	this.importerConfig
		.setAdm3ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions.skip);
	try {
	    processAndCheckGeonamesAdm3Importer();
	    fail("the wrongNumberOfFieldsThrows option should be set to true and is "
		    + importerConfig.isWrongNumberOfFieldsThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "The cause of the exception is not of type WrongNumberOfFieldsException",
		    e.getCause().getClass(), WrongNumberOfFieldsException.class);

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedWNOFOption);
	    importerConfig.setAdm3FileName(savedAdmFileName);
	    importerConfig.setAdm3ExtracterStrategyIfAlreadyExists(admStrategy);
	}
    }

    @Test
    public void testImportAdm4WithWrongNumberOfFieldsThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedWNOFOption = importerConfig.isWrongNumberOfFieldsThrows();
	String savedAdmFileName = importerConfig.getAdm4FileName();
	AdmExtracterStrategyOptions admStrategy = importerConfig
		.getAdm4ExtracterStrategyIfAlreadyExists();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	// set options
	this.importerConfig
		.setAdm4FileName(ADM4_FILENAME_WITH_WRONG_NUMBER_OF_FIELDS);
	this.importerConfig.setWrongNumberOfFieldsThrows(true);
	this.importerConfig
		.setAdm4ExtracterStrategyIfAlreadyExists(AdmExtracterStrategyOptions.skip);
	try {
	    processAndCheckGeonamesAdm4Importer();
	    fail("the wrongNumberOfFieldsThrows option should be set to true and is "
		    + importerConfig.isWrongNumberOfFieldsThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "The cause of the exception is not of type WrongNumberOfFieldsException",
		    e.getCause().getClass(), WrongNumberOfFieldsException.class);

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedWNOFOption);
	    importerConfig.setAdm4FileName(savedAdmFileName);
	    importerConfig.setAdm4ExtracterStrategyIfAlreadyExists(admStrategy);
	}
    }

    @Test
    public void testImportFeaturesWithWrongNumberOfFieldsThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedOption = importerConfig.isWrongNumberOfFieldsThrows();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	processAndCheckGeonamesAdm4Importer();
	// set option
	this.importerConfig.setWrongNumberOfFieldsThrows(true);
	try {
	    processAndCheckGeonamesFeatureImporter();
	    fail("the wrongNumberOfFieldsThrows option should be set to true and is "
		    + importerConfig.isWrongNumberOfFieldsThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "The cause of the exception is not of type WrongNumberOfFieldsException",
		    e.getCause().getClass(), WrongNumberOfFieldsException.class);

	} finally {
	    // restore option
	    importerConfig.setWrongNumberOfFieldsThrows(savedOption);
	}
    }

    @Test
    public void testImportAlternateNamesWithWrongNumberOfFieldsThrowsOptionsToTrueShouldThrows() {
	// this test is unuseful because the format has optional fields
	// (shortNames,preferredNamed,...)
	return;
    }

    /* test missing fields */

    @Test
    public void testAdmExtracterWithMissingRequiredFieldThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();

	// set option
	this.importerConfig.setMissingRequiredFieldThrows(true);
	try {
	    processAndCheckAdmExtracter();
	    fail("The MissingRequiredFieldThrows option should be set to true and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "The cause of the exception is not of type MissingRequiredFieldThrows",
		    e.getCause().getClass(),
		    MissingRequiredFieldException.class);

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	}
    }

    @Test
    public void testAdmExtracterWithMissingRequiredFieldThrowsOptionsToFalseShouldNotThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();

	// set option
	this.importerConfig.setMissingRequiredFieldThrows(false);
	try {
	    processAndCheckAdmExtracter();
	} catch (GeonamesProcessorException e) {
	    fail("The MissingRequiredFieldThrows option should be set to false and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should nothas been throws");

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	}
    }

    @Test
    public void testImportAdm1WithMissingRequiredFieldThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm1FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	// set option
	this.importerConfig.setAdm1FileName(ADM1_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(true);
	try {
	    processAndCheckGeonamesAdm1Importer();
	    fail("the MissingRequiredFieldThrows option should be set to true and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "the cause of the exception is not of type MissingRequiredFieldThrows",
		    e.getCause().getClass(),
		    MissingRequiredFieldException.class);

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm1FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportAdm1WithMissingRequiredFieldThrowsOptionsToFalseShouldNotThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm1FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	// set option
	this.importerConfig.setAdm1FileName(ADM1_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(false);
	try {
	    processAndCheckGeonamesAdm1Importer();

	} catch (GeonamesProcessorException e) {
	    fail("the MissingRequiredFieldThrows option should be set to false and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should not has been throws");

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm1FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportAdm2WithMissingRequiredFieldThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm2FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	// set option
	this.importerConfig.setAdm2FileName(ADM2_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(true);
	try {
	    processAndCheckGeonamesAdm2Importer();
	    fail("the MissingRequiredFieldThrows option should be set to true and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "the cause of the exception is not of type MissingRequiredFieldThrows",
		    e.getCause().getClass(),
		    MissingRequiredFieldException.class);

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm2FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportAdm2WithMissingRequiredFieldThrowsOptionsToFalseShouldNotThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm2FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	// set option
	this.importerConfig.setAdm2FileName(ADM2_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(false);
	try {
	    processAndCheckGeonamesAdm2Importer();
	} catch (GeonamesProcessorException e) {
	    fail("the MissingRequiredFieldThrows option should be set to false and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should not has been throws");

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm2FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportAdm3WithMissingRequiredFieldThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm3FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	// set option
	this.importerConfig.setAdm3FileName(ADM3_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(true);
	try {
	    processAndCheckGeonamesAdm3Importer();
	    fail("the MissingRequiredFieldThrows option should be set to true and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "the cause of the exception is not of type MissingRequiredFieldThrows",
		    e.getCause().getClass(),
		    MissingRequiredFieldException.class);
	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm3FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportAdm3WithMissingRequiredFieldThrowsOptionsToFalseShouldNotThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm3FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	// set option
	this.importerConfig.setAdm3FileName(ADM3_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(false);
	try {
	    processAndCheckGeonamesAdm3Importer();

	} catch (GeonamesProcessorException e) {
	    fail("The MissingRequiredFieldThrows option should be set to false and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should not has been throws");
	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm3FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportAdm4WithMissingRequiredFieldThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm4FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	// set option
	this.importerConfig.setAdm4FileName(ADM4_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(true);
	try {
	    processAndCheckGeonamesAdm4Importer();
	    fail("the MissingRequiredFieldThrows option should be set to true and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "the cause of the exception is not of type MissingRequiredFieldThrows",
		    e.getCause().getClass(),
		    MissingRequiredFieldException.class);

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm4FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportAdm4WithMissingRequiredFieldThrowsOptionsToFalseShouldNotThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	String savedAdmFileName = importerConfig.getAdm4FileName();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	// set option
	try {
	    this.importerConfig
		    .setAdm4FileName(ADM4_FILENAME_WITH_MISSING_FIELDS);
	    this.importerConfig.setMissingRequiredFieldThrows(false);
	    processAndCheckGeonamesAdm4Importer();
	} catch (GeonamesProcessorException e) {
	    fail("the MissingRequiredFieldThrows option should be set to false and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should not has been throws");

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig.setAdm4FileName(savedAdmFileName);
	}
    }

    @Test
    public void testImportFeaturesWithMissingRequiredFieldThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	processAndCheckGeonamesAdm4Importer();
	// set option
	this.importerConfig.setMissingRequiredFieldThrows(true);
	try {
	    processAndCheckGeonamesFeatureImporter();
	    fail("the MissingRequiredFieldThrows option should be set to true and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "the cause of the exception is not of type MissingRequiredFieldThrows",
		    e.getCause().getClass(),
		    MissingRequiredFieldException.class);

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	}
    }

    @Test
    public void testImportFeaturesWithMissingRequiredFieldThrowsOptionsToFalseShouldNotThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();

	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	processAndCheckGeonamesAdm4Importer();
	// set option
	this.importerConfig.setMissingRequiredFieldThrows(false);
	try {
	    processAndCheckGeonamesFeatureImporter();
	} catch (GeonamesProcessorException e) {
	    fail("the MissingRequiredFieldThrows option should be set to false and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should not has been throws");
	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	}
    }

    @Test
    public void testImportAlternateNamesWithMissingRequiredFieldThrowsOptionsToTrueShouldThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	boolean savedIGFAOption = importerConfig
		.isImportGisFeatureEmbededAlternateNames();
	String savedFileName = importerConfig.getAlternateNamesFileName();
	// force alternateNames to be imported from alternatenames file

	importerConfig.setImportGisFeatureEmbededAlternateNames(false);
	processAndCheckGeonamesFileRetriever();
	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	processAndCheckGeonamesAdm4Importer();
	int allAlternateNamesSize = processAndCheckGeonamesFeatureImporter();

	// set option
	this.importerConfig
		.setAlternateNamesFileName(ALTERNATENAME_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(true);
	try {
	    processAndCheckGeonamesAlternateNamesImporter(allAlternateNamesSize);
	    fail("the MissingRequiredFieldThrows option should be set to true and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should has been throws");
	} catch (GeonamesProcessorException e) {
	    assertEquals(
		    "The cause of the exception is not of type MissingRequiredFieldThrows",
		    e.getCause().getClass(),
		    MissingRequiredFieldException.class);

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig
		    .setImportGisFeatureEmbededAlternateNames(savedIGFAOption);
	    importerConfig.setAlternateNamesFileName(savedFileName);
	}
    }

    @Test
    public void testImportAlternateNamesWithMissingRequiredFieldThrowsOptionsToFalseShouldNotThrows() {
	// save option
	boolean savedMRFOption = importerConfig.isMissingRequiredFieldThrows();
	boolean savedIGFAOption = importerConfig
		.isImportGisFeatureEmbededAlternateNames();
	String savedFileName = importerConfig.getAlternateNamesFileName();
	// force alternateNames to be imported from alternatenames file

	importerConfig.setImportGisFeatureEmbededAlternateNames(false);
	processAndCheckGeonamesFileRetriever();
	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	processAndCheckGeonamesAdm4Importer();
	int allAlternateNamesSize = processAndCheckGeonamesFeatureImporter();

	// set option
	this.importerConfig
		.setAlternateNamesFileName(ALTERNATENAME_FILENAME_WITH_MISSING_FIELDS);
	this.importerConfig.setMissingRequiredFieldThrows(false);
	try {
	    processAndCheckGeonamesAlternateNamesImporter(allAlternateNamesSize);

	} catch (GeonamesProcessorException e) {
	    fail("the MissingRequiredFieldThrows option should be set to false and is "
		    + importerConfig.isMissingRequiredFieldThrows()
		    + " and should not has been throws");

	} finally {
	    // restore option
	    importerConfig.setMissingRequiredFieldThrows(savedMRFOption);
	    importerConfig
		    .setImportGisFeatureEmbededAlternateNames(savedIGFAOption);
	    importerConfig.setAlternateNamesFileName(savedFileName);
	}
    }

    /**
     * test all the geonames processor in the correct order and check Expected
     * Results
     */
    private void importall() {
	processAndCheckGeonamesFileRetriever();
	processAndCheckAdmExtracter();
	processAndCheckGeonamesLanguageImporter();
	processAndCheckGeonamesCountryImporter();
	processAndCheckGeonamesAdm1Importer();
	processAndCheckGeonamesAdm2Importer();
	processAndCheckGeonamesAdm3Importer();
	processAndCheckGeonamesAdm4Importer();
	int allAlternateNamesSize = processAndCheckGeonamesFeatureImporter();
	processAndCheckGeonamesAlternateNamesImporter(allAlternateNamesSize);
    }

    /**
     * @param allAlternateNamesSize
     */
    private void processAndCheckGeonamesAlternateNamesImporter(
	    int allAlternateNamesSize) {
	this.geonamesAlternateNamesImporter.process();
	commitAndOptimize();
	List<AlternateName> allAlternateNamesAfterImport = this.alternateNameDao
		.getAll();
	assertNotNull(
		"The fulltextSearch should not return null result but empty List",
		allAlternateNamesAfterImport);
	if (importerConfig.isImportGisFeatureEmbededAlternateNames()) {
	    assertTrue(
		    "The alternateNames size : "
			    + allAlternateNamesAfterImport.size()
			    + ", should be the same after the alternateNamesImporter : "
			    + allAlternateNamesSize
			    + ", because ImportGisFeatureEmbededAlternateNames="
			    + importerConfig
				    .isImportGisFeatureEmbededAlternateNames()
			    + " and alternatenames files should be ignore",
		    allAlternateNamesSize == allAlternateNamesAfterImport
			    .size());
	} else {
	    assertTrue(
		    "The alternateNames size : "
			    + allAlternateNamesAfterImport.size()
			    + ", should not be the same after the alternateNamesImporter : "
			    + allAlternateNamesSize
			    + ", because ImportGisFeatureEmbededAlternateNames="
			    + importerConfig
				    .isImportGisFeatureEmbededAlternateNames()
			    + " and alternatenames files should not be ignore",
		    allAlternateNamesSize != allAlternateNamesAfterImport
			    .size());
	}

	// TODO v2 test some value
	// language etc

	// test fulltext
    }

    /**
     * @return the number of alternatenames imported
     */
    private int processAndCheckGeonamesFeatureImporter() {
	long countCountry;
	Country country;
	this.geonamesFeatureImporter.process();
	commitAndOptimize();

	// check that no unwanted Adm has been saved
	long countAdm = this.admDao.count();
	long nbADM1 = this.admDao.countByLevel(1);
	long nbADM2 = this.admDao.countByLevel(2);
	long nbADM3 = this.admDao.countByLevel(3);
	long nbADM4 = this.admDao.countByLevel(4);
	assertEquals(
		"wrong number of Adm1 found after all the admX importers have processed ",
		2, nbADM1);
	assertEquals(
		"wrong number of Adm2 found after all the admX importers have processed ",
		1, nbADM2);
	assertEquals(
		"wrong number of Adm3 found after all the admX importers have processed ",
		4, nbADM3);
	assertEquals(
		"wrong number of Adm4 found after all the admX importers have processed ",
		36, nbADM4);

	assertEquals(
		"wrong number of Adm found after all the admX importers have processed ",
		nbADM1 + nbADM2 + nbADM3 + nbADM4, countAdm);

	// check number of Countries
	countCountry = this.countryDao.count();
	assertEquals("Wrong number of countries found ", 243, countCountry);

	country = this.countryDao.getByIso3166Alpha2Code("FR");
	assertNotNull("The country 'FR' have not been imported", country);
	// check that Country specific fields are properly set
	assertEquals("wrong Area for country " + country, new Double(547030),
		country.getArea());
	// check capital is trimed and with correct value
	assertEquals("wrong capitalName for country " + country, "Paris",
		country.getCapitalName());
	assertEquals("wrong continent for country " + country, "EU", country
		.getContinent());
	assertEquals("wrong currencyCode for country " + country, "EUR",
		country.getCurrencyCode());
	assertEquals("wrong currencyName for country " + country, "Euro",
		country.getCurrencyName());
	assertEquals("wrong fipsCode for country " + country, "FR", country
		.getFipsCode());
	assertEquals("wrong iso3166Alpha2Code for country " + country, "FR",
		country.getIso3166Alpha2Code());
	assertEquals("wrong iso3166Alpha3Code for country " + country, "FRA",
		country.getIso3166Alpha3Code());
	assertEquals("wrong iso3166NumericCode for country " + country, 250,
		country.getIso3166NumericCode());
	assertEquals("wrong phonePrefix for country " + country, "+33", country
		.getPhonePrefix());
	assertEquals("wrong postalCodeMask " + country, "#####", country
		.getPostalCodeMask());
	assertEquals("wrong postalCodeRegex for country " + country,
		"^(\\d{5})$", country.getPostalCodeRegex());
	assertEquals("wrong tld for country, maybe it is not in lowercase "
		+ country, ".fr", country.getTld());
	// check that Country specific fields are properly set
	assertEquals("wrong featureId for country " + country,
		new Long(3017382), country.getFeatureId());
	assertEquals("wrong featureCode for country " + country, "PCLI",
		country.getFeatureCode());
	try {
	    assertNotNull(gisFeatureDao.getByFeatureId(4043988L));
	} catch (RuntimeException e1) {
	    fail("country with featureClass TERR should be detected as a country");
	}
	try {
	    assertNotNull(gisFeatureDao.getByFeatureId(4020092L));
	} catch (RuntimeException e1) {
	    fail("country with featureClass ADMD should be detected as a country");
	}
	try {
	    assertNotNull(gisFeatureDao.getByFeatureId(3577279L));
	} catch (RuntimeException e1) {
	    fail("country with featureClass PCLIX should be detected as a country");
	}
	try {
	    assertNotNull(gisFeatureDao.getByFeatureId(661485L));
	} catch (RuntimeException e1) {
	    fail("country with featureClass ISL should be detected as a country");
	}
	try {
	    assertNotNull(gisFeatureDao.getByFeatureId(2622009L));
	} catch (RuntimeException e1) {
	    fail("country with featureClass ISLS should be detected as a country");
	}

	assertEquals("wrong featureClass for country " + country, "A", country
		.getFeatureClass());
	assertEquals("wrong asciiname for country " + country,
		"Republic of France", country.getAsciiName());
	// assertEquals("wrong name for country, maybe it is not trimed " +
	// country, "Républic of France", toUTF8(country.getName()));
	assertEquals(
		"wrong name for country, maybe it is not trimed or the name is not the one of the countries.txt file(we don't want to update the name with the country file one"
			+ country, "France", toUTF8(country.getName()));
	assertEquals("wrong population for country " + country, new Integer(
		60656178), country.getPopulation());
	SimpleDateFormat dateFormatter = new SimpleDateFormat(
		Constants.GIS_DATE_PATTERN);
	Date expectedDate = null;
	try {
	    expectedDate = dateFormatter.parse("2007-10-08");
	} catch (ParseException e) {
	    fail("can not parse date 2007-10-08");
	}
	assertEquals("wrong modificationDate for country " + country,
		expectedDate, country.getModificationDate());
	assertNotNull("location can not be null for country" + country, country
		.getLocation());
	assertEquals("wrong latitude for country " + country, new Double(46),
		country.getLatitude());
	assertEquals("wrong longitude for country " + country, new Double(2.0),
		country.getLongitude());
	assertEquals("wrong gTopo30 for country " + country, new Integer(560),
		country.getGtopo30());
	assertEquals("wrong timeZone for country " + country, "Europe/Paris",
		country.getTimezone());
	// check that some field have not been set
	assertNull("adm1Code should be null for " + country, country
		.getAdm1Code());
	assertNull("adm2Code should be null for " + country, country
		.getAdm2Code());
	assertNull("adm3Code should be null for " + country, country
		.getAdm3Code());
	assertNull("adm4Code should be null for " + country, country
		.getAdm4Code());
	assertNull("adm1Name should be null for " + country, country
		.getAdm1Name());
	assertNull("adm2Name should be null for " + country, country
		.getAdm2Name());
	assertNull("adm3Name should be null for " + country, country
		.getAdm3Name());
	assertNull("adm4Name should be null for " + country, country
		.getAdm4Name());
	assertNull("adm should be null for " + country, country.getAdm());

	// check alternatenames depending on
	// ImportGisFeatureEmbededAlternateNames option
	List<AlternateName> alternateNames = country.getAlternateNames();
	if (importerConfig.isImportGisFeatureEmbededAlternateNames()) {
	    assertNotNull(
		    "the alternateNames for country should not be null for "
			    + country, alternateNames);
	    assertEquals("", 121, alternateNames.size());
	    for (AlternateName alternateName : alternateNames) {
		assertEquals("The source of AlternateName " + alternateName
			+ " should be set to Embeded ",
			AlternateNameSource.EMBEDED, alternateName.getSource());
		assertNotNull("An alternateName should have a name ",
			alternateName.getName());
	    }
	} else {
	    assertEquals(
		    "country "
			    + country
			    + "should not have alternateNames because ImportGisFeatureEmbededAlternateNames="
			    + importerConfig
				    .isImportGisFeatureEmbededAlternateNames(),
		    0, alternateNames.size());
	}

	// check ADM
	assertEquals(
		"Some Adm are missing. it seems that some have been deleted after they been updated by geonamesFeatureImporter",
		43, this.admDao.count());
	assertEquals("wrong number of Adm1 found", new Long(2).longValue(),
		this.admDao.countByLevel(1));
	assertEquals("wrong number of Adm2 found", new Long(1).longValue(),
		this.admDao.countByLevel(2));
	assertEquals("wrong number of Adm3 found", new Long(4).longValue(),
		this.admDao.countByLevel(3));
	assertEquals("wrong number of Adm4 found", new Long(36).longValue(),
		this.admDao.countByLevel(4));

	// check that Adm1 has been updated
	Adm adm = this.admDao.getAdm1("FR", "A8");
	assertEquals("wrong AsciiName for " + adm, "Region Ile-de-France", adm
		.getAsciiName());
	alternateNames = adm.getAlternateNames();
	if (importerConfig.isImportGisFeatureEmbededAlternateNames()) {
	    assertNotNull(
		    "The alternateNames should not be null because ImportGisFeatureEmbededAlternateNames="
			    + importerConfig
				    .isImportGisFeatureEmbededAlternateNames(),
		    alternateNames);
	    assertEquals("Wrong number of alternateNames for " + adm, 4,
		    alternateNames.size());
	} else {
	    assertTrue(
		    adm
			    + " should not have alternateNames beacause ImportGisFeatureEmbededAlternateNames="
			    + importerConfig
				    .isImportGisFeatureEmbededAlternateNames(),
		    alternateNames.size() == 0);
	}
	assertEquals("wrong name for " + adm, "Région Île-de-France",
		toUTF8(adm.getName()));
	assertEquals("wrong AsciiName for " + adm, "Region Ile-de-France", adm
		.getAsciiName());
	assertEquals("wrong Location for " + adm, 2.5, adm.getLocation().getX());
	assertEquals("wrong Location for " + adm, 48.5, adm.getLocation()
		.getY());
	assertEquals("wrong featureClass for " + adm, "A", adm
		.getFeatureClass());
	assertEquals("wrong featureCode for " + adm, "ADM1", adm
		.getFeatureCode());
	assertEquals("wrong Population for " + adm, new Integer(11341257), adm
		.getPopulation());
	assertEquals("wrong TimeZone for " + adm, "Europe/Paris", adm
		.getTimezone());
	assertEquals("wrong gtopo30 for " + adm, new Integer(75), adm
		.getGtopo30());
	expectedDate = null;
	try {
	    expectedDate = dateFormatter.parse("2007-08-03");
	} catch (ParseException e) {
	    fail("can not parse date 2007-08-03");
	}
	assertEquals("wrong ModificationDate for " + adm, expectedDate, adm
		.getModificationDate());
	assertEquals("wrong source for " + adm, GISSource.GEONAMES, adm
		.getSource());
	assertEquals("wrong featureId for " + adm, new Long(3012874), adm
		.getFeatureId());
	assertEquals("An Adm1 should have an adm1Name", "Région Île-de-France",
		toUTF8(adm.getAdm1Name()));
	assertNull("An Adm1 shouldn't have adm2Name", adm.getAdm2Name());
	assertNull("An Adm1 shouldn't have adm3Name", adm.getAdm3Name());
	assertNull("An Adm1 shouldn't have adm4Name", adm.getAdm4Name());

	assertNull("An Adm1 shouldn't have adm2 code", adm.getAdm2Code());
	assertNull("An Adm1 shouldn't have adm3 code", adm.getAdm3Code());
	assertNull("An Adm1 shouldn't have adm4 code", adm.getAdm4Code());

	// check adm2,3,4 we assume that if adm1 fields are ok then the others
	// Adm too because it is independant from the level
	// we just check that no adm are orphan exept adm1
	List<Adm> allAdm = this.admDao.getAll();
	for (Adm admtoCheck : allAdm) {
	    if (admtoCheck.getLevel() == 1) {
		assertNull("An Adm1 shouldn't have a parent " + admtoCheck
			+ " have a parent", admtoCheck.getParent());
	    } else {
		assertNotNull("only Adm1 shouldn't have a parent. "
			+ admtoCheck + " don't", admtoCheck.getParent());
	    }
	    assertNull("An Adm shouldn't be linked to an Adm ", admtoCheck
		    .getAdm());
	}

	GisFeature flexGisFeature = this.gisFeatureDao.getByFeatureId(2969218L);
	GisFeature nearestParentGisFeature = this.gisFeatureDao
		.getByFeatureId(2969221L);
	GisFeature wrongParentGisFeature = this.gisFeatureDao
		.getByFeatureId(2969222L);
	GisFeature gisFeatureWithUnknowAdm = this.gisFeatureDao
		.getByFeatureId(2969223L);

	// flexAdm1 (does not depends on isTryToDetectAdmIfNotFound
	Adm flexAdm = this.admDao.getAdmByCountryAndCodeAndLevel("FR", "92077",
		4).get(0);
	assertEquals(
		"The GisFeature "
			+ flexGisFeature
			+ " must be linked to a correct ADM "
			+ flexAdm
			+ " even if its Adm1code is 00 (autodetection of Adm1code if it is equals to 0)",
		flexAdm, flexGisFeature.getAdm());

	if (importerConfig.isTryToDetectAdmIfNotFound()) {

	    // nearestParent
	    Adm nearestParentAdm = this.admDao.getAdmByCountryAndCodeAndLevel(
		    "FR", "A8", 1).get(0);
	    assertEquals(
		    "The GisFeature "
			    + nearestParentGisFeature
			    + "must be linked to the nearest Adm Parent Found : "
			    + nearestParentAdm
			    + ", because adm2code is wrong and adm1code is ok so the nearestparent is the Adm1 ",
		    nearestParentAdm, nearestParentGisFeature.getAdm());

	    Adm wrongParentAdm = this.admDao.getAdmByCountryAndCodeAndLevel(
		    "FR", "92", 2).get(0);
	    assertEquals("The GisFeature " + wrongParentGisFeature
		    + "must be linked to the Adm : " + wrongParentAdm
		    + ", because adm2code is ok and adm1code is KO  ",
		    wrongParentAdm, wrongParentGisFeature.getAdm());

	    // test isSyncAdmCodesWithLinkedAdmOnes()
	    if (importerConfig.isSyncAdmCodesWithLinkedAdmOnes()) {
		assertEquals(nearestParentGisFeature.getAdm().getAdm1Code(),
			nearestParentGisFeature.getAdm1Code());
		assertEquals(nearestParentGisFeature.getAdm().getAdm2Code(),
			nearestParentGisFeature.getAdm2Code());
		assertEquals(wrongParentGisFeature.getAdm().getAdm1Code(),
			wrongParentGisFeature.getAdm1Code());
		assertEquals(wrongParentGisFeature.getAdm().getAdm2Code(),
			wrongParentGisFeature.getAdm2Code());
	    } else {
		assertEquals("A8", nearestParentGisFeature.getAdm1Code());
		assertEquals("99", nearestParentGisFeature.getAdm2Code());
		assertEquals("A8", nearestParentGisFeature.getAdm1Code());
		assertEquals("99", nearestParentGisFeature.getAdm2Code());

	    }
	    assertNull(nearestParentGisFeature.getAdm3Code());
	    assertNull(nearestParentGisFeature.getAdm4Code());

	    // unknow ADM
	    assertNull(
		    "The gisFeature "
			    + gisFeatureWithUnknowAdm
			    + " should not be linked to an Adm because no Adm exists with those codes",
		    gisFeatureWithUnknowAdm.getAdm());

	} else {
	    assertNull(
		    "The GisFeature "
			    + nearestParentGisFeature
			    + "must be linked to a null ADM because adm2code is wrong and adm1code is ok isTryToDetectAdmIfNotFound is "
			    + importerConfig.isTryToDetectAdmIfNotFound(),
		    nearestParentGisFeature.getAdm());
	    assertNull(
		    "The gisFeature "
			    + gisFeatureWithUnknowAdm
			    + " should not be linked to an Adm because no Adm exists with those codes",
		    gisFeatureWithUnknowAdm.getAdm());
	}

	// test acceptRegExString
	GisFeature acceptedGisFeature = this.gisFeatureDao
		.getByFeatureId(3000816L);
	assertNotNull(
		"the GisFeature of type V.FRST should be inserted according to the acceptRegExString option",
		acceptedGisFeature);
	GisFeature rejectedGisFeature = this.gisFeatureDao
		.getByFeatureId(3007862L);
	assertNull(
		"the GisFeature of type L.PRK should not be inserted according to the acceptRegExString option",
		rejectedGisFeature);

	// check that gisFeature with wrong featureCode have their featureCode
	// set to importerConfig.DEFAULT_FEATURE_CODE
	GisFeature gisFeatureWithNoFeatureCode = this.gisFeatureDao
		.getByFeatureId(3007830L);
	assertNotNull(
		"The gisFeature with featureid 3007830 should not be null even if his featureCode is not Correct",
		gisFeatureWithNoFeatureCode);
	assertEquals("The GisFeature 3007830 should have a featureCode="
		+ ImporterConfig.DEFAULT_FEATURE_CODE,
		ImporterConfig.DEFAULT_FEATURE_CODE,
		gisFeatureWithNoFeatureCode.getFeatureCode());

	// check that gisFeature with no featureclass have their featureclass
	// set to importerConfig.DEFAULT_FEATURE_CLASS
	GisFeature gisFeatureWithNoFeatureClass = this.gisFeatureDao
		.getByFeatureId(3007675L);
	assertNotNull(
		"The gisFeature 3007675 should not be null even if his featureClass is not Correct",
		gisFeatureWithNoFeatureClass);
	assertEquals("The GisFeature 3007675 should have a featureClass="
		+ ImporterConfig.DEFAULT_FEATURE_CLASS,
		ImporterConfig.DEFAULT_FEATURE_CLASS,
		gisFeatureWithNoFeatureClass.getFeatureClass());

	// check that gisFeature with no featureclass and no feature code have
	// their featureclass and featurecode to the defaultValue
	GisFeature gisFeatureWithNoFeatureClassAndNoFeatureCode = this.gisFeatureDao
		.getByFeatureId(3007676L);
	assertEquals("The GisFeature 3007676 should have a featureClass="
		+ ImporterConfig.DEFAULT_FEATURE_CLASS,
		ImporterConfig.DEFAULT_FEATURE_CLASS,
		gisFeatureWithNoFeatureClassAndNoFeatureCode.getFeatureClass());
	assertEquals("The GisFeature 3007676 should have a featureCode="
		+ ImporterConfig.DEFAULT_FEATURE_CODE,
		ImporterConfig.DEFAULT_FEATURE_CODE,
		gisFeatureWithNoFeatureClassAndNoFeatureCode.getFeatureCode());

	// wrong number of fields
	GisFeature gisFeatureWithWrongNumberOfFields = this.gisFeatureDao
		.getByFeatureId(3007677L);
	assertNull(
		"The GisFeature with featureid 3007677 should not be imported because it has wrong number of fields",
		gisFeatureWithWrongNumberOfFields);

	// abandonned places
	GisFeature abandonned = this.gisFeatureDao.getByFeatureId(3007678L);
	assertNull(
		"The GisFeature with featureid 3007678 should not be imported because it is abandoned populated place",
		abandonned);

	// destroyed place
	GisFeature destroyed = this.gisFeatureDao.getByFeatureId(3007679L);
	assertNull(
		"The GisFeature with featureid 3007679 should not be imported because it is a destroyed populated place",
		destroyed);

	// test fulltext

	// check gisFeature are linked to the correct adm
	// adm1
	GisFeature checkAdmGisFeature = this.cityDao.getByFeatureId(3000751L);
	Adm checkedAdm = this.admDao.getAdmByCountryAndCodeAndLevel("FR", "A8",
		1).get(0);
	assertNotNull("City with featureId 3000751 should not be null ",
		checkAdmGisFeature);
	assertEquals("Wrong Adm for " + checkAdmGisFeature, checkedAdm,
		checkAdmGisFeature.getAdm());
	// adm2
	checkAdmGisFeature = this.cityDao.getByFeatureId(2975469L);
	checkedAdm = this.admDao.getAdmByCountryAndCodeAndLevel("FR", "92", 2)
		.get(0);
	assertNotNull("City with featureId 2975469 should not be null ",
		checkAdmGisFeature);
	assertEquals("Wrong Adm for " + checkAdmGisFeature, checkedAdm,
		checkAdmGisFeature.getAdm());
	// adm3
	checkAdmGisFeature = this.cityDao.getByFeatureId(3037423L);
	checkedAdm = this.admDao.getAdmByCountryAndCodeAndLevel("FR", "921", 3)
		.get(0);
	assertNotNull("City with featureId 3037423 should not be null ",
		checkAdmGisFeature);
	assertEquals("Wrong Adm for " + checkAdmGisFeature, checkedAdm,
		checkAdmGisFeature.getAdm());
	// adm4 3016321
	checkAdmGisFeature = this.cityDao.getByFeatureId(3016321L);
	checkedAdm = this.admDao.getAdmByCountryAndCodeAndLevel("FR", "92036",
		4).get(0);
	assertNotNull("City with featureId 3016321 should not be null ",
		checkAdmGisFeature);
	assertEquals("Wrong Adm for " + checkAdmGisFeature, checkedAdm,
		checkAdmGisFeature.getAdm());

	// unused
	List<Adm> unusedAdms = this.admDao.getUnused();
	checkedAdm = this.admDao.getAdmByCountryAndCodeAndLevel("FR", "922", 3)
		.get(0);
	assertNotNull("the unused adm must not be null ", unusedAdms);
	assertEquals("Wrong number of unused Adms ", 20, unusedAdms.size());

	assertNotNull("A forest must have been saved according to the regexp",
		forestDao.getByFeatureId(3000816L));

	// check zipcode
	// city without zipCode
	City cityWithNoZipCode = this.cityDao.getByFeatureId(0000001L);
	assertNotNull("the city with no zip code should not be null",
		cityWithNoZipCode);
	assertNull("city with FeatureId 0000001 should not have zipcode",
		cityWithNoZipCode.getZipCode());
	// city with one zipCode
	City cityWithOneZipCode = this.cityDao.getByFeatureId(2974678L);
	assertNotNull("the city with one zip code should not be null",
		cityWithOneZipCode);
	assertEquals("city with FeatureId 2974678 should have a zipcode",
		"02310", cityWithOneZipCode.getZipCode());
	// city with two zipcode
	City cityWithTwoZipCode = this.cityDao.getByFeatureId(3015490L);
	assertNotNull("the city with two zip code should not be null",
		cityWithTwoZipCode);
	assertNull(
		"The city with featureId 3015490 should not have a zipcode because it must be ambiguous",
		cityWithTwoZipCode.getZipCode());

	// TODO test pplx have a zipcode

	// check alternateNames
	List<AlternateName> alternateNames0 = cityWithNoZipCode
		.getAlternateNames();
	assertNotNull(
		"The city with featureId 0000001 (0 zipcode and 1 alternateNames) should have alternateNames ",
		alternateNames0);
	if (importerConfig.isImportGisFeatureEmbededAlternateNames()) {
	    // nozipcodes
	    assertEquals(
		    "According to the ImportGisFeatureEmbededAlternateNames option, he city with featureId 0000001 (0 zipcode and 1 alternateNames) should have 1 alternateNames",
		    1, alternateNames0.size());
	    // check alternateNames for one zipcode
	    List<AlternateName> alternateNames1 = cityWithOneZipCode
		    .getAlternateNames();
	    assertNotNull(
		    "The city with featureId 2974678 (one zipcode and 6 alternateNames) should have alternateNames ",
		    alternateNames1);
	    assertEquals(
		    "The city with featureId 2974678 (one zipcode and 6 alternateNames) should have 7 alternateNames",
		    7, alternateNames1.size());

	    // check alternate names for two zipcodes
	    List<AlternateName> alternateNames2 = cityWithTwoZipCode
		    .getAlternateNames();
	    assertNotNull(
		    "The city with featureId 3015490 (two zipcodes and a name) should have alternateNames ",
		    alternateNames2);
	    assertEquals(
		    "The city with featureId 3015490 (two zipcodes and a name) should have 3 alternateNames",
		    3, alternateNames2.size());

	} else {
	    List<AlternateName> allAlternateNames = this.alternateNameDao
		    .getAll();
	    assertEquals(
		    "According to the ImportGisFeatureEmbededAlternateNames option, the city with featureId 0000001 (0 zipcode and 1 alternateNames) should have 0 alternateNames",
		    0, allAlternateNames.size());
	}

	// Check that an unkwown country won't throw and won't be imported
	Country unknowCountry = this.countryDao.getByFeatureId(-3017382L);
	assertNull(
		"Country with featureId = -3017382 should have been ignore because it has no entry in "
			+ importerConfig.getCountriesFileName(), unknowCountry);

	// check alternatenames for adm and country (already checked)

	// check that alternateNames shouldbe imported or not according to the
	// ImportGisFeatureEmbededAlternateNames option
	List<AlternateName> allAlternateNames = this.alternateNameDao.getAll();
	int allAlternateNamesSize = allAlternateNames.size();
	assertNotNull(
		"the getAll() method should not return null List but empty list",
		allAlternateNames);
	if (importerConfig.isImportGisFeatureEmbededAlternateNames()) {
	    assertTrue(
		    "the alternateNames size should not be 0 because ImportGisFeatureEmbededAlternateNames="
			    + importerConfig
				    .isImportGisFeatureEmbededAlternateNames(),
		    allAlternateNamesSize != 0);
	} else {
	    assertTrue(
		    "the alternateNames  size should  be 0 because ImportGisFeatureEmbededAlternateNames="
			    + importerConfig
				    .isImportGisFeatureEmbededAlternateNames(),
		    allAlternateNamesSize == 0);
	}

	// check that no entry correspond to the alternate Names that will be
	// imported
	if (importerConfig.isImportGisFeatureEmbededAlternateNames()) {
	    // test fulltextsearch for alternatenames that have benn sync
	    Map<Long, GisFeature> expectedresults = new HashMap<Long, GisFeature>();
	    // a zipCode
	    expectedresults = new HashMap<Long, GisFeature>();
	    expectedresults.put(2995597L, new GisFeature());
	    isSearchGivesResults("92430", null, null, expectedresults);
	    // a non ascii city name
	    expectedresults.put(2974678L, new GisFeature());
	    expectedresults.put(6451981L, new GisFeature());
	    isSearchGivesResults(toUTF8("Sèvres"), null, null, expectedresults);
	    // a non ascii alternateName for a city (not ascii)
	    expectedresults = new HashMap<Long, GisFeature>();
	    expectedresults.put(2981041L, new GisFeature());
	    isSearchGivesResults("La Montagne-Cherie", null, null,
		    expectedresults);

	} else {
	    isSearchGivesEmptyResults("alterantony", null, null);
	    isSearchGivesEmptyResults("92201", null, null);
	    isSearchGivesEmptyResults("Région parisienne", null, null);
	    isSearchGivesEmptyResults("FranceAlteres", null, null);
	    isSearchGivesEmptyResults("gésilloniata", null, null);
	    isSearchGivesEmptyResults("gesilloniciao", null, null);
	    isSearchGivesEmptyResults("gesillonrevolution", null, null);
	    isSearchGivesEmptyResults("Boulogne-Billancourt0", null, null);
	    isSearchGivesEmptyResults("Boulogne-Billancourt1", null, null);
	    isSearchGivesEmptyResults("Boulogne-Billancourt2", null, null);
	    isSearchGivesEmptyResults("Boulogne-Billancourt3", null, null);
	}
	return allAlternateNamesSize;
    }

    /**
     * 
     */
    private void processAndCheckGeonamesAdm4Importer() {
	Adm adm3;
	this.geonamesAdm4Importer.process();
	commitAndOptimize();
	// check adm is saved in Datastore
	Adm adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92012");
	checkAdmName(adm4, "Boulogne-Billancourt");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92014");
	checkAdmName(adm4, "Bourg-la-Reine");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92023");
	checkAdmName(adm4, "Clamart");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92032");
	checkAdmName(adm4, "Fontenay-aux-Roses");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92033");
	checkAdmName(adm4, "Garches");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92036");
	checkAdmName(adm4, "Gennevilliers");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92040");
	checkAdmName(adm4, "Issy-les-Moulineaux");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92044");
	checkAdmName(adm4, "Levallois-Perret");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92046");
	checkAdmName(adm4, "Malakoff");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92048");
	checkAdmName(adm4, "Meudon");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92049");
	checkAdmName(adm4, "Montrouge");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92050");
	checkAdmName(adm4, "Nanterre");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92051");
	checkAdmName(adm4, "Neuilly-sur-Seine");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92062");
	checkAdmName(adm4, "Puteaux");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92063");
	checkAdmName(adm4, "Rueil-Malmaison");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92071");
	checkAdmName(adm4, "Sceaux");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92072");
	checkAdmName(adm4, "Sèvres");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92073");
	checkAdmName(adm4, "Suresnes");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92077");
	checkAdmName(adm4, "Ville-d'Avray");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92078");
	checkAdmName(adm4, "Villeneuve-la-Garenne");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92002");
	checkAdmName(adm4, "Antony");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92020");
	checkAdmName(adm4, "Châtillon");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92025");
	checkAdmName(adm4, "Colombes");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92064");
	checkAdmName(adm4, "Saint-Cloud");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92007");
	checkAdmName(adm4, "Bagneux");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92022");
	checkAdmName(adm4, "Chaville");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92024");
	checkAdmName(adm4, "Clichy");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92004");
	checkAdmName(adm4, "Asnières-sur-Seine");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92026");
	checkAdmName(adm4, "Courbevoie");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92035");
	checkAdmName(adm4, "La Garenne-Colombes");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92060");
	checkAdmName(adm4, "Le Plessis-Robinson");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92019");
	checkAdmName(adm4, "Châtenay-Malabry");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92047");
	checkAdmName(adm4, "Marnes-la-Coquette");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "921", "92075");
	checkAdmName(adm4, "Vanves");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "923", "92076");
	checkAdmName(adm4, "Vaucresson");
	adm4 = this.admDao.getAdm4("FR", "A8", "92", "922", "92009");
	checkAdmName(adm4, "Bois-Colombes");
	// check codes, names, parent and child for this adm3 only
	// the mandatory codes are tested with the Dao

	assertNotNull("An Adm4 should be linked to an adm3 ", adm4.getParent());
	adm3 = this.admDao.getAdmByCountryAndCodeAndLevel("FR",
		adm4.getAdm3Code(), 3).get(0);
	assertEquals("An Adm4 should be linked to an adm3 ", adm3, adm4
		.getParent());
	assertNotNull("Adm3 " + adm3 + " should have children ", adm3
		.getChildren());
	assertEquals("wrong number of childreen for adm3 " + adm3, 15, adm3
		.getChildren().size());
    }

    private void commitAndOptimize() {
	this.solRSynchroniser.commit();
	this.solRSynchroniser.optimize();// to avoid too many open files
	// http://grep.codeconsult.ch/2006/07/18/lucene-too-many-open-files-explained/
    }

    /**
     * 
     */
    private void processAndCheckGeonamesAdm3Importer() {
	Adm adm2;
	this.geonamesAdm3Importer.process();
	commitAndOptimize();
	// check adm is saved in Datastore
	Adm adm3 = this.admDao.getAdm3("FR", "A8", "92", "921");
	checkAdmName(adm3, "Arrondissement d'Antony");

	adm3 = this.admDao.getAdm3("FR", "A8", "92", "6456890");
	checkAdmName(adm3, "Adm3 with last missing field");

	// check codes, names, parent and child for this adm3 only
	assertNull("An Adm3 shouldn't have adm4Name", adm3.getAdm4Name());

	assertNull("An Adm3 shouldn't have adm4 code", adm3.getAdm4Code());
	assertNotNull("An Adm3 should be linked to an adm2 ", adm3.getParent());
	adm2 = this.admDao.getAdmByCountryAndCodeAndLevel("FR",
		adm3.getAdm2Code(), 2).get(0);
	assertEquals("An Adm3 should be linked to an adm2 ", adm2, adm3
		.getParent());
	assertNotNull("Adm2 " + adm2 + " should have children ", adm2
		.getChildren());
	assertEquals("wrong number of childreen for Adm2 " + adm2, 4, adm2
		.getChildren().size());

	adm3 = this.admDao.getAdm3("FR", "A8", "92", "922");
	checkAdmName(adm3, "Arrondissement de Nanterre");

	adm3 = this.admDao.getAdm3("FR", "A8", "92", "923");
	checkAdmName(adm3, "Arrondissement de Boulogne-Billancourt");
    }

    /**
     * 
     */
    private void processAndCheckGeonamesAdm2Importer() {
	Adm adm1;
	this.geonamesAdm2Importer.process();
	commitAndOptimize();
	// check adm is saved in Datastore
	Adm adm2 = this.admDao.getAdm2("FR", "A8", "92");
	checkAdmName(adm2, "Département des Hauts-de-Seine");
	// check codes, names, parent and child for this adm2
	// the mandatory codes are tested with the Dao
	assertNull("An Adm2 shouldn't have adm3Name", adm2.getAdm3Name());
	assertNull("An Adm2 shouldn't have adm4Name", adm2.getAdm4Name());

	assertNull("An Adm2 shouldn't have adm3 code", adm2.getAdm3Code());
	assertNull("An Adm2 shouldn't have adm4 code", adm2.getAdm4Code());
	assertNotNull("An Adm2 should be linked to an adm1 ", adm2.getParent());
	adm1 = this.admDao.getAdmByCountryAndCodeAndLevel("FR",
		adm2.getAdm1Code(), 1).get(0);
	assertEquals("An Adm2 should be linked to an adm1 ", adm1, adm2
		.getParent());
	assertNotNull("Adm1 " + adm1 + " should have  child ", adm1
		.getChildren());
	assertEquals("Adm1 " + adm1 + " should have one child ", 1, adm1
		.getChildren().size());
	assertEquals("Adm1 " + adm1 + " should have one child ", adm2, adm1
		.getChildren().get(0));

	// do some other check because adm2 export format contains featureId and
	// AsciiName
	assertEquals("Departement des Hauts-de-Seine", adm2.getAsciiName());
	assertEquals(new Long(3013657), adm2.getFeatureId());
    }

    /**
     * 
     */
    private void processAndCheckGeonamesAdm1Importer() {
	this.geonamesAdm1Importer.process();
	commitAndOptimize();
	// check adm is saved in Datastore
	Adm adm1 = this.admDao.getAdm1("FR", "A8");
	checkAdmName(adm1, "Région Île-de-France");
	// check codes, names, parent and child for this adm1
	// the mandatory codes are tested with the Dao
	assertNull("An Adm1 shouldn't have adm2Name", adm1.getAdm2Name());
	assertNull("An Adm1 shouldn't have adm3Name", adm1.getAdm3Name());
	assertNull("An Adm1 shouldn't have adm4Name", adm1.getAdm4Name());

	assertNull("An Adm1 shouldn't have adm2 code", adm1.getAdm2Code());
	assertNull("An Adm1 shouldn't have adm3 code", adm1.getAdm3Code());
	assertNull("An Adm1 shouldn't have adm4 code", adm1.getAdm4Code());
	assertNull("An Adm1 shouldn't be linked to an Adm ", adm1.getAdm());
	assertNull("An Adm1 shouldn't have a parent ", adm1.getParent());
    }

    /**
     * 
     */
    private void processAndCheckGeonamesCountryImporter() {
	Language language;
	this.geonamesCountryImporter.process();
	long countCountry = this.countryDao.count();
	assertEquals("Wrong number of countries found ", 243, countCountry);
	// TODO v1 check country code of gisFeature
	// check name is trimed
	Country country = this.countryDao.getByName("France");
	assertNotNull(
		"No country found for France  maybe the country name is not trimed or the country haven't been imported",
		country);
	assertEquals(
		"The population haven't been set properly, maybe the comma haven't been replaces",
		new Integer("60656178"), country.getPopulation());
	assertEquals(
		"The area haven't been set properly, maybe the comma haven't been replaces",
		new Double("547030"), country.getArea());
	// check languages have been set
	List<Language> spokenLanguages = country.getSpokenLanguages();
	assertNotNull("GetSpokenLanguages should not have return a null List",
		spokenLanguages);
	assertEquals("Wrong number of spoken Languages", 5, spokenLanguages
		.size());
	language = this.languageDao.getByIso639Alpha2Code("FR");
	checkSpokenLanguages(spokenLanguages, language);
	language = this.languageDao.getByIso639Alpha2Code("DE");
	checkSpokenLanguages(spokenLanguages, language);
	language = this.languageDao.getByIso639Alpha2Code("BR");
	checkSpokenLanguages(spokenLanguages, language);
	language = this.languageDao.getByIso639Alpha2Code("CO");
	checkSpokenLanguages(spokenLanguages, language);
	language = this.languageDao.getByIso639Alpha2Code("OC");
	checkSpokenLanguages(spokenLanguages, language);
    }

    /**
     * 
     */
    private void processAndCheckGeonamesLanguageImporter() {
	this.geonamesLanguageImporter.process();
	commitAndOptimize();
	assertEquals(
		"All the Language have not been imported, the first line has not been ignored",
		7600, this.languageDao.count());
	Language lang = this.languageDao.getByIso639Alpha2Code("FR");
	assertEquals("French", lang.getIso639LanguageName());
	// check upperCase for iso-alpha2
	assertEquals("Iso 639 alpha2 code must be correct and in upper Case",
		"FR", lang.getIso639Alpha2LanguageCode());
	// check upperCase for iso-alpha3
	assertEquals("Iso 639 alpha3 code must be correct and in upperCase",
		"FRA", lang.getIso639Alpha3LanguageCode());
	Language language = this.languageDao.getByIso639Alpha2Code("FR");
	assertNotNull(
		"No Language found for CO, maybe the iso alpha2 Code have not been set o upper Case",
		language);
	language = this.languageDao.getByIso639Alpha3Code("FRA");
	assertNotNull(
		"No Language found for CO, maybe the iso alpha Code have not been set o upper Case",
		language);
    }

    /**
     * 
     */
    private void processAndCheckAdmExtracter() {
	// ADMExtracter
	this.admExtracter.process();

	// TODO v1 check adm1 et adm2

	// check ADM3
	String admFileLocation = this.importerConfig.getGeonamesDir()
		+ this.importerConfig.getAdm3FileName();
	File admFile = new File(admFileLocation);
	assertTrue("The adm3 File " + admFileLocation
		+ " has not been extracted ", admFile.exists());
	Map<String, String[]> admMap = null;
	try {
	    admMap = fileToMap(admFile);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}

	// check that Map has elements
	assertNotNull(admMap);
	assertEquals(4, admMap.size());

	// check File values
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.6456890",
		"Adm3 with last missing field");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921",
		"Duplicate Adm3");// should be Arrondissement d'Antony if
	// there is no duplicate
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922",
		"Arrondissement de Nanterre");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923",
		"Arrondissement de Boulogne-Billancourt");

	// check Adm4
	admFileLocation = this.importerConfig.getGeonamesDir()
		+ this.importerConfig.getAdm4FileName();
	admFile = new File(admFileLocation);
	assertTrue("The adm4 File " + admFileLocation
		+ " has not been extracted ", admFile.exists());
	try {
	    admMap = fileToMap(admFile);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}

	// check that Map has elements
	assertNotNull(admMap);
	assertEquals(37, admMap.size());

	checkFormatAndValuesOfExtractedAdm(admMap, "FR.AA.92.922.645689",
		"Adm4 with last missing field");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92012",
		"Boulogne-Billancourt");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92014",
		"Bourg-la-Reine");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92023",
		"Clamart");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92032",
		"Fontenay-aux-Roses");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92033",
		"Garches");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92036",
		"Gennevilliers");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92040",
		"Issy-les-Moulineaux");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92044",
		"Levallois-Perret");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92046",
		"Malakoff");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92048",
		"Meudon");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92049",
		"Montrouge");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92050",
		"Nanterre");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92051",
		"Neuilly-sur-Seine");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92062",
		"Puteaux");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92063",
		"Rueil-Malmaison");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92071",
		"Sceaux");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92072",
		"Sèvres");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92073",
		"Suresnes");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92077",
		"Ville-d'Avray");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92078",
		"Villeneuve-la-Garenne");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92002",
		"Antony");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92020",
		"Châtillon");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92025",
		"Colombes");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92064",
		"Saint-Cloud");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92007",
		"Bagneux");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92022",
		"Chaville");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92024",
		"Clichy");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92004",
		"Asnières-sur-Seine");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92026",
		"Courbevoie");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92035",
		"La Garenne-Colombes");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92060",
		"Le Plessis-Robinson");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92019",
		"Châtenay-Malabry");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92047",
		"Marnes-la-Coquette");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.921.92075",
		"Vanves");
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.923.92076",
		"Duplicate adm4");// should be vaucresson if there is no
	// duplicate
	checkFormatAndValuesOfExtractedAdm(admMap, "FR.A8.92.922.92009",
		"Bois-Colombes");
    }

    /**
     * 
     */
    private void processAndCheckGeonamesFileRetriever() {

	// create a temporary directory to download files
	File tempDir = GeolocTestHelper.createTempDir(this.getClass()
		.getSimpleName());

	// save geonamesdir in order to restore it
	String savedGeonamesDir = this.importerConfig.getGeonamesDir();

	// get files to download
	List<String> filesToDownload = this.importerConfig
		.getDownloadFilesListFromOption();

	this.importerConfig.setGeonamesDir(tempDir.getAbsolutePath());

	// check that the directory is ending with the / or \ according to the
	// System
	assertTrue("geonamesDir must ends with" + File.separator,
		importerConfig.getGeonamesDir().endsWith(File.separator));
	this.geonamesFileRetriever.process();

	// check that geonamesDownloadURL ends with '/' : normally "/" is added
	// if not
	assertTrue("geonamesDownloadURL must ends with '/' but was "
		+ importerConfig.getGeonamesDownloadURL(), importerConfig
		.getGeonamesDownloadURL().endsWith("/"));

	// check that files have been Downloaded
	File file = null;
	for (String fileToDownload : filesToDownload) {
	    file = new File(importerConfig.getGeonamesDir() + fileToDownload);
	    if (importerConfig.isRetrieveFiles()) {
		assertTrue("Le fichier " + fileToDownload
			+ " have not been downloaded in "
			+ importerConfig.getGeonamesDir(), file.exists());
	    } else {
		assertFalse("Le fichier " + fileToDownload
			+ " have been downloaded in "
			+ importerConfig.getGeonamesDir()
			+ " even if the option retrievefile is"
			+ importerConfig.isRetrieveFiles(), file.exists());
	    }
	}

	// check that files have been unzip
	for (String fileToDownload : filesToDownload) {
	    String fileNameWithTxtExtension = fileToDownload.substring(0,
		    (fileToDownload.length()) - 4)
		    + ".txt";
	    file = new File(importerConfig.getGeonamesDir()
		    + fileNameWithTxtExtension);
	    if (importerConfig.isRetrieveFiles()) {
		assertTrue("Le fichier " + fileNameWithTxtExtension
			+ " have not been unzip in "
			+ importerConfig.getGeonamesDir(), file.exists());
	    } else {
		assertFalse("Le fichier " + fileToDownload
			+ " have been unzip in "
			+ importerConfig.getGeonamesDir()
			+ " even if the option retrievefile is"
			+ importerConfig.isRetrieveFiles(), file.exists());
	    }
	}

	// delete temp dir
	assertTrue("the tempDir has not been deleted", GeolocTestHelper
		.DeleteNonEmptyDirectory(tempDir));

	// restore geonamesDir
	this.importerConfig.setGeonamesDir(savedGeonamesDir);

    }

    /**
     * 
     */
    private void isSearchGivesEmptyResults(String substring,
	    String countryCode, Class<?> clazz) {
	List<GisFeature> searchResults = this.gisFeatureDao.listFromText(
		substring, true);
	assertNotNull(
		"The fulltextSearch should not return null result but empty List",
		searchResults);
	assertTrue("The full text search '" + substring
		+ "' should return an Empty List ", 0 == searchResults.size());
    }

    /**
     * 
     */
    private void isSearchGivesResults(String substring, String countryCode,
	    Class<?> clazz, Map<Long, GisFeature> expectedFeatureId) {
	List<GisFeature> searchResults = this.gisFeatureDao
		.listAllFeaturesFromText(substring, true);
	assertNotNull(
		"The fulltextSearch should not return null result but empty List",
		searchResults);
	assertTrue("The full text search '" + substring
		+ "' should not return an Empty List ", 0 != searchResults
		.size());
	for (GisFeature gisFeature : searchResults) {
	    assertTrue(
		    "the fulltext search doesn't return the expected results "
			    + gisFeature.getFeatureId(), expectedFeatureId
			    .containsKey(gisFeature.getFeatureId()));
	}
    }

    private void checkSpokenLanguages(List<Language> spokenLanguages,
	    Language language) {
	assertNotNull("Could not find a language for "
		+ language.getIso639LanguageName(), language);
	assertTrue("Country should have " + language.getIso639LanguageName()
		+ " in his spoken languages", spokenLanguages
		.contains(language));
    }

    private void checkAdmName(Adm adm, String name) {
	assertNotNull("could not find Adm for " + name, adm);
	assertEquals("the adm " + adm + "should have the name " + name + ";",
		name, toUTF8(adm.getName()));
	List<GisFeature> searchResult = this.gisFeatureDao.listFromText(name,
		true);
	assertNotNull("The fulltext search " + name
		+ " should not return a null list", searchResult);// todo
	// v1
	// test
	// +test
	// maj+
	// -
	assertEquals(
		"The fulltextsearch "
			+ name
			+ " should return 0 result because we don't synchronise dirty feature",
		0, searchResult.size());
    }

    private void checkFormatAndValuesOfExtractedAdm(
	    Map<String, String[]> admMap, String key, String value) {
	String[] line = admMap.get(key);
	assertNotNull("No ADM is found for " + key, line);
	assertEquals("The format of the adm3 file is incorrect", 2, line.length);
	assertTrue("The name of " + line[1] + " is not trimed",
		isTrimed(line[1]));
	assertEquals("The name is not set properly for " + key + " : " + value,
		value, toUTF8(line[1]));
    }

    private boolean isTrimed(String string) {
	return !string.startsWith(" ") && !string.endsWith(" ");
    }

    /**
     * @param file
     * @return a Map with first field (featureId in most case) as key and the
     *         splitted fields as value
     */
    private Map<String, String[]> fileToMap(File file) {
	Map<String, String[]> returnMap = new HashMap<String, String[]>();
	InputStream inputStream = null;
	BufferedReader reader = null;
	try {
	    inputStream = new BufferedInputStream(new FileInputStream(file));
	    reader = new BufferedReader(new InputStreamReader(inputStream,
		    Constants.CHARSET));
	    String line = null;
	    do {
		line = reader.readLine();
		if (line != null && !line.trim().equals("")) {
		    String[] fields = line.split("\t");
		    returnMap.put(fields[0], fields);
		}

	    } while (line != null);
	} catch (FileNotFoundException e) {
	    fail();
	} catch (UnsupportedEncodingException e) {
	    fail();
	} catch (IOException e) {
	    fail();
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (IOException e) {
		    fail();
		}
	    }
	    if (reader != null) {
		try {
		    reader.close();
		} catch (IOException e) {
		    fail();
		    e.printStackTrace();
		}
	    }
	}

	return returnMap;
    }

    @Required
    public void setGeonamesAdm1Importer(IGeonamesProcessor geonamesAdm1Importer) {
	this.geonamesAdm1Importer = geonamesAdm1Importer;
    }

    @Required
    public void setGeonamesAdm2Importer(IGeonamesProcessor geonamesAdm2Importer) {
	this.geonamesAdm2Importer = geonamesAdm2Importer;
    }

    @Required
    public void setAdmExtracter(IGeonamesProcessor admExtracter) {
	this.admExtracter = admExtracter;
    }

    @Required
    public void setGeonamesAdm3Importer(IGeonamesProcessor geonamesAdm3Importer) {
	this.geonamesAdm3Importer = geonamesAdm3Importer;
    }

    @Required
    public void setGeonamesAdm4Importer(IGeonamesProcessor geonamesAdm4Importer) {
	this.geonamesAdm4Importer = geonamesAdm4Importer;
    }

    @Required
    public void setGeonamesCountryImporter(
	    IGeonamesProcessor geonamesCountryImporter) {
	this.geonamesCountryImporter = geonamesCountryImporter;
    }

    @Required
    public void setGeonamesFeatureImporter(
	    IGeonamesProcessor geonamesFeatureImporter) {
	this.geonamesFeatureImporter = geonamesFeatureImporter;
    }

    @Required
    public void setGeonamesLanguageImporter(
	    IGeonamesProcessor geonamesLanguageImporter) {
	this.geonamesLanguageImporter = geonamesLanguageImporter;
    }

    /**
     * @param geonamesAlternateNamesImporter
     *                the geonamesAlternateNamesImporter to set
     */
    @Required
    public void setGeonamesAlternateNamesImporter(
	    IGeonamesProcessor geonamesAlternateNamesImporter) {
	this.geonamesAlternateNamesImporter = geonamesAlternateNamesImporter;
    }

    /**
     * @param importerConfig
     *                the importerConfig to set
     */
    @Required
    public void setImporterConfig(ImporterConfig importerConfig) {
	this.importerConfig = importerConfig;
    }

    /**
     * @param geonamesFileRetriever
     *                the geonamesFileRetriever to set
     */
    @Required
    public void setGeonamesFileRetriever(
	    IGeonamesProcessor geonamesFileRetriever) {
	this.geonamesFileRetriever = geonamesFileRetriever;
    }

    /**
     * @param admDao
     *                the admDao to set
     */
    @Required
    public void setAdmDao(IAdmDao admDao) {
	this.admDao = admDao;
    }

    /**
     * @param cityDao
     *                the cityDao to set
     */
    @Required
    public void setCityDao(ICityDao cityDao) {
	this.cityDao = cityDao;
    }

    /**
     * @param gisFeatureDao
     *                the gisFeatureDao to set
     */
    @Required
    public void setGisFeatureDao(IGisFeatureDao gisFeatureDao) {
	this.gisFeatureDao = gisFeatureDao;
    }

    /**
     * @param countryDao
     *                the countryDao to set
     */
    @Required
    public void setCountryDao(ICountryDao countryDao) {
	this.countryDao = countryDao;
    }

    /**
     * @param languageDao
     *                the languageDao to set
     */
    @Required
    public void setLanguageDao(ILanguageDao languageDao) {
	this.languageDao = languageDao;
    }

    @Required
    public void setAlternateNameDao(IAlternateNameDao alternateNameDao) {
	this.alternateNameDao = alternateNameDao;
    }

}
