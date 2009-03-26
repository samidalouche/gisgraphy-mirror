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

import javax.persistence.PersistenceException;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.entity.Street;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.IntrospectionHelper;
import com.gisgraphy.hibernate.criterion.DistanceRestriction;
import com.gisgraphy.hibernate.criterion.ProjectionOrder;
import com.gisgraphy.hibernate.criterion.ResultTransformerUtil;
import com.gisgraphy.hibernate.projection.ProjectionBean;
import com.gisgraphy.hibernate.projection.SpatialProjection;
import com.vividsolutions.jts.geom.Point;

/**
 * A data access object for {@link Street} Object
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Repository
public class OpenStreetMapDao extends GenericDao<OpenStreetMap, Long> implements IOpenStreetMapDao
{

    /**
     * Default constructor
     */
    public OpenStreetMapDao() {
	//super(StreetOSM.class);
	super(OpenStreetMap.class);
    }
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#getNearestAndDistanceFrom(com.vividsolutions.jts.geom.Point, double, int, int, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<StreetDistance> getNearestAndDistanceFrom(
	    final Point point, final double distance,
	    final int firstResult, final int maxResults,
	    final String streetType,final String namePrefix) {
	
	Assert.notNull(point);
	return (List<StreetDistance>) this.getHibernateTemplate().execute(
		new HibernateCallback() {

		    public Object doInHibernate(Session session)
			    throws PersistenceException {
			Criteria criteria = session
				.createCriteria(OpenStreetMap.class);
			if (maxResults > 0) {
			    criteria = criteria.setMaxResults(maxResults);
			}
			if (firstResult >= 1) {
			    criteria = criteria.setFirstResult(firstResult - 1);
			}
			if (streetType != null) {
			    criteria = criteria.add(Restrictions.eq("streetType", streetType));
			}
			if (namePrefix != null) {
			    criteria = criteria.add(Restrictions.like("name", namePrefix));
			}
			String[] wktLineStrings={"LINESTRING (0 0, 10 10, 20 20)","LINESTRING (30 30, 40 40, 50 50)"};
			criteria = criteria.add(org.hibernatespatial.criterion.SpatialRestrictions.
				intersects(OpenStreetMap.SHAPE_COLUMN_NAME, null, 
					GeolocHelper.createMultiLineString(wktLineStrings)));
			List<String> fieldList = IntrospectionHelper
				.getFieldsAsList(OpenStreetMap.class);

			Projection projections = ProjectionBean.fieldList(
				fieldList).add(
				SpatialProjection.distance_sphere(point).as(
					"distance"));
			criteria.setProjection(projections);
			criteria.addOrder(new ProjectionOrder("distance"));
			criteria.setCacheable(true);
			// List<Object[]> queryResults =testCriteria.list();
			List<?> queryResults = criteria.list();

			List<GisFeatureDistance> results = ResultTransformerUtil
				.transformToGisFeatureDistance(
					(String[]) ArrayUtils
						.add(
							IntrospectionHelper
								.getFieldsAsArray(OpenStreetMap.class),
							"distance"),
					queryResults);
			return results;
		    }
		});
    }

}
