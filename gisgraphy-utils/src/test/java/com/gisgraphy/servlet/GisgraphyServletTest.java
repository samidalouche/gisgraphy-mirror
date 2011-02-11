package com.gisgraphy.servlet;

import java.io.UnsupportedEncodingException;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.ServletTester;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.gisgraphy.serializer.IoutputFormatVisitor;
import com.gisgraphy.serializer.OutputFormat;



public class GisgraphyServletTest {
    
    @Test
    public void initShouldTakeTheDebugParameterWhenTrue() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(GisgraphyMockServlet.class, "/*");
	sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "true");
	servletTester.start();
	GisgraphyMockServlet servlet = (GisgraphyMockServlet) sh.getServlet();
	Assert.assertTrue(servlet.isDebugMode());
	servletTester.stop();

    }

    @Test
    public void initShouldTakeTheDebugParameterWhenIncorrect() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(GisgraphyMockServlet.class, "/*");
	sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "foo");
	servletTester.start();
	GisgraphyMockServlet servlet = (GisgraphyMockServlet) sh.getServlet();
	Assert.assertFalse(servlet.isDebugMode());
	servletTester.stop();

    }

    @Test
    public void debugParameterShouldBeFalseByDefault() throws Exception {

	ServletTester servletTester = new ServletTester();
	ServletHolder sh = servletTester.addServlet(GisgraphyMockServlet.class, "/*");
	servletTester.start();
	GisgraphyMockServlet servlet = (GisgraphyMockServlet) sh.getServlet();
	Assert.assertFalse(servlet.isDebugMode());
    }
    
    @Test
    public void sendCustomError() throws UnsupportedEncodingException{
	final IoutputFormatVisitor errorvisitor = EasyMock.createMock(IoutputFormatVisitor.class);
	String formatedErrorMessage = "formatedErrorMessage";
	String errorMessage="Basic error Message";
	EasyMock.expect(errorvisitor.visitJSON(OutputFormat.JSON)).andReturn(formatedErrorMessage);
	EasyMock.replay(errorvisitor);
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	response.setCommitted(true);
	GisgraphyMockServlet mockServlet = new GisgraphyMockServlet(){
	    public com.gisgraphy.serializer.IoutputFormatVisitor getErrorVisitor(String errorMessage) {
		return errorvisitor;
	    };
	};
	mockServlet.sendCustomError(errorMessage, OutputFormat.JSON, response, request);
	EasyMock.verify(errorvisitor);
	System.out.println(response.getContentAsString());
    }

}
