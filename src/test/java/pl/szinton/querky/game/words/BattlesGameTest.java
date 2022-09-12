package pl.szinton.querky.game.words;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import pl.szinton.querky.service.rest.WordsDictionaryService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BattlesGameTest {

    private static WordsDictionaryService dictionaryService;
    private static BattlesGame game;

    @BeforeAll
    static void init() {
        dictionaryService = Mockito.mock(WordsDictionaryService.class);
    }

    @BeforeEach
    void prepareForTest() {
        game = new BattlesGame(dictionaryService);
    }

    private void setCurrentWordMock(String word) {
        Mockito.when(dictionaryService.getRandomWord()).thenReturn(word);
    }

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
    public void checkMatchingLetters_testDifferentMatchingCases(String currentWord, String guessedWord, int[] matches) {
        setCurrentWordMock(currentWord);
        game.startNextRound();

        LetterMatch actualMatches = game.checkMatchingLetters(guessedWord);

        LetterMatch expectedMatches = new LetterMatch(matches);
        assertEquals(expectedMatches, actualMatches);
    }
}
