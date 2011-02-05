package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;


public class OutputStyleHelperTest {
    
    OutputStyleHelper outputStyleHelper = new OutputStyleHelper();
    
    @Test
    public void AllOutputStyleShouldBeImplemented(){
    	for (OutputStyle outputStyle:OutputStyle.values()){
    		try {
				outputStyleHelper.getFulltextFieldList(outputStyle, "fr");
			} catch (RuntimeException e) {
				Assert.fail("getfulltextfield is not implemented for "+outputStyle);
			}
    	}
	
    }

    @Test
    public void outputStyleGetFieldListForShortShouldBeCorrect() {
	String list = outputStyleHelper.getFulltextFieldList(OutputStyle.SHORT,null);
	assertEquals("The field list has a wrong size for SHORT :" + list, 8,
		list.split(",").length);
	assertFalse(
		"The field list for SHORT must not contains ',,' : " + list,
		list.contains(",,"));
	assertFalse(
		"The field list for SHORT must not ends with ',' : " + list,
		list.endsWith(","));
	list = outputStyleHelper.getFulltextFieldList(OutputStyle.SHORT,"fr");
	assertEquals(
		"The field list for SHORT should not be different for a specified country:"
			+ list, 8, list.split(",").length);
	assertFalse(
		"The field list for SHORT must not ends with ',' : " + list,
		list.endsWith(","));

    }

    @Test
    public void outputStyleGetFieldListForMediumShouldBeCorrect() {
	String list = outputStyleHelper.getFulltextFieldList(OutputStyle.MEDIUM,null);
	assertEquals("The field list has a wrong size for MEDIUM :" + list, 34,
		list.split(",").length);
	assertFalse("The field list for MEDIUM must not contains ',,' : "
		+ list, list.contains(",,"));
	assertFalse("The field list for MEDIUM must not ends with ',' : "
		+ list, list.endsWith(","));
	list = outputStyleHelper.getFulltextFieldList(OutputStyle.MEDIUM,"fr");
	assertEquals(
		"The field list for MEDIUM should not be different for a specified country : "
			+ list, 34, list.split(",").length);
	assertFalse("The field list for MEDIUM must not ends with ',' : "
		+ list, list.endsWith(","));

    }

    @Test
    public void outputStyleGetFieldListForLongShouldBeCorrect() {
	String list =outputStyleHelper.getFulltextFieldList(OutputStyle.LONG,null);
	assertEquals("The field list has a wrong size for LONG :" + list, 42,
		list.split(",").length);
	assertFalse("The field list for LONG must not contains ',,' : " + list,
		list.contains(",,"));
	assertFalse("The field list for LONG must not ends with ',' : " + list,
		list.endsWith(","));
	list = outputStyleHelper.getFulltextFieldList(OutputStyle.LONG,"fr");
	assertEquals(
		"The field list for LONG should not be different for a specified country : "
			+ list, 42, list.split(",").length);
	assertFalse("The field list for LONG must not ends with ',' : " + list,
		list.endsWith(","));

    }

    @Test
    public void outputStyleGetFieldListForFullShouldBeCorrect() {
	String list = outputStyleHelper.getFulltextFieldList(OutputStyle.FULL,null);
	assertEquals(
		"The field list has a wrong size for FULL without countryCode :"
			+ list, 2, list.split(",").length);
	assertFalse("The field list for FULL must not contains ',,' : " + list,
		list.contains(",,"));
	assertFalse("The field list for FULL must not ends with ',' : " + list,
		list.endsWith(","));
	list = outputStyleHelper.getFulltextFieldList(OutputStyle.FULL,"fr");
	assertEquals(
		"The field list for medium should be different for a specified country :"
			+ list, 50, list.split(",").length);
	assertFalse("The field list for FULL must not ends with ',' : " + list,
		list.endsWith(","));

    }

    @Test
    public void getFieldListshouldbeConsistant() {
	// without style, without language 
	assertEquals(outputStyleHelper.getFulltextFieldList(OutputStyle.getDefault(),Output.DEFAULT_LANGUAGE_CODE), outputStyleHelper.getFulltextFieldList(Output
		.withDefaultFormat()));
	// with style without language
	assertEquals(outputStyleHelper.getFulltextFieldList(OutputStyle.FULL,null), outputStyleHelper.getFulltextFieldList(Output
		.withDefaultFormat().withStyle(OutputStyle.FULL)));
	// with style and language
	
	assertEquals(outputStyleHelper.getFulltextFieldList(OutputStyle.FULL,"FR"), outputStyleHelper.getFulltextFieldList(Output
		.withDefaultFormat().withStyle(OutputStyle.FULL).withLanguageCode("FR")));
    }
    
}
