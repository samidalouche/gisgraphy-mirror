package com.gisgraphy.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gisgraphy.domain.valueobject.Constants;

public class XpathChecker {

    
    /**
     * Validates a query matches some XPath test expressions and closes the
     * query
     */
    public static void assertQ(String message, String response, String... tests) {
	try {
	    String m = (null == message) ? "" : message + " ";
	    String results = XpathChecker.validateXPath(response, tests);
	    if (null != results) {
		throw new RuntimeException(m + "query failed XPath: " + results
			+ " xml response was: " + response);
	    }
	} catch (XPathExpressionException e1) {
	    throw new RuntimeException("XPath is invalid", e1);
	} catch (Exception e2) {
	    throw new RuntimeException("Exception during query", e2);
	}
    }
    
    /**
     * A helper method which valides a String against an array of XPath test
     * strings.
     * 
     * @param xml
     *                The xml String to validate
     * @param tests
     *                Array of XPath strings to test (in boolean mode) on the
     *                xml
     * @return null if all good, otherwise the first test that fails.
     */
    public static String validateXPath(String xml, String... tests)
	    throws XPathExpressionException, SAXException {

	if (tests == null || tests.length == 0) {
	    return null;
	}

	Document document = null;
	try {
	    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    document = builder.parse(new ByteArrayInputStream(xml
		    .getBytes(Constants.CHARSET)));
	    
	}
	 catch (ParserConfigurationException e) {
		    throw new RuntimeException ("error during initialisation of the DocumentBuilder ");
		}
	catch (UnsupportedEncodingException e1) {
	    throw new RuntimeException("Totally weird UTF-8 exception", e1);
	} catch (IOException e2) {
	    throw new RuntimeException("Totally weird io exception", e2);
	}

	XPath xpath = XPathFactory.newInstance().newXPath();
	for (String xp : tests) {
	    xp = xp.trim();
	    Boolean bool = (Boolean) xpath.evaluate(xp, document,
		    XPathConstants.BOOLEAN);

	    if (!bool) {
		return xp;
	    }
	}
	return null;

    }

    
}
