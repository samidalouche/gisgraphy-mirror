package com.gisgraphy.webapp.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.jstester.JsTester;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.domain.valueobject.SolrResponseDto;

public class GeocodingActionTest {

    @Test
    public void testGetCountries() {
	ICountryDao countryDaoMock = EasyMock.createMock(ICountryDao.class);
	EasyMock.expect(countryDaoMock.getAllSortedByName()).andReturn((new ArrayList<Country>()));
	EasyMock.replay(countryDaoMock);

	GeocodingAction action = new GeocodingAction();
	action.setCountryDao(countryDaoMock);

	action.getCountries();

	EasyMock.verify(countryDaoMock);

    }
   
    @Test
    public void testGetLatLongJson() {
	GeocodingAction action = new GeocodingAction();
	List<SolrResponseDto> ambiguousCities = createAmbiguousCities();
	action.setAmbiguousCities(ambiguousCities);
	String feed = action.getLatLongJson();

	JsTester jsTester = new JsTester();
	jsTester.onSetUp();

	// JsTester
	jsTester.eval("evalresult= eval(" + feed + ");");
	Assert.assertNotNull(jsTester.eval("evalresult"));
	Assert.assertEquals(ambiguousCities.get(0).getLat().toString(), (jsTester.eval("evalresult.result[0]['lat']")).toString());
	Assert.assertEquals(ambiguousCities.get(0).getLng(), jsTester.eval("evalresult.result[0]['lng']"));
	Assert.assertEquals(ambiguousCities.get(1).getLat().toString(), (jsTester.eval("evalresult.result[1]['lat']")).toString());
	Assert.assertEquals(ambiguousCities.get(1).getLng(), jsTester.eval("evalresult.result[1]['lng']"));

    }

    private List<SolrResponseDto> createAmbiguousCities() {
	List<SolrResponseDto> ambiguousCities = new ArrayList<SolrResponseDto>();
	SolrResponseDto city1 = org.easymock.classextension.EasyMock.createMock(SolrResponseDto.class);
	org.easymock.classextension.EasyMock.expect(city1.getLat()).andStubReturn(3.2D);
	EasyMock.expect(city1.getLng()).andStubReturn(4.5D);
	EasyMock.replay(city1);

	SolrResponseDto city2 = org.easymock.classextension.EasyMock.createMock(SolrResponseDto.class);
	org.easymock.classextension.EasyMock.expect(city2.getLat()).andStubReturn(3.2D);
	EasyMock.expect(city2.getLng()).andStubReturn(4.5D);
	EasyMock.replay(city2);

	ambiguousCities.add(city1);
	ambiguousCities.add(city2);
	return ambiguousCities;
    }

}
