package com.gisgraphy.hibernate.criterion;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.geoloc.street.StreetType;
import com.gisgraphy.domain.repository.IOpenStreetMapDao;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.helper.StringHelper;
import com.gisgraphy.hibernate.projection.ProjectionBean;
import com.gisgraphy.hibernate.projection._OpenstreetmapDTO;
import com.gisgraphy.test.GeolocTestHelper;
import com.gisgraphy.test._DaoHelper;
import com.vividsolutions.jts.geom.LineString;


public class FulltextRestrictionTest extends AbstractIntegrationHttpSolrTestCase{
    
    private _DaoHelper testDao;

    private IOpenStreetMapDao openStreetMapDao;
    
       
    @SuppressWarnings("unchecked")
    @Test
    public void testFulltextRestriction() {
	OpenStreetMap streetOSM = GeolocTestHelper.createOpenStreetMapForJohnKenedyStreet();
	streetOSM.setName("Champs-Elysées");
	openStreetMapDao.save(streetOSM);
	assertNotNull(openStreetMapDao.get(streetOSM.getId()));
	
	
	int numberOfLineUpdated = openStreetMapDao.updateTS_vectorColumnForStreetNameSearch();
	assertEquals("It should have 2 lines updated : one for partial and one for fulltext",2, numberOfLineUpdated);
	
	
	HibernateCallback hibernateCallback = new HibernateCallback() {

	    public Object doInHibernate(Session session)
		    throws PersistenceException {

		Criteria testCriteria = session.createCriteria(OpenStreetMap.class);
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("name");
		fieldList.add("gid");
		
		Projection projection = ProjectionBean.fieldList(fieldList, true);
		testCriteria.setProjection(projection).add(
			new FulltextRestriction(OpenStreetMap.FULLTEXTSEARCH_VECTOR_COLUMN_NAME, "Champs elysees"))//case sensitive accent
			.setResultTransformer(
				Transformers.aliasToBean(_OpenstreetmapDTO.class));

		List<_OpenstreetmapDTO> results = testCriteria.list();
		return results;
	    }
	};

	List<_OpenstreetmapDTO> streets = (List<_OpenstreetmapDTO>) testDao
		.testCallback(hibernateCallback);
	assertEquals(
		"According to the fulltext restriction, it should have a result (it should be case insensitive,accent insensitive and '-' insensitive",
		1, streets.size());
	
	assertEquals(
		"According to the fulltext restriction, the result is incorrect",
		streetOSM.getGid(), streets.get(0).getGid());
	
	HibernateCallback hibernateCallbackminusSign = new HibernateCallback() {

	    public Object doInHibernate(Session session)
		    throws PersistenceException {

		Criteria testCriteria = session.createCriteria(OpenStreetMap.class);
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("name");
		fieldList.add("gid");
		
		Projection projection = ProjectionBean.fieldList(fieldList, true);
		testCriteria.setProjection(projection).add(
			new FulltextRestriction(OpenStreetMap.FULLTEXTSEARCH_VECTOR_COLUMN_NAME, "Champs-elysees"))//'-'
			.setResultTransformer(
				Transformers.aliasToBean(_OpenstreetmapDTO.class));

		List<_OpenstreetmapDTO> results = testCriteria.list();
		return results;
	    }
	};

	 streets = (List<_OpenstreetmapDTO>) testDao
		.testCallback(hibernateCallbackminusSign);
	assertEquals(
		"According to the fulltext restriction, it should have a result (it should be '-' insensitive",
		1, streets.size());
	
	assertEquals(
		"According to the fulltext restriction, the result is incorrect",
		streetOSM.getGid(), streets.get(0).getGid());
	
	HibernateCallback hibernateCallbackZeroResult = new HibernateCallback() {

	    public Object doInHibernate(Session session)
		    throws PersistenceException {

		Criteria testCriteria = session.createCriteria(OpenStreetMap.class);
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("name");
		fieldList.add("gid");
		
		Projection projection = ProjectionBean.fieldList(fieldList, true);
		testCriteria.setProjection(projection).add(
			new FulltextRestriction(OpenStreetMap.FULLTEXTSEARCH_VECTOR_COLUMN_NAME, "Champ elysees"))//wrong word
			.setResultTransformer(
				Transformers.aliasToBean(_OpenstreetmapDTO.class));

		List<_OpenstreetmapDTO> results = testCriteria.list();
		return results;
	    }
	};

	 streets = (List<_OpenstreetmapDTO>) testDao
		.testCallback(hibernateCallbackZeroResult);
	assertEquals(
		"According to the fulltext restriction, it should not have result ",
		0, streets.size());
	

    }
    
    public void setOpenStreetMapDao(IOpenStreetMapDao openStreetMapDao) {
        this.openStreetMapDao = openStreetMapDao;
    }

    public void setTestDao(_DaoHelper testDao) {
	this.testDao = testDao;
    }

}
