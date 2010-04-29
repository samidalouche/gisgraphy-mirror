package com.gisgraphy.domain.geoloc.service.geoloc.street;

import org.junit.Test;


public class PostalAddressParserTest {
    
    @Test
    public void parseFrenchAddress(){
	PostalAddressParser parser = new PostalAddressParser();
	parser.parse("157 bd du 3 juillet 95190 saint jean de luz", "fr");
    }

}
