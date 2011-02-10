package com.gisgraphy.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.ServletTester;



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

}
