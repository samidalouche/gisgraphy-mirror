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
package com.gisgraphy.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.struts2.components.ActionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IFullTextSearchEngine;
import com.gisgraphy.domain.repository.CountryDao;
import com.gisgraphy.domain.valueobject.FulltextResultsDto;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.helper.GeolocHelper;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * street search Action
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class StreetSearchAction extends ActionSupport {
    
    private static Logger logger = LoggerFactory.getLogger(StreetSearchAction.class);
    
    /**
     * 
     */
    private static final long serialVersionUID = -9018894533914543310L;
    
    private List<City> ambiguousCities;
    
    private String ambiguouscity;
    
    private String city;
    
    private String lng;
    
    private String lat;
    
    public String jsonFeed;
    
    
    private IFullTextSearchEngine fullTextSearchEngine;
    
    private List<ActionMessage> actionMessages = new ArrayList<ActionMessage>();
    
    private List<ActionMessage> errorMessages = new ArrayList<ActionMessage>();
    
    Map<Long,String> featureIdLatLongMap = new HashMap<Long,String>();
    
    private String message = "";
    
    private String errorMessage = "";


    private CountryDao countryDao;
    private String countryCode;


    @SuppressWarnings("unchecked")
    @Override
    public String execute() throws Exception {
	try {
	    setActionMessages(actionMessages);
	    setActionErrors(errorMessages);
	      
	    if (city != null){
		 if (countryCode==null || "".equals(countryCode)){
		     System.err.println("countrycode is null and city is"+city );
		        //TODO localized
		        errorMessage="You must select a country before enter a city";
		       // addActionError("You must select the country first");
		        return Action.INPUT;
		    }
	      // FulltextQuery fulltextQuery = new FulltextQuery(city,Pagination.DEFAULT_PAGINATION,Output.DEFAULT_OUTPUT,City.class,getCountryCode());
	      // ambiguousCities = (List<City>)fullTextSearchEngine.executeQueryToDatabaseObjects(fulltextQuery);
	       ambiguousCities = new ArrayList<City>();
	       City city1 = new City();
	       city1.setFeatureId(3L);
	       city1.setLocation(GeolocHelper.createPoint(1.5F,2.5F));
	       city1.setName("city1");
	       
	       City city2 = new City();
	       city2.setFeatureId(4L);
	       city2.setLocation(GeolocHelper.createPoint(3.5F,4.5F));
	       city2.setName("city2");
	       ambiguousCities.add(city1);
	       ambiguousCities.add(city2);
	       
	       int numberOfPossibleCitiesThatMatches = ambiguousCities.size();
	       if (numberOfPossibleCitiesThatMatches == 0){
	           errorMessage="No city con be found for "+city;
	           //addActionError();
	           return Action.INPUT;
	       }
	       else if (numberOfPossibleCitiesThatMatches==1){
	           message = "city found "+ambiguousCities.get(0).getName();
	           City cityfound =  ambiguousCities.get(0);
	           lat=cityfound.getLatitude().toString();
	           lng = cityfound.getLongitude().toString();
	           //search street
	       // addActionMessage(string);
	           return Action.INPUT;
	       }
	       else {
	           //more than one city suits
		  getLatLongJson();
	       }
	       
	        
	        
	    }
	    else if (ambiguouscity != null) {
		   message = "city found "+ambiguouscity;
		   city=ambiguouscity;
		   
		
	    }
	    
	} catch (Exception e) {
	   errorMessage= e.getMessage();
	}
	
	return Action.INPUT;
    }

    public String getLatLongJson() {
	if (ambiguousCities == null){
	    return "";
	}
	StringBuffer sb = new StringBuffer("[");
	int index = 1;
	for (City city : ambiguousCities){
	    //featureIdLatLongMap.put(city.getFeatureId(), city.getLatitude()+";"+city.getLongitude());
	    sb.append("{\"lat\":");
	    sb.append(city.getLatitude());
	    sb.append(",");
	    sb.append("\"lng\":");
	    sb.append(city.getLongitude());
	    sb.append("}");
	    if (index != ambiguousCities.size()){
		 sb.append(",");
	    }
	    index = index+1;
	    
	}
	sb.append("]");
	return sb.toString();
    }
    
    

    /**
     * @return the ambiguousCities
     */
    public List<City> getAmbiguousCities() {
        return ambiguousCities;
    }
    
    /**
     * @return the ambiguousCity
     */
    public String getAmbiguouscity() {
        return ambiguouscity;
    }

   
    /**
     * @return the available countries
     */
    public List<Country> getCountries() {
	List<Country> countries = new ArrayList<Country>();
	Country country1 = new Country("FR","FRA",33);
	country1.setName("france");
	Country country2 = new Country("US","USA",1);
	country2.setName("etats unis");
	countries.add(country1);
	countries.add(country2);
	return countries;
	//return countryDao.getAllSortedByName();
    }

    
    public boolean isCityAmbiguous(){
	return (ambiguouscity != null && ambiguousCities.size() > 1);
    }

    /**
     * @param ambiguousCity the ambiguousCity to set
     */
    public void setAmbiguouscity(String ambiguouscity) {
        this.ambiguouscity = ambiguouscity;
    }
    
   

 
    /**
     * @param countryDao
     *                the countryDao to set
     */
    @Required
    public void setCountryDao(CountryDao countryDao) {
	this.countryDao = countryDao;
    }

    /**
     * @param fullTextSearchEngine the fullTextSearchEngine to set
     */
    public void setFullTextSearchEngine(IFullTextSearchEngine fullTextSearchEngine) {
        this.fullTextSearchEngine = fullTextSearchEngine;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return the lng
     */
    public String getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * @return the lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

}
