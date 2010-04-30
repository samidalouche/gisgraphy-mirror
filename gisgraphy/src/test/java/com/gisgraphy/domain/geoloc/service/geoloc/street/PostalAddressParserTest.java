package com.gisgraphy.domain.geoloc.service.geoloc.street;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class PostalAddressParserTest {
    
    @Test
    public void parseFrenchAddress(){
	PostalAddressParser parser = new PostalAddressParser();
	Address address =parser.parse("157 bd du 3 juillet 95190 saint jean de luz", "fr");
	assertEquals("157",address.getStreetNumber());
	assertEquals("bd du 3 juillet",address.getStreetName());
	assertEquals("95190",address.getZipCode());
	assertEquals("saint jean de luz",address.getCity());
	
    }

}
