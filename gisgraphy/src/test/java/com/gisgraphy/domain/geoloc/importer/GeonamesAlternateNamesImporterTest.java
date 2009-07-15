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
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.ISpellCheckerIndexer;
import com.gisgraphy.domain.repository.IAlternateNameDao;
import com.gisgraphy.domain.repository.ISolRSynchroniser;
import com.gisgraphy.domain.repository.SolRSynchroniser;
import com.gisgraphy.domain.valueobject.ImporterStatus;
import com.gisgraphy.domain.valueobject.ImporterStatusDto;
import com.gisgraphy.domain.valueobject.NameValueDTO;

public class GeonamesAlternateNamesImporterTest extends AbstractIntegrationHttpSolrTestCase {
    
    private ImporterConfig importerConfig;
    
    private GeonamesAlternateNamesImporter geonamesAlternateNamesImporter;
    
    
    private ISpellCheckerIndexer spellCheckerIndexer;

  

    /**
     * @param solRSynchroniser the solRSynchroniser to set
     */
    public void setSolRSynchroniser(ISolRSynchroniser solRSynchroniser) {
        this.solRSynchroniser = solRSynchroniser;
    }

    /**
     * @param spellCheckerIndexer the spellCheckerIndexer to set
     */
    public void setSpellCheckerIndexer(ISpellCheckerIndexer spellCheckerIndexer) {
        this.spellCheckerIndexer = spellCheckerIndexer;
    }

    @Test
    public void testRollback() {
	GeonamesAlternateNamesImporter geonamesAlternateNamesImporter = new GeonamesAlternateNamesImporter();
	IAlternateNameDao alternateNameDao = EasyMock
		.createMock(IAlternateNameDao.class);
	EasyMock.expect(alternateNameDao.deleteAll()).andReturn(5);
	EasyMock.replay(alternateNameDao);
	geonamesAlternateNamesImporter.setAlternateNameDao(alternateNameDao);
	List<NameValueDTO<Integer>> deleted = geonamesAlternateNamesImporter
		.rollback();
	assertEquals(1, deleted.size());
	assertEquals(5, deleted.get(0).getValue().intValue());
    }
    
    @Test
    public void testTeardown(){
	ISolRSynchroniser mockSolRSynchroniser = EasyMock.createMock(ISolRSynchroniser.class);
	mockSolRSynchroniser.commit();
	EasyMock.expectLastCall();
	mockSolRSynchroniser.optimize();
	EasyMock.expectLastCall();
	ISpellCheckerIndexer mockSpellCheckerIndexer = EasyMock.createMock(ISpellCheckerIndexer.class);
	EasyMock.expect(mockSpellCheckerIndexer.buildAllIndex()).andReturn(new HashMap<String, Boolean>());
	EasyMock.replay(mockSolRSynchroniser);
	EasyMock.replay(mockSpellCheckerIndexer);
	GeonamesAlternateNamesImporter importer = new GeonamesAlternateNamesImporter();
	importer.setSolRSynchroniser(mockSolRSynchroniser);
	importer.setSpellCheckerIndexer(mockSpellCheckerIndexer);
	importer.tearDown();
	EasyMock.verify(mockSolRSynchroniser);
	EasyMock.verify(mockSpellCheckerIndexer);
    }

    
    @Test
    public void testTeardownShouldBeCalledWhateverImportGisFeatureEmbededAlternateNamesOptions(){
	boolean savedvalue = importerConfig.isImportGisFeatureEmbededAlternateNames();
	try {
	 //teardown must be called even if ImportGisFeatureEmbededAlternateNames is true
	importerConfig.setImportGisFeatureEmbededAlternateNames(true);
	ISolRSynchroniser mockSolRSynchroniser = EasyMock.createMock(ISolRSynchroniser.class);
	mockSolRSynchroniser.commit();
	EasyMock.expectLastCall();
	mockSolRSynchroniser.optimize();
	EasyMock.expectLastCall();
	ISpellCheckerIndexer mockSpellCheckerIndexer = EasyMock.createMock(ISpellCheckerIndexer.class);
	EasyMock.expect(mockSpellCheckerIndexer.buildAllIndex()).andReturn(new HashMap<String, Boolean>());
	EasyMock.replay(mockSolRSynchroniser);
	EasyMock.replay(mockSpellCheckerIndexer);
	
	geonamesAlternateNamesImporter.setSolRSynchroniser(mockSolRSynchroniser);
	geonamesAlternateNamesImporter.setSpellCheckerIndexer(mockSpellCheckerIndexer);
	geonamesAlternateNamesImporter.process();
	EasyMock.verify(mockSolRSynchroniser);
	EasyMock.verify(mockSpellCheckerIndexer);
	} finally {
	    importerConfig.setImportGisFeatureEmbededAlternateNames(savedvalue);
	    geonamesAlternateNamesImporter.setSolRSynchroniser(solRSynchroniser);
	    geonamesAlternateNamesImporter.setSpellCheckerIndexer(spellCheckerIndexer);
	}
    }
    
    @Test
    public void StatusShouldBeEqualsToSkipedIfisImportGisFeatureEmbededAlternateNamesIsTrue(){
	SolRSynchroniser solRSynchroniser = EasyMock.createNiceMock(SolRSynchroniser.class);
	ISpellCheckerIndexer spellChecker = EasyMock.createNiceMock(ISpellCheckerIndexer.class);
	
	GeonamesAlternateNamesImporter geonamesAlternateNamesImporter = new GeonamesAlternateNamesImporter();
	ImporterConfig importerConfig = new ImporterConfig();
	importerConfig.setImportGisFeatureEmbededAlternateNames(true);
	geonamesAlternateNamesImporter.setImporterConfig(importerConfig);
	geonamesAlternateNamesImporter.setSolRSynchroniser(solRSynchroniser);
	geonamesAlternateNamesImporter.setSpellCheckerIndexer(spellChecker);
	geonamesAlternateNamesImporter.process();
	Assert.assertEquals(ImporterStatus.SKIPED, geonamesAlternateNamesImporter.getStatus());
	ImporterStatusDto statusDto = new ImporterStatusDto(geonamesAlternateNamesImporter);
	Assert.assertEquals(100, statusDto.getPercent());
    }
    
     
    
    
    /**
     * @param importerConfig the importerConfig to set
     */
    @Required
    public void setImporterConfig(ImporterConfig importerConfig) {
        this.importerConfig = importerConfig;
    }

    /**
     * @param geonamesAlternateNamesImporter the geonamesAlternateNamesImporter to set
     */
    @Required
    public void setGeonamesAlternateNamesImporter(
    	GeonamesAlternateNamesImporter geonamesAlternateNamesImporter) {
        this.geonamesAlternateNamesImporter = geonamesAlternateNamesImporter;
    }
}
