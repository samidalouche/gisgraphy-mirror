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
 *
 */
package com.gisgraphy.domain.valueobject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.repository.IGisDao;
import com.gisgraphy.helper.IntrospectionHelper;
import com.gisgraphy.test.FeedChecker;
import com.gisgraphy.test.GeolocTestHelper;

public class GisFeatureDistanceTest extends AbstractIntegrationHttpSolrTestCase {

    @Autowired
    protected IGisDao<? extends GisFeature>[] daos;

    @Test
    public void testGisFeatureDistanceShouldBeMappedWithJAXBAndHaveCalculatedFieldsWhenConstructWithGisFeatureAndDistance() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GisFeatureDistance.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = GeolocTestHelper
		    .createFullFilledGisFeatureDistanceWithGisFeatureConstructor();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(result, outputStream);
	    FeedChecker.checkGisFeatureDistanceJAXBMapping(result, outputStream.toString(Constants.CHARSET),"");
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testGisFeatureDistanceShouldBeMappedWithJAXBAndHaveCalculatedFieldsWhenConstructWithBuilder() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GisFeatureDistance.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = GeolocTestHelper
		    .createFullFilledGisFeatureDistanceWithBuilder();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(result, outputStream);
	    FeedChecker.checkGisFeatureDistanceJAXBMapping(result, outputStream.toString(Constants.CHARSET),"");
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    fail(e.getMessage());
	}
    }
    


    @Test
    public void testGisFeatureDistanceShouldHaveZipCodeIfGisFeatureIsCity() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GisFeatureDistance.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = GeolocTestHelper
		    .createFullFilledGisFeatureDistanceForCity();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(result, outputStream);
	    FeedChecker.checkGisFeatureDistanceJAXBMapping(result, outputStream.toString(Constants.CHARSET),"");
	    for (String zipCode: result.getZipCodes()){
	    FeedChecker.assertQ("Zipcode should be output if The GisFeature is a city",
		    outputStream.toString(Constants.CHARSET), "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/zipCode[.='" + zipCode + "']");
	    }
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    fail("unsupported encoding for " + Constants.CHARSET);
	}
    }
    
    @Test
    public void testGisFeatureDistanceShouldHaveZipCodeIfGisFeatureIsAdm() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GisFeatureDistance.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = GeolocTestHelper
		    .createFullFilledGisFeatureDistanceForAdm();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(result, outputStream);
	    FeedChecker.checkGisFeatureDistanceJAXBMapping(result, outputStream.toString(Constants.CHARSET),"");
	    FeedChecker.assertQ("Zipcode should be output if The GisFeature is a city",
		    outputStream.toString(Constants.CHARSET), "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/level[.='" + result.getLevel() + "']");
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    fail("unsupported encoding for " + Constants.CHARSET);
	}
    }
    
    @Test
    public void testGisFeatureDistanceShouldHaveZipCodeIfGisFeatureIsCitySubdivision() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GisFeatureDistance.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = GeolocTestHelper
		    .createFullFilledGisFeatureDistanceForCitySubdivision();
	    
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(result, outputStream);
	    FeedChecker.checkGisFeatureDistanceJAXBMapping(result, outputStream.toString(Constants.CHARSET),"");
	    for (int i=0;i< result.getZipCodes().size();i++){
	    FeedChecker.assertQ("Zipcode should be output if The GisFeature is a citysubdivision",
		    outputStream.toString(Constants.CHARSET), "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/zipCode[.='" + result.getZipCodes().get(i) + "']");}
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    fail("unsupported encoding for " + Constants.CHARSET);
	}
    }
    
    @Test
    public void testGisFeatureDistanceShouldHaveCountryInfosAndCalculatedFieldsWhenConstructWithConstructorCountry() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GisFeatureDistance.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = new GisFeatureDistance(GeolocTestHelper.createFullFilledCountry(), 3D);
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(result, outputStream);
	    FeedChecker.checkGisFeatureDistanceJAXBMapping(result, outputStream.toString(Constants.CHARSET),"");
	    String streamToString = outputStream.toString(Constants.CHARSET);
	    FeedChecker.assertQ("area should be filled if The GisFeature is a Country",
		    streamToString, "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/area[.='" + result.getArea() + "']",
			    "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/tld[.='" + result.getTld() + "']",
			    "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/capitalName[.='" + result.getCapitalName() + "']",
			    "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/continent[.='" + result.getContinent() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/postalCodeRegex[.='" + result.getPostalCodeRegex() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/currencyCode[.='" + result.getCurrencyCode() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/currencyName[.='" + result.getCurrencyName() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/equivalentFipsCode[.='" + result.getEquivalentFipsCode() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/fipsCode[.='" + result.getFipsCode() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/iso3166Alpha2Code[.='" + result.getIso3166Alpha2Code() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/iso3166Alpha3Code[.='" + result.getIso3166Alpha3Code() + "']",
			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/iso3166NumericCode[.='" + result.getIso3166NumericCode() + "']",

			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/phonePrefix[.='" + result.getPhonePrefix() + "']",

			    "/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/postalCodeMask[.='" + result.getPostalCodeMask() + "']");
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    fail("unsupported encoding for " + Constants.CHARSET);
	}
    }

    @Test
    public void testGisFeatureDistanceShouldHaveAllThefieldOfClassThatExtendsGisFeature() {
	List<Class<? extends GisFeature>> classList = new ArrayList<Class<? extends GisFeature>>();
	for (int i = 0; i < daos.length; i++) {
	    classList.add(daos[i].getPersistenceClass());
	}
	List<String> GisFeatureDistanceFields = inspectGisFeatureDistance();
	for (Class<? extends GisFeature> gisClass : classList) {
	    for (String field : IntrospectionHelper.getFieldsAsList(gisClass)){
		assertTrue("GisFeatureDistance does not contain "+field+ " and should  because "+gisClass+" has this field",GisFeatureDistanceFields.contains(field));
	    }
	}
    }
    

   @Test
   public void testEqualsShouldTakeFeatureIdIntoAccout(){
	Long featureId = 123L;
	GisFeatureDistance gisFeatureDistance = GisFeatureDistance.
							GisFeatureDistanceBuilder.gisFeatureDistance()
							.withFeatureId(featureId).build();
	GisFeatureDistance gisFeatureDistanceEquals = GisFeatureDistance.GisFeatureDistanceBuilder.gisFeatureDistance().withFeatureId(featureId).build();
	GisFeatureDistance gisFeatureDistanceNotEquals = GisFeatureDistance.GisFeatureDistanceBuilder.gisFeatureDistance().withFeatureId(featureId+1).build();
	Assert.assertEquals("gisFeaturedistance with the same featureid should be equals", gisFeatureDistance, gisFeatureDistanceEquals);
	Assert.assertFalse("gisFeaturedistance without the same featureid should not be equals", gisFeatureDistance.equals(gisFeatureDistanceNotEquals));
   }

    private List<String> inspectGisFeatureDistance(){
	Class<?> clazzParent = GisFeatureDistance.class;
	List<String> introspectedFields = new ArrayList<String>();
	    do {
		int searchMods = 0x0;
		searchMods |= modifierFromString("private");

		Field[] flds = clazzParent.getDeclaredFields();
		for (Field f : flds) {
		    int foundMods = f.getModifiers();
		    if ((foundMods & searchMods) == searchMods
			    && !f.isSynthetic() && f.getType() != List.class
			    && !Modifier.isFinal(foundMods)) {
			introspectedFields.add(f.getName());
		    }
		}
		clazzParent = (Class<?>) clazzParent.getSuperclass();
	    } while (clazzParent != Object.class);
	return introspectedFields;
    }
    
    private static int modifierFromString(String s) {
	int m = 0x0;
	if ("public".equals(s))
	    m |= Modifier.PUBLIC;
	else if ("protected".equals(s))
	    m |= Modifier.PROTECTED;
	else if ("private".equals(s))
	    m |= Modifier.PRIVATE;
	else if ("static".equals(s))
	    m |= Modifier.STATIC;
	else if ("final".equals(s))
	    m |= Modifier.FINAL;
	else if ("transient".equals(s))
	    m |= Modifier.TRANSIENT;
	else if ("volatile".equals(s))
	    m |= Modifier.VOLATILE;
	return m;
    }
    
}
