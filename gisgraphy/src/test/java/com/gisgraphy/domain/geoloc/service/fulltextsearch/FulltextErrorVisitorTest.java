package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import net.sf.jstester.JsTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.errors.IoutputFormatVisitor;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.test.FeedChecker;


public class FulltextErrorVisitorTest  {
    
    private String errorMessage = "My Message";
    IoutputFormatVisitor FulltextErrorVisitor;
    

    @Before
    public void onSetUp() throws Exception {
	FulltextErrorVisitor = new FulltextErrorVisitor(errorMessage);
    }
    
    
    @Test
    public void testFulltextErrorVisitorString() {
	IoutputFormatVisitor FulltextErrorVisitor = new FulltextErrorVisitor(errorMessage);
	Assert.assertEquals("The error message is not well set ",errorMessage, FulltextErrorVisitor.getErrorMessage());
    }
    
    @Test
    public void testFulltextErrorVisitor() {
	IoutputFormatVisitor FulltextErrorVisitor = new FulltextErrorVisitor();
	Assert.assertEquals("An error message should be provided when no message is specified ",IoutputFormatVisitor.DEFAULT_ERROR_MESSAGE, FulltextErrorVisitor.getErrorMessage());
    }

    @Test
    public void testVisitXML() {

	    String result = FulltextErrorVisitor.visitXML(OutputFormat.XML);
	    
	    FeedChecker.checkFulltextErrorXML(result,errorMessage);
    }


    @Test
    public void testVisitJSON() {
	JsTester jsTester = null;
	IoutputFormatVisitor FulltextErrorVisitor = new FulltextErrorVisitor(errorMessage);
	String result = FulltextErrorVisitor.visitJSON(OutputFormat.JSON);
	try {
	    jsTester = new JsTester();
	    jsTester.onSetUp();

	    // JsTester
	    jsTester.eval("evalresult= eval(" + result + ");");
	    String error = jsTester.eval("evalresult.responseHeader.error")
	    .toString();
   
	    assertEquals(errorMessage, error);

	    error = jsTester.eval("evalresult.responseHeader.status")
	    .toString();
	    Assert.assertEquals("-1.0", error);// -1.0 because it is considered as a
	    // float

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
	    String result = FulltextErrorVisitor.visitPYTHON(OutputFormat.PYTHON);
	    checkErrorMessageIsPresentInOutputStream(result);
    }




    @Test
    public void testVisitRUBY() {
	    String result = FulltextErrorVisitor.visitRUBY(OutputFormat.RUBY);
	    checkErrorMessageIsPresentInOutputStream(result);
    }

    @Test
    public void testVisitPHP() {
	    String result = FulltextErrorVisitor.visitPHP(OutputFormat.PHP);
	    checkErrorMessageIsPresentInOutputStream(result);
	    
    }

    @Test
    public void testVisitATOM() {
	    String result = FulltextErrorVisitor.visitATOM(OutputFormat.ATOM);
	    
	    FeedChecker.checkFulltextErrorXML(result,errorMessage);
    }

    @Test
    public void testVisitGEORSS() {
	    String result = FulltextErrorVisitor.visitGEORSS(OutputFormat.GEORSS);
	    
	    FeedChecker.checkFulltextErrorXML(result,errorMessage);
    }

    private void checkErrorMessageIsPresentInOutputStream(String result) {
	Assert.assertTrue("the error Message should contains the error Message", result.contains(errorMessage));
    }


   


}
