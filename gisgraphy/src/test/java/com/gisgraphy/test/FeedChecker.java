package com.gisgraphy.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sf.jstester.JsTester;

import org.junit.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.gisgraphy.domain.geoloc.entity.City;
import com.gisgraphy.domain.valueobject.Constants;
import com.gisgraphy.domain.valueobject.GeolocResultsDto;
import com.gisgraphy.domain.valueobject.GisFeatureDistance;
import com.gisgraphy.domain.valueobject.Pagination;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;
import com.gisgraphy.helper.URLUtils;

public class FeedChecker {

    
    /**
     * Validates a query matches some XPath test expressions and closes the
     * query
     */
    public static void assertQ(String message, String response, String... tests) {
	try {
	    String m = (null == message) ? "" : message + " ";
	    String results = FeedChecker.validateXPath(response, tests);
	    if (null != results) {
		throw new RuntimeException(m + "query failed XPath: " + results
			+ " xml response was: " + response);
	    }
	} catch (XPathExpressionException e1) {
	    throw new RuntimeException("XPath is invalid", e1);
	} catch (Exception e2) {
	    throw new RuntimeException("Exception during query", e2);
	}
    }
    
    /**
     * A helper method which valides a String against an array of XPath test
     * strings.
     * 
     * @param xml
     *                The xml String to validate
     * @param tests
     *                Array of XPath strings to test (in boolean mode) on the
     *                xml
     * @return null if all good, otherwise the first test that fails.
     */
    public static String validateXPath(String xml, String... tests)
	    throws XPathExpressionException, SAXException {

	if (tests == null || tests.length == 0) {
	    return null;
	}

	Document document = null;
	try {
	    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    document = builder.parse(new ByteArrayInputStream(xml
		    .getBytes(Constants.CHARSET)));
	    
	}
	 catch (ParserConfigurationException e) {
		    throw new RuntimeException ("error during initialisation of the DocumentBuilder ");
		}
	catch (UnsupportedEncodingException e1) {
	    throw new RuntimeException("Totally weird UTF-8 exception", e1);
	} catch (IOException e2) {
	    throw new RuntimeException("Totally weird io exception", e2);
	}

	XPath xpath = XPathFactory.newInstance().newXPath();
	for (String xp : tests) {
	    xp = xp.trim();
	    Boolean bool = (Boolean) xpath.evaluate(xp, document,
		    XPathConstants.BOOLEAN);

	    if (!bool) {
		return xp;
	    }
	}
	return null;

    }

    public static void checkStreetSearchResultsDtoATOM(StreetSearchResultsDto streetSearchResultsDto, String results) {
        StreetDistance streetDistance = streetSearchResultsDto.getResult().get(0);
        assertQ("The query returns incorrect values", results, "/"
        	+ "feed/title[.='"+ Constants.FEED_TITLE + "']",
        	"/feed/link[@href='"+ Constants.FEED_LINK + "']",
        	"/feed/tagline[.='"+Constants.FEED_DESCRIPTION+"']",
        	"/feed/entry/title[.='"+streetDistance.getName()+"']",
        	"/feed/entry/link[@href='"+Constants.STREET_BASE_URL+streetDistance.getGid()+"']",
        	"/feed/entry/author/name[.='"+Constants.MAIL_ADDRESS+"']",
        	"/feed/entry/itemsPerPage[.='"+Pagination.DEFAULT_PAGINATION.getMaxNumberOfResults()+"']",
        	"/feed/entry/totalResults[.='1']",
        	"/feed/entry/startIndex[.='1']",
        	"/feed/entry/point[.='"+streetDistance.getLat()+" "+streetDistance.getLng()+"']"
        	);
    }

