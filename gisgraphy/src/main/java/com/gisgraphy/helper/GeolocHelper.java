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
package com.gisgraphy.helper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.operation.TransformException;
import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.FeatureCode;
import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Provides useful methods for geolocalisation
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeolocHelper {

    /**
     * Create a JTS point from the specified longitude and latitude for the SRID
     * (aka : Spatial Reference IDentifier) 4326 (WGS84)<br>
     * 
     * @see <a href="http://en.wikipedia.org/wiki/SRID">SRID</a>
     * @see SRID
     * @param longitude
     *                The longitude for the point
     * @param latitude
     *                The latitude for the point
     * @return A jts point from the specified longitude and latitude
     * @throws IllegalArgumentException
     *                 if latitude is not between -90 and 90, or longitude is
     *                 not between -180 and 180
     */
    public static Point createPoint(Float longitude, Float latitude) {
	if (longitude < -180 || longitude > 180) {
	    throw new IllegalArgumentException(
		    "Longitude should be between -180 and 180");
	}
	if (latitude < -90 || latitude > 90) {
	    throw new IllegalArgumentException(
		    "latitude should be between -90 and 90");
	}
	GeometryFactory factory = new GeometryFactory(new PrecisionModel(
		PrecisionModel.FLOATING), SRID.WGS84_SRID.getSRID());
	Point point = (Point) factory
		.createPoint(new Coordinate(longitude, latitude));
	return point;
    }
    
    
    /**
     * Create a JTS MultiLineString from the specified array of linestring for the SRID
     * (aka : Spatial Reference IDentifier) 4326 (WGS84)<br>
     * 
     * example : {"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40 40, 50 50)"}
     * 
     * @see <a href="http://en.wikipedia.org/wiki/SRID">SRID</a>
     * @see SRID
     * @param wktLineStrings
     *                The longitude for the point
     * @return A MultilineStringObject from the specified array of linestring
     * @throws IllegalArgumentException
     *                 if the string are not correct
     */
    public static MultiLineString createMultiLineString(String[] wktLineStrings) {
	LineString[] lineStrings = new LineString[wktLineStrings.length];
	for (int i = 0;i< wktLineStrings.length;i++){
	 LineString ls;
	try {
	    ls = (LineString) new WKTReader().read(wktLineStrings[i]);
	} catch (com.vividsolutions.jts.io.ParseException pe) {
	   throw new IllegalArgumentException(wktLineStrings[i]+" is not valid "+pe);
	}
	catch (ClassCastException cce) {
		   throw new IllegalArgumentException(wktLineStrings[i]+" is not a LINESTRING");
	}
	 lineStrings[i] = ls;
	}
	
	GeometryFactory factory = new GeometryFactory(new PrecisionModel(
		PrecisionModel.FLOATING), SRID.WGS84_SRID.getSRID());
	MultiLineString multiLineString = (MultiLineString) factory
		.createMultiLineString(lineStrings);
	return multiLineString;
    }

    /**
     * Calculate the distance between the specified point.
     * 
     * @param point1
     *                The first JTS point
     * @param point2
     *                The second JTS point
     * @return The calculated distance
     */
    // TODO v2 unit helper
    public static double distance(Point point1, Point point2) {
	Assert.isTrue(point1 != null && point2 != null,
		"Can not calculate distance for null point");

	// Unit<Length> targetUnit = (unit != null) ? unit : SI.METER;
	try {
	    double distance = JTS.orthodromicDistance(point1.getCoordinate(),
		    point2.getCoordinate(), DefaultGeographicCRS.WGS84);
	    return distance;
	    // return SI.METER.getConverterTo(targetUnit).convert(distance);
	} catch (TransformException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Return the class corresponding to the specified String or null if not
     * found. The Class will be searched in the 'entity' package. The search is
     * not case sensitive. This method is mainly used to determine an entity
     * Class from a web parameter
     * 
     * @param classNameWithoutPackage
     *                the simple name of the Class we want to retrieve
     * @return The class corresponding to the specified String or null if not
     *         found.
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends GisFeature> getClassEntityFromString(
	    String classNameWithoutPackage) {
	if (classNameWithoutPackage != null) {
	    return FeatureCode.entityClass.get(classNameWithoutPackage
		    .toLowerCase());
	}
	return null;
    }

    /**
     * parse a string and return the corresponding double value, it accepts
     * comma or point as decimal separator
     * 
     * @param number
     *                the number with a point or a comma as decimal separator
     * @return the float value corresponding to the parsed string
     * @throws ParseException
     *                 in case of errors
     */
    public static float parseInternationalDouble(String number)
	    throws ParseException {
	NumberFormat nffrench = NumberFormat.getInstance(Locale.FRENCH);
	NumberFormat nfus = NumberFormat.getInstance(Locale.US);

	Number numberToReturn = number.indexOf(',') != -1 ? nffrench
		.parse(number) : nfus.parse(number);
	return numberToReturn.floatValue();
    }
    
    /**
     * @param lat the central latitude for the Polygon
     * @param lng the central longitude for the polygon
     * @param distance the distance in meters from the point to create the polygon
     * @return a polygon / square with a side of distance * 2
     * throws {@link RuntimeException} if an error occured 
     * thros {@link IllegalArgumentException} if lat, long or distance is not correct
     */
    public static Polygon createPolygonBox(Float lng, Float lat,Float distance){
	Assert.notNull(lat,"lat should not be null");
	Assert.notNull(lng,"long should not be null");
	Assert.notNull(distance, "distance should not be null");
	
	if (distance <=0){
	    throw new IllegalArgumentException("distance is incorect : "+distance);
	}
	

	double latrad = ((lat * Math.PI) / 180);
	double lngrad = ((lng * Math.PI) / 180);
	double angulardistance = distance/Constants.RADIUS_OF_EARTH_IN_METERS;
	double deltaYLatInDegrees = Math.abs(Math.asin( Math.sin(latrad)*Math.cos(angulardistance) + 
                 Math.cos(latrad)*Math.sin(angulardistance)*Math.cos(0) ));
	
	double deltaXlngInDegrees = lngrad + Math.atan2(Math.sin(0) * Math.sin(angulardistance) * Math.cos(latrad), 
                Math.cos(angulardistance)-Math.sin(latrad)*Math.sin(latrad));

	lngrad = (lngrad+Math.PI)%(2*Math.PI) - Math.PI;
	
	double latdeg = ((deltaYLatInDegrees * 180 ) /Math.PI);
	double slngdeg = ((deltaXlngInDegrees * 180 ) /Math.PI);

	//System.err.println("deltaY"+deltaYLatInDegrees);
	//System.err.println("deltaX"+deltaXlngInDegrees);
	double minX = lng - slngdeg;
	double maxX = lng + slngdeg;
	double minY = lat - latdeg;
	double maxY = lat + latdeg;
	Point point1 = createPoint(Double.valueOf(lng).floatValue(), Double.valueOf(lat).floatValue());
	Point point2 = createPoint(Double.valueOf(slngdeg).floatValue(), Double.valueOf(latdeg).floatValue());
	//System.err.println("point1="+point1);
	//System.err.println("point2="+point2);
	System.err.println("distance="+distance(point1, point2));
	
	
	
	WKTReader reader = new WKTReader();
	StringBuffer sb = new StringBuffer("POLYGON((");
	String polygonString = sb.append(minX).append(" ").append(minY).append(",")
	.append(maxX).append(" ").append(minY).append(",")
	.append(maxX).append(" ").append(maxY).append(",")
	.append(minX).append(" ").append(maxY).append(",")
	.append(minX).append(" ").append(minY).append("))").toString();
	
	
	try {
	    Polygon polygon = (Polygon) reader.read(polygonString);
	    polygon.setSRID(SRID.WGS84_SRID.getSRID());
	    return polygon;
	} catch (com.vividsolutions.jts.io.ParseException e) {
	    throw new RuntimeException("can not create Polygon for lat="+lat+" long="+lng+" and distance="+distance +" : "+e);
	}
    }
}
