package com.gisgraphy.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.junit.Assert;
import org.junit.Test;
import org.mortbay.jetty.LocalConnector;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.serializer.UniversalSerializer;
import com.gisgraphy.serializer.testdata.BeanForTest;
import com.gisgraphy.serializer.testdata.TestUtils;

public class RestClientTest {

	BeanForTest beanForTest = TestUtils.createBeanForTest();

	@Test
	public void getWithStatusOK() throws Exception {
		ServletTester servletTester = new ServletTester();
		String url = servletTester.createSocketConnector(true);
		System.out.println(url);
		servletTester.addServlet(MockServlet.class, "/*");
		servletTester.start();
		RestClient restClient = new RestClient();
		BeanForTest beanForTestActual =  restClient.get(url+"/test",BeanForTest.class, OutputFormat.JSON);
		Assert.assertEquals(beanForTest, beanForTestActual);

	}

}