    public static void checkGeolocResultsDtoATOM(GeolocResultsDto geolocResultsDto, String results) {
        GisFeatureDistance gisFeatureDistance = geolocResultsDto.getResult().get(0);
        assertQ("The query returns incorrect values", results, "/"
        	+ "feed/title[.='"+ Constants.FEED_TITLE + "']",
        	"/feed/link[@href='"+ Constants.FEED_LINK + "']",
        	"/feed/tagline[.='"+Constants.FEED_DESCRIPTION+"']",
        	"/feed/entry/title[.='"+gisFeatureDistance.getName()+"']",
        	"/feed/entry/link[@href='"+Constants.GISFEATURE_BASE_URL+gisFeatureDistance.getFeatureId()+"']",
        	"/feed/entry/author/name[.='"+Constants.MAIL_ADDRESS+"']",
        	"/feed/entry/itemsPerPage[.='"+Pagination.DEFAULT_PAGINATION.getMaxNumberOfResults()+"']",
        	"/feed/entry/totalResults[.='1']",
        	"/feed/entry/startIndex[.='1']",
        	"/feed/entry/point[.='"+gisFeatureDistance.getLat()+" "+gisFeatureDistance.getLng()+"']"
        	);
    }

    public static void checkGeolocResultsDtoGEORSS(GeolocResultsDto geolocResultsDto, String results) {
        GisFeatureDistance gisFeatureDistance = geolocResultsDto.getResult().get(0);
        assertQ("The query returns incorrect values", results, 
        	"/rss/channel/title[.='"+ Constants.FEED_TITLE + "']",
        	"/rss/channel/link[.='"+ Constants.FEED_LINK + "']",
        	"/rss/channel/description[.='"+Constants.FEED_DESCRIPTION+"']",
        	"/rss/channel/item/title[.='"+gisFeatureDistance.getName()+"']",
        	"/rss/channel/item/guid[.='"+Constants.GISFEATURE_BASE_URL+gisFeatureDistance.getFeatureId()+"']",
        	"/rss/channel/item/creator[.='"+Constants.MAIL_ADDRESS+"']",
        	"/rss/channel/item/itemsPerPage[.='"+Pagination.DEFAULT_PAGINATION.getMaxNumberOfResults()+"']",
        	"/rss/channel/item/totalResults[.='1']",
        	"/rss/channel/item/startIndex[.='1']",
        	"/rss/channel/item/point[.='"+gisFeatureDistance.getLat()+" "+gisFeatureDistance.getLng()+"']"
        	);
    }

    public static void checkStreetSearchResultsDtoGEORSS(StreetSearchResultsDto streetSearchResultsDto, String results) {
        StreetDistance streetDistance = streetSearchResultsDto.getResult().get(0);
        assertQ("The query returns incorrect values", results, 
        	"/rss/channel/title[.='"+ Constants.FEED_TITLE + "']",
        	"/rss/channel/link[.='"+ Constants.FEED_LINK + "']",
        	"/rss/channel/description[.='"+Constants.FEED_DESCRIPTION+"']",
        	"/rss/channel/item/title[.='"+streetDistance.getName()+"']",
        	"/rss/channel/item/guid[.='"+Constants.STREET_BASE_URL+streetDistance.getGid()+"']",
        	"/rss/channel/item/creator[.='"+Constants.MAIL_ADDRESS+"']",
        	"/rss/channel/item/itemsPerPage[.='"+Pagination.DEFAULT_PAGINATION.getMaxNumberOfResults()+"']",
        	"/rss/channel/item/totalResults[.='1']",
        	"/rss/channel/item/startIndex[.='1']",
        	"/rss/channel/item/point[.='"+streetDistance.getLat()+" "+streetDistance.getLng()+"']"
        	);
    }

