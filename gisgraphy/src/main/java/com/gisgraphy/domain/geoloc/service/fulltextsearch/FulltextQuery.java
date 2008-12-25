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

import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.AbstractGisQuery;
import com.gisgraphy.domain.repository.ICountryDao;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;
import com.gisgraphy.helper.GeolocHelper;
import com.gisgraphy.servlet.FulltextServlet;

/**
 * A fulltext Query
 * 
 * @see Pagination
 * @see Output
 * @see IFullTextSearchEngine
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
@Configurable
@Component
public class FulltextQuery extends AbstractGisQuery {

    public final static int QUERY_MAX_LENGTH = 200;

    /**
     * Constructor needed by CGLib
     */
    @SuppressWarnings("unused")
    private FulltextQuery() {
	super();
    }

    /**
     * The type of GIS to search , if null : search for all place type.
     */
    private Class<? extends GisFeature> placeType = null;

    @Autowired
    @Qualifier("countryDao")
    private ICountryDao countryDao;

    private String query = "";
    private String countryCode;

    /**
     * @param req
     *                an HttpServletRequest to construct a {@link FulltextQuery}
     */
    public FulltextQuery(HttpServletRequest req) {
	super();
	this.query = req.getParameter(FulltextServlet.QUERY_PARAMETER);
	if (query == null || "".equals(query.trim())) {
	    throw new FullTextSearchException("query is not specified or empty");
	}
	if (query.length() > FulltextQuery.QUERY_MAX_LENGTH) {
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

	pagination = Pagination.paginate().from(from).to(to)
		.limitNumberOfResults(FulltextServlet.DEFAULT_MAX_RESULTS);
	// output
	OutputFormat format = OutputFormat.getFromString(req
		.getParameter(FulltextServlet.FORMAT_PARAMETER));
	OutputStyle style = OutputStyle.getFromString(req
		.getParameter(FulltextServlet.STYLE_PARAMETER));
	String languageparam = req.getParameter(FulltextServlet.LANG_PARAMETER);
	Output output = Output.withFormat(format).withLanguageCode(
		languageparam).withStyle(style);

	// placetype
	Class<? extends GisFeature> clazz = GeolocHelper
		.getClassEntityFromString(req
			.getParameter(FulltextServlet.PLACETYPE_PARAMETER));

	// countrycode
	String countrycodeParam = req
		.getParameter(FulltextServlet.COUNTRY_PARAMETER);
	this.countryCode = countrycodeParam == null ? null : countrycodeParam
		.toUpperCase();

	if ("true".equalsIgnoreCase(req
		.getParameter(FulltextServlet.INDENT_PARAMETER))
		|| "on".equalsIgnoreCase(req
			.getParameter(FulltextServlet.INDENT_PARAMETER))) {
	    output.withIndentation();
	}

	this.pagination = pagination;
	this.placeType = clazz;
	this.output = output;
    }

    /**
     * @param query
     *                The text to query, if the query is a number zipcode will
     *                be searched
     * @param pagination
     *                The pagination specification, if null : use default
     * @param output
     *                {@link Output} The output specification , if null : use
     *                default
     * @param placeType
     *                the type of gis to search , if null : search for all place
     *                type.
     * @param countryCode
     *                Limit the search to the specified countryCode. If null or
     *                wrong it would be ignored (no check is done)
     * @throws An
     *                 {@link IllegalArgumentException} if the query is null or
     *                 empty string
     */
    public FulltextQuery(String query, Pagination pagination, Output output,
	    Class<? extends GisFeature> placeType, String countryCode) {
	super(pagination, output);
	Assert.notNull(query, "Query must not be empty");
	Assert.isTrue(!"".equals(query.trim()), "Query must not be empty");
	if (query.length() > FulltextQuery.QUERY_MAX_LENGTH) {
	    throw new IllegalArgumentException("query is limited to "
		    + FulltextQuery.QUERY_MAX_LENGTH + "characters");
	}
	this.query = query;
	withPlaceType(placeType);
	limitToCountryCode(countryCode);
    }

    /**
     * @param query
     *                The text to search
     * @throws An
     *                 {@link IllegalArgumentException} if the query is null or
     *                 an empty string
     */
    public FulltextQuery(String query) {
	super();
	Assert.notNull(query, "Query must not be null");
	Assert.isTrue(!"".equals(query.trim()), "Query must not be empty");
	this.query = query;
    }

    /**
     * @return The searched text for this FullTextQuery
     */
    public String getQuery() {
	return query;
    }

    /**
     * @param countryCode
     *                the countryCode to set. Limit the query to the specified
     *                countrycode, if the country code is null, it will be
     *                ignored. If null or invalid, it will be ignored (no check
     *                is done)
     */
    public FulltextQuery limitToCountryCode(String countryCode) {
	this.countryCode = countryCode;
	return this;
    }

    /**
     * @return the countryCode of the country that the query will be restricted
     *         to
     */
    public String getCountryCode() {
	return countryCode;
    }

    /**
     * @return Wether the output will be indented
     * @see Output#isIndented()
     */
    public boolean isOutputIndented() {
	return output.isIndented();
    }

    private boolean isNumericQuery() {
	try {
	    Integer.parseInt(this.query);
	    return true;
	} catch (NumberFormatException e) {
	    return false;
	}
    }

    /**
     * @return the placeType : it limits the search to an object of a specifict
     *         class
     */
    public Class<? extends GisFeature> getPlaceType() {
	return this.placeType;
    }

    /**
     * @param placeType
     *                The placeType to set, if null, search for all placetype
     * @return The current query to chain methods
     */
    public FulltextQuery withPlaceType(Class<? extends GisFeature> placeType) {
	this.placeType = placeType;
	return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String asString = this.getClass().getSimpleName() + " '" + this.query
		+ "' for ";
	if (this.placeType == null) {
	    asString += "all placeType";
	} else {
	    asString += this.placeType.getSimpleName();
	}
	asString += " with " + getOutput() + " and " + pagination;
	return asString;
    }

    private String getCountryNameByCode() {
	Country country = null;
	if (countryCode == null) {
	    return "";
	}
	country = countryDao.getByIso3166Alpha2Code(countryCode.toUpperCase());
	return country != null ? country.getName() : "";
    }

    /**
     * @return A query string for the specified parameter (starting with '?')
     *         the name of the parameters are defined in {@link Constants}
     */
    public String toQueryString() {
	return ClientUtils.toQueryString(parameterize(), false);
    }

    /**
     * @return A Representation of all the needed parameters
     */
    public ModifiableSolrParams parameterize() {
	ModifiableSolrParams parameters = new ModifiableSolrParams();
	StringBuffer query = new StringBuffer(getQuery());
	parameters.set(Constants.INDENT_PARAMETER, isOutputIndented() ? "on"
		: "off");
	parameters.set(Constants.ECHOPARAMS_PARAMETER, "none");
	parameters.set(Constants.START_PARAMETER, String
		.valueOf(getFirstPaginationIndex() - 1));// sub
	// 1
	// because
	// solr
	// start
	// at 0
	parameters.set(Constants.ROWS_PARAMETER, String.valueOf(getPagination()
		.getMaxNumberOfResults()));
	if (getOutputFormat() == OutputFormat.ATOM) {
	    parameters.set(Constants.STYLESHEET_PARAMETER,
		    Constants.ATOM_STYLESHEET);
	} else if (getOutputFormat() == OutputFormat.GEORSS) {
	    parameters.set(Constants.STYLESHEET_PARAMETER,
		    Constants.GEORSS_STYLESHEET);
	}
	parameters.set(Constants.OUTPUT_FORMAT_PARAMETER, getOutputFormat()
		.getParameterValue());
	// force Medium style if ATOM or Geo RSS
	if (getOutputFormat() == OutputFormat.ATOM
		|| getOutputFormat() == OutputFormat.GEORSS) {
	    parameters.set(Constants.FL_PARAMETER, OutputStyle.MEDIUM
		    .getFieldList(getOutput().getLanguageCode()));
	} else {
	    parameters.set(Constants.FL_PARAMETER, getOutput().getFields());
	}

	parameters.set(Constants.QT_PARAMETER, Constants.SolrQueryType.standard
		.toString());
	if (this.countryCode != null && getCountryNameByCode() != null) {
	    query.append(" ").append(getCountryNameByCode());

	}
	if (isNumericQuery()) {
	    // we overide the query type
	    parameters.set(Constants.QT_PARAMETER,
		    Constants.SolrQueryType.numeric.toString());
	} else if (this.placeType != null) {
	    parameters.set(Constants.QT_PARAMETER,
		    Constants.SolrQueryType.typed.toString());
	    query.append(" ").append(this.placeType.getSimpleName()).append(
		    FullTextFields.PLACETYPECLASS_SUFFIX.getValue())
		    .append(" ");
	}
	// we add the query param
	parameters.set(Constants.QUERY_PARAMETER, query.toString());

	return parameters;
    }

}
