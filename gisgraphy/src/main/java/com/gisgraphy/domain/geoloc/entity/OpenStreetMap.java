/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.domain.geoloc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.gisgraphy.domain.valueobject.SRID;
import com.gisgraphy.helper.IntrospectionIgnoredField;
import com.vividsolutions.jts.geom.LineSegment;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;

/**
 * Represents a {@link OpenStreetMap}.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(name = "streetosmsequence", sequenceName = "street_osm_sequence")
public class OpenStreetMap  {


    /**
     * Needed by CGLib
     */
    public OpenStreetMap() {
	super();
    }
    
    @IntrospectionIgnoredField
    private Long id;
    
    private Long gid;

    private String name;
    
    private String streetType;
    
    private String oneWay;
    
    private Point location;
    
    @IntrospectionIgnoredField
    private MultiLineString shape;
    
    private String countryCode;
    
    private Double length;

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "streetosmsequence")
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return an uniqueid that identify the street
     */
    @Index(name = "streetosmgidindex")
    @Column(unique = true, nullable = false)
    public Long getGid() {
        return gid;
    }
    
    /**
     * @param gid the gid to set
     */
    public void setGid(Long gid) {
        this.gid = gid;
    }


    /**
     * @return the name
     */
    @Index(name = "streetosmnameIndex")
    @Column(length = 200)
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type of the street
     */
    //TODO OSM ENUM?
    @Index(name = "streetosmtypeIndex")
    @Column(length = 50)
    public String getStreetType() {
        return streetType;
    }

    /**
     * @param type the type to set
     */
    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    /**
     * @return the oneway
     */
    //TODO OSM Enum? boolean 
    @Index(name = "streetosmonewayIndex")
    @Column(length = 9)
    public String getOneWay() {
        return oneWay;
    }


    /**
     * @param oneWay the oneWay to set
     */
    public void setOneWay(String oneWay) {
        this.oneWay = oneWay;
    }
    
    

    /**
     * Returns The JTS location point of the current street : The Geometry
     * representation for the latitude, longitude. The Return type is a JTS
     * point. The Location is calculate from the 4326 {@link SRID}
     * 
     * @see SRID
     * @return The JTS Point
     */
    @Type(type = "org.hibernatespatial.GeometryUserType")
    public Point getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * @return the shape
     */
    @Type(type = "org.hibernatespatial.GeometryUserType")
    @Column(nullable = false)
    public MultiLineString getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(MultiLineString shape) {
        this.shape = shape;
    }

    /**
      * @return The ISO 3166 alpha-2 letter code.
     */
    @Index(name = "gisFeatureCountryindex")
    @Column(length = 3)
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countrycode the countrycode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the length of the street in meters
     */
    public Double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(Double length) {
        this.length = length;
    }

  
}
