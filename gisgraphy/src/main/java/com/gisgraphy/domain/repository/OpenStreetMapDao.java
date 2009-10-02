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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.entity.Street;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.IntrospectionHelper;
import com.gisgraphy.hibernate.criterion.ProjectionOrder;
import com.gisgraphy.hibernate.criterion.ResultTransformerUtil;
import com.gisgraphy.hibernate.projection.ProjectionBean;
import com.gisgraphy.hibernate.projection.SpatialProjection;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

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
	    final StreetType streetType, final Boolean oneWay ,final String name) {
	
	Assert.notNull(point);
	return (List<StreetDistance>) this.getHibernateTemplate().execute(
		new HibernateCallback() {

		    public Object doInHibernate(Session session)
			    throws PersistenceException {
			Criteria criteria = session
				.createCriteria(OpenStreetMap.class);
			
			List<String> fieldList = IntrospectionHelper
				.getFieldsAsList(OpenStreetMap.class);

			Projection projections = ProjectionBean.fieldList(
				fieldList,false).add(
				SpatialProjection.distance_sphere(point).as(
					"distance"));
			criteria.setProjection(projections);
			criteria.addOrder(new ProjectionOrder("distance"));
			if (maxResults > 0) {
			    criteria = criteria.setMaxResults(maxResults);
			}
			if (firstResult >= 1) {
			    criteria = criteria.setFirstResult(firstResult - 1);
			}
			
			Polygon polygonBox = GeolocHelper.createPolygonBox(point.getX(), point.getY(), distance);
			criteria = criteria.add(SpatialRestrictions.
				intersects(OpenStreetMap.SHAPE_COLUMN_NAME, polygonBox, 
					polygonBox));
			if (name != null) {
			    criteria = criteria.add(Restrictions.ilike("name", "%"+name+"%"));
			}
			if (streetType != null) {
			    criteria = criteria.add(Restrictions.eq("streetType",streetType));
			}
			if (oneWay != null) {
			    criteria = criteria.add(Restrictions.eq("oneWay",oneWay));
			}
			criteria.setCacheable(true);
			// List<Object[]> queryResults =testCriteria.list();
			List<?> queryResults = criteria.list();
			
			if (queryResults != null && queryResults.size()!=0){
			List<StreetDistance> results = ResultTransformerUtil
				.transformToStreetDistance(
					(String[]) ArrayUtils
						.add(
							IntrospectionHelper
								.getFieldsAsArray(OpenStreetMap.class),
							"distance"),
					queryResults);
			return results;
			} else {
			    return new ArrayList<StreetDistance>();
			}
			
		    }
		});
    }
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#getByGid(java.lang.Long)
     */
    public OpenStreetMap getByGid(final Long gid) {
	Assert.notNull(gid);
	return (OpenStreetMap) this.getHibernateTemplate().execute(
		new HibernateCallback() {

		    public Object doInHibernate(Session session)
			    throws PersistenceException {
			String queryString = "from "
				+ OpenStreetMap.class.getSimpleName()
				+ " as c where c.gid= ?";

			Query qry = session.createQuery(queryString);
			qry.setCacheable(true);

			qry.setParameter(0, gid);

			OpenStreetMap result = (OpenStreetMap) qry.uniqueResult();
			return result;
		    }
		});
    }

    
    /** 
     * this method save the entity and update the fulltext field 
     */
    @Override
    public OpenStreetMap save(final OpenStreetMap o) {
	return (OpenStreetMap) this.getHibernateTemplate().execute(
			new HibernateCallback() {

			    public Object doInHibernate(Session session)
				    throws PersistenceException {
				session.saveOrUpdate(o);
				String updateField = "UPDATE openStreetMap SET "+OpenStreetMap.FULLTEXT_COLUMN_NAME+" = to_tsvector(coalesce(name,'')) where id="+o.getId();  
				Query qryUpdateField = session.createSQLQuery(updateField);
				qryUpdateField.executeUpdate();
				return o;
				
			    }
			});
    }
    
    


    public void buildFulltextIndex() {
	 this.getHibernateTemplate().execute(
		new HibernateCallback() {

		    public Object doInHibernate(Session session)
			    throws PersistenceException {
			String createColumn = "ALTER TABLE openStreetMap ADD COLUMN search_idx_openstreetmap_name tsvector;  ";
			String updateField = "UPDATE openStreetMap SET search_idx_openstreetmap_name = to_tsvector(coalesce(name,''))";  

			Query qryCreateColumn = session.createSQLQuery(createColumn);
			qryCreateColumn.executeUpdate();
			
			Query qryUpdateField = session.createSQLQuery(updateField);
			qryUpdateField.executeUpdate();
			return 1;
			
		    }
		});
    
	
    }
    
    

}
