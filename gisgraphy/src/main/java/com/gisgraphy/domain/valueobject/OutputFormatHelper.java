package com.gisgraphy.domain.valueobject;

import org.apache.commons.lang.NotImplementedException;

import com.gisgraphy.domain.geoloc.service.errors.UnsupportedFormatException;

public class OutputFormatHelper {

    private final static OutputFormat[] FULLTEXTSEARCH_SUPPORTED_FORMAT =  { OutputFormat.XML, OutputFormat.JSON,
		OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.PYTHON,OutputFormat.RUBY };
    
    private final static OutputFormat[] GEOLOCSEARCH_SUPPORTED_FORMAT = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.ATOM, OutputFormat.GEORSS };
    
    private final static OutputFormat[] STREETSEARCH_SUPPORTED_FORMAT = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.ATOM, OutputFormat.GEORSS };
    
	/**
	 * @param serviceType
	 *                the service type we'd like to know all the formats
	 * @return the formats for the specified service
	 * @throws NotImplementedException
	 *                 if the service is not implemented by the algorithm
	 */
	public static OutputFormat[] listFormatByService(
		GisgraphyServiceType serviceType) {
	    switch (serviceType) {
	    case FULLTEXT:
		return FULLTEXTSEARCH_SUPPORTED_FORMAT;
	    case GEOLOC:
		return GEOLOCSEARCH_SUPPORTED_FORMAT;
		
	    case STREET:
		return STREETSEARCH_SUPPORTED_FORMAT;
	    default:
		throw new NotImplementedException("The service type "
			+ serviceType + "is not implemented");
	    }
	
	}

	/**
	 * @param format
	 *                the format to check
	 * @param serviceType
	 *                the service type we'd like to know if the format is
	 *                applicable
	 * @return the format if the format is applicable for the service or the
	 *         default one.
	 * @throws UnsupportedFormatException
	 *                 if the service is not implemented by the algorithm
	 */
	public static OutputFormat getDefaultForServiceIfNotSupported(OutputFormat format,
		GisgraphyServiceType serviceType) {
	    switch (serviceType) {
	    case FULLTEXT:
		// fulltext accept all formats
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    case GEOLOC:
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    case STREET:
		return isFormatSupported(format,serviceType)==true?format:OutputFormat.getDefault();
	    default:
		throw new UnsupportedFormatException("The service type "
			+ serviceType + "is not implemented");
	    }
	}
	
	/**
	 * @param serviceType the type of service we'd like to know if the format is supported
	 * @param outputFormat the output format
	 * @return true if the format is supported by the specified {@link GisgraphyServiceType}
	 */
	public static boolean isFormatSupported(OutputFormat outputFormat,GisgraphyServiceType serviceType){
		for (OutputFormat format : listFormatByService(serviceType)){
		    if (format == outputFormat){
			return true;
		    }
		} return false;
	}

}
