package com.gisgraphy.rest;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URLEncoder;

import org.apache.commons.beanutils.PropertyUtils;

import com.gisgraphy.addressparser.exception.AddressParserException;
import com.gisgraphy.domain.Constants;

public class BeanToQueryString {
    
    public static String toQueryString(Object object){
	if (object == null){
	    throw new AddressParserException("Can not processQueryString for null address query");
	}
	StringBuilder sb = new StringBuilder(128);
	try {
	    boolean first=true;
	    String andValue ="&";
	    for (PropertyDescriptor thisPropertyDescriptor : Introspector.getBeanInfo(object.getClass(), Object.class).getPropertyDescriptors()) {
		Object property = PropertyUtils.getProperty(object, thisPropertyDescriptor.getName());
		if (property!=null){
		 sb.append( first?"?":andValue );
		 sb.append(thisPropertyDescriptor.getName());
		 sb.append("=");
		 sb.append(URLEncoder.encode(property.toString(),Constants.CHARSET));
		 first=false;
		}
	    }
	} catch (Exception e) {
	    throw new AddressParserException("can not generate url form addressQuery : "+e.getMessage(),e);
	}
	return sb.toString();
    }

}
