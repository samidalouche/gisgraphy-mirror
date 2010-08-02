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
import com.gisgraphy.helper.GeolocHelper;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * An implementation of the <code>Criterion</code> interface that implements
 * distance restriction
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class IntersectsRestriction implements Criterion {

    /**
     * Point we have to calculate the distance from
     */
    private Point point = null;

    /**
     * The distance restriction
     */
    private double distance;

    private Polygon polygon;

    private String columnName;

   

    private static final long serialVersionUID = 1L;

    /**
     * @param point
     *                Point we have to calculate the distance from
     * @param distance
     *                The distance restriction
     * @param useIndex
     *                Wether we must use index or not
     */
    public IntersectsRestriction(String columnName, Polygon polygon) {
	this.columnName = columnName;
	this.polygon = polygon;
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
		GisFeature.LOCATION_COLUMN_NAME, polygon) };

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
		this.columnName);
	StringBuffer result = new StringBuffer(columnName).append(
		" && ").append(" ? ");
	return result.toString();
	    }

}
