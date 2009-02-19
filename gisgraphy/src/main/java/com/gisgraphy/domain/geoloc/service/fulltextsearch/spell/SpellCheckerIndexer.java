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

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.FullTextFields;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient;
import com.gisgraphy.domain.valueobject.Constants;

/**
 * 
 * Solr implementation of {@link ISpellCheckerIndexer}
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public class SpellCheckerIndexer implements ISpellCheckerIndexer {
	
	 private IsolrClient solrClient;
	 
	
	

	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.ISpellCheckerIndexer#buildAllIndex()
	 */
	public void buildAllIndex(){
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.ISpellCheckerIndexer#buildIndex(com.gisgraphy.domain.geoloc.service.fulltextsearch.spell.SpellCheckerDictionaryNames)
	 */
	public void buildIndex(SpellCheckerDictionaryNames spellCheckerDictionaryName){
		if (!SpellCheckerConfig.isEnabled()){
			throw new SpellCheckerException("The spellchecker is not enabled");
		}
		
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQueryType(Constants.SolrQueryType.spellcheck.toString());
		solrQuery.add(Constants.SPELLCHECKER_DICTIONARY_NAME_PARAMETER, spellCheckerDictionaryName.toString());
		solrQuery.add(Constants.SPELLCHECKER_BUILD_PARAMETER, "true");
		solrQuery.add(Constants.SPELLCHECKER_ENABLED_PARAMETER, "true");
		try {
			solrClient.getServer().query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
			throw new SpellCheckerException(e);
		}
		
	}
	
	 /**
     * @param solrClient
     *                the solrClient to set
     */
    @Required
    public void setSolrClient(IsolrClient solrClient) {
	this.solrClient = solrClient;
    }
    
   
}
