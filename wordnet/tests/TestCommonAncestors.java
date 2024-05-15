import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import main.AutograderBuddy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TestCommonAncestors {
    public static final String WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String LARGE_WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String HUGE_WORDS_FILE = "data/ngrams/top_49887_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    /** This is an example from the spec for a common-ancestors query on the word "adjustment".
     * You should add more tests for the other spec examples! */
    @Test
    public void testSpecAdjustment() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("adjustment");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[adjustment, alteration, event, happening, modification, natural_event, occurrence, occurrent]";
        assertThat(actual).isEqualTo(expected);
    }

    // TODO: Add more unit tests (including edge case tests) here.
    @Test
    public void testMultipleContexts() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("change");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[act, action, alteration, change, event, happening, human_action, human_activity, modification, natural_event, occurrence, occurrent]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testBigFile() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("freeway");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[artefact, artifact, entity, expressway, freeway, highway, main_road, motorway, object, physical_entity, physical_object, pike, road, route, state_highway, superhighway, throughway, thruway, unit, way, whole]";
        assertThat(actual).isEqualTo(expected);
    }
    // TODO: Create similar unit test files for the k != 0 cases.

    @Test
    public void testSimpleK() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("oblong");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[artefact, artifact, entity, expressway, freeway, highway, main_road, motorway, object, physical_entity, physical_object, pike, road, route, state_highway, superhighway, throughway, thruway, unit, way, whole]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testSimpleMultiWords() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("change", "adjustment");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[alteration, event, happening, modification, natural_event, occurrence, occurrent]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testMultiHyponyms() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("action");

        NgordnetQuery nq = new NgordnetQuery(words, 1980, 2020, 5, NgordnetQueryType.HYPONYMS);
        String actual = studentHandler.handle(nq);
        String expected = "[development, get, right, set, way]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testMultiAncestors() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("action");

        NgordnetQuery nq = new NgordnetQuery(words, 1980, 2020, 5, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[action, process, state, whole, work]";
        assertThat(actual).isEqualTo(expected);
    }
}
