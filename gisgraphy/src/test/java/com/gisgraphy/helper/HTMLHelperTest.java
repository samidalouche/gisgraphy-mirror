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
package com.gisgraphy.helper;

import static com.gisgraphy.servlet.FulltextServlet.QUERY_PARAMETER;
import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.servlet.FulltextServlet;
import com.gisgraphy.servlet.GeolocServlet;
import com.gisgraphy.test.GeolocTestHelper;

public class HTMLHelperTest extends TestCase {

    public void testIsParametesrEmptyShouldReturnValidResult() {
	MockHttpServletRequest req = GeolocTestHelper
		.createMockHttpServletRequestForFullText();
	assertTrue(!HTMLHelper.isParametersEmpty(req, QUERY_PARAMETER));
	req.removeParameter(FulltextServlet.QUERY_PARAMETER);
	assertTrue(HTMLHelper.isParametersEmpty(req, QUERY_PARAMETER));

	req = GeolocTestHelper.createMockHttpServletRequestForGeoloc();
	assertTrue(!HTMLHelper.isParametersEmpty(req,
		GeolocServlet.LAT_PARAMETER, GeolocServlet.LONG_PARAMETER));
	req.removeParameter(GeolocServlet.LAT_PARAMETER);
	assertTrue(HTMLHelper.isParametersEmpty(req,
		GeolocServlet.LAT_PARAMETER));
	assertTrue(HTMLHelper.isParametersEmpty(req,
		GeolocServlet.LAT_PARAMETER, GeolocServlet.LONG_PARAMETER));
	req.removeParameter(GeolocServlet.LONG_PARAMETER);
	assertTrue(HTMLHelper.isParametersEmpty(req,
		GeolocServlet.LAT_PARAMETER));
	assertTrue(HTMLHelper.isParametersEmpty(req,
		GeolocServlet.LONG_PARAMETER));
	assertTrue(HTMLHelper.isParametersEmpty(req,
		GeolocServlet.LAT_PARAMETER, GeolocServlet.LONG_PARAMETER));
    }

}
