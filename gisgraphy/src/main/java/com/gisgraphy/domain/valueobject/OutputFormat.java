package com.gisgraphy.domain.valueobject;

import com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor;
import com.gisgraphy.domain.geoloc.service.geoloc.GeolocErrorVisitor;

/**
 * All the possible output format, 
 * should be in uppercase
 */
public enum OutputFormat {
/**
 * 
 */
XML {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "standard";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "application/xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitXML(this);
    }
    
   

},
JSON {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "json";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "application/json";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitJSON(this);
    }
   
},
PYTHON {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "python";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "text/plain";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitPYTHON(this);
    }
    
   
},
PHP {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "php";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "text/plain";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitPHP(this);
    }
    
    
},
ATOM {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "xslt";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "application/xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitATOM(this);
    }
    
   
},
GEORSS {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "xslt";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "application/xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitGEORSS(this);
    }
    
    
},
RUBY {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "ruby";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "text/plain";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitRUBY(this);
    }
    
    
   
},
UNSUPPORTED {
    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.Output.OutputFormat#getParameterValue()
     */
    @Override
    public String getParameterValue() {
	return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#getContentType()
     */
    @Override
    public String getContentType() {
	return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.valueobject.Output.OutputFormat#accept(com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor)
     */
    @Override
    public String accept(IoutputFormatVisitor visitor) {
	return visitor.visitUNSUPPORTED(this);
    }
    
    
};

/**
 * @return The default format
 */
public static OutputFormat getDefault() {
    return OutputFormat.XML;
}

/**
 * @return The value of the parameter (e.g : the query type for fulltext
 *         query)
 */
public abstract String getParameterValue();

/**
 * @return The value of the HTTP contenttype associated to the Output
 *         format
 */
public abstract String getContentType();

/**
 * @param format
 *                the format as String
 * @return the outputFormat from the String or the default OutputFormat
 *         if the format can not be determine
 * @see #getDefault()
 */
public static OutputFormat getFromString(String format) {
    OutputFormat outputFormat = OutputFormat.getDefault();
    try {
	outputFormat = OutputFormat.valueOf(format.toUpperCase());
    } catch (RuntimeException e) {
    }
    return outputFormat;

}



public static final String RSS_VERSION = "rss_2.0";

public static final String ATOM_VERSION = "atom_0.3";

/**
 * Method to implement the visitor pattern
 * 
 * @param visitor
 *                accept a visitor
 * @return a string return by the visitor
 * @see IoutputFormatVisitor
 * @see GeolocErrorVisitor
 */
public abstract String accept(IoutputFormatVisitor visitor);

}
