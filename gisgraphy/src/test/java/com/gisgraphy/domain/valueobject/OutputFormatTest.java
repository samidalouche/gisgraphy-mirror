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

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output.OutputFormat;

public class OutputFormatTest extends TestCase {

    @Test
    public void testGetFromStringShouldReturnCorrectValues() {
	assertEquals("A null value should return the default outputFormat ",
		OutputFormat.getDefault(), OutputFormat.getFromString(null));
	assertEquals(
		"A correct value should return the associated outputFormat ",
		OutputFormat.JSON, OutputFormat.getFromString("JSON"));
	assertEquals("getFromString should be case insensitive ",
		OutputFormat.XML, OutputFormat.getFromString("xml"));
	assertEquals(
		"An incorrect value should return the default outputFormat ",
		OutputFormat.getDefault(), OutputFormat.getFromString("unknow"));
    }

    @Test
    public void getByServiceShouldImplementsAllGisgraphyService() {
	for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
	    try {
		OutputFormat.isForService(OutputFormat.XML, serviceType);
	    } catch (RuntimeException e) {
		fail(e.getMessage());
	    }
	}
    }

    @Test
    public void getListByServiceShouldImplementsAllGisgraphyService() {
	for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
	    try {
		OutputFormat.listByService(serviceType);
	    } catch (RuntimeException e) {
		fail(e.getMessage());
	    }
	}
    }

    @Test
    public void getListByServiceShouldReturnCorrectValues() {
	Assert.assertEquals(Arrays.asList(OutputFormat.values()), Arrays
		.asList(OutputFormat
			.listByService(GisgraphyServiceType.FULLTEXT)));
	OutputFormat[] expected = { OutputFormat.XML, OutputFormat.JSON };
	Assert.assertEquals(Arrays.asList(expected), Arrays.asList(OutputFormat
		.listByService(GisgraphyServiceType.GEOLOC)));

    }

    @Test
    public void getByServiceShouldReturnsCorrectValues() {
	assertEquals(OutputFormat.XML, OutputFormat.isForService(
		OutputFormat.XML, GisgraphyServiceType.GEOLOC));
	assertEquals(OutputFormat.JSON, OutputFormat.isForService(
		OutputFormat.JSON, GisgraphyServiceType.GEOLOC));
	assertEquals(OutputFormat.getDefault(), OutputFormat.isForService(
		OutputFormat.RUBY, GisgraphyServiceType.GEOLOC));
	assertEquals(OutputFormat.getDefault(), OutputFormat.isForService(
		OutputFormat.PYTHON, GisgraphyServiceType.GEOLOC));

	// fulltext service allows all formats
	for (OutputFormat format : OutputFormat.values()) {
	    assertEquals(format, OutputFormat.isForService(format,
		    GisgraphyServiceType.FULLTEXT));
	}
    }
}
