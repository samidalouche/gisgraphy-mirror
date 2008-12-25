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
package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import junit.framework.TestCase;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.jasper.servlet.JspServlet;
import org.apache.solr.servlet.SolrDispatchFilter;
import org.apache.solr.servlet.SolrServlet;
import org.apache.solr.servlet.SolrUpdateServlet;
import org.junit.Test;
import org.mortbay.jetty.testing.ServletTester;

public class SolrClientTest extends TestCase {

    @Test
    public void testSolrClientConstructorShouldNotAcceptNullParameters() {
	try {
	    new SolrClient(null, new MultiThreadedHttpConnectionManager());
	    fail("solrClient constructor does not accept null URL");
	} catch (IllegalArgumentException e) {
	}
	try {
	    new SolrClient("", null);
	    fail("solrClient constructor does not accept null multiThreadedHttpConnectionManager");
	} catch (IllegalArgumentException e) {
	}
    }

    @Test
    public void testIsALive() {
	ServletTester tester = null;
	try {
	    tester = new ServletTester();
	    String fulltextContext = "/solr";
	    tester.addFilter(SolrDispatchFilter.class, "/*", 0);
	    tester.setContextPath(fulltextContext);

	    // TODO v2 a solrdir for test
	    // TODO v2 remove deprecated
	    tester.addServlet(SolrServlet.class, "/select/*");
	    String separator = System.getProperty("file.separator");
	    tester.setResourceBase("target" + separator + "classes" + separator
		    + "solradmin");
	    tester.addServlet("org.mortbay.jetty.servlet.DefaultServlet", "/");
	    tester.addServlet(JspServlet.class, "*.jsp");

	    tester.addServlet(SolrUpdateServlet.class, "/update/*");

	    String fulltextSearchUrl = tester.createSocketConnector(true)
		    + fulltextContext;
	    tester.start();
	    IsolrClient clientAlive = new SolrClient("http://nowhere.tld/solr",
		    new MultiThreadedHttpConnectionManager());
	    assertFalse(clientAlive.isServerAlive());
	    clientAlive.bindToUrl(fulltextSearchUrl);
	    assertTrue(clientAlive.isServerAlive());
	} catch (Exception e) {
	    fail(e.getMessage());
	} finally {
	    if (tester != null) {
		try {
		    tester.stop();
		} catch (Exception e) {
		}
	    }
	}

    }

    @Test
    public void testBindToURL() {
	ServletTester tester = null;
	try {
	    tester = new ServletTester();
	    String fulltextContext = "/solr";
	    tester.addFilter(SolrDispatchFilter.class, "/*", 0);
	    tester.setContextPath(fulltextContext);

	    // TODO v2 a solrdir for test
	    // TODO v2 remove deprecated
	    tester.addServlet(SolrServlet.class, "/select/*");
	    String separator = System.getProperty("file.separator");
	    tester.setResourceBase("target" + separator + "classes" + separator
		    + "solradmin");
	    tester.addServlet("org.mortbay.jetty.servlet.DefaultServlet", "/");
	    tester.addServlet(JspServlet.class, "*.jsp");

	    tester.addServlet(SolrUpdateServlet.class, "/update/*");

	    String fulltextSearchUrl = null;
	    try {
		fulltextSearchUrl = tester.createSocketConnector(true)
			+ fulltextContext;
		tester.start();
	    } catch (Exception e) {
		fail(e.getMessage());
	    }
	    IsolrClient clientAlive = new SolrClient("http://nowhere.tld/solr",
		    new MultiThreadedHttpConnectionManager());
	    clientAlive.bindToUrl(fulltextSearchUrl);
	    try {
		clientAlive.bindToUrl("http://nowhere.tld:4747solr");
		fail("BindToUrl should throw for malformedUrl");
	    } catch (Exception e) {
	    }

	} finally {
	    if (tester != null) {
		try {
		    tester.stop();
		} catch (Exception e) {
		}
	    }
	}

    }

}
