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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import junit.framework.TestCase;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.test.GeolocTestHelper;
import com.gisgraphy.test.XpathChecker;

public class GeolocResultsDtoTest extends TestCase {

    @Test
    public void testGeolocResultsDtoShouldBeMappedWithJAXB() {
	GisFeatureDistance result = null;
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GeolocResultsDto.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    result = GeolocTestHelper
		    .createFullFilledGisFeatureDistanceWithGisFeatureConstructor();
	    List<GisFeatureDistance> list = new ArrayList<GisFeatureDistance>();
	    list.add(result);
	    GeolocResultsDto geolocResultsDto = new GeolocResultsDto(list, 300L);
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(geolocResultsDto, outputStream);
	    checkJAXBMapping(geolocResultsDto, outputStream);
	} catch (PropertyException e) {
	    fail(e.getMessage());
	} catch (JAXBException e) {
	    fail(e.getMessage());
	}
    }

    @Test
    public void testGeolocResultsDtoForEmptyListShouldreturnValidXML() {
	try {
	    JAXBContext context = JAXBContext
		    .newInstance(GeolocResultsDto.class);
	    Marshaller m = context.createMarshaller();
	    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    List<GisFeatureDistance> list = new ArrayList<GisFeatureDistance>();
	    GeolocResultsDto geolocResultsDto = new GeolocResultsDto(list, 200L);
	    long qTime = geolocResultsDto.getQTime();
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    m.marshal(geolocResultsDto, outputStream);
	    try {
		XpathChecker.assertQ(
			"GeolocResultsDto for an Empty List should return valid XML",
			outputStream.toString(Constants.CHARSET), "/"
				+ Constants.GEOLOCRESULTSDTO_JAXB_NAME + "",
			"/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME
				+ "/numFound[.='0']", "/"
				+ Constants.GEOLOCRESULTSDTO_JAXB_NAME
				+ "/QTime[.='" + qTime + "']");
	    } catch (UnsupportedEncodingException e) {
		fail("unsupported encoding exception for " + Constants.CHARSET);
	    }
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
    // TODO refactoring with GisFeatureDistanceTest
    private void checkJAXBMapping(GeolocResultsDto geolocResultsDto,
	    ByteArrayOutputStream outputStream) {
	try {
	    List<GisFeatureDistance> results = geolocResultsDto.getResult();
	    assertEquals("The number of results found is incorrect",1, results.size());
	    GisFeatureDistance result = results.get(0);
	    XpathChecker.assertQ(
		    "GisFeatureDistance list is not correcty mapped with jaxb",
		    outputStream.toString(Constants.CHARSET),
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/name[.='" + result.getName() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm1Code[.='" + result.getAdm1Code() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm2Code[.='" + result.getAdm2Code() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm3Code[.='" + result.getAdm3Code() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm4Code[.='" + result.getAdm4Code() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm1Name[.='" + result.getAdm1Name() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm2Name[.='" + result.getAdm2Name() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm3Name[.='" + result.getAdm3Name() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/adm4Name[.='" + result.getAdm4Name() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/asciiName[.='" + result.getAsciiName() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/countryCode[.='" + result.getCountryCode()
			    + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/elevation[.='" + result.getElevation() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/featureClass[.='" + result.getFeatureClass()
			    + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/featureCode[.='" + result.getFeatureCode()
			    + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/featureId[.='" + result.getFeatureId() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/gtopo30[.='" + result.getGtopo30() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/population[.='" + result.getPopulation() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/timezone[.='" + result.getTimezone() + "']",
		    "/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/lat[.='" + result.getLat() + "']", "/"
			    + Constants.GEOLOCRESULTSDTO_JAXB_NAME + "/"
			    + Constants.GISFEATUREDISTANCE_JAXB_NAME
			    + "/lng[.='" + result.getLng() + "']", "/"
			    + Constants.GEOLOCRESULTSDTO_JAXB_NAME
			    + "/numFound[.='" + geolocResultsDto.getNumFound() + "']", "/"
			    + Constants.GEOLOCRESULTSDTO_JAXB_NAME
			    + "/QTime[.='" + geolocResultsDto.getQTime() + "']"

	    );
	} catch (UnsupportedEncodingException e) {
	    fail("unsupported encoding for " + Constants.CHARSET);
	}
    }

}
