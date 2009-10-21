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
package com.gisgraphy.webapp.action;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.importer.IImporterManager;
import com.gisgraphy.domain.geoloc.importer.ImporterConfig;
import com.opensymphony.xwork2.Action;

public class ImportConfirmActionTest {

    @Test
    public void executeShouldReturnStatusViewIfInProgress() throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress()).andStubReturn(true);
	EasyMock.expect(mockImporterManager.isAlreadyDone()).andStubReturn(
		false);
	EasyMock.replay(mockImporterManager);
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportConfirmAction.STATUS, action.execute());
    }

    @Test
    public void executeShouldReturnSuccessViewIfNotInProgress()
	    throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress())
		.andStubReturn(false);
	EasyMock.expect(mockImporterManager.isAlreadyDone()).andStubReturn(
		false);
	EasyMock.replay(mockImporterManager);
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportConfirmAction.SUCCESS, action.execute());
    }

    @Test
    public void executeShouldReturnSuccessViewIfNotAlreadyDone()
	    throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress())
		.andStubReturn(false);
	EasyMock.expect(mockImporterManager.isAlreadyDone()).andStubReturn(
		false);
	EasyMock.replay(mockImporterManager);
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportConfirmAction.SUCCESS, action.execute());
    }

    @Test
    public void executeShouldReturnStatusViewIfAlreadyDone() throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress())
		.andStubReturn(false);
	EasyMock.expect(mockImporterManager.isAlreadyDone())
		.andStubReturn(true);
	EasyMock.replay(mockImporterManager);
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportConfirmAction.STATUS, action.execute());
    }
    
    @Test
    public void isGeonamesImporterEnabled(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterConfig(importerConfig);
	Assert.assertTrue("isGeonamesImporterEnabled should return the same value as the importerConfig One ",action.isGeonamesImporterEnabled());
	importerConfig.setGeonamesImporterEnabled(false);
	Assert.assertFalse("isGeonamesImporterEnabled should return the same value as the importerConfig One ",action.isGeonamesImporterEnabled());
    }
    
    @Test
    public void disableGeonamesImporter(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());
	
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterConfig(importerConfig);
	
	Assert.assertTrue("isGeonamesImporterEnabled should return the same value as the importerConfig One ",action.isGeonamesImporterEnabled());
	Assert.assertEquals(Action.SUCCESS,action.disableGeonamesImporter());
	Assert.assertFalse("isGeonamesImporterEnabled should return the same value as the importerConfig One ",action.isGeonamesImporterEnabled());
    }
    
    @Test
    public void enableGeonamesImporter(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());
	
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterConfig(importerConfig);
	importerConfig.setGeonamesImporterEnabled(false);
	Assert.assertFalse("isGeonamesImporterEnabled should return the same value as the importerConfig One ",action.isGeonamesImporterEnabled());
	
	Assert.assertEquals(Action.SUCCESS,action.enableGeonamesImporter());
	Assert.assertTrue("isGeonamesImporterEnabled should return the same value as the importerConfig One ",action.isGeonamesImporterEnabled());
    }
    
    @Test
    public void isOpenStreetMapImporterEnabled(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue(importerConfig.isOpenstreetmapImporterEnabled());
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterConfig(importerConfig);
	Assert.assertTrue("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ",action.isOpenStreetMapImporterEnabled());
	importerConfig.setOpenstreetmapImporterEnabled(false);
	Assert.assertFalse("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ",action.isOpenStreetMapImporterEnabled());
    }
    
    @Test
    public void disableOpenStreetMapImporter(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue(importerConfig.isOpenstreetmapImporterEnabled());
	
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterConfig(importerConfig);
	
	Assert.assertTrue("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ",action.isOpenStreetMapImporterEnabled());
	Assert.assertEquals(Action.SUCCESS,action.disableOpenStreetMapImporter());
	Assert.assertFalse("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ",action.isOpenStreetMapImporterEnabled());
    }
    
    @Test
    public void enableOpenStreetMapImporter(){
	ImporterConfig importerConfig = new ImporterConfig();
	Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());
	
	ImportConfirmAction action = new ImportConfirmAction();
	action.setImporterConfig(importerConfig);
	importerConfig.setOpenstreetmapImporterEnabled(false);
	Assert.assertFalse("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ",action.isOpenStreetMapImporterEnabled());
	
	Assert.assertEquals(Action.SUCCESS,action.enableOpenStreetMapImporter());
	Assert.assertTrue("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ",action.isOpenStreetMapImporterEnabled());
    }

}
