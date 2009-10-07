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
package com.gisgraphy.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide some usefull method to copute strinfg for autocompletion and fulltextsearch
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class StringHelper {

    protected static final Logger logger = LoggerFactory.getLogger(StringHelper.class);

/**
 * Process a string to apply filter as lucene and solr does :
 * 	- remove accent
 * 	- lowercase
 * 	- word delimiter ('-', '.'
 * @param originalString the string to process
 * @return the transformed String or null if the original String is null
 */
public static final String TransformStringForFulltextIndexation(String originalString){
       return originalString== null ? null:EncodingHelper.removeAccents(originalString)
    		   .toLowerCase().replace("-", " ").replace(".", " ")
    		   .replace("\"", " ").replace("'", " ");
       
   }

/**
 * Process a string to in order to be stored in a specific postgres 
 * field to allow the index usage for ilike (ilike(String%):
 * e.g : 'it s ok'=> 'it' 'it ' 'it s' 'it s ' 'it s o' 'it s ok' 
 * 
 * @param originalString the string to process
 * @return the transformed String or null if the original String is null
 */
public static final String TransformStringForIlikeIndexation(String originalString){
		if (originalString == null){
			return null;
		}
		//use hashset to remove duplicate
		String substring = null;
		String result = "";
		StringBuffer sb = new StringBuffer();
		try {
			for (int i=0; i< originalString.length();i++){
				for (int j=i+1; j <= originalString.length();j++){
						substring = originalString.substring(i,j);
						if (!substring.endsWith(" ") ){//we have alredy add the entry the last loop
							if (substring.startsWith(" ")){//need to trim?
								substring = substring.substring(1);
							}
							if (substring.length()>1){//only index string that have length >=2
								result = result+substring+"_";
							}
						}
				}
			}
		} catch (RuntimeException e) {
			return result;
		}
			return result;
   }
}
