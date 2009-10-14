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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.repository.IGisDao;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.service.IInternationalisationService;

/**
 * Create the required index for all the Geonames databases
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public class GeonamesDatabaseIndexer implements IImporterProcessor {
    
    
    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(GeonamesDatabaseIndexer.class);
    
    @Autowired
    private IGisDao<? extends GisFeature>[] daos;
    
    @Autowired
    private ImporterConfig importerConfig;
    
    @Autowired
    private IInternationalisationService internationalisationService;
    
    private IGisDao<? extends GisFeature> currentDao;
    
    private int numberOfDaoThatHaveBeenIndexed = 0;
    
    private ImporterStatus status = ImporterStatus.WAITING;
    
    private String statusMessage = "";
    
    

    public String getCurrentFileName() {
	return currentDao == null ? "?" :this.currentDao.getPersistenceClass().getSimpleName();
    }

    public int getNumberOfLinesToProcess() {
	return currentDao == null ? 0 :this.daos.length;
    }

    public int getReadFileLine() {
	return numberOfDaoThatHaveBeenIndexed;
    }

    public ImporterStatus getStatus() {
	return status;
    }

    public String getStatusMessage() {
	return statusMessage;
    }

    public int getTotalReadLine() {
	return numberOfDaoThatHaveBeenIndexed;
    }

    public void process() {
	try {
	    if (shouldBeSkipped()){
		this.status = ImporterStatus.SKIPPED;
		return;
	    }
	    this.status = ImporterStatus.PROCESSING;
	    setup();
	   for (int i=0; i < daos.length;i++){
	       statusMessage = internationalisationService.getString("import.message.createIndex",new String[]{daos[i].getPersistenceClass().getSimpleName()});
	       daos[i].createGISTIndexForLocationColumn();
	   }
	    
	this.status = ImporterStatus.PROCESSED;
	this.statusMessage="";
	} catch (Exception e) {
	    e.printStackTrace();
	    this.status = ImporterStatus.ERROR;
	    this.statusMessage = "An error occurred when processing "
		    + this.getClass().getSimpleName() + " on dao "
		    + getCurrentFileName() + " : " + e.getCause();
	    logger.error(statusMessage);
	    throw new GeonamesProcessorException(statusMessage, e.getCause());
	} finally {
	    try {
		tearDown();
	    } catch (Exception e) {
		this.status = ImporterStatus.ERROR;
		this.statusMessage = "An error occured on teardown :"+e;
		logger.error(statusMessage);
	    }
	}

    }

    /**
     * Template method that can be override. This method is called before the
     * process start. it is not called for each file processed.
     */
    protected void setup() {
    }
    /**
     * Template method that can be override. This method is called after the end
     * of the process. it is not called for each file processed.
     * You should always call super.tearDown() when you overide this method
     */
    protected void tearDown() {
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.IImporterProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
	return new ArrayList<NameValueDTO<Integer>>();
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.IImporterProcessor#shouldBeSkipped()
     */
    public boolean shouldBeSkipped() {
	return !importerConfig.isGeonamesImporterEnabled();
    }

    /**
     * @param importerConfig the importerConfig to set
     */
    public void setImporterConfig(ImporterConfig importerConfig) {
        this.importerConfig = importerConfig;
    }

}
