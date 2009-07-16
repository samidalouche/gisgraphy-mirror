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

import com.gisgraphy.domain.repository.IAdmDao;
import com.gisgraphy.domain.valueobject.NameValueDTO;

public class GeonamesAdm2ImporterTest extends TestCase {

    @Test
    public void testRollback() {
	GeonamesAdm2Importer geonamesAdm2Importer = new GeonamesAdm2Importer();
	IAdmDao admDao = EasyMock.createMock(IAdmDao.class);
	EasyMock.expect(admDao.deleteAllByLevel(2)).andReturn(4);
	EasyMock.replay(admDao);
	geonamesAdm2Importer.setAdmDao(admDao);
	List<NameValueDTO<Integer>> deleted = geonamesAdm2Importer.rollback();
	assertEquals(1, deleted.size());
	assertEquals(4, deleted.get(0).getValue().intValue());
    }
    
    @Test
    public void testShouldBeSkipShouldReturnCorrectValue(){
	ImporterConfig importerConfig = new ImporterConfig();
	GeonamesAdm2Importer geonamesAdm2Importer = new GeonamesAdm2Importer();
	geonamesAdm2Importer.setImporterConfig(importerConfig);
	
	importerConfig.setGeonamesImporterEnabled(false);
	Assert.assertTrue(geonamesAdm2Importer.shouldBeSkipped());
	
	importerConfig.setGeonamesImporterEnabled(true);
	Assert.assertFalse(geonamesAdm2Importer.shouldBeSkipped());
		
    }

}
