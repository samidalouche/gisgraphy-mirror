package com.gisgraphy.addressparser.web;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.IAddressParserService;
import com.gisgraphy.addressparser.exception.AddressParserErrorVisitor;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.domain.Constants;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.HTMLHelper;
import com.gisgraphy.serializer.IoutputFormatVisitor;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.GisgraphyServlet;

public abstract class AbstractAddressParserServlet extends GisgraphyServlet {

    /**
     * The logger
     */
    protected static Logger logger = Logger.getLogger(AbstractAddressParserServlet.class);

    public final static String ADDRESS_PARAMETER = "address";
    public final static String COUNTRY_PARAMETER = "country";
    public final static String OUTPUT_FORMAT_PARAMETER = "format";
    public final static String CALLBACK_PARAMETER = "callback";
    public final static String INDENT_PARAMETER = "indent";
    public static final int QUERY_MAX_LENGTH = 400;

    
    public abstract IAddressParserService getAddressParserService() ;
    
    protected AddressQueryHttpBuilder getAddressQueryHttpBuilder(){
    	return AddressQueryHttpBuilder.getInstance();
    }
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 7804855543117309510L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws AddressParserException ,IOException{
	OutputFormat format = OutputFormat.getDefault();
	try {
	    format = setResponseContentType(req, resp);
	    // check empty query
	    if (HTMLHelper.isParametersEmpty(req, ADDRESS_PARAMETER,COUNTRY_PARAMETER)) {
		sendCustomError(ResourceBundle.getBundle(
			com.gisgraphy.addressparser.Constants.BUNDLE_KEY).getString(
			"error.requiredparameters"), format, resp,req);
		return;
	    }
	    
		AddressQuery query = getAddressQueryHttpBuilder().buildFromRequest(req);
	    if (logger.isDebugEnabled()){
		logger.debug("query=" + query);
	    }
	    String UA = req.getHeader(Constants.HTTP_USER_AGENT_HEADER_NAME);
	    String referer = req.getHeader(Constants.HTTP_REFERER_HEADER_NAME);
	    if (logger.isInfoEnabled()){
		logger.info("A parser request from "+req.getRemoteHost()+" / "+req.getRemoteAddr()+" was received , Referer : "+referer+" , UA : "+UA);
	    }
	    
	    getAddressParserService().executeAndSerialize(query, resp
		    .getOutputStream());
	} catch (RuntimeException e) {
	    logger.error("error while execute a Parser query from http request : " + e);
	    String errorMessage = this.debugMode ? " : " + e.getMessage() : "";
	    sendCustomError(ResourceBundle
		    .getBundle(com.gisgraphy.addressparser.Constants.BUNDLE_KEY).getString(
			    "error.error")
		    + errorMessage, format, resp,req);
	}
    }

    @Override
    public IoutputFormatVisitor getErrorVisitor(String errorMessage) {
	return new AddressParserErrorVisitor(errorMessage);
    }

    @Override
    public GisgraphyServiceType getGisgraphyServiceType() {
	return GisgraphyServiceType.ADDRESS_PARSER;
    }

}
