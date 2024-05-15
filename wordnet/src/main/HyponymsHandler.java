package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class HyponymsHandler extends NgordnetQueryHandler {

    String words;
    String counts;
    String synsets;
    String hyponyms;
    NGramMap ngm;

    WordNetGraphMaker database;

    public HyponymsHandler(String wordFile, String countFile, String synsetFile, String hyponymFile) {
        this.words = wordFile;
        this.counts = countFile;
        this.synsets = synsetFile;
        this.hyponyms = hyponymFile;

        this.database = new WordNetGraphMaker(synsets, hyponyms);
    }

    public HyponymsHandler(NGramMap ngm, String synsetFile, String hyponymFile) {
        this.ngm = ngm;
        this.synsets = synsetFile;
        this.hyponyms = hyponymFile;

        this.database = new WordNetGraphMaker(synsets, hyponyms);
    }

    public String handle(NgordnetQuery q) {
        Set<String> wordList = new TreeSet<>();

        if (q.ngordnetQueryType() == NgordnetQueryType.HYPONYMS) {
            if (q.k() == 0) {
                wordList = database.getHyponyms(q.words());
            }
            if (q.k() > 0) {
                wordList = database.getHyponyms(q.words(), q.startYear(), q.endYear(), q.k(), words, counts);
            }
            if (wordList == null) {
                return null;
            }
        }
        if (q.ngordnetQueryType() == NgordnetQueryType.ANCESTORS) {
            if (q.k() == 0) {
                wordList = database.getAncestors(q.words());
            }
            if (q.k() > 0) {
                wordList = database.getAncestors(q.words(), q.startYear(), q.endYear(), q.k(), words, counts);
            }
        }

        return wordList.toString();
    }

}
