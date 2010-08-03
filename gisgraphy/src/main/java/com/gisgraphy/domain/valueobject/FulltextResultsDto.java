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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
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


    private long numFound = 0;

    private Map<String, Suggestion> suggestionMap = new HashMap<String, Suggestion>();

    private String collatedResult;

    private String spellCheckProposal;

    private int QTime = 0;

    private int resultsSize = 0;

    private Float maxScore = 0F;

    private List<SolrResponseDto> results = DEFAULT_RESULTS;

    /**
     * @param response
     *            The {@link QueryResponse} to build the DTO
     */
    public FulltextResultsDto(QueryResponse response) {
	super();
	this.results = SolrUnmarshaller.unmarshall(response);
	this.QTime = response.getQTime();
	this.numFound = response.getResults().getNumFound();
	this.maxScore = response.getResults().getMaxScore();
	this.resultsSize = results == null ? 0 : results.size();
	SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
	if (spellCheckResponse != null) {
	    Map<String, Suggestion> suggestionMapInternal = spellCheckResponse.getSuggestionMap();
	    if (suggestionMapInternal != null) {
		suggestionMap = spellCheckResponse.getSuggestionMap();
	    }
	    if (spellCheckResponse.getCollatedResult()!= null){
		collatedResult = spellCheckResponse.getCollatedResult().trim();
	    }
	    List<Suggestion> suggestions = spellCheckResponse.getSuggestions();
	    if (suggestions.size()!= 0){
	    StringBuffer sb = new StringBuffer();
	    for (Suggestion suggestion : suggestions) {
		sb.append(suggestion.getSuggestions().get(0)).append(" ");
	    }
	    spellCheckProposal = sb.toString().trim();
	    }
	}

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
     * @return the suggestionMap<{@link String},{@link Suggestion}> with the
     *         entered searched term as key and a {@linkplain Suggestion} as
     *         value that contains several information (see SolRj javadoc). it
     *         will never return null but an empty map if there is no suggestion
     */
    public Map<String, Suggestion> getSuggestionMap() {
	return suggestionMap;
    }
    
    /**
     * @return the collatedResult returned by Solr
     */
    public String getCollatedResult() {
        return collatedResult;
    }

    /**
     * @return a string for the best proposal for spellChecking
     */
    public String getSpellCheckProposal() {
        return spellCheckProposal;
    }

}
