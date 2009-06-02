package com.gisgraphy.domain.geoloc.service.geoloc.street;


public enum StreetType {
    
    byway,
    minor,
    secondary_link,
    construction,
    unsurfaced,
    bridleway,
    primary_link,
    living_street,
    trunk_link,
    steps,
    path,
    road,
    pedestrian,
    trunk,
    motorway,
    cycleway,
    motorway_link,
    primary,
    footway,
    tertiary,
    secondary,
    track,
    unclassified,
    service,
    residential;


	/**
	 * @param type
	 *                the type as String
	 * @return the streetType from the String or null
	 *         if the StreetType can not be determine
	 */
    //todo osm tests
	public static StreetType getFromString(String type) {
	    StreetType streetType = null;
	    try {
		streetType = StreetType.valueOf(type.toUpperCase());
	    } catch (RuntimeException e) {
	    }
	    return streetType;

	}
    
}
