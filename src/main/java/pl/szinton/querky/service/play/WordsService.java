package pl.szinton.querky.service.play;

import org.springframework.stereotype.Service;
import pl.szinton.querky.enums.WordsEvent;
import pl.szinton.querky.game.WordsBattlesGame;
import pl.szinton.querky.message.EventMessage;

import java.util.HashMap;
import java.util.Map;

@Service
public class WordsService {

    protected static final int TABLES_COUNT = 1;

    protected final Map<String, Integer> playersOnTables;
    protected final Map<Integer, WordsBattlesGame> gameTables;

    public WordsService() {
        this.playersOnTables = new HashMap<>();
        this.gameTables = new HashMap<>();
        for (int i = 0; i < TABLES_COUNT; i++) {
            WordsBattlesGame newGame = new WordsBattlesGame();
            this.gameTables.put(100 + i, newGame);
        }
    }

    public int getPlayersTableNumber(String username) {
        return playersOnTables.get(username);
    }

    public EventMessage joinTable(String username, int tableNumber) {
        if (!gameTables.containsKey(tableNumber)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_NO_SUCH_TABLE);
        }
        WordsBattlesGame table = gameTables.get(tableNumber);
        if (table.hasPlayer(username)) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_PLAYER_ALREADY_ON_TABLE);
        }
        if (table.hasReachedPlayersLimit()) {
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_TABLE_FULL);
        }
        table.addPlayer(username);
        playersOnTables.put(username, tableNumber);
        return EventMessage.fromWordsEvent(WordsEvent.TABLE_DATA, tableNumber, table.getGameStartTimeLeft(),
                table.getRoundsLeft(), table.getRoundTimeLeft(), table.getGameState(), table.getPlayersList());
    }

    public EventMessage broadcastJoinedTable(String username) {
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_JOINED_TABLE, username);
    }

    public boolean leaveTable(String username) {
        int tableNumber = getPlayersTableNumber(username);
        WordsBattlesGame table = gameTables.get(tableNumber);
        if (!table.hasPlayer(username)) {
            return false;
        }
        table.removePlayer(username);
        playersOnTables.remove(username);
        return true;
    }

    public EventMessage broadcastLeftTable(String username) {
        return EventMessage.fromWordsEvent(WordsEvent.PLAYER_LEFT_TABLE, username);
    }
}
