package com.gisgraphy.addressparser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.domain.Constants;
import com.gisgraphy.rest.BeanToQueryString;
import com.gisgraphy.rest.IRestClient;
import com.gisgraphy.rest.RestClient;
import com.gisgraphy.serializer.OutputFormat;

public class AddressParserClient implements IAddressParserService {
    
	protected OutputFormat PREFERED_FORMAT = OutputFormat.JSON;
	
	private static Logger logger = LoggerFactory.getLogger(AddressParserClient.class);
	
    public static final String DEFAULT_ADDRESS_PARSER_BASE_URL = "http://addressparser.appspot.com/webaddressparser";
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
	addressQuery.setFormat(PREFERED_FORMAT);
	String url =getUrl(addressQuery); 
	return getRestClient().get(url, AddressResultsDto.class, PREFERED_FORMAT);
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
