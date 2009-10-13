package com.gisgraphy.service.impl;

import java.util.Locale;

import junit.framework.Assert;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.gisgraphy.domain.repository.AbstractTransactionalTestCase;

public class InternationalisationServiceTest extends AbstractTransactionalTestCase {

    private InternationalisationService internationalisationService;

    private ReloadableResourceBundleMessageSource applicationResourcesSource;

    private ReloadableResourceBundleMessageSource createRessourceBundleTest() {
	ReloadableResourceBundleMessageSource testBundle = new ReloadableResourceBundleMessageSource();
	testBundle.setBasename("ApplicationResources");
	testBundle.setFallbackToSystemLocale(false);
	return testBundle;
    }
    
     public void testsetLocale() {
	try {
	    internationalisationService.setResourceBundle(createRessourceBundleTest());
	    internationalisationService.setLocale(Locale.FRANCE);
	    Assert.assertEquals(Locale.FRANCE, internationalisationService.getLocale());
	    internationalisationService.setLocale(Locale.ENGLISH);
	    Assert.assertEquals(Locale.ENGLISH, internationalisationService.getLocale());
	} finally {
	    assertNotNull("could not restore applicationRessource because it's null", applicationResourcesSource);
	    internationalisationService.setResourceBundle(applicationResourcesSource);
	}
    }

 

    public void testGetString() {
	try {
	    internationalisationService.setResourceBundle(createRessourceBundleTest());
	    internationalisationService.setLocale(Locale.ENGLISH);
	    Assert.assertEquals(Locale.ENGLISH, internationalisationService.getLocale());
	    Assert.assertEquals("Help for test", internationalisationService.getString("global.help"));

	    internationalisationService.setLocale(Locale.FRANCE);
	    Assert.assertEquals("Aide for testé", internationalisationService.getString("global.help"));
	} finally {
	    assertNotNull("could not restore applicationRessource because it's null", applicationResourcesSource);
	    internationalisationService.setResourceBundle(applicationResourcesSource);
	}
    }

    public void testGetStringWithParams() {
	try {
	    internationalisationService.setResourceBundle(createRessourceBundleTest());
	    String param = "myParam";
	    internationalisationService.setLocale(Locale.ENGLISH);
	    Assert.assertEquals(param, internationalisationService.getString("errors.detail", new String[] { param }));

	    internationalisationService.setLocale(Locale.FRANCE);
	    Assert.assertEquals(param, internationalisationService.getString("errors.detail", new String[] { param }));
	} finally {
	    assertNotNull("could not restore applicationRessource because it's null", applicationResourcesSource);
	    internationalisationService.setResourceBundle(applicationResourcesSource);
	}
    }

    public void testGetStringWithParamsWithUnknowKey() {
	try {
	    internationalisationService.setResourceBundle(createRessourceBundleTest());
	    String param = "myParam";
	    String unknowKey = "foo";
	    internationalisationService.setLocale(Locale.ENGLISH);
	    Assert.assertEquals(unknowKey, internationalisationService.getString(unknowKey, new String[] { param }));

	    internationalisationService.setLocale(Locale.FRANCE);
	    Assert.assertEquals(unknowKey, internationalisationService.getString(unknowKey, new String[] { param }));
	} finally {
	    assertNotNull("could not restore applicationRessource because it's null", applicationResourcesSource);
	    internationalisationService.setResourceBundle(applicationResourcesSource);
	}
    }

    public void testGetStringWithUnknowKey() {
	try {
	    internationalisationService.setResourceBundle(createRessourceBundleTest());
	    String unknowKey = "foo";
	    internationalisationService.setLocale(Locale.ENGLISH);
	    Assert.assertEquals(unknowKey, internationalisationService.getString(unknowKey));

	    internationalisationService.setLocale(Locale.FRANCE);
	    Assert.assertEquals(unknowKey, internationalisationService.getString(unknowKey));
	} finally {
	    assertNotNull("could not restore applicationRessource because it's null", applicationResourcesSource);
	    internationalisationService.setResourceBundle(applicationResourcesSource);
	}
    }

    /**
     * @param internationalisationService
     *            the internationalisationService to set
     */
    public void setInternationalisationService(InternationalisationService internationalisationService) {
	this.internationalisationService = internationalisationService;
    }

    /**
     * @param applicationResourcesSource
     *            the applicationResourcesSource to set
     */
    public void setApplicationResourcesSource(ReloadableResourceBundleMessageSource applicationResourcesSource) {
	this.applicationResourcesSource = applicationResourcesSource;
    }

}
