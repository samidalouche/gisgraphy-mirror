package com.gisgraphy.helper;

import junit.framework.Assert;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.entity.Forest;

public class ClassNameHelperTest {

    @Test
    public void StripEnhancerClassShouldReturnTheStripedClassName() {
	Assert.assertTrue("Adm".equalsIgnoreCase(ClassNameHelper.stripEnhancerClass("Adm$$EnhancerByCGLIB$$cf6539bc")));
	Assert.assertTrue("fdm".equalsIgnoreCase(ClassNameHelper.stripEnhancerClass("fdm$$EnhancerByCGLIB$$cf6539bc")));
	Assert.assertTrue("Forest".equalsIgnoreCase(ClassNameHelper.stripEnhancerClass(Forest.class.getSimpleName())));
    }

}
