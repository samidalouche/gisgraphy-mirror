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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocErrorVisitor;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocQuery;
import com.gisgraphy.domain.geoloc.service.geoloc.IGeolocSearchEngine;
import com.gisgraphy.domain.geoloc.service.geoloc.IStreetSearchEngine;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchErrorVisitor;
import com.gisgraphy.domain.geoloc.service.geoloc.StreetSearchQuery;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.helper.HTMLHelper;

/**
 * Provides a servlet Wrapper around The Gisgraphy street Service, it Maps web
 * parameters to create a {@linkplain StreetSearchQuery}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class StreetServlet extends GisgraphyServlet {

    /**
     * Default serialVersionUID
     */
    private static final long serialVersionUID = 8544156407519263142L;
    
    public static final String STREETTYPE_PARAMETER = "streettype";
    public static final String LAT_PARAMETER = "lat";
    public static final String LONG_PARAMETER = "lng";
    public static final String RADIUS_PARAMETER = "radius";
    public static final String ONEWAY_PARAMETER = "oneway";
    public static final String NAME_PREFIX_PARAMETER = "nameprefix";
    public static final int DEFAULT_MAX_RESULTS = 50;
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
	    streetSearchEngine = (IStreetSearchEngine) springContext
		    .getBean("streetSearchEngine");
	    logger
		    .info("streetSearchEngine is injected :"
			    + streetSearchEngine);
	    this.debugMode = Boolean.valueOf(getInitParameter("debugMode"));
	    logger.info("StreetServlet debugmode = " + this.debugMode);
	} catch (Exception e) {
	    logger.error("Can not start StreetServlet : " + e.getMessage());
	}
    }


    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(StreetServlet.class);

    private IStreetSearchEngine streetSearchEngine;

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
	    if (HTMLHelper
		    .isParametersEmpty(req, LAT_PARAMETER, LONG_PARAMETER)) {
		sendCustomError(ResourceBundle.getBundle(
			Constants.BUNDLE_ERROR_KEY).getString(
			"error.emptyLatLong"), format, resp,req);
		return;
	    }
	    StreetSearchQuery query = new StreetSearchQuery(req);
	    if (logger.isDebugEnabled()){
	    logger.debug("query=" + query);
	    }
	    String UA = req.getHeader("User-Agent");
	    String referer = req.getHeader("Referer");
	    if (logger.isInfoEnabled()){
		logger.info("A street requestfrom "+req.getRemoteHost()+" / "+req.getRemoteAddr()+" was received , Referer : "+referer+" , UA : "+UA);
	    }

	    streetSearchEngine.executeAndSerialize(query, resp
		    .getOutputStream());
	} catch (RuntimeException e) {
	    logger.error("error while creating geoloc query : " + e);
	    String errorMessage = this.debugMode ? " : " + e.getMessage() : "";
	    sendCustomError(ResourceBundle
		    .getBundle(Constants.BUNDLE_ERROR_KEY).getString(
			    "error.error")
		    + errorMessage, format, resp,req);
	    return;
	}

    }


 

   
    /**
     * @param streetSearchEngine
     *                the streetSearchEngine to set
     */
    public void setStreetSearchEngine(IStreetSearchEngine streetSearchEngine) {
	this.streetSearchEngine = streetSearchEngine;
    }

   

    /* (non-Javadoc)
     * @see com.gisgraphy.servlet.GisgraphyServlet#getGisgraphyServiceType()
     */
    @Override
    public GisgraphyServiceType getGisgraphyServiceType() {
	return GisgraphyServiceType.STREET;
    }


    /* (non-Javadoc)
     * @see com.gisgraphy.servlet.GisgraphyServlet#getErrorVisitor(java.lang.String)
     */
    @Override
    public IoutputFormatVisitor getErrorVisitor(String errorMessage) {
	return new StreetSearchErrorVisitor(errorMessage);
    }

}
