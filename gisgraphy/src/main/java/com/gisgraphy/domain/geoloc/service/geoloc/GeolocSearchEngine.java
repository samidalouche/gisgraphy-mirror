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
package com.gisgraphy.domain.geoloc.service.geoloc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.repository.IGisDao;
import com.gisgraphy.domain.repository.IRepositoryStrategy;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.gisgraphy.service.IStatsUsageService;
import com.gisgraphy.stats.StatsUsageType;

/**
 * Default (threadsafe) implementation of {@link IGeolocSearchEngine}.
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class GeolocSearchEngine implements IGeolocSearchEngine {

    @Resource
    IStatsUsageService statsUsageService;

    private static JAXBContext contextForList;

    @Resource
    IRepositoryStrategy repositoryStrategy;
    /**
     * Json filter, to not serialize all the properties
     */
    protected JsonConfig jsonConfig = new JsonConfig();

    /**
     * The logger
     */
    protected static final Logger logger = LoggerFactory
	    .getLogger(GeolocSearchEngine.class);

    /**
     * 
     */
    public GeolocSearchEngine() {
	super();
	try {
	    contextForList = JAXBContext.newInstance(GeolocResultsDto.class);
	    jsonConfig.setIgnoreTransientFields(true);// does not seems to
	    // work
	    jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
		public boolean apply(Object source, String name, Object value) {
		    if (name.contains("location")
			    || name.contains("gisFeature")) {
			return true;
		    }
		    return false;
		}
	    });
	} catch (JAXBException e) {
	    throw new GeolocServiceException(e.getMessage(), e.getCause());
	}
    }

    @Autowired
    IGisDao<? extends GisFeature>[] iDaos;

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.geoloc.IGeolocSearchEngine#executeQuery(com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery)
     */
    public GeolocResultsDto executeQuery(GeolocQuery query)
	    throws ServiceException {
	statsUsageService.increaseUsage(StatsUsageType.GEOLOC);
	Assert.notNull(query, "Can not execute a null query");
	long start = System.currentTimeMillis();
	Class<? extends GisFeature> placetype = query.getPlaceType();
	IGisDao<? extends GisFeature> dao = repositoryStrategy
		.getDao(GisFeature.class);
	if (placetype != null) {
	    dao = repositoryStrategy.getDao(placetype);
	}
	if (dao == null) {
	    throw new GeolocServiceException(
		    "No gisFeatureDao or no placetype can be found for "
			    + placetype + " can be found.");
	}
	List<GisFeatureDistance> results = dao.getNearestAndDistanceFrom(query
		.getPoint(), query.getRadius(),
		query.getFirstPaginationIndex(), query.getMaxNumberOfResults());

	long end = System.currentTimeMillis();
	long qTime = end - start;
	logger.info(query + " took " + (qTime) + " ms and returns "
		+ results.size() + " results");
	return new GeolocResultsDto(results, qTime);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.IQueryProcessor#executeAndSerialize(com.gisgraphy.domain.geoloc.service.AbstractGisQuery,
     *      java.io.OutputStream)
     */
    public void executeAndSerialize(GeolocQuery query, OutputStream outputStream)
	    throws ServiceException {
	Assert.notNull(query, "Can not execute a null query");
	Assert.notNull(outputStream,
		"Can not serialize into a null outputStream");
	GeolocResultsDto geolocResultsDto = executeQuery(query);
	if (query.getOutputFormat() == OutputFormat.JSON) {
	    /*
	     * JSONArray jsonArray =
	     * JSONArray.fromObject(results,this.jsonConfig ); int indentFactor =
	     * query.isOutputIndented()?DEFAULT_INDENT_FACTOR:0;
	     * jsonArray.toString(indentFactor);
	     */
	    // for performance reason json is not indented
	    JSON json = JSONSerializer.toJSON(geolocResultsDto, jsonConfig);
	    final Writer writer;
	    try {
		writer = new OutputStreamWriter(outputStream, Constants.CHARSET);
	    } catch (Exception e) {
		throw new GeolocServiceException("unknow encoding "
			+ Constants.CHARSET, e);
	    }

	    json.write(writer);
	    try {
		writer.flush();
	    } catch (IOException e) {
		throw new GeolocServiceException("error during flush", e);
	    }
	} else {
	    // this is XML or something else but we output XML because only JSON
	    // and XML are supported
	    try {
		Marshaller m = contextForList.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, query
			.isOutputIndented());
		m.marshal(geolocResultsDto, outputStream);
	    } catch (Exception e) {
		throw new ServiceException(e);
	    }

	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.geoloc.service.IQueryProcessor#executeQueryToString(com.gisgraphy.domain.geoloc.service.AbstractGisQuery)
     */
    public String executeQueryToString(GeolocQuery query)
	    throws ServiceException {
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	executeAndSerialize(query, outputStream);
	return outputStream.toString();
    }

}
