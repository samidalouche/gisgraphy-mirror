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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.AbstractGisQuery;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.OutputFormatHelper;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.GeolocServlet;
import com.gisgraphy.servlet.GisgraphyServlet;
import com.vividsolutions.jts.geom.Point;

/**
 * A GeolocQuery Query
 * 
 * @see Pagination
 * @see Output
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeolocQuery extends AbstractGisQuery {

   

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result
		+ ((placeType == null) ? 0 : placeType.hashCode());
	result = prime * result + ((point == null) ? 0 : point.hashCode());
	long temp;
	temp = Double.doubleToLongBits(radius);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final GeolocQuery other = (GeolocQuery) obj;
	if (placeType == null) {
	    if (other.placeType != null)
		return false;
	} else if (!placeType.equals(other.placeType))
	    return false;
	if (point == null) {
	    if (other.point != null)
		return false;
	} else if (point.getX() != (other.point.getX())){
	    return false;
	}
	else if (point.getY() != (other.point.getY())){
	    return false;
	}
	if (Double.doubleToLongBits(radius) != Double
		.doubleToLongBits(other.radius))
	    return false;
	return true;
    }

    /**
     * needed by CGlib
     */
    @SuppressWarnings("unused")
    private GeolocQuery() {
	super();
    }

    /**
     * The type of gis to search , if null : search for all place type.
     */
    private Class<? extends GisFeature> placeType = GisgraphyConfig.defaultGeolocSearchPlaceTypeClass;

    /**
     * Default radius in meters
     */
    public static final double DEFAULT_RADIUS = 10000;

    private Point point;
    private double radius = DEFAULT_RADIUS;
    private boolean distanceField = true;

    /**
     * @param req
     *                an HttpServletRequest to construct a {@link GeolocQuery}
     */
    public GeolocQuery(HttpServletRequest req) {
	// point
	Float latitude;
	Float longitude;
	// lat
	try {
	    latitude = GeolocHelper.parseInternationalDouble(req
		    .getParameter(GeolocServlet.LAT_PARAMETER));
	} catch (Exception e) {
	    throw new GeolocSearchException("latitude is not correct or empty");
	}
	if (latitude == null) {
	    throw new GeolocSearchException("latitude is not correct or empty");
	}
	// long
	try {
	    longitude = GeolocHelper.parseInternationalDouble(req
		    .getParameter(GeolocServlet.LONG_PARAMETER));
	} catch (Exception e) {
	    throw new GeolocSearchException(
		    "longitude is not correct or empty");
	}
	if (longitude == null) {
	    throw new GeolocSearchException(
		    "longitude is not correct or empty");
	}
	// point
	try {
	    this.point = GeolocHelper.createPoint(longitude, latitude);
	} catch (RuntimeException e1) {
	    throw new GeolocSearchException(e1.getMessage());
	}
	if (point == null) {
	    throw new GeolocSearchException("can not determine Point");
	}
	// radius
	try {
	    withRadius(GeolocHelper.parseInternationalDouble(req
		    .getParameter(GeolocServlet.RADIUS_PARAMETER)));
	} catch (Exception e) {
	    this.radius = DEFAULT_RADIUS;
	}

	// pagination
	Pagination pagination = null;
	int from;
	int to;
	try {
	    from = Integer.valueOf(
		    req.getParameter(GisgraphyServlet.FROM_PARAMETER)).intValue();
	} catch (Exception e) {
	    from = Pagination.DEFAULT_FROM;
	}

	try {
	    to = Integer.valueOf(req.getParameter(GisgraphyServlet.TO_PARAMETER))
		    .intValue();
	} catch (NumberFormatException e) {
	    to = -1;
	}

	pagination = Pagination.paginateWithMaxResults(this.getMaxLimitResult()).from(from).to(to)
		.limitNumberOfResults(this.getMaxLimitResult());
	// output format
	OutputFormat format = OutputFormat.getFromString(req
		.getParameter(GisgraphyServlet.FORMAT_PARAMETER));
	format = OutputFormatHelper.getDefaultForServiceIfNotSupported(format, GisgraphyServiceType.GEOLOC);
	Output output = Output.withFormat(format);

	// indent
	if ("true".equalsIgnoreCase(req
		.getParameter(GisgraphyServlet.INDENT_PARAMETER))
		|| "on".equalsIgnoreCase(req
			.getParameter(GisgraphyServlet.INDENT_PARAMETER))) {
	    output.withIndentation();
	}

	//placetype
	Class<? extends GisFeature> clazz = GeolocHelper
		.getClassEntityFromString(req
			.getParameter(GeolocServlet.PLACETYPE_PARAMETER));

	//distance field
	if ("false".equalsIgnoreCase(req
		.getParameter(GeolocServlet.DISTANCE_PARAMETER))
		|| "off".equalsIgnoreCase(req
			.getParameter(GeolocServlet.DISTANCE_PARAMETER))) {
	    withDistanceField(false);
	}
	
	if (req.getParameter(GeolocServlet.CALLBACK_PARAMETER)!=null){
	    withCallback(req.getParameter(GeolocServlet.CALLBACK_PARAMETER));
	}
	
	this.pagination = pagination;
	this.placeType = clazz == null ? GisgraphyConfig.defaultGeolocSearchPlaceTypeClass
		: clazz;
	this.output = output;
    }

    /**
     * @param point
     *                the point to query around
     * @param radius
     *                The radius (distance) in meters
     * @param pagination
     *                The pagination specification, if null : use default
     * @param output
     *                {@link Output} The output specification , if null : use
     *                default
     * @param placeType
     *                the type of gis to search , if null : search for all place
     *                type.
     * @throws An
     *                 {@link IllegalArgumentException} if the point is null
     */
    public GeolocQuery(Point point, double radius, Pagination pagination,
	    Output output, Class<? extends GisFeature> placeType) {
	super(pagination, output);
	Assert.notNull(point, "point must not be null");
	this.point = point;
	withRadius(radius);
	withPlaceType(placeType);
    }

    /**
     * Constructor with default {@linkplain Pagination}, {@linkplain Output},
     * and placetype (see
     * {@link GisgraphyConfig#defaultGeolocSearchPlaceTypeClass}
     * 
     * @param point
     *                The point from which we want to find GIS Object
     * @param radius
     *                The radius (distance) in meters
     */
    public GeolocQuery(Point point, double radius) {
	this(point, radius, null, null,
		GisgraphyConfig.defaultGeolocSearchPlaceTypeClass);
    }

    /**
     * @return The latitude (north-south) .
     * @see #getLongitude()
     */
    @Transient
    public Double getLatitude() {
	Double latitude = null;
	if (this.point != null) {
	    latitude = this.point.getY();
	}
	return latitude;
    }

    /**
     * @return Returns the longitude (east-West).
     * @see #getLongitude()
     */
    @Transient
    public Double getLongitude() {
	Double longitude = null;
	if (this.point != null) {
	    longitude = this.point.getX();
	}
	return longitude;
    }

    /**
     * Constructor with default {@linkplain Pagination}, {@linkplain Output},
     * radius and placetype
     * 
     * @param point
     *                The point from which we want to find GIS Object
     * @see #DEFAULT_RADIUS
     */
    public GeolocQuery(Point point) {
	this(point, GeolocQuery.DEFAULT_RADIUS, null, null,
		GisgraphyConfig.defaultGeolocSearchPlaceTypeClass);
    }

    /**
     * @return The point from which we want to find GIS Object
     */
    public Point getPoint() {
	return this.point;
    }

    /**
     * @param radius
     *                The radius to set. Limit the query to the specified
     *                radius, if the radius is <=0 , it will be set to the
     *                default radius.
     */
    private GeolocQuery withRadius(double radius) {
	if (radius <= 0) {
	    this.radius = DEFAULT_RADIUS;
	} else {
	    this.radius = radius;
	}
	return this;
    }

    /**
     * @return The radius
     */
    public double getRadius() {
	return this.radius;
    }

    /**
     * @return Wether the output will be indented
     * @see Output#isIndented()
     */
    public boolean isOutputIndented() {
	return output.isIndented();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String asString = "GeolocQuery (lat='"
		+ point.getY() + "',long='" + point.getX() + "') and radius="
		+ this.radius + " for ";
	if (this.placeType == null) {
	    asString += "all placeType";
	} else {
	    asString += this.placeType.getSimpleName();
	}
	asString += " with " + getOutput() + " and " + pagination +" and distance = "+distanceField;
	return asString;
    }

    /**
     * @return the placeType : it limits the search to an object of a specific
     *         class
     */
    public Class<? extends GisFeature> getPlaceType() {
	return this.placeType;
    }

    /**
     * @param placeType
     *                The placeType to set, if null, search for all placetype
     * @return The current query to chain methods
     */
    public GeolocQuery withPlaceType(Class<? extends GisFeature> placeType) {
	this.placeType = placeType;
	return this;
    }

    @Override
    public int getMaxLimitResult() {
	return GeolocServlet.DEFAULT_MAX_RESULTS;
    }

    /**
     * @return true if the distance should be calculate or not
     */
    public boolean hasDistanceField() {
        return distanceField;
    }

    /**
     * @param distanceField If the distance should be include or not
     * @return Whether the distance field should be output 
     */
    public GeolocQuery withDistanceField(boolean distanceField) {
        this.distanceField = distanceField;
        return this;
    }
    
    

}
