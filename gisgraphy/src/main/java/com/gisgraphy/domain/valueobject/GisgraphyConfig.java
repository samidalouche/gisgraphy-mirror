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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.domain.geoloc.entity.GisFeature;

/**
 * A bean that contains some property for gisgraphy
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class GisgraphyConfig {

    /**
     * The logger
     */
    public static  Logger logger = LoggerFactory
	    .getLogger(GisgraphyConfig.class);

    /**
     * 
     */
    public static Class<? extends GisFeature> defaultGeolocSearchPlaceTypeClass = null;
    
    
    public static  String googleMapAPIKey ;
    
    public static  String googleanalytics_uacctcode ;

	public static final String ENVIRONEMENT_PROPERTIES_FILE = "env";

    /**
     * @param defaultGeolocSearchPlaceType
     *                the defaultGeolocSearchPlaceType to set
     */
    @SuppressWarnings("unchecked")
    public void setDefaultGeolocSearchPlaceType(
	    String defaultGeolocSearchPlaceType) {
	if (defaultGeolocSearchPlaceType == null
		|| defaultGeolocSearchPlaceType.trim().length() == 0) {
	    defaultGeolocSearchPlaceTypeClass = null;
	}
	try {
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = (Class<? extends GisFeature>) Class
		    .forName(Constants.ENTITY_PACKAGE
			    + defaultGeolocSearchPlaceType);
	    logger.info("defaultGeolocSearchPlaceType"
		    + GisgraphyConfig.defaultGeolocSearchPlaceTypeClass);
	} catch (ClassNotFoundException e) {
	    logger.warn("can not set defaultGeolocSearchPlaceTypeClass with "
		    + defaultGeolocSearchPlaceType + " : " + e.getMessage());
	    GisgraphyConfig.defaultGeolocSearchPlaceTypeClass = null;
	}
    }



    /**
     * @param googleMapAPIKey the googleMapAPIKey to set
     */
    public void setGoogleMapAPIKey(String googleMapAPIKey) {
	if (googleMapAPIKey==null || "".equals(googleMapAPIKey.trim())){
	    logger.warn("googleMapAPIKey is not set, please set it in env.properties file and re-launch Gisgraphy, if you want to use google maps functionnalities, ");
	}
	else {
	    logger.info("set googleMapAPIKey to "+googleMapAPIKey);
	}
        GisgraphyConfig.googleMapAPIKey = googleMapAPIKey;
    }
    
    /**
     * @param code the Google analytics uacctcode to set
     */
    public void setGoogleanalytics_uacctcode(String code) {
	if (code==null || "".equals(code.trim())){
	    logger.warn("googleanalytics_uacctcode is not set, please set it in env.properties file and re-launch Gisgraphy, if you want to use google analytics functionnalities, ");
	}
	else {
	    logger.info("set googleanalytics_uacctcode to "+code);
	}
        GisgraphyConfig.googleanalytics_uacctcode = code;
    }

    
}
