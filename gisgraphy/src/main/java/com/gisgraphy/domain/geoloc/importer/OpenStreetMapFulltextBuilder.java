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
import java.util.ArrayList;
import java.util.List;

import org.hibernate.FlushMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.StringHelper;
import com.gisgraphy.service.IInternationalisationService;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * build the fulltext engine in order to use the street fulltext search
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapFulltextBuilder implements IImporterProcessor {

	protected ImporterStatus status = ImporterStatus.WAITING;

	protected ImporterConfig importerConfig;

	@Autowired
	private IInternationalisationService internationalisationService;

	private int lineProcessed = 0;

	private int numberOfLineToProcess = 0;
	/**
	 * The logger
	 */
	protected static final Logger logger = LoggerFactory.getLogger(OpenStreetMapFulltextBuilder.class);

	public static Long generatedId = 0L;

	private IOpenStreetMapDao openStreetMapDao;

	private String statusMessage;

	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear()
	 */
	protected void flushAndClear() {
		openStreetMapDao.flushAndClear();

	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
	 */
	public List<NameValueDTO<Integer>> rollback() {
		List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
		logger.info("deleting openstreetmap entities...");
		int deleted = openStreetMapDao.deleteAll();
		if (deleted != 0) {
			deletedObjectInfo.add(new NameValueDTO<Integer>(openStreetMapDao.getPersistenceClass().getSimpleName(), deleted));
		}
		logger.info(deleted + " openstreetmap entities have been deleted");
		resetStatus();
		generatedId = 0L;
		return deletedObjectInfo;
	}

	/**
	 * @param openStreetMapDao the openStreetMapDao to set
	 */
	public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
		this.openStreetMapDao = openStreetMapDao;
	}

	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#tearDown()
	 */
	protected void tearDown() {
		/*super.tearDown();
		logger.info("building postgres fulltext fields...");
		statusMessage=internationalisationService.getString("import.build.openstreetmap.fulltext.searchEngine");
		int numberOfLineupdated = openStreetMapDao.updateTS_vectorColumnForStreetNameSearch();
		logger.info(numberOfLineupdated + " fulltext field have been updated");*/
		statusMessage = "";
	}

	public String getCurrentFileName() {
		return this.getClass().getSimpleName();
	}

	public int getNumberOfLinesToProcess() {
		return numberOfLineToProcess;
	}

	public int getReadFileLine() {
		return lineProcessed;
	}

	public ImporterStatus getStatus() {
		return status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public int getTotalReadLine() {
		return lineProcessed;
	}

	public void process() {
		status = ImporterStatus.PROCESSING;
		try {
			if (!shouldBeSkipped()) {
				if (importerConfig.isRetrieveFiles()) {
					logger.info("OpenStreetMapFulltextBuilder will be skiped");
				}
				statusMessage = internationalisationService.getString("import.build.openstreetmap.fulltext.searchEngine");
				this.status = ImporterStatus.PROCESSED;
			} else {
				this.status = ImporterStatus.SKIPPED;
				logger.info("DownloadFiles option is set to false, we will not download and decompress files");
			}
			statusMessage = "";
		} catch (Exception e) {
			this.statusMessage = "error retrieving or decompres file : " + e.getMessage();
			logger.error(statusMessage);
			status = ImporterStatus.ERROR;
			throw new ImporterException(statusMessage, e);
		} finally {
			tearDown();
		}

	}

	public void resetStatus() {

	}

	public boolean shouldBeSkipped() {
		return !importerConfig.isOpenstreetmapImporterEnabled();
	}

}
