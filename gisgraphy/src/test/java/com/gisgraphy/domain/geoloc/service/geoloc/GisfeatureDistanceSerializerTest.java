package com.gisgraphy.domain.geoloc.service.geoloc;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.gisgraphy.domain.geoloc.service.errors.UnsupportedFormatException;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;

public class GisfeatureDistanceSerializerTest {

    @Test
    public void testSerializeShouldThrowIfTheformatisNotSupported() {
	IGeolocResultsDtoSerializer geolocResultsDtoSerializer = new GeolocResultsDtoSerializer();
	try {
	    geolocResultsDtoSerializer.serialize(new ByteArrayOutputStream(),
		    OutputFormat.RUBY, new GeolocResultsDto(),true);
	    fail();
	} catch (UnsupportedFormatException e) {
	    //ok
	}
    }

}
