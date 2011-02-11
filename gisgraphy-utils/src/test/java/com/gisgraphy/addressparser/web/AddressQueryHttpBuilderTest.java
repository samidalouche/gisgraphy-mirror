package com.gisgraphy.addressparser.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.GisgraphyServlet;


public class AddressQueryHttpBuilderTest {
    
    @Test
    public void buildFromRequest(){
    AddressQueryHttpBuilder builder = AddressQueryHttpBuilder.getInstance();
	MockHttpServletRequest request = new MockHttpServletRequest();
	//without parameters
	try {
		builder.buildFromRequest(request);
		Assert.fail("without parameters the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	//country
	//without country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	try {
		builder.buildFromRequest(request);
		Assert.fail("without coutry parameter the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, " ");
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty country equals to space, the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "");
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty string the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//address
	//without address
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "US");
	try {
		builder.buildFromRequest(request);
		Assert.fail("without coutry parameter the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	//with empty address
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, " ");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty country equals to space, the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//with empty country
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty string the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//with too long address country
	request = new MockHttpServletRequest();
	String tooLongAddress = RandomStringUtils.random(AbstractAddressParserServlet.QUERY_MAX_LENGTH+1);
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, tooLongAddress);
	try {
		builder.buildFromRequest(request);
		Assert.fail("with empty string the builder should throws");
	} catch (AddressParserException e) {
		//ignore
	}
	
	//all ok
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	AddressQuery query = builder.buildFromRequest(request);
	Assert.assertEquals("address", query.getAddress());
	Assert.assertEquals("us", query.getCountry());
	
	// test outputFormat
	// with no value specified
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	query = builder.buildFromRequest(request);
	assertEquals("When no " + AbstractAddressParserServlet.OUTPUT_FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getFormat());
	// with wrong value
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.OUTPUT_FORMAT_PARAMETER, "UNK");
	query =builder.buildFromRequest(request);
	assertEquals("When wrong " + AbstractAddressParserServlet.OUTPUT_FORMAT_PARAMETER
		+ " is specified, the  parameter should be set to  "
		+ OutputFormat.getDefault(), OutputFormat.getDefault(), query
		.getFormat());
	// test case sensitive
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.OUTPUT_FORMAT_PARAMETER, "json");
	query =builder.buildFromRequest(request);
	assertEquals(AbstractAddressParserServlet.OUTPUT_FORMAT_PARAMETER
		+ " should be case insensitive  ", OutputFormat.JSON, query
		.getFormat());
	
	request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.OUTPUT_FORMAT_PARAMETER, "unsupported");
	query =builder.buildFromRequest(request);
    assertEquals(GisgraphyServlet.FORMAT_PARAMETER
	    + " should set default if not supported  ", OutputFormat.getDefault(), query
	    .getFormat());
    

    
    //test indent
    // with no value specified
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertEquals("When no " + AbstractAddressParserServlet.INDENT_PARAMETER
	    + " is specified, the  parameter should be set to default indentation",AddressQuery.DEFAULT_INDENTATION
	    ,query.isIndent());
    // with wrong value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.INDENT_PARAMETER, "unk");
	query =builder.buildFromRequest(request);
    assertEquals("When wrong " + AbstractAddressParserServlet.INDENT_PARAMETER
	    + " is specified, the  parameter should be set to default value",AddressQuery.DEFAULT_INDENTATION,
	    query.isIndent());
    // test case sensitive
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.INDENT_PARAMETER, "TrUe");
	query =builder.buildFromRequest(request);
    assertTrue(AbstractAddressParserServlet.INDENT_PARAMETER
	    + " should be case insensitive  ", query.isIndent());
    // test 'on' value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.INDENT_PARAMETER, "On");
	query =builder.buildFromRequest(request);
    assertTrue(
    		AbstractAddressParserServlet.INDENT_PARAMETER
		    + " should be true for 'on' value (case insensitive and on value)  ",
	    query.isIndent());

    //callback not set
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	query =builder.buildFromRequest(request);
    assertNull("callback should be null when not set",
	     query.getCallback());
    
    //callback set with non alpha value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.CALLBACK_PARAMETER, "doit(");
	query =builder.buildFromRequest(request);
    assertNull("callback should not be set when not alphanumeric",
	     query.getCallback());
    
    //callback set with alpha value
    request = new MockHttpServletRequest();
	request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
	request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
	request.setParameter(AbstractAddressParserServlet.CALLBACK_PARAMETER, "doit");
	query =builder.buildFromRequest(request);
    assertEquals("callback should not be set when alphanumeric",
	     "doit",query.getCallback());
	
    }

}
