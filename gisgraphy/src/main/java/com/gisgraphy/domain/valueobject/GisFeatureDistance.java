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
package com.gisgraphy.domain.valueobject;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.ZipCodeAware;
import com.gisgraphy.helper.URLUtils;
import com.vividsolutions.jts.geom.Point;

/**
 * Value object that represents a gisFeature with a distance The JAXB node name
 * is {@link Constants#GISFEATUREDISTANCE_JAXB_NAME}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@XmlRootElement(name = Constants.GISFEATUREDISTANCE_JAXB_NAME)
@XmlAccessorType(XmlAccessType.FIELD)
public class GisFeatureDistance {

    public static class GisFeatureDistanceBuilder {

	public static GisFeatureDistanceBuilder gisFeatureDistance() {
	    return new GisFeatureDistanceBuilder();
	}

	private final GisFeatureDistance gisFeatureDistance;

	private GisFeatureDistanceBuilder() {
	    gisFeatureDistance = new GisFeatureDistance();
	}

	public GisFeatureDistance build() {
	    gisFeatureDistance.updateFields();
	    return gisFeatureDistance;
	}

	public GisFeatureDistanceBuilder withName(String name) {
	    gisFeatureDistance.name = name;
	    return this;
	}

	public GisFeatureDistanceBuilder withLocation(Point location) {
	    gisFeatureDistance.location = location;
	    return this;
	}

	public GisFeatureDistanceBuilder withDistance(Double distance) {
	    gisFeatureDistance.distance = distance;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm1Code(String adm1Code) {
	    gisFeatureDistance.adm1Code = adm1Code;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm2Code(String adm2Code) {
	    gisFeatureDistance.adm2Code = adm2Code;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm3Code(String adm3Code) {
	    gisFeatureDistance.adm3Code = adm3Code;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm4Code(String adm4Code) {
	    gisFeatureDistance.adm4Code = adm4Code;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm1Name(String adm1Name) {
	    gisFeatureDistance.adm1Name = adm1Name;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm2Name(String adm2Name) {
	    gisFeatureDistance.adm2Name = adm2Name;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm3Name(String adm3Name) {
	    gisFeatureDistance.adm3Name = adm3Name;
	    return this;
	}

	public GisFeatureDistanceBuilder withAdm4Name(String adm4Name) {
	    gisFeatureDistance.adm4Name = adm4Name;
	    return this;
	}

	public GisFeatureDistanceBuilder withAsciiName(String asciiName) {
	    gisFeatureDistance.asciiName = asciiName;
	    return this;
	}

	public GisFeatureDistanceBuilder withCountryCode(String countryCode) {
	    if (countryCode != null) {
		gisFeatureDistance.countryCode = countryCode.toUpperCase();
	    }
	    return this;
	}

	public GisFeatureDistanceBuilder withElevation(Integer elevation) {
	    gisFeatureDistance.elevation = elevation;
	    return this;
	}

	public GisFeatureDistanceBuilder withFeatureClass(String featureClass) {
	    gisFeatureDistance.featureClass = featureClass;
	    return this;
	}

	public GisFeatureDistanceBuilder withFeatureCode(String featureCode) {
	    gisFeatureDistance.featureCode = featureCode;
	    return this;
	}

	public GisFeatureDistanceBuilder withFeatureId(Long featureId) {
	    gisFeatureDistance.featureId = featureId;
	    return this;
	}

	public GisFeatureDistanceBuilder withGtopo30(Integer gtopo30) {
	    gisFeatureDistance.gtopo30 = gtopo30;
	    return this;
	}

	public GisFeatureDistanceBuilder withPopulation(Integer population) {
	    gisFeatureDistance.population = population;
	    return this;
	}

	public GisFeatureDistanceBuilder withTimeZone(String timezone) {
	    gisFeatureDistance.timezone = timezone;
	    return this;
	}

	public GisFeatureDistanceBuilder withZipCode(String zipCode) {
	    gisFeatureDistance.zipCode = zipCode;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withArea(Double area) {
	    gisFeatureDistance.area = area;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withLevel(Integer level) {
	    gisFeatureDistance.level = level;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withTld(String tld) {
	    gisFeatureDistance.tld = tld;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withCapitalName(String capitalName) {
	    gisFeatureDistance.capitalName = capitalName;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withContinent(String continent) {
	    gisFeatureDistance.continent = continent;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withPostalCodeRegex(String postalCodeRegex) {
	    gisFeatureDistance.postalCodeRegex = postalCodeRegex;
	    return this;
	}
	
	    
	public GisFeatureDistanceBuilder withCurrencyCode(String currencyCode) {
	    gisFeatureDistance.currencyCode = currencyCode;
	    return this;
	}
	    
	public GisFeatureDistanceBuilder withCurrencyName(String currencyName) {
	    gisFeatureDistance.currencyName = currencyName;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withEquivalentFipsCode(String equivalentFipsCode) {
	    gisFeatureDistance.equivalentFipsCode = equivalentFipsCode;
	    return this;
	}
	  
	public GisFeatureDistanceBuilder withFipsCode(String fipsCode) {
	    gisFeatureDistance.fipsCode = fipsCode;
	    return this;
	}

	public GisFeatureDistanceBuilder withIso3166Alpha2Code(String iso3166Alpha2Code) {
	    gisFeatureDistance.iso3166Alpha2Code = iso3166Alpha2Code;
	    return this;
	}
	
	public GisFeatureDistanceBuilder withIso3166Alpha3Code(String iso3166Alpha3Code) {
	    gisFeatureDistance.iso3166Alpha3Code = iso3166Alpha3Code;
	    return this;
	}

	public GisFeatureDistanceBuilder withIso3166NumericCode(Integer iso3166NumericCode) {
	    gisFeatureDistance.iso3166NumericCode = iso3166NumericCode;
	    return this;
	}

	public GisFeatureDistanceBuilder withPhonePrefix(String phonePrefix) {
	    gisFeatureDistance.phonePrefix = phonePrefix;
	    return this;
	}
	    
	public GisFeatureDistanceBuilder withPostalCodeMask(String postalCodeMask) {
	    gisFeatureDistance.postalCodeMask = postalCodeMask;
	    return this;
	}


	public GisFeatureDistanceBuilder withPlaceType(
		Class<? extends GisFeature> placeType) {
	    if (placeType != null) {
		gisFeatureDistance.placeType = placeType.getSimpleName();
	    }
	    return this;
	}

    }

    @XmlTransient
    @Transient
    protected static final Logger logger = LoggerFactory
	    .getLogger(GisFeatureDistance.class);

    @XmlTransient
    @Transient
    private GisFeature gisFeature;

    @XmlTransient
    @Transient
    private Point location;

    private Double distance;

    private String name;

    private String adm1Code;

    private String adm2Code;

    private String adm3Code;

    private String adm4Code;

    private String adm1Name;

    private String adm2Name;

    private String adm3Name;

    private String adm4Name;

    private String asciiName;

    private String countryCode;

    private Integer elevation;

    private String featureClass;

    private String featureCode;

    private Long featureId;

    private Integer gtopo30;

    private Integer population;

    private String timezone;

    private Double lat;

    private Double lng;

    private String placeType;

    private String zipCode;

    private String google_map_url;

    private String yahoo_map_url;

    private String country_flag_url;
    
    private Integer level;
    
    private Double area;

    private String tld;

    private String capitalName;

    private String continent;

    private String postalCodeRegex;
    
    private String currencyCode;

    private String currencyName;

    private String equivalentFipsCode;

    private String fipsCode;

    private String iso3166Alpha2Code;

    private String iso3166Alpha3Code;

    private Integer iso3166NumericCode;
    
    private String phonePrefix;

    private String postalCodeMask;

    public GisFeatureDistance() {
	super();
    }

    /**
     * @param gisFeature
     *                The gisFeature
     * @param distance
     *                The distance
     */
    public GisFeatureDistance(GisFeature gisFeature, Double distance) {
	super();
	this.distance = distance;
	this.gisFeature = gisFeature;
	if (gisFeature != null) {

	    this.adm1Code = gisFeature.getAdm1Code();
	    this.adm2Code = gisFeature.getAdm2Code();
	    this.adm3Code = gisFeature.getAdm3Code();
	    this.adm4Code = gisFeature.getAdm4Code();

	    this.adm1Name = gisFeature.getAdm1Name();
	    this.adm2Name = gisFeature.getAdm2Name();
	    this.adm3Name = gisFeature.getAdm3Name();
	    this.adm4Name = gisFeature.getAdm4Name();

	    if (gisFeature.getAsciiName() != null) {
		this.asciiName = gisFeature.getAsciiName().trim();
	    }
	    if (gisFeature.getCountryCode() != null) {
		this.countryCode = gisFeature.getCountryCode().toUpperCase();
	    }
	    this.elevation = gisFeature.getElevation();
	    this.featureClass = gisFeature.getFeatureClass();
	    this.featureCode = gisFeature.getFeatureCode();
	    this.featureId = gisFeature.getFeatureId();
	    this.gtopo30 = gisFeature.getGtopo30();
	    this.location = gisFeature.getLocation();
	    this.name = gisFeature.getName().trim();
	    this.population = gisFeature.getPopulation();
	    this.timezone = gisFeature.getTimezone();
	    if (gisFeature instanceof ZipCodeAware) {
		this.zipCode = ((ZipCodeAware) gisFeature).getZipCode();
	    }
	    this.placeType = gisFeature.getClass().getSimpleName()
		    .toLowerCase();
	    updateFields();
	}
    }
    
    
    
    /**
     * @param adm
     *                The adm
     * @param distance
     *                The distance
     */
    public GisFeatureDistance(Adm adm, Double distance) {
	this((GisFeature)adm,distance);
	this.level = adm.getLevel();
    }
    
    /**
     * @param country
     *                The country
     * @param distance
     *                The distance
     */
    public GisFeatureDistance(Country country, Double distance) {
	this((GisFeature)country,distance);
	    this.elevation = country.getElevation();
	    this.featureClass = country.getFeatureClass();
	    this.featureCode = country.getFeatureCode();
	    this.featureId = country.getFeatureId();
	    this.gtopo30 = country.getGtopo30();
	    this.location = country.getLocation();
	    this.name = country.getName().trim();
	    this.population = country.getPopulation();
	    this.timezone = country.getTimezone();
	    this.area=country.getArea();
	    this.tld=country.getTld();
	    this.capitalName=country.getCapitalName();
	    this.continent=country.getContinent();
	    this.postalCodeMask=country.getPostalCodeMask();
	    this.postalCodeRegex=country.getPostalCodeRegex();
	    this.currencyCode=country.getCurrencyCode();
	    this.currencyName=country.getCurrencyName();
	    this.equivalentFipsCode = country.getEquivalentFipsCode();
	    this.fipsCode=country.getFipsCode();
	    this.iso3166Alpha2Code= country.getIso3166Alpha2Code();
	    this.iso3166Alpha3Code= country.getIso3166Alpha3Code();
	    this.iso3166NumericCode= country.getIso3166NumericCode();
	    this.phonePrefix=country.getPhonePrefix();
    }

    /**
     * update the calculated fields (GoogleMapUrl,YahooMapURL,CountryFlag,...)
     * 
     */
    public void updateFields() {
	this.google_map_url = URLUtils.createGoogleMapUrl(this.location,
		this.name);
	this.yahoo_map_url = URLUtils.createYahooMapUrl(this.location);
	this.country_flag_url = URLUtils.createCountryFlagUrl(this.countryCode);
	if (this.location != null) {
	    this.lat = location.getY();
	    this.lng = location.getX();
	}
	if (featureClass != null && featureCode != null) {
	    try {
		this.placeType = FeatureCode.valueOf(
			featureClass + "_" + featureCode).getObject()
			.getClass().getSimpleName();
	    } catch (RuntimeException e) {
	    }
	}
    }

    /**
     * @return The distance
     */
    public Double getDistance() {
	return distance;
    }

    /**
     * @return The gisfeature
     */
    @Transient
    @XmlTransient
    public GisFeature getGisFeature() {
	return this.gisFeature;
    }

    /**
     * @return the name
     */
    public String getName() {
	return this.name;
    }

    /**
     * @return the location
     */
    @Transient
    @XmlTransient
    public Point getLocation() {
	return this.location;
    }

    /**
     * @return the adm1Code
     */
    public String getAdm1Code() {
	return this.adm1Code;
    }

    /**
     * @return the adm2Code
     */
    public String getAdm2Code() {
	return this.adm2Code;
    }

    /**
     * @return the adm3Code
     */
    public String getAdm3Code() {
	return this.adm3Code;
    }

    /**
     * @return the adm4Code
     */
    public String getAdm4Code() {
	return this.adm4Code;
    }

    /**
     * @return the adm1Name
     */
    public String getAdm1Name() {
	return this.adm1Name;
    }

    /**
     * @return the adm2Name
     */
    public String getAdm2Name() {
	return this.adm2Name;
    }

    /**
     * @return the adm3Name
     */
    public String getAdm3Name() {
	return this.adm3Name;
    }

    /**
     * @return the adm4Name
     */
    public String getAdm4Name() {
	return this.adm4Name;
    }

    /**
     * @return the asciiName
     */
    public String getAsciiName() {
	return this.asciiName;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
	return this.countryCode;
    }

    /**
     * @return the elevation
     */
    public Integer getElevation() {
	return this.elevation;
    }

    /**
     * @return the featureClass
     */
    public String getFeatureClass() {
	return this.featureClass;
    }

    /**
     * @return the featureCode
     */
    public String getFeatureCode() {
	return this.featureCode;
    }

    /**
     * @return the featureId
     */
    public Long getFeatureId() {
	return this.featureId;
    }

    /**
     * @return the gtopo30
     */
    public Integer getGtopo30() {
	return this.gtopo30;
    }

    /**
     * @return the population
     */
    public Integer getPopulation() {
	return this.population;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
	return this.timezone;
    }

    /**
     * @return the lat
     */
    public Double getLat() {
	return this.lat;
    }

    /**
     * @return the lng
     */
    public Double getLng() {
	return this.lng;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
	return this.zipCode;
    }

    /**
     * @return the placeType
     */
    public String getPlaceType() {
	return placeType;
    }

    /**
     * @return the google_map_url
     */
    public String getGoogle_map_url() {
	return this.google_map_url;
    }

    /**
     * @return the yahoo_map_url
     */
    public String getYahoo_map_url() {
	return this.yahoo_map_url;
    }

    /**
     * @return the country_flag_url
     */
    public String getCountry_flag_url() {
	return this.country_flag_url;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @return the area
     */
    public Double getArea() {
        return area;
    }

    /**
     * @return the tld
     */
    public String getTld() {
        return tld;
    }

    /**
     * @return the capitalName
     */
    public String getCapitalName() {
        return capitalName;
    }

    /**
     * @return the continent
     */
    public String getContinent() {
        return continent;
    }

    /**
     * @return the postalCodeRegex
     */
    public String getPostalCodeRegex() {
        return postalCodeRegex;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @return the currencyName
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @return the equivalentFipsCode
     */
    public String getEquivalentFipsCode() {
        return equivalentFipsCode;
    }

    /**
     * @return the fipsCode
     */
    public String getFipsCode() {
        return fipsCode;
    }

    /**
     * @return the iso3166Alpha2Code
     */
    public String getIso3166Alpha2Code() {
        return iso3166Alpha2Code;
    }

    /**
     * @return the iso3166Alpha3Code
     */
    public String getIso3166Alpha3Code() {
        return iso3166Alpha3Code;
    }

    /**
     * @return the iso3166NumericCode
     */
    public int getIso3166NumericCode() {
        return iso3166NumericCode;
    }

    /**
     * @return the phonePrefix
     */
    public String getPhonePrefix() {
        return phonePrefix;
    }

    /**
     * @return the postalCodeMask
     */
    public String getPostalCodeMask() {
        return postalCodeMask;
    }

}
