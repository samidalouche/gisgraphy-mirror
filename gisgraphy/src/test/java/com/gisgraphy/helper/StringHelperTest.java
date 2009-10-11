package com.gisgraphy.helper;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.test.GeolocTestHelper;

public class StringHelperTest {

    @Test
    public void testTransformStringForFulltextIndexation() {
	Assert.assertEquals("letter without accent should not be modified"," e e e e e je me souviens de ce zouave qui jouait du xylophone en buvant du whisky c est ok c est super",
		StringHelper.transformStringForFulltextIndexation("-é \u00E8 \u00E9 \u00EA \u00EB JE ME SOUVIENS de ce zouave qui jouait du-xylophone en buvant.du whisky c'est ok c\"est super "));
    }
    
    @Test
    public void TransformStringForFulltextIndexationForNullString() {
	Assert.assertNull(StringHelper.transformStringForFulltextIndexation(null));
    }
    
    @Test
    public void TransformStringForIlikeIndexationForNullString(){
    	Assert.assertNull(StringHelper.transformStringForPartialWordIndexation(null,'_'));
    }
    
    @Test
    public void TransformStringForIlikeIndexation(){
	char delimiter ='-';
	String transformedString = StringHelper.transformStringForPartialWordIndexation("it s ok;",delimiter);
	String[] splited = transformedString.split(String.valueOf(" "));
	List<String> list =Arrays.asList(splited);
	//s ok, s o, it s, t s o, t s, it s ok, ok, it s o, it, t s ok
	Assert.assertEquals("There is not the number of words expected, maybe there is duplicate, or single char are indexed but should not, or ..., here is the tansformed string :"+transformedString,10, list.size());
    	Assert.assertTrue(list.contains("it-s-ok"));
    	Assert.assertTrue(list.contains("it"));
    	Assert.assertTrue(list.contains("it-s"));
    	Assert.assertTrue(list.contains("it-s-o"));
    	Assert.assertTrue(list.contains("t-s"));
    	Assert.assertTrue(list.contains("t-s-o"));
    	Assert.assertTrue(list.contains("t-s-ok"));
    	Assert.assertTrue(list.contains("s-o"));
    	Assert.assertTrue(list.contains("s-ok"));
    	Assert.assertTrue(list.contains("ok"));
    }
    
    @Test
    public void TransformStringForIlikeIndexationWithSpecialChar(){
	char delimiter ='-';
	String transformedString = StringHelper.transformStringForPartialWordIndexation("it's ok",delimiter);
	String[] splited = transformedString.split(String.valueOf(" "));
	List<String> list =Arrays.asList(splited);
	//s ok, s o, it s, t s o, t s, it s ok, ok, it s o, it, t s ok
	Assert.assertEquals("There is not the number of words expected, maybe there is duplicate, or single char are indexed but should not, or ..., here is the tansformed string :"+transformedString,10, list.size());
    	Assert.assertTrue(list.contains("it-s-ok"));
    	Assert.assertTrue(list.contains("it"));
    	Assert.assertTrue(list.contains("it-s"));
    	Assert.assertTrue(list.contains("it-s-o"));
    	Assert.assertTrue(list.contains("t-s"));
    	Assert.assertTrue(list.contains("t-s-o"));
    	Assert.assertTrue(list.contains("t-s-ok"));
    	Assert.assertTrue(list.contains("s-o"));
    	Assert.assertTrue(list.contains("s-ok"));
    	Assert.assertTrue(list.contains("ok"));
    }
    
    @Test
    public void transformStringForPartialWordIndexationWithLongString(){
	char delimiter ='-';
	String longString = RandomStringUtils.random(StringHelper.MAX_STRING_INDEXABLE_LENGTH+1,new char[] {'e'});
	Assert.assertEquals("the string to test is not of the expected size the test will fail",StringHelper.MAX_STRING_INDEXABLE_LENGTH+1, longString.length());
	String transformedString = StringHelper.transformStringForPartialWordIndexation(longString,delimiter);
	Assert.assertNull("string that are longer than "+StringHelper.MAX_STRING_INDEXABLE_LENGTH+" should return null",transformedString);
    }
    
    
    
    
    @Test
    public void transformStringForIlikeSearch(){
	char delimiter ='-';
	String transformedString = StringHelper.transformStringForPartialWordSearch("C'est-tr\u00E9s ",delimiter);
	Assert.assertEquals("c-est-tres", transformedString);
    }
    
    
    @Test
    public void updateOpenStreetMapEntityForIndexation(){
	OpenStreetMap openStreetMap = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	//we reset textsearch name and partial search name
	openStreetMap.setPartialSearchName(null);
	openStreetMap.setTextSearchName(null);
	StringHelper.updateOpenStreetMapEntityForIndexation(openStreetMap);
	Assert.assertEquals("The value of partial search name is not correct",StringHelper.transformStringForPartialWordIndexation(openStreetMap.getName(),StringHelper.WHITESPACE_CHAR_DELIMITER), openStreetMap.getPartialSearchName());
	Assert.assertEquals("The value of text search name is not correct",StringHelper.transformStringForFulltextIndexation(openStreetMap.getName()), openStreetMap.getTextSearchName());
    }
    
    @Test
    public void updateOpenStreetMapEntityForIndexationWithANullName(){
	OpenStreetMap openStreetMap = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	openStreetMap.setName(null);
	openStreetMap.setPartialSearchName(null);
	openStreetMap.setTextSearchName(null);
	StringHelper.updateOpenStreetMapEntityForIndexation(openStreetMap);
	Assert.assertNull("The value of partial search name should be null if name is null", openStreetMap.getPartialSearchName());
	Assert.assertNull("The value of text search name should be null if name is null", openStreetMap.getTextSearchName());
    }
    
    @Test
    public void updateOpenStreetMapEntityForIndexationWithALongName(){
	OpenStreetMap openStreetMap = GeolocTestHelper.createOpenStreetMapForPeterMartinStreet();
	String longName = RandomStringUtils.random(StringHelper.MAX_STRING_INDEXABLE_LENGTH+1,new char[] {'e'});
	openStreetMap.setName(longName);
	openStreetMap.setPartialSearchName(null);
	openStreetMap.setTextSearchName(null);
	StringHelper.updateOpenStreetMapEntityForIndexation(openStreetMap);
	Assert.assertNull("The value of partial search should be null if name is too long", openStreetMap.getPartialSearchName());
	Assert.assertEquals("The value of text search name should not be null and correct if name is too long",StringHelper.transformStringForFulltextIndexation(openStreetMap.getName()), openStreetMap.getTextSearchName());
    }
    
    


}
