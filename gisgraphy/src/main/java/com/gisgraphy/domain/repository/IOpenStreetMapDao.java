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
     *                distance The radius in meters
     * @param pointId
     *                the id of the point that we don't want to be include, it
     *                is used to not include the gisFeature from which we want
     *                to find the nearest
     * @param firstResult
     *                the firstResult index (for pagination), numbered from 1,
     *                if < 1 : it will not be taken into account
     * @param maxResults
     *                The Maximum number of results to retrieve (for
     *                pagination), if <= 0 : it will not be taken into acount
     * @param requiredClass
     *                the class of the object to be retireved
     * @return A List of GisFeatureDistance with the nearest elements or an
     *         emptylist (never return null), ordered by distance.<u>note</u>
     *         the specified gisFeature will not be included into results
     * @see GisFeatureDistance
     * @return a list of gisFeature (never return null but an empty list)
     */
    public List<StreetDistance> getNearestAndDistanceFrom(
	    final Point point, final double distance,
	    final int firstResult, final int maxResults,
	    final String streetType,final String namePrefix) ;
    
}
