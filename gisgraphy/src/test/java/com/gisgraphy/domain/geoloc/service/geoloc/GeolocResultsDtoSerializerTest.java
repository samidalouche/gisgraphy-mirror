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
package com.gisgraphy.domain.geoloc.service.geoloc;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.errors.UnsupportedFormatException;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.test.FeedChecker;
import com.gisgraphy.test.GeolocTestHelper;

public class GeolocResultsDtoSerializerTest {

    @Test
    public void testSerializeShouldThrowIfTheformatisNotSupported() {
	IGeolocResultsDtoSerializer geolocResultsDtoSerializer = new GeolocResultsDtoSerializer();
	try {
	    geolocResultsDtoSerializer.serialize(new ByteArrayOutputStream(),
		    OutputFormat.RUBY, new GeolocResultsDto(),true,1);
	    fail();
	} catch (UnsupportedFormatException e) {
	    //ok
	}
    }
    
    @Test
    public void testSerializeShouldSerializeInXML() throws UnsupportedEncodingException {
	IGeolocResultsDtoSerializer geolocResultsDtoSerializer = new GeolocResultsDtoSerializer();
	    GeolocResultsDto geolocResultsDto = GeolocTestHelper.createGeolocResultsDto(310L);
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    geolocResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.XML, geolocResultsDto,true,1);
	    FeedChecker.checkGeolocResultsDtoJAXBMapping(geolocResultsDto, 
		    byteArrayOutputStream.toString(Constants.CHARSET));
    }
    
    @Test
    public void testSerializeShouldSerializeInJSON() throws UnsupportedEncodingException {
	IGeolocResultsDtoSerializer geolocResultsDtoSerializer = new GeolocResultsDtoSerializer();
	    GeolocResultsDto geolocResultsDto = GeolocTestHelper.createGeolocResultsDto(310L);
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    geolocResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.JSON, geolocResultsDto,true,1);
	    FeedChecker.checkGeolocResultsDtoJSON(geolocResultsDto, 
		    byteArrayOutputStream.toString(Constants.CHARSET));
    }
    
    @Test
    public void testSerializeShouldSerializeInGEORSS() throws UnsupportedEncodingException {
	IGeolocResultsDtoSerializer geolocResultsDtoSerializer = new GeolocResultsDtoSerializer();
	    GeolocResultsDto geolocResultsDto = GeolocTestHelper.createGeolocResultsDto(310L);
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    geolocResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.GEORSS, geolocResultsDto,true,1);
	    FeedChecker.checkGeolocResultsDtoGEORSS(geolocResultsDto, 
		    byteArrayOutputStream.toString(Constants.CHARSET));
    }
    
    @Test
    public void testSerializeShouldSerializeInATOM() throws UnsupportedEncodingException {
	IGeolocResultsDtoSerializer geolocResultsDtoSerializer = new GeolocResultsDtoSerializer();
	    GeolocResultsDto geolocResultsDto = GeolocTestHelper.createGeolocResultsDto(310L);
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    geolocResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.ATOM, geolocResultsDto,true,1);
	    FeedChecker.checkGeolocResultsDtoATOM(geolocResultsDto, 
		    byteArrayOutputStream.toString(Constants.CHARSET));
    }
    
    
    

}
