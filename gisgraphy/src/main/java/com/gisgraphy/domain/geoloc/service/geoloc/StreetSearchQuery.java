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

import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.servlet.FulltextServlet;
import com.gisgraphy.servlet.StreetServlet;
import com.vividsolutions.jts.geom.Point;

public class StreetSearchQuery extends GeolocQuery {
    
    public final static int NAME_PREFIX_MAX_LENGTH = 200;

    private StreetType streetType = null;
    
    private String namePrefix = null;

    private String oneWay;
    

    public StreetSearchQuery(HttpServletRequest req) {
	super(req);
	//streettype
	withStreetType(StreetType.getFromString(req
	.getParameter(StreetServlet.STREETTYPE_PARAMETER)));
	
	//OneWay
	if ("true".equalsIgnoreCase(req
		.getParameter(StreetServlet.ONEWAY_PARAMETER))
		|| "on".equalsIgnoreCase(req
			.getParameter(StreetServlet.ONEWAY_PARAMETER))) {
	    withOneWay("true");
	}
	//namePrefix
	withNamePrefix(req.getParameter(FulltextServlet.QUERY_PARAMETER));

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
     * @param oneWay the oneWay type criteria of the street
     * @throws An
     *                 {@link IllegalArgumentException} if the point is null
     */
    public StreetSearchQuery(Point point, double radius, Pagination pagination,
	    Output output, StreetType streetType,String oneWay,String namePrefix) {
	super(point, radius, pagination, output, null);
	withStreetType(streetType).
	withNamePrefix(namePrefix).withOneWay(oneWay);
    }

    /**
     *  @param point
     *                the point to search street around
     * @param radius
     *                The radius (distance)
     * @param streetType
     *                the type of street to search , if null : search for all street
     *                type.
     * @param oneWay the oneWay type criteria of the street
     * @throws An
     *                 {@link IllegalArgumentException} if the point is null
     */
    public StreetSearchQuery(Point point, double radius, StreetType streetType) {
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
    public StreetSearchQuery(Point point, StreetType streetType) {
	super(point);
	withStreetType(streetType);
    }

    /**
     * @return the type of street we'd like to query
     */
    public StreetType getStreetType() {
	return this.streetType;
    }

    /**
     * @param streetType the StreetType of street we'd like to query
     * @return The current query to chain methods
     */
    public StreetSearchQuery withStreetType(StreetType streetType) {
	this.streetType = streetType;
	return this;
    }
    
    /**
     * @return the string that the street must starts with (aka : 'name%'). 
     */
    public String getNamePrefix() {
	return this.namePrefix;
    }

    /**
     * @param namePrefix the string that the street must starts with (aka : 'name%').
     * Don't prefix with 'Street' !
     * not taken into account if empty string or null.
     * @throws StreetSearchException if length is greater than @see {@link StreetSearchQuery#NAME_PREFIX_MAX_LENGTH}
     * @return The current query to chain methods
     */
    public StreetSearchQuery withNamePrefix(String namePrefix) {
	if (namePrefix == null || "".equals(namePrefix.trim())){
	    return this;
	}
	
	if (namePrefix.length() > StreetSearchQuery.NAME_PREFIX_MAX_LENGTH) {
	    throw new StreetSearchException("namePrefix is limited to "
		    + StreetSearchQuery.NAME_PREFIX_MAX_LENGTH + "characters");
	}
	
	this.namePrefix = namePrefix;
	return this;
    }
    
    /**
     * @param oneWay The oneWay type criteria of the street
     * @return  The current query to chain methods
     */
    public StreetSearchQuery withOneWay(String oneWay){
	this.oneWay = oneWay;
	return this;
    }

    @Override
    public int getMaxLimitResult() {
	return StreetServlet.DEFAULT_MAX_RESULTS;
    }

    /**
     * @return the oneWay criteria
     */
    public String getOneWay() {
	return this.oneWay;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String asString = "StreetSearchQuery (lat='"
		+ getPoint().getY() + "',long='" + getPoint().getX() + "') and namePrefix="+this.namePrefix+" and radius="
		+ getRadius() + " for streetType="+streetType+" and oneWay="+this.oneWay;
	asString += " with " + getOutput() + " and " + pagination;
	return asString;
    }
    
}
