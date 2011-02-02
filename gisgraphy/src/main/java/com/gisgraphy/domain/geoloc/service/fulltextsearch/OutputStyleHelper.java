package com.gisgraphy.domain.geoloc.service.fulltextsearch;

import com.gisgraphy.domain.valueobject.Output;
import com.gisgraphy.domain.valueobject.Output.OutputStyle;

public class OutputStyleHelper {
    
    public String getFulltextFieldList(OutputStyle outputStyle,String languageCode){
	if (outputStyle == Output.OutputStyle.SHORT){
	    return new StringBuffer("score,").append(
			FullTextFields.FEATUREID.getValue()).append(",")
			.append(FullTextFields.NAME.getValue()).append(",")
			.append(FullTextFields.FULLY_QUALIFIED_NAME.getValue())
			.append(",").append(FullTextFields.ZIPCODE.getValue())
			.append(",")
			.append(FullTextFields.PLACETYPE.getValue())
			.append(",").append(
				FullTextFields.COUNTRYCODE.getValue()).append(
				",").append(
				FullTextFields.COUNTRYNAME.getValue())
			.toString();
	    
	} else if (outputStyle == Output.OutputStyle.MEDIUM){
	    return new StringBuffer(getFulltextFieldList(OutputStyle.SHORT,languageCode))
		.append(",").append(FullTextFields.LAT.getValue())
		.append(",").append(FullTextFields.LONG.getValue())
		.append(",").append(
			FullTextFields.FEATURECLASS.getValue()).append(
			",").append(
			FullTextFields.FEATURECODE.getValue()).append(
			",").append(
			FullTextFields.POPULATION.getValue()).append(
			",")
		.append(FullTextFields.NAMEASCII.getValue())
		.append(",").append(FullTextFields.TIMEZONE.getValue())
		.append(",").append(FullTextFields.ELEVATION.getValue())
		
		//country fields only
		.append(",").append(FullTextFields.CONTINENT.getValue())
		.append(",").append(FullTextFields.CURRENCY_CODE.getValue())
		.append(",").append(FullTextFields.CURRENCY_NAME.getValue())
		.append(",").append(FullTextFields.FIPS_CODE.getValue())
		.append(",").append(FullTextFields.ISOALPHA2_COUNTRY_CODE.getValue())
		.append(",").append(FullTextFields.ISOALPHA3_COUNTRY_CODE.getValue())
		.append(",").append(FullTextFields.POSTAL_CODE_MASK.getValue())
		.append(",").append(FullTextFields.POSTAL_CODE_REGEX.getValue())
		.append(",").append(FullTextFields.PHONE_PREFIX.getValue())
		.append(",").append(FullTextFields.SPOKEN_LANGUAGES.getValue())
		.append(",").append(FullTextFields.TLD.getValue())
		.append(",").append(FullTextFields.CAPITAL_NAME.getValue())
		.append(",").append(FullTextFields.AREA.getValue())
		
		//adm only
		.append(",").append(FullTextFields.LEVEL.getValue())
		
		.append(",").append(FullTextFields.GTOPO30.getValue())
		.append(",").append(
			FullTextFields.COUNTRY_FLAG_URL.getValue())
		.append(",").append(
			FullTextFields.GOOGLE_MAP_URL.getValue())
		.append(",").append(
			FullTextFields.YAHOO_MAP_URL.getValue())
		.toString();
	} else if (outputStyle == Output.OutputStyle.LONG){
	    StringBuffer sb = new StringBuffer(getFulltextFieldList(OutputStyle.MEDIUM,languageCode)).append(",").append(
			FullTextFields.ADM1NAME.getValue()).append(",").append(
			FullTextFields.ADM2NAME.getValue()).append(",").append(
			FullTextFields.ADM3NAME.getValue()).append(",").append(
			FullTextFields.ADM4NAME.getValue()).append(",").append(
			FullTextFields.ADM1CODE.getValue()).append(",").append(
			FullTextFields.ADM2CODE.getValue()).append(",").append(
			FullTextFields.ADM3CODE.getValue()).append(",").append(
			FullTextFields.ADM4CODE.getValue());
		return sb.toString();
	}else if (outputStyle == Output.OutputStyle.FULL){
	    if (languageCode != null) {
		    StringBuffer sb = new StringBuffer(getFulltextFieldList(OutputStyle.LONG,languageCode)).append(",").append(
			    FullTextFields.COUNTRYNAME.getValue()).append(
			    FullTextFields.ALTERNATE_NAME_SUFFIX.getValue())
			    .append(",").append(
				    FullTextFields.ADM1NAME.getValue()).append(
				    FullTextFields.ALTERNATE_NAME_SUFFIX
					    .getValue()).append(",").append(
				    FullTextFields.ADM2NAME.getValue()).append(
				    FullTextFields.ALTERNATE_NAME_SUFFIX
					    .getValue()).append(",").append(
				    FullTextFields.NAME.getValue()).append(
				    FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX
					    .getValue()).append(languageCode)
			    .append(",").append(FullTextFields.NAME.getValue())
			    .append(
				    FullTextFields.ALTERNATE_NAME_SUFFIX
					    .getValue()).append(",").append(
				    FullTextFields.COUNTRYNAME.getValue())
			    .append(
				    FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX
					    .getValue()).append(languageCode)
			    .append(",").append(
				    FullTextFields.ADM1NAME.getValue()).append(
				    FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX
					    .getValue()).append(languageCode)
			    .append(",").append(
				    FullTextFields.ADM2NAME.getValue()).append(
				    FullTextFields.ALTERNATE_NAME_DYNA_SUFFIX
					    .getValue()).append(languageCode);

		    return sb.toString();
		} else {
		    return "*,score";
		}
	} else { 
	    throw new RuntimeException(outputStyle+" is not implemented");
	}
    }
    
    public String getFulltextFieldList(Output output){
	return getFulltextFieldList(output.getStyle(),output.getLanguageCode());
	
    }

}
