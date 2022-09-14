package pl.szinton.querky.game.words;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static pl.szinton.querky.game.words.Constants.GUESS_COUNT;

@Getter
class Player {

    private final String username;
    private boolean isPlaying;
    private int points;
    private final List<LetterMatch> letterMatches;

    public Player(String username) {
        this.username = username;
        this.isPlaying = false;
        this.points = 0;
        this.letterMatches = new ArrayList<>();
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void addPoints(int pointsGain) {
        this.points += pointsGain;
    }

    public void resetPoints() {
        points = 0;
    }

    public void resetLetterMatches() {
        letterMatches.clear();
    }

    public void makeGuess(LetterMatch match) {
        if (this.letterMatches.size() < GUESS_COUNT) {
            letterMatches.add(match);
        }
    }
}
