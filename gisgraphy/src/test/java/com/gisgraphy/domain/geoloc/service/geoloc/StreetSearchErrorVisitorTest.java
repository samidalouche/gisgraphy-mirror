package com.gisgraphy.domain.geoloc.service.geoloc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import net.sf.jstester.JsTester;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.test.FeedChecker;


public class StreetSearchErrorVisitorTest {
    
    private String errorMessage = "My Message";

    @Test
    public void testStreetSearchErrorVisitorString() {
	IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	Assert.assertEquals("The error message is not well set ",errorMessage, StreetSearchErrorVisitor.getErrorMessage());
    }
    
    @Test
    public void testStreetSearchErrorVisitor() {
	IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor();
	Assert.assertEquals("An error message should be provided when no message is specified ",IoutputFormatVisitor.DEFAULT_ERROR_MESSAGE, StreetSearchErrorVisitor.getErrorMessage());
    }

    @Test
    public void testVisitXML() {

	    IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	    String result = StreetSearchErrorVisitor.visitXML(OutputFormat.XML);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }


    @Test
    public void testVisitJSON() {
	JsTester jsTester = null;
	try {
	    jsTester = new JsTester();
	    jsTester.onSetUp();
	    IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	    String result = StreetSearchErrorVisitor.visitJSON(OutputFormat.JSON);

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
	IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	    String result = StreetSearchErrorVisitor.visitPYTHON(OutputFormat.PYTHON);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitRUBY() {
	IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	    String result = StreetSearchErrorVisitor.visitRUBY(OutputFormat.RUBY);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitPHP() {
	IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	    String result = StreetSearchErrorVisitor.visitPHP(OutputFormat.PHP);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitATOM() {
	IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	    String result = StreetSearchErrorVisitor.visitATOM(OutputFormat.ATOM);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }

    @Test
    public void testVisitGEORSS() {
	IoutputFormatVisitor StreetSearchErrorVisitor = new StreetSearchErrorVisitor(errorMessage);
	    String result = StreetSearchErrorVisitor.visitGEORSS(OutputFormat.GEORSS);
	    
	    FeedChecker.assertQ("The XML error is not correct", result, "//error[.='" + errorMessage + "']");
    }


}
