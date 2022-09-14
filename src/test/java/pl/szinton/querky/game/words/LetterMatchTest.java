package pl.szinton.querky.game.words;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LetterMatchTest {

    private static Stream<Arguments> matchingLettersCases() {
        return Stream.of(
                Arguments.of("POLSKA", "DRZEWU", new int[]{0, 0, 0, 0, 0, 0}),
                Arguments.of("POLSKA", "SPÓŁKA", new int[]{1, 1, 0, 0, 2, 2}),
                Arguments.of("POLSKA", "POPOPO", new int[]{2, 2, 0, 0, 0, 0}),
                Arguments.of("POLSKA", "OPOPSK", new int[]{1, 1, 0, 0, 1, 1}),
                Arguments.of("POLSKA", "POLSKA", new int[]{2, 2, 2, 2, 2, 2}),
                Arguments.of("POLSKA", "KRESKA", new int[]{0, 0, 0, 2, 2, 2}),
                Arguments.of("POLSKA", "PUDLOM", new int[]{2, 0, 0, 1, 1, 0}),
                Arguments.of("POLSKA", "POLKOM", new int[]{2, 2, 2, 1, 0, 0}),
                Arguments.of("ROKOKO", "OOOOOO", new int[]{0, 2, 0, 2, 0, 2}),
                Arguments.of("ROKOKO", "OPOPOP", new int[]{1, 0, 1, 0, 1, 0}),
                Arguments.of("ROKOKO", "ABABOO", new int[]{0, 0, 0, 0, 1, 2})
        );
    }

    @ParameterizedTest
    @MethodSource("matchingLettersCases")
    public void createFromWordsMatching_testDifferentMatchingCases(String targetWord, String guessedWord, int[] matches) {
        LetterMatch actualMatches = LetterMatch.createFromWordsMatching(targetWord, guessedWord);
        LetterMatch expectedMatches = new LetterMatch(matches);
        assertEquals(expectedMatches, actualMatches);
    }
}
