package pl.szinton.querky.game;

import pl.szinton.querky.enums.WordsGameState;

import java.util.HashMap;
import java.util.Map;

public class WordsBattlesGame {

    protected static final int GAME_START_COUNTDOWN_DURATION = 15;
    protected static final int GAME_DURATION = 15 * 60;
    protected static final int ROUND_START_COUNTDOWN_DURATION = 10;
    protected static final int ROUND_DURATION = 60;
    protected static final int POINTS_TO_WIN = 200;

    protected int gameTimeLeft;
    protected int roundTimeLeft;
    protected WordsGameState gameState;
    protected final Map<String, Player> players;

    public WordsBattlesGame() {
        this.gameTimeLeft = GAME_START_COUNTDOWN_DURATION + GAME_DURATION;
        this.roundTimeLeft = ROUND_START_COUNTDOWN_DURATION + ROUND_DURATION;
        this.gameState = WordsGameState.WAITING_FOR_PLAYERS;
        this.players = new HashMap<>();
    }

    public void addPlayer(String username) {
        Player newPlayer = new Player(true, 0, false);
        players.put(username, newPlayer);
    }

    protected record Player(boolean isPlaying, int points, boolean isReady) {
    }
}
