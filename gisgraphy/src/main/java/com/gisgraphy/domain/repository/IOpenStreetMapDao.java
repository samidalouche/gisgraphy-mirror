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
package com.gisgraphy.domain.repository;

import java.util.List;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.vividsolutions.jts.geom.Point;

public interface IOpenStreetMapDao extends IDao<OpenStreetMap, java.lang.Long> {

    /**
     * base method for all findNearest
     * 
     * @param point
     *                The point from which we want to find GIS Object
     * @param distance
     *                The radius in meters
     * @param firstResult
     *                the firstResult index (for pagination), numbered from 1,
     *                if < 1 : it will not be taken into account
     * @param maxResults
     *                The Maximum number of results to retrieve (for
     *                pagination), if <= 0 : it will not be taken into acount
     * @param streetType
     *                The type of street
     * @param oneWay
     *                whether the street should be oneway or not
     * @param name
     *                the name the street name must contains
     * 
     * @return A List of StreetDistance with the nearest elements or an
     *         empty list (never return null), ordered by distance.
     * @see StreetDistance
     */
    public List<StreetDistance> getNearestAndDistanceFrom(
	    final Point point, final double distance,
	    final int firstResult, final int maxResults,
	    final StreetType streetType,Boolean oneWay, final String name) ;
    
    /**
     * @param gid the gid of the openstreetmap entity we want to retrieve
     * @return the OpenstreetMap entity or null if not found
     */
    public OpenStreetMap getByGid(final Long gid) ;
    
}
