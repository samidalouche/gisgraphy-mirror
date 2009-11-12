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

import java.util.ResourceBundle;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.importer.IImporterManager;
import com.gisgraphy.domain.geoloc.importer.ImporterConfig;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.helper.PropertiesHelper;
import com.opensymphony.xwork2.Action;

public class ImportConfirmActionTest {

	private IImporterManager createMockImporterManager(boolean inProgress, boolean alreadyDone) {
		IImporterManager mockImporterManager = EasyMock.createMock(IImporterManager.class);
		EasyMock.expect(mockImporterManager.isInProgress()).andStubReturn(inProgress);
		EasyMock.expect(mockImporterManager.isAlreadyDone()).andStubReturn(alreadyDone);
		EasyMock.replay(mockImporterManager);
		return mockImporterManager;
	}
	
	@Test
	public void executeShouldReturnStatusViewIfInProgress() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(true, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STATUS, action.execute());
	}


	@Test
	public void executeShouldReturnSuccessViewIfNotInProgress() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.SUCCESS, action.execute());
	}

	@Test
	public void executeShouldReturnSuccessViewIfNotAlreadyDone() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.SUCCESS, action.execute());
	}

	@Test
	public void executeShouldReturnStatusViewIfAlreadyDone() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, true);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STATUS, action.execute());
	}

	//STEP1
	@Test
	public void step1ShouldReturnStatusViewIfInProgress() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(true, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STATUS, action.step1());
	}


	@Test
	public void step1ShouldReturnStep1ViewIfNotInProgress() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STEP_BASE_VIEW_NAME+1, action.step1());
		Assert.assertEquals(1, action.step_number);
	}

	@Test
	public void step1ShouldReturnStep1ViewIfNotAlreadyDone() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STEP_BASE_VIEW_NAME+1, action.step1());
		Assert.assertEquals(1, action.step_number);
	}

	@Test
	public void step1ShouldReturnStatusViewIfAlreadyDone() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, true);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STATUS, action.step1());
	}
	
	//STEP2
	@Test
	public void step2ShouldReturnStatusViewIfInProgress() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(true, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STATUS, action.step2());
	}


	@Test
	public void step2ShouldReturnStep1ViewIfNotInProgress() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STEP_BASE_VIEW_NAME+2, action.step2());
		Assert.assertEquals(2, action.step_number);
	}

	@Test
	public void step2ShouldReturnStep1ViewIfNotAlreadyDone() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, false);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STEP_BASE_VIEW_NAME+2, action.step2());
		Assert.assertEquals(2, action.step_number);
	}

	@Test
	public void step2ShouldReturnStatusViewIfAlreadyDone() throws Exception {
		IImporterManager mockImporterManager = createMockImporterManager(false, true);
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterManager(mockImporterManager);
		Assert.assertEquals(ImportConfirmAction.STATUS, action.step2());
	}
	
	

	@Test
	public void isGeonamesImporterEnabled() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterConfig(importerConfig);
		Assert.assertTrue("isGeonamesImporterEnabled should return the same value as the importerConfig One ", action.isGeonamesImporterEnabled());
		importerConfig.setGeonamesImporterEnabled(false);
		Assert.assertFalse("isGeonamesImporterEnabled should return the same value as the importerConfig One ", action.isGeonamesImporterEnabled());
	}

	@Test
	public void disableGeonamesImporter() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());

		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterConfig(importerConfig);

		Assert.assertTrue("isGeonamesImporterEnabled should return the same value as the importerConfig One ", action.isGeonamesImporterEnabled());
		Assert.assertEquals(Action.SUCCESS, action.disableGeonamesImporter());
		Assert.assertFalse("isGeonamesImporterEnabled should return the same value as the importerConfig One ", action.isGeonamesImporterEnabled());
	}

	@Test
	public void enableGeonamesImporter() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());

		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterConfig(importerConfig);
		importerConfig.setGeonamesImporterEnabled(false);
		Assert.assertFalse("isGeonamesImporterEnabled should return the same value as the importerConfig One ", action.isGeonamesImporterEnabled());

		Assert.assertEquals(Action.SUCCESS, action.enableGeonamesImporter());
		Assert.assertTrue("isGeonamesImporterEnabled should return the same value as the importerConfig One ", action.isGeonamesImporterEnabled());
	}

	@Test
	public void isOpenStreetMapImporterEnabled() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue(importerConfig.isOpenstreetmapImporterEnabled());
		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterConfig(importerConfig);
		Assert.assertTrue("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ", action.isOpenStreetMapImporterEnabled());
		importerConfig.setOpenstreetmapImporterEnabled(false);
		Assert.assertFalse("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ", action.isOpenStreetMapImporterEnabled());
	}

	@Test
	public void disableOpenStreetMapImporter() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue(importerConfig.isOpenstreetmapImporterEnabled());

		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterConfig(importerConfig);

		Assert.assertTrue("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ", action.isOpenStreetMapImporterEnabled());
		Assert.assertEquals(Action.SUCCESS, action.disableOpenStreetMapImporter());
		Assert.assertFalse("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ", action.isOpenStreetMapImporterEnabled());
	}

	@Test
	public void enableOpenStreetMapImporter() {
		ImporterConfig importerConfig = new ImporterConfig();
		Assert.assertTrue(importerConfig.isGeonamesImporterEnabled());

		ImportConfirmAction action = new ImportConfirmAction();
		action.setImporterConfig(importerConfig);
		importerConfig.setOpenstreetmapImporterEnabled(false);
		Assert.assertFalse("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ", action.isOpenStreetMapImporterEnabled());

		Assert.assertEquals(Action.SUCCESS, action.enableOpenStreetMapImporter());
		Assert.assertTrue("isOpenStreetMapImporterEnabled should return the same value as the importerConfig One ", action.isOpenStreetMapImporterEnabled());
	}

	
	

	@Test
	public void getConfigValuesMap() {
		ImportConfirmAction action = new ImportConfirmAction();
		Assert.assertEquals(PropertiesHelper.convertBundleToMap(ResourceBundle.getBundle(Constants.ENVIRONEMENT_BUNDLE_KEY)), action.getConfigValuesMap());
	}
}
