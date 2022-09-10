package pl.szinton.querky.service.play;

import org.springframework.stereotype.Service;
import pl.szinton.querky.enums.WordsEvent;
import pl.szinton.querky.game.WordsBattlesGame;
import pl.szinton.querky.message.EventMessage;

import java.util.HashMap;
import java.util.Map;

@Service
public class WordsService {

    private static final int TABLES_COUNT = 1;

    private final Map<String, Integer> playersOnTables;
    private final Map<Integer, WordsBattlesGame> gameTables;

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
            return EventMessage.fromWordsEvent(WordsEvent.ERROR_NO_SUCH_TABLE,
                    "There is no table with this number.");
        }
        playersOnTables.put(username, tableNumber);
        gameTables.get(tableNumber).addPlayer(username);
        // TODO: create table data
        return EventMessage.fromWordsEvent(WordsEvent.TABLE_DATA, "1", "2", "3", "4", "5");
    }
}
