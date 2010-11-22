package com.gisgraphy.helper;

import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.repository.AbstractTransactionalTestCase;

public class GisFeatureHelperTest extends AbstractTransactionalTestCase {
    
    private GisFeatureHelper gisFeatureHelper;

    @Test
    public void testGetInstance() {
	assertNotNull("gisFeatureHelper should return an instance", GisFeatureHelper.getInstance());
    }

    @Test
    public void testGetFullyQualifiedNameGisFeatureBoolean() {
	GisFeature gisFeature = createGisFeatureMock();
	Country country = new Country();
	country.setName("countryName");
	EasyMock.expect(gisFeature.getCountryCode()).andReturn("FR");
	EasyMock.replay(gisFeature);
	GisFeatureHelper.getInstance().getFullyQualifiedName(gisFeature, true);
	EasyMock.verify(gisFeature);
    }

    private GisFeature createGisFeatureMock() {
	GisFeature gisFeature = EasyMock.createMock(GisFeature.class);
	EasyMock.expect(gisFeature.getAdm1Name()).andReturn("adm1name");
	EasyMock.expect(gisFeature.getAdm2Name()).andReturn("adm2name");
	EasyMock.expect(gisFeature.getName()).andReturn("name");
	
	return gisFeature;
    }

    @Test
    public void testGetFullyQualifiedNameGisFeature() {
	GisFeature gisFeature = createGisFeatureMock();
	EasyMock.replay(gisFeature);
	GisFeatureHelper.getInstance().getFullyQualifiedName(gisFeature);
	EasyMock.verify(gisFeature);
    }

    @Test
    public void testGetCountry() {
	//fail("Not yet implemented");
    }

  

    public void setGisFeatureHelper(GisFeatureHelper gisFeatureHelper) {
        this.gisFeatureHelper = gisFeatureHelper;
    }

}
