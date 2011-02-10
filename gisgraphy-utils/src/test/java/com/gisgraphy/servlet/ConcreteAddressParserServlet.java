/**
 * 
 */
package com.gisgraphy.servlet;

import com.gisgraphy.addressparser.IAddressParserService;
import com.gisgraphy.addressparser.web.AbstractAddressParserServlet;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.serializer.IoutputFormatVisitor;

public class ConcreteAddressParserServlet extends AbstractAddressParserServlet{
	
    /**
     * 
     */
    private static final long serialVersionUID = -2124269033751536102L;



	public ConcreteAddressParserServlet() {
		super();
	}


	@Override
	public IoutputFormatVisitor getErrorVisitor(String errorMessage) {
	    return null;
	}



	@Override
	public GisgraphyServiceType getGisgraphyServiceType() {
	    return null;
	}



	@Override
	public IAddressParserService getAddressParserService() {
	    return null;
	}
	
}