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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextFields;
import com.gisgraphy.domain.repository.exception.RepositoryException;

/**
 * Java Dto for a solr fulltext response. it is used by
 * {@link FulltextResultsDto}
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class SolrResponseDto {

    /**
     * Used by cglib
     */
    @SuppressWarnings("unused")
    private SolrResponseDto() {
	super();
    }

    /**
     * Create a {@link SolrResponseDto} from a {@link SolrDocument}
     */
    public SolrResponseDto(SolrDocument solrDocument) {
	super();
	if (solrDocument != null) {
	    this.name = getFieldAsString(solrDocument, FullTextFields.NAME
		    .getValue());
	    this.feature_id = getFieldAsLong(solrDocument,
		    FullTextFields.FEATUREID.getValue());
	    this.feature_class = getFieldAsString(solrDocument,
		    FullTextFields.FEATURECLASS.getValue());
	    this.feature_code = getFieldAsString(solrDocument,
		    FullTextFields.FEATURECODE.getValue());
	    this.name_ascii = getFieldAsString(solrDocument,
		    FullTextFields.NAMEASCII.getValue());
	    this.elevation = getFieldAsInteger(solrDocument,
		    FullTextFields.ELEVATION.getValue());
	    this.gtopo30 = getFieldAsInteger(solrDocument,
		    FullTextFields.GTOPO30.getValue());
	    this.timezone = getFieldAsString(solrDocument,
		    FullTextFields.TIMEZONE.getValue());
	    this.fully_qualified_name = getFieldAsString(solrDocument,
		    FullTextFields.FULLY_QUALIFIED_NAME.getValue());
	    this.placetype = getFieldAsString(solrDocument,
		    FullTextFields.PLACETYPE.getValue());
	    this.population = getFieldAsInteger(solrDocument,
		    FullTextFields.POPULATION.getValue());
	    this.lat = getFieldAsDouble(solrDocument, FullTextFields.LAT
		    .getValue());
	    this.lng = getFieldAsDouble(solrDocument, FullTextFields.LONG
		    .getValue());
	    this.adm1_code = getFieldAsString(solrDocument,
		    FullTextFields.ADM1CODE.getValue());
	    this.adm2_code = getFieldAsString(solrDocument,
		    FullTextFields.ADM2CODE.getValue());
	    this.adm3_code = getFieldAsString(solrDocument,
		    FullTextFields.ADM3CODE.getValue());
	    this.adm4_code = getFieldAsString(solrDocument,
		    FullTextFields.ADM4CODE.getValue());
	    this.adm1_name = getFieldAsString(solrDocument,
		    FullTextFields.ADM1NAME.getValue());
	    this.adm2_name = getFieldAsString(solrDocument,
		    FullTextFields.ADM2NAME.getValue());
	    this.adm3_name = getFieldAsString(solrDocument,
		    FullTextFields.ADM3NAME.getValue());
	    this.adm4_name = getFieldAsString(solrDocument,
		    FullTextFields.ADM4NAME.getValue());
	    this.zipcode = getFieldAsString(solrDocument,
		    FullTextFields.ZIPCODE.getValue());
	    this.country_name = getFieldAsString(solrDocument,
		    FullTextFields.COUNTRYNAME.getValue());
	    this.country_flag_url = getFieldAsString(solrDocument,
		    FullTextFields.COUNTRY_FLAG_URL.getValue());
	    this.google_map_url = getFieldAsString(solrDocument,
		    FullTextFields.GOOGLE_MAP_URL.getValue());
	    this.yahoo_map_url = getFieldAsString(solrDocument,
		    FullTextFields.YAHOO_MAP_URL.getValue());
	    this.name_alternates = getFieldsToList(solrDocument,
		    FullTextFields.NAME.getValue()+FullTextFields.ALTERNATE_NAME_SUFFIX.getValue());
	    this.adm1_names_alternate = getFieldsToList(solrDocument,
		    FullTextFields.ADM1NAME.getValue()+FullTextFields.ALTERNATE_NAME_SUFFIX.getValue());
	    this.adm2_names_alternate = getFieldsToList(solrDocument,
		    FullTextFields.ADM2NAME.getValue()+FullTextFields.ALTERNATE_NAME_SUFFIX.getValue());
	    this.country_names_alternate = getFieldsToList(solrDocument,
		    FullTextFields.COUNTRYNAME.getValue()+FullTextFields.ALTERNATE_NAME_SUFFIX.getValue());

	    this.name_alternates_localized = getFieldsToMap(solrDocument,
		    FullTextFields.NAME.getValue()+FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue());
	    this.adm1_names_alternate_localized = getFieldsToMap(solrDocument,
		    FullTextFields.ADM1NAME.getValue()+FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue());
	    this.adm2_names_alternate_localized = getFieldsToMap(solrDocument,
		    FullTextFields.ADM2NAME.getValue()+FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue());
	    this.country_names_alternate_localized = getFieldsToMap(
		    solrDocument,  FullTextFields.COUNTRYNAME.getValue()+FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX.getValue());
	    //countryspecific
	    this.continent=getFieldAsString(solrDocument,
		    FullTextFields.CONTINENT.getValue());
	    this.currency_code = getFieldAsString(solrDocument,
		    FullTextFields.CURRENCY_CODE.getValue());
	    this.currency_name= getFieldAsString(solrDocument,
		    FullTextFields.CURRENCY_NAME.getValue());
	    this.fips_code= getFieldAsString(solrDocument,
		    FullTextFields.FIPS_CODE.getValue());
	    this.isoalpha2_country_code= getFieldAsString(solrDocument,
		    FullTextFields.ISOALPHA2_COUNTRY_CODE.getValue());
	    this.isoalpha3_country_code= getFieldAsString(solrDocument,
		    FullTextFields.ISOALPHA3_COUNTRY_CODE.getValue());
	    this.postal_code_mask= getFieldAsString(solrDocument,
		    FullTextFields.POSTAL_CODE_MASK.getValue());
	    this.postal_code_regex= getFieldAsString(solrDocument,
		    FullTextFields.POSTAL_CODE_REGEX.getValue());
	    this.phone_prefix= getFieldAsString(solrDocument,
		    FullTextFields.PHONE_PREFIX.getValue());
	    this.spoken_languages=getFieldsToList(solrDocument,
		    FullTextFields.SPOKEN_LANGUAGES.getValue());
	    this.tld= getFieldAsString(solrDocument,
		    FullTextFields.TLD.getValue());
	    this.capital_name= getFieldAsString(solrDocument,
		    FullTextFields.CAPITAL_NAME.getValue());
	    this.area= getFieldAsDouble(solrDocument,
		    FullTextFields.AREA.getValue());
	    this.level= getFieldAsInteger(solrDocument,
		    FullTextFields.LEVEL.getValue());
	}
    }

    private Map<String, List<String>> getFieldsToMap(SolrDocument solrDocument,
	    String fieldNamePrefix) {
	Map<String, List<String>> result = new HashMap<String, List<String>>();
	for (String fieldName : solrDocument.getFieldNames()) {
	    if (fieldName.startsWith(fieldNamePrefix)) {
		for (Object fieldValue : solrDocument.getFieldValues(fieldName)) {
		    String fieldValueString = (String) fieldValue;
		    String languageCode = fieldName.substring(fieldName
			    .lastIndexOf("_") + 1);
		    List<String> languageList = result.get(languageCode);
		    if (languageList == null) {
			languageList = new ArrayList<String>();
			result.put(languageCode, languageList);
		    }
		    languageList.add(fieldValueString);
		}
	    }
	}
	return result;
    }

    private List<String> getFieldsToList(SolrDocument solrDocument,
	    String fieldname) {
	List<String> list = new ArrayList<String>();
	if (solrDocument.getFieldValues(fieldname) != null) {
	    for (Object o : solrDocument.getFieldValues(fieldname)) {
		if (o == null) {
		    continue;
		} else if (o instanceof String) {
		    list.add(o.toString());
		} else {
		    throw new RepositoryException(fieldname
			    + " is not a String but a "
			    + o.getClass().getSimpleName());
		}
	    }
	}
	return list;
    }

    private Integer getFieldAsInteger(SolrDocument solrDocument,
	    String fieldname) {
	Object o = solrDocument.getFieldValue(fieldname);
	if (o == null) {
	    return null;
	} else if (o instanceof Integer) {
	    return (Integer) o;
	} else {
	    throw new RepositoryException(fieldname
		    + " is not an Integer but a "
		    + o.getClass().getSimpleName());
	}
    }

    @SuppressWarnings("unused")
    private Float getFieldAsFloat(SolrDocument solrDocument, String fieldname) {
	Object o = solrDocument.getFieldValue(fieldname);
	if (o == null) {
	    return null;
	} else if (o instanceof Float) {
	    return (Float) o;
	} else {
	    throw new RepositoryException(fieldname + " is not a Float but a "
		    + o.getClass().getSimpleName());
	}
    }

    private Long getFieldAsLong(SolrDocument solrDocument, String fieldname) {
	Object o = solrDocument.getFieldValue(fieldname);
	if (o == null) {
	    return null;
	} else if (o instanceof Long) {
	    return (Long) o;
	} else {
	    throw new RepositoryException(fieldname + " is not a Long but a "
		    + o.getClass().getSimpleName());
	}
    }

    private Double getFieldAsDouble(SolrDocument solrDocument, String fieldname) {
	Object o = solrDocument.getFieldValue(fieldname);
	if (o == null) {
	    return null;
	} else if (o instanceof Double) {
	    return (Double) o;
	} else {
	    throw new RepositoryException(fieldname + " is not a Double but a "
		    + o.getClass().getSimpleName());
	}
    }

    private String getFieldAsString(SolrDocument solrDocument, String fieldname) {
	Object o = solrDocument.getFieldValue(fieldname);
	if (o == null) {
	    return null;
	} else if (o instanceof String) {
	    return (String) o;
	} else {
	    throw new RepositoryException(fieldname + " is not a String but a "
		    + o.getClass().getSimpleName());
	}
    }

    private String name;
    private List<String> name_alternates;
    private Map<String, List<String>> name_alternates_localized;

    private Long feature_id;
    private String feature_class;
    private String feature_code;
    private String name_ascii;
    private Integer elevation;
    private Integer gtopo30;
    private String timezone;
    private String fully_qualified_name;
    private String placetype;
    private Integer population;
    private Double lat;
    private Double lng;
    private String adm1_code;
    private String adm2_code;
    private String adm3_code;
    private String adm4_code;
    
    //country specific fields
    private String continent;
    private String currency_code;
    private String currency_name;
    private String fips_code;
    private String isoalpha2_country_code;
    private String isoalpha3_country_code;
   

    private String postal_code_mask;
    private String postal_code_regex;
    private String phone_prefix;
    private List<String> spoken_languages;
    private String tld;
    private String capital_name;
    private Double area;
    
    //Adm only
    private Integer level;

    private String adm1_name;
    private List<String> adm1_names_alternate;
    private Map<String, List<String>> adm1_names_alternate_localized;

    private String adm2_name;
    private List<String> adm2_names_alternate;
    private Map<String, List<String>> adm2_names_alternate_localized;

    private String adm3_name;
    private String adm4_name;
    private String zipcode;
    private String country_code;

    private String country_name;
    private List<String> country_names_alternate;
    private Map<String, List<String>> country_names_alternate_localized;

    private String country_flag_url;
    private String google_map_url;
    private String yahoo_map_url;

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the name_alternates
     */
    public List<String> getName_alternates() {
	return name_alternates;
    }

    /**
     * @return the name_alternates_localized
     */
    public Map<String, List<String>> getName_alternates_localized() {
	return name_alternates_localized;
    }

    /**
     * @return the feature_id
     */
    public Long getFeature_id() {
	return feature_id;
    }

    /**
     * @return the feature_class
     */
    public String getFeature_class() {
	return feature_class;
    }

    /**
     * @return the feature_code
     */
    public String getFeature_code() {
	return feature_code;
    }

    /**
     * @return the name_ascii
     */
    public String getName_ascii() {
	return name_ascii;
    }

    /**
     * @return the elevation
     */
    public Integer getElevation() {
	return elevation;
    }

    /**
     * @return the gtopo30
     */
    public Integer getGtopo30() {
	return gtopo30;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
	return timezone;
    }

    /**
     * @return the fully_qualified_name
     */
    public String getFully_qualified_name() {
	return fully_qualified_name;
    }

    /**
     * @return the placetype
     */
    public String getPlacetype() {
	return placetype;
    }

    /**
     * @return the population
     */
    public Integer getPopulation() {
	return population;
    }

    /**
     * @return the lat
     */
    public Double getLat() {
	return lat;
    }

    /**
     * @return the lng
     */
    public Double getLng() {
	return lng;
    }

    /**
     * @return the adm1_code
     */
    public String getAdm1_code() {
	return adm1_code;
    }

    /**
     * @return the adm2_code
     */
    public String getAdm2_code() {
	return adm2_code;
    }

    /**
     * @return the adm3_code
     */
    public String getAdm3_code() {
	return adm3_code;
    }

    /**
     * @return the adm4_code
     */
    public String getAdm4_code() {
	return adm4_code;
    }

    /**
     * @return the adm1_name
     */
    public String getAdm1_name() {
	return adm1_name;
    }

    /**
     * @return the adm1_names_alternate
     */
    public List<String> getAdm1_names_alternate() {
	return adm1_names_alternate;
    }

    /**
     * @return the adm1_names_alternate_localized
     */
    public Map<String, List<String>> getAdm1_names_alternate_localized() {
	return adm1_names_alternate_localized;
    }

    /**
     * @return the adm2_name
     */
    public String getAdm2_name() {
	return adm2_name;
    }

    /**
     * @return the adm2_names_alternate
     */
    public List<String> getAdm2_names_alternate() {
	return adm2_names_alternate;
    }

    /**
     * @return the adm2_names_alternate_localized
     */
    public Map<String, List<String>> getAdm2_names_alternate_localized() {
	return adm2_names_alternate_localized;
    }

    /**
     * @return the adm3_name
     */
    public String getAdm3_name() {
	return adm3_name;
    }

    /**
     * @return the adm4_name
     */
    public String getAdm4_name() {
	return adm4_name;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
	return zipcode;
    }

    /**
     * @return the country_code
     */
    public String getCountry_code() {
	return country_code;
    }

    /**
     * @return the country_name
     */
    public String getCountry_name() {
	return country_name;
    }

    /**
     * @return the country_names_alternate
     */
    public List<String> getCountry_names_alternate() {
	return country_names_alternate;
    }

    /**
     * @return the country_names_alternate_localized
     */
    public Map<String, List<String>> getCountry_names_alternate_localized() {
	return country_names_alternate_localized;
    }

    /**
     * @return the country_flag_url
     */
    public String getCountry_flag_url() {
	return country_flag_url;
    }

    /**
     * @return the google_map_url
     */
    public String getGoogle_map_url() {
	return google_map_url;
    }

    /**
     * @return the yahoo_map_url
     */
    public String getYahoo_map_url() {
	return yahoo_map_url;
    }

    /**
     * @return the continent
     */
    public String getContinent() {
        return continent;
    }

    /**
     * @return the currency_code
     */
    public String getCurrency_code() {
        return currency_code;
    }

    /**
     * @return the currency_name
     */
    public String getCurrency_name() {
        return currency_name;
    }

    /**
     * @return the fips_code
     */
    public String getFips_code() {
        return fips_code;
    }

    /**
     * @return the isoalpha2_country_code
     */
    public String getIsoalpha2_country_code() {
        return isoalpha2_country_code;
    }

    /**
     * @return the isoalpha3_country_code
     */
    public String getIsoalpha3_country_code() {
        return isoalpha3_country_code;
    }

    /**
     * @return the postal_code_mask
     */
    public String getPostal_code_mask() {
        return postal_code_mask;
    }

    /**
     * @return the postal_code_regex
     */
    public String getPostal_code_regex() {
        return postal_code_regex;
    }

    /**
     * @return the phone_prefix
     */
    public String getPhone_prefix() {
        return phone_prefix;
    }

    /**
     * @return the spoken_languages
     */
    public List<String> getSpoken_languages() {
        return spoken_languages;
    }

    /**
     * @return the tld
     */
    public String getTld() {
        return tld;
    }

    /**
     * @return the capital_name
     */
    public String getCapital_name() {
        return capital_name;
    }

    /**
     * @return the area
     */
    public Double getArea() {
        return area;
    }
    
    public Integer getLevel() {
        return level;
    }

}
