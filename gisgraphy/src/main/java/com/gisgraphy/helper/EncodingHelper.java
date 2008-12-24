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
package com.gisgraphy.helper;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.Constants;

/**
 * Encoding helper
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class EncodingHelper {
    
    protected static final Logger logger = LoggerFactory
    .getLogger(GisFeature.class);
    

    /**
     * useful for windows only, this method is a workaround for encoding
     * problems on Windows.<br>
     * any suggestion are welcomed
     * 
     * @return the string in utf-8
     */
    public static String toUTF8(String string) {
	String utf8 = "";
	try {
	    utf8 = new String(string.getBytes(Constants.CHARSET));
	} catch (UnsupportedEncodingException e1) {
	    e1.printStackTrace();
	    throw new RuntimeException("can not change String Encoding");
	}
	return utf8;
    }
    
    /**
     * Set the file.encoding and sun.jnu.encoding to UTF-8
     */
    public static void setJVMEncodingToUTF8() {
	setSystemProperty("file.encoding",Constants.CHARSET);
	setSystemProperty("sun.jnu.encoding",Constants.CHARSET);
    }

    private static void setSystemProperty(String name,String value) {
	if (System.getProperty(name)== null || !System.getProperty(name).equals(value)) {
	    logger.info("change system property from "
		    + System.getProperty(name) + " to "
		    + value);
	    System.setProperty(name, value);
	   
	    logger.info("System property"+name+" is now : "
		    + System.getProperty(name));
	} else {
	    logger.info(name+"="
		    + System.getProperty("file.encoding"));
	}
    }
}
