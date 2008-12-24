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
 * Basic class for Event that occurred on GisFeature
 * 
 * @see GisFeaturesEvent
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GisFeatureEvent implements IEvent {

    /**
     * The {@link GisFeature} the current event refers to
     */
    private GisFeature gisFeature;

    /**
     * @return The {@link GisFeature} the current event refers to
     */
    public GisFeature getGisFeature() {
	return this.gisFeature;
    }

    /**
     * Default constructor
     * 
     * @param gisFeature
     *                The {@link GisFeature} the current event refers to
     */
    public GisFeatureEvent(GisFeature gisFeature) {
	super();
	this.gisFeature = gisFeature;
    }

}
