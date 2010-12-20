package com.gisgraphy.domain.valueobject;

import static com.gisgraphy.domain.valueobject.OutputFormatHelper.isFormatSupported;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

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
			OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.PYTHON,OutputFormat.RUBY, };
		OutputFormat[] expected = { OutputFormat.XML, OutputFormat.JSON,
			OutputFormat.ATOM, OutputFormat.GEORSS,OutputFormat.PHP,OutputFormat.RUBY,OutputFormat.PYTHON };
		Assert.assertEquals(Arrays.asList(expectedFulltext), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.FULLTEXT)));
		Assert.assertEquals(Arrays.asList(expected), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.GEOLOC)));
		Assert.assertEquals(Arrays.asList(expected), Arrays.asList(OutputFormatHelper
			.listFormatByService(GisgraphyServiceType.STREET)));

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
	    

	    @Test
	    public void isFormatSupportedShouldReturnCorrectValues() {
		for (OutputFormat format : OutputFormat.values()) {
			if (format == OutputFormat.UNSUPPORTED ||format == OutputFormat.YAML){
				Assert.assertFalse(isFormatSupported(format,GisgraphyServiceType.FULLTEXT));
			} else {
				Assert.assertTrue(isFormatSupported(format,GisgraphyServiceType.FULLTEXT));
			}
		}

		for (OutputFormat format : OutputFormat.values()) {
		    if (format == OutputFormat.XML || format == OutputFormat.JSON || format == OutputFormat.GEORSS || format == OutputFormat.ATOM || format == OutputFormat.PHP || format == OutputFormat.PYTHON || format == OutputFormat.RUBY) {
			Assert.assertTrue(isFormatSupported(format,GisgraphyServiceType.GEOLOC));
			Assert.assertTrue(isFormatSupported(format,GisgraphyServiceType.STREET));
		    } else {
			Assert.assertFalse(isFormatSupported(format,GisgraphyServiceType.GEOLOC));
			Assert.assertFalse(isFormatSupported(format,GisgraphyServiceType.STREET));
		    }
		}
	    }

	
}
