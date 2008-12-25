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

import com.gisgraphy.domain.geoloc.entity.Language;
import com.gisgraphy.domain.repository.ILanguageDao;
import com.gisgraphy.domain.valueobject.NameValueDTO;

/**
 * Import the languages.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeonamesLanguageImporter extends AbstractGeonamesProcessor {

    private ILanguageDao languageDao;

    /**
     * Default constructor
     */
    public GeonamesLanguageImporter() {
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
	 * --------------------------------------------------- 0 ISO 639-3 1 ISO
	 * 639-2 (unused here) 2 ISO 639-1 3 Language Name
	 */
	// checkNumberOfColumn(fields);
	Language lang = new Language();

	if (!isEmptyField(fields, 0, true)) {
	    lang.setIso639Alpha3LanguageCode(fields[0].toUpperCase());
	}

	if (!isEmptyField(fields, 2, false)) {
	    lang.setIso639Alpha2LanguageCode(fields[2].toUpperCase());
	}

	if (!isEmptyField(fields, 3, true)) {
	    lang.setIso639LanguageName(fields[3]);
	}

	this.languageDao.save(lang);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#shouldIgnoreFirstLine()
     */
    @Override
    protected boolean shouldIgnoreFirstLine() {
	return true;
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
	this.languageDao.setFlushMode(FlushMode.COMMIT);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.AbstractGeonamesProcessor#flushAndClear()
     */
    @Override
    protected void flushAndClear() {
	this.languageDao.flushAndClear();
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
     * @param languageDao
     *                the languageDao to set
     */
    @Required
    public void setLanguageDao(ILanguageDao languageDao) {
	this.languageDao = languageDao;
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
		+ importerConfig.getLanguageFileName());
	return files;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.importer.IGeonamesProcessor#rollback()
     */
    public List<NameValueDTO<Integer>> rollback() {
	List<NameValueDTO<Integer>> deletedObjectInfo = new ArrayList<NameValueDTO<Integer>>();
	logger.info("deleting languages...");
	int deletedlang = languageDao.deleteAll();
	if (deletedlang != 0) {
	    deletedObjectInfo.add(new NameValueDTO<Integer>(Language.class
		    .getSimpleName(), deletedlang));
	}
	logger.info(deletedlang + " languages have been deleted");
	resetStatusFields();
	return deletedObjectInfo;
    }

}
