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
package com.gisgraphy.hibernate.projection;

public class _CityDTO {

    /**
     * 
     */
    public _CityDTO() {
	super();
    }

    private String name;
    private Long featureId;
    private Double distance;

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *                the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the featureId
     */
    public Long getFeatureId() {
	return featureId;
    }

    /**
     * @param featureId
     *                the featureId to set
     */
    public void setFeatureId(Long featureId) {
	this.featureId = featureId;
    }

    /**
     * @return the distance
     */
    public Double getDistance() {
	return distance;
    }

    /**
     * @param distance
     *                the distance to set
     */
    public void setDistance(Double distance) {
	this.distance = distance;
    }

}
