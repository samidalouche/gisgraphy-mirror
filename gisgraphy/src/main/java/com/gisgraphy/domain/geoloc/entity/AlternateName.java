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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.gisgraphy.domain.valueobject.AlternateNameSource;

/**
 * Represents an Alternate Name of a feature
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(name = "AlternateName", sequenceName = "Alternatename_sequence")
public class AlternateName {

    private Long id;

    private Integer alternateNameId;

    private String name;

    private GisFeature gisFeature;

    private AlternateNameSource source;

    private String language;

    private boolean isPreferredName;

    private boolean isShortName;

    /**
     * @param name
     *                The name of the alternateName
     * @param source
     *                From which source the alternateName come From
     */
    public AlternateName(String name, AlternateNameSource source) {
	super();
	this.name = name;
	this.source = source;
    }

    /**
     * Default constructor (Needed by CGLib)
     */
    public AlternateName() {
	super();
    }

    /**
     * The datastore id
     * 
     * @return The DatastoreId, it is not a domain value, just a technical One
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AlternateName")
    public Long getId() {
	return this.id;
    }

    /**
     * Set the datastore id. You should never call this method. It is the
     * responsability of the dataStore
     * 
     * @param id
     *                The datastore id
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return The name of the current AlternateName
     */
    @Index(name = "Alternatenameindex")
    @Column(nullable = false, length = 200)
    public String getName() {
	return name;
    }

    /**
     * Set the name of the current AlternateName
     * 
     * @param name
     *                The name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * The GisFeature, the AlternateName refers To
     * 
     * @return the GisFeature, the AlternateName refers To
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "gisFeature")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Index(name = "AlternatenameGisFeatureindex")
    public GisFeature getGisFeature() {
	return gisFeature;
    }

    /**
     * Set The GisFeature, the AlternateName refers To
     * 
     * @param gisFeature
     *                The GisFeature, the AlternateName refers To
     */
    public void setGisFeature(GisFeature gisFeature) {
	this.gisFeature = gisFeature;
    }

    /**
     * It tells from which files / gazetteers it has been imported
     * 
     * @return The source from which the AlternateName come From (from the
     *         AlternateName file, from the dump of the country file, a personal
     *         add )
     * @see AlternateNameSource
     */
    @Column(nullable = false)
    public AlternateNameSource getSource() {
	return source;
    }

    /**
     * Set the source from which the AlternateName come From
     */
    public void setSource(AlternateNameSource source) {
	this.source = source;
    }

    /**
     * Returns the alternateNameId. The alternatenameid could be null if the
     * alternateName come from the Country dump file.
     * 
     * @return The alternateNameId
     */
    public Integer getAlternateNameId() {
	return alternateNameId;
    }

    /**
     * Set the AlternateNameId
     * 
     * @param alternateNameId
     *                The AlternateNameId to set
     */
    public void setAlternateNameId(Integer alternateNameId) {
	this.alternateNameId = alternateNameId;
    }

    /**
     * Whether the AlternateName is the preferred name for The
     * {@link GisFeature}
     * 
     * @return true if the AlternateName is the preferred name for the
     *         {@link GisFeature}
     */
    public boolean isPreferredName() {
	return isPreferredName;
    }

    /**
     * Set the AlternateName as a preferred name or Not
     * 
     * @param isPreferredName
     *                The preferred Name property
     */
    public void setPreferredName(boolean isPreferredName) {
	this.isPreferredName = isPreferredName;
    }

    /**
     * Whether the AlternateName is a short name for The {@link GisFeature}
     * 
     * @return true if the AlternateName is a short name for the
     *         {@link GisFeature}
     */
    public boolean isShortName() {
	return isShortName;
    }

    /**
     * Set the AlternateName as a short name or Not
     * 
     * @param isShortName
     *                The short Name property
     */
    public void setShortName(boolean isShortName) {
	this.isShortName = isShortName;
    }

    /**
     * @return The iso639 Alpha2 or alpha 3 LanguageCode of the
     *         {@link AlternateName}
     */
    @Column(length = 7)
    public String getLanguage() {
	return language;
    }

    /**
     * Set the the iso639 Alpha2 or alpha 3 LanguageCode for the current
     * AlternateName
     * 
     * @param language
     *                the language to set, it will be automaticaly upercased
     */
    public void setLanguage(String language) {
	this.language = language == null ? language : language.toUpperCase();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int PRIME = 31;
	int result = super.hashCode();
	result = PRIME * result
		+ ((gisFeature == null) ? 0 : gisFeature.hashCode());
	result = PRIME * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
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
	final AlternateName other = (AlternateName) obj;
	if (gisFeature == null) {
	    if (other.gisFeature != null) {
		return false;
	    }
	} else if (!gisFeature.equals(other.gisFeature)) {
	    return false;
	}
	if (name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!name.equals(other.name)) {
	    return false;
	}
	return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return name + "[" + alternateNameId + "][linked to "
		+ gisFeature.getFeatureId() + "[lang=" + language + "][short="
		+ isShortName + "][preferred=" + isPreferredName + "]";
    }
}
