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
package com.gisgraphy.webapp.action;

import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.importer.IImporterManager;
import com.gisgraphy.domain.geoloc.importer.ImporterConfig;
import com.gisgraphy.domain.geoloc.importer.ImporterManager;
import com.gisgraphy.domain.geoloc.importer.ImporterMetaDataException;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IFullTextSearchEngine;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.helper.PropertiesHelper;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action that retrieve the configuration and
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * @see ImporterManager
 */
public class ImportConfirmAction extends ActionSupport {

    /**
     * Default serial ID
     */
    private static final long serialVersionUID = 2387732133217655558L;

    private ImporterConfig importerConfig;

    private IImporterManager importerManager;
    
    private String errorMessage = "";

    public static final String STATUS = "status";

    public static final String STEP_BASE_VIEW_NAME = "importWizardStep";

    private IFullTextSearchEngine fullTextSearchEngine;

    public int step_number;

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
	boolean alreadyDone;
	try {
	    alreadyDone = importerManager.isAlreadyDone();
	    if (importerManager.isInProgress() || alreadyDone) {
		return STATUS;
	    }
	} catch (ImporterMetaDataException e) {
	    errorMessage= e.getMessage();
	    return ERROR;
	}
	return super.execute();
    }

    public String step1() throws Exception {
	boolean alreadyDone;
	try {
	    alreadyDone = importerManager.isAlreadyDone();
	    if (importerManager.isInProgress() || alreadyDone) {
		return STATUS;
	    }
	} catch (ImporterMetaDataException e) {
	    errorMessage= e.getMessage();
	    return ERROR;
	}
	this.step_number = 1;
	return STEP_BASE_VIEW_NAME + step_number;
    }

    public String step2() throws Exception {
	boolean alreadyDone;
	try {
	    alreadyDone = importerManager.isAlreadyDone();
	    if (importerManager.isInProgress() || alreadyDone) {
		return STATUS;
	    }
	} catch (ImporterMetaDataException e) {
	    errorMessage= e.getMessage();
	    return ERROR;
	}
	this.step_number = 2;
	return STEP_BASE_VIEW_NAME + step_number;
    }

    /**
     * @return the importerConfig
     */
    public ImporterConfig getImporterConfig() {
	return importerConfig;
    }

    /**
     * @return true if the directory with the file to import exists and is
     *         accessible
     */
    public boolean isDownloadDirectoryAccessible() {
	return importerConfig.isGeonamesDownloadDirectoryAccessible();
    }

    /**
     * @return true if the directory with the file to import exists and is
     *         accessible
     */
    public boolean isOpenStreetMapDownloadDirectoryAccessible() {
	return importerConfig.isOpenStreetMapDownloadDirectoryAccessible();
    }

    /**
     * @return true if the regexp of the feature class/ code are correct
     */
    public boolean isRegexpCorrects() {
	return importerConfig.isRegexpCorrects();
    }

    /**
     * @return true if he fulltext search engine is alive
     */
    public boolean isFulltextSearchEngineAlive() {
	return fullTextSearchEngine.isAlive();
    }

    /**
     * @return true if he fulltext search engine is alive
     */
    public String getFulltextSearchEngineURL() {
	return fullTextSearchEngine.getURL();
    }

    /**
     * @return true if he Geonames importer is enabled
     */
    public boolean isGeonamesImporterEnabled() {
	return importerConfig.isGeonamesImporterEnabled();
    }

    /**
     * Disable Geonames importer
     */
    public String disableGeonamesImporter() {
	importerConfig.setGeonamesImporterEnabled(false);
	return Action.SUCCESS;
    }

    /**
     * Enable Geonames importer
     */
    public String enableGeonamesImporter() {
	importerConfig.setGeonamesImporterEnabled(true);
	return Action.SUCCESS;
    }

    /**
     * @return true if he openStreetMap importer is enabled
     */
    public boolean isOpenStreetMapImporterEnabled() {
	return importerConfig.isOpenstreetmapImporterEnabled();
    }

    /**
     * Disable OpenStreetMap importer
     */
    public String disableOpenStreetMapImporter() {
	importerConfig.setOpenstreetmapImporterEnabled(false);
	return Action.SUCCESS;
    }

    /**
     * Enable OpenStreetMap importer
     */
    public String enableOpenStreetMapImporter() {
	importerConfig.setOpenstreetmapImporterEnabled(true);
	return Action.SUCCESS;
    }

    /**
     * @param importerManager
     *            the importerManager to set
     */
    @Required
    public void setImporterManager(IImporterManager importerManager) {
	this.importerManager = importerManager;
    }

    /**
     * @param fullTextSearchEngine
     *            the fullTextSearchEngine to set
     */
    @Required
    public void setFullTextSearchEngine(IFullTextSearchEngine fullTextSearchEngine) {
	this.fullTextSearchEngine = fullTextSearchEngine;
    }

    public Map<String, String> getConfigValuesMap() {
	return PropertiesHelper.convertBundleToMap(ResourceBundle.getBundle(GisgraphyConfig.ENVIRONEMENT_PROPERTIES_FILE));
    }

    /**
     * @param importerConfig
     *            the importerConfig to set
     */
    public void setImporterConfig(ImporterConfig importerConfig) {
	this.importerConfig = importerConfig;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
