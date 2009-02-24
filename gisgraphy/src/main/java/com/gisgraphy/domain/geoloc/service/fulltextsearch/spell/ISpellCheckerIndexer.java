package com.gisgraphy.domain.geoloc.service.fulltextsearch.spell;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public interface ISpellCheckerIndexer {

    /**
     * Re-index all the {@linkplain SpellCheckerDictionaryNames}
     * 
     * @throws a
     *                 {@link SpellCheckerException} if the spellChecker is not
     *                 alive or if an error occured
     * @return a map with dictioanry name as key and boolean as value. the
     *         boolean is equal to true if the index has succeed for the
     *         dictionary
     */
    public Map<String, Boolean> buildAllIndex();

    /**
     * re-index the dictionary for the specified spellchecker dictionary name
     * 
     * @param spellCheckerDictionaryName
     *                the spellChecker Dictionary to index / re-index
     * @throws a
     *                 {@link SpellCheckerException} if the spellChecker is not
     *                 alive or if an error occured
     */
    public boolean buildIndex(
	    SpellCheckerDictionaryNames spellCheckerDictionaryName);

}