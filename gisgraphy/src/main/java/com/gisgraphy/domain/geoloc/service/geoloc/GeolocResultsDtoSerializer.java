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
package com.gisgraphy.domain.geoloc.service.geoloc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import com.gisgraphy.domain.geoloc.service.ServiceException;
import com.gisgraphy.domain.geoloc.service.errors.UnsupportedFormatException;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.GisgraphyServiceType;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.Output.OutputFormat;
import com.sun.syndication.feed.module.georss.GeoRSSModule;
import com.sun.syndication.feed.module.georss.gml.GMLModuleImpl;
import com.sun.syndication.feed.module.opensearch.OpenSearchModule;
import com.sun.syndication.feed.module.opensearch.impl.OpenSearchModuleImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * serialize @link {@link GeolocResultsDto} into several formats
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class GeolocResultsDtoSerializer implements
	IGeolocResultsDtoSerializer {
    
    private static JAXBContext contextForList;

    /**
     * Json filter, to not serialize all the properties
     */
    protected JsonConfig jsonConfig = new JsonConfig();

    /**
     * Default Constructor
     */
    public GeolocResultsDtoSerializer() {
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gisgraphy.domain.geoloc.service.geoloc.IGisFeatureDistanceSerializer
     * #serialize(java.io.OutputStream,
     * com.gisgraphy.domain.valueobject.Output.OutputFormat,
     * com.gisgraphy.domain.valueobject.GeolocResultsDto)
     */
    public void serialize(OutputStream outputStream, OutputFormat outputFormat,
	    GeolocResultsDto geolocResultsDto, boolean indent) {
	if (!outputFormat.isSupported(GisgraphyServiceType.GEOLOC)) {
	    throw new UnsupportedFormatException(outputFormat
		    + " is not applicable for Geoloc");
	} 
	   
	else if (outputFormat == OutputFormat.JSON) {
	    serializeToJSON(outputStream,
		    geolocResultsDto,indent);
	} else 	if (outputFormat==OutputFormat.ATOM){
	    serializeToFeed(outputStream,geolocResultsDto,ATOM_VERSION);
	}
	else if (outputFormat==OutputFormat.GEORSS) {
	    serializeToFeed(outputStream,geolocResultsDto,RSS_VERSION);
	}
	else {
	    //default
	    serializeToXML(outputStream,
		    geolocResultsDto,indent);
	}
	
	return;
    }
    
    private void serializeToXML(OutputStream outputStream,
	    GeolocResultsDto geolocResultsDto,boolean indent) {
	 try {
		Marshaller m = contextForList.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, indent);
		m.marshal(geolocResultsDto, outputStream);
	    } catch (Exception e) {
		throw new ServiceException(e);
	    }
    }
    
    private void serializeToJSON(OutputStream outputStream,
	    GeolocResultsDto geolocResultsDto,boolean indent){
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
}

    @SuppressWarnings("unchecked")
    private void serializeToFeed(OutputStream outputStream,
	    GeolocResultsDto geolocResultsDto,String feedVersion) {
	SyndFeed synFeed = new SyndFeedImpl();
	Writer oWriter = null;
	try {

	    synFeed.setFeedType(feedVersion);
	    

	    synFeed.setTitle(Constants.FEED_TITLE);
	    synFeed.setLink(Constants.FEED_LINK);
	    synFeed.setDescription(Constants.FEED_DESCRIPTION);
	    List<SyndEntry> entries = new ArrayList<SyndEntry>();

	    for (GisFeatureDistance gisFeatureDistance : geolocResultsDto
		    .getResult()) {

		SyndEntry entry = new SyndEntryImpl();
		GeoRSSModule geoRSSModuleGML = new GMLModuleImpl();
		OpenSearchModule openSearchModule = new OpenSearchModuleImpl();

		geoRSSModuleGML.setLatitude(gisFeatureDistance.getLat());
		geoRSSModuleGML.setLongitude(gisFeatureDistance.getLng());

		openSearchModule
			.setItemsPerPage(Pagination.DEFAULT_MAX_RESULTS);
		openSearchModule
			.setTotalResults(geolocResultsDto.getNumFound());
		openSearchModule.setStartIndex(1);

		entry.getModules().add(openSearchModule);
		entry.getModules().add(geoRSSModuleGML);
		entry.setTitle(gisFeatureDistance.getName());
		entry
			.setLink(Constants.FEATURE_BASE_URL+
				+ gisFeatureDistance.getFeatureId());
		SyndContent description = new SyndContentImpl();
		description.setType(OutputFormat.ATOM.getContentType());
		description.setValue(gisFeatureDistance.getName());
		entry.setDescription(description);
		entries.add(entry);
	    }
	    
	    synFeed.setEntries(entries);

	    try {
		oWriter = new OutputStreamWriter(outputStream, Constants.CHARSET);
	    } catch (UnsupportedEncodingException e) {
		throw new RuntimeException("unknow encoding "+Constants.CHARSET);
	    }

	    // Copy synfeed to output
	    SyndFeedOutput output = new SyndFeedOutput();
	    try {
		output.output(synFeed, oWriter);
		 // Flush
		    oWriter.flush();
	    } catch (Exception e) {
		throw new RuntimeException(e);
	    }

	   
	} finally {
	    if (oWriter != null)
		try {
		    oWriter.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    if (outputStream != null)
		try {
		    outputStream.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	}

    }

}
