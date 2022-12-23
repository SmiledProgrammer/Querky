package pl.szinton.querky.game.words;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static pl.szinton.querky.game.words.Constants.GUESS_COUNT;

@Getter
public class Player {

    private final String username;
    private boolean isPlaying;
    private int points;
    private final List<LetterMatch> letterMatches;
    private boolean hasGuessedCorrectly;

    public Player(String username) {
        this.username = username;
        this.isPlaying = false;
        this.points = 0;
        this.letterMatches = new ArrayList<>();
        this.hasGuessedCorrectly = false;
    }

    public void processPoints(int guessPlace, int playerCount) {
        if (guessPlace == -1) {
            return;
        }
        int guessTries = letterMatches.size();
        int basePoints = 0;
        if (guessTries <= GUESS_COUNT - 2) {
            basePoints = 100;
        } else if (guessTries <= GUESS_COUNT - 1) {
            basePoints = 80;
        } else if (guessTries == GUESS_COUNT) {
            basePoints = 40;
        }
        float multiplier = (playerCount - guessPlace) / (float) playerCount;
        int points = (int) (basePoints * multiplier);
        this.points += points;
    }

    public void makeGuess(LetterMatch match, boolean correct) {
        if (letterMatches.size() < GUESS_COUNT) {
            letterMatches.add(match);
            hasGuessedCorrectly = correct;
        }
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void resetPoints() {
        points = 0;
    }

    public void resetLetterMatches() {
        letterMatches.clear();
        hasGuessedCorrectly = false;
    }

    public boolean hasGuessedCorrectly() {
        return hasGuessedCorrectly;
    }

    public boolean hasGuessesLeft() {
        return letterMatches.size() < GUESS_COUNT;
    }
}