    public static void checkGeolocResultsDtoJSON(GeolocResultsDto geolocResultsDto, String results) {
        JsTester jsTester = null;
        GisFeatureDistance gisFeatureDistance = geolocResultsDto.getResult().get(0);
        try {
            jsTester = new JsTester();
            jsTester.onSetUp();
    
            // JsTester
            jsTester.eval("evalresult= eval(" + results + ");");
            jsTester.assertNotNull("evalresult");
            Assert.assertNotNull(jsTester.eval("evalresult.QTime"));
            Assert.assertEquals(geolocResultsDto.getNumFound(), ((Double)jsTester.eval(
            "evalresult.numFound")).longValue());
            Assert.assertEquals(gisFeatureDistance.getName(), jsTester.eval(
        	    "evalresult.result[0]['name']").toString());
            Assert.assertEquals(gisFeatureDistance.getFeatureId().toString(),(jsTester.eval("evalresult.result[0]['featureId']")).toString());
            Assert.assertEquals(gisFeatureDistance.getAsciiName(), jsTester.eval(
        	    "evalresult.result[0]['asciiName']").toString());
            Assert.assertEquals(gisFeatureDistance.getLat().toString(),(jsTester
        	    .eval("evalresult.result[0]['lat']")).toString());
            Assert.assertEquals(gisFeatureDistance.getLng(), jsTester
        	    .eval("evalresult.result[0]['lng']"));
            Assert.assertEquals(gisFeatureDistance.getAdm1Code(), jsTester
        	    .eval("evalresult.result[0]['adm1Code']"));
            Assert.assertEquals(gisFeatureDistance.getAdm2Code(), jsTester
        	    .eval("evalresult.result[0]['adm2Code']"));
            Assert.assertEquals(gisFeatureDistance.getAdm3Code(), jsTester
        	    .eval("evalresult.result[0]['adm3Code']"));
            Assert.assertEquals(gisFeatureDistance.getAdm4Code(), jsTester
        	    .eval("evalresult.result[0]['adm4Code']"));
            Assert.assertEquals(gisFeatureDistance.getAdm1Name(), jsTester
        	    .eval("evalresult.result[0]['adm1Name']"));
            Assert.assertEquals(gisFeatureDistance.getAdm2Name(), jsTester
        	    .eval("evalresult.result[0]['adm2Name']"));
            Assert.assertEquals(gisFeatureDistance.getAdm3Name(), jsTester
        	    .eval("evalresult.result[0]['adm3Name']"));
            Assert.assertEquals(gisFeatureDistance.getAdm4Name(), jsTester
        	    .eval("evalresult.result[0]['adm4Name']"));
            Assert.assertEquals(gisFeatureDistance.getFeatureClass(), jsTester
        	    .eval("evalresult.result[0]['featureClass']"));
            Assert.assertEquals(gisFeatureDistance.getFeatureCode(), jsTester
        	    .eval("evalresult.result[0]['featureCode']"));
            Assert.assertEquals(gisFeatureDistance.getCountryCode(), jsTester
        	    .eval("evalresult.result[0]['countryCode']"));
            Assert.assertEquals(gisFeatureDistance.getPopulation(), jsTester
        	    .eval("evalresult.result[0]['population']"));
            Assert.assertEquals(gisFeatureDistance.getElevation(), jsTester
        	    .eval("evalresult.result[0]['elevation']"));
            Assert.assertEquals(gisFeatureDistance.getGtopo30(), jsTester
        	    .eval("evalresult.result[0]['gtopo30']"));
            Assert.assertEquals(gisFeatureDistance.getTimezone(), jsTester
        	    .eval("evalresult.result[0]['timezone']"));
            Assert.assertEquals(gisFeatureDistance.getDistance(), jsTester
        	    .eval("evalresult.result[0]['distance']"));
            if (gisFeatureDistance.getPlaceType().equals(City.class.getSimpleName()) && gisFeatureDistance.getZipCode()!= null){
            Assert.assertEquals(gisFeatureDistance.getZipCode(), jsTester
            		    .eval("evalresult.result[0]['zipCode']"));
            }
            Assert.assertEquals(gisFeatureDistance.getPlaceType(), jsTester
        	    .eval("evalresult.result[0]['placeType']"));
            Assert.assertTrue(jsTester.eval("evalresult.QTime").toString() != "0");
            Assert.assertEquals(1D, jsTester.eval("evalresult.numFound"));
            Assert.assertEquals(URLUtils.createGoogleMapUrl(gisFeatureDistance.getLocation(), gisFeatureDistance
        	    .getName()), jsTester
        	    .eval("evalresult.result[0]['google_map_url']"));
            Assert.assertEquals(URLUtils.createYahooMapUrl(gisFeatureDistance.getLocation()), jsTester
        	    .eval("evalresult.result[0]['yahoo_map_url']"));
            Assert.assertEquals(URLUtils.createCountryFlagUrl(gisFeatureDistance.getCountryCode()),
        	    jsTester.eval("evalresult.result[0]['country_flag_url']"));
    
        } finally {
            if (jsTester != null) {
        	jsTester.onTearDown();
            }
        }
    }

