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

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

/**
 * Default implementation for IsolrClient.it represent a client to connect to solR server
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class SolrClient implements IsolrClient {

    protected static final Logger logger = LoggerFactory
	    .getLogger(SolrClient.class);

    private SolrServer server;

    private String URL;

    /**
     * Default constructor needed by spring
     */
    public SolrClient() {
	super();
    }

    /**
     * @param solrUrl
     *                The solr URL of the server to connect
     */
    @Autowired
    public SolrClient(@Qualifier("fulltextSearchUrl")
    String solrUrl, @Qualifier("multiThreadedHttpConnectionManager")
    MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager) {
	try {
	    Assert.notNull(solrUrl, "solrClient does not accept null solrUrl");
	    Assert
		    .notNull(multiThreadedHttpConnectionManager,
			    "solrClient does not accept null multiThreadedHttpConnectionManager");

	    this.server = new CommonsHttpSolrServer(new URL(solrUrl),
		    new HttpClient(multiThreadedHttpConnectionManager));
	    this.URL = solrUrl;
	    logger.info("connecting to solr on " + solrUrl + "...");
	} catch (MalformedURLException e) {
	    throw new RuntimeException("Error connecting to Solr! : "
		    + e.getMessage());
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient#bindToUrl(java.lang.String)
     */
    public void bindToUrl(String solrUrl) {
	try {
	    this.server = new CommonsHttpSolrServer(new URL(solrUrl));
	    this.URL = solrUrl;
	    logger
		    .info("fulltextSearchUrl for FullTextSearchEngine is changed to "
			    + solrUrl);
	} catch (Exception e) {
	    throw new RuntimeException("Error connecting to Solr!");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient#getConnection()
     */
    public SolrServer getServer() {
	return this.server;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient#getURL()
     */
    public String getURL() {
	return URL;
    }
    
    public boolean isServerAlive(){
	try {
	    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    XPath xpath = XPathFactory.newInstance().newXPath();
	    if (xpath == null || builder == null ) {
	       throw new RuntimeException("Can not determine if searchengine is alive");
	    }
	    SolrPingResponse response = getServer().ping();
	    if (response == null){
	        return false;
	    }
	    return ((String) response.getResponse().get("status")).equals("OK");
	} catch (Exception e) {
	    logger.error("can not determine if server is alive "+ e.getMessage());
	    return false;
	}
	
    }

}
