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
package com.gisgraphy.domain.geoloc.service.fulltextsearch;

/**
 * All the declared fields in Solr
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public enum FullTextFields {
	FEATUREID("feature_id"),
	FEATURECLASS("feature_class"),
	FEATURECODE("feature_code"),
	NAME("name"),
	NAMEASCII("name_ascii"),
	ELEVATION("elevation"),
	GTOPO30("gtopo30"),
	TIMEZONE("timezone"),
	ALL_NAME("all_name"),
	FULLY_QUALIFIED_NAME("fully_qualified_name"),
	PLACETYPE("placetype"),
	PLACETYPECLASS("placetypeclass"),
	PLACETYPECLASS_SUFFIX("class"),
	POPULATION("population"),
	LAT("lat"),
	LONG("lng"),
	ADM1CODE("adm1_code"),
	ADM2CODE("adm2_code"),
	ADM3CODE("adm3_code"),
	ADM4CODE("adm4_code"),
	ADM1NAME("adm1_name"),
	ALL_ADM1_NAME("all_adm1_name"),
	ADM2NAME("adm2_name"),
	ALL_ADM2_NAME("all_adm2_name"),
	ADM3NAME("adm3_name"),
	ADM4NAME("adm4_name"),
	ZIPCODE("zipcode"),
	COUNTRYCODE("country_code"),
	COUNTRYNAME("country_name"),
	ALL_COUNTRY_NAME("all_country_name"),
	ALTERNATE_NAME_SUFFIX("_alternate"),
	ALTERNATE_NAME_DYNA_SUFFIX("_alternate_"),
	COUNTRY_FLAG_URL("country_flag_url"),
	GOOGLE_MAP_URL("google_map_url"),
	YAHOO_MAP_URL("yahoo_map_url");
	;

	/** The value of the fulltext field in the schema.xml */
	private final String value;

	/** Default constructor */
	private FullTextFields(String value) {
		this.value = value;
	}

	/** Get the value of the fulltext field in the schema.xml */
	public String getValue() {
		return this.value;
	}

}