    public static void checkGisFeatureDistanceJAXBMapping(GisFeatureDistance result,
            String feed, String parentXpath) {
        assertQ(
            "GisFeatureDistance is not correcty mapped with jaxb",
            feed,
            parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME + "/name[.='"
        	    + result.getName() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm1Code[.='" + result.getAdm1Code() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm2Code[.='" + result.getAdm2Code() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm3Code[.='" + result.getAdm3Code() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm4Code[.='" + result.getAdm4Code() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm1Name[.='" + result.getAdm1Name() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm2Name[.='" + result.getAdm2Name() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm3Name[.='" + result.getAdm3Name() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/adm4Name[.='" + result.getAdm4Name() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/asciiName[.='" + result.getAsciiName() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/countryCode[.='" + result.getCountryCode()
        	    + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/elevation[.='" + result.getElevation() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/featureClass[.='" + result.getFeatureClass()
        	    + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/featureCode[.='" + result.getFeatureCode()
        	    + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/placeType[.='" + result.getPlaceType() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/featureId[.='" + result.getFeatureId() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/gtopo30[.='" + result.getGtopo30() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/population[.='" + result.getPopulation() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/timezone[.='" + result.getTimezone() + "']",
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME + "/lat[.='"
        	    + result.getLat() + "']", 
        	    parentXpath+"/" + Constants.GISFEATUREDISTANCE_JAXB_NAME + "/distance[.='"
        	    + result.getDistance() + "']",
        	    parentXpath+"/"
        	    + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/lng[.='" + result.getLng() + "']", 
        	    parentXpath+"/"
        	    + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/google_map_url[.='"
        	    + result.getGoogle_map_url() + "']", 
        	    parentXpath+"/"+ Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/yahoo_map_url[.='" + result.getYahoo_map_url()
        	    + "']",  
        	    parentXpath+"/"
        	    + Constants.GISFEATUREDISTANCE_JAXB_NAME
        	    + "/country_flag_url[.='"
        	    + result.getCountry_flag_url() + "']"
    
        );
    }

    public static  void checkGeolocResultsDtoJAXBMapping(GeolocResultsDto geolocResultsDto,
            String feed) {
        List<GisFeatureDistance> results = geolocResultsDto.getResult();
        GisFeatureDistance result = results.get(0);
        checkGisFeatureDistanceJAXBMapping(result,feed,"/" + Constants.GEOLOCRESULTSDTO_JAXB_NAME );
        assertQ(
            "GeolocResultsDto list is not correcty mapped with jaxb",
            feed, "/"
        	    + Constants.GEOLOCRESULTSDTO_JAXB_NAME
        	    + "/numFound[.='" + geolocResultsDto.getNumFound() + "']", "/"
        	    + Constants.GEOLOCRESULTSDTO_JAXB_NAME
        	    + "/QTime[.='" + geolocResultsDto.getQTime() + "']"
    
        );
    }

    public static void checkStreetSearchResultsDtoJSON(StreetSearchResultsDto streetSearchResultsDto, String results) {
        JsTester jsTester = null;
        StreetDistance streetDistance = streetSearchResultsDto.getResult().get(0);
        try {
            jsTester = new JsTester();
            jsTester.onSetUp();
    
            // JsTester
            jsTester.eval("evalresult= eval(" + results + ");");
            Assert.assertNotNull(jsTester.eval("evalresult"));
            Assert.assertNotNull(jsTester.eval("evalresult.QTime"));
            Assert.assertNotNull(jsTester.eval("evalresult.query"));
            Assert.assertEquals(streetSearchResultsDto.getNumFound(), ((Double)jsTester.eval(
            "evalresult.numFound")).longValue());
            Assert.assertEquals(streetDistance.getName(), jsTester.eval(
        	    "evalresult.result[0]['name']").toString());
            Assert.assertEquals(streetDistance.getGid().toString(),(jsTester.eval("evalresult.result[0]['gid']")).toString());
            Assert.assertEquals(streetDistance.getLat().toString(),(jsTester
        	    .eval("evalresult.result[0]['lat']")).toString());
            Assert.assertEquals(streetDistance.getLng(), jsTester
        	    .eval("evalresult.result[0]['lng']"));
            Assert.assertEquals(streetDistance.getOneWay(), jsTester
        	    .eval("evalresult.result[0]['oneWay']"));
            Assert.assertEquals(streetDistance.getStreetType().toString(), jsTester
        	    .eval("evalresult.result[0]['streetType']"));
            Assert.assertEquals(streetDistance.getDistance(), jsTester
        	    .eval("evalresult.result[0]['distance']"));
            Assert.assertEquals(streetDistance.getLength(), jsTester
        	    .eval("evalresult.result[0]['length']"));
            Assert.assertEquals(streetDistance.getCountryCode(), jsTester
        	    .eval("evalresult.result[0]['countryCode']"));	
            Assert.assertTrue(jsTester.eval("evalresult.QTime").toString() != "0");
            Assert.assertEquals(1D, jsTester.eval("evalresult.numFound"));
        } finally {
            if (jsTester != null) {
        	jsTester.onTearDown();
            }
        }
    }

    public static void checkStreetDistanceJAXBMapping(StreetDistance result,String feed, String parentXpath){
        assertQ(
        	    "streetDistance is not correcty mapped with jaxb",
        	    feed,
        "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.STREETDISTANCE_JAXB_NAME
            + "/name[.='" + result.getName() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/gid[.='" + result.getGid() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/oneWay[.='" + result.getOneWay() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/streetType[.='" + result.getStreetType() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/distance[.='" + result.getDistance() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/lat[.='" + result.getLat() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/lng[.='" + result.getLng() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/length[.='" + result.getLength() + "']",
            "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME + "/"
            + Constants.GISFEATUREDISTANCE_JAXB_NAME
            + "/countryCode[.='" + result.getCountryCode() + "']");
    }

    public static void checkStreetSearchResultsDtoJAXBMapping(StreetSearchResultsDto streetSearchResultsDto,
            ByteArrayOutputStream outputStream) {
        try {
            List<StreetDistance> results = streetSearchResultsDto.getResult();
            StreetDistance result = results.get(0);
            String feed = outputStream.toString(Constants.CHARSET);
            checkStreetDistanceJAXBMapping(result, feed, "/" + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME);
            assertQ(
        	    "streetSearchResultsDto is not correcty mapped with jaxb",
        	    feed,
        	    		"/"
        		    + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME
        		    + "/numFound[.='" + streetSearchResultsDto.getNumFound() + "']", "/"
        		    + Constants.STREETSEARCHRESULTSDTO_JAXB_NAME
        		    + "/QTime[.='" + streetSearchResultsDto.getQTime() + "']",
        		    "/"+ Constants.STREETSEARCHRESULTSDTO_JAXB_NAME
        		    + "/query[.='" + streetSearchResultsDto.getQuery() + "']"
    
            );
        } catch (UnsupportedEncodingException e) {
           throw new RuntimeException("an error has occured during checkStreetSearchResultsDtoJAXBMapping : "+e);
        }
    }
    
    public static void checkFulltextErrorXML(String result,String errorMessage) {
	FeedChecker.assertQ("The XML error is not correct", result,
	    "//*[@name='status'][.='-1']", "//*[@name='error'][.='"
		    + errorMessage + "']");
    }

    
}
