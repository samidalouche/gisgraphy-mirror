package com.gisgraphy.addressparser.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.ServletTester;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.IAddressParserService;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.helper.OutputFormatHelper;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.ConcreteAddressParserServlet;
import com.gisgraphy.servlet.GisgraphyMockServlet;
import com.gisgraphy.servlet.GisgraphyServlet;


public class AbstractAddressParserServletTest {

	private boolean customErrorSended = false;
	private IAddressParserService mockAddressParserService;
	private AddressQueryHttpBuilder mockAddressQueryHttpBuilder;
	
  
	
	@Before
	public void setup(){
		customErrorSended = false;
		mockAddressParserService = EasyMock.createNiceMock(IAddressParserService.class);
	    mockAddressParserService.executeAndSerialize((AddressQuery)EasyMock.anyObject(),(OutputStream)EasyMock.anyObject());
		EasyMock.replay(mockAddressParserService);
		
		mockAddressQueryHttpBuilder = EasyMock.createNiceMock(AddressQueryHttpBuilder.class);
		EasyMock.expect(mockAddressQueryHttpBuilder.buildFromRequest((HttpServletRequest)EasyMock.anyObject())).andReturn(new AddressQuery("address","us"));
		EasyMock.replay(mockAddressQueryHttpBuilder);
		
	}
	
	  @Test
	    public void initShouldTakeTheDebugParameterWhenTrue() throws Exception {

		ServletTester servletTester = new ServletTester();
		ServletHolder sh = servletTester.addServlet(ConcreteAddressParserServlet.class, "/*");
		sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "true");
		servletTester.start();
		ConcreteAddressParserServlet servlet = (ConcreteAddressParserServlet) sh.getServlet();
		Assert.assertTrue(servlet.isDebugMode());
		servletTester.stop();

	    }

	    @Test
	    public void initShouldTakeTheDebugParameterWhenIncorrect() throws Exception {

		ServletTester servletTester = new ServletTester();
		ServletHolder sh = servletTester.addServlet(ConcreteAddressParserServlet.class, "/*");
		sh.setInitParameter(GisgraphyServlet.DEBUG_MODE_PARAMETER_NAME, "foo");
		servletTester.start();
		ConcreteAddressParserServlet servlet = (ConcreteAddressParserServlet) sh.getServlet();
		Assert.assertFalse(servlet.isDebugMode());
		servletTester.stop();

	    }

	    @Test
	    public void debugParameterShouldBeFalse() throws Exception {

		ServletTester servletTester = new ServletTester();
		ServletHolder sh = servletTester.addServlet(ConcreteAddressParserServlet.class, "/*");
		servletTester.start();
		ConcreteAddressParserServlet servlet = (ConcreteAddressParserServlet) sh.getServlet();
		Assert.assertFalse(servlet.isDebugMode());
	    }
	
	private AbstractAddressParserServlet servlet = new AbstractAddressParserServlet(){
		public com.gisgraphy.addressparser.IAddressParserService getAddressParserService() {return mockAddressParserService;};
		@Override
		public void sendCustomError(String arg0, OutputFormat arg1, HttpServletResponse arg2, HttpServletRequest arg3) {
			customErrorSended = true;
		}
		@Override
		protected AddressQueryHttpBuilder getAddressQueryHttpBuilder() {
			return mockAddressQueryHttpBuilder;
		}
	};
	
	@Test
	public void doGetWithoutParameter() throws AddressParserException, IOException{
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		servlet.doGet(request, response);
		Assert.assertTrue(customErrorSended);
		
	}
	
	@Test
	public void doGetWithoutAddressParameter() throws AddressParserException, IOException{
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
		servlet.doGet(request, response);
		Assert.assertTrue(customErrorSended);
		
	}
	
	@Test
	public void doGetWithoutCountryParameter() throws AddressParserException, IOException{
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
		servlet.doGet(request, response);
		Assert.assertTrue(customErrorSended);
		
	}
	
	@Test
	public void doGetWithRequiredParameter() throws AddressParserException, IOException{
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
		request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
		servlet.doGet(request, response);
		Assert.assertFalse(customErrorSended);
		EasyMock.verify(mockAddressParserService);
		EasyMock.verify(mockAddressQueryHttpBuilder);
		
	}
	
	 @Test
	    public void testFulltextServletShouldReturnCorrectContentType() {

		for (OutputFormat format : OutputFormat.values()) {
		    try {
		    	MockHttpServletResponse response = new MockHttpServletResponse();
				
				MockHttpServletRequest request = new MockHttpServletRequest();
				request.setParameter(AbstractAddressParserServlet.ADDRESS_PARAMETER, "address");
				request.setParameter(AbstractAddressParserServlet.COUNTRY_PARAMETER, "us");
				request.setParameter(AbstractAddressParserServlet.FORMAT_PARAMETER, format.name());
				servlet.doGet(request, response);

			String contentType = (String) response.getHeader("Content-Type");
			if (OutputFormatHelper.isFormatSupported(format, GisgraphyServiceType.ADDRESS_PARSER)){
			Assert.assertEquals(format.getContentType(), contentType);
			}
		    } catch (IOException e) {
			Assert.fail("An exception has occured " + e.getMessage());
		    }
		}

	    }

}
