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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IFullTextSearchEngine;
import com.gisgraphy.domain.valueobject.FulltextResultsDto;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.SolrResponseDto;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.opensymphony.xwork2.ActionSupport;

/**
 * DisplayFeature Action
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class DisplayFeatureAction extends ActionSupport {


    /**
     * The reference in the localized file for the error for the fact featureId is required
     */
    public static final String ERROR_REF_REQUIRED_FEATURE_ID = "required.featureId";


    /**
     * The reference in the localized file for the error for the fact that the specified featureId is not a numeric value
     */
    public static final String ERROR_REF_NON_NUMERIC_FEATUREID = "displayfeature.featureid.numeric";


    /**
     * The reference in the localized file for the error for the fact that more than one features were founds for the specified featureId
     */
    public static final String ERROR_REF_NON_UNIQUE_RESULT = "result.nouniqueresult";


    /**
     * The reference in the localized file for the error for the fact that no features were found for the specified featureId
     */
    public static final String ERROR_REF_NORESULT = "result.noresult";

    /**
     * The reference in the localized file for general error
     */
    static final String ERROR_REF_GENERAL_ERROR = "displayfeature.error";

    /**
     * Default Generated serial Id
     */
    private static final long serialVersionUID = 2940477008022016677L;

    private static final Output FULL_OUTPUT = Output.DEFAULT_OUTPUT
	    .withStyle(OutputStyle.FULL);

    private static Logger logger = LoggerFactory
	    .getLogger(DisplayFeatureAction.class);

    private IFullTextSearchEngine fullTextSearchEngine;

    private String featureId;

    private SolrResponseDto result = null;

    public static final String ERROR = "error";

    /**
     * @return the fullyqualified name if exists, or the name
     */
    public String getPreferedName() {
	if (result == null) {
	    return "";
	} else {
	    String fully_qualified_name = result.getFully_qualified_name();
	    return !StringUtils.isEmpty(fully_qualified_name) ?  fully_qualified_name : result.getName();
	}
    }

    private boolean isNumeric(String number) {
	try {
	    Integer.parseInt(number);
	    return true;
	} catch (NumberFormatException e) {
	    return false;
	}
    }

    /**
     * The error message reference in the localized properties file
     */
    private String errorRef = "";

    private String errorMessage = "";

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
    @Override
    public String execute() throws Exception {
	try {
	    if (StringUtils.isEmpty(featureId)) {
		errorRef = ERROR_REF_REQUIRED_FEATURE_ID;
		return ERROR;
	    }
	    if (!isNumeric(featureId)) {
		errorRef = ERROR_REF_NON_NUMERIC_FEATUREID;
		return ERROR;
	    }
	    FulltextQuery fulltextQuery = (FulltextQuery) new FulltextQuery(
		    featureId).withOutput(FULL_OUTPUT);
	    FulltextResultsDto responseDTO = fullTextSearchEngine
		    .executeQuery(fulltextQuery);

	    List<SolrResponseDto> results = responseDTO.getResults();
	    int resultSize = results.size();
	    if (resultSize == 0) {
		errorRef = ERROR_REF_NORESULT;
		return ERROR;
	    } else if (resultSize > 1) {
		errorRef = ERROR_REF_NON_UNIQUE_RESULT;
		return ERROR;
	    } else {
		result = results.get(0);
	    }
	} catch (RuntimeException e) {
	    if (e.getCause() != null) {
		logger.warn("An error occured during search : "
			+ e.getCause().getMessage());
	    } else {
		logger.warn("An error occured during search : "
			+ e.getMessage());
	    }
	    this.errorRef = ERROR_REF_GENERAL_ERROR;
	    this.errorMessage = e.getMessage();
	    return ERROR;
	}
	return SUCCESS;
    }

    /**
     * @param fullTextSearchEngine
     *                the fullTextSearchEngine to set
     */
    @Required
    public void setFullTextSearchEngine(
	    IFullTextSearchEngine fullTextSearchEngine) {
	this.fullTextSearchEngine = fullTextSearchEngine;
    }

    /**
     * @param featureId
     *                the featureId to set
     */
    public void setFeatureId(String featureId) {
	this.featureId = featureId;
    }

    /**
     * @return the errorRef
     */
    public String getErrorRef() {
	return errorRef;
    }

    /**
     * @return the result
     */
    public SolrResponseDto getResult() {
	return result;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
	return errorMessage;
    }

}
