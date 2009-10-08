package com.gisgraphy.helper;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class StringHelperTest {

    @Test
    public void testTransformStringForFulltextIndexation() {
	Assert.assertEquals("letter without accent should not be modified"," e e e e e je me souviens de ce zouave qui jouait du xylophone en buvant du whisky c est ok c est super",
		StringHelper.TransformStringForFulltextIndexation("-é \u00E8 \u00E9 \u00EA \u00EB JE ME SOUVIENS de ce zouave qui jouait du-xylophone en buvant.du whisky c'est ok c\"est super"));
    }
    
    @Test
    public void TransformStringForFulltextIndexationForNullString() {
	Assert.assertNull(StringHelper.TransformStringForFulltextIndexation(null));
    }
    
    @Test
    public void TransformStringForIlikeIndexationForNullString(){
    	Assert.assertNull(StringHelper.TransformStringForIlikeIndexation(null,'_'));
    }
    
    @Test
    public void TransformStringForIlikeIndexation(){
	char delimiter ='_';
	String transformedString = StringHelper.TransformStringForIlikeIndexation("it s ok",delimiter);
	String[] splited = transformedString.split(String.valueOf(" "));
	List<String> list =Arrays.asList(splited);
	//s ok, s o, it s, t s o, t s, it s ok, ok, it s o, it, t s ok
	Assert.assertEquals("There is not the number of words expected, maybe there is duplicate, or single char are indexed but should not, or ..., here is the tansformed string :"+transformedString,10, list.size());
    	Assert.assertTrue(list.contains("it_s_ok"));
    	Assert.assertTrue(list.contains("it"));
    	Assert.assertTrue(list.contains("it_s"));
    	Assert.assertTrue(list.contains("it_s_o"));
    	Assert.assertTrue(list.contains("t_s"));
    	Assert.assertTrue(list.contains("t_s_o"));
    	Assert.assertTrue(list.contains("t_s_ok"));
    	Assert.assertTrue(list.contains("s_o"));
    	Assert.assertTrue(list.contains("s_ok"));
    	Assert.assertTrue(list.contains("ok"));
    }


}
