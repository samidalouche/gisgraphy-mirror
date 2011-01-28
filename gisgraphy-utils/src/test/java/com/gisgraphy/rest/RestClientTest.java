package com.gisgraphy.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;
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
		servletTester.addServlet(MockServlet.class, "/*");
		servletTester.start();
		String responseString = servletTester.getResponses("GET /test HTTP/1.0\r\n\r\n");
		final HttpTester response = new HttpTester();
		response.parse(responseString);
		String content = response.getContent();
		System.out.println(content);
		InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
		BeanForTest beanForTestActual = UniversalSerializer.getInstance().read(inputStream, BeanForTest.class, OutputFormat.JSON);
		Assert.assertEquals(beanForTest, beanForTestActual);

	}

}
