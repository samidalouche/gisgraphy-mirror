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
package com.gisgraphy.addressparser.web;

import javax.servlet.http.HttpServletRequest;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.OutputFormatHelper;
import com.gisgraphy.serializer.OutputFormat;

/**
 * An address Query builder. it build Address query from HTTP Request
 * 
 * @see AddressParser
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class AddressQueryHttpBuilder {
	
	
 private static AddressQueryHttpBuilder instance = new AddressQueryHttpBuilder();
 
 public static AddressQueryHttpBuilder getInstance(){
	 return instance;
	 
 }
    /**
     * @param req
     *                an HttpServletRequest to construct a {@link AddressQuery}
     */
    public AddressQuery buildFromRequest(HttpServletRequest req) {
    	
    	//address Parameter
	String adressParameter = req.getParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER);
	if (adressParameter == null || "".equals(adressParameter.trim())){
	    throw new AddressParserException("address is not specified or empty");
	}
	if (adressParameter.length() > AbstractAddressParserServlet.QUERY_MAX_LENGTH) {
	    throw new AddressParserException("address is limited to "
		    + AbstractAddressParserServlet.QUERY_MAX_LENGTH + "characters");
	}
	//country parameter
	String countryParameter = req.getParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER);
	if (countryParameter == null || countryParameter.trim().length()==0){
	    throw new AddressParserException("country parameter is not specified or empty");
	}
	
	AddressQuery query = new AddressQuery(adressParameter,countryParameter);
	//outputformat
	OutputFormat outputFormat = OutputFormat.getFromString(req
			.getParameter(AbstractAddressParserServlet.FORMAT_PARAMETER));
	outputFormat =OutputFormatHelper.getDefaultForServiceIfNotSupported(outputFormat, GisgraphyServiceType.ADDRESS_PARSER);
	query.setFormat(outputFormat);
	

	String callbackParameter = req.getParameter(AbstractAddressParserServlet.CALLBACK_PARAMETER);
	if (callbackParameter!=null){
	    query.setCallback(callbackParameter);
	}
	// indent
	if ("true".equalsIgnoreCase(req
		.getParameter(AbstractAddressParserServlet.INDENT_PARAMETER))
		|| "on".equalsIgnoreCase(req
			.getParameter(AbstractAddressParserServlet.INDENT_PARAMETER))) {
	    query.setIndent(true);
	}
	
	
	return query;
    } 

    

}
