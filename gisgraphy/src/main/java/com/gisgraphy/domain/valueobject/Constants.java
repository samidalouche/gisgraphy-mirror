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
/**
 *
 */
package com.gisgraphy.domain.valueobject;

import org.dom4j.io.OutputFormat;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a> Some
 *         Constants
 */
public class Constants {

    /**
     * Default charset
     */
    public static final String CHARSET = "UTF-8";

    /**
     * Default Date pattern
     */
    public static final String GIS_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * The field on which we want to search
     */
    public static final String QF_PARAMETER = "qf";

    /**
     * The output format (XML, json,...) parameter name
     * 
     * @see OutputFormat
     */
    public final static String OUTPUT_FORMAT_PARAMETER = "wt";
    
    /**
     * The XSLT stylesheet parameter name
     * 
     * @see OutputFormat
     */
    public final static String STYLESHEET_PARAMETER = "tr";
    
    /**
     * The name of the XSL to apply to output ATOM format
     * 
     * @see OutputFormat
     */
    public final static String ATOM_STYLESHEET = "atom.xsl";
    
    /**
     * The name of the XSL to apply to output Geo RSS format
     * 
     * @see OutputFormat
     */
    public final static String GEORSS_STYLESHEET = "georss.xsl";

    /**
     * The field list to return
     */
    public static final String FL_PARAMETER = "fl";

    /**
     * The parameter to specifie the text to search
     */
    public static final String QUERY_PARAMETER = "q";

    /**
     * The query type parameter (dismax, standard, ...)
     */
    public static final String QT_PARAMETER = "qt";

    /**
     * The rows parameter (useful for paginate)
     */
    public static final String ROWS_PARAMETER = "rows";

    /**
     * The indent parameter
     */
    public static final String INDENT_PARAMETER = "indent";

    /**
     * The echoparam parameter (if the query parameters should be print in the
     * response)
     */
    public static final String ECHOPARAMS_PARAMETER = "echoParams";

    /**
     * The start rows parameter (useful for paginate)
     */
    public static final String START_PARAMETER = "start";

    /**
     * A string that is used to load a class from a string with class.forName()
     */
    public static final String ENTITY_PACKAGE = "com.gisgraphy.domain.geoloc.entity.";

    /**
     * All the Query type in the Gisgraphy solr config file
     */
    public enum SolrQueryType {
	standard, typed, numeric, simple, deep, advanced
    };

    /**
     * The radius of the earth
     */
    public static final double RADIUS_OF_EARTH_IN_METERS = 6371008;

    /**
     * An Array of the applicationContext names
     */
    public final static String[] APPLICATION_CONTEXT_NAMES = new String[] {

    "classpath:/applicationContext.xml",
	    "classpath:/applicationContext-resources.xml",
	    "classpath:/applicationContext-repository.xml",
	    "classpath:/applicationContext-geoloc.xml",
	    "classpath:/applicationContext-dao.xml",
	    "classpath:/applicationContext-service.xml",
	    "classpath:/WEB-INF/applicationContext-struts.xml",
	    "classpath:**/applicationContext*.xml" };

    /**
     * An Array of the ApplicationContext names for tests
     */
    public final static String[] APPLICATION_CONTEXT_NAMES_FOR_TEST = new String[] {
	   
	    "classpath:/applicationContext.xml",
	    "classpath:/applicationContext-resources.xml",
	    "classpath:/applicationContext-repository.xml",
	    "classpath:/applicationContext-geoloc.xml",
	    "classpath:/applicationContext-dao.xml",
	    "classpath:/applicationContext-service.xml",
	    "classpath:/WEB-INF/applicationContext-struts.xml",
	    "classpath:**/applicationContext*.xml",
	    "classpath:/applicationContext-test.xml",
	    "classpath:/applicationContext-dao-test.xml"};

    /**
     * A String of the ApplicationContext names
     */
    public final static String APPLICATION_CONTEXT_NAMES_STRING = new StringBuffer(
	    "classpath:/applicationContext.xml,").append(
	    "classpath:/applicationContext-resources.xml,").append(
	    "classpath:/applicationContext-repository.xml,").append(
	    "classpath:/applicationContext-geoloc.xml,").append(
	    "classpath:/applicationContext-dao.xml,").toString();

    /**
     * A String of the ApplicationContext names used for tests
     */
    public final static String APPLICATION_CONTEXT_NAMES_STRING_FOR_TESTS = new StringBuffer(
	    "classpath:/applicationContext-test.xml,").append(
	    "classpath:/applicationContext.xml,").append(
	    "classpath:/applicationContext-resources.xml,").append(
	    "classpath:/applicationContext-repository.xml,").append(
	    "classpath:/applicationContext-geoloc.xml,").append(
	    "classpath:/applicationContext-dao.xml,").append(
	    "classpath:/applicationContext-service.xml,").append(
	    "Classpath:/WEB-INF/applicationContext-struts.xml,").append(
	    "classpath:/applicationContext-dao-test.xml")
	    .toString();

    /**
     * The node name for {@link GisFeatureDistance} node in JAXB
     */
    public final static String GISFEATUREDISTANCE_JAXB_NAME = "result";

    /**
     * The node name for {@link GeolocResultsDto} node in JAXB
     */
    public final static String GEOLOCRESULTSDTO_JAXB_NAME = "results";

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * The name of the ResourceBundle used in this application for errors
     */
    public static final String BUNDLE_ERROR_KEY = "ApplicationErrorResources";

    /**
     * The name of the ResourceBundle for Feature class / code used in this
     * application
     */
    public static final String FEATURECODE_BUNDLE_KEY = "featurecodes";

  
  }
