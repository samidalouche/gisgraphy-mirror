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
package com.gisgraphy.servlet;

import java.io.IOException;
import java.util.ResourceBundle;

import net.sf.jstester.JsTester;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.ServletTester;
import org.springframework.beans.factory.annotation.Autowired;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.geoloc.IStreetSearchEngine;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.test.FeedChecker;

public class StreetServletTest extends AbstractIntegrationHttpSolrTestCase {

    private static ServletTester servletTester;
    private static String streetServletUrl;

    @Autowired
    private IStreetSearchEngine streetSearchEngine;

    public static final String STREET_SERVLET_CONTEXT = "/street";

    private static boolean streetServletStarted = false;

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase#onSetUp()
     */
    @Override
    public void onSetUp() throws Exception {
	super.onSetUp();

	if (!streetServletStarted) {
	    // we only launch street servlet once
	    servletTester = new ServletTester();
	    servletTester.setContextPath("/");
	    ServletHolder holder = servletTester.addServlet(
		    StreetServlet.class, STREET_SERVLET_CONTEXT + "/*");
	    streetServletUrl = servletTester.createSocketConnector(true);
	    servletTester.start();
	    StreetServlet streetServlet = (StreetServlet) holder.getServlet();
	    streetServlet.setStreetSearchEngine(streetSearchEngine);
	    streetServletStarted = true;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase#onTearDown()
     */
    @Override
    public void onTearDown() throws Exception {
	super.onTearDown();
	// servletTester.stop();
    }

    public void testServletShouldReturnCorrectContentTypeForSupportedFormat() {
	String url = streetServletUrl + STREET_SERVLET_CONTEXT
		+ "/geolocsearch";

	String queryString;
	for (OutputFormat format : OutputFormat.values()) {
	    GetMethod get = null;
	    try {
		queryString = StreetServlet.LAT_PARAMETER+"=3&"+StreetServlet.LONG_PARAMETER+"=4&format=" + format.toString();
		HttpClient client = new HttpClient();
		get = new GetMethod(url);

		get.setQueryString(queryString);
		client.executeMethod(get);
		// result = get.getResponseBodyAsString();
		
		Header contentType = get.getResponseHeader("Content-Type");
		OutputFormat expectedformat = format.isSupported(GisgraphyServiceType.STREET)?format:OutputFormat.getDefault();
		assertTrue(contentType.getValue().equals(
			expectedformat.getContentType()));

	    } catch (IOException e) {
		fail("An exception has occured " + e.getMessage());
	    } finally {
		if (get != null) {
		    get.releaseConnection();
		}
	    }
	}

    }
    
    public void testServletShouldReturnCorrectContentTypeForSupportedFormatWhenErrorOccured() {
	String url = streetServletUrl + STREET_SERVLET_CONTEXT
		+ "/streetsearch";

	String queryStringWithMissingLat;
	for (OutputFormat format : OutputFormat.values()) {
	    GetMethod get = null;
	    try {
		queryStringWithMissingLat = StreetServlet.LONG_PARAMETER+"=4&format=" + format.toString();
		HttpClient client = new HttpClient();
		get = new GetMethod(url);

		get.setQueryString(queryStringWithMissingLat);
		client.executeMethod(get);
		// result = get.getResponseBodyAsString();
		
		Header contentType = get.getResponseHeader("Content-Type");
		OutputFormat expectedformat = format.isSupported(GisgraphyServiceType.STREET)?format:OutputFormat.getDefault();
		assertEquals("The content-type is not correct",expectedformat.getContentType(),contentType.getValue());

	    } catch (IOException e) {
		fail("An exception has occured " + e.getMessage());
	    } finally {
		if (get != null) {
		    get.releaseConnection();
		}
	    }
	}

    }
    
    public void testServletShouldReturnCorrectStatusCode() {
	String url = streetServletUrl + STREET_SERVLET_CONTEXT
		+ "/streetsearch";

	String queryStringWithMissingLat;
	    GetMethod get = null;
	    try {
		queryStringWithMissingLat = StreetServlet.LONG_PARAMETER+"=4&format=" + OutputFormat.JSON.toString();
		HttpClient client = new HttpClient();
		get = new GetMethod(url);

		get.setQueryString(queryStringWithMissingLat);
		client.executeMethod(get);
		// result = get.getResponseBodyAsString();
		
		assertEquals("status code is not correct ",500 ,get.getStatusCode());

	    } catch (IOException e) {
		fail("An exception has occured " + e.getMessage());
	    } finally {
		if (get != null) {
		    get.releaseConnection();
		}
	    }

    }

    public void testServletShouldReturnCorrectJSONError() {

	JsTester jsTester = null;
	String url = streetServletUrl + STREET_SERVLET_CONTEXT
		+ "/streetsearch";

	String result;
	String queryString;
	OutputFormat format = OutputFormat.JSON;
	GetMethod get = null;
	try {
	    jsTester = new JsTester();
	    jsTester.onSetUp();
	    queryString = "format=" + format.getParameterValue();
	    HttpClient client = new HttpClient();
	    get = new GetMethod(url);

	    get.setQueryString(queryString);
	    client.executeMethod(get);
	    result = get.getResponseBodyAsString();

	    // JsTester
	    jsTester.eval("evalresult= eval(" + result + ");");
	    jsTester.assertNotNull("evalresult");
	    String error = jsTester.eval("evalresult.error").toString();
	    String expected = ResourceBundle.getBundle(
		    Constants.BUNDLE_ERROR_KEY).getString("error.emptyLatLong");
	    assertEquals(expected, error);

	} catch (IOException e) {
	    fail("An exception has occured " + e.getMessage());
	} finally {
	    if (jsTester != null) {
		jsTester.onTearDown();
	    }
	    if (get != null) {
		get.releaseConnection();
	    }
	}

    }

    public void testServletShouldReturnCorrectXMLError() {

	String url = streetServletUrl + STREET_SERVLET_CONTEXT
		+ "/streetsearch";

	String result;
	String queryString;
	OutputFormat format = OutputFormat.XML;
	GetMethod get = null;
	try {
	    queryString = "format=" + format.getParameterValue();
	    HttpClient client = new HttpClient();
	    get = new GetMethod(url);

	    get.setQueryString(queryString);
	    client.executeMethod(get);
	    result = get.getResponseBodyAsString().trim();

	    // JsTester
	    String expected = ResourceBundle.getBundle(
		    Constants.BUNDLE_ERROR_KEY).getString("error.emptyLatLong");
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='"
		    + expected + "']");
	} catch (IOException e) {
	    fail("An exception has occured " + e.getMessage());
	} finally {
	    if (get != null) {
		get.releaseConnection();
	    }
	}

    }
    
    public void testgetGisgraphyServiceTypeShouldReturnTheCorrectValue(){
	GisgraphyServlet servlet = new StreetServlet();
    	assertEquals(GisgraphyServiceType.STREET, servlet.getGisgraphyServiceType());

    }
}
