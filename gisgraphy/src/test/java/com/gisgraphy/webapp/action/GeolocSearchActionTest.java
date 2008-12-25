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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.IGeolocSearchEngine;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.servlet.GeolocServlet;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;

public class GeolocSearchActionTest {

    GeolocSearchAction action;
    List<GisFeatureDistance> results;
    IGeolocSearchEngine mockSearchEngine;
    GeolocResultsDto mockResultDTO;

    @Before
    public void setup() {
	ConfigurationManager configurationManager = new ConfigurationManager();
	configurationManager
		.addContainerProvider(new XWorkConfigurationProvider());
	Configuration config = configurationManager.getConfiguration();
	Container container = config.getContainer();

	ValueStack stack = container.getInstance(ValueStackFactory.class)
		.createValueStack();
	stack.getContext().put(ActionContext.CONTAINER, container);
	ActionContext.setContext(new ActionContext(stack.getContext()));
	ServletActionContext.setRequest(new MockHttpServletRequest());
	results = new ArrayList<GisFeatureDistance>();
	action = new GeolocSearchAction();
	mockSearchEngine = EasyMock.createMock(IGeolocSearchEngine.class);
	action.setGeolocSearchEngine(mockSearchEngine);
	mockResultDTO = EasyMock.createMock(GeolocResultsDto.class);
	EasyMock.expect(mockResultDTO.getResult()).andReturn(results);
    }

    @Test
    public void testSearch() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest("GET",
		"/search.html");
	ServletActionContext.setRequest(request);
	request.setParameter(GeolocServlet.LAT_PARAMETER.toString(), "3.5");
	request.setParameter(GeolocServlet.LONG_PARAMETER.toString(), "4.5");
	GisFeatureDistance mockGisFeatureDistance = EasyMock
		.createMock(GisFeatureDistance.class);
	EasyMock.replay(mockGisFeatureDistance);
	results.add(mockGisFeatureDistance);
	EasyMock.replay(mockResultDTO);
	EasyMock.expect(
		mockSearchEngine.executeQuery((GeolocQuery) EasyMock
			.anyObject())).andReturn(mockResultDTO);
	EasyMock.replay(mockSearchEngine);
	String returnAction = action.search();
	assertEquals(FulltextSearchAction.SUCCESS, returnAction);
	assertEquals(mockResultDTO, action.getResponseDTO());
    }

    @Test
    public void testIsDisplayResults() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest("GET",
		"/search.html");
	ServletActionContext.setRequest(request);
	request.setParameter(GeolocServlet.LAT_PARAMETER.toString(), "3.5");
	request.setParameter(GeolocServlet.LONG_PARAMETER.toString(), "4.5");
	GisFeatureDistance mockGisFeatureDistance = EasyMock
		.createMock(GisFeatureDistance.class);
	EasyMock.replay(mockGisFeatureDistance);
	results.add(mockGisFeatureDistance);
	EasyMock.replay(mockResultDTO);
	EasyMock.expect(
		mockSearchEngine.executeQuery((GeolocQuery) EasyMock
			.anyObject())).andReturn(mockResultDTO);
	EasyMock.replay(mockSearchEngine);
	String returnAction = action.search();
	assertEquals(FulltextSearchAction.SUCCESS, returnAction);
	assertEquals(mockResultDTO, action.getResponseDTO());
	assertTrue(action.isDisplayResults());
    }

    @Test
    public void testSearchWhenFailed() throws Exception {
	String errorMessage = "message";
	MockHttpServletRequest request = new MockHttpServletRequest("GET",
		"/search.html");
	ServletActionContext.setRequest(request);
	request.setParameter(GeolocServlet.LAT_PARAMETER.toString(), "3.5");
	request.setParameter(GeolocServlet.LONG_PARAMETER.toString(), "4.5");
	EasyMock.replay(mockResultDTO);
	EasyMock.expect(
		mockSearchEngine.executeQuery((GeolocQuery) EasyMock
			.anyObject())).andThrow(
		new RuntimeException(errorMessage));
	EasyMock.replay(mockSearchEngine);
	String returnAction = action.search();
	assertEquals(FulltextSearchAction.SUCCESS, returnAction);
	assertEquals(errorMessage, action.getErrorMessage());
	assertNull(action.getResponseDTO());
	assertFalse(action.isDisplayResults());
    }

    @Test
    public void testSearchPopupShouldReturnPopupView() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest("GET",
		"/search.html");
	ServletActionContext.setRequest(request);
	request.setParameter(GeolocServlet.LAT_PARAMETER.toString(), "3.5");
	request.setParameter(GeolocServlet.LONG_PARAMETER.toString(), "4.5");
	GisFeatureDistance mockGisFeatureDistance = EasyMock
		.createMock(GisFeatureDistance.class);
	EasyMock.replay(mockGisFeatureDistance);
	results.add(mockGisFeatureDistance);
	EasyMock.replay(mockResultDTO);
	EasyMock.expect(
		mockSearchEngine.executeQuery((GeolocQuery) EasyMock
			.anyObject())).andReturn(mockResultDTO);
	EasyMock.replay(mockSearchEngine);
	String returnAction = action.searchpopup();
	assertEquals(FulltextSearchAction.POPUP_VIEW, returnAction);
	assertEquals(mockResultDTO, action.getResponseDTO());
    }

    @Test
    public void testGetStyleShouldReturnStyle() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest("GET",
		"/search.html");
	ServletActionContext.setRequest(request);
	request.setParameter(GeolocServlet.LAT_PARAMETER.toString(), "3.5");
	request.setParameter(GeolocServlet.LONG_PARAMETER.toString(), "4.5");
	action.setPlacetype("GisFeature");
	assertEquals("GisFeature", action.getPlacetype());
    }

    @Test
    public void testGetStyleShouldReturnDefaultStyle() throws Exception {
	MockHttpServletRequest request = new MockHttpServletRequest("GET",
		"/search.html");
	ServletActionContext.setRequest(request);
	request.setParameter(GeolocServlet.LAT_PARAMETER.toString(), "3.5");
	request.setParameter(GeolocServlet.LONG_PARAMETER.toString(), "4.5");
	action.setPlacetype(null);
	assertEquals(GisgraphyConfig.defaultGeolocSearchPlaceTypeClass, action
		.getPlacetype());
    }

    @Test
    public void testGetFormatsShouldReturnFormatForGeoloc() {
	Assert.assertEquals(Arrays.asList(OutputFormat
		.listByService(GisgraphyServiceType.GEOLOC)), Arrays
		.asList(action.getFormats()));

    }

}
