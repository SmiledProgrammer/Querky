package pl.szinton.querky.game.words;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.szinton.querky.enums.WordsLetterMatch;
import pl.szinton.querky.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.szinton.querky.game.words.Constants.WORD_LENGTH;

@ToString
@EqualsAndHashCode
public class LetterMatch {

    private final int[] matches;

    public LetterMatch(WordsLetterMatch[] wordLetterMatches) {
        this.matches = new int[WORD_LENGTH];
        for (int i = 0; i < WORD_LENGTH; i++) {
            int matchCode = wordLetterMatches[i].getCode();
            this.matches[i] = matchCode;
        }
    }

    public LetterMatch(int[] wordLetterMatchesCodes) {
        this.matches = wordLetterMatchesCodes;
    }

    public static LetterMatch createFromWordsMatching(String targetWord, String guessedWord) {
        WordsLetterMatch[] matches = new WordsLetterMatch[WORD_LENGTH];
        List<Character> charsLeftToProcess = targetWord.chars()
                .mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));
        List<Boolean> indexLeftToProcess = ListUtils.createAndInitList(WORD_LENGTH, true);
        for (int i = 0; i < WORD_LENGTH; i++) {
            char targetWordLetter = targetWord.charAt(i);
            char guessedWordLetter = guessedWord.charAt(i);
            if (guessedWordLetter == targetWordLetter) {
                matches[i] = WordsLetterMatch.LETTER_CORRECT;
                charsLeftToProcess.remove(Character.valueOf(guessedWordLetter));
                indexLeftToProcess.set(i, false);
            }
        }
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (!indexLeftToProcess.get(i)) {
                continue;
            }
            char guessedWordLetter = guessedWord.charAt(i);
            if (charsLeftToProcess.contains(guessedWordLetter)) {
                matches[i] = WordsLetterMatch.LETTER_MISPLACED;
            } else {
                matches[i] = WordsLetterMatch.LETTER_NOT_IN_WORD;
            }
            charsLeftToProcess.remove(Character.valueOf(guessedWordLetter));
        }
        return new LetterMatch(matches);
    }

    public int[] getMatches() {
        return matches;
    }
}
