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
package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import javax.servlet.http.HttpServletRequest;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.fulltext.service.exception.FullTextSearchException;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.serializer.OutputFormat;
import com.gisgraphy.servlet.FulltextServlet;
import com.gisgraphy.servlet.GisgraphyServlet;

/**
 * A Fulltext Query builder. it build Fulltext query from HTTP Request
 * 
 * @see Pagination
 * @see Output
 * @see IFullTextSearchEngine
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class FulltextQueryHttpBuilder {
	
	
 private static FulltextQueryHttpBuilder instance = new FulltextQueryHttpBuilder();
 
 public static FulltextQueryHttpBuilder getInstance(){
	 return instance;
	 
 }
    /**
     * @param req
     *                an HttpServletRequest to construct a {@link FulltextQueryHttpBuilder}
     */
    public FulltextQuery buildFromRequest(HttpServletRequest req) {
    	FulltextQuery query = null;
	String httpQueryParameter = req.getParameter(FulltextServlet.QUERY_PARAMETER);
	if (httpQueryParameter != null){
		query = new FulltextQuery(httpQueryParameter.trim());
	}
	if (httpQueryParameter == null || "".equals(httpQueryParameter.trim())) {
	    throw new FullTextSearchException("query is not specified or empty");
	}
	if (httpQueryParameter.length() > FulltextQuery.QUERY_MAX_LENGTH) {
	    throw new FullTextSearchException("query is limited to "
		    + FulltextQuery.QUERY_MAX_LENGTH + "characters");
	}
	// pagination
	Pagination pagination = null;
	int from;
	int to;
	try {
	    from = Integer.valueOf(
		    req.getParameter(FulltextServlet.FROM_PARAMETER))
		    .intValue();
	} catch (Exception e) {
	    from = Pagination.DEFAULT_FROM;
	}

	try {
	    to = Integer
		    .valueOf(req.getParameter(FulltextServlet.TO_PARAMETER))
		    .intValue();
	} catch (NumberFormatException e) {
	    to = -1;
	}

	pagination = Pagination.paginateWithMaxResults(query.getMaxLimitResult()).from(from).to(to)
		.limitNumberOfResults(query.getMaxLimitResult());
	// output
	OutputFormat format = OutputFormat.getFromString(req
		.getParameter(FulltextServlet.FORMAT_PARAMETER));
	OutputStyle style = OutputStyle.getFromString(req
		.getParameter(FulltextServlet.STYLE_PARAMETER));
	String languageparam = req.getParameter(FulltextServlet.LANG_PARAMETER);
	Output output = Output.withFormat(format).withLanguageCode(
		languageparam).withStyle(style);

	// placetype
	String[] placetypeParameters = req
		.getParameterValues(FulltextServlet.PLACETYPE_PARAMETER);
	Class<? extends GisFeature>[] clazzs = null;
	if (placetypeParameters!=null){
		clazzs = new Class[placetypeParameters.length]; 
		for (int i=0;i<placetypeParameters.length;i++){
			Class<? extends GisFeature> classEntityFromString = GeolocHelper.getClassEntityFromString(placetypeParameters[i]);
				clazzs[i]= classEntityFromString;
		}
	}
	

	// countrycode
	String countrycodeParam = req
		.getParameter(FulltextServlet.COUNTRY_PARAMETER);
	if (countrycodeParam == null){
		query.limitToCountryCode(null);
	
	} else {
		query.limitToCountryCode(countrycodeParam
				.toUpperCase());
		
	}

	//indentation
	if ("true".equalsIgnoreCase(req
		.getParameter(GisgraphyServlet.INDENT_PARAMETER))
		|| "on".equalsIgnoreCase(req
			.getParameter(GisgraphyServlet.INDENT_PARAMETER))) {
	    output.withIndentation();
	}
	//spellchecking
	if ("true".equalsIgnoreCase(req
			.getParameter(FulltextServlet.SPELLCHECKING_PARAMETER))
			|| "on".equalsIgnoreCase(req
				.getParameter(FulltextServlet.SPELLCHECKING_PARAMETER))) {
		    query.withSpellChecking();
		}
	else if ("false".equalsIgnoreCase(req.getParameter(FulltextServlet.SPELLCHECKING_PARAMETER))) {
		query.withoutSpellChecking();
	}

	query.withPagination(pagination);
	query.withPlaceTypes(clazzs);
	query.withOutput(output);
	
	return query;
    } 

    

}
