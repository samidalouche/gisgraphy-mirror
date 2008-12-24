/**
 * ResultTransformerUtil.java
 * 
 * Mercer Inc.
 * JBossMHR
 * Copyright 2008 All Rights Reserved
 * @since 1.0 May 14, 2008
 * =============================================================================================
 * $Id: ResultTransformerUtil.java,v 1.1 2008/05/14 14:44:23 abhishekm Exp $
 * =============================================================================================
 */
package com.gisgraphy.hibernate.criterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.transform.AliasToBeanResultTransformer;

import com.gisgraphy.domain.valueobject.GisFeatureDistance;

/**
 * The Class ResultTransformerUtil.
 * 
 * @author Abhishek Mirge
 */
public class ResultTransformerUtil<T> {

	/**
	 * Transform to bean.
	 * See bug http://opensource.atlassian.com/projects/hibernate/browse/HHH-2463
	 * 
	 * @param aliasList the alias list
	 * @param resultList the result list
	 * 
	 * @return the list of GisFeatureDistance
	 */
	public static List<GisFeatureDistance> transformToGisFeatureDistance(String aliasList[], List<?> resultList) {
		List<GisFeatureDistance> transformList = new ArrayList<GisFeatureDistance>();
		if (aliasList != null && !resultList.isEmpty()) {
			AliasToBeanResultTransformer tr = new AliasToBeanResultTransformer(GisFeatureDistance.class);
			Iterator<?> it = resultList.iterator();
			Object[] obj;
			while (it.hasNext()) {
				obj = (Object[]) it.next();
				GisFeatureDistance gisFeatureDistance = (GisFeatureDistance)tr.transformTuple(obj, aliasList);
				gisFeatureDistance.updateFields();
				transformList.add(gisFeatureDistance);
			}
		}
		return transformList;
	}

}
