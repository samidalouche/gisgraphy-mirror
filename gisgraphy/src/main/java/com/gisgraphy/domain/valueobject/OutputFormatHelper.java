package com.gisgraphy.domain.valueobject;

import org.apache.commons.lang.NotImplementedException;

import com.gisgraphy.domain.geoloc.service.errors.UnsupportedFormatException;

public class OutputFormatHelper {

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
		// fulltext accept all formats
		return OutputFormat.values();
	    case GEOLOC:
		OutputFormat[] resultGeoloc = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.ATOM, OutputFormat.GEORSS };
		return resultGeoloc;
		
	    case STREET:
		OutputFormat[] resultStreet = { OutputFormat.XML, OutputFormat.JSON, OutputFormat.ATOM, OutputFormat.GEORSS };
		return resultStreet;
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
		if (outputFormat == OutputFormat.XML){
			return true;
		} else if (outputFormat == OutputFormat.JSON){
			return true;
		}  else if (outputFormat == OutputFormat.PHP){
			return serviceType == GisgraphyServiceType.FULLTEXT;
		} else if (outputFormat == OutputFormat.PYTHON){
			return serviceType == GisgraphyServiceType.FULLTEXT;
		} else if (outputFormat == OutputFormat.RUBY){
			return serviceType == GisgraphyServiceType.FULLTEXT;
		} else if (outputFormat == OutputFormat.GEORSS){
			return true;
		} else if (outputFormat == OutputFormat.ATOM){
			return true;
		} else {
			return false;
		}
	}

}
