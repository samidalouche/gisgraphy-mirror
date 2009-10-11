/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import java.io.File;

import org.apache.jasper.servlet.JspServlet;
import org.apache.solr.servlet.SolrDispatchFilter;
import org.apache.solr.servlet.SolrServlet;
import org.apache.solr.servlet.SolrUpdateServlet;
import org.apache.solr.util.TestHarness;
import org.mortbay.jetty.testing.ServletTester;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.repository.AbstractTransactionalTestCase;
import com.gisgraphy.domain.repository.ISolRSynchroniser;

/**
 * An Abstract base class that makes writing Solr JUnit tests "easier"
 * 
 * @see #setUp
 * @see #tearDown
 */
public abstract class AbstractIntegrationHttpSolrTestCase extends
	AbstractTransactionalTestCase {

    private static boolean serverStarted = false;

    /**
     * Harness initialized by initTestHarness.
     * <p>
     * For use in test methods as needed.
     * </p>
     */
    protected TestHarness h;
    /**
     * LocalRequestFactory initialized by initTestHarness using sensible
     * defaults.
     * <p>
     * For use in test methods as needed.
     * </p>
     */
    protected TestHarness.LocalRequestFactory lrf;

    /**
     * Subclasses must define this method to return the name of the schema.xml
     * they wish to use.
     */
    public String getSchemaFile() {
	return "schema.xml";
    }

    /**
     * Subclasses must define this method to return the name of the
     * solrconfig.xml they wish to use.
     */
    public String getSolrConfigFile() {
	return "solrconfig.xml";
    }

    /**
     * the solr server for tests
     */
    protected ServletTester tester;

    protected boolean isMustStartServlet() {
	return true;
    }

    /**
     * the URL of the fulltextSearchUrl
     */
    protected String fulltextSearchUrl;

    protected ISolRSynchroniser solRSynchroniser;

    protected IsolrClient solrClient;

    protected IFullTextSearchEngine fullTextSearchEngine;

    protected String FULLTEXT_SEARCH_ENGINE_CONTEXT = "/solr";

    /**
     * The directory used to story the index managed by the TestHarness h
     */
    protected File dataDir;

    /**
     * Initializes things your test might need
     * <ul>
     * <li>Creates a dataDir in the "java.io.tmpdir"</li>
     * <li>initializes the TestHarness h using this data directory, and
     * getSchemaPath()</li>
     * <li>initializes the LocalRequestFactory lrf using sensible defaults.</li>
     * </ul>
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onSetUp() throws Exception {
	super.onSetUp();


	if (!serverStarted && isMustStartServlet()) {
	    String separator = System.getProperty("file.separator");
	    String solrDataDirPropertyName = "solr.data.dir";
	    String solrDataDirValue = "./target" + separator + "classes" + separator
		    + "data";
	    if (System.getProperty(solrDataDirPropertyName) == null
		    || !System.getProperty(solrDataDirPropertyName).equals(solrDataDirValue)) {
		logger.info("change system property from "
			+ System.getProperty(solrDataDirPropertyName) + " to " + solrDataDirValue);
		System.setProperty(solrDataDirPropertyName, solrDataDirValue);

		logger.info("System property" + solrDataDirPropertyName + " is now : "
			+ System.getProperty(solrDataDirPropertyName));
	    } else {
		logger.info(solrDataDirPropertyName + "=" + System.getProperty("file.encoding"));
	    }
	    tester = new ServletTester();

	    tester.addFilter(SolrDispatchFilter.class, "/*", 0);
	    tester.setContextPath(FULLTEXT_SEARCH_ENGINE_CONTEXT);

	    // TODO v2 a solrdir for test
	    // TODO v2 remove deprecated
	    tester.addServlet(SolrServlet.class, "/select/*");

	    tester.setResourceBase("target" + separator + "classes" + separator
		    + "solradmin");
	    tester.addServlet("org.mortbay.jetty.servlet.DefaultServlet", "/");
	    tester.addServlet(JspServlet.class, "*.jsp");

	    tester.addServlet(SolrUpdateServlet.class, "/update/*");

	    fulltextSearchUrl = tester.createSocketConnector(true)
		    + FULLTEXT_SEARCH_ENGINE_CONTEXT;
	    tester.start();

	    // set log to off
	    // comment this line to see solr logs
	    tester
		    .getResponses("GET /solr/admin/action.jsp?log=OFF HTTP/1.0\r\n\r\n");

	    this.solrClient.bindToUrl(fulltextSearchUrl);
	    serverStarted = true;
	}
	if (isMustStartServlet()) {
	    this.solRSynchroniser.deleteAll();
	}

    }

   

    /**
     * Shuts down the test harness, and makes the best attempt possible to
     * delete dataDir, unless the system property "solr.test.leavedatadir" is
     * set.
     */
    @Override
    public void onTearDown() throws Exception {
	super.onTearDown();
	// server.stop();
	solRSynchroniser.deleteAll();
	// tester.stop();
	// TODO v2 remove solrdir after all test
    }

    @Required
    public void setSolRSynchroniser(ISolRSynchroniser solRSynchroniser) {
	this.solRSynchroniser = solRSynchroniser;
    }

    @Required
    public void setFullTextSearchEngine(
	    IFullTextSearchEngine fullTextSearchEngine) {
	this.fullTextSearchEngine = fullTextSearchEngine;
    }

    /**
     * @param solrClient
     *                the solrClient to set
     */
    @Required
    public void setSolrClient(IsolrClient solrClient) {
	this.solrClient = solrClient;
    }

}
