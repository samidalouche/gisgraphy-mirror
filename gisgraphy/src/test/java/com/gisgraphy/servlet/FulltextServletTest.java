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
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IFullTextSearchEngine;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.test.XpathChecker;

public class FulltextServletTest extends AbstractIntegrationHttpSolrTestCase {

    private static ServletTester servletTester;
    private static String fulltextServletUrl;

    @Autowired
    private IFullTextSearchEngine fullTextSearchEngine;

    public static final String FULLTEXT_SERVLET_CONTEXT = "/fulltext";

    private static boolean fulltextServletStarted = false;

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase#onSetUp()
     */
    @Override
    public void onSetUp() throws Exception {
	super.onSetUp();

	if (!fulltextServletStarted) {
	    // we only launch fulltext servlet once
	    servletTester = new ServletTester();
	    servletTester.setContextPath("/");
	    ServletHolder holder = servletTester.addServlet(
		    FulltextServlet.class, FULLTEXT_SERVLET_CONTEXT + "/*");
	    fulltextServletUrl = servletTester.createSocketConnector(true);
	    servletTester.start();
	    FulltextServlet fulltextServlet = (FulltextServlet) holder
		    .getServlet();
	    fulltextServlet.setFullTextSearchEngine(fullTextSearchEngine);
	    fulltextServletStarted = true;
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

    public void testFulltextServletShouldReturnCorrectContentType() {
	String url = fulltextServletUrl + FULLTEXT_SERVLET_CONTEXT
		+ "/fulltextsearch";

	// String result;
	String queryString;
	for (OutputFormat format : OutputFormat.values()) {
	    GetMethod get = null;
	    try {
		queryString = "q=test&format=" + format.toString();
		HttpClient client = new HttpClient();
		get = new GetMethod(url);

		get.setQueryString(queryString);
		client.executeMethod(get);
		// result = get.getResponseBodyAsString();

		Header contentType = get.getResponseHeader("Content-Type");
		assertTrue(contentType.getValue().equals(
			format.getContentType()));

	    } catch (IOException e) {
		fail("An exception has occured " + e.getMessage());
	    } finally {
		if (get != null) {
		    get.releaseConnection();
		}
	    }
	}

    }

    public void testFulltextServletShouldReturnCorrectJSONError() {

	JsTester jsTester = null;
	String url = fulltextServletUrl + FULLTEXT_SERVLET_CONTEXT
		+ "/fulltextsearch";

	String result;
	String queryString;
	OutputFormat format = OutputFormat.JSON;
	GetMethod get = null;
	try {
	    jsTester = new JsTester();
	    jsTester.onSetUp();
	    queryString = "q=&format=" + format.getParameterValue();
	    HttpClient client = new HttpClient();
	    get = new GetMethod(url);

	    get.setQueryString(queryString);
	    client.executeMethod(get);
	    result = get.getResponseBodyAsString();
	    
	    //check content 
	    Header contentType = get.getResponseHeader("Content-Type");
		assertEquals("The content-type is not correct",OutputFormat.JSON.getContentType(),contentType.getValue()
			);

	    // JsTester
	    jsTester.eval("evalresult= eval(" + result + ");");
	    jsTester.assertNotNull("evalresult");
	    String error = jsTester.eval("evalresult.responseHeader.error")
		    .toString();
	    String expected = ResourceBundle.getBundle(
		    Constants.BUNDLE_ERROR_KEY).getString("error.emptyQuery");
	    assertEquals(expected, error);

	    error = jsTester.eval("evalresult.responseHeader.status")
		    .toString();
	    assertEquals("-1.0", error);// -1.0 because it is considered as a
	    // float

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

    public void testFulltextServletShouldReturnCorrectXMLError() {

	String url = fulltextServletUrl + FULLTEXT_SERVLET_CONTEXT
		+ "/fulltextsearch";

	String result;
	String queryString;
	OutputFormat format = OutputFormat.XML;
	GetMethod get = null;
	try {
	    queryString = "q=&format=" + format.getParameterValue();
	    HttpClient client = new HttpClient();
	    get = new GetMethod(url);

	    get.setQueryString(queryString);
	    client.executeMethod(get);
	    result = get.getResponseBodyAsString().trim();

	    // JsTester
	    String expected = ResourceBundle.getBundle(
		    Constants.BUNDLE_ERROR_KEY).getString("error.emptyQuery");
	    XpathChecker.assertQ("The XML error is not correct", result,
		    "//*[@name='status'][.='-1']", "//*[@name='error'][.='"
			    + expected + "']");
	} catch (IOException e) {
	    fail("An exception has occured " + e.getMessage());
	} finally {
	    if (get != null) {
		get.releaseConnection();
	    }
	}

    }

}
