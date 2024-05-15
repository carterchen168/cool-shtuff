package main;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
public class SynsetMatcher {
    public HashMap<Integer, ArrayList<String>> thing;
    public HashMap<String, ArrayList<Integer>> inverseThing;
    public int count;
    public int inverseCount;


    public SynsetMatcher(String synsetFile) {
        In synsetInput = new In(synsetFile);
        this.thing = new HashMap<>();
        this.inverseThing = new HashMap<>();
        this.count = 0;
        this.inverseCount = 0;

        while (!synsetInput.isEmpty()) {
            String nextLine = synsetInput.readLine();
            String[] splitLine = nextLine.split(",");
            int synsetId = Integer.parseInt(splitLine[0]);
            String[] synsetSplit = splitLine[1].split(" ");

            for (String synset : synsetSplit) {
                ArrayList<String> temp = new ArrayList<>();
                ArrayList<Integer> tempTwo = new ArrayList<>();

                if (thing.get(synsetId) == null) {
                    temp.add(synset);
                    thing.put(synsetId, temp);
                } else {
                    ArrayList<String> other = thing.get(synsetId);
                    other.add(synset);
                    thing.put(synsetId, other);
                }
                count += 1;

                if (inverseThing.get(synset) == null) {
                    tempTwo.add(synsetId);
                    inverseThing.put(synset, tempTwo);
                } else {
                    ArrayList<Integer> otherTwo = inverseThing.get(synset);
                    otherTwo.add(synsetId);
                    inverseThing.put(synset, otherTwo);
                }
                inverseCount += 1;
            }
        }
    }

    public ArrayList<String> getWords(int synsetId) {
        return thing.get(synsetId);
    }
    public ArrayList<Integer> getNumbers(String word) {
        return inverseThing.get(word);
    }
    public int numOfSynsets() {
        return count;
    }
    public int numOfWords() { return inverseCount;}


}
