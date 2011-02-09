package com.gisgraphy.addressparser;

import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.serializer.OutputFormat;


public class RemoteAddressParserServiceTest {
    
    @Test
    public void getBaseUrlShouldReturnTheConstructor(){
	String baseURL="baseURL";
	RemoteAddressParserService service = new RemoteAddressParserService(baseURL);
	Assert.assertEquals(baseURL, service.getBaseURL());
    }
    
    @Test
    public void defaultConstructor(){
	RemoteAddressParserService service = new RemoteAddressParserService();
	Assert.assertEquals(RemoteAddressParserService.DEFAULT_ADDRESS_PARSER_BASE_URL, service.getBaseURL());
    }
    
    @Test
    public void execute(){
	 final String url = "URL";
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	EasyMock.expect(mockRestClient.get(url, AddressResultsDto.class, OutputFormat.JSON)).andReturn(null);
	EasyMock.replay(mockRestClient);
	RemoteAddressParserService service = new RemoteAddressParserService(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.execute(query);
	EasyMock.verify(mockRestClient);
    }
    
    @Test
    public void executeAndSerialize(){
	 final String url = "URL";
	 final OutputStream outputStream = new ByteArrayOutputStream();
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	mockRestClient.get(url, outputStream,OutputFormat.JSON);
	EasyMock.replay(mockRestClient);
	RemoteAddressParserService service = new RemoteAddressParserService(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.executeAndSerialize(query,outputStream);
	EasyMock.verify(mockRestClient);
    }
    
    @Test
    public void executeToString(){
	 final String url = "URL";
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	mockRestClient.get((String)EasyMock.anyObject(), (OutputStream)EasyMock.anyObject(),(OutputFormat)EasyMock.anyObject());
	EasyMock.replay(mockRestClient);
	RemoteAddressParserService service = new RemoteAddressParserService(){
	    @Override
	    protected IRestClient getRestClient() {
	        return mockRestClient;
	    }
	    
	    @Override
	    protected String getUrl(AddressQuery query) {
	    return url;
	    }
	};
	AddressQuery query =new AddressQuery("rawAddress", "US");
	service.executeToString(query);
	EasyMock.verify(mockRestClient);
    }
    
    
    @Test(expected=AddressParserException.class)
    public void executeWithNullQuery(){
	RemoteAddressParserService service = new RemoteAddressParserService();
	service.execute(null);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeAndSerializeWithNullQuery(){
	RemoteAddressParserService service = new RemoteAddressParserService();
	service.executeAndSerialize(null,new ByteArrayOutputStream());
    }
    
    @Test(expected=AddressParserException.class)
    public void executeAndSerializeWithNullOutputStream(){
	RemoteAddressParserService service = new RemoteAddressParserService();
	service.executeAndSerialize(new AddressQuery("rawAddress", "us"),null);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeToStringWithNullQuery(){
	RemoteAddressParserService service = new RemoteAddressParserService();
	service.executeToString(null);
    }

}
