package com.gisgraphy.webapp.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.domain.geoloc.service.geoloc.IStreetSearchEngine;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchEngine;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;
import com.gisgraphy.servlet.StreetServlet;


public class StreetSearchActionTest {
    
    @Test
    public void isDisplayResultsShouldReturnTrueIfThereIsSomeResults(){
	StreetSearchAction streetSearchAction = new StreetSearchAction();
	Assert.assertFalse("isDisplayResults sould be false if there is no results to display",streetSearchAction.isDisplayResults());
    }

    @Test
    public void getStreetTypesShouldReturnStreetTypeEnumValues(){
	StreetSearchAction streetSearchAction = new StreetSearchAction();
	Assert.assertArrayEquals(StreetType.values(),streetSearchAction.getStreetTypes());
    }
    
    @Test
    public void searchpopupShouldReturnCorrectView() throws Exception{
	final MockHttpServletRequest request = new MockHttpServletRequest();
	      request.addParameter(StreetServlet.LAT_PARAMETER, "3.2");
	      request.addParameter(StreetServlet.LONG_PARAMETER, "1.5");
	
	StreetSearchAction streetSearchAction = new StreetSearchAction(){
	    @Override
	    protected HttpServletRequest getRequest() {
	      
	      return request;
	    }
	};
	IStreetSearchEngine streetSearchEngine = EasyMock.createMock(IStreetSearchEngine.class);
	EasyMock.expect(streetSearchEngine.executeQuery(new StreetSearchQuery(request))).andReturn(new StreetSearchResultsDto());
	EasyMock.replay(streetSearchEngine);
	
	streetSearchAction.setStreetSearchEngine(streetSearchEngine);
	Assert.assertEquals(SearchAction.POPUP_VIEW,streetSearchAction.searchpopup());
    }
    
    @Test
    public void searchShouldReturnCorrectView() throws Exception{
	final MockHttpServletRequest request = new MockHttpServletRequest();
	      request.addParameter(StreetServlet.LAT_PARAMETER, "3.2");
	      request.addParameter(StreetServlet.LONG_PARAMETER, "1.5");
	
	StreetSearchAction streetSearchAction = new StreetSearchAction(){
	    @Override
	    protected HttpServletRequest getRequest() {
	      
	      return request;
	    }
	};
	IStreetSearchEngine streetSearchEngine = EasyMock.createMock(IStreetSearchEngine.class);
	EasyMock.expect(streetSearchEngine.executeQuery(new StreetSearchQuery(request))).andReturn(new StreetSearchResultsDto());
	EasyMock.replay(streetSearchEngine);
	
	streetSearchAction.setStreetSearchEngine(streetSearchEngine);
	Assert.assertEquals(SearchAction.SUCCESS,streetSearchAction.search());
    }
    
    @Test
    public void searchShouldSetErrorMassageIfAnErrorOccured() throws Exception{
	final MockHttpServletRequest request = new MockHttpServletRequest();
	      request.addParameter(StreetServlet.LAT_PARAMETER, "3.2");
	      request.addParameter(StreetServlet.LONG_PARAMETER, "1.5");
	
	StreetSearchAction streetSearchAction = new StreetSearchAction(){
	    @Override
	    protected HttpServletRequest getRequest() {
	      
	      return request;
	    }
	};
	IStreetSearchEngine streetSearchEngine = EasyMock.createMock(IStreetSearchEngine.class);
	String errorMessage = "errorMessage";
	EasyMock.expect(streetSearchEngine.executeQuery((StreetSearchQuery)EasyMock.anyObject())).andThrow(new RuntimeException(errorMessage));
	EasyMock.replay(streetSearchEngine);
	
	streetSearchAction.setStreetSearchEngine(streetSearchEngine);
	Assert.assertEquals(SearchAction.SUCCESS,streetSearchAction.search());
	Assert.assertEquals(errorMessage,streetSearchAction.getErrorMessage());
    }
    
    @Test
    public void getNameOptionsShouldReturnCorrectValues(){
	StreetSearchAction streetSearchAction = new StreetSearchAction(){
	    @Override
	    public String getText(String textName) {
	        if ("search.street.includeNoNameStreet".equals(textName)){
	            return "includeValue";
	        }
	        if ("search.street.dont.includeNoNameStreet".equals(textName)){
	            return "notincludeValue";
	        }
	        return null;
	    }
	};
	
	Map<String, String> nameOptions = streetSearchAction.getNameOptions();
	Assert.assertEquals("includeValue", nameOptions.get(""));
	Assert.assertEquals("notincludeValue", nameOptions.get("%"));
    }
    
    

}
