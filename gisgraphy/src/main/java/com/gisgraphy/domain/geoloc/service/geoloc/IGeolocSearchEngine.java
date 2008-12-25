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
package com.gisgraphy.domain.geoloc.service.geoloc;

import com.gisgraphy.domain.geoloc.service.IQueryProcessor;
import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;

/**
 * Execute a {@linkplain GeolocQuery} and returns the results in a specific
 * format
 * 
 * @see GeolocQuery
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public interface IGeolocSearchEngine extends IQueryProcessor<GeolocQuery> {

    /**
     * Execute the query and returns a {@link GeolocResultsDto}
     * 
     * @param query
     *                The GeolocQuery to execute
     * @return The Java Objects. Never return null but an empty list
     * @throws ServiceException
     *                 If an error occurred
     */
    public GeolocResultsDto executeQuery(GeolocQuery query)
	    throws ServiceException;

}
