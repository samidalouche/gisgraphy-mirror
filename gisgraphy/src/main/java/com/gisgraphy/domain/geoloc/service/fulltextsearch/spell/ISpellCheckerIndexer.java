package com.gisgraphy.domain.geoloc.service.fulltextsearch.spell;

/**
 * 
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public interface ISpellCheckerIndexer {

	/**
	 * Re-index all the {@linkplain SpellCheckerDictionaryNames}
	 * @throws a {@link SpellCheckerException} if the spellChecker is not alive or if an error occured
	 */
	public void buildAllIndex();

	/**
	 * re-index the dictionary for the specified spellchecker dictionary name 
	 * @param spellCheckerDictionaryName the spellChecker Dictionary to index / re-index
	 * @throws a {@link SpellCheckerException} if the spellChecker is not alive or if an error occured
	 */
	public void buildIndex(SpellCheckerDictionaryNames spellCheckerDictionaryName);

}