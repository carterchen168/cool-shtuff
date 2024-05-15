package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;


/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    TimeSeries counts;
    ArrayList<TimeSeries> wordCounts;


    TreeMap<String, ArrayList<TimeSeries>> test;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordsInput = new In(wordsFilename);
        In countsInput = new In(countsFilename);
        this.counts = new TimeSeries();
        this.wordCounts = new ArrayList<TimeSeries>();
        this.test = new TreeMap<>();
        TimeSeries placeholder;
        String word = "";
        String comparator;

        while (!wordsInput.isEmpty()) {
            comparator = word;
            placeholder = new TimeSeries();
            String nextLine = wordsInput.readLine();
            String[] splitLine = nextLine.split("\t");
            word = splitLine[0];
            int year = Integer.parseInt(splitLine[1]);
            double numWords = Double.parseDouble(splitLine[2]);

            if (!(comparator.equals(word))) {
                wordCounts = new ArrayList<TimeSeries>();
            }
            placeholder.put(year, numWords);
            wordCounts.add(placeholder);
            test.put(word, wordCounts);
        }

        while (!countsInput.isEmpty()) {
            String nextLine = countsInput.readLine();
            String[] splitLine = nextLine.split(",");
            int year = Integer.parseInt(splitLine[0]);
            double numWords = Double.parseDouble(splitLine[1]);
            counts.put(year, numWords);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries history = new TimeSeries();
        if (this.test.containsKey(word)) {
            ArrayList<TimeSeries> placeholder = this.test.get(word);
            for (TimeSeries timePeriod : placeholder) {
                for (Map.Entry<Integer, Double> entry : timePeriod.entrySet()) {
                    int year = entry.getKey();
                    if (year >= startYear && year <= endYear) {
                        history.put(year, timePeriod.get(year));
                    }
                }
            }
        }
        return history;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        /** System.out.println(test.keySet());
        for (String key : test.keySet()) {
            System.out.println(test.get(key));
        } */
        TimeSeries history = new TimeSeries();
        if (this.test.containsKey(word)) {
            ArrayList<TimeSeries> placeholder = this.test.get(word);
            for (TimeSeries timePeriod : placeholder) {
                for (Map.Entry<Integer, Double> entry : timePeriod.entrySet()) {
                    int year = entry.getKey();
                    history.put(year, timePeriod.get(year));
                }
            }
        }
        return history;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return counts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries wordWeightedHistory = new TimeSeries();
        if (this.test.containsKey(word)) {
            ArrayList<TimeSeries> history = this.test.get(word);
            for (TimeSeries timePeriod : history) {
                int year = timePeriod.firstKey();
                if (year >= startYear && year <= endYear) {
                    TimeSeries divided = timePeriod.dividedBy(counts);
                    wordWeightedHistory.put(year, divided.get(year));
                }
            }
        }
        return wordWeightedHistory;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries wordWeightedHistory = new TimeSeries();
        if (this.test.containsKey(word)) {
            ArrayList<TimeSeries> history = this.test.get(word);
            for (TimeSeries timePeriod : history) {
                int year = timePeriod.firstKey();
                TimeSeries divided = timePeriod.dividedBy(counts);
                wordWeightedHistory.put(year, divided.get(year));
            }
        }
        return wordWeightedHistory;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries summedHistory = new TimeSeries();
        for (String word : words) {
            summedHistory = summedHistory.plus(weightHistory(word, startYear, endYear));
        }

        return summedHistory;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries summedHistory = new TimeSeries();
        for (String word : words) {
            summedHistory = summedHistory.plus(weightHistory(word));
        }

        return summedHistory;
    }

}
