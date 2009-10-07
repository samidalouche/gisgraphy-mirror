package com.gisgraphy.helper;

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
    	Assert.assertNull(StringHelper.TransformStringForIlikeIndexation(null));
    }
    
    @Test
    public void TransformStringForIlikeIndexation(){
    	//Assert.assertEquals("it_it s_it s o_it s ok",StringHelper.TransformStringForIlikeIndexation("it s ok"));
    }


}
