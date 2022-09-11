package pl.szinton.querky.game;

import lombok.Getter;
import pl.szinton.querky.enums.WordsGameState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class WordsBattlesGame {

    protected static final int PLAYERS_LIMIT = 4;
    protected static final int GAME_START_COUNTDOWN_DURATION = 15;
    protected static final int ROUND_COUNT = 20;
    protected static final int ROUND_START_COUNTDOWN_DURATION = 10;
    protected static final int ROUND_DURATION = 60;

    protected int gameStartTimeLeft;
    protected int roundsLeft;
    protected int roundTimeLeft;
    protected WordsGameState gameState;
    protected final Map<String, Player> players;

    public WordsBattlesGame() {
        this.gameStartTimeLeft = GAME_START_COUNTDOWN_DURATION;
        this.roundsLeft = ROUND_COUNT;
        this.roundTimeLeft = ROUND_START_COUNTDOWN_DURATION + ROUND_DURATION;
        this.gameState = WordsGameState.WAITING_FOR_PLAYERS;
        this.players = new HashMap<>();
    }

    public void addPlayer(String username) {
        if (!hasPlayer(username) && !hasReachedPlayersLimit()) {
            Player newPlayer = new Player(username, true, 0);
            players.put(username, newPlayer);
        }
    }

    public void removePlayer(String username) {
        players.remove(username);
    }

    public boolean hasPlayer(String username) {
        return players.containsKey(username);
    }

    public boolean hasReachedPlayersLimit() {
        return players.size() == PLAYERS_LIMIT;
    }

    public List<Player> getPlayersList() {
        return new ArrayList<>(players.values());
    }

    protected record Player(String username, boolean isPlaying, int points) {
    }
}
