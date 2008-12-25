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
package com.gisgraphy.domain.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.gisgraphy.domain.geoloc.entity.Adm;
import com.gisgraphy.domain.geoloc.entity.AlternateName;
import com.gisgraphy.domain.geoloc.entity.Country;
import com.gisgraphy.domain.geoloc.entity.GisFeature;
import com.gisgraphy.domain.geoloc.entity.Language;
import com.gisgraphy.domain.geoloc.entity.ZipCodeAware;
import com.gisgraphy.domain.geoloc.entity.event.GisFeatureDeleteAllEvent;
import com.gisgraphy.domain.geoloc.entity.event.GisFeatureDeletedEvent;
import com.gisgraphy.domain.geoloc.entity.event.GisFeatureStoredEvent;
import com.gisgraphy.domain.geoloc.entity.event.IEvent;
import com.gisgraphy.domain.geoloc.entity.event.PlaceTypeDeleteAllEvent;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextFields;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.SolrClient;
import com.gisgraphy.helper.EncodingHelper;
import com.gisgraphy.helper.URLUtils;

/**
 * Interface of data access object for {@link Language}
 * 
 * @see ISolRSynchroniser
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class SolRSynchroniser implements ISolRSynchroniser {

    /**
     * Needed by cglib
     */
    @SuppressWarnings("unused")
    private SolRSynchroniser() {

    }

    protected static final Logger logger = LoggerFactory
	    .getLogger(SolRSynchroniser.class);

    private IsolrClient solClient;

    public SolRSynchroniser(SolrClient solrClient) {
	Assert
		.notNull(solrClient,
			"can not instanciate solRsynchroniser because the solrClient is null");
	this.solClient = solrClient;
    }

    /**
     * @param gisFeatureEvent
     */
    private void handleEvent(GisFeatureDeletedEvent gisFeatureEvent) {
	try {
	    solClient.getServer().deleteById(
		    gisFeatureEvent.getGisFeature().getFeatureId().toString());
	    solClient.getServer().commit(true, true);
	} catch (SolrServerException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.repository.ISolRSynchroniser#deleteAll()
     */
    public void deleteAll() {
	try {
	    logger.info("The entire index will be reset");
	    this.solClient.getServer().deleteByQuery("*:*");
	    this.solClient.getServer().commit();
	    this.solClient.getServer().optimize();
	} catch (SolrServerException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.repository.ISolRSynchroniser#deleteAll()
     */
    public void handleEvent(PlaceTypeDeleteAllEvent placeTypeDeleteAllEvent) {
	deleteAllByPlaceType(placeTypeDeleteAllEvent.getPlaceType());
    }

    public void deleteAllByPlaceType(Class<? extends GisFeature> placetype) {
	try {
	    logger.info("GisFeature of type"
		    + placetype.getClass().getSimpleName() + " will be reset");
	    this.solClient.getServer().deleteByQuery(
		    FullTextFields.PLACETYPE.getValue() + ":"
			    + placetype.getSimpleName());
	    this.solClient.getServer().commit();
	    this.solClient.getServer().optimize();
	} catch (SolrServerException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    private void handleEvent(GisFeatureDeleteAllEvent gisFeatureDeleteAllEvent) {
	try {
	    for (GisFeature gisFeature : gisFeatureDeleteAllEvent
		    .getGisFeatures()) {
		solClient.getServer().deleteById(
			gisFeature.getFeatureId().toString());
	    }
	    solClient.getServer().commit(true, true);
	} catch (SolrServerException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.repository.ISolRSynchroniser#handleEvent(com.gisgraphy.domain.geoloc.entity.event.IEvent)
     */
    public void handleEvent(IEvent event) {
	logger.debug("An event has been received ");
	if (event instanceof GisFeatureStoredEvent) {
	    handleEvent((GisFeatureStoredEvent) event);
	} else if (event instanceof GisFeatureDeletedEvent) {
	    handleEvent((GisFeatureDeletedEvent) event);
	} else if (event instanceof GisFeatureDeleteAllEvent) {
	    handleEvent((GisFeatureDeleteAllEvent) event);
	} else if (event instanceof PlaceTypeDeleteAllEvent) {
	    handleEvent((PlaceTypeDeleteAllEvent) event);
	} else {
	    logger.debug("unknow event " + event.getClass().getSimpleName());
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.repository.ISolRSynchroniser#commit()
     */
    public void commit() {
	try {
	    this.solClient.getServer().commit(true, true);
	} catch (SolrServerException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gisgraphy.domain.repository.ISolRSynchroniser#optimize()
     */
    public void optimize() {
	try {
	    this.solClient.getServer().optimize();
	} catch (SolrServerException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    private void handleEvent(GisFeatureStoredEvent gisfeatureCreatedEventEvent) {

	SolrInputDocument ex = new SolrInputDocument();
	GisFeature gisFeature = gisfeatureCreatedEventEvent.getGisFeature();

	if (gisFeature == null) {
	    logger.info("Can not synchronize a null gisFeature");
	    return;
	}
	if (gisFeature.getFeatureId() == null || gisFeature.getFeatureId() <= 0) {
	    logger
		    .info("Can not synchronize GisFeature with wrong featureId : "
			    + gisFeature.getFeatureId());
	    return;
	}

	if (gisFeature.getLatitude() == 0 && gisFeature.getLongitude() == 0) {
	    logger.info("Can not synchronize GisFeature "
		    + gisFeature.getFeatureId() + " with wrong Location "
		    + gisFeature.getName() + ": [" + gisFeature.getLongitude()
		    + "," + gisFeature.getLatitude() + "]");
	    return;
	}

	if (!gisFeature.isFullTextSearchable()) {
	    logger.debug(gisFeature.getClass().getSimpleName()
		    + " is not FullTextSearchable");
	    return;
	}

	ex.setField(FullTextFields.FEATUREID.getValue(), gisFeature
		.getFeatureId());
	ex.setField(FullTextFields.FEATURECLASS.getValue(), gisFeature
		.getFeatureClass());
	ex.setField(FullTextFields.FEATURECODE.getValue(), gisFeature
		.getFeatureCode());
	ex.setField(FullTextFields.NAME.getValue(), EncodingHelper
		.toUTF8(gisFeature.getName()));
	ex.setField(FullTextFields.NAMEASCII.getValue(), gisFeature
		.getAsciiName());

	ex.setField(FullTextFields.ELEVATION.getValue(), gisFeature
		.getElevation());
	ex.setField(FullTextFields.GTOPO30.getValue(), gisFeature.getGtopo30());
	ex.setField(FullTextFields.TIMEZONE.getValue(), gisFeature
		.getTimezone());

	ex.setField(FullTextFields.PLACETYPE.getValue(), gisFeature.getClass()
		.getSimpleName());
	ex.setField(FullTextFields.PLACETYPECLASS.getValue(), gisFeature
		.getClass().getSimpleName()
		+ FullTextFields.PLACETYPECLASS_SUFFIX.getValue());
	ex.setField(FullTextFields.POPULATION.getValue(), gisFeature
		.getPopulation());
	ex.setField(FullTextFields.FULLY_QUALIFIED_NAME.getValue(),
		EncodingHelper.toUTF8(gisFeature.getFullyQualifiedName(false)));
	ex.setField(FullTextFields.LAT.getValue(), gisFeature.getLatitude());
	ex.setField(FullTextFields.LONG.getValue(), gisFeature.getLongitude());
	ex.setField(FullTextFields.COUNTRY_FLAG_URL.getValue(), URLUtils
		.createCountryFlagUrl(gisFeature.getCountryCode()));
	ex.setField(FullTextFields.GOOGLE_MAP_URL.getValue(), URLUtils
		.createGoogleMapUrl(gisFeature.getLocation(), gisFeature
			.getName()));
	ex.setField(FullTextFields.YAHOO_MAP_URL.getValue(), URLUtils
		.createYahooMapUrl(gisFeature.getLocation()));
	// setAdmCode from adm not from the gisfeature one because of
	// syncAdmCodesWithLinkedAdmOnes if it is false , the value may not be
	// the same
	Adm adm = gisFeature.getAdm();
	// we set admCode once for all
	if (adm != null) {
	    ex.setField(FullTextFields.ADM1CODE.getValue(), adm.getAdm1Code());
	    ex.setField(FullTextFields.ADM2CODE.getValue(), adm.getAdm2Code());
	    ex.setField(FullTextFields.ADM3CODE.getValue(), adm.getAdm3Code());
	    ex.setField(FullTextFields.ADM4CODE.getValue(), adm.getAdm4Code());
	}
	while (adm != null) {
	    int level = adm.getLevel();
	    String admLevelName = FullTextFields
		    .valueOf("ADM" + level + "NAME").getValue();
	    ex.setField(admLevelName, EncodingHelper.toUTF8(adm.getName()));
	    if (level == 1 || level == 2) {
		populateAlternateNames(admLevelName, adm.getAlternateNames(),
			ex);
	    }
	    adm = adm.getParent();
	}

	/*
	 * if (gisFeature.getAdm()!= null && gisFeature.getAdm().getAdm1Code() !=
	 * null) { ex.setField(FullTextFields.ADM1CODE.getValue(),
	 * gisFeature.getAdm().getAdm1Code()); } if (gisFeature.getAdm()!= null &&
	 * gisFeature.getAdm().getAdm2Code()!= null) {
	 * ex.setField(FullTextFields.ADM2CODE.getValue(),
	 * gisFeature.getAdm().getAdm2Code()); } if (gisFeature.getAdm()!= null &&
	 * gisFeature.getAdm().getAdm3Code() != null) {
	 * ex.setField(FullTextFields.ADM3CODE.getValue(),
	 * gisFeature.getAdm().getAdm3Code()); } if (gisFeature.getAdm()!= null &&
	 * gisFeature.getAdm().getAdm4Code() != null) {
	 * ex.setField(FullTextFields.ADM4CODE.getValue(),
	 * gisFeature.getAdm().getAdm4Code()); } // admNames if
	 * (gisFeature.getAdm()!= null && gisFeature.getAdm().getAdm1Name() !=
	 * null) { ex.setField(FullTextFields.ADM1NAME.getValue(),
	 * EncodingHelper.toUTF8(gisFeature.getAdm().getAdm1Name())); } if
	 * (gisFeature.getAdm()!= null && gisFeature.getAdm().getAdm2Name() !=
	 * null) { ex.setField(FullTextFields.ADM2NAME.getValue(),
	 * EncodingHelper.toUTF8(gisFeature.getAdm().getAdm2Name())); } if
	 * (gisFeature.getAdm()!= null && gisFeature.getAdm().getAdm3Name() !=
	 * null) {
	 * ex.setField(FullTextFields.ADM3NAME.getValue(),EncodingHelper.toUTF8(gisFeature.getAdm().getAdm3Name())); }
	 * if (gisFeature.getAdm()!= null && gisFeature.getAdm().getAdm4Name() !=
	 * null) { ex.setField(FullTextFields.ADM4NAME.getValue(),
	 * EncodingHelper.toUTF8(gisFeature.getAdm().getAdm4Name())); }
	 */
	if (gisFeature instanceof ZipCodeAware) {
	    try {
		ZipCodeAware city = (ZipCodeAware) gisFeature;
		if (city.getZipCode() != null) {
		    ex.setField(FullTextFields.ZIPCODE.getValue(), city
			    .getZipCode());
		}
	    } catch (ClassCastException cce) {
		logger
			.warn(gisFeature
				+ " is of a zipcodeAware features but but we can not cast it to ZipCodeAware");
	    }
	}

	// No prefix for cities

	List<AlternateName> alternatenames = gisFeature.getAlternateNames();
	populateAlternateNames(FullTextFields.NAME.getValue(), alternatenames,
		ex);

	// we don't want this fields
	// populateAlternateNames("adm2_", adm2.getAlternateNames(), ex);
	// populateAlternateNames("adm1_", adm1.getAlternateNames(), ex);

	String countryCode = gisFeature.getCountryCode();
	if (countryCode != null) {
	    ex.setField(FullTextFields.COUNTRYCODE.getValue(), countryCode
		    .toUpperCase());
	    Country country = gisFeature.getCountry();
	    if (country != null) {
		populateAlternateNames(FullTextFields.COUNTRYNAME.getValue(),
			country.getAlternateNames(), ex);
		ex.setField(FullTextFields.COUNTRYNAME.getValue(),
			EncodingHelper.toUTF8(country.getName()));
	    }
	}

	try {
	    this.solClient.getServer().add(ex);
	} catch (SolrServerException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

    }

    private void populateAlternateNames(String fieldPrefix,
	    List<AlternateName> alternateNames, SolrInputDocument ex) {
	if (alternateNames == null || alternateNames.size() == 0) {
	    return;
	}

	Map<String, List<String>> alternateNamesByAlpha3LanguageCode = new HashMap<String, List<String>>();

	List<String> alternateNamesWithoutAnAlpha3Code = new ArrayList<String>();
	// List<String> alternateNamesAsStrings = new ArrayList<String>();
	if (alternateNames != null) {
	    for (AlternateName alternateName : alternateNames) {
		String alpha3Code = alternateName.getLanguage();
		if (alpha3Code == null) {
		    alternateNamesWithoutAnAlpha3Code.add(EncodingHelper
			    .toUTF8(alternateName.getName()));
		    continue;
		}
		alpha3Code = alpha3Code.toLowerCase();
		List<String> alternateNamesForCurrentLanguage = alternateNamesByAlpha3LanguageCode
			.get(alpha3Code);
		if (alternateNamesForCurrentLanguage == null) {
		    alternateNamesForCurrentLanguage = new ArrayList<String>();
		    alternateNamesByAlpha3LanguageCode.put(alpha3Code,
			    alternateNamesForCurrentLanguage);
		}
		alternateNamesForCurrentLanguage.add(EncodingHelper
			.toUTF8(alternateName.getName()));
	    }
	}

	// Traverse the keys in the map, generating the fields in solr
	Set<String> keys = alternateNamesByAlpha3LanguageCode.keySet();
	for (String key : keys) {
	    List<String> alternateNamesForCurrentLanguage = alternateNamesByAlpha3LanguageCode
		    .get(key);
	    ex
		    .setField(
			    fieldPrefix
				    + FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX
					    .getValue() + key.toUpperCase(),
			    alternateNamesForCurrentLanguage
				    .toArray(new String[alternateNamesForCurrentLanguage
					    .size()]));
	}

	// Handle all the names without alpha 3 codes
	ex.setField(fieldPrefix
		+ FullTextFields.ALTERNATE_NAME_SUFFIX.getValue(),
		alternateNamesWithoutAnAlpha3Code
			.toArray(new String[alternateNamesWithoutAnAlpha3Code
				.size()]));
    }

}
