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

import javax.servlet.http.HttpServletRequest;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.vividsolutions.jts.geom.Point;

public class StreetGeolocQuery extends GeolocQuery {

    private String streetType = null;

    public StreetGeolocQuery(HttpServletRequest req) {
	super(req);
	//TODO OSM +test
    }

    /**
     * @param point
     *                the text to query, if the query is a number zipcode will
     *                be searched
     * @param radius
     *                The radius (distance)
     * @param pagination
     *                The pagination specification, if null : use default
     * @param output
     *                {@link Output} The output specification , if null : use
     *                default
     * @param streetType
     *                the type of street to search , if null : search for all street
     *                type.
     * @throws An
     *                 {@link IllegalArgumentException} if the point is null
     */
    public StreetGeolocQuery(Point point, double radius, Pagination pagination,
	    Output output, String streetType) {
	super(point, radius, pagination, output, null);
	withStreetType(streetType);
    }

    /**
     *  @param point
     *                the point to search street around
     * @param radius
     *                The radius (distance)
     * @param streetType
     *                the type of street to search , if null : search for all street
     *                type.
     * @throws An
     *                 {@link IllegalArgumentException} if the point is null
     */
    public StreetGeolocQuery(Point point, double radius, String streetType) {
	super(point, radius);
	withStreetType(streetType);
    }

    /**
     * @param point
     *                the point to search street around
     * @param streetType
     *                the type of street to search , if null : search for all street
     *                type.
     */
    public StreetGeolocQuery(Point point, String streetType) {
	super(point);
	withStreetType(streetType);
    }

    /**
     * @return the type of street we'd like to query
     */
    public String getStreetType() {
	return this.streetType;
    }

    /**
     * @param streetType the type of street we'd like to query
     * @return The current query to chain methods
     */
    public StreetGeolocQuery withStreetType(String streetType) {
	this.streetType = streetType;
	return this;
    }

}
