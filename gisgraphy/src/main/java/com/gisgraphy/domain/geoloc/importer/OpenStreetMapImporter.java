/**
 * 
 */
package com.gisgraphy.domain.geoloc.importer;

import java.io.File;
import java.util.List;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.repository.OpenStreetMapDao;
import com.gisgraphy.domain.valueobject.NameValueDTO;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

/**
 * Import the street from an (pre-processed) openStreet map data file .
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapImporter extends AbstractGeonamesProcessor {
    
    private static Long fakeId = 0L;
    
    private OpenStreetMapDao openStreetMapDao;

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	openStreetMapDao.flushAndClear();

    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	return ImporterHelper.listCountryFilesToImport(importerConfig.getOpenStreetMapDir());
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	return 9;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#processData(java.lang.String)
     */
    @Override
    protected void processData(String line) throws GeonamesProcessorException {
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
	}

	WKBReader wkReader = new WKBReader();
	if (!isEmptyField(fields, 2, false)) {
	    try {
		Point location = (Point) wkReader.read(hexToBytes(fields[2].trim()));
		street.setLocation(location);
	    } catch (ParseException e) {
		logger.warn("can not parse location for "+fields[1]+" : "+e);
	    }
	}
	
	if (!isEmptyField(fields, 3, false)) {
	    street.setLength(new Double(fields[3].trim()));
	}
	
	if (!isEmptyField(fields, 4, false)) {
	    street.setCountryCode(fields[4].trim());
	}
	
		fakeId= fakeId+1;
	        street.setGid(new Long(fakeId));
	
	if (!isEmptyField(fields, 6, false)) {
	    StreetType type;
	    try {
		type = StreetType.valueOf(fields[6]);
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
		street.setShape((MultiLineString)wkReader.read(hexToBytes(fields[8])));
	    } catch (ParseException e) {
		logger.warn("can not parse shape for "+fields[1] +" : "+e);
	    }
	    
	}
		
	openStreetMapDao.save(street);

    }
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#shouldBeSkiped()
     */
    @Override
    protected boolean shouldBeSkipped() {
	return !importerConfig.isOpenstreetmapImporterEnabled();
    }
    
    private byte[] hexToBytes(String wkb) {
	      // convert the String of hex values to a byte[]
	      byte[] wkbBytes = new byte[wkb.length() / 2];

	      for (int i = 0; i < wkbBytes.length; i++) {
	          byte b1 = getFromChar(wkb.charAt(i * 2));
	          byte b2 = getFromChar(wkb.charAt((i * 2) + 1));
	          wkbBytes[i] = (byte) ((b1 << 4) | b2);
	      }

	      return wkbBytes;
	    }


    
    /**
     * Turns a char that encodes four bits in hexadecimal notation into a byte
     *
     * @param c
     *
     */
    public static byte getFromChar(char c) {
        if (c <= '9') {
            return (byte) (c - '0');
        } else if (c <= 'F') {
            return (byte) (c - 'A' + 10);
        } else {
            return (byte) (c - 'a' + 10);
        }
    }



    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#setCommitFlushMode()
     */
    @Override
    protected void setCommitFlushMode() {
	openStreetMapDao.flushAndClear();

    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#shouldIgnoreComments()
     */
    @Override
    protected boolean shouldIgnoreComments() {
	return true;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#shouldIgnoreFirstLine()
     */
    @Override
    protected boolean shouldIgnoreFirstLine() {
	return false;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
	// TODO Auto-generated method stub
	return null;
    }
    
    /**
     * @param openStreetMapDao the openStreetMapDao to set
     */
    public void setOpenStreetMapDao(OpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }

   
    
}
