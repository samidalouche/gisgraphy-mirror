package com.gisgraphy.client.addressparser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.easymock.classextension.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.addressparser.AddressParserClient;
import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.serializer.OutputFormat;


public class AddressParserClientTest {
    
    @Test
    public void getBaseUrlShouldReturnTheConstructor(){
	String baseURL="baseURL";
	AddressParserClient service = new AddressParserClient(baseURL);
	Assert.assertEquals(baseURL, service.getBaseURL());
    }
    
    @Test
    public void defaultConstructor(){
	AddressParserClient service = new AddressParserClient();
	Assert.assertEquals(AddressParserClient.DEFAULT_ADDRESS_PARSER_BASE_URL, service.getBaseURL());
    }
    
    @Test
    public void execute(){
	 final String url = "URL";
	final IRestClient mockRestClient = EasyMock.createMock(IRestClient.class);
	EasyMock.expect(mockRestClient.get(url, AddressResultsDto.class, OutputFormat.JSON)).andReturn(null);
	EasyMock.replay(mockRestClient);
	AddressParserClient service = new AddressParserClient(){
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
	AddressParserClient service = new AddressParserClient(){
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
	AddressParserClient service = new AddressParserClient(){
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
	AddressParserClient service = new AddressParserClient();
	service.execute(null);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeAndSerializeWithNullQuery(){
	AddressParserClient service = new AddressParserClient();
	service.executeAndSerialize(null,new ByteArrayOutputStream());
    }
    
    @Test(expected=AddressParserException.class)
    public void executeAndSerializeWithNullOutputStream(){
	AddressParserClient service = new AddressParserClient();
	service.executeAndSerialize(new AddressQuery("rawAddress", "us"),null);
    }
    
    @Test(expected=AddressParserException.class)
    public void executeToStringWithNullQuery(){
	AddressParserClient service = new AddressParserClient();
	service.executeToString(null);
    }

}
