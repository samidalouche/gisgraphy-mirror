package com.gisgraphy.domain.repository;

import java.util.List;

import com.gisgraphy.domain.geoloc.entity.OpenStreetMap;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.vividsolutions.jts.geom.Point;

public interface IOpenStreetMapDao extends IDao<OpenStreetMap, java.lang.Long> {

    //TODO OSM doc
    /**
     * base method for all findNearest
     * 
     * @param point
     *                The point from which we want to find GIS Object
     * @param distance
     *                The radius in meters
     * @param firstResult
     *                the firstResult index (for pagination), numbered from 1,
     *                if < 1 : it will not be taken into account
     * @param maxResults
     *                The Maximum number of results to retrieve (for
     *                pagination), if <= 0 : it will not be taken into acount
     * @param streetType
     *                The type of street
     * @param oneWay
     *                wether the street should be oneway or not
     * @param namePrefix
     *                the name the street name must starts
     * 
     * @return A List of StreetDistance with the nearest elements or an
     *         empty list (never return null), ordered by distance.
     * @see StreetDistance
     */
    public List<StreetDistance> getNearestAndDistanceFrom(
	    final Point point, final double distance,
	    final int firstResult, final int maxResults,
	    final String streetType,String oneWay, final String namePrefix) ;
    
}
