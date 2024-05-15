package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap thing;
    public HistoryTextHandler(NGramMap map) {
        this.thing = map;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String response = "";
        for (String word : words) {
            response += word + ": " + thing.weightHistory(word, startYear, endYear).toString() + "\n";
        }
        return response;
    }
}
