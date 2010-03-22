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

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.StringHelper;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Import the street from an (pre-processed) openStreet map data file .
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapImporter extends AbstractImporterProcessor {
	
	
    
    public static Long generatedId = 0L;
    
    private IOpenStreetMapDao openStreetMapDao;

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	openStreetMapDao.flushAndClear();

    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	return ImporterHelper.listCountryFilesToImport(importerConfig.getOpenStreetMapDir());
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	return 9;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#processData(java.lang.String)
     */
    @Override
    protected void processData(String line) throws ImporterException {
	String[] fields = line.split("\t");

	//
	// Line table has the following fields :
	// --------------------------------------------------- 
	//0: id; 1 name; 2 location; 3 length ;4 countrycode; 5 : gid ;
	//6 type; 7 oneway; 8 : shape;
	//
	checkNumberOfColumn(fields);
	OpenStreetMap street = new OpenStreetMap();
	
	// set name
	if (!isEmptyField(fields, 1, false)) {
	    street.setName(fields[1].trim());
	    StringHelper.updateOpenStreetMapEntityForIndexation(street);
	}

	if (!isEmptyField(fields, 2, false)) {
	    try {
		Point location = (Point) GeolocHelper.convertFromHEXEWKBToGeometry(fields[2]);
		street.setLocation(location);
	    } catch (RuntimeException e) {
		logger.warn("can not parse location for "+fields[1]+" : "+e);
	    }
	}
	
	if (!isEmptyField(fields, 3, false)) {
	    street.setLength(new Double(fields[3].trim()));
	}
	
	if (!isEmptyField(fields, 4, false)) {
	    street.setCountryCode(fields[4].trim());
	}
	
	generatedId= generatedId+1;
	street.setGid(new Long(generatedId));
	
	if (!isEmptyField(fields, 6, false)) {
	    StreetType type;
	    try {
		type = StreetType.valueOf(fields[6].toUpperCase());
		street.setStreetType(type);
	    } catch (Exception e) {
		logger.warn("can not determine streetType for "+fields[1]+" : "+e);
	    }
	    
	}
	
	if (!isEmptyField(fields, 7, false)) {
	    boolean oneWay = false;
	    try {
		oneWay  = Boolean.valueOf(fields[7]);
		street.setOneWay(oneWay);
	    } catch (Exception e) {
		logger.warn("can not determine streetType for "+fields[1]+" : "+e);
	    }
	    
	}
	
	if (!isEmptyField(fields, 8, true)) {
	    try {
		street.setShape((MultiLineString)GeolocHelper.convertFromHEXEWKBToGeometry(fields[8]));
	    } catch (RuntimeException e) {
		logger.warn("can not parse shape for "+fields[1] +" : "+e);
	    }
	    
	}
		
	openStreetMapDao.save(street);

    }
    
   
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldBeSkiped()
     */
    @Override
    public boolean shouldBeSkipped() {
	return !importerConfig.isOpenstreetmapImporterEnabled();
    }
    
   


    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#setCommitFlushMode()
     */
    @Override
    protected void setCommitFlushMode() {
    	this.openStreetMapDao.setFlushMode(FlushMode.COMMIT);
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldIgnoreComments()
     */
    @Override
    protected boolean shouldIgnoreComments() {
	return true;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractImporterProcessor#shouldIgnoreFirstLine()
     */
    @Override
    protected boolean shouldIgnoreFirstLine() {
	return false;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
    	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
    	logger.info("deleting openstreetmap entities...");
    	int deleted = openStreetMapDao.deleteAll();
    	if (deleted != 0) {
    	    deletedObjectInfo
    		    .add(new NameValueDTO<Integer>(openStreetMapDao.getPersistenceClass().getSimpleName(), deleted));
    	}
    	logger.info(deleted + " openstreetmap entities have been deleted");
    	resetStatus();
    	generatedId= 0L;
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
    @Override
    protected void tearDown() {
    	super.tearDown();
    	logger.info("building postgres fulltext fields...");
    	statusMessage=internationalisationService.getString("import.build.openstreetmap.fulltext.searchEngine");
    	int numberOfLineupdated = openStreetMapDao.updateTS_vectorColumnForStreetNameSearch();
    	logger.info(numberOfLineupdated + " fulltext field have been updated");
    	statusMessage="";
    }

   
    
}
