package main;

import browser.NgordnetServer;
import ngrams.NGramMap;
import org.slf4j.LoggerFactory;

public class Main {
    static {
        LoggerFactory.getLogger(Main.class).info("\033[1;38mChanging text color to white");
    }
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";
        String countFile = "./data/ngrams/total_counts.csv";
        String wordFile = "./data/ngrams/top_49887_words.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);

        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hyponyms", new HyponymsHandler(wordFile, countFile, synsetFile, hyponymFile));
        hns.register("commonancestors", new HyponymsHandler(wordFile, countFile, synsetFile, hyponymFile));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}