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
package com.gisgraphy.hibernate.projection;

import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.test.GeolocTestHelper;
import com.gisgraphy.test._DaoHelper;

public class SpatialProjectionTest extends AbstractIntegrationHttpSolrTestCase {

    private ICityDao cityDao;

    private _DaoHelper testDao;

    @SuppressWarnings("unchecked")
    @Test
    public void testDistance_sphere() {
	final City p1 = GeolocTestHelper.createCity("paris", 48.86667F,
		2.3333F, 1L);
	City p2 = GeolocTestHelper.createCity("bordeaux", 44.83333F, -0.56667F,
		3L);

	this.cityDao.save(p1);
	this.cityDao.save(p2);

	HibernateCallback hibernateCallback = new HibernateCallback() {

	    @SuppressWarnings("unchecked")
	    public Object doInHibernate(Session session)
		    throws PersistenceException {

		Criteria testCriteria = session.createCriteria(City.class);
		ProjectionList projection = Projections.projectionList()
		.add(Projections.property("name").as("name"))
			.add(
				SpatialProjection.distance_sphere(
					p1.getLocation()).as("distance"));
		//remove the from point
		testCriteria.add(Restrictions.ne("id", p1.getId()))
			.setProjection(projection);
		testCriteria.setResultTransformer(Transformers
			.aliasToBean(_CityDTO.class));

		List<_CityDTO> results = testCriteria.list();
		return results;
	    }
	};

	List<_CityDTO> cities = (List<_CityDTO>) testDao
		.testCallback(hibernateCallback);
	assertEquals(1, cities.size());
	assertEquals("bordeaux", cities.get(0).getName());
	Double calculatedDist = p1.distance(p2.getLocation());
	Double retrieveDistance = cities.get(0).getDistance();
	double percent = (Math.abs(calculatedDist - retrieveDistance) * 100)
		/ Math.min(retrieveDistance, calculatedDist);
	assertTrue(percent < 1);

    }

    @Required
    public void setCityDao(ICityDao cityDao) {
	this.cityDao = cityDao;
    }

    @Required
    public void setTestDao(_DaoHelper testDao) {
	this.testDao = testDao;
    }

}
