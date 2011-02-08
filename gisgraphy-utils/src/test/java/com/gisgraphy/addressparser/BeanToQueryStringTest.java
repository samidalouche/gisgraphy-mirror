package com.gisgraphy.addressparser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.AssertThrows;

import com.gisgraphy.addressparser.exception.AddressParserException;


public class BeanToQueryStringTest {
    
    @Test(expected=AddressParserException.class)
    public void toQueryStringWithNull(){
	BeanToQueryString.toQueryString(null);
    }
    
    @Test
    public void toQueryStringWithSomeNullValues(){
	AddressQuery query = new AddressQuery("my address", "country");
	String queryString = BeanToQueryString.toQueryString(query);
	Map<String, String> parameters = splitURLParams(queryString,"&");
	Assert.assertTrue(queryString.startsWith("?"));
	Assert.assertEquals(query.getAddress(),parameters.get("address"));
	Assert.assertEquals(4,parameters.size());
	Assert.assertEquals(query.getCountry(),parameters.get("country"));
	Assert.assertNull(parameters.get("callback"));
    }
    
    private static HashMap<String, String> splitURLParams(String completeURL,
	    String andSign) {
	int i;
	HashMap<String, String> searchparms = new HashMap<String, String>();
	;
	i = completeURL.indexOf("?");
	if (i > -1) {
	    String searchURL = completeURL
		    .substring(completeURL.indexOf("?") + 1);

	    String[] paramArray = searchURL.split(andSign);
	    for (int c = 0; c < paramArray.length; c++) {
		String[] paramSplited = paramArray[c].split("=");
		try {
		    searchparms.put(paramSplited[0], java.net.URLDecoder
			    .decode(paramSplited[1], "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		    return new HashMap<String, String>();
		}

	    }
	    // dumpHashtable;
	    java.util.Iterator<String> keys = searchparms.keySet().iterator();
	    while (keys.hasNext()) {
		String s = (String) keys.next();
	    }

	}
	return searchparms;
    }

}
