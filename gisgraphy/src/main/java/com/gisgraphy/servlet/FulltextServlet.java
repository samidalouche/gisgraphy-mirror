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
import java.io.Writer;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextErrorVisitor;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IFullTextSearchEngine;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.helper.EncodingHelper;
import com.gisgraphy.helper.HTMLHelper;

/**
 * Provides a servlet Wrapper around The Gisgraphy fulltext Service
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * @see GeolocServlet
 */
public class FulltextServlet extends HttpServlet {

    public static final String FROM_PARAMETER = "from";
    public static final String TO_PARAMETER = "to";
    public static final String COUNTRY_PARAMETER = "country";
    public static final String LANG_PARAMETER = "lang";
    public static final String FORMAT_PARAMETER = "format";
    public static final String STYLE_PARAMETER = "style";
    public static final String INDENT_PARAMETER = "indent";
    public static final String PLACETYPE_PARAMETER = "placetype";
    public static final String QUERY_PARAMETER = "q";
    public static final int DEFAULT_MAX_RESULTS = 10;
    public static final String SPELLCHECKING_PARAMETER = "spellchecking";

    private boolean debugMode = false;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
	try {
	    WebApplicationContext springContext = WebApplicationContextUtils
		    .getWebApplicationContext(getServletContext());
	    fullTextSearchEngine = (IFullTextSearchEngine) springContext
		    .getBean("fullTextSearchEngine");
	    logger.info("fullTextSearchEngine is injected :"
		    + fullTextSearchEngine);
	    this.debugMode = Boolean.valueOf(getInitParameter("debugMode"));
	    logger.info("GeolocServlet debugmode = " + this.debugMode);
	    EncodingHelper.setJVMEncodingToUTF8();
	} catch (Exception e) {
	    logger.error("Can not start fulltextServlet : " + e.getMessage());
	    e.printStackTrace();
	}
    }

    /**
     * Default serialVersionUID
     */
    private static final long serialVersionUID = -9054548241743095743L;

    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(FulltextServlet.class);

    private IFullTextSearchEngine fullTextSearchEngine;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	OutputFormat format = OutputFormat.getDefault();
	try {
	    format = setResponseContentType(req, resp);
	    // check empty query
	    if (HTMLHelper.isParametersEmpty(req, QUERY_PARAMETER)) {
		sendCustomError(ResourceBundle.getBundle(
			Constants.BUNDLE_ERROR_KEY).getString(
			"error.emptyQuery"), format, resp,req);
		return;
	    }
	    FulltextQuery query = new FulltextQuery(req);
	    logger.debug("query=" + query);
	    logger.debug("fulltext engine=" + fullTextSearchEngine);
	    fullTextSearchEngine.executeAndSerialize(query, resp
		    .getOutputStream());
	} catch (RuntimeException e) {
	    logger.error("error while creating full text query : " + e);
	    e.printStackTrace();
	    String errorMessage = this.debugMode ? " : " + e.getMessage() : "";
	    sendCustomError(ResourceBundle
		    .getBundle(Constants.BUNDLE_ERROR_KEY).getString(
			    "error.error")
		    + errorMessage, format, resp,req);
	    return;
	}

    }
    
    private OutputFormat setResponseContentType(HttpServletRequest req,
	    HttpServletResponse resp) {
	OutputFormat format;
	String formatParam = req.getParameter(FORMAT_PARAMETER);
	format = OutputFormat.getFromString(formatParam);
	format = OutputFormat.getDefaultForServiceIfNotSupported(format, GisgraphyServiceType.FULLTEXT);
	resp.setHeader("content-type", format.getContentType());
	return format;
    }

    public void sendCustomError(String errorMessage, OutputFormat format,
	    HttpServletResponse resp,HttpServletRequest req) {
	IoutputFormatVisitor visitor = new FulltextErrorVisitor(errorMessage);
	String response = format.accept(visitor);
	Writer writer = null;
	try {
	    resp.reset();
	    setResponseContentType(req, resp);
	    writer = resp.getWriter();
	    writer.append(response);
	    writer.flush();
	} catch (IOException e) {
	    logger.warn("Error when sending error");
	} finally {
	    if (writer != null) {
		try {
		    writer.close();
		} catch (IOException e) {
		    logger
			    .warn("Error when closing writer after sending error");
		}
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doOptions(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doTrace(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * @param fullTextSearchEngine
     *                the fullTextSearchEngine to set
     */
    public void setFullTextSearchEngine(
	    IFullTextSearchEngine fullTextSearchEngine) {
	this.fullTextSearchEngine = fullTextSearchEngine;
    }

}
