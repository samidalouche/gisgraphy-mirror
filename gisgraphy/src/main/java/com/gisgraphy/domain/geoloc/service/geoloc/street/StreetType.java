package com.gisgraphy.domain.geoloc.service.geoloc.street;

public enum StreetType {

    BYWAY, MINOR, SECONDARY_LINK, CONSTRUCTION, UNSURFACED, BRIDLEWAY, PRIMARY_LINK, LIVING_STREET, TRUNK_LINK, STEPS, PATH, ROAD, PEDESTRIAN, TRUNK, MOTROWAY, CYCLEWAY, MOTORWAY_LINK, PRIMARY, FOOTWAY, TERTIARY, SECONDARY, TRACK, UNCLASSIFIED, SERVICE, RESIDENTIAL;

    /**
     * @param type
     *            the type as String
     * @return the streetType from the String or null if the StreetType can not
     *         be determine
     */
    public static StreetType getFromString(String type) {
	StreetType streetType = null;
	try {
	    streetType = StreetType.valueOf(type.toUpperCase());
	} catch (RuntimeException e) {
	}
	return streetType;

    }

}
