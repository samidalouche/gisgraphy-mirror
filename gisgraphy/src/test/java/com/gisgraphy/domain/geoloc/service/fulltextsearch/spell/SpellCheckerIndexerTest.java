package com.gisgraphy.domain.geoloc.service.fulltextsearch.spell;

import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import com.gisgraphy.domain.geoloc.service.fulltextsearch.AbstractIntegrationHttpSolrTestCase;
import com.gisgraphy.domain.valueobject.Constants;

public class SpellCheckerIndexerTest extends
	AbstractIntegrationHttpSolrTestCase {

    private ISpellCheckerIndexer spellCheckerIndexer;

    @Test
    public void testBuildAllIndex() {
	Map<String,Boolean> results = spellCheckerIndexer.buildAllIndex();
	assertEquals(SpellCheckerDictionaryNames.values().length, results.size());
	for (String key : results.keySet()){
	    assertTrue(results.get(key).booleanValue());
	}
    }

    @Test
    public void testBuildIndexShouldReturnFalseIfSpellCheckIsNotEnabled() {
	boolean savedSpellCheckingValue = SpellCheckerConfig.enabled;
	try {
	    SpellCheckerConfig.enabled = false;
	    assertFalse(spellCheckerIndexer
		    .buildIndex(SpellCheckerDictionaryNames.getDefault()));

	} finally {
	    SpellCheckerConfig.enabled = savedSpellCheckingValue;
	}
    }

    @Test
    public void testBuildIndexShouldReturnFalseIfAnErrorOccured() {
	SpellCheckerIndexer wrongSpellCheckerIndexer = new SpellCheckerIndexer();
	wrongSpellCheckerIndexer.setSolrClient(null);
	boolean savedSpellCheckingValue = SpellCheckerConfig.enabled;
	try {
	    assertFalse(wrongSpellCheckerIndexer
		    .buildIndex(SpellCheckerDictionaryNames.getDefault()));
	} finally {
	    SpellCheckerConfig.enabled = savedSpellCheckingValue;
	}
    }

    @Test
    public void testBuildIndexShouldReturnTrueIfOK() {
	boolean savedSpellCheckingValue = SpellCheckerConfig.enabled;
	try {
	    SpellCheckerConfig.enabled = true;
	    assertTrue(spellCheckerIndexer
		    .buildIndex(SpellCheckerDictionaryNames.getDefault()));

	} finally {
	    SpellCheckerConfig.enabled = savedSpellCheckingValue;
	}
    }

    @Test
    public void testThatSpellCheckerShouldNotAcceptAnInexistingSpellCheckerDictionaryName() {
	SolrQuery solrQuery = new SolrQuery();
	solrQuery.setQueryType(Constants.SolrQueryType.spellcheck.toString());
	solrQuery.add(Constants.SPELLCHECKER_DICTIONARY_NAME_PARAMETER,
		"notExistingInSolrConfig.xml");
	solrQuery.add(Constants.SPELLCHECKER_BUILD_PARAMETER, "true");
	solrQuery.add(Constants.SPELLCHECKER_ENABLED_PARAMETER, "true");
	solrQuery.setQuery("spell");
	try {
	    QueryResponse response = solrClient.getServer().query(solrQuery);
	    if (response.getStatus() != 0) {
		fail("Status should not be 0 when the name of the dictionnary name is not defined in solrConfig.xml");
	    }
	    fail("dictionnary name that are not defined in solrConfig.xml should not be accepted");
	} catch (Exception e) {
	    System.err.println(e);
	}

    }

    @Required
    public void setSpellCheckerIndexer(ISpellCheckerIndexer spellCheckerIndexer) {
	this.spellCheckerIndexer = spellCheckerIndexer;
    }

}
