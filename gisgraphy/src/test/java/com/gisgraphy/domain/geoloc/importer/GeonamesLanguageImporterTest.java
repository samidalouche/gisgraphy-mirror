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

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.gisgraphy.domain.repository.ILanguageDao;
import com.gisgraphy.domain.valueobject.NameValueDTO;

public class GeonamesLanguageImporterTest extends TestCase {

    public void testRollback() {
	GeonamesLanguageImporter geonamesLanguageImporter = new GeonamesLanguageImporter();
	ILanguageDao languageDao = EasyMock.createMock(ILanguageDao.class);
	EasyMock.expect(languageDao.deleteAll()).andReturn(4);
	EasyMock.replay(languageDao);
	geonamesLanguageImporter.setLanguageDao(languageDao);
	List<NameValueDTO<Integer>> deleted = geonamesLanguageImporter
		.rollback();
	assertEquals(1, deleted.size());
	assertEquals(4, deleted.get(0).getValue().intValue());
    }
    
    @Test
    public void testShouldBeSkipShouldReturnCorrectValue(){
	ImporterConfig importerConfig = new ImporterConfig();
	GeonamesLanguageImporter geonamesLanguageImporter = new GeonamesLanguageImporter();
	geonamesLanguageImporter.setImporterConfig(importerConfig);
	
	importerConfig.setGeonamesImporterEnabled(false);
	Assert.assertTrue(geonamesLanguageImporter.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(true);
	Assert.assertFalse(geonamesLanguageImporter.shouldBeSkipped());
		
    }

}
