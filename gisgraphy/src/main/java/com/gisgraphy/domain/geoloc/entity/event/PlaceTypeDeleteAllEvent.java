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
package com.gisgraphy.domain.geoloc.entity.event;

import com.gisgraphy.domain.geoloc.entity.GisFeature;

/**
 * Event that occurred when all {@link GisFeature}s of a placetype are deleted
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class PlaceTypeDeleteAllEvent implements IGisRepositoryEvent {

     private Class<? extends GisFeature> placeType;


    /**
     * @return The placetype that all gisfeature are deleted
     */
    public Class<? extends GisFeature> getPlaceType() {
        return placeType;
    }



    /**
     * Default constructor
     * 
     * @param placeType
     *                The placetype that all GisFeature will be deleted
     */
    public PlaceTypeDeleteAllEvent(Class<? extends GisFeature> placeType) {
	super();
	this.placeType = placeType;
    }

}
