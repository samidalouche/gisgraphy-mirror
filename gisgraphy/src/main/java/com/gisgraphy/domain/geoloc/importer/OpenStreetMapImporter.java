/**
 * 
 */
package com.gisgraphy.domain.geoloc.importer;

import java.io.File;
import java.util.List;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.repository.OpenStreetMapDao;
import com.gisgraphy.domain.valueobject.NameValueDTO;

/**
 * Import the street from an (pre-processed) openStreet map data file .
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class OpenStreetMapImporter extends AbstractGeonamesProcessor {
    
    private OpenStreetMapDao openStreetMapDao;

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#getFiles()
     */
    @Override
    protected File[] getFiles() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#getNumberOfColumns()
     */
    @Override
    protected int getNumberOfColumns() {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#processData(java.lang.String)
     */
    @Override
    protected void processData(String line) throws GeonamesProcessorException {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#setCommitFlushMode()
     */
    @Override
    protected void setCommitFlushMode() {
	// TODO Auto-generated method stub

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
