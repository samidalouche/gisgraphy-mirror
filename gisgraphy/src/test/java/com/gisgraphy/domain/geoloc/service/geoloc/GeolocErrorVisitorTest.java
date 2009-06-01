package com.gisgraphy.domain.geoloc.service.geoloc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import net.sf.jstester.JsTester;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.test.FeedChecker;

public class GeolocErrorVisitorTest {

    private String errorMessage = "My Message";

    @Test
    public void testGeolocErrorVisitorString() {
	IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	Assert.assertEquals("The error message is not well set ",errorMessage, geolocErrorVisitor.getErrorMessage());
    }
    
    @Test
    public void testGeolocErrorVisitor() {
	IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor();
	Assert.assertEquals("An error message should be provided when no message is specified ",IoutputFormatVisitor.DEFAULT_ERROR_MESSAGE, geolocErrorVisitor.getErrorMessage());
    }

    @Test
    public void testVisitXML() {

	    IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	    String result = geolocErrorVisitor.visitXML(OutputFormat.XML);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }


    @Test
    public void testVisitJSON() {
	JsTester jsTester = null;
	try {
	    jsTester = new JsTester();
	    jsTester.onSetUp();
	    IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	    String result = geolocErrorVisitor.visitJSON(OutputFormat.JSON);

	    // JsTester
	    jsTester.eval("evalresult= eval(" + result + ");");
	    jsTester.assertNotNull("evalresult");
	    String error = jsTester.eval("evalresult.error").toString();

	    assertEquals(errorMessage, error);

	} catch (Exception e) {
	    fail("An exception has occured " + e.getMessage());
	} finally {
	    if (jsTester != null) {
		jsTester.onTearDown();
	    }

	}

    }

    @Test
    public void testVisitPYTHON() {
	IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	    String result = geolocErrorVisitor.visitPYTHON(OutputFormat.PYTHON);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitRUBY() {
	IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	    String result = geolocErrorVisitor.visitRUBY(OutputFormat.RUBY);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitPHP() {
	IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	    String result = geolocErrorVisitor.visitPHP(OutputFormat.PHP);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitATOM() {
	IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	    String result = geolocErrorVisitor.visitATOM(OutputFormat.ATOM);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitGEORSS() {
	IoutputFormatVisitor geolocErrorVisitor = new GeolocErrorVisitor(errorMessage);
	    String result = geolocErrorVisitor.visitGEORSS(OutputFormat.GEORSS);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

}