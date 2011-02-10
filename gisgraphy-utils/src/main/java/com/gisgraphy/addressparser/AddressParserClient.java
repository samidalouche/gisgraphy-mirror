package com.gisgraphy.addressparser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.gisgraphy.addressparser.AddressQuery;
import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.addressparser.IAddressParserService;
import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.rest.BeanToQueryString;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.OutputFormat;

public class AddressParserClient implements IAddressParserService {
    
    public static final String DEFAULT_ADDRESS_PARSER_BASE_URL = "http://addressparser.gisgraphy.com/services/parse";
    private String baseURL = DEFAULT_ADDRESS_PARSER_BASE_URL;
    
    public AddressParserClient() {
    }
    
    public AddressParserClient(String baseURL){
	this.baseURL =baseURL;
    }
    
    private RestClient restClient = new RestClient();
    
    protected IRestClient getRestClient(){
	return restClient;
    }

    public AddressResultsDto execute(AddressQuery addressQuery) throws AddressParserException {
	if (addressQuery == null){
	    throw new AddressParserException("Can not execute a null Adsress query");
	}
	String url =getUrl(addressQuery); 
	return getRestClient().get(url, AddressResultsDto.class, OutputFormat.JSON);
    }

    public void executeAndSerialize(AddressQuery addressQuery, OutputStream outputStream) throws AddressParserException {
	if (addressQuery == null){
	    throw new AddressParserException("Can not stream a null Adsress query");
	}
	if (outputStream == null){
	    throw new AddressParserException("Can not serialize in a null stream");
	}
	String url =getUrl(addressQuery); 
	getRestClient().get(url, outputStream, OutputFormat.JSON);
	
    }

    public String executeToString(AddressQuery addressQuery) throws AddressParserException {
	if (addressQuery == null){
	    throw new AddressParserException("Can not stream a null Adsress query to string");
	}
	String url =getUrl(addressQuery); 
	OutputStream outputStream = new ByteArrayOutputStream();
	getRestClient().get(url, outputStream, OutputFormat.JSON);
	return outputStream.toString();
    }
    
    protected String getUrl(AddressQuery query ){
	return baseURL+BeanToQueryString.toQueryString(query);
    }

    public String getBaseURL() {
        return baseURL;
    }

}
