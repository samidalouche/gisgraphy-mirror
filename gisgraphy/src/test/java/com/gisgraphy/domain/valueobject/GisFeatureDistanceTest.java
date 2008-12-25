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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.test.GeolocTestHelper;

public class GisFeatureDistanceTest extends AbstractIntegrationHttpSolrTestCase {

    @Test
    public void testGisFeatureDistanceShouldBeMappedWithJAXBWhenConstructWithGisFeatureAndDistance() {
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
	    checkJAXBMapping(result, outputStream);
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testGisFeatureDistanceShouldBeMappedWithJAXBWhenConstructWithAllFields() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GisFeatureDistance.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = GeolocTestHelper
		    .createFullFilledGisFeatureDistanceWithFieldsConstructor();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(result, outputStream);
	    checkJAXBMapping(result, outputStream);
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	}
    }

    /**
     * @param result
     * @param outputStream
     */
    private void checkJAXBMapping(GisFeatureDistance result,
	    ByteArrayOutputStream outputStream) {
	try {
	    assertQ(
		    "GisFeatureDistance is not correcty mapped with jaxb",
		    outputStream.toString(Constants.CHARSET),
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME + "/name[.='"
			    + result.getName() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm1Code[.='" + result.getAdm1Code() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm2Code[.='" + result.getAdm2Code() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm3Code[.='" + result.getAdm3Code() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm4Code[.='" + result.getAdm4Code() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm1Name[.='" + result.getAdm1Name() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm2Name[.='" + result.getAdm2Name() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm3Name[.='" + result.getAdm3Name() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm4Name[.='" + result.getAdm4Name() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/asciiName[.='" + result.getAsciiName() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/countryCode[.='" + result.getCountryCode()
			    + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/elevation[.='" + result.getElevation() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/featureClass[.='" + result.getFeatureClass()
			    + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/featureCode[.='" + result.getFeatureCode()
			    + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/placeType[.='" + result.getPlaceType() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/featureId[.='" + result.getFeatureId() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/gtopo30[.='" + result.getGtopo30() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/population[.='" + result.getPopulation() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/timezone[.='" + result.getTimezone() + "']",
		    "/" + Constants.GISFEATUREDISTANCE_JAXB_NAME + "/lat[.='"
			    + result.getLat() + "']", "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/lng[.='" + result.getLng() + "']", "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/google_map_url[.='"
			    + result.getGoogle_map_url() + "']", "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/yahoo_map_url[.='" + result.getYahoo_map_url()
			    + "']", "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/country_flag_url[.='"
			    + result.getCountry_flag_url() + "']"

	    );
	} catch (UnsupportedEncodingException e) {
	    fail("unsupported encoding for " + Constants.CHARSET);
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
	    checkJAXBMapping(result, outputStream);
	    assertQ("Zipcode should be output if The GisFeature is a city",
		    outputStream.toString(Constants.CHARSET), "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/zipCode[.='" + result.getZipCode() + "']");
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	} catch (UnsupportedEncodingException e) {
	    fail("unsupported encoding for " + Constants.CHARSET);
	}
    }

}
