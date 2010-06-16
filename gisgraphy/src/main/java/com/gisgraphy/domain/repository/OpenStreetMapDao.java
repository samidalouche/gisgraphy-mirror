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
import org.apache.commons.lang.NotImplementedException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.StreetSearchMode;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.valueobject.GisgraphyConfig;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.IntrospectionHelper;
import com.gisgraphy.hibernate.criterion.FulltextRestriction;
import com.gisgraphy.hibernate.criterion.ProjectionOrder;
import com.gisgraphy.hibernate.criterion.ResultTransformerUtil;
import com.gisgraphy.hibernate.projection.ProjectionBean;
import com.gisgraphy.hibernate.projection.SpatialProjection;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A data access object for {@link OpenStreetMap} Object
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Repository
public class OpenStreetMapDao extends GenericDao<OpenStreetMap, Long> implements IOpenStreetMapDao
{
	@Autowired
	IDatabaseHelper databaseHelper;
	
	/**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(OpenStreetMapDao.class);
	
    /**
     * Default constructor
     */
    public OpenStreetMapDao() {
	    super(OpenStreetMap.class);
    }
    

    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#getNearestAndDistanceFrom(com.vividsolutions.jts.geom.Point, double, int, int, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<StreetDistance> getNearestAndDistanceFrom(
	    final Point point, final double distance,
	    final int firstResult, final int maxResults,
	    final StreetType streetType, final Boolean oneWay ,final String name, final StreetSearchMode streetSearchMode) {
	
	Assert.notNull(point);
	if (name != null && streetSearchMode==null){
		throw new IllegalArgumentException("streetSearchmode can not be null if name is provided");
	}
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
//				SpatialProjection.distance_sphere(point, GisFeature.LOCATION_COLUMN_NAME).as(
//					"distance"));
						SpatialProjection.distance_pointToLine(point, OpenStreetMap.SHAPE_COLUMN_NAME).as(
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
					if (streetSearchMode==StreetSearchMode.CONTAINS){
					    	criteria = criteria.add(Restrictions.isNotNull("name"));//optimisation!
					    	criteria = criteria.add(Restrictions.ilike(OpenStreetMap.FULLTEXTSEARCH_PROPERTY_NAME, "%"+name+"%"));
					    	//criteria = criteria.add(new PartialWordSearchRestriction(OpenStreetMap.PARTIALSEARCH_VECTOR_COLUMN_NAME, name));
					} else if (streetSearchMode == StreetSearchMode.FULLTEXT){
						  criteria = criteria.add(new FulltextRestriction(OpenStreetMap.FULLTEXTSEARCH_VECTOR_PROPERTY_NAME, name));
					} else {
						throw new NotImplementedException(streetSearchMode+" is not implemented for street search");
					}
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

    
  
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#buildIndexForStreetNameSearch()
     */
    public Integer updateTS_vectorColumnForStreetNameSearch() {
	return (Integer) this.getHibernateTemplate().execute(
			 new HibernateCallback() {

			    public Object doInHibernate(Session session)
				    throws PersistenceException {
				session.flush();
				logger.info("will update "+OpenStreetMap.FULLTEXTSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" field");
				String updateFulltextField = "UPDATE openStreetMap SET "+OpenStreetMap.FULLTEXTSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" = to_tsvector('simple',coalesce("+OpenStreetMap.FULLTEXTSEARCH_COLUMN_NAME+",'')) where name is not null";  
				Query qryUpdateFulltextField = session.createSQLQuery(updateFulltextField);
				int numberOfLineUpdatedForFulltext = qryUpdateFulltextField.executeUpdate();
				int numberOfLineUpdatedForPartial = 0;
				if (GisgraphyConfig.PARTIAL_SEARH_EXPERIMENTAL){
					logger.info("will update "+OpenStreetMap.PARTIALSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" field");
					String updatePartialWordField = "UPDATE openStreetMap SET "+OpenStreetMap.PARTIALSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" = to_tsvector('simple',coalesce("+OpenStreetMap.PARTIALSEARCH_COLUMN_NAME+" ,'')) where name is not null";
					Query qryUpdateParialWordField = session.createSQLQuery(updatePartialWordField);
					numberOfLineUpdatedForPartial = qryUpdateParialWordField.executeUpdate();
					session.flush();
				}
				return Integer.valueOf(numberOfLineUpdatedForFulltext + numberOfLineUpdatedForPartial);
				
			    }
			});
    }
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#buildIndexForStreetNameSearch()
     */
    public Integer updateTS_vectorColumnForStreetNameSearchPaginate(final int from,final int to ) {
	return (Integer) this.getHibernateTemplate().execute(
			 new HibernateCallback() {

			    public Object doInHibernate(Session session)
				    throws PersistenceException {
				session.flush();
				logger.info("will update "+OpenStreetMap.FULLTEXTSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" field");
				String updateFulltextField = "UPDATE openStreetMap SET "+OpenStreetMap.FULLTEXTSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" = to_tsvector('simple',coalesce("+OpenStreetMap.FULLTEXTSEARCH_COLUMN_NAME+",'')) where gid >= "+from+" and gid <= "+to+" and name is not null";  
				Query qryUpdateFulltextField = session.createSQLQuery(updateFulltextField);
				int numberOfLineUpdatedForFulltext = qryUpdateFulltextField.executeUpdate();
				int numberOfLineUpdatedForPartial = 0;
				if (GisgraphyConfig.PARTIAL_SEARH_EXPERIMENTAL){
					logger.info("will update "+OpenStreetMap.PARTIALSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" field");
					String updatePartialWordField = "UPDATE openStreetMap SET "+OpenStreetMap.PARTIALSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+" = to_tsvector('simple',coalesce("+OpenStreetMap.PARTIALSEARCH_COLUMN_NAME+" ,'')) where gid >= "+from+" and gid <= "+to+" and name is not null";
					Query qryUpdateParialWordField = session.createSQLQuery(updatePartialWordField);
					numberOfLineUpdatedForPartial = qryUpdateParialWordField.executeUpdate();
					session.flush();
				}
				return Integer.valueOf(numberOfLineUpdatedForFulltext + numberOfLineUpdatedForPartial);
				
			    }
			});
    }


    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#createGISTIndex()
     */
    public void createSpatialIndexes() {
	 this.getHibernateTemplate().execute(
			 new HibernateCallback() {

			    public Object doInHibernate(Session session)
				    throws PersistenceException {
				session.flush();
				logger.info("will create GIST index for  the "+OpenStreetMap.SHAPE_COLUMN_NAME+" column");
				String createIndexForShape = "CREATE INDEX "+OpenStreetMap.SHAPE_COLUMN_NAME.toLowerCase()+"indexopenstreetmap ON openstreetmap USING GIST ("+OpenStreetMap.SHAPE_COLUMN_NAME.toLowerCase()+")";  
				Query qryUpdateFulltextField = session.createSQLQuery(createIndexForShape);
				qryUpdateFulltextField.executeUpdate();
				return null;
			    }
			});
   }
    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#createGISTIndex()
     */
    public void createFulltextIndexes() {
	 this.getHibernateTemplate().execute(
			 new HibernateCallback() {

			    public Object doInHibernate(Session session)
				    throws PersistenceException {
				session.flush();
				logger.info("will create Fulltext index");
				String createFulltextIndex = "CREATE INDEX "+OpenStreetMap.FULLTEXTSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+"indexopenstreetmap ON openstreetmap USING gin("+OpenStreetMap.FULLTEXTSEARCH_VECTOR_PROPERTY_NAME.toLowerCase()+")";  
				Query fulltextIndexQuery = session.createSQLQuery(createFulltextIndex);
				fulltextIndexQuery.executeUpdate();
				
				return null;
			    }
			});
   }
    
    public void clearTextSearchName() {
	 this.getHibernateTemplate().execute(
			 new HibernateCallback() {

			    public Object doInHibernate(Session session)
				    throws PersistenceException {
				session.flush();
				logger.info("will clear textSearchName");
				String clearTextSearchNameQueryString = "Update openstreetmap set  "+OpenStreetMap.FULLTEXTSEARCH_PROPERTY_NAME.toLowerCase()+"= null";  
				Query fulltextIndexQuery = session.createSQLQuery(clearTextSearchNameQueryString);
				fulltextIndexQuery.executeUpdate();
				
				return null;
			    }
			});
  }

    
    /* (non-Javadoc)
     * @see com.gisgraphy.domain.repository.IOpenStreetMapDao#countEstimate()
     */
    public long countEstimate(){
	return (Long) this.getHibernateTemplate().execute(
		new HibernateCallback() {

		    public Object doInHibernate(Session session)
			    throws PersistenceException {
			String queryString = "select max(gid) from "
				+ persistentClass.getSimpleName();

			Query qry = session.createQuery(queryString);
			qry.setCacheable(true);
			Long count = (Long) qry.uniqueResult();
			return count;
		    }
		});
    }
}
