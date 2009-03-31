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
package com.gisgraphy.domain.geoloc.service;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;

/**
 * An abstract query for all GisQuery. define {@link Output},
 * {@link Pagination}, and a placetype
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public abstract class AbstractGisQuery {

    /**
     * @param pagination
     *                The {@linkplain Pagination} specification, if null : use
     *                default
     * @param output
     *                {@linkplain Output} The output specification , if null :
     *                use default
     */
    public AbstractGisQuery(Pagination pagination, Output output) {
	super();
	withPagination(pagination);
	withOutput(output);
    }

    /**
     * Constructor with default {@linkplain Pagination}, {@linkplain Output},
     * and placetype
     */
    public AbstractGisQuery() {
	super();
    }
    
    /**
     * @return the maximum number of results that the query should return
     */
    public int getMaxLimitResult(){
	return 10;
    }

    /**
     * @see Pagination
     */
    protected Pagination pagination = Pagination.DEFAULT_PAGINATION;

    /**
     * @see Output
     */
    protected Output output = Output.DEFAULT_OUTPUT;

    /**
     * @return the {@link Pagination} Object
     */
    public Pagination getPagination() {
	return pagination;
    }

    /**
     * @param pagination
     *                the pagination to set. If the pagination is null the
     *                {@link Pagination#DEFAULT_PAGINATION} is used
     * @return The current query to chain methods
     * @see Pagination
     */
    public AbstractGisQuery withPagination(Pagination pagination) {
	if (pagination == null) {
	    this.pagination = Pagination.DEFAULT_PAGINATION;
	} else {
	    this.pagination = pagination;
	}
	return this;
    }

    /**
     * @return The
     * @link {@link Output} object
     */
    public Output getOutput() {
	return output;
    }

    /**
     * @param output
     *                The {@link Output} Object to set. If the output is null :
     *                the {@link Output#DEFAULT_OUTPUT} is used
     */
    public AbstractGisQuery withOutput(Output output) {
	if (output == null) {
	    this.output = Output.DEFAULT_OUTPUT;
	} else {
	    this.output = output;
	}
	return this;
    }

    /**
     * @return The verbose style mode
     * @see OutputStyle
     */
    public OutputStyle getOutputStyle() {
	return this.output.getStyle();
    }

    /**
     * @return The 'from' pagination value
     * @see Pagination
     */
    public int getFirstPaginationIndex() {
	return this.pagination.getFrom();
    }

    /**
     * @return The 'to' pagination value
     * @see Pagination
     */
    public int getLastPaginationIndex() {
	return this.pagination.getTo();
    }

    /**
     * @return The number of results that the query is limited
     * @see Pagination
     */
    public int getMaxNumberOfResults() {
	return this.pagination.getMaxNumberOfResults();
    }

    /**
     * @return The output format
     * @see OutputFormat
     */
    public OutputFormat getOutputFormat() {
	return this.output.getFormat();
    }

    /**
     * @return The iso639 Alpha2 LanguageCode that the output results should be
     */
    public String getOutputLanguage() {
	return this.output.getLanguageCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String asString = this.getClass().getSimpleName() + " with "
		+ getOutput() + " and " + pagination;
	return asString;
    }

}
