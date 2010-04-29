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
package com.gisgraphy.domain.geoloc.service.geoloc.street;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gisgraphy.domain.geoloc.importer.ImporterHelper;

/**
 * Parse a String and try to extract street number, zipcode etc
 * 
 * @author @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public class PostalAddressParser {
    
    Map<String, List<Pattern>> AddressRegexpsByCountryCode = new HashMap<String, List<Pattern>>();
    
    public PostalAddressParser() {
	AddressRegexpsByCountryCode.put("fr", ImporterHelper.compileRegex("^([0-9\\s]*)\\s*([\\w+\\s+]+)(\\d{5}|[,]+)\\s+([\\w+\\s+]+)"));
    }
    
    /**
     * Parse a String and try to extract street number, zipcode etc
     * @param address  the string that represent an address
     * @param countryCode the countrycode of the country of the adresse
     * @return the object with the field extracting
     */
    public Address parse(String address,String countryCode){
	List<Pattern> patterns = AddressRegexpsByCountryCode.get(countryCode);
	if (patterns == null){
	    throw new RuntimeException("Can not get regexp for "+countryCode);
	}
	for (Pattern pattern: patterns){
	 Matcher matcher = pattern.matcher(address);
	   if (matcher.find()) {
	      System.err.println(matcher.group(1));
	      System.err.println(matcher.group(2));
	      System.err.println(matcher.group(3));
	      System.err.println(matcher.group(4));
	      //System.err.println(matcher.group(5));
	      
	   }
	}

	return null;
    }

}
