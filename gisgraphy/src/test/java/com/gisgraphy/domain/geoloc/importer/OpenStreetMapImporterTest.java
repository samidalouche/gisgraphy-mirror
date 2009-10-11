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

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.StringHelper;
import com.vividsolutions.jts.geom.Point;



public class OpenStreetMapImporterTest extends AbstractIntegrationHttpSolrTestCase {
    
    private IImporterProcessor openStreetMapImporter;
    
    private IOpenStreetMapDao openStreetMapDao;
  


    public void testImporterShouldImport(){
	OpenStreetMapImporter.GeneratedId = 0L;
	openStreetMapImporter.process();
	assertEquals(4L,openStreetMapDao.count());
	OpenStreetMap openStreetMap = openStreetMapDao.getByGid(1L);
	assertTrue("The oneWay attribute is not correct",openStreetMap.getOneWay());
	assertEquals("The countryCode is not correct ","FR",openStreetMap.getCountryCode());
	assertEquals("The streetType is not correct",StreetType.RESIDENTIAL, openStreetMap.getStreetType());
	assertEquals("The name is not correct","Bachlettenstrasse", openStreetMap.getName());
	assertEquals("The location->X is not correct ",((Point)GeolocHelper.convertFromHEXEWKBToGeometry("010100000006C82291A0521E4054CC39B16BC64740")).getX(), openStreetMap.getLocation().getX());
	assertEquals("The location->Y is not correct ",((Point)GeolocHelper.convertFromHEXEWKBToGeometry("010100000006C82291A0521E4054CC39B16BC64740")).getY(), openStreetMap.getLocation().getY());
	assertEquals("The length is not correct",0.00142246604529, openStreetMap.getLength());
	assertEquals("The partialSearchName is not correct",StringHelper.transformStringForPartialWordIndexation(openStreetMap.getName(), StringHelper.WHITESPACE_CHAR_DELIMITER), openStreetMap.getPartialSearchName());
	assertEquals("The textSearchName is not correct",StringHelper.transformStringForFulltextIndexation(openStreetMap.getName()), openStreetMap.getTextSearchName());
	assertEquals("The shape is not correct ",GeolocHelper.convertFromHEXEWKBToGeometry("010500000001000000010200000005000000591EFF603B531E40F88667AE78C64740446ADAC534531E40348BAB2578C647405BB164332C531E407033CB5477C64740754A51781A521E403A56CE8360C64740CD63833B06521E409A081B9E5EC64740").toString(), openStreetMap.getShape().toString());
    }

   
    
    @Test
    public void testShouldBeSkipShouldReturnCorrectValue(){
	ImporterConfig importerConfig = new ImporterConfig();
	OpenStreetMapImporter openStreetMapImporter = new OpenStreetMapImporter();
	openStreetMapImporter.setImporterConfig(importerConfig);
	
	importerConfig.setOpenstreetmapImporterEnabled(false);
	Assert.assertTrue(openStreetMapImporter.shouldBeSkipped());
	
	importerConfig.setOpenstreetmapImporterEnabled(true);
	Assert.assertFalse(openStreetMapImporter.shouldBeSkipped());
		
    }
    
    
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }

    
    public void setOpenStreetMapImporter(IImporterProcessor openStreetMapImporter) {
        this.openStreetMapImporter = openStreetMapImporter;
    }

}
