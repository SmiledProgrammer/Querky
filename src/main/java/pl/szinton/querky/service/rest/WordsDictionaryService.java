package pl.szinton.querky.service.rest;

import org.springframework.stereotype.Service;
import pl.szinton.querky.utils.TextFileUtils;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class WordsDictionaryService {

    private static final String WORDS_TO_ENTER_PATH = "static/dictionaries/words-to-enter.txt";
    private static final String WORDS_TO_GUESS_PATH = "static/dictionaries/words-to-guess.txt";

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

    public String getRandomWord() {
        int randomIndex = random.nextInt(wordsToGuess.size());
        return wordsToGuess.get(randomIndex);
    }
}
