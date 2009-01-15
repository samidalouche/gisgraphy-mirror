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

import com.gisgraphy.domain.valueobject.GisgraphyServiceType;

/**
 * this exception is used when a format is not supported (e.g : not applicable for a specific {@linkplain GisgraphyServiceType}
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class UnsupportedFormatException extends RuntimeException {

    /**
     * 
     */
    public UnsupportedFormatException() {
	super();
	// TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     * @param arg1
     */
    public UnsupportedFormatException(String arg0, Throwable arg1) {
	super(arg0, arg1);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public UnsupportedFormatException(String arg0) {
	super(arg0);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param arg0
     */
    public UnsupportedFormatException(Throwable arg0) {
	super(arg0);
	// TODO Auto-generated constructor stub
    }

}
