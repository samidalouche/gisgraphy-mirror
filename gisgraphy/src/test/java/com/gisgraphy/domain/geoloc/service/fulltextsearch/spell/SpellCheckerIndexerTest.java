package com.gisgraphy.domain.geoloc.service.fulltextsearch.spell;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.geoloc.service.fulltextsearch.IsolrClient;

public class SpellCheckerIndexerTest extends AbstractIntegrationHttpSolrTestCase{

    ISpellCheckerIndexer spellCheckerIndexer;
    
   

    @Test
    public void testBuildAllIndex() {
    }

    @Test
    public void testBuildIndexShouldReturnFalseIfSpellCheckIsNotEnabled() {
	boolean savedSpellCheckingValue = SpellCheckerConfig.enabled;
	try {
	    SpellCheckerConfig.enabled = false;
	    assertFalse(spellCheckerIndexer.buildIndex(SpellCheckerDictionaryNames.getDefault()));
	
	} finally {
	    SpellCheckerConfig.enabled = savedSpellCheckingValue;
	}
    }
    
    @Test
    public void testBuildIndexShouldReturnFalseIfAnErrorOccured() {
	IsolrClient solrClient = EasyMock.createMock(IsolrClient.class);
	EasyMock.expect(solrClient.getServer()).andReturn(null);
	EasyMock.replay(solrClient);
	SpellCheckerIndexer wrongSpellCheckerIndexer = new SpellCheckerIndexer();
	wrongSpellCheckerIndexer.setSolrClient(null);
	boolean savedSpellCheckingValue = SpellCheckerConfig.enabled;
	try {
	    assertFalse(wrongSpellCheckerIndexer.buildIndex(SpellCheckerDictionaryNames.getDefault()));
	}  finally {
	    SpellCheckerConfig.enabled = savedSpellCheckingValue;
	}
    }
    
    @Test
    public void testBuildIndexShouldReturnTrueIfOK() {
	boolean savedSpellCheckingValue = SpellCheckerConfig.enabled;
	try {
	    SpellCheckerConfig.enabled = true;
	    assertTrue(spellCheckerIndexer.buildIndex(SpellCheckerDictionaryNames.getDefault()));
	
	} finally {
	    SpellCheckerConfig.enabled = savedSpellCheckingValue;
	}
    }
    
    @Required
    public void setSpellCheckerIndexer(ISpellCheckerIndexer spellCheckerIndexer) {
        this.spellCheckerIndexer = spellCheckerIndexer;
    }

}
