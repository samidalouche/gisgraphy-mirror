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
package com.gisgraphy.domain.geoloc.service.fulltextsearch.spell;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.FulltextQuery;

/**
 * 
 * SpecllChecker options
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class SpellCheckerConfig {

	/**
	 * Wether the spellchecker is active or not. default is true
	 */
	public static boolean enabled = true;

	/**
	 * Wether the default value is active or not, if we don't specify the value
	 * in the {@link FulltextQuery}.default is true
	 */
	public static boolean activeByDefault = true;

	/**
	 * the default spellchecker name, default value is the value returned by
	 * {@link SpellCheckerNames#getDefault()}
	 */
	public static SpellCheckerNames SpellcheckerName = SpellCheckerNames
			.getDefault();

	/**
	 * The number of suggestion spellchecked, default is 2
	 */
	public static int numberOfSuggestion = 2;

	/**
	 * suggest a query with all the terms with the best suggestion for each
	 * word. default is true
	 */
	public static boolean collateResults = true;

	/**
	 * @return the enabled
	 */
	public static boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public static void setEnabled(boolean enabled) {
		SpellCheckerConfig.enabled = enabled;
	}

	/**
	 * @return the activeByDefault
	 */
	public static boolean isActiveByDefault() {
		return activeByDefault;
	}

	/**
	 * @param activeByDefault
	 *            the activeByDefault to set
	 */
	public static void setActiveByDefault(boolean activeByDefault) {
		SpellCheckerConfig.activeByDefault = activeByDefault;
	}

	/**
	 * @return the spellcheckerName
	 */
	public static SpellCheckerNames getSpellcheckerName() {
		return SpellcheckerName;
	}

	/**
	 * @param spellcheckerName
	 *            the spellcheckerName to set
	 */
	public static void setSpellcheckerName(SpellCheckerNames spellcheckerName) {
		SpellcheckerName = spellcheckerName;
	}

	/**
	 * @return the numberOfSuggestion
	 */
	public static int getNumberOfSuggestion() {
		return numberOfSuggestion;
	}

	/**
	 * @param numberOfSuggestion
	 *            the numberOfSuggestion to set
	 */
	public static void setNumberOfSuggestion(int numberOfSuggestion) {
		SpellCheckerConfig.numberOfSuggestion = numberOfSuggestion;
	}

	/**
	 * @return the collateResults
	 */
	public static boolean isCollateResults() {
		return collateResults;
	}

	/**
	 * @param collateResults
	 *            the collateResults to set
	 */
	public static void setCollateResults(boolean collateResults) {
		SpellCheckerConfig.collateResults = collateResults;
	}

}
