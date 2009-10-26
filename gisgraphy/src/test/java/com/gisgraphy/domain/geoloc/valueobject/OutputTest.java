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
package com.gisgraphy.domain.geoloc.valueobject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;

public class OutputTest  {

    @Test
    public void outputTestWithNullValuesSetTheDefaultFormat() {
	assertEquals(OutputFormat.getDefault(), Output.withFormat(null)
		.getFormat());
    }

    @Test
    public void withFormatShouldSetTheFormat() {
	assertEquals(OutputFormat.JSON, Output.withFormat(OutputFormat.JSON)
		.getFormat());
    }

    @Test
    public void withIndentationShouldSetTheidentation() {
	Assert.assertTrue(Output.withDefaultFormat().withIndentation().isIndented());
    }

    @Test
    public void withDefaultFormatShouldBeSetToFormat() {
	assertEquals(OutputFormat.getDefault(), Output.withDefaultFormat()
		.getFormat());
    }

    @Test
    public void withLanguageCodeWithNullOrEmptyShouldBeSetToDefault() {
	assertEquals(Output.DEFAULT_LANGUAGE_CODE, Output.withDefaultFormat()
		.withLanguageCode(null).getLanguageCode());
	assertEquals(Output.DEFAULT_LANGUAGE_CODE, Output.withDefaultFormat()
		.withLanguageCode(" ").getLanguageCode());
	assertEquals(Output.DEFAULT_LANGUAGE_CODE, Output.withDefaultFormat()
		.withLanguageCode("").getLanguageCode());
    }

    @Test
    public void withLanguageCodeShouldBeUpperCased() {
	assertEquals("FR", Output.withDefaultFormat().withLanguageCode("fr")
		.getLanguageCode());
    }

    @Test
    public void withStyleWithNullShouldBeSetToDefault() {
	assertEquals(OutputStyle.getDefault(), Output.withDefaultFormat()
		.withStyle(null).getStyle());
    }

    @Test
    public void defaultOutputShouldHaveDefaultParameters() {
	assertEquals(Output.DEFAULT_LANGUAGE_CODE, Output.DEFAULT_OUTPUT
		.getLanguageCode());
	assertEquals(OutputFormat.getDefault(), Output.DEFAULT_OUTPUT
		.getFormat());
	assertEquals(OutputStyle.getDefault(), Output.DEFAULT_OUTPUT
		.getStyle());
    }

    @Test
    public void withIndentationShouldSetTheIndentationToTrue() {
	assertEquals(true, Output.withDefaultFormat().withIndentation()
		.isIndented());
	assertEquals(false, Output.DEFAULT_OUTPUT.isIndented());
    }

    @Test
    public void outputStyleGetFieldListForShortShouldBeCorrect() {
	String list = OutputStyle.SHORT.getFieldList(null);
	assertEquals("The field list has a wrong size for SHORT :" + list, 8,
		list.split(",").length);
	assertFalse(
		"The field list for SHORT must not contains ',,' : " + list,
		list.contains(",,"));
	assertFalse(
		"The field list for SHORT must not ends with ',' : " + list,
		list.endsWith(","));
	list = OutputStyle.SHORT.getFieldList("fr");
	assertEquals(
		"The field list for SHORT should not be different for a specified country:"
			+ list, 8, list.split(",").length);
	assertFalse(
		"The field list for SHORT must not ends with ',' : " + list,
		list.endsWith(","));

    }

    @Test
    public void outputStyleGetFieldListForMediumShouldBeCorrect() {
	String list = OutputStyle.MEDIUM.getFieldList(null);
	assertEquals("The field list has a wrong size for MEDIUM :" + list, 34,
		list.split(",").length);
	assertFalse("The field list for MEDIUM must not contains ',,' : "
		+ list, list.contains(",,"));
	assertFalse("The field list for MEDIUM must not ends with ',' : "
		+ list, list.endsWith(","));
	list = OutputStyle.MEDIUM.getFieldList("fr");
	assertEquals(
		"The field list for MEDIUM should not be different for a specified country : "
			+ list, 34, list.split(",").length);
	assertFalse("The field list for MEDIUM must not ends with ',' : "
		+ list, list.endsWith(","));

    }

    @Test
    public void outputStyleGetFieldListForLongShouldBeCorrect() {
	String list = OutputStyle.LONG.getFieldList(null);
	assertEquals("The field list has a wrong size for LONG :" + list, 42,
		list.split(",").length);
	assertFalse("The field list for LONG must not contains ',,' : " + list,
		list.contains(",,"));
	assertFalse("The field list for LONG must not ends with ',' : " + list,
		list.endsWith(","));
	list = OutputStyle.LONG.getFieldList("fr");
	assertEquals(
		"The field list for LONG should not be different for a specified country : "
			+ list, 42, list.split(",").length);
	assertFalse("The field list for LONG must not ends with ',' : " + list,
		list.endsWith(","));

    }

    @Test
    public void outputStyleGetFieldListForFullShouldBeCorrect() {
	String list = OutputStyle.FULL.getFieldList(null);
	assertEquals(
		"The field list has a wrong size for FULL without countryCode :"
			+ list, 2, list.split(",").length);
	assertFalse("The field list for FULL must not contains ',,' : " + list,
		list.contains(",,"));
	assertFalse("The field list for FULL must not ends with ',' : " + list,
		list.endsWith(","));
	list = OutputStyle.FULL.getFieldList("fr");
	assertEquals(
		"The field list for medium should be different for a specified country :"
			+ list, 50, list.split(",").length);
	assertFalse("The field list for FULL must not ends with ',' : " + list,
		list.endsWith(","));

    }

    @Test
    public void getFieldListshouldbeConsistant() {
	assertEquals(OutputStyle.getDefault()
		.getFieldList(Output.DEFAULT_LANGUAGE_CODE), Output
		.withDefaultFormat().getFields());
	// with style
	assertEquals(OutputStyle.FULL.getFieldList(null), Output
		.withDefaultFormat().withStyle(OutputStyle.FULL).getFields());
	// with style and language
	assertEquals(OutputStyle.FULL.getFieldList("FR"), Output
		.withDefaultFormat().withStyle(OutputStyle.FULL)
		.withLanguageCode("FR").getFields());
    }

}
