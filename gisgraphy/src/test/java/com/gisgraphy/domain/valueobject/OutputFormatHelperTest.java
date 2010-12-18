package com.gisgraphy.domain.valueobject;

import static com.gisgraphy.domain.valueobject.OutputFormatHelper.isSupported;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.gisgraphy.domain.valueobject.Output.OutputFormat;


public class OutputFormatHelperTest {
	 @Test
	    public void getListByServiceShouldImplementsAllGisgraphyService() {
		for (GisgraphyServiceType serviceType : GisgraphyServiceType.values()) {
		    try {
			OutputFormatHelper.listByService(serviceType);
		    } catch (Exception e) {
			Assert.fail(e.getMessage());
		    }
		}
	    }

	    @Test
	    public void getListByServiceShouldReturnCorrectValues() {
		Assert.assertEquals(Arrays.asList(OutputFormat.values()), Arrays
			.asList(OutputFormatHelper
				.listByService(GisgraphyServiceType.FULLTEXT)));
		OutputFormat[] expected = { OutputFormat.XML, OutputFormat.JSON,
			OutputFormat.ATOM, OutputFormat.GEORSS };
		Assert.assertEquals(Arrays.asList(expected), Arrays.asList(OutputFormatHelper
			.listByService(GisgraphyServiceType.GEOLOC)));

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
			if (format ==OutputFormat.UNSUPPORTED){
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
		   if (isSupported(format,serviceType)){
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
