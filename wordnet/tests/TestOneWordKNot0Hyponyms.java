import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import main.AutograderBuddy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TestOneWordKNot0Hyponyms {
    public static final String WORDS_FILE = "data/ngrams/frequency-EECS.csv";
    public static final String BIG_WORDS_FILE = "data/ngrams/top_49887_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets-EECS.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms-EECS.txt";

    public static final String BIG_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String BIG_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    @Test
    public void testActKNot0() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("CS61A");

        NgordnetQuery nq = new NgordnetQuery(words, 2010, 2020, 4, NgordnetQueryType.HYPONYMS);
        String actual = studentHandler.handle(nq);
        String expected = "[CS170, CS61A, CS61B, CS61C]";
        assertThat(actual).isEqualTo(expected);
    }

    // TODO: Add more unit tests (including edge case tests) here.
    @Test
    public void testBiggerData() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                BIG_WORDS_FILE, TOTAL_COUNTS_FILE, BIG_SYNSET_FILE, BIG_HYPONYM_FILE);
        List<String> words = List.of("action");

        NgordnetQuery nq = new NgordnetQuery(words, 1960, 2020, 10, NgordnetQueryType.HYPONYMS);
        String actual = studentHandler.handle(nq);
        String expected = "[change, development, following, get, left, right, section, service, set, way]";
        assertThat(actual).isEqualTo(expected);
    }
}
