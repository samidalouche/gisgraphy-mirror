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


public class ImportActionTest {
    
    @Test
    public void testdoWaitShouldReturnWaitView() throws Exception{
	Assert.assertEquals(ImportAction.WAIT, new ImportAction().doWait());
    }
    
    @Test
    public void testStatusShouldReturnWaitView() throws Exception{
	Assert.assertEquals(ImportAction.WAIT, new ImportAction().status());
    }
    
    @Test
    public void executeShouldReturnWaitViewIfInProgress() throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress()).andStubReturn(true);
	EasyMock.expect(mockImporterManager.isAlreadyDone()).andStubReturn(
		false);
	EasyMock.replay(mockImporterManager);
	ImportAction action = new ImportAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportAction.WAIT, action.execute());
    }

    @Test
    public void executeShouldImportIfNotInProgress()
	    throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress())
		.andStubReturn(false);
	EasyMock.expect(mockImporterManager.isAlreadyDone()).andStubReturn(
		false);
	mockImporterManager.importAll();
	EasyMock.expectLastCall();
	EasyMock.replay(mockImporterManager);
	ImportAction action = new ImportAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportAction.SUCCESS, action.execute());
    }

    @Test
    public void executeShouldImportIfNotAlreadyDone()
	    throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress())
		.andStubReturn(false);
	EasyMock.expect(mockImporterManager.isAlreadyDone()).andStubReturn(
		false);
	mockImporterManager.importAll();
	EasyMock.expectLastCall();
	EasyMock.replay(mockImporterManager);
	ImportAction action = new ImportAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportAction.SUCCESS, action.execute());
    }

    @Test
    public void executeShouldReturnWaitViewIfAlreadyDone() throws Exception {
	IImporterManager mockImporterManager = EasyMock
		.createMock(IImporterManager.class);
	EasyMock.expect(mockImporterManager.isInProgress())
		.andStubReturn(false);
	EasyMock.expect(mockImporterManager.isAlreadyDone())
		.andStubReturn(true);
	EasyMock.replay(mockImporterManager);
	ImportAction action = new ImportAction();
	action.setImporterManager(mockImporterManager);
	Assert.assertEquals(ImportAction.WAIT, action.execute());
    }


}
