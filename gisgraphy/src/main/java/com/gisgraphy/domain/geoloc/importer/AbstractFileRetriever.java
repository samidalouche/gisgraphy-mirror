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
/**
 *
 */
package com.gisgraphy.domain.geoloc.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.NameValueDTO;

/**
 *  Base class to download files from a server
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public abstract class AbstractFileRetriever implements IGeonamesProcessor {

    protected ImporterConfig importerConfig;

    protected String currentFileName;

    protected ImporterStatus status = ImporterStatus.UNPROCESSED;

    protected int fileIndex = 0;

    protected int numberOfFileToDownload = 0;

    protected String statusMessage = "";

    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(AbstractFileRetriever.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#process()
     */
    public void process() throws GeonamesProcessorException {
	status = ImporterStatus.PROCESSING;
	try {
	    if (importerConfig.isRetrieveFiles()) {
		logger
			.info("DownloadFiles option is set to true, we will download and unzip files");
		List<String> downloadFileList = importerConfig
			.getDownloadFilesListFromOption();
		this.numberOfFileToDownload = downloadFileList.size();
		for (String file : downloadFileList) {
		    this.fileIndex++;
		    this.currentFileName = file;
		    ImporterHelper.download(getDownloadBaseUrl()
			    + file, getDownloadDirectory() + file);
		}

		decompressFiles();
	    } else {
		logger
			.info("DownloadFiles option is set to false, we will not download and unzip files");
	    }

	} catch (Exception e) {
	    this.statusMessage = "error retrieving or decompres file : " + e.getMessage();
	    logger.error(statusMessage);
	    status = ImporterStatus.ERROR;
	    throw new GeonamesProcessorException(statusMessage, e);
	} finally {
	    if (this.status != ImporterStatus.ERROR) {
		this.status = ImporterStatus.PROCESSED;
	    }
	}

    }

    /**
     * Method to call if files must be decompress (untar or unzip)
     * @throws IOException
     */
    public abstract void decompressFiles() throws IOException ;

    /** 
     * @return The directory where the file should be downloaded
     */
    public abstract String getDownloadDirectory();

    /**
     * @return the base URL from wich the file should be downloaded
     */
    public abstract String getDownloadBaseUrl() ;

   

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#getReadFileLine()
     */
    public int getReadFileLine() {
	return 0;
    }

    public int getTotalReadLine() {
	return this.fileIndex;
    }

    public String getCurrentFileName() {
	return this.currentFileName;
    }

    public int getNumberOfLinesToProcess() {
	return this.numberOfFileToDownload;
    }

    public ImporterStatus getStatus() {
	return this.status;
    }

    public String getStatusMessage() {
	return this.statusMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
	List<String> filesToImport = importerConfig
		.getDownloadFilesListFromOption();
	int deleted = 0;
	for (String fileName : filesToImport) {
	    File file = new File(getDownloadDirectory() + fileName);
	    if (file.delete()) {
		logger
			.info("the files " + file.getName()
				+ " has been deleted");
		deleted++;
	    } else {
		logger.info("the files " + file.getName()
			+ " hasn't been deleted");
	    }
	}
	deletedObjectInfo.add(new NameValueDTO<Integer>("Downloaded files",
		deleted));
	currentFileName = null;
	status = ImporterStatus.UNPROCESSED;
	fileIndex = 0;
	numberOfFileToDownload = 0;
	statusMessage = "";
	return deletedObjectInfo;
    }
    
    /**
     * @param importerConfig
     *                The importerConfig to set
     */
    @Required
    public void setImporterConfig(ImporterConfig importerConfig) {
	this.importerConfig = importerConfig;
    }

}
