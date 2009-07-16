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
 * Import the Adm of level 2 file. It is the first step of the adm2 import
 * process, the import will be complete when all the datastore object will be
 * updated by the {@link GeonamesFeatureImporter}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeonamesAdm2Importer extends AbstractGeonamesProcessor {

    private IAdmDao admDao;

    /**
     * Default constructor
     */
    public GeonamesAdm2Importer() {
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

	/*
	 * line table has the following fields :
	 * --------------------------------------------------- 0 code : 1 name 2
	 * asciiname 3 gisFeatureId
	 */

	// REM : tha adm2 export format (4 fields) is different from Adm1,3,4 (2
	// fields)
	checkNumberOfColumn(fields);
	// isEmptyField(fields,0,true);
	// isEmptyField(fields,1,true);
	Adm adm2 = new Adm(2);
	adm2.setLocation(GeolocHelper.createPoint(0F, 0F));
	if (!isEmptyField(fields, 3, true)) {
	    try {
		adm2.setFeatureId(new Long(fields[3]));
	    } catch (NumberFormatException e) {
		logger.warn(fields[3] + " is not a valid gisFeatureId");
		return;
	    }
	} else {
	    adm2.setFeatureId((++AbstractGeonamesProcessor.nbGisInserted) * -1);
	}
	adm2.setSource(GISSource.GEONAMES);

	// process code
	if (!isEmptyField(fields, 0, true)) {
	    String[] fullAdm2Code = fields[0].split("\\.");
	    // check the format
	    if (fullAdm2Code.length != 3) {
		logger.warn("adm2 importer needs code with 3 fields : "
			+ dumpFields(fullAdm2Code) + " is not correct");
		return;
	    }

	    String countryCode = fullAdm2Code[0].toUpperCase();
	    String adm1Code = fullAdm2Code[1];
	    String adm2Code = fullAdm2Code[2];

	    // set the code Value
	    if (!isEmptyField(fullAdm2Code, 0, true)) {
		adm2.setCountryCode(countryCode);
	    }
	    if (!isEmptyField(fullAdm2Code, 1, true)) {
		adm2.setAdm1Code(adm1Code);
	    }
	    if (!isEmptyField(fullAdm2Code, 2, true)) {
		adm2.setAdm2Code(adm2Code);
	    }

	    Adm duplicate = this.admDao
		    .getAdm2(countryCode, adm1Code, adm2Code);

	    if (duplicate != null) {
		logger
			.warn(adm2
				+ " will not be saved because it is duplicate (same codes) with "
				+ duplicate);
		return;
	    }

	    // try to get the parent Adm1

	    Adm adm1 = null;
	    if (false) {
		adm1 = this.admDao.suggestMostAccurateAdm(countryCode,
			adm1Code, null, null, null, null);
		logger.debug("suggested Adm=" + adm1);
	    } else {
		adm1 = this.admDao.getAdm1(countryCode, adm1Code);
	    }

	    // if found add to Adm1Childs
	    if (adm1 == null) {
		logger.warn("could not find adm1 with country=" + countryCode
			+ " and code=" + adm1Code + " for adm2 " + adm2);
		return;
	    } else {
		adm1.addChild(adm2);
	    }
	}

	if (!isEmptyField(fields, 2, false)) {
	    adm2.setAsciiName(fields[2].trim());
	}

	if (!isEmptyField(fields, 1, true)) {
	    adm2.setName(fields[1].trim());
	}

	// fallback if asciiName is not null and name is null
	if (isEmptyField(fields, 1, false) && !isEmptyField(fields, 2, false)) {
	    adm2.setName(fields[2].trim());
	}

	this.admDao.save(adm2);

    }
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#shouldBeSkiped()
     */
    @Override
    protected boolean shouldBeSkipped() {
	return !importerConfig.isGeonamesImporterEnabled();
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
	return 4;
    }

    /**
     * @param admDao
     *                The admDao to set
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
		+ importerConfig.getAdm2FileName());
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

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
	logger.info("deleting adm2...");
	int deletedadm = admDao.deleteAllByLevel(2);
	if (deletedadm != 0) {
	    deletedObjectInfo
		    .add(new NameValueDTO<Integer>("ADM2", deletedadm));
	}
	logger.info(deletedadm + " adm2s have been deleted");
	resetStatusFields();
	return deletedObjectInfo;
    }

}
