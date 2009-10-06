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

package com.gisgraphy.hibernate.criterion;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.TypedValue;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Point;

/**
 * An implementation of the <code>Criterion</code> interface that implements
 * distance restriction
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class DistanceRestriction implements Criterion {

    /**
     * Point we have to calculate the distance from
     */
    private Point point = null;

    private boolean useIndex = true;

    /**
     * The distance restriction
     */
    private double distance;

    private static final String INTERSECTION = "&&";
    private static final String BBOX = "BOX3D";

    private static final long serialVersionUID = 1L;

    /**
     * @param point
     *                Point we have to calculate the distance from
     * @param distance
     *                The distance restriction
     * @param useIndex
     *                Wethe we must use index
     */
    public DistanceRestriction(Point point, double distance, boolean useIndex) {
	this.point = point;
	this.distance = distance;
	this.useIndex = useIndex;
    }

    /**
     * @param point
     *                Point we have to calculate the distance from
     * @param distance
     *                The distance restriction by default use index
     */
    public DistanceRestriction(Point point, double distance) {
	this.point = point;
	this.distance = distance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.criterion.Criterion#getTypedValues(org.hibernate.Criteria,
     *      org.hibernate.criterion.CriteriaQuery)
     */
    public TypedValue[] getTypedValues(Criteria criteria,
	    CriteriaQuery criteriaQuery) throws HibernateException {
	return new TypedValue[] { criteriaQuery.getTypedValue(criteria,
		GisFeature.LOCATION_COLUMN_NAME, point) };

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hibernate.criterion.Criterion#toSqlString(org.hibernate.Criteria,
     *      org.hibernate.criterion.CriteriaQuery)
     */
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	    throws HibernateException {
	String columnName = criteriaQuery.getColumn(criteria,
		GisFeature.LOCATION_COLUMN_NAME);
	StringBuffer result = new StringBuffer("( distance_sphere(").append(
		columnName).append(", ?) <=").append(this.distance).append(")");
	return useIndex ? result.append(" AND ").append(
		getBoundingBox(criteriaQuery.getSQLAlias(criteria), this.point
			.getY(), this.point.getX(), distance)).toString()
		: result.toString();

    }

    /**
     * @param alias the
     *                sql alias
     * @param lat
     *                the latitude
     * @param lon
     *                the longitude
     * @param distance
     *                the boundingbox distance
     * @return a sql String that represents the bounding box
     */
    private String getBoundingBox(String alias, double lat, double lon,
	    double distance) {

	double deltaXInDegrees = Math.abs(Math.toDegrees(Math.asin(Math
		.sin(distance / Constants.RADIUS_OF_EARTH_IN_METERS)
		/ Math.cos(lat))));
	double deltaYInDegrees = Math.abs(Math.toDegrees(distance
		/ Constants.RADIUS_OF_EARTH_IN_METERS));

	double minX = lon - deltaXInDegrees;
	double maxX = lon + deltaXInDegrees;
	double minY = lat - deltaYInDegrees;
	double maxY = lat + deltaYInDegrees;

	StringBuffer sb = new StringBuffer();
	// {alias}.location && setSRID(BOX3D(...), 4326)
	sb.append(alias);
	sb.append(".").append(GisFeature.LOCATION_COLUMN_NAME);
	sb.append(" ");
	sb.append(INTERSECTION);
	sb.append(" setSRID(");

	// Construct the BBOX : 'BOX3D(-119.2705528794688
	// 33.15289952334886,-117.2150071205312 34.95154047665114)'::box3d
	sb.append("cast (");
	sb.append("'");
	sb.append(BBOX);
	sb.append("(");
	sb.append(minX); // minX
	sb.append(" ");
	sb.append(minY); // minY
	sb.append(",");
	sb.append(maxX); // maxX
	sb.append(" ");
	sb.append(maxY); // maxY
	sb.append(")'as box3d)"); // cannot use the ::box3d notation, since
	// nativeSQL interprets :param as a named
	// parameter

	// end of the BBOX, finish the setSRID
	sb.append(", ");
	sb.append(SRID.WGS84_SRID.getSRID());
	sb.append(") ");

	return sb.toString();

    }

}
