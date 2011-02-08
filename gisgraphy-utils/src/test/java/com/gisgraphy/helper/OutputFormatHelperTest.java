package com.gisgraphy.helper;

import static com.gisgraphy.helper.OutputFormatHelper.isFormatSupported;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.serializer.OutputFormat;


public class OutputFormatHelperTest {
	 @Test
	    public void getListFormatByServiceShouldImplementsAllGisgraphyService() {
		for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
		    try {
			OutputFormatHelper.listFormatByService(serviceType);
		    } catch (Exception e) {
			Assert.fail(e.getMessage());
		    }
		}
	    }

	    @Test
	    public void getListFormatByServiceShouldReturnCorrectValues() {
		OutputFormat[] expectedFulltext = { OutputFormat.XML, OutputFormat.JSON,
			OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.PYTHON,OutputFormat.RUBY };
		OutputFormat[] expectedForStreetAndGeoloc = { OutputFormat.XML, OutputFormat.JSON,
			OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON , OutputFormat.YAML };
		OutputFormat[] expectedForAddress = { OutputFormat.XML, OutputFormat.JSON,
				OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON , OutputFormat.YAML };
		
		Assert.assertEquals(Arrays.asList(expectedFulltext), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.FULLTEXT)));
		Assert.assertEquals(Arrays.asList(expectedForStreetAndGeoloc), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.GEOLOC)));
		Assert.assertEquals(Arrays.asList(expectedForStreetAndGeoloc), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.STREET)));
		Assert.assertEquals(Arrays.asList(expectedForAddress), Arrays.asList(OutputFormatHelper
				.listFormatByService(GisgraphyServiceType.ADDRESS_PARSER)));

	    }
	    
	    @Test
	    public void getDefaultForServiceIfNotSupportedShouldImplementsAllGisgraphyService() {
		for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
		    try {
			OutputFormatHelper.getDefaultForServiceIfNotSupported(
				OutputFormat.XML, serviceType);
		    } catch (RuntimeException e) {
			Assert.fail(e.getMessage());
		    }
		}
	    }

	   

	    @Test
	    public void getDefaultForServiceIfNotSupportedShouldReturnsCorrectValues() {
		// fulltext service allows all formats
		for (OutputFormat format : OutputFormat.values()) {
			if (format ==OutputFormat.UNSUPPORTED || format == OutputFormat.YAML){
				 Assert.assertEquals(OutputFormat.getDefault(), OutputFormatHelper
						    .getDefaultForServiceIfNotSupported(format,
							    GisgraphyServiceType.FULLTEXT));
			} else{
		    Assert.assertEquals(format, OutputFormatHelper
			    .getDefaultForServiceIfNotSupported(format,
				    GisgraphyServiceType.FULLTEXT));
			}
		}
		//street
		for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()){
		for (OutputFormat format : OutputFormat.values()) {
		   if (isFormatSupported(format,serviceType)){
		    Assert.assertEquals(format, OutputFormatHelper
			    .getDefaultForServiceIfNotSupported(format,
				    serviceType));
		   }
		   else {
		       Assert.assertEquals(OutputFormat.getDefault(), OutputFormatHelper.getDefaultForServiceIfNotSupported(format, serviceType));
		   }
		}
		}
	    }
	    

	
}
