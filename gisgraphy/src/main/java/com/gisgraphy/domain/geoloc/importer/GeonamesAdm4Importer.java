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
 * Import the Adm of level 4 file. It is the first step of the adm4 import
 * process, the import will be complete when all the datastore object will be
 * updated by the {@link GeonamesFeatureImporter}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeonamesAdm4Importer extends AbstractImporterProcessor {

    private IAdmDao admDao;

    /**
     * Default constructor
     */
    public GeonamesAdm4Importer() {
	super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#processData(java.lang.String)
     */
    @Override
    protected void processData(String line) {
	String[] fields = line.split("\t");

	/*
	 * line table has the following fields :
	 * --------------------------------------------------- 0 : code ; 1 :
	 * name
	 */
	checkNumberOfColumn(fields);
	Adm adm4 = new Adm(4);
	adm4.setLocation(GeolocHelper.createPoint(0F, 0F));
	adm4.setFeatureId((++AbstractImporterProcessor.nbGisInserted) * -1);
	adm4.setSource(GISSource.GEONAMES);

	// process code
	if (!isEmptyField(fields, 0, true)) {
	    String[] fullAdm4Code = fields[0].split("\\.");
	    // check the format
	    if (fullAdm4Code.length != 5) {
		logger.warn("adm4 importer needs code with 5 fields : "
			+ dumpFields(fullAdm4Code) + " is not correct");
		return;
	    }

	    if (!isEmptyField(fields, 1, true)) {
		adm4.setName(fields[1].trim());
	    }

	    String countryCode = fullAdm4Code[0].toUpperCase();
	    String adm1Code = fullAdm4Code[1];
	    String adm2Code = fullAdm4Code[2];
	    String adm3Code = fullAdm4Code[3];
	    String adm4Code = fullAdm4Code[4];

	    // set the code Value
	    if (!isEmptyField(fullAdm4Code, 0, true)) {
		adm4.setCountryCode(countryCode);
	    }
	    if (!isEmptyField(fullAdm4Code, 1, true)) {
		adm4.setAdm1Code(adm1Code);
	    }
	    if (!isEmptyField(fullAdm4Code, 2, true)) {
		adm4.setAdm2Code(adm2Code);
	    }
	    if (!isEmptyField(fullAdm4Code, 3, true)) {
		adm4.setAdm3Code(adm3Code);
	    }
	    if (!isEmptyField(fullAdm4Code, 4, true)) {
		adm4.setAdm4Code(adm4Code);
	    }

	    Adm duplicate = this.admDao.getAdm4(countryCode, adm1Code,
		    adm2Code, adm3Code, adm4Code);
	    if (duplicate != null) {
		logger
			.warn(adm4
				+ " will not be saved because it is duplicate (same codes) with "
				+ duplicate);
		return;
	    }

	    // try to get the parent Adm3
	    Adm adm3 = null;

	    adm3 = this.admDao.getAdm3(countryCode, adm1Code, adm2Code,
		    adm3Code);

	    // if found add to Adm3Childs
	    if (adm3 == null) {
		logger.warn("could not find adm3 for " + countryCode + "."
			+ adm1Code + "." + adm2Code + "." + adm3Code
			+ " for the adm4 " + adm4);
		return;
	    } else {
		adm3.addChild(adm4);
	    }
	}

	if (!isEmptyField(fields, 2, false)) {
	    adm4.setAsciiName(fields[2].trim());
	}

	// fallback if asciiName is not null and name is null
	if (isEmptyField(fields, 1, false) && !isEmptyField(fields, 2, false)) {
	    adm4.setName(fields[2].trim());
	}

	this.admDao.save(adm4);

    }
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldBeSkiped()
     */
    @Override
    protected boolean shouldBeSkipped() {
	return !importerConfig.isGeonamesImporterEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldIgnoreFirstLine()
     */
    @Override
    protected boolean shouldIgnoreFirstLine() {
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldIgnoreComments()
     */
    @Override
    protected boolean shouldIgnoreComments() {
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#setCommitFlushMode()
     */
    @Override
    protected void setCommitFlushMode() {
	this.admDao.setFlushMode(FlushMode.COMMIT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	this.admDao.flushAndClear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	return 2;
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
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	File[] files = new File[1];
	files[0] = new File(importerConfig.getGeonamesDir()
		+ importerConfig.getAdm4FileName());
	return files;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getMaxInsertsBeforeFlush()
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
	logger.info("deleting adm4...");
	int deletedadm = admDao.deleteAllByLevel(4);
	if (deletedadm != 0) {
	    deletedObjectInfo
		    .add(new NameValueDTO<Integer>("ADM4", deletedadm));
	}
	logger.info(deletedadm + " adm4s have been deleted");
	resetStatusFields();
	return deletedObjectInfo;
    }

}
