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
package com.gisgraphy.domain.geoloc.service.errors;

import com.gisgraphy.domain.valueobject.Output.OutputFormat;

/**
 * Visitao (visitor Pattern) for the outputFormat enum
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public interface IoutputFormatVisitor {
    
    /**
     * The default error message when no message is given
     */
    public static final String DEFAULT_ERROR_MESSAGE = "Internal Error";

    /**
     * @param format
     *                The OutputFormat
     * @return the String in the XML format
     */
    String visitXML(OutputFormat format);

    /**
     * @param format
     *                The OutputFormat
     * @return the String in the JSON format
     */
    String visitJSON(OutputFormat format);

    /**
     * @param format
     *                The OutputFormat
     * @return the String in the PYTHON format
     */
    String visitPYTHON(OutputFormat format);

    /**
     * @param format
     *                The OutputFormat
     * @return the String in the RUBY format
     */
    String visitRUBY(OutputFormat format);

    /**
     * @param format
     *                The OutputFormat
     * @return the String in the PHP format
     */
    String visitPHP(OutputFormat format);

    /**
     * @param format
     *                The OutputFormat
     * @return the String in the ATOM format
     */
    String visitATOM(OutputFormat format);

    /**
     * @param format
     *                The OutputFormat
     * @return the String in the RSS format
     */
    String visitGEORSS(OutputFormat format);
    
    /**
     * @param format
     *                The OutputFormat
     * @return the String in the UNSUPPORTED format
     */
    String visitUNSUPPORTED(OutputFormat format);
    
    /**
     * @return the errorMessage
     */
    public String getErrorMessage();

}