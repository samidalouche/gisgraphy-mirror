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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.repository.ICityDao;
import com.gisgraphy.test.GeolocTestHelper;
import com.gisgraphy.test._DaoHelper;

public class ProjectionBeanTest extends AbstractIntegrationHttpSolrTestCase {

    private _DaoHelper testDao;

    private ICityDao cityDao;

    @SuppressWarnings("unchecked")
    public void testPropertiesList() {

	City p1 = GeolocTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);

	this.cityDao.save(p1);
	HibernateCallback hibernateCallback = new HibernateCallback() {

	    @SuppressWarnings("unchecked")
	    public Object doInHibernate(Session session)
		    throws PersistenceException {

		Criteria testCriteria = session.createCriteria(City.class);
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("name");
		fieldList.add("featureId");
		ProjectionList projection = ProjectionBean.fieldList(fieldList);
		testCriteria.setProjection(projection);
		testCriteria.setResultTransformer(Transformers
			.aliasToBean(City.class));

		List<City> results = testCriteria.list();
		return results;
	    }
	};

	List<City> cities = (List<City>) testDao
		.testCallback(hibernateCallback);
	assertEquals(1, cities.size());
	assertEquals("paris", cities.get(0).getName());
	assertEquals("1", cities.get(0).getFeatureId() + "");
    }

    @SuppressWarnings("unchecked")
    public void testBeanFieldList() {

	City p1 = GeolocTestHelper.createCity("paris", 48.86667F, 2.3333F, 1L);

	this.cityDao.save(p1);
	HibernateCallback hibernateCallback = new HibernateCallback() {

	    @SuppressWarnings("unchecked")
	    public Object doInHibernate(Session session)
		    throws PersistenceException {

		try {
		    Criteria testCriteria = session.createCriteria(City.class);
		    String[] ignoreFields = { "distance" };
		    ProjectionList projection = ProjectionBean.beanFieldList(
			    _CityDTO.class, ignoreFields);
		    testCriteria.setProjection(projection);
		    testCriteria.setResultTransformer(Transformers
			    .aliasToBean(_CityDTO.class));

		    List<_CityDTO> results = testCriteria.list();
		    return results;
		} catch (HibernateException e) {
		    fail("An exception has occured : maybe ignoreFields are not taken into account if the error is 'could not resolve property: distance...");
		    throw e;
		}
	    }
	};

	List<_CityDTO> cities = (List<_CityDTO>) testDao
		.testCallback(hibernateCallback);
	assertEquals(1, cities.size());
	assertEquals("paris", cities.get(0).getName());
	assertEquals("1", cities.get(0).getFeatureId() + "");
    }

    @Required
    public void setCityDao(ICityDao cityDao) {
	this.cityDao = cityDao;
    }

    /**
     * @param testDao
     *                the testDao to set
     */
    public void setTestDao(_DaoHelper testDao) {
	this.testDao = testDao;
    }

}
