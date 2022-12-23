package pl.szinton.querky.service.rest;

import org.springframework.stereotype.Service;
import pl.szinton.querky.utils.TextFileUtils;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class WordsDictionaryService {

    private static final String WORDS_TO_ENTER_PATH = "dictionaries/words-to-enter.txt";
    private static final String WORDS_TO_GUESS_PATH = "dictionaries/words-to-guess.txt";

    private final Set<String> wordsToEnter;
    private final List<String> wordsToGuess;
    private final Random random;

    public WordsDictionaryService() {
        wordsToEnter = TextFileUtils.loadFileIntoSet(WORDS_TO_ENTER_PATH);
        wordsToGuess = TextFileUtils.loadFileIntoList(WORDS_TO_GUESS_PATH);
        random = new Random();
    }

    public boolean doesNotContainWord(String guessEntryWord) {
        return !wordsToEnter.contains(guessEntryWord);
    }

    public String getRandomWordToGuess() {
        int randomIndex = getRandomWordToGuessIndex();
        return wordsToGuess.get(randomIndex);
    }

    public int getRandomWordToGuessIndex() {
        return random.nextInt(wordsToGuess.size());
    }

    public String getWordToGuessByIndex(int wordIndex) {
        try {
            return wordsToGuess.get(wordIndex);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
