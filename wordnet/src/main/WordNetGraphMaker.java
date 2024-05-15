package main;

import edu.princeton.cs.algs4.In;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class WordNetGraphMaker {
    SynsetMatcher synsetMatcher;
    GraphClass wordNetGraph;
    ArrayList<Integer> thing;
    int count;
    private String synsetFile;
    private String hyponymFile;
    HashMap<Integer, Set<Integer>> ancestorList;
    HashMap<String, Set<String>> ancestorListStrings;
    Set<String> masterAncestorSet;

    public WordNetGraphMaker(String synsetFile, String hyponymFile) {
        this.synsetFile = synsetFile;
        this.hyponymFile = hyponymFile;
        this.synsetMatcher = new SynsetMatcher(synsetFile);
        this.wordNetGraph = new GraphClass();
        this.ancestorList = new HashMap<>();
        this.ancestorListStrings = new HashMap<>();

        In hyponymInput = new In(hyponymFile);
        while (!hyponymInput.isEmpty()) {
            String nextLine = hyponymInput.readLine();
            String[] splitLine = nextLine.split(",");

            int hyponymId = Integer.parseInt(splitLine[0]);
            ArrayList<Integer> thisIntegerList = new ArrayList<>();
            Set<Integer> integerSet = new TreeSet<>();
            for (int i = 1; i < splitLine.length; i++) {
                int nextHyponym = Integer.parseInt(splitLine[i]);
                thisIntegerList.add(nextHyponym);
                if (ancestorList.containsKey(nextHyponym)) {
                    Set<Integer> previous = ancestorList.get(nextHyponym);
                    previous.add(hyponymId);
                    ancestorList.put(nextHyponym, previous);
                } else {
                    Set<Integer> newThing = new TreeSet<>();
                    newThing.add(hyponymId);
                    ancestorList.put(nextHyponym, newThing);
                }

                ArrayList<String> stringList = synsetMatcher.getWords(nextHyponym);
                ArrayList<String> originalList = synsetMatcher.getWords(hyponymId);
                iterateThrough(stringList, originalList);

            }
            wordNetGraph.addSynset(hyponymId, thisIntegerList);

        }
        /**
        for (String word : ancestorListStrings.keySet()) {
            System.out.println(word + " " + ancestorListStrings.get(word));
        }
        for (int integer : ancestorList.keySet()) {
            System.out.println(integer + " " + ancestorList.get(integer));
        } */
    }
    public void iterateThrough(ArrayList<String> bigList, ArrayList<String> parentWords) {
        Set<String> newThing = new TreeSet<>();
        if (bigList.isEmpty()) {
            return;
        }
        for (String big : bigList) {
            for (String parentWord : parentWords) {
                if (ancestorListStrings.get(big) == null) {
                    Set<String> insert = new TreeSet<>();
                    insert.add(parentWord);
                    ancestorListStrings.put(big, insert);
                } else {
                    Set<String> insert = ancestorListStrings.get(big);
                    insert.add(parentWord);
                    ancestorListStrings.put(big, insert);
                }
            }
        }
    }

    /** public ArrayList<Integer> getNumbers(ArrayList<Integer> stuff) {
        this.thing = stuff;
        this.count += 1;
    } */
    public Set<Integer> keepTrack(ArrayList<Integer> start, Set<Integer> newbie) {
        for (Integer startInt : start) {
            newbie.add(startInt);
            if (wordNetGraph.getIdList(startInt) != null) {
                keepTrack(wordNetGraph.getIdList(startInt), newbie);
            }
        }
        return newbie;
    }

    public Set<String> getHyponyms(String word) {
        Set<String> hyponymsSet = new TreeSet<>();
        ArrayList<Integer> wordIds = synsetMatcher.getNumbers(word);
        if (wordIds != null) {
            Set<Integer> thingy = keepTrack(wordIds, new TreeSet<>());
            System.out.println(thingy);
            for (Integer id : thingy) {
                ArrayList<String> wordRepresentations = synsetMatcher.getWords(id);
                hyponymsSet.addAll(wordRepresentations);
            }
        }
        return hyponymsSet;
    }
    public Set<String> getHyponyms(List<String> words) {
        Set<String> hyponymsSet = new TreeSet<>();

        for (String word : words) {
            System.out.println(word);
            Set<String> temporary = getHyponyms(word);
            if (hyponymsSet.isEmpty()) {
                hyponymsSet.addAll(temporary);
            } else {
                hyponymsSet.retainAll(temporary);
            }
        }

        return hyponymsSet;
    }

    /**
    public Set<Integer> keepTrack(ArrayList<Integer> start, Set<Integer> newbie) {
        for (Integer startInt : start) {
            newbie.add(startInt);
            if (wordNetGraph.getIdList(startInt) != null) {
                keepTrack(wordNetGraph.getIdList(startInt), newbie);
            }
        }
        return newbie;
    }
     */
    public Set<String> getAncestors(List<String> words) {
        List<Set<String>> setList = new ArrayList<>();

        if (words.isEmpty()) {
            return null;
        }
        for (String word : words) {
            ArrayList<Integer> intList = new ArrayList<>();
            ArrayList<Integer> wordInts = synsetMatcher.getNumbers(word);
            intList.addAll(wordInts);
            intList.addAll(getAncestorsHelper(wordInts));
            Set<String> otherThingy = new TreeSet<>(wordNetGraph.convertIdToWords(intList, synsetMatcher));
            setList.add(otherThingy);
        }

        Set<String> first = setList.getFirst();
        for (Set<String> thing : setList) {
            if (setList.size() == 1) {
                return thing;
            }
            if (first != thing) {
                first.retainAll(thing);
            }

        }
        return first;
    }

    public List<Integer> getAncestorsHelper(List<Integer> integers) {
        List<Integer> intList = new ArrayList<>();
        for (int integer : integers) {
            if (ancestorList.get(integer) != null) {
                Set<Integer> stuff = ancestorList.get(integer);
                intList.addAll(stuff);
                List<Integer> test = new ArrayList<>(ancestorList.get(integer));
                intList.addAll(getAncestorsHelper(test));
            }
        }
        return intList;
    }


    public Set<String> getAncestors(List<String> wordList, int startYear, int endYear, int k, String wordFile, String countFile) {
        NGramMap thingy = new NGramMap(wordFile, countFile);
        TreeMap<Double, String> stuff = new TreeMap<>();

        Set<String> test = getHyponyms(wordList);
        Set<String> finalThing = new TreeSet<>();
        for (String word : test) {
            TimeSeries current = thingy.countHistory(word, startYear, endYear);
            double totalCount = 0.0;
            for (double thing : current.data()) {
                totalCount += thing;
            }
            if (!stuff.containsValue(word)) {
                stuff.put(totalCount, word);
            }
        }


        int i = 0;
        for (double count : stuff.descendingKeySet()) {
            if (i >= k) {
                break;
            }
            finalThing.add(stuff.get(count));
            i += 1;
        }

        return finalThing;
    }


    public Set<String> getHyponyms(List<String> words, int startYear, int endYear, int k, String wordFile, String countFile) {
        NGramMap thingy = new NGramMap(wordFile, countFile);
        TreeMap<Double, String> stuff = new TreeMap<>();

        Set<String> test = getHyponyms(words);
        Set<String> finalThing = new TreeSet<>();
        for (String word : test) {
            TimeSeries current = thingy.countHistory(word, startYear, endYear);
            double totalCount = 0.0;
            for (double thing : current.data()) {
                totalCount += thing;
            }
            if (!stuff.containsValue(word)) {
                stuff.put(totalCount, word);
            }
        }


        int i = 0;
        for (double count : stuff.descendingKeySet()) {
            if (i >= k) {
                break;
            }
            finalThing.add(stuff.get(count));
            i += 1;
        }

        return finalThing;
        /**
        for (String word : test) {
            TimeSeries current = thingy.countHistory(word, startYear, endYear);
            double totalCount = 0.0;
            for (double thing : current.data()) {
                totalCount += thing;
            }
            if (stuff.get(totalCount) == null) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(word);
                stuff.put(totalCount, temp);
            } else {
                ArrayList<String> temp = stuff.get(totalCount);
                temp.add(word);
                stuff.put(totalCount, temp);
            }
        }

        Set<String> wordList = new TreeSet<>();

        List<Double> keyValues = new ArrayList<>(stuff.keySet());
        keyValues.sort(Collections.reverseOrder());
        int i = 0;
        for (double count : keyValues) {
            ArrayList<String> current = stuff.get(count);
            Collections.sort(current);

            for (String word : current) {
                if (i >= k) {
                    break;
                }
                wordList.add(word);
                i += 1;
            }
        }

        return wordList;
        */
    }
}
