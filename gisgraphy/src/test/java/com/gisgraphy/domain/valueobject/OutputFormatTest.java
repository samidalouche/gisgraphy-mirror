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

import static com.gisgraphy.domain.valueobject.OutputFormatHelper.isSupported;

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
    public void isSupportedShouldReturnCorrectValues() {
	for (OutputFormat format : OutputFormat.values()) {
		if (format == OutputFormat.UNSUPPORTED){
			Assert.assertFalse(isSupported(format,GisgraphyServiceType.FULLTEXT));
		} else {
			Assert.assertTrue(isSupported(format,GisgraphyServiceType.FULLTEXT));
		}
	}

	for (OutputFormat format : OutputFormat.values()) {
	    if (format == OutputFormat.XML || format == OutputFormat.JSON || format == OutputFormat.GEORSS || format == OutputFormat.ATOM) {
		Assert.assertTrue(isSupported(format,GisgraphyServiceType.GEOLOC));
		Assert.assertTrue(isSupported(format,GisgraphyServiceType.STREET));
	    } else {
		Assert.assertFalse(isSupported(format,GisgraphyServiceType.GEOLOC));
		Assert.assertFalse(isSupported(format,GisgraphyServiceType.STREET));
	    }
	}
    }
}
