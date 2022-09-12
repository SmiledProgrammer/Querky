package pl.szinton.querky.game.words;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.szinton.querky.enums.WordsLetterMatch;

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

    public int[] getMatches() {
        return matches;
    }
}
