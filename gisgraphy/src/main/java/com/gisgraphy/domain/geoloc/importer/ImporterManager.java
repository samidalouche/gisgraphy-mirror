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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.hibernate.FlushMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient;
import com.gisgraphy.domain.repository.IDatabaseHelper;
import com.gisgraphy.domain.repository.IGisDao;
import com.gisgraphy.domain.repository.IImporterStatusListDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.valueobject.ImporterStatusDto;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.helper.FileHelper;

/**
 * Do the importing stuff
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class ImporterManager implements IImporterManager {

    private List<IImporterProcessor> importers = null;

    private ImporterConfig importerConfig;

    @Autowired
    IGisDao<? extends GisFeature>[] iDaos;

    private long startTime = 0;

    private long endTime = 0;

    private boolean inProgress = false;


    private ISolRSynchroniser solRSynchroniser;

    IImporterStatusListDao importerStatusListDao;
    
    @Autowired
    private IsolrClient solrClient;
    
    @Autowired
    private IDatabaseHelper databaseHelper;

    public static final String IMPORTER_METADATA_RELATIVE_PATH = "IMPORTER-METADATA-DO_NOT_REMOVE";


    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(ImporterManager.class);

    public static final String ALREADY_DONE_FILE_NAME = "importNotAlreadyDone";

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#importAll()
     */
    public synchronized void importAll() {
	if (this.inProgress == true) {
	    logger
		    .error("You can not run an import because an other one is in progress");
	    return;
	}
	if (isAlreadyDone() == true) {
	    logger
		    .error("You can not run an import because an other has already been done, if you want to run an other import, you must reset all the database and the fulltext search engine first");
	    return;
	}
	this.startTime = System.currentTimeMillis();
	importerStatusListDao.delete();
	try {
	    solrClient.setSolRLogLevel(Level.WARNING);
	    this.inProgress = true;
	    for (IImporterProcessor importer : importers) {
		logger.info("will now process "
			+ importer.getClass().getSimpleName());
		importer.process();
	    }
	    logger.info("end of import");
	} finally {
	    try {
		this.importerStatusListDao.saveOrUpdate(ComputeStatusDtoList());
	    } catch (RuntimeException e) {
		logger.error("Can not save statusDtoList : " + e.getMessage());
	    }
	    try {
		this.endTime = System.currentTimeMillis();
		this.inProgress = false;
		setAlreadyDone(true);
	    } catch (Exception e) {
		logger.error("The import is done but we can not persist the already done status : "+e.getMessage());
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#getStatusDtoList()
     */
    public List<ImporterStatusDto> getStatusDtoList() {
	try {
	    if (isInProgress()) {
		return ComputeStatusDtoList();
	    } else {
		return importerStatusListDao.get();
	    }
	} catch (RuntimeException e) {
	    logger.error("Can not retrieve or process statusDtoList : "
		    + e.getMessage());
	    return new ArrayList<ImporterStatusDto>();
	}
    }

    private List<ImporterStatusDto> ComputeStatusDtoList() {
	List<ImporterStatusDto> list = new ArrayList<ImporterStatusDto>();
	for (IImporterProcessor processor : importers) {
	    list.add(new ImporterStatusDto(processor));
	}
	return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#getImporters()
     */
    public List<IImporterProcessor> getImporters() {
	return importers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#getImporterConfig()
     */
    public ImporterConfig getImporterConfig() {
	return importerConfig;
    }

    /**
     * @return the time the last import took. If the import is in progress,
     *         returns the time it took from the beginning. If the import has
     *         not been started yet return 0.
     */
    public long getTimeElapsed() {
	if (this.startTime == 0) {
	    return 0;
	} else {
	    if (this.inProgress) {
		return (System.currentTimeMillis() - startTime) / 1000;
	    } else {
		return (this.endTime - this.startTime) / 1000;
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#getFormatedTimeElapsed()
     */
    public String getFormatedTimeElapsed() {
	return ImporterHelper.formatSeconds(getTimeElapsed());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#isInProgress()
     */
    public boolean isInProgress() {
	return inProgress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#isAlreadyDone()
     */
    public boolean isAlreadyDone() {
	return new File(getAlreadyDoneFilePath()).exists();
    }

    
    
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#resetImport()
     */
    public List<String> resetImport() throws Exception {
	solrClient.setSolRLogLevel(Level.WARNING);
	List<String> warningAndErrorMessage = new ArrayList<String>();

	    File tempDir = FileHelper.createTempDir(this.getClass().getSimpleName());
	    File fileToCreateTablesToReRunImport = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "createTables.sql");
	    File fileToDropTablesToReRunImport = new File(tempDir.getAbsolutePath() + System.getProperty("file.separator") + "dropTables.sql");
	    databaseHelper.generateSqlDropSchemaFileToRerunImport(fileToDropTablesToReRunImport);
	    databaseHelper.generateSQLCreationSchemaFileToRerunImport(fileToCreateTablesToReRunImport);
	    
	    List<String> dropErrorMessage = databaseHelper.execute(fileToDropTablesToReRunImport, true);
	    List<String> creationErrorMessage = databaseHelper.execute(fileToCreateTablesToReRunImport, true);
	    warningAndErrorMessage.addAll(dropErrorMessage);
	    warningAndErrorMessage.addAll(creationErrorMessage);
	    
	    resetFullTextSearchEngine();
	    setAlreadyDone(false);
	    for (IImporterProcessor importer :importers){
		importer.resetStatus();
	    }
	    importerStatusListDao.delete();
	    this.inProgress = false;
	return warningAndErrorMessage;

    }

    /**
     * @param deletedObjectInfo
     * @param importer
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void rollbackInTransaction(
	    List<NameValueDTO<Integer>> deletedObjectInfo,
	    IImporterProcessor importer) {
	deletedObjectInfo.addAll(importer.rollback());
    }

    private void setCommitFlushModeForAllDaos() {
	for (IGisDao<? extends GisFeature> gisDao : iDaos) {
	    gisDao.setFlushMode(FlushMode.COMMIT);
	}
    }

    /**
     * 
     */
    private void resetFullTextSearchEngine() {
	logger.info("will reset fulltext search engine");
	solRSynchroniser.deleteAll();
	logger.info("fulltext search engine has been reset");
	logger.info("end of reset");
    }

    /**
     * @param solRSynchroniser
     *                the solRSynchroniser to set
     */
    @Required
    public void setSolRSynchroniser(ISolRSynchroniser solRSynchroniser) {
	this.solRSynchroniser = solRSynchroniser;
    }

    /**
     * @param importerConfig
     *                The {@link ImporterConfig} to set
     */
    @Required
    public void setImporterConfig(ImporterConfig importerConfig) {
	this.importerConfig = importerConfig;
    }

    /**
     * @param importers
     *                The importers to process
     */
    @Required
    public void setImporters(List<IImporterProcessor> importers) {
	this.importers = importers;
    }

    /**
     * @param daos
     *                the iDaos to set
     */
    public void setIDaos(IGisDao<? extends GisFeature>[] daos) {
	iDaos = daos;
    }

    private void setAlreadyDone(boolean alreadyDone) {
	File alreadyDoneFile = new File(getAlreadyDoneFilePath());
	if (alreadyDone == false) {
	    if (!alreadyDoneFile.exists()) {
		try {
		    boolean created = alreadyDoneFile.createNewFile();
		    if (created == false) {
			throw new ImporterException("Can not change the already done status to " + alreadyDone);
		    }
		} catch (IOException e) {
		    throw new ImporterException("Can not change the already done status to " + alreadyDone + " : " + e);
		}
	    }

	} else {
	    if (alreadyDoneFile.exists()) {
		boolean deleted = alreadyDoneFile.delete();
		if (deleted == false) {
		    throw new ImporterException("Can not change the already done status to " + alreadyDone);
		}
	    }

	}
    }
    
    protected String getAlreadyDoneFilePath() {
	String dirpath = importerConfig.getGeonamesDir() + ImporterManager.IMPORTER_METADATA_RELATIVE_PATH + File.separator;
	File directory = new File(dirpath);
	if (!directory.exists()) {
	    if (!directory.mkdir()) {
		throw new RuntimeException("Can not create ImporterMetadataDirectory");
	    }
	}
	return dirpath + ALREADY_DONE_FILE_NAME;
    }
    
    /**
     * @param importerStatusListDao
     *                the importerStatusListDao to set
     */
    @Required
    public void setImporterStatusListDao(
	    IImporterStatusListDao importerStatusListDao) {
	this.importerStatusListDao = importerStatusListDao;
    }

    /**
     * @param solrClient the solrClient to set
     */
    @Required
    public void setSolrClient(IsolrClient solrClient) {
        this.solrClient = solrClient;
    }

    /**
     * @param databaseHelper the databaseHelper to set
     */
    @Required
    public void setDatabaseHelper(IDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    

}
