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
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.valueobject.GISSource;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.helper.GeolocHelper;

/**
 * Import the Adm of level 1 file. It is the first step of the Adm1 import
 * process, the import will be complete when all the datastore object will be
 * updated by the {@link GeonamesFeatureImporter}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeonamesAdm1Importer extends AbstractGeonamesProcessor {

    private IAdmDao admDao;

    /**
     * Default constructor
     */
    public GeonamesAdm1Importer() {
	super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#processData(java.lang.String)
     */
    @Override
    protected void processData(String line) {
	String[] fields = line.split("\t");

	//
	// Line table has the following fields :
	// --------------------------------------------------- 0 : Code ; 1 :
	// name
	//
	checkNumberOfColumn(fields);
	Adm adm = new Adm(1);
	String[] code = {};
	if (!isEmptyField(fields, 0, true)) {
	    code = fields[0].split("\\.");
	    if (!isEmptyField(code, 0, true)) {
		adm.setCountryCode(code[0].toUpperCase());
	    }
	    if (!isEmptyField(code, 1, true)) {
		adm.setAdm1Code(code[1]);
	    }
	}

	if (!isEmptyField(fields, 1, true)) {
	    adm.setName(fields[1].trim());
	}

	adm.setLocation(GeolocHelper.createPoint(0F, 0F));
	adm.setFeatureId((++AbstractGeonamesProcessor.nbGisInserted) * -1);
	adm.setSource(GISSource.GEONAMES);
	adm.setFeatureClass("A");
	adm.setFeatureCode("ADM1");

	Adm duplicate = this.admDao.getAdm1(code[0], code[1]);
	if (duplicate == null) {
	    // no adm1 with the same Adm1Code exist
	    this.admDao.save(adm);
	} else {
	    logger
		    .warn(adm
			    + " will not be saved because it is duplicate (same codes) with "
			    + duplicate);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#shouldIgnoreFirstLine()
     */
    @Override
    protected boolean shouldIgnoreFirstLine() {
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#shouldIgnoreComments()
     */
    @Override
    protected boolean shouldIgnoreComments() {
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#setCommitFlushMode()
     */
    @Override
    protected void setCommitFlushMode() {
	this.admDao.setFlushMode(FlushMode.COMMIT);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	this.admDao.flushAndClear();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	return 2;
    }

    /**
     * @param admDao
     *                the admDao to set
     */
    @Required
    public void setAdmDao(IAdmDao admDao) {
	this.admDao = admDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	File[] files = new File[1];
	files[0] = new File(importerConfig.getGeonamesDir()
		+ importerConfig.getAdm1FileName());
	return files;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#getMaxInsertsBeforeFlush()
     */
    @Override
    protected int getMaxInsertsBeforeFlush() {
	// we commit each times because we don't want duplicate adm and the
	// cache is NONSTRICT_READ_WRITE (assynchronous)
	return 1;
    }
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
	logger.info("deleting adm1...");
	int deletedadm = admDao.deleteAllByLevel(1);
	if (deletedadm != 0){
	    deletedObjectInfo.add(new NameValueDTO<Integer>("ADM1",deletedadm));
	}
	logger.info(deletedadm + " adm1s have been deleted");
	resetStatusFields();
	return deletedObjectInfo;
    }

}
