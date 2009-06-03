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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.FlushMode;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.repository.IGisDao;
import com.gisgraphy.domain.repository.IImporterStatusListDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.valueobject.ImporterStatusDto;
import com.gisgraphy.domain.valueobject.NameValueDTO;

/**
 * Do the importing stuff
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class ImporterManager implements IImporterManager {

    private List<IGeonamesProcessor> importers = null;

    private ImporterConfig importerConfig;

    @Autowired
    IGisDao<? extends GisFeature>[] iDaos;

    private long startTime = 0;

    private long endTime = 0;

    private boolean inProgress = false;

    private boolean alreadyDone = false;

    private ISolRSynchroniser solRSynchroniser;

    IImporterStatusListDao importerStatusListDao;

    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(ImporterManager.class);

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
	if (this.alreadyDone == true) {
	    logger
		    .error("You can not run an import because an other has already been done, if you want to run an other import, you must reset all the database and the fulltext search engine first");
	    return;
	}
	this.startTime = System.currentTimeMillis();
	importerStatusListDao.delete();
	try {
	    this.inProgress = true;
	    for (IGeonamesProcessor importer : importers) {
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
	    this.endTime = System.currentTimeMillis();
	    this.inProgress = false;
	    this.alreadyDone = true;
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
	for (IGeonamesProcessor processor : importers) {
	    list.add(new ImporterStatusDto(processor));
	}
	return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#getImporters()
     */
    public List<IGeonamesProcessor> getImporters() {
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
	return alreadyDone;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IImporterManager#resetImport()
     */
    public List<NameValueDTO<Integer>> resetImport() {
	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();

	List<IGeonamesProcessor> reverseImporters = importers;
	Collections.reverse(reverseImporters);
	setCommitFlushModeForAllDaos();
	for (IGeonamesProcessor importer : reverseImporters) {
	    logger.info("will reset " + importer.getClass().getSimpleName());
	    rollbackInTransaction(deletedObjectInfo, importer);
	}
	resetFullTextSearchEngine();
	this.alreadyDone = false;
	this.inProgress = false;
	return deletedObjectInfo;

    }

    /**
     * @param deletedObjectInfo
     * @param importer
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void rollbackInTransaction(
	    List<NameValueDTO<Integer>> deletedObjectInfo,
	    IGeonamesProcessor importer) {
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
    public void setImporters(List<IGeonamesProcessor> importers) {
	this.importers = importers;
    }

    /**
     * @param daos
     *                the iDaos to set
     */
    public void setIDaos(IGisDao<? extends GisFeature>[] daos) {
	iDaos = daos;
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

}
