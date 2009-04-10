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
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.test.GeolocTestHelper;
import com.gisgraphy.test.FeedChecker;

public class StreetSearchResultsDtoSerializerTest {

    @Test
    public void testSerializeShouldThrowIfTheformatisNotSupported() {
	IStreetSearchResultsDtoSerializer streetSearchResultsDtoSerializer = new StreetSearchResultsDtoSerializer();
	try {
	    streetSearchResultsDtoSerializer.serialize(new ByteArrayOutputStream(),
		    OutputFormat.RUBY, new StreetSearchResultsDto(),true,1);
	    fail();
	} catch (UnsupportedFormatException e) {
	    //ok
	}
    }
    
    @Test
    public void testSerializeShouldSerializeInXML() throws UnsupportedEncodingException {
	IStreetSearchResultsDtoSerializer streetSearchResultsDtoSerializer = new StreetSearchResultsDtoSerializer();
	    StreetSearchResultsDto streetSearchResultsDto = GeolocTestHelper.createStreetSearchResultsDto();
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    streetSearchResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.XML, streetSearchResultsDto,true,1);
	    FeedChecker.checkStreetSearchResultsDtoJAXBMapping(streetSearchResultsDto, 
		    byteArrayOutputStream);
    }

    @Test
    public void testSerializeShouldSerializeInJSON() throws UnsupportedEncodingException {
	IStreetSearchResultsDtoSerializer streetSearchResultsDtoSerializer = new StreetSearchResultsDtoSerializer();
	    StreetSearchResultsDto streetSearchResultsDto = GeolocTestHelper.createStreetSearchResultsDto();
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    streetSearchResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.JSON, streetSearchResultsDto,true,1);
	    FeedChecker.checkStreetSearchResultsDtoJSON(streetSearchResultsDto, 
		    byteArrayOutputStream.toString(Constants.CHARSET));
    }
   
    @Test
    public void testSerializeShouldSerializeInGEORSS() throws UnsupportedEncodingException {
	IStreetSearchResultsDtoSerializer streetSearchResultsDtoSerializer = new StreetSearchResultsDtoSerializer();
	   StreetSearchResultsDto streetSearchResultsDto = GeolocTestHelper.createStreetSearchResultsDto();
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    streetSearchResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.GEORSS, streetSearchResultsDto,true,1);
	    FeedChecker.checkStreetSearchResultsDtoGEORSS(streetSearchResultsDto, 
		    byteArrayOutputStream.toString(Constants.CHARSET));
    }
    
    @Test
    public void testSerializeShouldSerializeInATOM() throws UnsupportedEncodingException {
	IStreetSearchResultsDtoSerializer streetSearchResultsDtoSerializer = new StreetSearchResultsDtoSerializer();
	   StreetSearchResultsDto streetSearchResultsDto = GeolocTestHelper.createStreetSearchResultsDto();
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    streetSearchResultsDtoSerializer.serialize(byteArrayOutputStream,
		    OutputFormat.ATOM, streetSearchResultsDto,true,1);
	    FeedChecker.checkStreetSearchResultsDtoATOM(streetSearchResultsDto, 
		    byteArrayOutputStream.toString(Constants.CHARSET));
    }
    
}