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

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Represents a city subdivision Object
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CitySubdivision extends GisFeature implements ZipCodeAware {
    private Integer zipCode;

    /**
     * Constructor that populate the {@link CitySubdivision} with the gisFeature
     * fields<br>
     * 
     * @param gisFeature
     *                The gisFeature from which we want to populate the
     *                {@linkplain CitySubdivision}
     */
    public CitySubdivision(GisFeature gisFeature) {
	super(gisFeature);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.entity.ZipCodeAware#setZipCode(java.lang.Integer)
     */
    public void setZipCode(Integer zipCode) {
	this.zipCode = zipCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.entity.ZipCodeAware#getZipCode()
     */
    @Index(name = "citySubdivisionZipCode")
    public Integer getZipCode() {
	return zipCode;
    }

    /**
     * Default constructor (Needed by CGLib)
     */
    public CitySubdivision() {
	super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.entity.GisFeature#hashCode()
     */
    @Override
    public int hashCode() {
	final int PRIME = 31;
	int result = super.hashCode();
	result = PRIME * result
		+ ((getFeatureId() == null) ? 0 : getFeatureId().hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.entity.GisFeature#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj)) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final CitySubdivision other = (CitySubdivision) obj;
	if (getFeatureId() == null) {
	    if (other.getFeatureId() != null) {
		return false;
	    }
	} else if (!getFeatureId().equals(other.getFeatureId())) {
	    return false;
	}
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.entity.GisFeature#populate(com.gisgraphy.domain.geoloc.entity.GisFeature)
     */
    @Override
    public void populate(GisFeature gisFeature) {
	super.populate(gisFeature);
	if (gisFeature instanceof ZipCodeAware) {
	    this.setZipCode(((ZipCodeAware) gisFeature).getZipCode());
	}
    }

}
