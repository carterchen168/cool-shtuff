package main;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphClass {
    HashMap<Integer, ArrayList<Integer>> synsetList;

    public GraphClass() {
        this.synsetList = new HashMap<>();
    }

    public void addSynset(int id, ArrayList<Integer> adjacencyList) {
        if (synsetList.get(id) == null) {
            synsetList.put(id, adjacencyList);
        } else {
            ArrayList<Integer> current = getIdList(id);
            current.addAll(adjacencyList);
            synsetList.put(id, current);
        }
    }
    public void removeSynset(int id) {
        synsetList.remove(id);
    }

    public ArrayList<Integer> getIdList(int id) {
        return synsetList.get(id);
    }
    public ArrayList<String> convertIdToWords(ArrayList<Integer> idList, SynsetMatcher synsetMatcher) {
        ArrayList<String> test = new ArrayList<>();
        for (Integer id : idList) {
            ArrayList<String> existing = synsetMatcher.getWords(id);
            test.addAll(existing);
        }
        return test;
    }




}
