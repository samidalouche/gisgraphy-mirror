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
/**
 * This work was partially supported by the European Commission, under the 6th
 * Framework Programme, contract IST-2-004688-STP. This library is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.gisgraphy.hibernate.projection;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.SimpleProjection;
import org.hibernate.type.Type;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a> Some
 *         Spatial Projections. distance_sphere...
 */
public class SpatialProjection {

    /**
     * projection to get the distance_sphere of a point
     * @param point the point to get the distance
     * @return the projection
     */
    public static SimpleProjection distance_sphere(final Point point) {
	return new SimpleProjection() {

	    /**
	     * 
	     */
	    private static final long serialVersionUID = -8771843067497785957L;

	    /* (non-Javadoc)
	     * @see org.hibernate.criterion.Projection#getTypes(org.hibernate.Criteria, org.hibernate.criterion.CriteriaQuery)
	     */
	    public Type[] getTypes(Criteria criteria,
		    CriteriaQuery criteriaQuery) throws HibernateException {
		return new Type[] { Hibernate.DOUBLE};
	    }

	    /* (non-Javadoc)
	     * @see org.hibernate.criterion.Projection#toSqlString(org.hibernate.Criteria, int, org.hibernate.criterion.CriteriaQuery)
	     */
	    public String toSqlString(Criteria criteria, int position,
		    CriteriaQuery criteriaQuery) throws HibernateException {
		String columnName = criteriaQuery.getColumn(criteria, GisFeature.LOCATION_COLUMN_NAME);
		StringBuffer sb = new StringBuffer();
		String sqlString =  sb.append("distance_sphere(").append(columnName).append(", GeometryFromText( 'POINT(").append(point.getX()).append(" ").append(point.getY())
		.append(")',").append(SRID.WGS84_SRID.getSRID()).append(")) as y").append(position).append("_").toString();
		return sqlString;
	    }

	};

    }
    
    
    

}
