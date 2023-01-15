package pl.szinton.querky.game.words;

import pl.szinton.querky.enums.WordsGameState;
import pl.szinton.querky.game.IGame;

public interface IBattlesGame extends IGame {

    LetterMatch processPlayerGuess(String username, String guessWord);

    void checkIfAllPlayersFinished();

    void handleTimerTick();

    boolean playerDoesNotHaveGuessesLeft(String username);

    boolean playerHasAlreadyGuessedCurrentWord(String username);

    WordsGameState getGameState();

    int getGameStartTimeLeft();

    int getRoundsLeft();

    int getRoundTimeLeft();
}
