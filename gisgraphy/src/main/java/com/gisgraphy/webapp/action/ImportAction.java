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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.importer.IImporterManager;
import com.gisgraphy.domain.geoloc.importer.ImporterManager;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.ImporterStatusDto;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action that do the all import
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * @see ImporterManager
 */
public class ImportAction extends ActionSupport {
    /**
     * 
     */
    private static final long serialVersionUID = -1459641055753044129L;

    private static Logger logger = LoggerFactory.getLogger(ImportAction.class);

    public static String WAIT = "wait";

    private IImporterManager importerManager;

    /**
     * Run import if not in progress or already done, otherwise return Wait view
     * 
     * @return 'success'
     * @throws Exception
     *                 When errors occurred
     */
    public String doImport() throws Exception {
	if (importerManager.isInProgress() || importerManager.isAlreadyDone()) {
	    return WAIT;
	}
	ImportAction.logger.info("doImport");
	this.importerManager.importAll();
	return Action.SUCCESS;
    }

    /**
     * @return 'Wait'
     * @throws Exception
     *                 if errors occurred
     */
    public String doWait() throws Exception {
	ImportAction.logger.debug("dowait");
	return WAIT;
    }

    /**
     * @return 'Wait'
     * @throws Exception
     *                 if errors occurred
     */
    public String status() throws Exception {
	ImportAction.logger.debug("dowait");
	return WAIT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
	return this.doImport();
    }
    
    public  ImporterStatus[] getStatusEnumList(){
	return ImporterStatus.values();
    }

    /**
     * @param importerManager
     *                the importerManager to set
     */
    @Required
    public void setImporterManager(IImporterManager importerManager) {
	this.importerManager = importerManager;
    }

    /**
     * @return the importerStatusDtoList
     */
    public List<ImporterStatusDto> getImporterStatusDtoList() {
	return importerManager.getStatusDtoList();
    }

    /**
     * @return the importerManager
     */
    @Required
    public IImporterManager getImporterManager() {
	return importerManager;
    }

}
