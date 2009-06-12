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
package com.gisgraphy.domain.valueobject;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output.OutputFormat;

public class OutputFormatTest  {

    @Test
    public void getFromStringShouldReturnCorrectValues() {
	Assert.assertEquals("A null value should return the default outputFormat ",
		OutputFormat.getDefault(), OutputFormat.getFromString(null));
	Assert.assertEquals(
		"A correct value should return the associated outputFormat ",
		OutputFormat.JSON, OutputFormat.getFromString("JSON"));
	Assert.assertEquals("getFromString should be case insensitive ",
		OutputFormat.XML, OutputFormat.getFromString("xml"));
	Assert.assertEquals(
		"An incorrect value should return the default outputFormat ",
		OutputFormat.getDefault(), OutputFormat.getFromString("unknow"));
    }

    @Test
    public void getDefaultForServiceIfNotSupportedShouldImplementsAllGisgraphyService() {
	for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
	    try {
		OutputFormat.getDefaultForServiceIfNotSupported(
			OutputFormat.XML, serviceType);
	    } catch (RuntimeException e) {
		Assert.fail(e.getMessage());
	    }
	}
    }

    @Test
    public void getListByServiceShouldImplementsAllGisgraphyService() {
	for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
	    try {
		OutputFormat.listByService(serviceType);
	    } catch (Exception e) {
		Assert.fail(e.getMessage());
	    }
	}
    }

    @Test
    public void getListByServiceShouldReturnCorrectValues() {
	Assert.assertEquals(Arrays.asList(OutputFormat.values()), Arrays
		.asList(OutputFormat
			.listByService(GisgraphyServiceType.FULLTEXT)));
	OutputFormat[] expected = { OutputFormat.XML, OutputFormat.JSON,
		OutputFormat.ATOM, OutputFormat.GEORSS };
	Assert.assertEquals(Arrays.asList(expected), Arrays.asList(OutputFormat
		.listByService(GisgraphyServiceType.GEOLOC)));

    }

    @Test
    public void getDefaultForServiceIfNotSupportedShouldReturnsCorrectValues() {
	// fulltext service allows all formats
	for (OutputFormat format : OutputFormat.values()) {
	    Assert.assertEquals(format, OutputFormat
		    .getDefaultForServiceIfNotSupported(format,
			    GisgraphyServiceType.FULLTEXT));
	}
	//street
	for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()){
	for (OutputFormat format : OutputFormat.values()) {
	   if (format.isSupported(serviceType)){
	    Assert.assertEquals(format, OutputFormat
		    .getDefaultForServiceIfNotSupported(format,
			    serviceType));
	   }
	   else {
	       Assert.assertEquals(OutputFormat.getDefault(), OutputFormat.getDefaultForServiceIfNotSupported(format, serviceType));
	   }
	}
	}
    }

    @Test
    public void isSupportedShouldReturnCorrectValues() {
	for (OutputFormat format : OutputFormat.values()) {
	    Assert.assertTrue(format.isSupported(GisgraphyServiceType.FULLTEXT));
	}

	for (OutputFormat format : OutputFormat.values()) {
	    if (format == OutputFormat.XML || format == OutputFormat.JSON || format == OutputFormat.GEORSS || format == OutputFormat.ATOM) {
		Assert.assertTrue(format.isSupported(GisgraphyServiceType.GEOLOC));
		Assert.assertTrue(format.isSupported(GisgraphyServiceType.STREET));
	    } else {
		Assert.assertFalse(format.isSupported(GisgraphyServiceType.GEOLOC));
		Assert.assertFalse(format.isSupported(GisgraphyServiceType.STREET));
	    }
	}
    }
}
