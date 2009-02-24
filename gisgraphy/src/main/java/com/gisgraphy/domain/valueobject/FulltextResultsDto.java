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
/**
 *
 */
package com.gisgraphy.domain.valueobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.SolrUnmarshaller;

/**
 * DTO (data transfer object) that contains a list of {@link SolrResponseDto}
 * and other metaData like the maximum score, The time the query has take
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class FulltextResultsDto {

    private final static List<SolrResponseDto> DEFAULT_RESULTS = new ArrayList<SolrResponseDto>();

    private List<SolrResponseDto> results = DEFAULT_RESULTS;

    private long numFound = 0;

    private int QTime = 0;

    private int resultsSize = 0;

    private Float maxScore = 0F;

    /**
     * @param response
     *                The {@link QueryResponse} to build the DTO
     */
    public FulltextResultsDto(QueryResponse response) {
	super();
	this.results = SolrUnmarshaller.unmarshall(response);
	this.QTime = response.getQTime();
	this.numFound = response.getResults().getNumFound();
	this.maxScore = response.getResults().getMaxScore();
	this.resultsSize = results == null ? 0 : results.size();
	/*Map<String, Suggestion> map = response.getSpellCheckResponse().getSuggestionMap();
	for (Entry<String, Suggestion> e : map.entrySet()){
	    System.err.println(e.getKey()+"="+e.getValue().getSuggestions().get(0));
	    
	}
	List<Suggestion> sug = response.getSpellCheckResponse().getSuggestions();
	for (Suggestion s : sug){
	    System.err.println(s.getSuggestions().get(0));
	}*/
    }

    /**
     * Default Constructor
     */
    public FulltextResultsDto() {
	super();
    }

    /**
     * @return The list of {@link SolrResponseDto}
     */
    public List<SolrResponseDto> getResults() {
	return results;
    }

    /**
     * @return the number of results that match the query. It is different from
     *         {@link #getResultsSize()} : due to pagination the numFound can be
     *         greater than the value returned by {@linkplain #getResultsSize()}
     */
    public long getNumFound() {
	return numFound;
    }

    /**
     * @return The execution time in ms
     */
    public int getQTime() {
	return QTime;
    }

    /**
     * @return The size of the results. It is different form {@link #numFound}
     *         It is different from {@link #getResultsSize()} : due to
     *         pagination the numFound can be greater than the value returned by
     *         {@linkplain #getResultsSize()}
     */
    public int getResultsSize() {
	return resultsSize;
    }

    /**
     * @return the maxScore
     */
    public Float getMaxScore() {
	return maxScore;
    }

    /**
     * @param resultsSize
     *                the resultsSize to set
     */
    public void setResultsSize(int resultsSize) {
	this.resultsSize = resultsSize;
    }

}
