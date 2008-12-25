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
/**
 *
 */
package com.gisgraphy.domain.geoloc.service.fulltextsearch;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class DidYouMeanIndexer {
    /*
     * private static final String DEFAULT_FIELD = "name"; private static String
     * originalindexdir =
     * "/home/dmasclet/workspace/solr/example/solr/data/index/"; private static
     * String spelldir =
     * "/home/dmasclet/workspace/solr/example/solr/data/spell2/";
     * 
     * 
     * public void createSpellIndex(String field, Directory
     * originalIndexDirectory, Directory spellIndexDirectory) throws IOException {
     * 
     * IndexReader indexReader = null; try { indexReader =
     * IndexReader.open(originalIndexDirectory); Dictionary dictionary = new
     * LuceneDictionary(indexReader, field); SpellChecker spellChecker = new
     * SpellChecker(spellIndexDirectory);
     * spellChecker.indexDictionary(dictionary); } finally { if (indexReader !=
     * null) { indexReader.close(); } } }
     * 
     * public static void main(String[] args) throws IOException {
     * 
     * FSDirectory origDir = FSDirectory.getDirectory(originalindexdir);
     * FSDirectory spellDir = FSDirectory.getDirectory(spelldir); // Call
     * intern() on field to work around bug in LuceneDictionary //
     * indexer.createSpellIndex(DEFAULT_FIELD, origDir, spellDir);
     * 
     * SpellChecker spell = new SpellChecker(spellDir);
     * spell.indexDictionary(new LuceneDictionary(IndexReader.open(origDir),
     * DEFAULT_FIELD)); }
     */

}
